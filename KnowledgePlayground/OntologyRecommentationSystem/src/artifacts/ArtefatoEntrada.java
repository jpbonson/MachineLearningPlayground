package artifacts;

import cartago.*;
import java.util.ArrayList;
import user_interface.Main;

public class ArtefatoEntrada extends Artifact
{
    private ArrayList<String> items1 = new ArrayList<String>();
    private ArrayList<String> items2 = new ArrayList<String>();
    private ArrayList<String> items3 = new ArrayList<String>();
    
    void init()
    {
        items1 = new ArrayList<String>();
        items2 = new ArrayList<String>();
        items3 = new ArrayList<String>();
        
        String args[] = new String[2];
        Main.main(args);
        Main.control.setArtefatoEntrada(this);
    }
    
    @OPERATION
    void get_para_Individuos(OpFeedbackParam<Object> res){
        if(items1.isEmpty())
        {
            res.set(null);
            return;
        }
        Object item = items1.remove(0);
        res.set(item);
    }
    
    @OPERATION
    void get_para_Classes(OpFeedbackParam<Object> res){
        if(items2.isEmpty())
        {
            res.set(null);
            return;
        }
        Object item = items2.remove(0);
        res.set(item);
    }
    
    @OPERATION
    void get_para_Categorias(OpFeedbackParam<Object> res){
        if(items3.isEmpty())
        {
            res.set(null);
            return;
        }
        Object item = items3.remove(0);
        res.set(item);
    }
    
    @OPERATION
    void isEmpty(ArrayList<String> array, OpFeedbackParam<Boolean> res){
        if(array.isEmpty())
        {
            res.set(true);
            return;
        }
        res.set(false);
    }
    
    void fornecerInformacoes(ArrayList<String> array_keywords)
    {
        for(String s: array_keywords)
        {
            items1.add(s);
            items2.add(s);
            items3.add(s);
        }
    }
}

