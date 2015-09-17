package artifacts;

import java.util.ArrayList;
import javax.swing.*;
import user_interface.categorias.PacoteComCampos;
import util.TipoDeSugestao;
import java.util.List;
import util.NLPTool;

public class Controladora
{
    ArtefatoEntrada artefatoEntrada;
    ArtefatoSaida artefatoSaida;
    public static int quantKeywords;
    
    public void setArtefatoEntrada(ArtefatoEntrada artefato)
    {
        artefatoEntrada = artefato;
    }
    
    public void setArtefatoSaida(ArtefatoSaida artefato)
    {
        artefatoSaida = artefato;
    }
    
    public void obterSugestoes(JTextArea text, ArrayList<PacoteComCampos> valores, TipoDeSugestao tipo)
    {
        ArrayList<String> keywords = new ArrayList<String>();
        for(PacoteComCampos p: valores)
            keywords.addAll(p.processarKeywords());

        System.out.println("keywords: "+keywords);

        quantKeywords = keywords.size();
        artefatoSaida.resetar();
        artefatoEntrada.fornecerInformacoes(keywords);
        //TesteThread myRunnable = new TesteThread(text, tipo);
        //Thread t = new Thread(myRunnable);
        //t.start();
    }
    
    class TesteThread implements Runnable
    {
        private JTextArea field;
        private TipoDeSugestao tipo;

        public TesteThread(JTextArea field, TipoDeSugestao tipo) {
            this.field = field;
            this.tipo = tipo;
        }
        
        public void run()
        {
            try{
                while(!artefatoSaida.isReady()){
                    Thread.yield();
                }
            }catch(Exception e){}
            List<String> items = artefatoSaida.getItemsDevolvidos(tipo);
            String t = "";
            for(String s: items)
                t+=s+"\n";
            field.setText(t);
        }
    }
}
