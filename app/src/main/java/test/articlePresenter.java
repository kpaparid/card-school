package test;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.WordController;
import com.example.marmi.cardschool.normal.Translator;

import fragments.EditBtn;
import fragments.FragmentLanguage;
import fragments.WiktionaryBtn;

public class articlePresenter {


    public String target = "en";
    public Cursor dtb;
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
