package mainFragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.transition.Fade;
import android.support.transition.Slide;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.Language;
import com.example.marmi.cardschool.data.Word;
import com.example.marmi.cardschool.normal.Translator;

import java.io.File;
import java.util.SimpleTimeZone;
import java.util.concurrent.ExecutionException;

import fragments.EditBtn;
import fragments.FragmentLanguage;
import fragments.WiktionaryBtn;

public class cardo extends templateFragment{


    private ConstraintLayout mainlayout;
    private ConstraintLayout back;
    private Boolean end = Boolean.FALSE;
    private Boolean flag = Boolean.TRUE;

    private View.OnClickListener textListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (!end) {
                if (flag) {
                    System.out.println("Trans UI");
                    finishTranslation(word);

                    transUI(word);
                    flag = Boolean.FALSE;
                } else {
                    if (dtb.moveToNext()) {
                        word = new Word(dtb);
                    } else {
                        dtb.moveToFirst();
                        word = new Word(dtb);

                    }
                    System.out.println("ORIG UI");
                    flag = Boolean.TRUE;
                    startTranslation(word);
                    origUI(word);
                }
            }
        }
    };
    private View.OnClickListener backListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (dtb.moveToPrevious()) {
                word = new Word(dtb);
                startTranslation(word);
                finishTranslation(word);
                transUI(word);
                flag = Boolean.FALSE;
            }
        }
    };



    private void origUI(Word w) {
        layout.setBackgroundColor(word.getColor());
        wordTxt.setText(w.getWordText());
        original.setText("");
    }
    public void transUI(Word w) {
        layout.setBackgroundColor(Color.parseColor("#DB045B"));
        System.out.println("TARGET " + target);
        wordTxt.setText(w.getTranslated(target));
        original.setText("~ " + w.getWordText());
    }
    public void startTranslation(Word w) {
        translation = new Translator();
        Language l = new Language(w, "de", target);
        translation.execute(l);
    }
    public void finishTranslation(Word w) {
        try {
            w.setTranslated(translation.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        word = w;
    }


    @Override
    public void initGUI() {
        addFragment("wiki","a");
        addFragment("edit","c");
        addFragment("language","b");
        wordTxt = v.findViewById(R.id.word);
        mainlayout = v.findViewById(R.id.mainlayout);
        layout = v.findViewById(R.id.layout);
        back = v.findViewById(R.id.back);
        original = v.findViewById(R.id.original);
        mainlayout.setOnClickListener(textListener);
        back.setOnClickListener(backListener);
        original.setText("");
    }
    @Override
    public int getLayoutID(){
        return R.layout.fr_card;
    }
    @Override
    public void backGround(){

        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        initDB(" WHERE rate >= " + nfrom + " AND rate <= " + nto +" "+ mode + " ORDER BY RANDOM()");
    }

    @Override
    public void postExecute(){
        word = new Word(dtb);
        System.out.println("colorize");
        layout.setBackgroundColor(word.getColor());
        progressBar.setVisibility(View.INVISIBLE);
        wordTxt.setText(word.getWordText());
        startTranslation(word);

    }

    @Override
    public void onInputLanguage(CharSequence input) {
        if (!input.equals("Error")) {
            target = input.toString();
            System.out.println(target);
            startTranslation(word);
            if (!flag) {
                finishTranslation(word);
                transUI(word);
            }
        }
    }
}
