package mainFragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.WordController;
import com.example.marmi.cardschool.normal.Translator;

import fragments.EditBtn;
import fragments.FragmentLanguage;
import fragments.WiktionaryBtn;

public class cardoPresenter extends templatePresenter{

    private Boolean end = Boolean.FALSE;
    private Boolean flag = Boolean.TRUE;

    public cardoPresenter(Cursor dtb) {
        super();
        this.dtb = dtb;
        wc = new WordController();
        importNextWord();
    }

    public boolean orig() {
        if (!end) {
            if (flag) {

                flag = Boolean.FALSE;
            } else {
                if (moveNextdtb()) {
                    importNextWord();
                } else {
                    moveFirstdtb();
                    importNextWord();
                }
                System.out.println("ORIG UI");
                flag = Boolean.TRUE;


            }
        }
        return flag;
    }

    public void backClick() {
        if (movePreviousdtb()) {
            importNextWord();

            flag = Boolean.FALSE;
        }
    }

    public boolean getBooleanNormal() {
        return flag;
    }
}
