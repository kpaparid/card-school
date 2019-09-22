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
import com.example.marmi.cardschool.data.WordController;
import com.example.marmi.cardschool.data.WordModel;
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
//                    finishTranslation(wordController);

                    transUI(wc);
                    flag = Boolean.FALSE;
                } else {
                    if (dtb.moveToNext()) {
//                        wordController = new Word(dtb);
                        wc.importWord(dtb);
                    } else {
                        dtb.moveToFirst();
                        wc.importWord(dtb);
//                        wordController = new Word(dtb);

                    }
                    System.out.println("ORIG UI");
                    flag = Boolean.TRUE;
//                    startTranslation(wordController);
                    origUI(wc);
                }
            }
        }
    };
    private View.OnClickListener backListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (dtb.moveToPrevious()) {
//                wordController = new Word(dtb);
                wc.importWord(dtb);
//                startTranslation(wordController);
//                finishTranslation(wordController);
                transUI(wc);
                flag = Boolean.FALSE;
            }
        }
    };



    private void origUI(WordController w) {
        layout.setBackgroundColor(wc.getColor());
        wordTxt.setText(w.getWordText());
        original.setText("");
    }
    public void transUI(WordController w) {
        layout.setBackgroundColor(Color.parseColor("#DB045B"));
        System.out.println("TARGET " + target);
        wordTxt.setText(w.getTranslated(target));
        original.setText("~ " + w.getWordText());
    }
//    public void startTranslation(Word w) {
//        translation = new Translator();
//        Language l = new Language(w, "de", target);
//        translation.execute(l);
//    }
//    public void finishTranslation(Word w) {
//        try {
//            w.setTranslated(translation.get());
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        wordController = w;
//    }


    @Override
    public void initGUI() {
        addFragment("wiki","b");
        addFragment("edit","c");
        addFragment("language","a");
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



        wc = new WordController();
        wc.importWord(dtb);
        System.out.println("colorize");
        layout.setBackgroundColor(wc.getColor());
        progressBar.setVisibility(View.INVISIBLE);
        wordTxt.setText(wc.getWordText());


    }

    public void addFragment(String type,String container) {
        int layoutID = 0;
        if(container.equals("a")){
            layoutID = R.id.containera;
        }else if(container.equals("b")){
            layoutID = R.id.containerb;
        }else if(container.equals("c")){
            layoutID = R.id.containerc;
        }

        if(type.equals("wiki")){
            WiktionaryBtn wik = new WiktionaryBtn();

            getChildFragmentManager().beginTransaction()
                    .replace(layoutID, wik)
                    .commit();
        }
        else if(type.equals("language")){
            FragmentLanguage fragmentLanguage = new FragmentLanguage();
            getChildFragmentManager().beginTransaction()
                    .replace(layoutID, fragmentLanguage)
                    .commit();
        }
        else if(type.equals("edit")){
            EditBtn editBtn = new EditBtn();
            getChildFragmentManager().beginTransaction()
                    .replace(layoutID, editBtn)
                    .commit();
        }







    }
    @Override
    public void onInputLanguage(CharSequence input) {
        if (!input.equals("Error")) {
            target = input.toString();
            System.out.println(target);
            transUI(wc);

//            startTranslation(wordController);
//            if (!flag) {
//                finishTranslation(wordController);
//                transUI(wordController);
//            }
        }
    }
}
