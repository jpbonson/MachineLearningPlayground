package artifacts;

import cartago.*;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import java.util.ArrayList;
import java.util.Arrays;
import util.NLPTool;

public class AbordagemPorClasses extends Artifact
{
    void init()
    {
        
    }
    
    @OPERATION
    void executar_abrangente(String keyphrase, int limit, OpFeedbackParam<Object> result)
    {
        System.out.println("LOG: Abordagem por Classes STARTED");
        
        keyphrase = NLPTool.toLowerCase(keyphrase);
        ArrayList<String> keywords = NLPTool.separateWords(keyphrase);
        String keyword = keywords.remove(0);
        
        String queryString = AbordagemPorIndividuo.getPrefixes()
                + "select distinct ?Concept where {"
                + "[] a ?Concept ."
                + " ?Concept rdfs:label ?title ."
                + " ?title <bif:contains> \""+keyword+"\" ."
                + filtrarClasses(AbordagemPorIndividuo.classes_filtradas, 5)
                + filtrarKeyphrase(keywords)
                + "} LIMIT "+limit;
        
        QueryExecution qe = new QueryEngineHTTP("http://dbpedia.org/sparql", queryString);
                
        ArrayList<String> classes = new ArrayList<String>();
        
        try
        {
            ResultSet results = qe.execSelect();
            for (; results.hasNext();)
            {
                QuerySolution s = results.next();
                classes.add(s.getResource("Concept").toString());
            }
        }
        finally
        {
           qe.close();
        }
        
        result.set(classes);
        
        System.out.println("LOG: Abordagem por Classes ENDED: "+classes.size());
    }
    
    @OPERATION
    void executar_especifico(ArrayList<String> classes, OpFeedbackParam<Object> result)
    {
        System.out.println("LOG: Abordagem por Classes Avancado STARTED");
        
        ArrayList<String> resultado = new ArrayList<String>();
        
        int cont = 1;
        for(String classe: classes)
        {
            //System.out.println("LOG: "+cont+" "+classe);
            cont++;
            String queryString = AbordagemPorIndividuo.getPrefixes()
                + "SELECT distinct ?Concept "
                + "WHERE {"
                
                + "{?object ?p ?Concept FILTER (?Concept = <"+classe+">) .}"
                
                + filtrarClasses(AbordagemPorIndividuo.classes_filtradas_avancado, 5)
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
                    parcial.add(s.getResource("Concept").toString());
                }
            }
            finally
            {
               qe.close();
            }
            //System.out.println("parcial: "+parcial);
            resultado.addAll(parcial);
        }
        System.out.println("classes: "+resultado);

        result.set(resultado);
        
        System.out.println("LOG: Abordagem por Classes Avancado ENDED: "+resultado.size());
    }
    
    @OPERATION
    void executar_getIndividuos(ArrayList<String> classes, int limit_subclasses, OpFeedbackParam<Object> result)
    {
        System.out.println("LOG: Abordagem por Classes Avancado - getIndividuos STARTED");
        
        ArrayList<String> individuos = new ArrayList<String>();
        for(String classe: classes)
            individuos.addAll(getAllSubClasses(classe, limit_subclasses));
        
        System.out.println("individuos-classes: "+individuos);
        
        result.set(individuos);
        
        System.out.println("LOG: Abordagem por Classes Avancado - getIndividuos ENDED: "+individuos.size());
    }
    
    public ArrayList<String> getAllSubClasses(String classe, int limit)
    {
        String queryString = AbordagemPorIndividuo.getPrefixes()
                + "select distinct ?subclass where {"
                + " ?subclass a <"+classe+"> option(TRANSITIVE, t_distinct, T_MAX(4))."
                + "} LIMIT "+limit;
        
        QueryExecution qe = new QueryEngineHTTP("http://dbpedia.org/sparql", queryString);
                
        ArrayList<String> subclasses = new ArrayList<String>();
        try
        {
            ResultSet results = qe.execSelect();
            for (; results.hasNext();)
            {
                QuerySolution s = results.next();
                subclasses.add(s.getResource("subclass").toString());
            }
        }
        finally
        {
           qe.close();
        }
        return subclasses;
    }
    
    private String filtrarClasses(ArrayList<String> classes, int t_max)
    {
        String option = "option(TRANSITIVE, t_distinct, T_MAX("+t_max+"))";
        String result = "";
        for(String s: classes)
            result += "filter (NOT EXISTS {?Concept rdfs:subClassOf "+s+" "+option+" }) .";
        return result; 
    }
    
    private String filtrarKeyphrase(ArrayList<String> keywords)
    {
        //String filters_keyphrase = "";
        //for(int i = 0; i < keywords.size(); i++)
        //    filters_keyphrase += "FILTER (CONTAINS(?title, \""+keywords.get(i) +"\")) .";
        //return filters_keyphrase;
        return "";
    }
}
