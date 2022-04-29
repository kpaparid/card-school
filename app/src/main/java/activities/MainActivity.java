package activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.EditText;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.WordController;
import com.example.marmi.cardschool.data.WordModel;

import java.util.ArrayList;

import fragments.Edit;
import fragments.WiktionaryWebFragment;
import mainFragments.Menu;
import test.ArticleView;
import test.QuizView;
import test.VoiceView;

public class MainActivity extends AppCompatActivity implements Menu.MenuListener, InsertFragment.FragmentListener, PrintFragment.FragmentListener, QuizView.FragmentListener, Edit.FragmentListener,  VoiceView.FragmentListener, ArticleView.FragmentListener {

    FragmentTransaction t;
    private Fragment fragment;
    private WordController wc;
    EditText nfrom;
    EditText nto;
    Edit edit;
    Boolean shuffle = true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        verifyStoragePermissions(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frs);


        if (savedInstanceState == null) {
            fragment = new Menu();
            t = getSupportFragmentManager().beginTransaction();
            t.add(R.id.frs, fragment);
            t.commit();
            t.addToBackStack(null);
            nfrom = findViewById(R.id.nfrom);
            nto = findViewById(R.id.nto);
        }
        }
    /**
     *Listener from inside, swaps fragment to UtilFragments
     */

    @Override
    public void onFragmentListener(WordModel word, String mode) {

        if(mode.equals("Wiki"))
        {
            WiktionaryWebFragment wik = new WiktionaryWebFragment();
            Bundle bundle = new Bundle();
            WordController wc = new WordController(word);
            bundle.putString("message", wc.getWiki());
            wik.setArguments(bundle);
            t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.frs, wik);
            t.addToBackStack(null);
            t.commit();

        }
        else if(mode.equals("Edit")) {
            edit = new Edit();
            Bundle bundle = new Bundle();
            String DESCRIBABLE_KEY = "word_key";
            bundle.putSerializable(DESCRIBABLE_KEY, word);
            edit.setArguments(bundle);
            t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.frs, edit);
            t.addToBackStack(null);
            t.commit();
        }
    }

    public void initDataBase(String query) {
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);
        ArrayList dtb;
        dtb = mDatabaseHelper.getData(query);
        if(dtb == null ||dtb.size() == 0){
            mDatabaseHelper.readData(mDatabaseHelper, this);
            dtb = mDatabaseHelper.getData(query);
        }
        mDatabaseHelper.close();
        wc = new WordController();
        wc.setList(dtb);
        wc.shuffle(shuffle);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    if(getSupportFragmentManager().getBackStackEntryCount() == 1){
                        finish();
                    }else {
                        getSupportFragmentManager().popBackStack();
                    }
                    return true;
                }
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     *Gets Mode from Menu and changes fragment
     */
    @Override
    public void onInputMenu(CharSequence input, String from, String to, String nmode) {
        switch (input.toString()){
            case "card":{
                shuffle = true;
                fragment = new VoiceView();
                break;
            }case "article":{
                shuffle = true;
                fragment = new ArticleView();
                break;
            }case "quiz":{
                shuffle = true;
                fragment = new QuizView();
                break;
            }case "insert":{
                shuffle = false;
                fragment = new InsertFragment();
                from = "0";
                to = "9999";
                break;
            }case "print":{
                shuffle = false;
                fragment = new PrintFragment();
                break;
            }case "imports":{
                shuffle = true;
                fragment = new ImportsFragment();
                from = "0";
                to = "9999";
                break;
            }
        }
        String query = " WHERE rate >= " + from + " AND rate <= " + to +" "+ nmode;
        initDataBase(query);

        Bundle bundle = new Bundle();
        bundle.putString("nfrom", from);
        bundle.putString("nto", to);
        bundle.putString("mode",nmode);
        bundle.putString("query",query);
        String DESCRIBABLE_KEY = "wc";
        bundle.putSerializable(DESCRIBABLE_KEY, wc);
        fragment.setArguments(bundle);
        t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.frs, fragment);

        t.addToBackStack(null);
        t.commit();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}