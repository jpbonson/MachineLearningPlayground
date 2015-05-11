package GASummarizer;

public class Summary implements Comparable
{
    private double fitness = -1;
    public boolean[] actives;
    public static int SENTENCE_EXTRACTION = -1;
    public double param1;
    public double param2;
    public double param3;
    public double param4;
    public double param5;
    
    public Summary(int sentencesQuantity)
    {
        initializeActives(sentencesQuantity);
    }
    
    public Summary(boolean[] actives)
    {
        this.actives = actives;
    }
    
    public void setFitness(double f)
    {
        this.fitness = f;
    }
    
    public double getFitness()
    {
        return fitness;
    }
    
    public int activatedSentences()
    {
        int cont = 0;
        for(int i = 0; i < actives.length; i++)
            if(actives[i])
                cont++;
        return cont;
    }
    
    private void initializeActives(int size)
    {
        actives = new boolean[size];
        
        int index;
        for(int i = 0; i < SENTENCE_EXTRACTION; i++)
        {
            index = (int)(Math.random()*size);
            if(actives[index] == true)
                i--;
            else
                actives[index] = true;
        }
    }
    
    public boolean isEqualTo(Summary summary)
    {
        for(int i = 0; i < actives.length; i++)
            if(actives[i] != summary.actives[i])
                return false;
        return true;
    }
    
    @Override
    public int compareTo(Object other)
    {
        Summary otherSummary = (Summary)other;
        if(fitness < otherSummary.fitness)
            return -1;
        if(fitness > otherSummary.fitness)
            return 1;
        return 0;
    }
    
    @Override
    public String toString()
    {
        String str = "";
        str += "["+fitness+" "+param1+" "+param2+" "+param3+" "+param4+" "+param5+"]: \n";
        for(int i = 0; i < actives.length; i++)
            if(actives[i])
                str+=1;
            else
                str+=0;
        return str+"\n";
    }
}
