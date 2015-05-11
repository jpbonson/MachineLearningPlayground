package GASummarizer;

import java.util.ArrayList;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class FitnessFunction
{
    public ArrayList<Sentence> sentences;
    public Sentence title;
    private DefaultDirectedWeightedGraph<Sentence, DefaultWeightedEdge> graph;
    public static double alfa = 0.95;
    public static double beta = 1;
    public static double gama = 0.9;
    public static double delta = 1;
    public static double epsilon = 1.3;
    
    public FitnessFunction(PreProcessedDoc doc)
    {
        this.title = doc.getTitle();
        this.sentences = doc.getSentences();
        this.graph = doc.getGraph();
    }
    
    public int getSentencesQuantity()
    {
        return sentences.size();
    }
    
    private boolean argTRF = true;
    private boolean argCF =  true;
    private boolean argRF =  true;
    private boolean argSF =  true;
    private boolean argKWF =  true;
    // summary length (number of sentences in summary) is supposed to be S
    // number of sentences in the main document is supposed to be N
    public double calculateFitness(Summary summary, ArrayList<Summary> population)
    {
        double trf, cf, rf, sf, kwf;
        
        if(argTRF)
            trf = calculateTRF(summary, population);
        else
            trf = 1;
        
        if(summary.getFitness() == -1)
        {
            if(argCF)
                cf = calculateCF(summary);
            else
                cf = 1;
        }
        else
            cf = summary.param2;
        
        if(argRF)
            rf = calculateRF(summary, population);
        else
            rf = 1;
        
        if(summary.getFitness() == -1)
        {
            if(argSF)
                sf = calculateSF(summary);
            else
                sf = 1;
        }
        else
            sf = summary.param4;
        
        if(summary.getFitness() == -1)
        {
            if(argKWF)
                kwf = calculateKWF(summary);
            else
                kwf = 1;
        }
        else
            kwf = summary.param5;

        double fitness = ((alfa*trf)+(beta*cf)+(gama*rf)+(delta*sf)+(epsilon*kwf))/(alfa+beta+gama+delta+epsilon);
        summary.param1 = trf;
        summary.param2 = cf;
        summary.param3 = rf;
        summary.param4 = sf;
        summary.param5 = kwf;

        //System.out.println("fitness: "+fitness);
        //double fitness = Math.random()*100;
        return fitness;
    }
    
    private double calculateKWF(Summary summary)
    {
        double sum = 0;

        for(int i = 0; i < summary.actives.length; i++)
        {
            if(summary.actives[i])
            {
                sum+= calculateKWFperSentence(sentences.get(i));
            }
        }

        return sum/(double)Summary.SENTENCE_EXTRACTION;
    }
    
    private double calculateKWFperSentence(Sentence sentence)
    {
        double initialScore = 0.5;
        double small = 0.1;
        double big = 0.2;
        double bigger = 0.3;

        if(sentence.getTerms().contains("important"))
            initialScore+=small;
        if(sentence.getTerms().contains("definitely"))
            initialScore+=small;
        if(sentence.getTerms().contains("in particular"))
            initialScore+=small;
        if(sentence.getTerms().contains("following"))
            initialScore+=small;
        if(sentence.getTerms().contains("therefore"))
            initialScore+=small;
        if(sentence.getTerms().contains("based on"))
            initialScore+=small;
        if(sentence.content.contains("We are going to"))
            initialScore+=small;
        if(sentence.content.contains("evaluation metrics"))
            initialScore+=small;
        if(sentence.getTerms().contains("overall"))
            initialScore+=small;
        if(sentence.getTerms().contains("furthermore"))
            initialScore+=small;
        if(sentence.getTerms().contains("firstly"))
            initialScore+=small;
        if(sentence.getTerms().contains("secondly"))
            initialScore+=small;
        if(sentence.getTerms().contains("initially"))
            initialScore+=small;
        

        if(sentence.getTerms().contains("unclear"))
            initialScore-=small;
        if(sentence.getTerms().contains("often"))
            initialScore-=small;
        if(sentence.getTerms().contains("perhaps"))
            initialScore-=small;
        if(sentence.getTerms().contains("maybe"))
            initialScore-=small;
        if(sentence.getTerms().contains("may"))
            initialScore-=small;
        if(sentence.getTerms().contains("possible"))
            initialScore-=small;
        if(sentence.getTerms().contains("easily"))
            initialScore-=small;
        if(sentence.getTerms().contains("briefly"))
            initialScore-=small;
        if(sentence.getTerms().contains("initial"))
            initialScore-=small;
        if(sentence.getTerms().contains("would"))
            initialScore-=small;
        if(sentence.getTerms().contains("previous"))
            initialScore-=small;
        if(sentence.getTerms().contains("especially"))
            initialScore-=small;
        if(sentence.getTerms().contains("obviously"))
            initialScore-=small;
        if(sentence.getTerms().contains("obvious"))
            initialScore-=small;
        

        if(sentence.content.contains("In our proposal"))
            initialScore+=big;
        if(sentence.content.contains("It means that"))
            initialScore+=big;
        else if(sentence.content.contains("it means that"))
            initialScore+=big;
        if(sentence.content.contains("exhibited in the experiments"))
            initialScore+=big;
        if(sentence.content.contains("The objective of this paper"))
            initialScore+=big;
        if(sentence.content.contains("It is worth remarking"))
            initialScore+=big;
        if(sentence.content.contains("The purpose of this"))
            initialScore+=big;
        if(sentence.content.contains("has shown that"))
            initialScore+=big;
        if(sentence.content.contains("We employ"))
            initialScore+=big;
        else if(sentence.content.contains("we employ"))
            initialScore+=big;
        if(sentence.content.contains("We utilize"))
            initialScore+=big;
        else if(sentence.content.contains("we utilize"))
            initialScore+=big;
        if(sentence.content.contains("We propose"))
            initialScore+=big;
        else if(sentence.content.contains("we propose"))
            initialScore+=big;
        if(sentence.content.contains("It is clear"))
            initialScore+=big;
        else if(sentence.content.contains("it is clear"))
            initialScore+=big;
        if(sentence.content.contains("We focus on"))
            initialScore+=big;
        else if(sentence.content.contains("we focus on"))
            initialScore+=big;
        if(sentence.content.contains("In our architecture"))
            initialScore+=big;
        if(sentence.content.contains("In this paper"))
            initialScore+=bigger;
        if(sentence.content.contains("This paper presents"))
            initialScore+=bigger;
        if(sentence.content.contains("This paper presented"))
            initialScore+=big;
        if(sentence.content.contains("The system demonstrated to"))
            initialScore+=big;
        if(sentence.content.contains("To evaluate the performance"))
            initialScore+=big;
        else if(sentence.content.contains("to evaluate the performance"))
            initialScore+=big;
        if(sentence.content.contains("Our approach results"))
            initialScore+=bigger;
        if(sentence.getTerms().contains("baseline"))
            initialScore+=big;
        if(sentence.getTerms().contains("outperform"))
            initialScore+=big;

        if(sentence.content.contains("for example"))
            initialScore-=bigger;
        else if(sentence.content.contains("For example"))
            initialScore-=bigger;
        else if(sentence.getTerms().contains("example"))
            initialScore-=bigger;
        if(sentence.content.contains("for instance"))
            initialScore-=bigger;
        else if(sentence.content.contains("For instance"))
            initialScore-=bigger;
        if(sentence.content.contains("for instance"))
            initialScore-=bigger;
        if(sentence.content.contains("for lack of space"))
            initialScore-=bigger;
        if(sentence.content.contains("This paper is organized"))
            initialScore-=bigger;
        if(sentence.getTerms().contains("recently"))
            initialScore-=big;
        if(sentence.getTerms().contains("specifically"))
            initialScore-=big;
        if(sentence.getTerms().contains("detail"))
            initialScore-=big;
        if(sentence.getTerms().contains("details"))
            initialScore-=big;
        if(sentence.content.contains("The remainder of"))
            initialScore-=bigger;
        if(sentence.content.contains("future work"))
            initialScore-=bigger;
        else if(sentence.content.contains("Future work"))
            initialScore-=bigger;
        if(sentence.content.contains("In the rest of this"))
            initialScore-=bigger;
        if(sentence.content.contains("The rest of this"))
            initialScore-=bigger;
        if(sentence.content.contains("earlier work"))
            initialScore-=bigger;
        else if(sentence.content.contains("Earlier work"))
            initialScore-=bigger;
        if(sentence.content.contains("In this section"))
            initialScore-=bigger;
        else if(sentence.content.contains("in this section"))
            initialScore-=bigger;
                    
        
        if(initialScore < 0)
            initialScore = 0;
        if(initialScore > 1)
            initialScore = 1;
        return initialScore;
    }
    
    private double calculateSF(Summary summary) // Section Factor
    {
        double sum = 0;
        for(int i = 0; i < summary.actives.length; i++)
            if(summary.actives[i])
            {
                if(sentences.get(i).section == Sentence.SECTION.INTRODUCTION)
                    sum += 0.8;
                else if(sentences.get(i).section == Sentence.SECTION.BACKGROUND)
                    sum += 0.85;
                else if(sentences.get(i).section == Sentence.SECTION.PROPOSAL)
                    sum += 1;
                else if(sentences.get(i).section == Sentence.SECTION.RESULTS)
                    sum += 0.9;
                else if(sentences.get(i).section == Sentence.SECTION.CONCLUSION)
                    sum += 0.8;
            }
        return sum/(double)Summary.SENTENCE_EXTRACTION;
    }
    
    // simplified from the original algorithm (lower performance)
    private double calculateRF(Summary summary, ArrayList<Summary> population)
    {
        double max = 0;
        double temp;
        for(Summary s: population)
        {
            temp = calculateR(s);
            if(temp > max)
                max = temp;
        }
        
        double r = calculateR(summary);
        
        if(r > max)
            max = r;
        return r/max;
    }
    
    private double calculateR(Summary m)
    {
        double sum = 0;
        for(int i = 0; i < m.actives.length-1; i++)
        {
            if(m.actives[i])
            {
                sum += graph.getEdgeWeight(graph.getEdge(sentences.get(i), sentences.get(i+1)));
            }
        }
        return sum;
    }
    
    private double calculateCF(Summary m)
    {
        double M = 0;
        double temp;
        for(int i = 0; i < m.actives.length; i++)
        {
            for(int j = i+1; j < sentences.size(); j++)
            {
                temp = graph.getEdgeWeight(graph.getEdge(sentences.get(i), sentences.get(j)));
                if(temp > M)
                {
                    M = temp;
                }
            }
        }
        double C = calculateC(m);
        double temp1 = Math.log10(C*9+1);
        double temp2 = Math.log10(M*9+1);
        return temp1/temp2;
    }
    
    // Let C be the average of similarities of all sentences in a summary
    private double calculateC(Summary m)
    {
        double sum = 0;
                
        for(int i = 0; i < m.actives.length; i++)
        {
            if(m.actives[i])
            {
                for(int j = i+1; j < m.actives.length; j++)
                {
                    if(m.actives[j])
                    {
                        sum += graph.getEdgeWeight(graph.getEdge(sentences.get(i), sentences.get(j)));
                    }
                }
            }
        }
        double total = (Summary.SENTENCE_EXTRACTION*(Summary.SENTENCE_EXTRACTION-1))/2;
        return sum/total;
    }
    
    private double calculateTRF(Summary summary, ArrayList<Summary> population)
    {
        double max = 0;
        double temp;
        for(Summary m: population)
        {
            temp = calculateTR(m);
            if(temp > max)
                max = temp;
        }
        temp = calculateTR(summary);
        if(temp > max)
                max = temp;
        
        double result = calculateTR(summary)/max;
        return result;
    }
    
    private double calculateTR(Summary m)
    {
        double sum = 0;
        
        for(int i = 0; i < m.actives.length; i++)
            if(m.actives[i])
            {
                sum += graph.getEdgeWeight(graph.getEdge(title, sentences.get(i)));
            }
        
        return sum/sentences.size();
    }
    
    // calcular fitness que depende apenas das sentences individualmente em separado, qd instancia o objeto?
    // e usar os resultados em  calculateFitness(boolean[] activesSentences)
    /*public int calculateFitness(int specificSentence)
    {
        // ...
        return (int)(Math.random()*10);
    }*/
}
