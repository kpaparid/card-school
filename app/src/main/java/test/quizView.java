package test;

import static android.graphics.Color.GREEN;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.WordController;
import com.example.marmi.cardschool.data.WordModel;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.Collections;

import fragments.EditBtn;
import fragments.FragmentLanguage;
import fragments.WiktionaryBtn;

public class QuizView extends Fragment implements FragmentLanguage.FragmentLanguageListener, WiktionaryBtn.NestedListener, EditBtn.NestedListener {

    public View v;
    public TextView wordTxt;
    public ConstraintLayout layout;
    public TextView original;
    public ProgressBar progressBar;
    public FragmentListener listener;
    public Button btn1;
    public Button btn2;
    public Button btn3;
    public Button btn4;
    public String from;
    public String to;
    public String mode;
    ArrayList<WordModel> words;
    ArrayList<Button> buttons;
    WordModel[] randoms = null;
    WordController wc = null;
    Bundle savedInstanceState;
    private QuizPresenter qP;
    private int index;
    private final View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {
            qP.click(v.getId());
        }
    };

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle sV) {
        v = inflater.inflate(getLayoutID(), container, false);
        this.savedInstanceState = sV;
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            randoms = new WordModel[3];
            wc = (WordController) savedInstanceState.getSerializable("wc0");
            randoms[0] = (WordModel) savedInstanceState.getSerializable("r0");
            randoms[1] = (WordModel) savedInstanceState.getSerializable("r1");
            randoms[2] = (WordModel) savedInstanceState.getSerializable("r2");

        }
        init();
        return v;
    }

    public void init() {
        qP = new QuizPresenter(getContext(), this, randoms, wc);
        initGUI();
        new InitAsync().execute();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        randoms = qP.getRandoms();
        outState.putSerializable("wc0", qP.getWordController());
        outState.putSerializable("r0", randoms[0]);
        outState.putSerializable("r1", randoms[1]);
        outState.putSerializable("r2", randoms[2]);
        super.onSaveInstanceState(outState);
    }

    public int getLayoutID() {
        return R.layout.fr_quiz;
    }

    public void resetGUI() {
        btn1.setBackgroundColor(Color.parseColor("#AD7F2D"));
        btn2.setBackgroundColor(Color.parseColor("#AD7F2D"));
        btn3.setBackgroundColor(Color.parseColor("#AD7F2D"));
        btn4.setBackgroundColor(Color.parseColor("#AD7F2D"));
    }


    public void update(final WordModel word, final WordModel random1, final WordModel random2, final WordModel random3) {

        words = new ArrayList<>();
        words.add(word);
        words.add(random1);
        words.add(random2);
        words.add(random3);

        Collections.shuffle(words);
        index = words.indexOf(word);

        buttons = new ArrayList<>();
        buttons.add(btn1);
        buttons.add(btn2);
        buttons.add(btn3);
        buttons.add(btn4);
        Log.e("", word.getEn_translated());
        Log.e("", random1.getEn_translated());
        Log.e("", random2.getEn_translated());
        Log.e("", random3.getEn_translated());
        anim();
        btn1.setText(words.get(0).getTranslated(qP.getTarget()));
        btn2.setText(words.get(1).getTranslated(qP.getTarget()));
        btn3.setText(words.get(2).getTranslated(qP.getTarget()));
        btn4.setText(words.get(3).getTranslated(qP.getTarget()));
        wordTxt.setText(word.getFullWordText());
    }

    private void anim() {

        ConstraintLayout transitionsContainer = v.findViewById(R.id.contentlayout);
        TransitionManager.beginDelayedTransition(transitionsContainer, new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
    }

    public void wrong(Button btn) {
        btn.setBackgroundColor(Color.RED);
        Button rightBtn = buttons.get(index);
        right(rightBtn);
    }

    public void right(Button btn) {
        btn.setBackgroundColor(GREEN);
    }

    public void backGround() {
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

    }

    public void postExecute() {
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
            WordModel[] currents = qP.getCurrents();
            update(currents[0], currents[1], currents[2], currents[3]);
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


        wc = qP.getWordController();
        listener.onFragmentListener(qP.getWordController().getWordModel(), mode);


    }

    /**
     * add fragment in container
     *
     * @param type      edit/wiki/language
     * @param container a/b/c
     */
    public void addFragment(String type, String container) {
        int layoutID = 0;
        switch (container) {
            case "a":
                layoutID = R.id.containera;
                break;
            case "b":
                layoutID = R.id.containerb;
                break;
            case "c":
                layoutID = R.id.containerc;
                break;
        }

        switch (type) {
            case "wiki":
                WiktionaryBtn wik = new WiktionaryBtn();

                getChildFragmentManager().beginTransaction()
                        .replace(layoutID, wik)
                        .commit();
                break;
            case "language":
                FragmentLanguage fragmentLanguage = new FragmentLanguage();
                getChildFragmentManager().beginTransaction()
                        .replace(layoutID, fragmentLanguage)
                        .commit();
                break;
            case "edit":
                EditBtn editBtn = new EditBtn();
                getChildFragmentManager().beginTransaction()
                        .replace(layoutID, editBtn)
                        .commit();
                break;
        }


    }

    public void initGUI() {
        addFragment("wiki", "b");
        addFragment("edit", "c");
        addFragment("language", "a");
        wordTxt = v.findViewById(R.id.word);
        wordTxt.setTextColor(Color.parseColor("#C69E4B"));
        TextView was = v.findViewById(R.id.was);
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


        ConstraintLayout backLayout = v.findViewById(R.id.background);
        backLayout.setBackgroundColor(Color.parseColor("#52444C"));

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
        void onFragmentListener(WordModel Word, String mode);
    }

    public class InitAsync extends AsyncTask<String, String, String> {
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
