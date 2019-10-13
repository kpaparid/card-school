package test;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.widget.Button;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.WordController;

public class quizPresenter{

    private quizView qV;
    private Context context;
    private WordController wordController;
    private WordController currentRandom1;
    private WordController currentRandom2;
    private WordController currentRandom3;
    private DatabaseHelper mDatabaseHelper;
    private String target = "en";
    private String from;
    private String to;
    private String mode;




    public quizPresenter(Context context, WordController[] wc, quizView qV) {

        this.context = context;
        this.qV = qV;
        if (qV.getArguments() != null) {
            from = qV.getArguments().getString("nfrom");
            to = qV.getArguments().getString("nto");
            mode =qV.getArguments().getString("mode");
            System.out.println(to);
        }

        if(wc == null){
            System.out.println("world controller null");
            wordController = new WordController();
            wordController.initDB(" WHERE rate >= " + from + " AND rate <= " + to + mode + " ORDER BY RANDOM()",context);
            randomGenerator(wordController.getType());
        }
        else {
            this.wordController = wc[0];
            this.currentRandom1 = wc[1];
            this.currentRandom2 = wc[2];
            this.currentRandom3 = wc[3];
        }

    }



    private void control(Button btn) {
        String text = btn.getText().toString();
        if (text == wordController.getTranslated(target)) {
            qV.right(btn);
        } else {
            qV.wrong(btn);
        }
    }

    public void randomGenerator(String type) {
        System.out.println("random generator");
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


        qV.update(wordController, currentRandom1, currentRandom2, currentRandom3);
        System.out.println("Finish InitV "+wordController.getWordText());
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


                wordController.moveToNext();
                randomGenerator(wordController.getType());
                qV.resetGUI();
                qV.update(wordController, currentRandom1, currentRandom2, currentRandom3);

                qV.setClickable(true);
            }
        };
        final Handler handler = new Handler();
        handler.postDelayed(r, 1700);


    }


    public WordController[] getCurrents(){
        WordController currents[] = {wordController, currentRandom1, currentRandom2, currentRandom3};
        return currents;
    }


    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }


    public WordController[] getWordController() {
        return new WordController[]{wordController,currentRandom1,currentRandom2,currentRandom3};
    }
}