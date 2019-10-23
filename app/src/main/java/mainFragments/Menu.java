package mainFragments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.marmi.cardschool.R;

import java.lang.reflect.Field;

public class Menu extends Fragment {

    private Button quiz;
    private Button card;
    private Button article;
    private Button insert;
    private Button print;
    private Button imports;
    private EditText nfrom;
    private EditText nto;
    private String from;
    private String to;
    private SeekBar seekBar;




    MenuListener listener;

    public interface MenuListener {
        void onInputMenu(CharSequence input, String from, String to, String mode);
    }

    View v;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        v = inflater.inflate(R.layout.main, container, false);
        super.onCreate(savedInstanceState);
        quiz = v.findViewById(R.id.Quiz);
        card = v.findViewById(R.id.Normal);
        article = v.findViewById(R.id.derdiedas);
        print = v.findViewById(R.id.print);
        imports = v.findViewById(R.id.imports);

        nfrom = v.findViewById(R.id.nfrom);
        nto = v.findViewById(R.id.nto);

        quiz.setOnClickListener(quizListener);
        card.setOnClickListener(cardListener);
        article.setOnClickListener(articleListener);
        print.setOnClickListener(printListener);
        imports.setOnClickListener(importsListener);


        insert = v.findViewById(R.id.insert);
        insert.setOnClickListener(insertListener);

        seekBar = v.findViewById(R.id.seekBar);



return v;
    }



    private View.OnClickListener printListener = new View.OnClickListener() {
        public void onClick(View v) {
            from = nfrom.getText().toString();
            to = nto.getText().toString();

            String mode = "";
            int skmode = seekBar.getProgress();
            if(skmode == 0){
                mode = "AND type = 'Nomen'";

            }else if (skmode == 1){
                mode = "AND type = 'Verb'";
            }else if (skmode == 2){
                mode = "AND type = 'Adjektiv'";
            }else if (skmode == 3){
                mode = "";
            }


            listener.onInputMenu("print", from, to, mode);
        }
    };
    private View.OnClickListener importsListener = new View.OnClickListener() {
        public void onClick(View v) {
            from = nfrom.getText().toString();
            to = nto.getText().toString();

            String mode = "";
            int skmode = seekBar.getProgress();
            if(skmode == 0){
                mode = "AND type = 'Nomen'";

            }else if (skmode == 1){
                mode = "AND type = 'Verb'";
            }else if (skmode == 2){
                mode = "AND type = 'Adjektiv'";
            }else if (skmode == 3){
                mode = "";
            }


            listener.onInputMenu("imports", from, to, mode);
        }
    };



    private View.OnClickListener quizListener = new View.OnClickListener() {
        public void onClick(View v) {
            from = nfrom.getText().toString();
            to = nto.getText().toString();
            String mode = "";
            int skmode = seekBar.getProgress();
            if(skmode == 0){
                mode = " AND type = 'Nomen'";

            }else if (skmode == 1){
                mode = " AND type = 'Verb'";
            }else if (skmode == 2){
                mode = " AND type = 'Adjektiv'";
            }else if (skmode == 3){
                mode = "";
            }

            System.out.println("toto "+to);
            listener.onInputMenu("quiz", from, to, mode);
        }
    };
    private View.OnClickListener cardListener = new View.OnClickListener() {
        public void onClick(View v) {
            from = nfrom.getText().toString();
            to = nto.getText().toString();
            String mode = "";
            int skmode = seekBar.getProgress();
            if(skmode == 0){
                mode = " AND type = 'Nomen'";

            }else if (skmode == 1){
                mode = " AND type = 'Verb'";
            }else if (skmode == 2){
                mode = " AND type = 'Adjektiv'";
            }else if (skmode == 3){
                mode = "";
            }

            listener.onInputMenu("card", from, to, mode);
        }
    };
    private View.OnClickListener articleListener = new View.OnClickListener() {
        public void onClick(View v) {
            from = nfrom.getText().toString();
            to = nto.getText().toString();
            String mode = "";
            int skmode = seekBar.getProgress();
            if(skmode == 0){
                mode = " AND type = 'Nomen'";

            }else if (skmode == 1){
                mode = " AND type = 'Verb'";
            }else if (skmode == 2){
                mode = " AND type = 'Adjektiv'";
            }else if (skmode == 3){
                mode = "";
            }
            listener.onInputMenu("article", from, to, mode);
        }
    };
    private View.OnClickListener insertListener = new View.OnClickListener() {
        public void onClick(View v) {
            from = nfrom.getText().toString();
            to = nto.getText().toString();
            String mode = "";
            int skmode = seekBar.getProgress();
            if(skmode == 0){
                mode = " AND type = 'Nomen'";

            }else if (skmode == 1){
                mode = " AND type = 'Verb'";
            }else if (skmode == 2){
                mode = " AND type = 'Adjektiv'";
            }else if (skmode == 3){
                mode = "";
            }

            listener.onInputMenu("insert", from, to, mode);
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (MenuListener) context;
    }
    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
