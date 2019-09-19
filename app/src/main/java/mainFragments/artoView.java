package mainFragments;

import android.graphics.Color;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.WordController;

import java.util.ArrayList;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

public class artoView extends templateView {


    ArrayList<Button> buttons;
    private Button der;
    private Button die;
    private Button das;
    private TextView correctArticle;
    private TextView wordTxt;
    private ConstraintLayout layout;
    private artoPresenter aP;

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
                    if (!aP.moveNextdtb()) {
                        aP.moveFirstdtb();
                    }
                    update();
                }
            };
            handler.postDelayed(r, 1200);
        }
    };
    private ConstraintLayout articleContainer;


    @Override
    public int getLayoutID(){
        return R.layout.fr_article;
    }

    private void update() {
        aP.importNextWord();
        wordTxt.setText(aP.getWord().getWordText().substring(4));
    }

    @Override
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
        if (text.equalsIgnoreCase(aP.getWord().getArticle())) {
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
            aP.setTarget(input.toString());
        }
    }




    @Override
    public void preExecute(){


        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        articleContainer = v.findViewById(R.id.articleContainer);
        aP = new artoPresenter(dtb);
        initDB(" WHERE rate >= " + from + " AND rate <= " + to + " AND type = 'Nomen'"+ " ORDER BY RANDOM()");


    }
    @Override
    public void postExecute(){
        initGUI();
        progressBar.setVisibility(View.INVISIBLE);
        articleContainer.setVisibility(View.VISIBLE);
        update();
    }
    public WordController getWord(){return aP.getWord();}


}
