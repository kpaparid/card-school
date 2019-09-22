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

public class templatePresenter {

    public WordController wc;
    public String target = "en";
    public Cursor dtb;
    private String mode;
    public String nfrom;
    public String nto;

    public Context context;





    public WordController getWord(){
        return wc;
    }


    public boolean moveNextdtb(){
        return dtb.moveToNext();
    }
    public boolean movePreviousdtb(){
        return dtb.moveToPrevious();
    }
    public boolean moveFirstdtb(){
        return dtb.moveToFirst();
    }

    public String getTarget(){
        return target;
    }
    public void importNextWord(){
        wc.importWord(dtb);
    }
    public void setTarget(String target) {
        this.target = target;
    }


}
