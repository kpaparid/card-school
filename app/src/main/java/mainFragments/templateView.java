package mainFragments;

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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.WordController;

import fragments.EditBtn;
import fragments.FragmentLanguage;
import fragments.WiktionaryBtn;

public class templateView extends Fragment implements FragmentLanguage.FragmentLanguageListener, WiktionaryBtn.NestedListener, EditBtn.NestedListener {


    View v;
    public ProgressBar progressBar;
    private FragmentListener listener;

    public Cursor dtb;
    public String from;
    public String to;
    public String mode;



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(getLayoutID(), container, false);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            from = getArguments().getString("nfrom");
            to = getArguments().getString("nto");
            mode =getArguments().getString("mode");
        }


        new init().execute();


        return v;
    }

    public void initDB(String query){


        DatabaseHelper mDatabaseHelper = new DatabaseHelper(getContext());
        dtb = mDatabaseHelper.getData(query);
        if(dtb==null){
            System.out.println("Reading Database cause Null");
            mDatabaseHelper.readData(mDatabaseHelper, getContext());
            dtb = mDatabaseHelper.getData(query);
        }
        mDatabaseHelper.close();

    }

    public WordController getWord(){return null;}
    public int getLayoutID(){return 0;}
    public void preInitGUI(){
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }
    public void initGUI() {
    }
    public void preExecute(){
    }
    public void postExecute(){
    }

    public class init extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {


            preExecute();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            postExecute();
        }
    }
    @Override
    public void nestedListenerClicked(String mode) {

        listener.onFragmentListener(getWord(),mode);
    }
    @Override
    public void onInputLanguage(CharSequence input) {    }
    public interface FragmentListener {
        void onFragmentListener(WordController Word, String mode);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (FragmentListener) context;
    }
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















}
