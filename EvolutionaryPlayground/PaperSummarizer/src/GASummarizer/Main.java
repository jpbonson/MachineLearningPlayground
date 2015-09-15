package GASummarizer;

import java.util.ArrayList;

public class Main
{   
    static double sum1 = 0;
    static double sum2 = 0;
    static double sum3 = 0;
    static double max1 = 0;
    static double max2 = 0;
    static double max3 = 0;
    static double min1 = Integer.MAX_VALUE;
    static double min2 = Integer.MAX_VALUE;
    static double min3 = Integer.MAX_VALUE;
    static Summary min_summary;
    static Summary max_summary;
    static double p, r, fm;
    static int id;
    static int run = 100;
    
    public static void main(String args[])
    {
        //run5sets();
        id = 5;
        FileToSentencesArray f = new FileToSentencesArray("summary"+id+".txt");
        System.out.println(f);
        Summary.SENTENCE_EXTRACTION = (int)Math.ceil(f.sentences.size()*0.2);
        //System.out.println(Summary.SENTENCE_EXTRACTION);
        
        PreProcessedDoc doc = new PreProcessedDoc(f, id);
        FitnessFunction fitnessFunction = new FitnessFunction(doc);
        
        //runTime(doc, fitnessFunction);
        runTimes(doc, fitnessFunction);
    }
    
        public static void runTime(PreProcessedDoc doc, FitnessFunction fitnessFunction)
    {
        System.out.println("START");
        GASummarizer ga = new GASummarizer(fitnessFunction);
        Summary s = ga.run();

        SummaryComparison comp = new SummaryComparison("summary"+id+"_GoldStandard.txt", s, doc.getSentences());
        System.out.println(comp);
        System.out.println();
        p = comp.calculatePrecision();
        r = comp.calculateRecall();
        fm = comp.calculateFMeasure();

        System.out.println("Precision: "+p);
        System.out.println("Recall: "+r);
        System.out.println("F-Measure: "+fm);
    }
    
    public static void run5sets()
    {
        id = 3;
        FileToSentencesArray f = new FileToSentencesArray("summary"+id+".txt");
        //ystem.out.println(f);
        Summary.SENTENCE_EXTRACTION = (int)Math.ceil(f.sentences.size()*0.2);
        //System.out.println(Summary.SENTENCE_EXTRACTION);
        
        PreProcessedDoc doc = new PreProcessedDoc(f, id);
        FitnessFunction fitnessFunction = new FitnessFunction(doc);
        
        //runTime(doc, fitnessFunction);
        runTimes(doc, fitnessFunction);
        //--------------------------------------------
        /*sum1 = 0;
        sum2 = 0;
        sum3 = 0;
        max1 = 0;
        max2 = 0;
        max3 = 0;
        min1 = Integer.MAX_VALUE;
        min2 = Integer.MAX_VALUE;
        min3 = Integer.MAX_VALUE;
        id = 2;
        
        f = new FileToSentencesArray("summary"+id+".txt");
        Summary.SENTENCE_EXTRACTION = (int)Math.ceil(f.sentences.size()*0.2);
        doc = new PreProcessedDoc(f, id);
        fitnessFunction = new FitnessFunction(doc);
        runTimes(doc, fitnessFunction);
        //--------------------------------------------
        sum1 = 0;
        sum2 = 0;
        sum3 = 0;
        max1 = 0;
        max2 = 0;
        max3 = 0;
        min1 = Integer.MAX_VALUE;
        min2 = Integer.MAX_VALUE;
        min3 = Integer.MAX_VALUE;
        id = 3;
        
        f = new FileToSentencesArray("summary"+id+".txt");
        Summary.SENTENCE_EXTRACTION = (int)Math.ceil(f.sentences.size()*0.2);
        doc = new PreProcessedDoc(f, id);
        fitnessFunction = new FitnessFunction(doc);
        runTimes(doc, fitnessFunction);*/
        //--------------------------------------------
        sum1 = 0;
        sum2 = 0;
        sum3 = 0;
        max1 = 0;
        max2 = 0;
        max3 = 0;
        min1 = Integer.MAX_VALUE;
        min2 = Integer.MAX_VALUE;
        min3 = Integer.MAX_VALUE;
        id = 4;
        
        f = new FileToSentencesArray("summary"+id+".txt");
        Summary.SENTENCE_EXTRACTION = (int)Math.ceil(f.sentences.size()*0.2);
        doc = new PreProcessedDoc(f, id);
        fitnessFunction = new FitnessFunction(doc);
        runTimes(doc, fitnessFunction);
        //--------------------------------------------
        sum1 = 0;
        sum2 = 0;
        sum3 = 0;
        max1 = 0;
        max2 = 0;
        max3 = 0;
        min1 = Integer.MAX_VALUE;
        min2 = Integer.MAX_VALUE;
        min3 = Integer.MAX_VALUE;
        id = 5;
        
        f = new FileToSentencesArray("summary"+id+".txt");
        Summary.SENTENCE_EXTRACTION = (int)Math.ceil(f.sentences.size()*0.2);
        doc = new PreProcessedDoc(f, id);
        fitnessFunction = new FitnessFunction(doc);
        runTimes(doc, fitnessFunction);
    }

