package activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.Word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fragments.FragmentLanguage;
import fragments.WiktionaryBtn;

public class InsertFragment extends Fragment implements WiktionaryBtn.NestedListener {


    private DatabaseHelper mDatabaseHelper;

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
    List<String> list;
    List<String> listArticle;
    LayoutInflater inflator;
    Button translate;
    List<String> missingAttribute;

    Integer rate = 11;
    String de;
    String en = "temp";
    String gr = "temp";
    String hr = "temp";
    String sr = "temp";
    String article;
    String type;
    FrameLayout wiki;

    Boolean flagTranslate = false;

    private FragmentListener listener;



    public interface FragmentListener {
        void onFragmentListener(Word word, String mode);
    }





    View v;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (FragmentListener) context;


    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        inflator = inflater;
        v = inflater.inflate(R.layout.fr_insert2, container, false);

        init();

return v;
    }

    private void init() {
        addFragment();
        missingAttribute = new ArrayList<>();

        ent = v.findViewById(R.id.ent);
        grt = v.findViewById(R.id.grt);
        set = v.findViewById(R.id.set);
        hrt = v.findViewById(R.id.hrt);
        word = v.findViewById(R.id.word);
        comma = v.findViewById(R.id.comma);
        plural = v.findViewById(R.id.plural);
        translate = v.findViewById(R.id.translate);
        wiki  = v.findViewById(R.id.btnW);
        ratet = v.findViewById(R.id.rate);

        insert = v.findViewById(R.id.insert);
        insert.setOnClickListener(submitListener);

        typeSpinner = v.findViewById(R.id.type);
        typeSpinner.setPrompt("Choose Type");

        articleSpinner = v.findViewById(R.id.article);
        articleSpinner.setPrompt("Choose Article");

        list = Arrays.asList(v.getContext().getResources().getStringArray(R.array.type));

        NewAdapter na = new NewAdapter(list);
        typeSpinner.setAdapter(na);
        typeSpinner.setSelection(list.size()-1);
        typeSpinner.setOnItemSelectedListener(typeListener);
        listArticle = Arrays.asList(v.getContext().getResources().getStringArray(R.array.article));

        na = new NewAdapter(listArticle);
        articleSpinner.setAdapter(na);
        articleSpinner.setSelection(listArticle.size()-1);
        articleSpinner.setOnItemSelectedListener(articleListener);


        mDatabaseHelper = new DatabaseHelper(getContext());
        translate.setOnClickListener(translateListener);
        word.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if(!word.getText().toString().equals(""))
                {
                    wiki.setVisibility(View.VISIBLE);
                }
                else {
                    wiki.setVisibility(View.GONE);
                }

//                List misAt = getMissingAttribute();
////                if(!misAt.contains("Word")){
////                    de = word.getText().toString();
////                    String dev = de;
////
////                    if(type.equals("Verb")) {
////                        dev = "wir " + dev;
////                        System.out.println(de);
////                    }
////                    try {
////                        en = new translateasync("en",dev).execute().get();
////                        en = en.replace("we ","");
////                        en = en.replace("the ","");
////                        gr = new translateasync("el",dev).execute().get();
////                        gr = gr.replace("το ","");
////                        hr = new translateasync("hr",dev).execute().get();
////                        hr = hr.replace("mi ","");
////                        sr = new translateasync("sr",dev).execute().get();
////                        sr = sr.replace("ми ","");
////
////                        ent.setText(en);
////                        grt.setText(gr);
////                        set.setText(sr);
////                        hrt.setText(hr);
////
////                    } catch (ExecutionException e) {
////                        e.printStackTrace();
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
////                }

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(word.getText().toString().equals("delete")){
                    mDatabaseHelper.delete();
                    getActivity().getSupportFragmentManager().popBackStack();
                }else if(word.getText().equals("")){

                }
                else {
                    word.setBackgroundResource(android.R.drawable.screen_background_dark_transparent);
                }
            }
        });
        ratet.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if(!word.getText().toString().equals(""))
                {
                    wiki.setVisibility(View.VISIBLE);
                }
                else {
                    wiki.setVisibility(View.GONE);
                }

