package test;

import android.content.Context;
import android.database.Cursor;

import com.example.marmi.cardschool.data.WordController;

public class CardPresenter {

    private CardView cV;
    public WordController wordController;
    public String target = "en";
    private String mode;
    public String from;
    public String to;
    public Context context;





    public CardPresenter(Context context, CardView cV, WordController wc) {

        this.context = context;
        this.cV = cV;
        if (cV.getArguments() != null) {
            from = cV.getArguments().getString("nfrom");
            to = cV.getArguments().getString("nto");
            mode =cV.getArguments().getString("mode");
        }

        if(wc == null){
            System.out.println("world controller null");
            wordController = new WordController();
            wordController.initDB(" WHERE rate >= " + from + " AND rate <= " + to + mode + " ORDER BY RANDOM()",context);
        }
        else {
            this.wordController = wc;
        }
    }



    public void backClick() {
        wordController.moveToPrevious();
    }


    public String getTarget(){
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }


    public void textClick() {
        if(!cV.Orig){
            System.out.println("orig");
            wordController.moveToNext();
            cV.origUI(wordController);
            cV.Orig = true;

        }else {
            System.out.println("trans");
            cV.transUI(wordController,target);
            cV.Orig = false;
        }
        
    }

    public WordController getWordController() {
        return wordController;
    }
}
