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

public class CardFragment extends Fragment implements FragmentLanguage.FragmentLanguageListener, WiktionaryBtn.NestedListener, EditBtn.NestedListener {

    public Word word;
    public Translator translation;
    public String target = "en";
    Activity context;
    View v;
    String nfrom;
    String nto;
    private ConstraintLayout layout;
    private ConstraintLayout mainlayout;
    private ConstraintLayout back;
    private TextView wordTxt;
    private TextView original;
    private Boolean end = Boolean.FALSE;
    private Boolean flag = Boolean.TRUE;
    private Spinner spinner;
    private Button btn;
    private Cursor dtb;
    String mode;

    private FragmentListener listener;
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
                        //end = true;
                        dtb.moveToFirst();
                        word = new Word(dtb);

                    }
                    System.out.println("ORIG UI");
                    flag = Boolean.TRUE;
                    startTranslation(word);
                    origUI(word);
                }
            } else {
                //db.setText("THE END");
            }
        }
    };
    private View.OnClickListener backListener = new View.OnClickListener() {
        public void onClick(View v) {
            System.out.println("BACKKKKK");
            if (dtb.moveToPrevious()) {
                word = new Word(dtb);
                startTranslation(word);
                finishTranslation(word);
                transUI(word);
                flag = Boolean.FALSE;
            }
        }
    };

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fr_card, container, false);
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            nfrom = getArguments().getString("nfrom");
            nto = getArguments().getString("nto");
            mode =getArguments().getString("mode");


        }

        initGUI();
        new init().execute();
        return v;

    }


    private void origUI(Word w) {



        layout.setBackgroundColor(word.getColor());
        wordTxt.setText(w.getWordText());
        original.setText("");
    }

    private void transUI(Word w) {
        layout.setBackgroundColor(Color.parseColor("#DB045B"));

        System.out.println("TARGET " + target);
        wordTxt.setText(w.getTranslated(target));

        original.setText("~ " + w.getWordText());
    }

    private void startTranslation(Word w) {
        translation = new Translator();
        Language l = new Language(w, "de", target);
        translation.execute(l);
    }

    private void finishTranslation(Word w) {
        try {
            w.setTranslated(translation.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        word = w;
    }

    private void initGUI() {


        addFragment();
        wordTxt = v.findViewById(R.id.word);
        mainlayout = v.findViewById(R.id.mainlayout);
        layout = v.findViewById(R.id.layout);
        back = v.findViewById(R.id.back);
        original = v.findViewById(R.id.original);

        mainlayout.setOnClickListener(textListener);

        back.setOnClickListener(backListener);


        original.setText("");
    }


    void initDB() {
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(getContext());
        String query = " WHERE rate >= " + nfrom + " AND rate <= " + nto +" "+ mode + " ORDER BY RANDOM()";
        dtb = mDatabaseHelper.getData(query);
       if(dtb==null){
           System.out.println("Reading Database cause Null");
           mDatabaseHelper.readData(mDatabaseHelper, getContext());
           dtb = mDatabaseHelper.getData(query);
        }
        mDatabaseHelper.close();
        System.out.println("init db");

    }


    @Override
    public void onInputLanguage(CharSequence input) {

        if (!input.equals("Error")) {
            System.out.println("TRIGGER CHANGE LANGUAGE");

            target = input.toString();
            System.out.println(target);
            startTranslation(word);
            if (!flag) {
                finishTranslation(word);
                transUI(word);

            } else {

            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (FragmentListener) context;

    }

    @Override
    public void nestedListenerClicked(String mode) {
        listener.onFragmentListener(word,mode);

    }

    private void addFragment() {


        WiktionaryBtn wik = new WiktionaryBtn();

        getChildFragmentManager().beginTransaction()
                .replace(R.id.btnW, wik)
                .commit();

        FragmentLanguage fragmentLanguage = new FragmentLanguage();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.containerb, fragmentLanguage)
                .commit();

        EditBtn editBtn = new EditBtn();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.containerc, editBtn)
                .commit();




    }

    public interface FragmentListener {
        void onFragmentListener(Word Word, String mode);
    }

    public class init extends AsyncTask<String, String, String> {

        private ProgressBar progressBar;

        @Override
        protected String doInBackground(String... params) {

            progressBar = v.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            initDB();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            word = new Word(dtb);
            System.out.println("colorize");
            layout.setBackgroundColor(word.getColor());
            progressBar.setVisibility(View.INVISIBLE);
            wordTxt.setText(word.getWordText());

            startTranslation(word);
        }
    }
}
