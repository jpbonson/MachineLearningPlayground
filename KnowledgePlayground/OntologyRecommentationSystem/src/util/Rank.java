package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Rank
{
    /*public static List<String> rankBySimilarity(int quant, ArrayList<String> keywords,  ArrayList<String> individuos) // INCOMPLETO
    {
        
    }*/
    
    public static List<String> rankByQuant(int quant, ArrayList<String> individuos)
    {
        if(individuos.isEmpty())
            return new ArrayList<String>();
        Collections.sort(individuos);
        Hashtable<String, Double> ranked = new Hashtable<String, Double>();
        double points;
        for(int i = 0; i < individuos.size(); i++)
        {
            ranked.put(individuos.get(i), 0.0);
        }
        for(int i = 0; i < individuos.size(); i++)
        {
            points = 1;
            ranked.put(individuos.get(i), ranked.get(individuos.get(i)) + points);
        }
        
        ArrayList<String> s_ranked = sortValue(ranked, false);
        if(quant > s_ranked.size())
            quant = s_ranked.size();
        return s_ranked.subList(0, quant);
    }
    
    public static List<String> rankByQuant(int quant, ArrayList<String> individuos, ArrayList<String> less_value, ArrayList<String> more_value)
    {
        if(individuos.isEmpty())
            return new ArrayList<String>();
        Collections.sort(individuos);
        Hashtable<String, Double> ranked = new Hashtable<String, Double>();
        double points;
        for(int i = 0; i < individuos.size(); i++)
        {
            ranked.put(individuos.get(i), 0.0);
        }
        for(int i = 0; i < individuos.size(); i++)
        {
            points = 1;
            if(less_value.contains(individuos.get(i)))
            {
                points = 0.8;
                less_value.remove(individuos.get(i));
                //System.out.println("LESS points: "+individuos.get(i));
            }else if(more_value.contains(individuos.get(i)))
            {
                points = 1.3;
                more_value.remove(individuos.get(i));
                //System.out.println("MORE points: "+individuos.get(i));
            }
            //else
            //{
            //    System.out.println("REGULAR points: "+individuos.get(i));
            //}
            ranked.put(individuos.get(i), ranked.get(individuos.get(i)) + points);
        }
        
        ArrayList<String> s_ranked = sortValue(ranked, false);
        if(quant > s_ranked.size())
            quant = s_ranked.size();
        return s_ranked.subList(0, quant);
    }

     private static ArrayList<String> sortValue(Hashtable<String, Double> t, boolean output){

       //Transfer as List and sort it
       ArrayList<Map.Entry<String, Double>> l = new ArrayList(t.entrySet());
       Collections.sort(l, new Comparator<Map.Entry<String, Double>>(){

         public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
            return -o1.getValue().compareTo(o2.getValue());
        }});

       ArrayList<String> s = new ArrayList<String>();
       for(Map.Entry<String, Double> entry: l)
       {
           if(!output)
               s.add(entry.getKey().toString());
           else
               s.add(entry.toString());
       }
       return s;
    }
}
