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
import android.view.ViewParent;
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


    View v;
    public ProgressBar progressBar;
    private FragmentListener listener;

    public String from;
    public String to;
    public String mode;
    protected ConstraintLayout layout;
    public TextView wordTxt;
    protected TextView original;
    protected VoicePresenter cP;
    public Boolean Orig = true;
    protected WordController wc = null;
    protected ImageButton play;
    protected ImageButton pause;
    private View mainlayout;
    private View back;


    public void onDestroy() {
        System.out.println("Destroy");
        cP.destroy();
        super.onDestroy();

    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(getLayoutID(), container, false);
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null)
        {
            System.out.println("no null");
            wc = (WordController) savedInstanceState.getSerializable("wc");
            Orig = savedInstanceState.getBoolean("orig");
        }

        new init().execute();


        return v;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        System.out.println("onSave");
        wc = cP.getWordController();
        System.out.println(wc.getFullWordText());
        outState.putSerializable("wc", wc);
        outState.putBoolean("orig", Orig);
        super.onSaveInstanceState(outState);
    }
    private View.OnClickListener textListener = new View.OnClickListener() {
        public void onClick(View v) {
            cP.textClick();

        }
    };

    private View.OnClickListener playListener = new View.OnClickListener() {
        public void onClick(View v) {
            System.out.println("Click Play");
            cP.play = true;
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.INVISIBLE);
            cP.textClick();
            mainlayout.setClickable(false);
            back.setClickable(false);
        }
    };
    private View.OnClickListener pauseListener = new View.OnClickListener() {
        public void onClick(View v) {

            System.out.println("Click Pause");
            cP.play = false;
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.INVISIBLE);
            mainlayout.setClickable(true);
            back.setClickable(true);


        }
    };
    private View.OnClickListener backListener = new View.OnClickListener() {
        public void onClick(View v) {
            cP.backClick();

        }
    };
    public void origUI(WordController w) {
        layout.setBackgroundColor(w.getColor());
        wordTxt.setText(w.getFullWordText());
        System.out.println("w: "+wordTxt.getText().toString());
        original.setText("");
    }
    public void transUI(WordController w,String target) {
        layout.setBackgroundColor(Color.parseColor("#DB045B"));
        wordTxt.setText(w.getTranslated(target));
        original.setText("~ " + w.getFullWordText());

    }

    public int getLayoutID(){
        return R.layout.fr_card;
    }
    public void initGUI() {
        mainlayout = v.findViewById(R.id.mainlayout);
        back = v.findViewById(R.id.back);
        mainlayout.setOnClickListener(textListener);
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
    public void preExecute(){
        preInitGUI();
        cP = new VoicePresenter(getContext(),this, wc);
    }
    public void postExecute(){
        addFragment("wiki","b");
        addFragment("edit","c");
        addFragment("language","a");
        initGUI();
        if(Orig){
            System.out.println("Orig post");
            System.out.println(cP.getWordController().getFullWordText()+":text");
            origUI(cP.getWordController());
        }else {

            System.out.println("Trans post");
            transUI(cP.getWordController(), cP.getTarget());
        }

    }
    public WordController getWord(){return cP.getWordController();}

    public void preInitGUI(){
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void nestedListenerClicked(String mode) {

        wc = cP.getWordController();
        listener.onFragmentListener(wc.getWordModel(),mode);
    }
    public interface FragmentListener {
        void onFragmentListener(WordModel Word, String mode);
        void initDataBase(String query);

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

    public class init extends AsyncTask<String, String, String> {
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


    @Override
    public void onInputLanguage(CharSequence input) {
        if (!input.equals("Error")) {
            String target = input.toString();
            System.out.println(target);

            if(Orig){
                origUI(cP.getWordController());
            }
            else {
                transUI(cP.getWordController(),target);
            }
            cP.setTarget(target);
        }
    }










}
