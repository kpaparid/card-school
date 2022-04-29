package test;

import android.content.Context;
import android.os.Handler;
import android.widget.Button;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.WordController;
import com.example.marmi.cardschool.data.WordModel;

import java.util.ArrayList;

public class QuizPresenter {

    private final QuizView qV;
    private final Context context;
    private final WordController wordController;
    private WordModel currentRandom1;
    private WordModel currentRandom2;
    private WordModel currentRandom3;
    private String target = "en";


    public QuizPresenter(Context context, QuizView qV, WordModel[] randoms, WordController wc) {

        this.context = context;
        this.qV = qV;
        if (randoms == null) {
            wordController = (WordController) qV.getArguments().getSerializable("wc");
            randomGenerator(wordController.getType());
        } else {

            wordController = wc;
            currentRandom1 = randoms[0];
            currentRandom2 = randoms[1];
            currentRandom3 = randoms[2];
        }
    }


    private void control(Button btn) {
        String text = btn.getText().toString();
        if (text.equals(wordController.getTranslated(target))) {
            qV.right(btn);
        } else {
            qV.wrong(btn);
        }
    }

    public void randomGenerator(String type) {
        String query = " WHERE rate >= " + 0 + " AND rate <= " + 199 + " AND type = '" + type + "' ORDER BY RANDOM() LIMIT 3";
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(context);
        WordController randomWords = mDatabaseHelper.getRandom(query);
        ArrayList<WordModel> list = randomWords.getList();
        currentRandom1 = list.get(0);
        currentRandom2 = list.get(1);
        currentRandom3 = list.get(2);
        mDatabaseHelper.close();

    }

    public void initV() {
        qV.update(wordController.getWordModel(), currentRandom1, currentRandom2, currentRandom3);
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
        qV.setClickable(false);
        final Runnable r = new Runnable() {
            public void run() {


                wordController.moveToNext();
                randomGenerator(wordController.getType());
                qV.resetGUI();
                qV.update(wordController.getWordModel(), currentRandom1, currentRandom2, currentRandom3);

                qV.setClickable(true);
            }
        };
        final Handler handler = new Handler();
        handler.postDelayed(r, 1700);


    }

    public WordModel[] getCurrents() {
        return new WordModel[]{wordController.getWordModel(), currentRandom1, currentRandom2, currentRandom3};
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public WordController getWordController() {
        return wordController;
    }

    public WordModel[] getRandoms() {
        return new WordModel[]{currentRandom1, currentRandom2, currentRandom3};
    }
}