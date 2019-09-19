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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.Language;
import com.example.marmi.cardschool.data.Word;
import com.example.marmi.cardschool.data.WordController;
import com.example.marmi.cardschool.data.WordModel;
import com.example.marmi.cardschool.normal.Translator;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import fragments.EditBtn;
import fragments.FragmentLanguage;
import fragments.WiktionaryBtn;

import static android.graphics.Color.GREEN;

public class quizoPresenter extends templatePresenter{
    ArrayList<WordController> words;
    ArrayList<Button> buttons;
    private Translator tr0;
    private Translator tr1;
    private Translator tr2;
    private Translator tr3;
    private int index;
    private quizoView qV;

    private WordController nextWord;
    private WordController currentRandom1;
    private WordController currentRandom2;
    private WordController currentRandom3;
    private WordController nextRandom1;
    private WordController nextRandom2;
    private WordController nextRandom3;
    private DatabaseHelper mDatabaseHelper;



    public quizoPresenter(quizoView quizoView, Context context){
        qV = quizoView;
        this.context = context;

    }
    public void btnClick(View v){
        switch (v.getId()) {
            case R.id.btn1: {
                control(qV.getBtn1());
                break;
            }

            case R.id.btn2: {
                control(qV.getBtn2());
                break;
            }


            case R.id.btn3: {
                control(qV.getBtn3());
                break;
            }
            case R.id.btn4: {
                control(qV.getBtn4());
                break;
            }

        }

        System.out.println("Post Execute");
        wc = nextWord;
        currentRandom1 = nextRandom1;
        currentRandom2 = nextRandom2;
        currentRandom3 = nextRandom3;

//        btn1.setClickable(false);
//        btn2.setClickable(false);
//        btn3.setClickable(false);
//        btn4.setClickable(false);

        final Runnable r = new Runnable() {
            public void run() {
                if (!dtb.moveToNext()) {
                    dtb.moveToFirst();
                }
                qV.resetGUI();
                update(wc, currentRandom1, currentRandom2, currentRandom3);
                randomGenerator(nextWord.getType());
//                btn1.setClickable(true);
//                btn2.setClickable(true);
//                btn3.setClickable(true);
//                btn4.setClickable(true);
            }
        };
        handler.postDelayed(r, 1700);


    }



    public void initV() {
        if(dtb == null){
            System.out.println("wtf InitV");
        }
        wc.importWord(dtb);
        randomGenerator(wc.getType());
        nextWord = wc;
        currentRandom1 = nextRandom1;
        currentRandom2 = nextRandom2;
        currentRandom3 = nextRandom3;
        update(wc, currentRandom1, currentRandom2, currentRandom3);
        if (!dtb.moveToNext()) {
            dtb.moveToFirst();
        }
        WordModel wm = new WordModel();
        nextWord = new WordController();
        nextWord.importWord(dtb);
        randomGenerator(nextWord.getType());
        System.out.println("Finish InitV");
    }

    private void randomGenerator(String type) {
        String query = " WHERE rate >= " + 0 + " AND rate <= " + 10 + " AND type = '" + type + "' ORDER BY RANDOM() LIMIT 3";
        mDatabaseHelper = new DatabaseHelper(context);
        Cursor randomWords = mDatabaseHelper.getRandom(3, query);
        nextRandom1 = new WordController();
        nextRandom1.importWord(randomWords);
        randomWords.moveToNext();
        nextRandom2 = new WordController();
        nextRandom2.importWord(randomWords);
        randomWords.moveToNext();
        nextRandom3 = new WordController();
        nextRandom3.importWord(randomWords);
        randomWords.moveToNext();
        mDatabaseHelper.close();

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
        if (text == wc.getTranslated(target)) {
            right(btn);
        } else {
            wrong(btn);
        }
    }






    public void update(final WordController word, final WordController random1, final WordController random2, final WordController random3) {

        words = new ArrayList<>();
        words.add(word);
        words.add(random1);
        words.add(random2);
        words.add(random3);

        Collections.shuffle(words);
        index = words.indexOf(word);

        buttons = new ArrayList<Button>();
        buttons.add(qV.getBtn1());
        buttons.add(qV.getBtn2());
        buttons.add(qV.getBtn3());
        buttons.add(qV.getBtn4());

        anim(word);
    }

    private void anim(final WordController word) {

        ConstraintLayout transitionsContainer = qV.getView().findViewById(R.id.contentlayout);
        TransitionManager.beginDelayedTransition(transitionsContainer, new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
        qV.getBtn1().setText(words.get(0).getTranslated(target));
        qV.getBtn1().setText(words.get(1).getTranslated(target));
        qV.getBtn1().setText(words.get(2).getTranslated(target));
        qV.getBtn1().setText(words.get(3).getTranslated(target));
        qV.getWas().setText(word.getWordText());
    }





    final Handler handler = new Handler();

    public void onInputLanguage(final CharSequence input) {

        if (!input.equals("Error")) {
            target = input.toString();
            qV.resetGUI();
            update(wc, currentRandom1, currentRandom2, currentRandom3);
        }
    }

}
