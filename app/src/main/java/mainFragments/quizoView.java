package mainFragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.Language;
import com.example.marmi.cardschool.data.Word;
import com.example.marmi.cardschool.data.WordController;
import com.example.marmi.cardschool.data.WordModel;
import com.example.marmi.cardschool.normal.Translator;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import fragments.EditBtn;
import fragments.FragmentLanguage;
import fragments.WiktionaryBtn;

import static android.graphics.Color.GREEN;

public class quizoView extends templateView{

    private TextView wordTxt;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    quizoPresenter qP;


    public quizoView(){
         qP = new quizoPresenter(this,getContext());
    }

    private View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {
            qP.btnClick(v);
        }
    };

    @Override
    public void preExecute(){
        System.out.println("init db");
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        System.out.println("mpainw initdb");
        //qP.initDB(" WHERE rate >= " + nfrom + " AND rate <= " + nto +" "+ mode + " ORDER BY RANDOM()",getContext());

    }
    @Override
    public void postExecute(){
        System.out.println("init gui");
        initGUI();
        resetGUI();
        qP.initV();
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.VISIBLE);
        btn4.setVisibility(View.VISIBLE);
        wordTxt.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getLayoutID(){
        return R.layout.fr_quiz;
    }


    public TextView getWas() {
        return wordTxt;
    }

    public View getView() {
        return v;
    }

    public Button getBtn1() {
        return btn1;
    }

    public Button getBtn2() {
        return btn2;
    }

    public Button getBtn3() {
        return btn3;
    }

    public Button getBtn4() {
        return btn4;
    }
    @Override
    public void initGUI() {

        addFragment("wiki","b");
        addFragment("edit","c");
        addFragment("language","a");
        wordTxt = v.findViewById(R.id.word);
        wordTxt.setTextColor(Color.parseColor("#C69E4B"));
        wordTxt = v.findViewById(R.id.was);
        wordTxt.setTextColor(Color.parseColor("#8EAE95"));
        wordTxt.setVisibility(View.VISIBLE);

        btn1 = v.findViewById(R.id.btn1);
        btn2 = v.findViewById(R.id.btn2);
        btn3 = v.findViewById(R.id.btn3);
        btn4 = v.findViewById(R.id.btn4);


        btn1.setOnClickListener(btnListener);
        btn2.setOnClickListener(btnListener);
        btn3.setOnClickListener(btnListener);
        btn4.setOnClickListener(btnListener);


        ConstraintLayout backlayout = v.findViewById(R.id.background);
        backlayout.setBackgroundColor(Color.parseColor("#52444C"));

        ConstraintLayout layout = v.findViewById(R.id.mainlayout);

        layout.setBackgroundColor(Color.parseColor("#1F2018"));

        resetGUI();

    }
    public void resetGUI() {
        getBtn1().setBackgroundColor(Color.parseColor("#AD7F2D"));
        getBtn2().setBackgroundColor(Color.parseColor("#AD7F2D"));
        getBtn3().setBackgroundColor(Color.parseColor("#AD7F2D"));
        getBtn4().setBackgroundColor(Color.parseColor("#AD7F2D"));
    }
}
