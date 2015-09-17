package user_interface.categorias;

import util.TipoDeCategoria;
import java.util.ArrayList;
import java.util.List;
import util.NLPTool;
import util.Rank;

public class PacoteComCampos
{
    public TipoDeCategoria categoria;
    public ArrayList<String> valores;
    
    public PacoteComCampos(TipoDeCategoria categoria, ArrayList<String> campos_valores)
    {
        this.categoria = categoria;
        this.valores = campos_valores;
    }
    
    public List<String> processarKeywords() // INCOMPLETO: Apenas para GeneralParaOBAA
    {
        if(categoria == TipoDeCategoria.GeneralParaOBAA)
        {
            ArrayList<String> title;
            ArrayList<String> description;
            ArrayList<String> keywords = new ArrayList<String>();
            ArrayList<String> keywords_final = new ArrayList<String>();
            
            //System.out.println("para processar: "+valores);
            
            title = NLPTool.stopWordsRemoval(NLPTool.toLowerCase(valores.get(0)));
            
            description = NLPTool.stopWordsRemoval(NLPTool.toLowerCase(valores.get(1)));
            
            keywords.add(valores.get(2));
            keywords = NLPTool.separateStringsByDotComma(keywords);

            keywords_final.addAll(title);
            keywords_final.addAll(description);
            keywords_final.addAll(keywords);
            NLPTool.removeEmptyStrings(keywords_final);

            //System.out.println("title: "+title);
            //System.out.println("description: "+description);
            //System.out.println("keywords: "+keywords);
            //System.out.println("keywords_final: "+keywords_final);
            
            keywords_final = NLPTool.toLowerCase(keywords_final);
            
            return Rank.rankByQuant(8, keywords_final);
        }
        else
        {
            return new ArrayList<String>();
        }
    }
}
