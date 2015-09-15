package GASummarizer;

import java.io.BufferedReader;
import java.util.ArrayList;

public class FileToSentencesArray
{
    public ArrayList<Sentence> sentences;
    public String title;
    
    private final String introduction_starts = "<introduction>";
    private final String background_starts = "<background>";
    private final String proposal_starts = "<proposal>";
    private final String results_starts = "<results>";
    private final String conclusion_starts = "<conclusion>";
    private Sentence.SECTION current_section = Sentence.SECTION.INTRODUCTION;
    
    public FileToSentencesArray(String fileName)
    {
        sentences = new ArrayList<>();
        processFile(fileName);
    }
    
    private void processFile(String fileName)
    {
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(fileName)))
        {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            title = line;
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
                if(line != null)
                {
                    if(line.equals(introduction_starts))
                        current_section = Sentence.SECTION.INTRODUCTION;
                    if(line.equals(background_starts))
                        current_section = Sentence.SECTION.BACKGROUND;
                    if(line.equals(proposal_starts))
                        current_section = Sentence.SECTION.PROPOSAL;
                    if(line.equals(results_starts))
                        current_section = Sentence.SECTION.RESULTS;
                    if(line.equals(conclusion_starts))
                        current_section = Sentence.SECTION.CONCLUSION;
                    if(line.length() > 2 && line.substring(0, 2).equals("<>"))
                        sentences.add(new Sentence(line.substring(2), current_section, sentences.size()));
                }
            }
        }catch(Exception e){
            System.out.println("EXCEPTION: "+e);
        }
    }
    
    public static void checkFileProcessing()
    {
        try (BufferedReader br = new BufferedReader(new java.io.FileReader("summary.txt")))
        {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            System.out.println("line: "+line);
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
                System.out.println("line: "+line);
            }
            //String everything = sb.toString();
            //System.out.println(everything);
        }catch(Exception e){
            System.out.println("EXCEPTION: "+e);
        }
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