    public static void runTimes(PreProcessedDoc doc, FitnessFunction fitnessFunction)
    {
        for(int i = 0; i < run; i++)
        {
            GASummarizer ga = new GASummarizer(fitnessFunction);
            Summary s = ga.run();

            SummaryComparison comp = new SummaryComparison("summary"+id+"_GoldStandard.txt", s, doc.getSentences());
            //System.out.println(comp);
            //System.out.println();
            p = comp.calculatePrecision();
            r = comp.calculateRecall();
            fm = comp.calculateFMeasure();
            sum1+=p;
            sum2+=r;
            sum3+=fm;
            if(p > max1)
                max1 = p;
            if(r > max2)
                max2 = r;
            if(fm > max3)
            {
                max3 = fm;
                max_summary = s;
            }
            if(p < min1)
                min1 = p;
            if(r < min2)
                min2 = r;
            if(fm < min3)
            {
                min3 = fm;
                min_summary = s;
            }
            //System.out.println(i+1);
            System.out.println((i+1)+" "+p+" "+r+" "+fm+" | min: "+min3+", max: "+max3+", avg: "+sum3/(double)(i+1));
            System.out.println(s);
            System.out.println();
            /*System.out.println("START");
            System.out.println("Average Precision: "+sum1/(i+1));
            System.out.println("Average Recall: "+sum2/(i+1));
            System.out.println("Average F-Measure: "+sum3/(i+1));
            System.out.println("Min Precision: "+min1);
            System.out.println("Min Recall: "+min2);
            System.out.println("Min F-Measure: "+min3);
            System.out.println("Max Precision: "+max1);
            System.out.println("Max Recall: "+max2);
            System.out.println("Max F-Measure: "+max3);
            System.out.println("END\n");*/
        }
        System.out.println("id: "+id);
        System.out.println("Average Precision: "+sum1/run);
        System.out.println("Average Recall: "+sum2/run);
        System.out.println("Average F-Measure: "+sum3/run);
        System.out.println("Min Precision: "+min1);
        System.out.println("Min Recall: "+min2);
        System.out.println("Min F-Measure: "+min3);
        System.out.println("Max Precision: "+max1);
        System.out.println("Max Recall: "+max2);
        System.out.println("Max F-Measure: "+max3);
        System.out.println("\nMin Summary: "+min_summary);
        ArrayList<Sentence> s_min = SummaryComparison.translateToText(doc.getSentences(), min_summary.actives);
        for(Sentence s: s_min)
            System.out.println(s);
        System.out.println("\nMax Summary: "+max_summary);
        ArrayList<Sentence> s_max = SummaryComparison.translateToText(doc.getSentences(), max_summary.actives);
        for(Sentence s: s_max)
            System.out.println(s);
        System.out.println();
    }
}
