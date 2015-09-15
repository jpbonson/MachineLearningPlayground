package GASummarizer;

import java.io.BufferedReader;
import java.util.ArrayList;

public class SummaryComparison
{
    private ArrayList<Sentence> gold_summary;
    private ArrayList<Sentence> auto_summary;
    private ArrayList<Sentence> original_sentences;
    
    public SummaryComparison(String fileName, Summary summary, ArrayList<Sentence> original_sentences)
    {
        this.original_sentences = original_sentences;
        gold_summary = new ArrayList<>();
        processFile(fileName);
        this.auto_summary = translateToText(original_sentences, summary.actives);
    }
    
    /*public double calculateSpeciﬁcity()
    {
        double temp1 = 0; // true negative
        for(Sentence s: original_sentences)
        {
            if(!containsSentence(gold_summary, s) && !containsSentence(auto_summary, s))
            {
                temp1++;
            }
        }
            
        double temp2 = temp1+(Summary.SENTENCE_EXTRACTION-getTruePositive());
        return temp1/temp2;
    }*/
    
    private boolean containsSentence(ArrayList<Sentence> array, Sentence s)
    {
        for(Sentence s2: array)
        {
            if(s2.content.equals(s.content))
            {
                return true;
            }
        }
        return false;
    }
    //The false positive rate (FP) is the proportion of negatives cases that were incorrectly classified as positive
    
    public double calculatePrecision()
    {
        double temp1 = getTruePositive();
        double fp = 0;
        for(Sentence s: original_sentences)
        {
            if(!containsSentence(gold_summary, s) && containsSentence(auto_summary, s))
            {
                fp++;
            }
        }
        
        double temp2 = temp1 + fp; // FP
        return temp1/temp2;
    }
    
    public double calculateRecall()
    {
        double temp1 = getTruePositive();
        double fn = 0;
        for(Sentence s: original_sentences)
        {
            if(containsSentence(gold_summary, s) && !containsSentence(auto_summary, s))
            {
                fn++;
            }
        }
        double temp2 = temp1 + fn; //FN
        return temp1/temp2;
    }
    
    private double getTruePositive()
    {
        double temp1 = 0; // true positive
        for(Sentence s1: gold_summary)
            for(Sentence s2: auto_summary)
            {
                if(s1.content.equals(s2.content))
                {
                    temp1++;
                    break;
                }
            }
        
        return temp1;
    }
    
    public double calculateFMeasure()
    {
        double p = calculatePrecision();
        double r = calculateRecall();
        if(p+r == 0)
            return 0;
        return (2*p*r)/(p+r);
    }
    
    private void processFile(String fileName)
    {
        int id = 0;
        Sentence.SECTION section = null;
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(fileName)))
        {
            String line = br.readLine();
            for(Sentence s2: original_sentences)
            {
                if(line.substring(2).equals(s2.content))
                {
                    id = s2.id;
                    section = s2.section;
                }
            }
            gold_summary.add(new Sentence(line.substring(2), section, id));
            while (line != null) {
                line = br.readLine();
                if(line != null)
                {
                    for(Sentence s2: original_sentences)
                    {
                        if(line.substring(2).equals(s2.content))
                        {
                            id = s2.id;
                            section = s2.section;
                        }
                    }
                    gold_summary.add(new Sentence(line.substring(2), section, id));
                }
            }
        }catch(Exception e){
            System.out.println("EXCEPTION: "+e);
        }
    }
    
    public static ArrayList<Sentence> translateToText(ArrayList<Sentence> sentences, boolean[] actives)
    {
        //String str = "";
        ArrayList<Sentence> array = new ArrayList<Sentence>();
        for(int i = 0; i < sentences.size(); i++)
            if(actives[i])
            {
                //str += sentences.get(i).content+" ";
                //str += sentences.get(i).content+"\n";
                //str += sentences.get(i)+"\n";
                array.add(sentences.get(i));
            }
        return array;
    }
    
    @Override
    public String toString()
    {
        String str = "Summary - Gold Standard:\n";
        for(Sentence s: gold_summary)
            str += s+"\n";
        str += "\n\nSummary - Automatic:\n";
        for(Sentence s: auto_summary)
            str += s+"\n";
        return str;
    }    
}
