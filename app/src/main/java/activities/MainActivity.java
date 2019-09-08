package activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.transition.Fade;
import android.support.transition.TransitionInflater;
import android.support.transition.TransitionSet;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.EditText;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.Word;

import java.util.LinkedList;
import java.util.Queue;

import fragments.Edit;
import fragments.WiktionaryWebFragment;
import mainFragments.ArticleFragment;
import mainFragments.CardFragment;
import mainFragments.Menu;
import mainFragments.QuizFragment;
import mainFragments.TestFragment;

public class MainActivity extends AppCompatActivity implements CardFragment.FragmentListener, Menu.MenuListener, QuizFragment.FragmentListener, ArticleFragment.FragmentListener, InsertFragment.FragmentListener, PrintFragment.FragmentListener{

    FragmentTransaction t;
    private WiktionaryWebFragment wik;
    private Fragment fragment;
    private long MOVE_DEFAULT_TIME = 100;
    private long FADE_DEFAULT_TIME = 200;
    EditText nfrom;
    EditText nto;
    Edit edit;
    PrintFragment print;
    private String currentMode;
    Queue<String> framgnetQueue = new LinkedList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        verifyStoragePermissions(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frs);
        fragment = new Menu();
        t = getSupportFragmentManager().beginTransaction();
        t.add(R.id.frs, fragment);
        t.commit();
        nfrom = findViewById(R.id.nfrom);
        nto = findViewById(R.id.nto);

    }

    @Override
    public void onFragmentListener(Word word, String mode) {

        if(mode.equals("Wiki"))
        {
            System.out.println("ftiaxnw Wiki");
            System.out.println("Wiki");
            wik = new WiktionaryWebFragment();
            Bundle bundle = new Bundle();
            bundle.putString("message", word.getWiki());
            wik.setArguments(bundle);
            t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.frs,wik);
            t.addToBackStack(null);
            t.commit();

        }
        else if(mode.equals("Edit")) {
            System.out.println("Edit");
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if(this.getSupportFragmentManager().getBackStackEntryCount()==0) {
                        System.out.println("Exit main");
                        finish();
                    }
                    else if (wik == null || wik.isHidden()) {
                        System.out.println("Exit default");
                        getSupportFragmentManager().popBackStack();
                    }
                    else if (wik.onBack()) {
                        System.out.println("CLICKED BACK");

                    }
                    else{
                        System.out.println("backstack Count "+getSupportFragmentManager().getBackStackEntryCount());
                        System.out.println("EXITING");
                        getSupportFragmentManager().popBackStack();
                    }

                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onInputMenu(CharSequence input, String from, String to, String nmode) {
        System.out.println(input);
        Fragment previous = fragment;
        if (input.equals("card")) {
            fragment = new CardFragment();
        } else if (input.equals("test")) {
            fragment = new TestFragment();
        } else if (input.equals("article")) {
            fragment = new ArticleFragment();
        } else if (input.equals("quiz")) {
            fragment = new QuizFragment();
        } else if (input.equals("insert")) {
            fragment = new InsertFragment();
        }else if (input.equals("print")) {
            fragment = new PrintFragment();
        }



        Bundle bundle = new Bundle();
        bundle.putString("nfrom", from);
        bundle.putString("nto", to);
        bundle.putString("mode",nmode);
        fragment.setArguments(bundle);
        System.out.println("set argument "+ from+"\t"+to);




        t = getSupportFragmentManager().beginTransaction();
        animator(previous,fragment);





        t.replace(R.id.frs, fragment);
        t.addToBackStack(null);
        t.commit();
    }

    private void animator(Fragment previous, Fragment fragment) {
        // 1. Exit for Previous Fragment
        Fade exitFade = new Fade();
        exitFade.setDuration(FADE_DEFAULT_TIME);
        previous.setExitTransition(exitFade);
        // 2. Shared Elements Transition
        TransitionSet enterTransitionSet = new TransitionSet();
        enterTransitionSet.addTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
        enterTransitionSet.setDuration(MOVE_DEFAULT_TIME);
        enterTransitionSet.setStartDelay(FADE_DEFAULT_TIME);
        fragment.setSharedElementEnterTransition(enterTransitionSet);
        // 3. Enter Transition for New Fragment
        Fade enterFade = new Fade();
        enterFade.setStartDelay(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME);
        enterFade.setDuration(FADE_DEFAULT_TIME);
        fragment.setEnterTransition(enterFade);
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