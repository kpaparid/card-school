package test;

import android.content.Context;

import com.example.marmi.cardschool.data.WordController;

public class articlePresenter {


    public String target = "en";
    private String mode;
    public String from;
    public String to;
    private WordController wordController;

    //public Context context;
    private articleView aV;

    private int index;

    public articlePresenter(Context context, articleView aV, WordController wc) {

        this.aV = aV;
        if (aV.getArguments() != null) {
            from = aV.getArguments().getString("nfrom");
            to = aV.getArguments().getString("nto");
            mode =aV.getArguments().getString("mode");
            wordController = (WordController) aV.getArguments().getSerializable("wc");
        }
        System.out.println(wordController.getWordText());
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
