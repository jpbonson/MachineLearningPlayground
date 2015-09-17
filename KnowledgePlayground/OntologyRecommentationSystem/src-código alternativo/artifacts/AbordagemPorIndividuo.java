package artifacts;

import cartago.*;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import java.util.ArrayList;
import java.util.Arrays;
import util.NLPTool;

public class AbordagemPorIndividuo extends Artifact
{
    void init()
    {
        
    }
    
    @OPERATION
    void executar_abrangente(String keyphrase, int limit, OpFeedbackParam<String[]> result)
    {
        System.out.println("LOG: Abordagem por Individuos STARTED");
        keyphrase = NLPTool.toLowerCase(keyphrase);
        ArrayList<String> keywords = NLPTool.separateWords(keyphrase);
        String keyword = keywords.remove(0);
        
        String queryString = getPrefixes()
                + "SELECT distinct ?object "
                + "WHERE {"
                
                + "{?object db-prop:title ?title . ?title <bif:contains> \""+keyword+"\" . "+filtrarKeyphrase(keywords, "title")+"}"
                + " UNION "
                + "{?object  db-prop:name ?name. ?name <bif:contains> \""+keyword+"\" . "+filtrarKeyphrase(keywords, "name")+"}"
                //+ " UNION "
                //+ "{?object  foaf:name ?label. ?label <bif:contains> \""+keyword+"\" . "+filtrarKeyphrase(keywords, "label")+"}"
                
                + filtrarClasses(classes_filtradas)
                + filtrarClasses(classes_filtradas_avancado)
                //+ filtrarCategorias()
                + "}"
                + "LIMIT "+limit;
        
        QueryExecution qe = new QueryEngineHTTP("http://dbpedia.org/sparql", queryString);

        ArrayList<String> individuos = new ArrayList<String>();
        try
        {
            ResultSet results = qe.execSelect();
            for (; results.hasNext();)
            {
                QuerySolution s = results.next();
                individuos.add(s.getResource("object").toString());
            }
        }
        finally
        {
           qe.close();
        }
        System.out.println("individuos: "+individuos);
        result.set(individuos.toArray(new String[0]));
        System.out.println("LOG: Abordagem por Individuos ENDED: "+individuos.size());
    }
    
    private String filtrarKeyphrase(ArrayList<String> keywords, String campo)
    {
        //String filters_keyphrase = "";
        //for(int i = 0; i < keywords.size(); i++)
        //    filters_keyphrase += "FILTER (CONTAINS(?"+campo+", \""+keywords.get(i) +"\")) .";
        //return filters_keyphrase;
        return "";
    }
    
    public static String getPrefixes()
    {
        String s = "PREFIX db-prop: <http://dbpedia.org/property/>"
                + "PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>"
                + "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "PREFIX dbpedia_category: <http://dbpedia.org/resource/Category:> "
                + "PREFIX dcterms: <http://purl.org/dc/terms/> "
                + "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
                + "PREFIX yago:<http://dbpedia.org/class/yago/> "
                + "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> ";
        return s;
    }
    
    public static ArrayList<String> classes_filtradas =
            new ArrayList(Arrays.asList(new String[]{"dbpedia-owl:PeriodicalLiterature", "dbpedia-owl:Album", 
            "dbpedia-owl:VideoGame", "dbpedia-owl:TelevisionEpisode", "dbpedia-owl:TelevisionShow",
            "dbpedia-owl:Film", "dbpedia-owl:Band", "<http://umbel.org/umbel/rc/TVShow_SingleCW>", "yago:LivingPeople",
            "yago:PuBlication106589574", "yago:Gathering107975026"}));
    public static ArrayList<String> classes_filtradas_avancado =
            new ArrayList(Arrays.asList(new String[]{ "yago:Event100029378",
            "yago:WebSite106359193", "yago:Award106696483", "yago:Professorship100598056",
            "yago:Medium106254669", "yago:HotSpot108586330", "yago:Contestant109613191",
            "yago:Trainer110722575", "yago:MemoryDevice103744840", "yago:Professional110480253", "yago:Person100007846",
            "yago:Music107020895"}));
    private String filtrarClasses(ArrayList<String> classes)
    {
        String result = "";
        for(String s: classes)
            result += "filter (NOT EXISTS {?object a "+s+" }) .";
        return result; 
    }
    
    public static ArrayList<String> categorias_filtradas =
            new ArrayList(Arrays.asList(new String[]{"Sports", "Comics",  "Albums", "Television_series", "College_football_seasons"}));
    public static ArrayList<String> categorias_filtradas_avancado =
            new ArrayList(Arrays.asList(new String[]{"Clubs_and_societies", "Economies_by_country", "Companies",
                "Sports_teams_by_country", "University_and_college_sports_clubs_by_country",
            "National_Register_of_Historic_Places_by_county", "National_Register_of_Historic_Places", "Hospitals",
            "Musical_compositions", "Hunting", "Deaths_from_disease"}));
    private String filtrarCategorias(ArrayList<String> categorias, int t_max)
    {
        String option = "option(TRANSITIVE , T_DISTINCT, t_max("+t_max+"))";
        String result = "";
        for(String s: categorias)
            result += "filter (NOT EXISTS {?object dcterms:subject ?Category . ?Category skos:broader dbpedia_category:"+s+" "+option+" }) .";
        return result;  
    }
}
