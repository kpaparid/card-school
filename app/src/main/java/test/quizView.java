package test;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.WordController;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.Collections;

import fragments.EditBtn;
import fragments.FragmentLanguage;
import fragments.WiktionaryBtn;

import static android.graphics.Color.GREEN;

public class quizView extends Fragment implements FragmentLanguage.FragmentLanguageListener, WiktionaryBtn.NestedListener, EditBtn.NestedListener {

    public View v;
    public TextView wordTxt;
    public ConstraintLayout layout;
    public TextView original;
    public ProgressBar progressBar;
    public FragmentListener listener;
    private quizPresenter qP;
    ArrayList<WordController> words;
    ArrayList<Button> buttons;
    private TextView was;
    public Button btn1;
    public Button btn2;
    public Button btn3;
    public Button btn4;
    public String from;
    public String to;
    public String mode;
    private int index;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(getLayoutID(), container, false);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            from = getArguments().getString("nfrom");
            to = getArguments().getString("nto");
            mode =getArguments().getString("mode");
        }
        init();
        return v;
    }
    public int getLayoutID(){
        return R.layout.fr_quiz;
    }
    private View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {
            qP.click(v.getId());
        }
    };


    public void init(){

        qP = new quizPresenter(getContext(),this);
        initGUI();
        new initAsync().execute();

    }
    public void resetGUI() {
        btn1.setBackgroundColor(Color.parseColor("#AD7F2D"));
        btn2.setBackgroundColor(Color.parseColor("#AD7F2D"));
        btn3.setBackgroundColor(Color.parseColor("#AD7F2D"));
        btn4.setBackgroundColor(Color.parseColor("#AD7F2D"));
    }


    public void update(final WordController word, final WordController random1, final WordController random2, final WordController random3) {

        words = new ArrayList<>();
        words.add(word);
        words.add(random1);
        words.add(random2);
        words.add(random3);

        Collections.shuffle(words);
        index = words.indexOf(word);

        buttons = new ArrayList<Button>();
        buttons.add(btn1);
        buttons.add(btn2);
        buttons.add(btn3);
        buttons.add(btn4);

        anim(word);
    }

    private void anim(final WordController word) {

        ConstraintLayout transitionsContainer = v.findViewById(R.id.contentlayout);
        TransitionManager.beginDelayedTransition(transitionsContainer, new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
        btn1.setText(words.get(0).getTranslated(qP.getTarget()));
        btn2.setText(words.get(1).getTranslated(qP.getTarget()));
        btn3.setText(words.get(2).getTranslated(qP.getTarget()));
        btn4.setText(words.get(3).getTranslated(qP.getTarget()));
        wordTxt.setText(word.getWordText());
    }






    public void wrong(Button btn) {
        btn.setBackgroundColor(Color.RED);
        Button rightbtn = buttons.get(index);
        right(rightbtn);
    }
    public void right(Button btn) {
        btn.setBackgroundColor(GREEN);
    }
    public void backGround(){
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

    }
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





    public void onInputLanguage(final CharSequence input) {


            if (!input.equals("Error")) {
                qP.setTarget(input.toString());
                resetGUI();
                WordController[] currents = qP.getCurrents();
                update(currents[0],currents[1],currents[2],currents[3]);
            }

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (FragmentListener) context;
    }


    /**
     * click within inner fragment (edit, wiki)
     * chain trigger to main
     */
    @Override
    public void nestedListenerClicked(String mode) {
       // dtb.moveToPrevious();
        listener.onFragmentListener(qP.wc,mode);
    }

    /**
     * add fragment in container
     * @param type edit/wiki/language
     * @param container a/b/c
     */
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
    public void initGUI(){addFragment("wiki","b");
        addFragment("edit","c");
        addFragment("language","a");
        wordTxt = v.findViewById(R.id.word);
        wordTxt.setTextColor(Color.parseColor("#C69E4B"));
        was = v.findViewById(R.id.was);
        was.setTextColor(Color.parseColor("#8EAE95"));
        was.setVisibility(View.VISIBLE);

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

        layout = v.findViewById(R.id.mainlayout);

        layout.setBackgroundColor(Color.parseColor("#1F2018"));

        resetGUI();



    }

    public void setClickable(boolean bool) {
        btn1.setClickable(bool);
        btn2.setClickable(bool);
        btn3.setClickable(bool);
        btn4.setClickable(bool);
    }



    public interface FragmentListener {
        void onFragmentListener(WordController Word, String mode);
    }

    public class initAsync extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            backGround();
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            postExecute();
        }
    }


}
