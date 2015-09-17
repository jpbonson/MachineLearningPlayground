package artifacts;

import cartago.*;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import user_interface.Main;
import util.NLPTool;
import util.Rank;
import util.TipoDeSugestao;

public class ArtefatoSaida extends Artifact
{
    private ArrayList<String> items;
    private ArrayList<String> items_less_value;
    private ArrayList<String> items_more_value;
    private boolean pronto = false;
    private int cont = 0;
    private List<String> resultados_por_quant;
    
    void init()
    {
        items = new ArrayList<String>();
        items_less_value = new ArrayList<String>();
        items_more_value = new ArrayList<String>();
        resultados_por_quant = new ArrayList<String>();

        Main.control.setArtefatoSaida(this);
    }
    
    @OPERATION
    void put(Object obj)
    {
        ArrayList<String> array = (ArrayList<String>)obj;
        items.addAll(array);

        cont++;
        int quantAgentsPorTipo = 3;
        if(cont ==  Controladora.quantKeywords*quantAgentsPorTipo)
        {
            resultados_por_quant = Rank.rankByQuant(20, items, items_less_value, items_more_value);
            System.out.println("rankeado: "+resultados_por_quant);
            pronto = true;
        }
    }
    
    @OPERATION
    void put_less_value(Object obj)
    {
        ArrayList<String> array = (ArrayList<String>)obj;
        items_less_value.addAll(array);
    }
    
    @OPERATION
    void put_more_value(Object obj)
    {
        ArrayList<String> array = (ArrayList<String>)obj;
        items_more_value.addAll(array);
    }
    
    public boolean isReady()
    {
        return pronto;
    }
    
    public List<String> getItemsDevolvidos(TipoDeSugestao tipo)
    {
        String temp;
        String result;
        List<String> list = new ArrayList<String>();
        if(tipo == TipoDeSugestao.SUGESTAO_TITLE)
        {
            for(String s: resultados_por_quant)
            {
                result = extrairTitleAndKeyword(s);
                if(!result.equals(""))
                {
                    temp = s+" : "+result;
                    list.add(temp);
                }
            }
            return list;
        }
        if(tipo == TipoDeSugestao.SUGESTAO_DESCRIPTION)
        {
            for(String s: resultados_por_quant)
            {
                result = extrairDescription(s);
                if(!result.equals(""))
                {
                    temp = s+" : "+result;
                    list.add(temp);
                }
            }
            return list;
        }
        if(tipo == TipoDeSugestao.SUGESTAO_KEYWORDS)
        {
            for(String s: resultados_por_quant)
            {
                result = extrairTitleAndKeyword(s);
                if(!result.equals(""))
                {
                    temp = s+" : "+result;
                    list.add(temp);
                }
            }
            return list;
        }
        return resultados_por_quant;
    }
    
    public void resetar()
    {
        pronto = false;
        items.clear();
        resultados_por_quant.clear();
        cont = 0;
    }
    
    private static String extrairDescription(String individuo)
    {
        //System.out.println("LOG: Description STARTED");
        ArrayList<String> individuos;
        individuos = extrairDescription_parcial(individuo, "dbpedia-owl:abstract");
        
        if(individuos.isEmpty())
        {
            individuos = extrairDescription_parcial(individuo, "rdfs:comment");
        }
       
        if(individuos.isEmpty())
        {
            return "";
        }
        //System.out.println("LOG: Description ENDED");
        return individuos.get(0);
    }
    
    private static ArrayList<String> extrairDescription_parcial(String individuo, String prop)
    {
        String queryString = AbordagemPorIndividuo.getPrefixes()
                + "SELECT distinct ?title "
                + "WHERE "
                + "{<"+individuo+"> "+prop+" ?title . FILTER ( lang(?title) = \"en\") .}"
                + "LIMIT 5";
        
        QueryExecution qe = new QueryEngineHTTP("http://dbpedia.org/sparql", queryString);
                
        ArrayList<String> individuos = new ArrayList<String>();
        try
        {
            ResultSet results = qe.execSelect();
            for (; results.hasNext();)
            {
                QuerySolution s = results.next();
                individuos.add(s.getLiteral("title").getString().toString());
            }
        }
        finally
        {
           qe.close();
        }
        
        return individuos;    
    }
    
    private static String extrairTitleAndKeyword(String individuo)
    {
        //System.out.println("LOG: Title e Keyword STARTED");
        ArrayList<String> individuos;
        individuos = extrairTitleAndKeyword_parcial(individuo, "db-prop:title");
        
        if(individuos.isEmpty())
        {
            individuos = extrairTitleAndKeyword_parcial(individuo, "db-prop:name");
        }
        
        if(individuos.isEmpty())
        {
            individuos = extrairTitleAndKeyword_parcial(individuo, "foaf:name");
        }
        
        if(individuos.isEmpty())
        {
            individuos = extrairTitleAndKeyword_parcial(individuo, "rdfs:label");
        }
        
        if(individuos.isEmpty())
        {
            return "";
        }
        //System.out.println("LOG: Title e Keyword ENDED");
        return individuos.get(0);
    }
    
    private static ArrayList<String> extrairTitleAndKeyword_parcial(String individuo, String prop)
    {
        String queryString = AbordagemPorIndividuo.getPrefixes()
                + "SELECT distinct ?title "
                + "WHERE "
                + "{ <"+individuo+"> "+prop+" ?title . FILTER ( lang(?title) = \"en\") .}"
                + "LIMIT 5";
        
        QueryExecution qe = new QueryEngineHTTP("http://dbpedia.org/sparql", queryString);
                
        ArrayList<String> individuos = new ArrayList<String>();
        try
        {
            ResultSet results = qe.execSelect();
            for (; results.hasNext();)
            {
                QuerySolution s = results.next();
                individuos.add(s.getLiteral("title").getString().toString());
            }
        }
        finally
        {
           qe.close();
        }
        
        return individuos;    
    }
}

