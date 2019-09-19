package mainFragments;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.Word;
import com.example.marmi.cardschool.data.WordController;

import java.util.ArrayList;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

public class artoPresenter extends templatePresenter{

    private int index;

    public artoPresenter(Cursor dtb) {
        super();
        this.dtb = dtb;
        wc = new WordController();
        importNextWord();
    }

    public Integer getIndex() {
        switch (getWord().getArticle()) {
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
        return index;
    }
}
