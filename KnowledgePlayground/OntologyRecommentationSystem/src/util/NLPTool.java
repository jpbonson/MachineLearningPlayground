package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.io.IOException;
import org.apache.lucene.util.*;
import org.apache.lucene.analysis.*;
import java.io.StringReader;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.*;
import org.apache.lucene.analysis.en.EnglishAnalyzer;

public class NLPTool
{
    public static String toLowerCase(String s)
    {
        return s.toLowerCase();
    }
    
    public static ArrayList<String> toLowerCase(ArrayList<String> array)
    {
        ArrayList<String> result = new ArrayList<String>();
        for(int i = 0; i < array.size(); i++)
        {
            result.add(array.get(i).toLowerCase());
        }
        return result;
    }
    
    public static ArrayList<String> separateWords(String sentence)
    {
        String[] t = sentence.split(" ");
        return new ArrayList(Arrays.asList(t));
    }
    
    public static void removeEmptyStrings(ArrayList<String> words)
    {
        for(int i=0; i < words.size(); i++)
            if(words.get(i).trim().isEmpty())
            {
                words.remove(i);
                i--;
            }
    }
    
    public static ArrayList<String> separateStringsByDotComma(ArrayList<String> words)
    {
        String[] keywords;
        ArrayList<String> temp = new ArrayList<String>();
        ArrayList<String> temp2 = new ArrayList<String>();
        for(int i=0; i < words.size(); i++)
        {
            keywords = words.get(i).split(";+");
            temp.addAll(Arrays.asList(keywords));
        }
        
        for(String s: temp)
        {
            temp2.add(s.trim());
        }
        
        removeEmptyStrings(temp2);
        
        return temp2;
    }
    
    public static ArrayList<String> stopWordsRemoval(String input)
    {
        // tokenize the input string
        TokenStream tokenStream = new ClassicTokenizer(Version.LUCENE_35, new StringReader(input));
        // remove stop words
        tokenStream = new StopFilter(Version.LUCENE_35, tokenStream, EnglishAnalyzer.getDefaultStopSet());

        // retrieve the remaining tokens
        Set<String> tokens = new HashSet<String>();
        CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
        try {
            while (tokenStream.incrementToken()) {
                tokens.add(token.toString());
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        
        ArrayList<String> array = new ArrayList<String>();
        for(String s: tokens)
        {
            if(!s.toLowerCase().equals("my") && !s.toLowerCase().equals("has") && !s.toLowerCase().equals("was")
                    && !s.toLowerCase().equals("does") && !s.toLowerCase().equals("goes") && !s.toLowerCase().equals("dies")
                    && !s.toLowerCase().equals("yes") && !s.toLowerCase().equals("gets") && !s.toLowerCase().equals("its")
                    && !s.toLowerCase().equals("he") && !s.toLowerCase().equals("she") && !s.toLowerCase().equals("it")
                    && !s.toLowerCase().equals("we") && !s.toLowerCase().equals("they") && !s.toLowerCase().equals("you"))
                array.add(stripEnglishPlural(s));
        }
        
        return array;
    }

        
    private static String stripEnglishPlural(String word)
    { 
        if ( word.endsWith("sses") || 
            word.endsWith("xes") || 
            word.endsWith("hes") )
        { 
            // remove 'es' 
            return word.substring(0,word.length()-2); 
        } 
        else if ( word.endsWith("ies") )
        { 
            // remove 'ies', replace with 'y' 
            return word.substring(0,word.length()-3)+'y'; 
        } 
        else if ( word.endsWith("s") && 
        !word.endsWith("ss") && 
        !word.endsWith("is") && 
        !word.endsWith("us") && 
        !word.endsWith("pos") ) { 
        // remove 's' 
            return word.substring(0,word.length()-1); 
        } 
        return word; 
    } 
}
