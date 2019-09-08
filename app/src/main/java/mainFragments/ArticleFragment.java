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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.Word;
import com.example.marmi.cardschool.normal.Translator;

import java.util.ArrayList;

import fragments.EditBtn;
import fragments.FragmentLanguage;
import fragments.WiktionaryBtn;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

public class ArticleFragment extends Fragment implements FragmentLanguage.FragmentLanguageListener, WiktionaryBtn.NestedListener, EditBtn.NestedListener  {


    public Translator translation;
    ArrayList<Button> buttons;
    private String nfrom;
    private String nto;
    View v;
    private Button der;
    private Button die;
    private Button das;
    private String target = "en";
    private TextView wordTxt;
    private DatabaseHelper mDatabaseHelper;
    private Cursor dtb;
    private Integer index = 0;
    private TextView correctArticle;
    private FragmentLanguage fragmentLanguage;
    private Word word;
    private FragmentListener listener;
    private String mode;
    private View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View v)
        {
            switch (v.getId()) {
                case R.id.der: {
                    control(der);
                    break;
                }

                case R.id.die: {
                    control(die);
                    break;
                }


                case R.id.das: {
                    control(das);
                    break;
                }


            }
            der.setClickable(false);
            die.setClickable(false);
            das.setClickable(false);
            //next.setVisibility(View.VISIBLE);


            final Handler handler = new Handler();
            final Runnable r = new Runnable() {
                public void run() {
                    resetGUI();
                    if (!dtb.moveToNext()) {
                        dtb.moveToFirst();
                    }
                    update();
                }
            };
            handler.postDelayed(r, 1200);
        }
    };

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fr_article, container, false);
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            nfrom = getArguments().getString("nfrom");
            nto = getArguments().getString("nto");
            mode =getArguments().getString("mode");
        }

        initGUI();
        init();
        new init().execute();

        return v;
    }

    private void update() {
        word = new Word(dtb);
        wordTxt.setText(word.getWordText().substring(4));
        switch (word.getArticle()) {
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

    }

    private void init() {

        buttons = new ArrayList<>();
        buttons.add(der);
        buttons.add(die);
        buttons.add(das);


    }

    private void initDB() {
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(getContext());
        System.out.println("nfrom "+nfrom);
        System.out.println("nto "+nto);
        String query = " WHERE rate >= " + nfrom + " AND rate <= " + nto + " AND type = 'Nomen'"+ " ORDER BY RANDOM()";
        dtb = mDatabaseHelper.getData(query);
        if(dtb==null){
            System.out.println("Reading Database cause Null");
            mDatabaseHelper.readData(mDatabaseHelper, getContext());
            dtb = mDatabaseHelper.getData(query);
        }
        mDatabaseHelper.close();
        System.out.println("init db");
    }

    private void initGUI() {

        addFragment();

        der = v.findViewById(R.id.der);
        die = v.findViewById(R.id.die);
        das = v.findViewById(R.id.das);
        wordTxt = v.findViewById(R.id.word);
        correctArticle = v.findViewById(R.id.correctArticle);


        der.setTextColor(Color.parseColor("#3C5C61"));
        die.setTextColor(Color.parseColor("#3C5C61"));
        das.setTextColor(Color.parseColor("#3C5C61"));
        wordTxt.setTextColor(Color.parseColor("#FBAC44"));

        der.setBackgroundColor(Color.parseColor("#FBAC44"));
        die.setBackgroundColor(Color.parseColor("#FBAC44"));
        das.setBackgroundColor(Color.parseColor("#FBAC44"));

        der.setOnClickListener(buttonListener);
        die.setOnClickListener(buttonListener);
        das.setOnClickListener(buttonListener);

        ConstraintLayout backlayout = v.findViewById(R.id.background);
        backlayout.setBackgroundColor(Color.parseColor("#0E2F3C"));

        ConstraintLayout layout = v.findViewById(R.id.mainlayout);
        layout.setBackgroundColor(Color.parseColor("#3C5C61"));


    }

    private void resetGUI() {
        der.setBackgroundColor(Color.parseColor("#FBAC44"));
        die.setBackgroundColor(Color.parseColor("#FBAC44"));
        das.setBackgroundColor(Color.parseColor("#FBAC44"));

        der.setClickable(true);
        die.setClickable(true);
        das.setClickable(true);



        der.setVisibility(View.VISIBLE);
        die.setVisibility(View.VISIBLE);
        das.setVisibility(View.VISIBLE);
        correctArticle.setVisibility(View.INVISIBLE);

    }


    private void wrong(Button btn) {
        System.out.println("WRONG");
        btn.setBackgroundColor(Color.RED);
        Button rightbtn = buttons.get(index);


        correctArticle.setVisibility(View.VISIBLE);

        correctArticle.setVisibility(View.VISIBLE);
        correctArticle.setText(buttons.get(index).getText());
        correctArticle.setTextColor(RED);

        //right(rightbtn);
    }

    private void right(Button btn) {
        btn.setBackgroundColor(GREEN);
        correctArticle.setVisibility(View.VISIBLE);
        correctArticle.setText(buttons.get(index).getText());
        correctArticle.setTextColor(GREEN);


    }

    private void control(Button btn) {
        String text = btn.getText().toString();
        if (text.equalsIgnoreCase(word.getArticle())) {
            right(btn);
        } else {
            wrong(btn);
        }
        der.setVisibility(View.INVISIBLE);
        die.setVisibility(View.INVISIBLE);
        das.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onInputLanguage(CharSequence input) {
        if (!input.equals("Error")) {
            target = input.toString();
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (FragmentListener) context;


    }

    @Override
    public void nestedListenerClicked(String mode) {
        listener.onFragmentListener(word, mode);

    }

    private void addFragment() {

        fragmentLanguage = new FragmentLanguage();
        WiktionaryBtn wik = new WiktionaryBtn();

        getChildFragmentManager().beginTransaction()
                .replace(R.id.btnW, wik)
                .commit();


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

        private ConstraintLayout articleContainer;
        private ProgressBar progressBar;
        @Override
        protected String doInBackground(String... params) {

            progressBar = v.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            articleContainer = v.findViewById(R.id.articleContainer);
            initDB();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            progressBar.setVisibility(View.INVISIBLE);
            articleContainer.setVisibility(View.VISIBLE);
            update();
            //wordTxt.setText(s);
        }
    }


}
