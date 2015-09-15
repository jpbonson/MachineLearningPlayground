package GASummarizer;

import java.io.*;
import java.util.ArrayList;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class PreProcessedDoc
{
    private ArrayList<Sentence> sentences;
    private Sentence title;
    private DefaultDirectedWeightedGraph<Sentence, DefaultWeightedEdge> graph;
    
    public PreProcessedDoc(FileToSentencesArray fileArray, int id)
    {
        File file = new File("graph"+id+".gr");
        if(file.exists())
        {
            try
            {
               FileInputStream fileIn = new FileInputStream("graph"+id+".gr");
               ObjectInputStream in = new ObjectInputStream(fileIn);
               graph = (DefaultDirectedWeightedGraph<Sentence, DefaultWeightedEdge>) in.readObject();
               in.close();
               fileIn.close();
            }catch(IOException | ClassNotFoundException i)
            {
               System.out.println(i);
            }
            sentences = new ArrayList<>(graph.vertexSet());
            title = sentences.remove(0);
        }
        else
        {
            this.title = new Sentence(fileArray.title, null, -1);
            this.sentences = fileArray.sentences;
            initializeGraph(id);
        }
        
        //System.out.println(title);
        //System.out.println(sentences);
        //System.out.println(graph.getEdge(title, sentences.get(1)));
        //System.out.println(graph.getEdgeWeight(graph.getEdge(title, sentences.get(1))));
    }
    
    public DefaultDirectedWeightedGraph<Sentence, DefaultWeightedEdge> getGraph()
    {
        return graph;
    }
    
    private void initializeGraph(int id)
    {
        graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        
        graph.addVertex(title);
        DefaultWeightedEdge e1;
        DefaultWeightedEdge e2;
        double weight1, weight2;
        for(int i = 0; i < sentences.size(); i++)
        {
            graph.addVertex(sentences.get(i));
            e1 = graph.addEdge(title, sentences.get(i));
            weight1 = computeSentencesSimilarityWithTitle(sentences.get(i));
            //System.out.println(weight1);
            graph.setEdgeWeight(e1, weight1);
            for(int j = 0; j < i; j++)
            {
                e2 = graph.addEdge(sentences.get(j), sentences.get(i));
                weight2 = computeSentencesSimilarity(sentences.get(j), sentences.get(i));
                graph.setEdgeWeight(e2, weight2);
                //System.out.println(weight2);
            }
        }
        
          try
          {
             FileOutputStream fileOut = new FileOutputStream("graph"+id+".gr");
             ObjectOutputStream out =
                                new ObjectOutputStream(fileOut);
             out.writeObject(graph);
             out.close();
             fileOut.close();
          }catch(IOException i)
          {
              System.out.println(i);
          }
    }
    
    private double computeSentencesSimilarityWithTitle(Sentence sj)
    {
        ArrayList<String> terms = mergeTerms(sj, title);
        double sum1 = 0;
        double sum2 = 0;
        double sum3 = 0;
        double wij, wiq;
        for(int i = 0; i < terms.size(); i++)
        {
            wij = computeTermWeight(sj, terms.get(i));
            wiq = computeTermWeightAtTitle(terms.get(i));

            sum1 += wij*wiq;
            sum2 += wij*wij;
            sum3 += wiq*wiq;
        }
        
        sum2 = Math.sqrt(sum2);
        sum3 = Math.sqrt(sum3);
        double temp = sum2*sum3;
        double result = sum1/temp;
        
        return result;
    }
    
    private double computeSentencesSimilarity(Sentence sm, Sentence sn)
    {
        ArrayList<String> terms = mergeTerms(sm, sn);
        double sum1 = 0;
        double sum2 = 0;
        double sum3 = 0;
        double wim, win;
        for(int i = 0; i < terms.size(); i++)
        {
            wim = computeTermWeight(sm, terms.get(i));
            win = computeTermWeight(sn, terms.get(i));
            
            sum1 += wim*win;
            sum2 += wim*wim;
            sum3 += win*win;
        }
        
        sum2 = Math.sqrt(sum2);
        sum3 = Math.sqrt(sum3);
        
        double temp = sum2*sum3;
        double result = sum1/temp;
        
        return result;
    }
    
    private ArrayList<String> mergeTerms(Sentence sm, Sentence sn)
    {
        ArrayList<String> terms = sm.getTerms();
        for(String t: sn.getTerms())
            if(!terms.contains(t))
                terms.add(t);
        return terms;
    }
    
    private double computeTermWeightAtTitle(String term)
    {
        Sentence sentence = title;
        double f = computeTermFrequency(sentence, term);
        double max = getMaxFrequencyTerm(sentence);
        double isf = computeISF(term);
        double temp = ((0.5*f)/max);
        temp += 0.5;
        temp *= isf;
        return temp;
    }
    
    // http://en.wikipedia.org/wiki/Tf%E2%80%93idf
    private double computeTermWeight(Sentence sentence, String term)
    {
        if(!sentence.getTerms().contains(term))
            return 0;
        double result = computeTF(sentence, term)*computeISF(term);
        return result;
    }
    
    private double computeTF(Sentence sentence, String term)
    {
        double f = computeTermFrequency(sentence, term);
        double max = getMaxFrequencyTerm(sentence);
        return f/max;
    }
    
    private double getMaxFrequencyTerm(Sentence sentence)
    {
        double max = 0;
        double temp;
        for(String t: sentence.getTerms())
        {
            temp = computeTermFrequency(sentence, t);
            if(temp > max)
            {
                max = temp;
            }
        }
        return max;
    }
    
    private double computeTermFrequency(Sentence sentence, String term)
    {
        double f = 0;
        for(String s: sentence.getTerms())
            if(s.equals(term))
                f++;
        return f;
    }
    
    private double computeISF(String term)
    {
        double N = sentences.size();
        double ni = 0;
        for(Sentence s: sentences)
            if(s.content.contains(term))
                ni++;

        double r = Math.log10(N/(ni+1));
        return r;
    }
    
    public Sentence getTitle()
    {
        return title;
    }
    
    public ArrayList<Sentence> getSentences()
    {
        return sentences;
    }
    
    @Override
    public String toString()
    {
        String str = "";
        str += "Title: "+title+"\n";
        for(Sentence s: sentences)
            str += s+"\n";
        return str;
    }
}
