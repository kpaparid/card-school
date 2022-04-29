package test;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.WordController;
import com.example.marmi.cardschool.data.WordModel;

import fragments.EditBtn;
import fragments.FragmentLanguage;
import fragments.WiktionaryBtn;

public class VoiceView extends Fragment implements FragmentLanguage.FragmentLanguageListener, WiktionaryBtn.NestedListener, EditBtn.NestedListener {


    public ProgressBar progressBar;
    public String from;
    public String to;
    public String mode;
    public TextView wordTxt;
    public Boolean Orig = true;
    protected ConstraintLayout layout;
    protected TextView original;
    protected VoicePresenter cP;
    protected WordController wc = null;
    protected ImageButton play;
    protected ImageButton pause;
    View v;
    private FragmentListener listener;
    private View mainLayout;
    private View back;
    private final View.OnClickListener textListener = new View.OnClickListener() {
        public void onClick(View v) {
            cP.textClick();

        }
    };
    private final View.OnClickListener playListener = new View.OnClickListener() {
        public void onClick(View v) {
            cP.play = true;
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.INVISIBLE);
            cP.textClick();
            mainLayout.setClickable(false);
            back.setClickable(false);
        }
    };
    private final View.OnClickListener pauseListener = new View.OnClickListener() {
        public void onClick(View v) {
            cP.play = false;
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.INVISIBLE);
            mainLayout.setClickable(true);
            back.setClickable(true);


        }
    };
    private final View.OnClickListener backListener = new View.OnClickListener() {
        public void onClick(View v) {
            cP.backClick();

        }
    };

    public void onDestroy() {
        cP.destroy();
        super.onDestroy();

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(getLayoutID(), container, false);
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            wc = (WordController) savedInstanceState.getSerializable("wc");
            Orig = savedInstanceState.getBoolean("orig");
        }

        new Init().execute();


        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        wc = cP.getWordController();
        outState.putSerializable("wc", wc);
        outState.putBoolean("orig", Orig);
        super.onSaveInstanceState(outState);
    }

    public void origUI(WordController w) {
        layout.setBackgroundColor(w.getColor());
        wordTxt.setText(w.getFullWordText());
        original.setText("");
    }

    public void transUI(WordController w, String target) {
        layout.setBackgroundColor(Color.parseColor("#DB045B"));
        wordTxt.setText(w.getTranslated(target));
        original.setText("~ " + w.getFullWordText());

    }

    public int getLayoutID() {
        return R.layout.fr_card;
    }

    public void initGUI() {
        mainLayout = v.findViewById(R.id.mainlayout);
        back = v.findViewById(R.id.back);
        mainLayout.setOnClickListener(textListener);
        back.setOnClickListener(backListener);
        layout = v.findViewById(R.id.layout);
        wordTxt = v.findViewById(R.id.word);
        original = v.findViewById(R.id.original);
        original.setText("");
        progressBar.setVisibility(View.INVISIBLE);
        play = v.findViewById(R.id.play);
        pause = v.findViewById(R.id.pause);
        play.setOnClickListener(playListener);
        pause.setOnClickListener(pauseListener);

    }

    public void preExecute() {
        preInitGUI();
        cP = new VoicePresenter(getContext(), this);
    }

    public void postExecute() {
        addFragment("wiki", "b");
        addFragment("edit", "c");
        addFragment("language", "a");
        initGUI();
        if (Orig) {
            origUI(cP.getWordController());
        } else {
            transUI(cP.getWordController(), cP.getTarget());
        }

    }

    public WordController getWord() {
        return cP.getWordController();
    }

    public void preInitGUI() {
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void nestedListenerClicked(String mode) {

        wc = cP.getWordController();
        listener.onFragmentListener(wc.getWordModel(), mode);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (FragmentListener) context;
    }

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

    @Override
    public void onInputLanguage(CharSequence input) {
        if (!input.equals("Error")) {
            String target = input.toString();
            if (Orig) {
                origUI(cP.getWordController());
            } else {
                transUI(cP.getWordController(), target);
            }
            cP.setTarget(target);
        }
    }

    public interface FragmentListener {
        void onFragmentListener(WordModel Word, String mode);

        void initDataBase(String query);

    }

    public class Init extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {


            preExecute();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            postExecute();
        }
    }


}
