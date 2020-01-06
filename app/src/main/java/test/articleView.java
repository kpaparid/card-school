package test;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
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
import com.example.marmi.cardschool.data.WordController;
import com.example.marmi.cardschool.data.WordModel;

import java.util.ArrayList;

import fragments.EditBtn;
import fragments.FragmentLanguage;
import fragments.WiktionaryBtn;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

public class articleView extends Fragment implements FragmentLanguage.FragmentLanguageListener, WiktionaryBtn.NestedListener, EditBtn.NestedListener {


    View v;
    public ProgressBar progressBar;
    private FragmentListener listener;

    public String from;
    public String to;
    public String mode;
    ArrayList<Button> buttons;
    private Button der;
    private Button die;
    private Button das;
    private TextView correctArticle;
    private TextView wordTxt;
    private ConstraintLayout layout;
    private WordController wc = null;
    private articlePresenter aP;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(getLayoutID(), container, false);
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null)
        {
            System.out.println("no null");
            wc = (WordController) savedInstanceState.getSerializable("wc");
        }

        aP = new articlePresenter(getContext(),this,wc);
        initGUI();
        update();


        return v;
}
    @Override
    public void onSaveInstanceState(Bundle outState) {
        System.out.println("onSave");
        wc = aP.getWordController();
        outState.putSerializable("wc", wc);
        super.onSaveInstanceState(outState);
    }


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
            final Handler handler = new Handler();
            final Runnable r = new Runnable() {
                public void run() {
                    resetGUI();
                    aP.getWordController().moveToNext();
                    update();
                }
            };
            handler.postDelayed(r, 1200);
        }
    };
    private ConstraintLayout articleContainer;

    private void update() {

        wordTxt.setText(aP.getWordController().getFullWordText().substring(4));
    }






    private void wrong(Button btn) {
        System.out.println("WRONG");
        btn.setBackgroundColor(Color.RED);
        correctArticle.setVisibility(View.VISIBLE);
        correctArticle.setVisibility(View.VISIBLE);
        correctArticle.setText(buttons.get(aP.getIndex()).getText());
        correctArticle.setTextColor(RED);
    }

    private void right(Button btn) {
        btn.setBackgroundColor(GREEN);
        correctArticle.setVisibility(View.VISIBLE);
        correctArticle.setText(buttons.get(aP.getIndex()).getText());
        correctArticle.setTextColor(GREEN);
    }

    private void control(Button btn) {
        String text = btn.getText().toString();
        if (text.equalsIgnoreCase(aP.getWordController().getArticle())) {
            right(btn);
        } else {
            wrong(btn);
        }
        der.setVisibility(View.INVISIBLE);
        die.setVisibility(View.INVISIBLE);
        das.setVisibility(View.INVISIBLE);
    }


























    public void initGUI() {
        addFragment("wiki","a");
        addFragment("edit","b");


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

        layout = v.findViewById(R.id.mainlayout);
        layout.setBackgroundColor(Color.parseColor("#3C5C61"));

        buttons = new ArrayList<>();
        buttons.add(der);
        buttons.add(die);
        buttons.add(das);
        articleContainer = v.findViewById(R.id.articleContainer);
        articleContainer.setVisibility(View.VISIBLE);


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

    @Override
    public void onInputLanguage(CharSequence input) {
        if (!input.equals("Error")) {
            aP.setTarget(input.toString());
        }
    }
    public int getLayoutID(){
        return R.layout.fr_article;
    }
    public void preInitGUI(){
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

    }


    @Override
    public void nestedListenerClicked(String mode) {

        listener.onFragmentListener(aP.getWordController().getWordModel(),mode);
    }
    public interface FragmentListener {
        void onFragmentListener(WordModel Word, String mode);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (FragmentListener) context;
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















}