//                List misAt = getMissingAttribute();
////                if(!misAt.contains("Word")){
////                    de = word.getText().toString();
////                    String dev = de;
////
////                    if(type.equals("Verb")) {
////                        dev = "wir " + dev;
////                        System.out.println(de);
////                    }
////                    try {
////                        en = new translateasync("en",dev).execute().get();
////                        en = en.replace("we ","");
////                        en = en.replace("the ","");
////                        gr = new translateasync("el",dev).execute().get();
////                        gr = gr.replace("το ","");
////                        hr = new translateasync("hr",dev).execute().get();
////                        hr = hr.replace("mi ","");
////                        sr = new translateasync("sr",dev).execute().get();
////                        sr = sr.replace("ми ","");
////
////                        ent.setText(en);
////                        grt.setText(gr);
////                        set.setText(sr);
////                        hrt.setText(hr);
////
////                    } catch (ExecutionException e) {
////                        e.printStackTrace();
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
////                }

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(!ratet.getText().equals("")){
                    ratet.setBackgroundResource(android.R.drawable.screen_background_dark_transparent);
                }
            }
        });

        plural.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                plural.setBackgroundResource(android.R.drawable.screen_background_dark_transparent);

            }
        });
    }
    private View.OnClickListener submitListener = new View.OnClickListener() {
        public void onClick(View v) {


        List misAt = getMissingAttribute();
        if(!(misAt.contains("Word")||misAt.contains("Rate")||misAt.contains("Article")||misAt.contains("Type"))){
            insert.setVisibility(View.GONE);
            translate.setVisibility(View.VISIBLE);
            if(type.equals("Nomen")){
                de =article +" "+de +",- "+plural.getText();
                System.out.println(de);
            }

            en = ent.getText().toString();
            gr = grt.getText().toString();
            hr = hrt.getText().toString();
            sr = set.getText().toString();
            rate = Integer.parseInt(ratet.getText().toString());

            Log.e("translate", "rate:  " + rate);
            Log.e("translate", "type:  " + type);
            Log.e("translate", "de:  " + de);
            Log.e("translate", "en:  " + en);
            Log.e("translate", "el:  " + gr);
            Log.e("translate", "hr:  " + hr);
            Log.e("translate", "sr:  " + sr);


            mDatabaseHelper.addData(type,rate,de,en,gr,hr,sr);
            mDatabaseHelper.exportDB();
            mDatabaseHelper.close();
            getActivity().getSupportFragmentManager().popBackStack();
        }
        }
    };
    private View.OnClickListener translateListener = new View.OnClickListener() {
        public void onClick(View v) {


            List misAt = getMissingAttribute();
            if(!(misAt.contains("Word")||misAt.contains("Article")||misAt.contains("Type"))){
                translate.setVisibility(View.GONE);
                insert.setVisibility(View.VISIBLE);
                de = word.getText().toString();
                String dev = de;

                if(type.equals("Verb")) {
                    dev = "wir " + dev;
                    System.out.println(de);
                }
                try {
                    en = new translateasync("en",dev).execute().get();
                    en = en.replace("we ","");
                    en = en.replace("the ","");
                    gr = new translateasync("el",dev).execute().get();
                    gr = gr.replace("το ","");
                    hr = new translateasync("hr",dev).execute().get();
                    hr = hr.replace("mi ","");
                    sr = new translateasync("sr",dev).execute().get();
                    sr = sr.replace("ми ","");

                    ent.setText(en);
                    grt.setText(gr);
                    set.setText(sr);
                    hrt.setText(hr);

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    class translateasync extends AsyncTask<String, Void, String> {

        private final String lang;
        private final String word;
        private Exception exception;
        translateasync(String lang, String word){
            this.lang = lang;
            this.word = word;

        }
        protected String doInBackground(String... urls) {
            String wiki = word;
            try {

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


            }catch (IOException e) {
                e.printStackTrace();
            }
            return wiki;

        }
        protected void onPostExecute(String feed) {
            flagTranslate = true;
        }
    }
    class NewAdapter extends BaseAdapter {

        List<String> list;

        NewAdapter(List list){
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size()-1;
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
    private AdapterView.OnItemSelectedListener articleListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            article = listArticle.get(i);
            if(!article.equals("Article")){
                articleSpinner.setBackgroundResource(android.R.drawable.screen_background_dark_transparent);
            }else {
                articleSpinner.setBackgroundResource(R.drawable.back);
            }

            System.out.println(article);


        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) { }};
    private AdapterView.OnItemSelectedListener typeListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


            type = list.get(i);
            System.out.println(type);
            if(type.equals("Nomen")){

                articleSpinner.setVisibility(View.VISIBLE);
                plural.setVisibility(View.VISIBLE);
                comma.setVisibility(View.VISIBLE);
            }
            else {
                articleSpinner.setVisibility(View.INVISIBLE);
                plural.setVisibility(View.GONE);
                comma.setVisibility(View.GONE);

//                articleSpinner.setSelection(listArticle.size()-1);
            }
            if(!type.equals("Type")){
                typeSpinner.setBackgroundResource(android.R.drawable.screen_background_dark_transparent);
            }else {
                typeSpinner.setBackgroundResource(R.drawable.back);
            }



        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) { }};
    private void addFragment() {

        WiktionaryBtn wik = new WiktionaryBtn();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.btnW, wik)
                .commit();



    }
    @Override
    public void nestedListenerClicked(String mode) {
        System.out.println("Clicked NestedListener");
        List misAt = getMissingAttribute();
        if(!misAt.contains("Word"))
        {
            System.out.println("word text "+word.getText().toString());
            System.out.println("word type "+ type);
            System.out.println("word text "+ article);
            listener.onFragmentListener(new Word("temp",-1,word.getText().toString(),"temp","temp","temp","temp"), "Wiki");

        }else {
            System.out.println("wtf");
        }

    }
    List getMissingAttribute(){
        missingAttribute = new ArrayList<>();
        if(word.getText().toString().equals("")){
            missingAttribute.add("Word");
            System.out.println("missing Word");
        }
        if(type.equals("Type")){
            missingAttribute.add("Type");
            System.out.println("missing Type");
        }
        if (type.equals("Nomen") && article.equals("Article")){
            missingAttribute.add("Article");
            System.out.println("missing Article");
        }
        if(type.equals("Nomen") && plural.getText().toString().equals("")){
            missingAttribute.add("Plural");
            System.out.println("missing Plural");
        }
        if(ratet.equals("")){
            missingAttribute.add("Rate");
        }




        return missingAttribute;
    }


























































//    private View.OnClickListener printListener = new View.OnClickListener() {
//        public void onClick(View v) {
//            Log.d("mpika","mpika");
//            Cursor d = mDatabaseHelper.getData("");
//            ArrayList<Cursor> listData = new ArrayList<>();
//            while (d.moveToNext()) {
//
//                //Log.d("Word     ", d.getString(3));
//
//                String de = d.getString(0);
//                String gr = d.getString(1);
//                String en = d.getString(2);
//                String th = d.getString(3);
//                String typeSpinner = d.getString(4);
//                Log.d("Databate\n","\t"+de+"\t"+en+"\t"+gr+"\t"+th+"\t"+typeSpinner);
//
//                listData.add(d);
//            }
//            Log.d("Insert    ","btn");
//            Intent myIntent = new Intent(getActivity(), PrintFragment.class);
//
//            InsertFragment.this.startActivity(myIntent);
//
//
//        }
//
//    };
//    private View.OnClickListener deleteListener = new View.OnClickListener() {
//
//        public void onClick(View v) {
//            Log.d("Database\n","delete");
//            mDatabaseHelper.delete();
//
//            listener.onInsertListener();
//        }
//    };




}
