package artifacts;

import cartago.*;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import java.util.ArrayList;
import java.util.Arrays;
import util.NLPTool;

public class AbordagemPorCategoria extends Artifact
{
    void init()
    {
        
    }
    
    @OPERATION
    void executar_abrangente(String keyphrase, int limit, OpFeedbackParam<Object> res)
    {
        System.out.println("LOG: Abordagem por Categoria Abrangente STARTED");
        int t_max = 6;
        ArrayList<String> result = new ArrayList<String>();
        boolean success = false;
        while(!success)
        {
            try
            {
                result = executar_abrangente2(keyphrase, t_max, limit);
                success = true;
            }catch(Exception e)
            {
                t_max-=2;
                if(t_max == 0)
                {
                    res.set(new ArrayList<String>());
                    System.out.println("LOG: Abordagem por Categoria Abrangente ENDED: 0");
                    return;
                }
            }
        }
        res.set(result);
        System.out.println("LOG: Abordagem por Categoria Abrangente ENDED: "+result.size());
    }
    
    @OPERATION
    void executar_especifico(ArrayList<String> categorias, OpFeedbackParam<Object> res)
    {
        System.out.println("LOG: Abordagem por Categoria Especifico STARTED");
        int t_max = 6;
        ArrayList<String> result = new ArrayList<String>();
        boolean success = false;
        while(!success)
        {
            try
            {
                result = executar_especifico2(categorias, t_max);
                success = true;
            }catch(Exception e)
            {
                t_max-=2;
                if(t_max == 0)
                {
                    res.set(new ArrayList<String>());
                    System.out.println("LOG: Abordagem por Categoria Especifico ENDED: 0");
                    return;
                }
            }
        }
        res.set(result);
        System.out.println("LOG: Abordagem por Categoria Especifico ENDED: "+result.size());
    }
    
    public ArrayList<String> executar_abrangente2(String keyphrase, int t_max, int limit)
    {
        keyphrase = NLPTool.toLowerCase(keyphrase);
        ArrayList<String> keywords = NLPTool.separateWords(keyphrase);
        String keyword = keywords.remove(0);
        
        String queryString = AbordagemPorIndividuo.getPrefixes()
                + "select distinct ?Category where {"
                + "?s dcterms:subject ?Category ."
                + " ?Category rdfs:label ?title ."
                + " ?title <bif:contains> \""+keyword+"\" ."
                + filtrarCategorias(AbordagemPorIndividuo.categorias_filtradas, t_max)
                + filtrarKeyphrase(keywords)
                + "} LIMIT "+limit;
        
        QueryExecution qe = new QueryEngineHTTP("http://dbpedia.org/sparql", queryString);
                
        ArrayList<String> categorias = new ArrayList<String>();
        try
        {
            ResultSet results = qe.execSelect();
            for (; results.hasNext();)
            {
                QuerySolution s = results.next();
                categorias.add(s.getResource("Category").toString());
            }
        }
        finally
        {
           qe.close();
        }
        
        return categorias;
    }
    
    @OPERATION
    ArrayList<String> executar_especifico2(ArrayList<String> categorias, int t_max)
    {
        System.out.println("LOG: Categorias, tentativa: "+t_max+" STARTED");
        
        ArrayList<String> resultado = new ArrayList<String>();
        
        int cont = 1;
        for(String categoria: categorias)
        {
            //System.out.println("LOG: "+cont+" "+categoria);
            cont++;
            String queryString = AbordagemPorIndividuo.getPrefixes()
                + "SELECT distinct ?Category "
                + "WHERE {"
                
                + "{?s dcterms:subject ?Category FILTER (?Category = <"+categoria+">) .}"
                
                + filtrarCategorias(AbordagemPorIndividuo.categorias_filtradas_avancado, t_max)
                + "}"
                + "LIMIT 5";
        
            QueryExecution qe = new QueryEngineHTTP("http://dbpedia.org/sparql", queryString);

            ArrayList<String> parcial = new ArrayList<String>();
            try
            {
                ResultSet results = qe.execSelect();
                for (; results.hasNext();)
                {
                    QuerySolution s = results.next();
                    parcial.add(s.getResource("Category").toString());
                }
            }
            finally
            {
               qe.close();
            }
            //System.out.println("parcial: "+parcial);
            resultado.addAll(parcial);
        }
        System.out.println("categorias: "+resultado);

        System.out.println("LOG: Categorias, tentativa: "+t_max+" ENDED");
        
        return resultado;
    }
    
    @OPERATION
    void executar_getIndividuos(ArrayList<String> categorias, int limit_ind, OpFeedbackParam<Object> result)
    {
        System.out.println("LOG: Abordagem por Categorias - getIndividuos STARTED");
        
        ArrayList<String> individuos = new ArrayList<String>();
        for(String classe: categorias)
            individuos.addAll(getAllIndividuals(classe, limit_ind));
        
        System.out.println("individuos-categorias: "+individuos);
        
        result.set(individuos);
        
        System.out.println("LOG: Abordagem por Categorias - getIndividuos ENDED: "+individuos.size());
    }
    
    public ArrayList<String> getAllIndividuals(String category, int limit)
    {
        String queryString = AbordagemPorIndividuo.getPrefixes()
                + "select distinct ?ind where {"
                + " ?ind dcterms:subject <"+category+"> ."
                + "} LIMIT "+limit;
        
        QueryExecution qe = new QueryEngineHTTP("http://dbpedia.org/sparql", queryString);
                
        ArrayList<String> ind = new ArrayList<String>();
        try
        {
            ResultSet results = qe.execSelect();
            for (; results.hasNext();)
            {
                QuerySolution s = results.next();
                ind.add(s.getResource("ind").toString());
            }
        }
        finally
        {
           qe.close();
        }
        return ind;
    }
    
    private String filtrarKeyphrase(ArrayList<String> keywords)
    {
        //String filters_keyphrase = "";
        //for(int i = 0; i < keywords.size(); i++)
        //    filters_keyphrase += "FILTER (CONTAINS(?title, \""+keywords.get(i) +"\")) .";
        //return filters_keyphrase;
        return "";
    }
    
    private String filtrarCategorias(ArrayList<String> categorias, int t_max)
    {
        String option = "option(TRANSITIVE, T_DISTINCT, t_max("+t_max+"), T_NO_CYCLES, t_shortest_only)";
        String result = "";
        for(String s: categorias)
            result += "filter (NOT EXISTS {?Category skos:broader dbpedia_category:"+s+" "+option+" }) .";
        return result;  
    }
}
