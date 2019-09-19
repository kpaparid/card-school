package mainFragments;

import android.database.Cursor;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.WordController;

public class cardoView extends templateView {




    private ConstraintLayout layout;
    private TextView wordTxt;
    private TextView original;

    private cardoPresenter cP;



    private View.OnClickListener textListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(cP.orig()){
                origUI(cP.getWord());
            }else {
                transUI(cP.getWord(),cP.getTarget());
            }
        }
    };
    private View.OnClickListener backListener = new View.OnClickListener() {
        public void onClick(View v) {
            cP.backClick();
            transUI(cP.getWord(),cP.getTarget());
        }
    };


    public void origUI(WordController w) {
        layout.setBackgroundColor(w.getColor());
        wordTxt.setText(w.getWordText());
        original.setText("");
    }
    public void transUI(WordController w,String target) {
        layout.setBackgroundColor(Color.parseColor("#DB045B"));
        System.out.println("TARGET " + target);
        wordTxt.setText(w.getTranslated(target));
        original.setText("~ " + w.getWordText());
    }

    @Override
    public int getLayoutID(){
        return R.layout.fr_card;
    }
    @Override
    public void initGUI() {
        ConstraintLayout mainlayout = v.findViewById(R.id.mainlayout);
        ConstraintLayout back = v.findViewById(R.id.back);
        mainlayout.setOnClickListener(textListener);
        back.setOnClickListener(backListener);
        layout = v.findViewById(R.id.layout);
        wordTxt = v.findViewById(R.id.word);
        original = v.findViewById(R.id.original);
        original.setText("");
        progressBar.setVisibility(View.INVISIBLE);
    }
    public void setMainWord(String text, int color){
        layout.setBackgroundColor(color);
        wordTxt.setText(text);
    }

    @Override
    public void onInputLanguage(CharSequence input) {
        if (!input.equals("Error")) {
            String target = input.toString();
            System.out.println(target);

            if(cP.getBooleanNormal()){
                origUI(cP.getWord());
            }
            else {
                transUI(cP.getWord(),target);
            }
            cP.setTarget(target);
        }
    }

    @Override
    public void preExecute(){
        preInitGUI();
        initDB(" WHERE rate >= " + from + " AND rate <= " + to +" "+ mode + " ORDER BY RANDOM()");
        cP = new cardoPresenter(dtb);
        System.out.println("pre execute " +dtb.getString(3));

    }
    @Override
    public void postExecute(){
        addFragment("wiki","b");
        addFragment("edit","c");
        addFragment("language","a");
        initGUI();
        setMainWord(cP.getWord().getWordText(), cP.getWord().getColor());
    }
    @Override
    public WordController getWord(){return cP.getWord();}














}
