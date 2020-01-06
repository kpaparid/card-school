package fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.WordController;
import com.example.marmi.cardschool.data.WordModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Edit extends Fragment implements WiktionaryBtn.NestedListener, Serializable {


    WordModel inputWord;
    TextInputEditText word;
    TextInputEditText plural;
    TextView comma;
    Spinner typeSpinner;
    Spinner articleSpinner;
    TextInputEditText ent;
    TextInputEditText grt;
    TextInputEditText set;
    TextInputEditText hrt;
    EditText ratet;
    Button insert;
    ImageView deleteWord;
    List<String> list;
    List<String> listArticle;
    LayoutInflater inflator;
    List<String> missingAttribute;
    Button translate;
    String rate = "11";
    String de;
    String en = "temp";
    String gr = "temp";
    String hr = "temp";
    String sr = "temp";
    String article;
    String type;
    FrameLayout wiki;
    FragmentManager fm;
    View v;
    private DatabaseHelper mDatabaseHelper;
    private FragmentListener listenerb;
    private View.OnClickListener submitListener = new View.OnClickListener() {
        public void onClick(View v) {
            List misAt = getMissingAttribute();
            if (!(misAt.contains("Word") || misAt.contains("Article") || misAt.contains("Type"))) {




                en = ent.getText().toString();
                gr = grt.getText().toString();
                hr = hrt.getText().toString();
                sr = set.getText().toString();
                rate = ratet.getText().toString();
                de = word.getText().toString();

                type = type.replace(" ","");
                while (de.endsWith(" ")){
                    de = de.substring(0,de.length()-1);
                }


                if(type.equals("Nomen")){
                    String plur = plural.getText().toString().replace(" ","");
                    de = article+" "+de+",- "+plur;
                    System.out.println(de);
                }


                Log.e("translate", "rate:  " + rate);
                Log.e("translate", "type:  " + type);
                Log.e("translate","article "+article);
                Log.e("translate", "de:  " + de);
                Log.e("translate", "en:  " + en);
                Log.e("translate", "el:  " + gr);
                Log.e("translate", "hr:  " + hr);
                Log.e("translate", "sr:  " + sr);

                mDatabaseHelper.deleteID(inputWord.getID());
                mDatabaseHelper.addData(type, Integer.parseInt(rate), de, en, gr, hr, sr);
                mDatabaseHelper.exportDB();


                fm.popBackStack();

            }
        }
    };


    private View.OnClickListener deleteListener = new View.OnClickListener() {
        public void onClick(View v) {

            mDatabaseHelper.deleteID(inputWord.getID());
            mDatabaseHelper.exportDB();
            fm.popBackStack();

            }

    };

    private AdapterView.OnItemSelectedListener articleListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            article = listArticle.get(i);
            if (!article.equals("Article")) {
                articleSpinner.setBackgroundResource(android.R.drawable.screen_background_dark_transparent);
            } else {
                articleSpinner.setBackgroundResource(R.drawable.back);
            }

            System.out.println(article);


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };
    private AdapterView.OnItemSelectedListener typeListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


            type = list.get(i);
            System.out.println(type);
            if (type.equals("Nomen")) {

                articleSpinner.setVisibility(View.VISIBLE);
                plural.setVisibility(View.VISIBLE);
                comma.setVisibility(View.VISIBLE);
            } else {
                articleSpinner.setVisibility(View.INVISIBLE);
                plural.setVisibility(View.GONE);
                comma.setVisibility(View.GONE);
                articleSpinner.setSelection(listArticle.size() - 1);
            }
            if (!type.equals("Type")) {
                typeSpinner.setBackgroundResource(android.R.drawable.screen_background_dark_transparent);
            } else {
                typeSpinner.setBackgroundResource(R.drawable.back);
            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listenerb = (FragmentListener) context;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflator = inflater;
        v = inflater.inflate(R.layout.fr_insert2, container, false);
        inputWord = (WordModel) getArguments().getSerializable("word_key");
        System.out.println("inputWord "+inputWord.getWordText());
        init();
        return v;
    }

    private void init() {

        fm = getActivity().getSupportFragmentManager();
        addFragment();
        missingAttribute = new ArrayList<>();
        ent = v.findViewById(R.id.ent);
        grt = v.findViewById(R.id.grt);
        set = v.findViewById(R.id.set);
        hrt = v.findViewById(R.id.hrt);
        word = v.findViewById(R.id.word);
        comma = v.findViewById(R.id.comma);
        plural = v.findViewById(R.id.plural);
        wiki = v.findViewById(R.id.containera);
        ratet = v.findViewById(R.id.rate);
        deleteWord = v.findViewById(R.id.deleteWord);
        deleteWord.setOnClickListener(deleteListener);
        deleteWord.setVisibility(View.VISIBLE);



        insert = v.findViewById(R.id.insert);
        insert.setOnClickListener(submitListener);

        translate = v.findViewById(R.id.translate);
        translate.setOnClickListener(translateListener);
        //translate.setVisibility(View.GONE);
        insert.setVisibility(View.VISIBLE);

        typeSpinner = v.findViewById(R.id.type);
        typeSpinner.setPrompt("Choose Type");

        articleSpinner = v.findViewById(R.id.article);
        articleSpinner.setPrompt("Choose Article");

        list = Arrays.asList(v.getContext().getResources().getStringArray(R.array.type));

        NewAdapter na = new NewAdapter(list);
        typeSpinner.setAdapter(na);
        typeSpinner.setSelection(list.size() - 1);
        typeSpinner.setOnItemSelectedListener(typeListener);

        listArticle = Arrays.asList(v.getContext().getResources().getStringArray(R.array.article));
        na = new NewAdapter(listArticle);
        articleSpinner.setAdapter(na);
        articleSpinner.setSelection(listArticle.size() - 1);
        articleSpinner.setOnItemSelectedListener(articleListener);




        mDatabaseHelper = new DatabaseHelper(getContext());
        word.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (!word.getText().toString().equals("")) {
                    wiki.setVisibility(View.VISIBLE);
                } else {
                    wiki.setVisibility(View.GONE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (word.getText().toString().equals("delete")) {
                    mDatabaseHelper.delete();
                    fm.popBackStack();

                }  else if(!word.getText().toString().equals("")) {
                    word.setBackgroundResource(android.R.drawable.screen_background_dark_transparent);
                }
            }
        });
        plural.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                plural.setBackgroundResource(android.R.drawable.screen_background_dark_transparent);

            }
        });

        type = inputWord.getType();
        article = inputWord.getArticle();

        switch (type){
            case "Verb":{
                typeSpinner.setSelection(0);
                break;
            }
            case "Adjektiv":{
                typeSpinner.setSelection(2);
                break;
            }
            case "Conj":{
                typeSpinner.setSelection(3);
                break;
            }
            case "Nomen":{
                typeSpinner.setSelection(1);
                switch (article){
                    case "der":{
                        articleSpinner.setSelection(0);
                        break;
                    }
                    case "die":{
                        articleSpinner.setSelection(1);
                        break;
                    }
                    case "das":{
                        articleSpinner.setSelection(2);
                        break;
                    }
                }
                plural.setText(inputWord.getPlural());
                break;
            }
        }

        word.setText(inputWord.getWordText());
        en = inputWord.getEn_translated();
        gr = inputWord.getGr_translated();
        de = inputWord.getWordText();
        sr = inputWord.getSr_translated();
        hr = inputWord.getHr_translated();
        rate = inputWord.getRate();
        ratet.setText(rate);
        ratet.setBackgroundResource(android.R.drawable.screen_background_dark_transparent);
        ratet.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(ratet.getText().equals("")){
                    ratet.setBackgroundResource(R.drawable.back);
                    System.out.println("back ");
                }
                else
                {
                    ratet.setBackgroundResource(android.R.drawable.screen_background_dark_transparent);
                }


            }
        });




        ent.setText(en);
        grt.setText(gr);
        hrt.setText(hr);
        set.setText(sr);


    }
    private View.OnClickListener translateListener = new View.OnClickListener() {
        public void onClick(View v) {
            List misAt = getMissingAttribute();
            if(!(misAt.contains("Word"))){
                de = word.getText().toString();
                String dev = de;
                if(type.equals("Verb")) {
                    dev = "wir " + dev;
                    System.out.println(de);
                }
                new translateasync(dev).execute();

            }
        }
    };
    Boolean translateFinished = true;
    private void loading(Boolean bool){
        ConstraintLayout cl = v.findViewById(R.id.constraintLayout5);
        if(bool){
            System.out.println("bool true");
            //pb.setVisibility(View.VISIBLE);

            cl.setAlpha(0.1f);
        }else {

            System.out.println("bool false");
            //pb.setVisibility(View.INVISIBLE);
            cl.setAlpha(1);
        }
    }
    class translateasync extends AsyncTask<String, Void, String[]> {

        private final String word;
        translateasync(String word){

            if(word.contains("(")&&word.contains("+")){
                System.out.println(word);
                word = word.substring(0,word.indexOf("(")-1);
            }
            this.word = word;
        }

        protected String translate(String lang) throws Exception{
            String wiki = word;
            String urlStr = "https://script.google.com/macros/s/AKfycbyrvkKW6iSQtj4F7zLSknEJlQYAU-mdis60YcOp2dJJS1iIlBsD/exec" +
                    "?q=" + URLEncoder.encode(wiki, "UTF-8") +
                    "&target=" + lang +
                    "&source=" + "de";
            URL url = new URL(urlStr);
            StringBuilder response = new StringBuilder();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            wiki = response.toString();
            return wiki;
        }


        protected void onPreExecute() {
            loading(true);
            System.out.println("pre execute");
            translateFinished = false;
        }



        protected String[] doInBackground(String... urls) {

            String[] wiki = new String[4];
            try {

                wiki[0] = translate("en");
                wiki[1] = translate("el");
                wiki[2] = translate("hr");
                wiki[3] = translate("sr");


            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("hmm");
            return wiki;

        }
        protected void onPostExecute(String[] feed) {

            System.out.println("post execute");
            loading(false);
            translateFinished = true;
            en = feed[0];
            en = en.replace("we ","");
            en = en.replace("the ","");

            gr = feed[1];
            gr = gr.replace("το ","");

            hr = feed[2];
            hr = hr.replace("mi ","");

            sr = feed[3];
            sr = sr.replace("ми ","");
            ent.setText(en);
            grt.setText(gr);
            set.setText(sr);
            hrt.setText(hr);


        }
    }
    private void addFragment() {

        WiktionaryBtn wik = new WiktionaryBtn();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.containera, wik)
                .commit();


    }



    List getMissingAttribute() {
        missingAttribute = new ArrayList<>();
        if (word.getText().toString().equals("")) {
            missingAttribute.add("Word");
            System.out.println("missing Word");
        }
        if (type.equals("Type")) {
            missingAttribute.add("Type");
            System.out.println("missing Type");
        }
        if (type.equals("Nomen") && article.equals("Article")) {
            missingAttribute.add("Article");
            System.out.println("missing Article");
        }
        if (type.equals("Nomen") && plural.getText().toString().equals("")) {
            missingAttribute.add("Plural");
            System.out.println("missing Plural");
        }


        return missingAttribute;
    }

    @Override
    public void nestedListenerClicked(String mode) {
        System.out.println("Clicked NestedListener");
        List misAt = getMissingAttribute();
        if(!misAt.contains("Word"))
        {
            System.out.println("wordController text "+word.getText().toString());
            System.out.println("wordController type "+ type);
            System.out.println("wordController text "+ article);
            inputWord.setWordText(word.getText().toString());
            listenerb.onFragmentListener(inputWord, "Wiki");

        }else {
            System.out.println("wtf");
        }

    }

    public interface FragmentListener {
        void onFragmentListener(WordModel word, String mode);
    }

    class NewAdapter extends BaseAdapter {

        List<String> list;

        NewAdapter(List list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size() - 1;
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflator.inflate(R.layout.insert_spinner, null);
            }
            TextView text = convertView.findViewById(R.id.text);
            text.setText(list.get(position));

            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflator.inflate(R.layout.insert_spinner, null);

            }
            TextView text = convertView.findViewById(R.id.text);
            text.setText(list.get(position));

            return convertView;
        }
    }


}
