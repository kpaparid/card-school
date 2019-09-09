package mainFragments;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.Language;
import com.example.marmi.cardschool.data.Word;
import com.example.marmi.cardschool.normal.Translator;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import static android.graphics.Color.GREEN;

public class quizo extends templateFragment{
    ArrayList<Word> words;
    ArrayList<Button> buttons;
    private Translator tr0;
    private Translator tr1;
    private Translator tr2;
    private Translator tr3;
    private TextView was;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private int index;
    private Word nextWord;
    private Word currentRandom1;
    private Word currentRandom2;
    private Word currentRandom3;
    private Word nextRandom1;
    private Word nextRandom2;
    private Word nextRandom3;
    private DatabaseHelper mDatabaseHelper;

    private View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn1: {
                    control(btn1);
                    break;
                }

                case R.id.btn2: {
                    control(btn2);
                    break;
                }


                case R.id.btn3: {
                    control(btn3);
                    break;
                }
                case R.id.btn4: {
                    control(btn4);
                    break;
                }

            }

            System.out.println("Post Execute");
            word = nextWord;
            currentRandom1 = nextRandom1;
            currentRandom2 = nextRandom2;
            currentRandom3 = nextRandom3;
            finishTrans(word, currentRandom1, currentRandom2, currentRandom3);

            btn1.setClickable(false);
            btn2.setClickable(false);
            btn3.setClickable(false);
            btn4.setClickable(false);
            final Runnable r = new Runnable() {
                public void run() {
                    if (!dtb.moveToNext()) {
                        dtb.moveToFirst();
                    }

                    resetGUI();
                    update(word, currentRandom1, currentRandom2, currentRandom3);
                    nextWord = new Word(dtb);
                    randomGenerator(nextWord.getType());
                    startTrans(nextWord, nextRandom1, nextRandom2, nextRandom3);
                    btn1.setClickable(true);
                    btn2.setClickable(true);
                    btn3.setClickable(true);
                    btn4.setClickable(true);
                }
            };


            handler.postDelayed(r, 1700);


        }
    };

    @Override
    public int getLayoutID(){
        return R.layout.fr_quiz;
    }

    private void initV() {

        if(dtb == null){
            System.out.println("wtf InitV");
        }
        word = new Word(dtb);

        randomGenerator(word.getType());
        nextWord = word;
        currentRandom1 = nextRandom1;
        currentRandom2 = nextRandom2;
        currentRandom3 = nextRandom3;
        startTrans(word, currentRandom1, currentRandom2, currentRandom3);
        finishTrans(word, currentRandom1, currentRandom2, currentRandom3);
        update(word, currentRandom1, currentRandom2, currentRandom3);


        if (!dtb.moveToNext()) {
            dtb.moveToFirst();
        }

        nextWord = new Word(dtb);
        randomGenerator(nextWord.getType());
        startTrans(nextWord, nextRandom1, nextRandom2, nextRandom3);
        System.out.println("Finish InitV");
    }

    private void randomGenerator(String type) {
        String query = " WHERE rate >= " + 0 + " AND rate <= " + 10 + " AND type = '" + type + "' ORDER BY RANDOM() LIMIT 3";
        mDatabaseHelper = new DatabaseHelper(getContext());
        Cursor randomWords = mDatabaseHelper.getRandom(3, query);
        nextRandom1 = new Word(randomWords);
        randomWords.moveToNext();
        nextRandom2 = new Word(randomWords);
        randomWords.moveToNext();
        nextRandom3 = new Word(randomWords);
        randomWords.moveToNext();
        mDatabaseHelper.close();

    }

    private void startTrans(final Word word, final Word random1, final Word random2, final Word random3) {

        tr0 = new Translator();
        tr1 = new Translator();
        tr2 = new Translator();
        tr3 = new Translator();

        startTranslation(word, tr0);
        startTranslation(random1, tr1);
        startTranslation(random2, tr2);
        startTranslation(random3, tr3);


    }

    private void finishTrans(final Word word, final Word random1, final Word random2, final Word random3) {
        this.word = finishTranslation(word, tr0);
        currentRandom1 = finishTranslation(random1, tr1);
        currentRandom2 = finishTranslation(random2, tr2);
        currentRandom3 = finishTranslation(random3, tr3);

    }

    private void startTranslation(Word w, Translator translator) {

        Language l = new Language(w, "de", target);
        translator.execute(l);


    }

    private Word finishTranslation(Word w, Translator translator) {

        try {
            w.setTranslated(translator.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return w;
    }

    private void wrong(Button btn) {
        btn.setBackgroundColor(Color.RED);
        Button rightbtn = buttons.get(index);
        right(rightbtn);
    }

    private void right(Button btn) {
        btn.setBackgroundColor(GREEN);
    }

    private void control(Button btn) {
        String text = btn.getText().toString();
        if (text == word.getTranslated(target)) {
            right(btn);
        } else {
            wrong(btn);
        }
    }

    @Override
    public void onInputLanguage(final CharSequence input) {

        if (!input.equals("Error")) {
            System.out.println("TRIGGER Error");
            target = input.toString();
            startTrans(word, currentRandom1, currentRandom2, currentRandom3);
            finishTrans(word, currentRandom1, currentRandom2, currentRandom3);
            System.out.println("translatiooon\t" + word.getTranslated(target));
            resetGUI();
            update(word, currentRandom1, currentRandom2, currentRandom3);
            startTrans(nextWord, nextRandom1, nextRandom2, nextRandom3);
        }
    }

    private void resetGUI() {
        btn1.setBackgroundColor(Color.parseColor("#AD7F2D"));
        btn2.setBackgroundColor(Color.parseColor("#AD7F2D"));
        btn3.setBackgroundColor(Color.parseColor("#AD7F2D"));
        btn4.setBackgroundColor(Color.parseColor("#AD7F2D"));
    }


    public void update(final Word word, final Word random1, final Word random2, final Word random3) {

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

        anim(word,random1,random2,random3);
    }

    private void anim(final Word word, final Word random1, final Word random2, final Word random3) {

        ConstraintLayout transitionsContainer = v.findViewById(R.id.contentlayout);
        TransitionManager.beginDelayedTransition(transitionsContainer,
                new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
        btn1.setText(words.get(0).getTranslated(target));
        btn2.setText(words.get(1).getTranslated(target));
        btn3.setText(words.get(2).getTranslated(target));
        btn4.setText(words.get(3).getTranslated(target));


        wordTxt.setText(word.getWordText());
    }


    @Override
    public void initGUI() {

        addFragment("wiki","a");
        addFragment("edit","c");
        addFragment("language","b");
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
    @Override
    public void backGround(){

        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        initDB(" WHERE rate >= " + nfrom + " AND rate <= " + nto +" "+ mode + " ORDER BY RANDOM()");
    }
    @Override
    public void postExecute(){
        System.out.println("init gui");
        initGUI();
        resetGUI();
        initV();
        btn1.setVisibility(View.VISIBLE);
        btn2.setVisibility(View.VISIBLE);
        btn3.setVisibility(View.VISIBLE);
        btn4.setVisibility(View.VISIBLE);
        wordTxt.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    final Handler handler = new Handler();

}
