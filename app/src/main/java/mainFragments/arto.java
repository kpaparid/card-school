package mainFragments;

import android.graphics.Color;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.Word;

import java.util.ArrayList;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

public class arto extends templateFragment{


    ArrayList<Button> buttons;
    private Button der;
    private Button die;
    private Button das;
    private Integer index = 0;
    private TextView correctArticle;


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
    private ConstraintLayout articleContainer;


    @Override
    public int getLayoutID(){
        return R.layout.fr_article;
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

        correctArticle.setText(buttons.get(index).getText());
        correctArticle.setTextColor(RED);

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
    public void backGround(){
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        articleContainer = v.findViewById(R.id.articleContainer);
        initDB(" WHERE rate >= " + nfrom + " AND rate <= " + nto + " AND type = 'Nomen'"+ " ORDER BY RANDOM()");


    }
    @Override
    public void postExecute(){
        progressBar.setVisibility(View.INVISIBLE);
        articleContainer.setVisibility(View.VISIBLE);
        update();
    }


}
