package test;

import android.content.Context;

import com.example.marmi.cardschool.data.WordController;

public class articlePresenter {


    public String target = "en";
    private String mode;
    public String from;
    public String to;
    private WordController wordController;

    public Context context;
    private articleView aV;

    private int index;

    public articlePresenter(Context context, articleView aV, WordController wc) {
        this.context = context;
        this.aV = aV;
        if (aV.getArguments() != null) {
            from = aV.getArguments().getString("nfrom");
            to = aV.getArguments().getString("nto");
            mode =aV.getArguments().getString("mode");
        }

        if(wc == null){
            System.out.println("world controller null");
            wordController = new WordController();
            wordController.initDB(" WHERE rate >= " + from + " AND rate <= " + to + " AND type = 'Nomen' ORDER BY RANDOM()",context);
        }
        else {
            this.wordController = wc;
        }
    }


    public Integer getIndex() {
        switch (wordController.getArticle()) {
            case "der": {
                index = 0;
                break;
            }
            case "die": {
                index = 1;
                break;
            }
            case "das": {
                index = 2;
                break;
            }
        }
        return index;
    }












    public String getTarget(){
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }


    public WordController getWordController() {
        return wordController;
    }
}
