package test;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.WordController;
import com.example.marmi.cardschool.data.WordModel;
import com.example.marmi.cardschool.normal.Translator;

import java.lang.reflect.Array;

public class quizPresenter{

    private quizView qV;
    private Context context;

    private WordController currentRandom1;
    private WordController currentRandom2;
    private WordController currentRandom3;
    private DatabaseHelper mDatabaseHelper;
    public WordController wc;
    private String target = "en";
    private Cursor dtb;


    public quizPresenter(Context context, quizView qV) {

        this.context = context;
        this.qV = qV;
        initDB(" WHERE rate >= \" + from + \" AND rate <= \" + to +\" \"+ nmode + \" ORDER BY RANDOM()");
    }

    public void initDB(String query){


        DatabaseHelper mDatabaseHelper = new DatabaseHelper(context);
        dtb = mDatabaseHelper.getData(query);
        if(dtb==null){
            System.out.println("Reading Database cause Null");
            mDatabaseHelper.readData(mDatabaseHelper, context);
            dtb = mDatabaseHelper.getData(query);
        }
        mDatabaseHelper.close();

    }


    private void control(Button btn) {
        String text = btn.getText().toString();
        if (text == wc.getTranslated(target)) {
            qV.right(btn);
        } else {
            qV.wrong(btn);
        }
    }

    public void randomGenerator(String type) {
        String query = " WHERE rate >= " + 0 + " AND rate <= " + 100 + " AND type = '" + type + "' ORDER BY RANDOM() LIMIT 3";
        mDatabaseHelper = new DatabaseHelper(context);
        Cursor randomWords = mDatabaseHelper.getRandom(3, query);
        currentRandom1 = new WordController(randomWords);
        randomWords.moveToNext();
        currentRandom2 = new WordController(randomWords);
        randomWords.moveToNext();
        currentRandom3 = new WordController(randomWords);
        randomWords.moveToNext();
        mDatabaseHelper.close();
    }
    public void initV() {
        if(dtb == null){
            System.out.println("wtf InitV");
        }
        wc = new WordController();
        wc.importWord(dtb);
        randomGenerator(wc.getType());
        qV.update(wc, currentRandom1, currentRandom2, currentRandom3);


        System.out.println("Finish InitV "+wc.getWordText());
    }


    public void click(int vID) {
        switch (vID) {
            case R.id.btn1: {
                control(qV.btn1);
                break;
            }
            case R.id.btn2: {
                control(qV.btn2);
                break;
            }
            case R.id.btn3: {
                control(qV.btn3);
                break;
            }
            case R.id.btn4: {
                control(qV.btn4);
                break;
            }
        }

        System.out.println("Post Execute");
        qV.setClickable(false);
        final Runnable r = new Runnable() {
            public void run() {
                if (!dtb.moveToNext()) {
                    System.out.println("move to first");
                    dtb.moveToFirst();
                }

                wc.importWord(dtb);
                qV.resetGUI();
                qV.update(wc, currentRandom1, currentRandom2, currentRandom3);
                randomGenerator(wc.getType());
                qV.setClickable(true);
            }
        };
        final Handler handler = new Handler();
        handler.postDelayed(r, 1700);


    }


    public WordController[] getCurrents(){
        WordController currents[] = {wc, currentRandom1, currentRandom2, currentRandom3};
        return currents;
    }


    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }
}