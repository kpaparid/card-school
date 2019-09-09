package mainFragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.Language;
import com.example.marmi.cardschool.data.Word;
import com.example.marmi.cardschool.data.WordController;
import com.example.marmi.cardschool.data.WordModel;
import com.example.marmi.cardschool.normal.Translator;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fragments.EditBtn;
import fragments.FragmentLanguage;
import fragments.WiktionaryBtn;

public class templateFragment extends Fragment implements FragmentLanguage.FragmentLanguageListener, WiktionaryBtn.NestedListener, EditBtn.NestedListener {

    //public Word word;
    public WordController wordController;
    public Translator translation;
    public String target = "en";
    public Activity context;
    public View v;
    public String nfrom;
    public String nto;
    public TextView wordTxt;
    public ConstraintLayout layout;
    public TextView original;
    public ProgressBar progressBar;
    public Cursor dtb;
    public String mode;
    public FragmentListener listener;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(getLayoutID(), container, false);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nfrom = getArguments().getString("nfrom");
            nto = getArguments().getString("nto");
            mode =getArguments().getString("mode");
        }
        init();

        return v;
    }



    public void init(){
        initGUI();
        new initAsync().execute();

    }


    public void initDB(String query) {
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(getContext());
        dtb = mDatabaseHelper.getData(query);
        if(dtb==null){
            System.out.println("Reading Database cause Null");
            mDatabaseHelper.readData(mDatabaseHelper, getContext());
            dtb = mDatabaseHelper.getData(query);
        }
        mDatabaseHelper.close();
        System.out.println("init db");

    }
    @Override
    public void onInputLanguage(CharSequence input){}
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (FragmentListener) context;
    }


    /**
     * click within inner fragment (edit, wiki)
     * chain trigger to main
     */
    @Override
    public void nestedListenerClicked(String mode) {
        listener.onFragmentListener(wordController,mode);
    }

    /**
     * add fragment in container
     * @param type edit/wiki/language
     * @param container a/b/c
     */
    public void addFragment(String type,String container) {
        int layoutID = 0;
        if(container.equals("a")){
            layoutID = R.id.containera;
        }else if(container.equals("b")){
            layoutID = R.id.containerb;
        }else if(container.equals("c")){
            layoutID = R.id.containerc;
        }

        if(type.equals("wiki")){
            WiktionaryBtn wik = new WiktionaryBtn();

            getChildFragmentManager().beginTransaction()
                    .replace(layoutID, wik)
                    .commit();
        }
        else if(type.equals("language")){
            FragmentLanguage fragmentLanguage = new FragmentLanguage();
            getChildFragmentManager().beginTransaction()
                    .replace(layoutID, fragmentLanguage)
                    .commit();
        }
        else if(type.equals("edit")){
            EditBtn editBtn = new EditBtn();
            getChildFragmentManager().beginTransaction()
                    .replace(layoutID, editBtn)
                    .commit();
        }







    }
    public void initGUI(){}
    public void backGround(){}
    public void postExecute(){}
    public int getLayoutID(){return 0;}
    public interface FragmentListener {
        void onFragmentListener(WordController Word, String mode);
    }

    public class initAsync extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            backGround();
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            postExecute();
        }
    }


}
