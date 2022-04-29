package test;

import android.content.Context;

import com.example.marmi.cardschool.data.WordController;

public class ArticlePresenter {


    public String target = "en";
    public String mode;
    public String from;
    public String to;
    private WordController wordController;
    private int index;

    public ArticlePresenter(Context context, ArticleView aV, WordController wc) {
        if (aV.getArguments() != null) {
            from = aV.getArguments().getString("nfrom");
            to = aV.getArguments().getString("nto");
            mode = aV.getArguments().getString("mode");
            wordController = (WordController) aV.getArguments().getSerializable("wc");
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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }


    public WordController getWordController() {
        return wordController;
    }
}
