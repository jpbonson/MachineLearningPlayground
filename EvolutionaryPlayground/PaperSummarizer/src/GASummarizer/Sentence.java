package GASummarizer;
import java.io.Serializable;
import java.util.ArrayList;

public class Sentence implements Serializable
{
    public enum SECTION{INTRODUCTION, BACKGROUND, PROPOSAL, RESULTS, CONCLUSION};
    
    public final String content;
    public final SECTION section;
    public final int id;
    
    public Sentence(String content, SECTION section, int id)
    {
        this.content = content;
        this.section = section;
        this.id = id;
    }
    
    public ArrayList<String> getTerms()
    {
        String[] words = content.split("\\s+");
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].replaceAll("\\.", "");
            words[i] = words[i].replaceAll("\\,", "");
            words[i] = words[i].replaceAll("\\;", "");
            words[i] = words[i].replaceAll("\\[", "");
            words[i] = words[i].replaceAll("\\]", "");
            words[i] = words[i].replaceAll("\\(", "");
            words[i] = words[i].replaceAll("\\)", "");
            words[i] = words[i].replaceAll("\\:", "");
            words[i] = words[i].replaceAll("\\!", "");
            words[i] = words[i].replaceAll("\\?", "");
            words[i] = words[i].toLowerCase();
            result.add(words[i]);
        }
        return result;
    }
    
    @Override
    public String toString()
    {
        return "<"+id+", "+section+"> "+content;
        //return id+"";
    }
}
