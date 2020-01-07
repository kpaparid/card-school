package activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.WordController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ImportsFragment extends Fragment {



    private DatabaseHelper mDatabaseHelper;

    private String type = " ";
    private String de;
    //private Context context;
    private View v;
    private EditText text;
    private EditText rateInput;
    private ProgressBar pb;
    private RecyclerView recyclerView;
    private ArrayList<WordController> list;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        LayoutInflater inflator = inflater;
        v = inflater.inflate(R.layout.texts_import, container, false);
        mDatabaseHelper = new DatabaseHelper(getContext());

//        try {
//            readInput();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        text = v.findViewById(R.id.text);
        Button end = v.findViewById(R.id.end);
        end.setOnClickListener(endListener);

        rateInput = v.findViewById(R.id.rate);
        pb = v.findViewById(R.id.pB);
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<WordController> list = new ArrayList<>();
        WordController wc = new WordController();
        wc.importWord(new String[]{" ",rateInput.getText().toString(),"empty"," "," "," "," ","0"});


        CustomAdapter adapter = new CustomAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        text.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0){

                    readInput2();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


            }
        });


        return v;
    }

    private void readInput2() {

        String plainText = text.getText().toString();
        String[] words = plainText.split("\n");
        int lineNum = 0;

        String type = " ";
        list = new ArrayList<>();
        while (words.length>lineNum){
            //System.out.println("c "+words[lineNum]);

            if(!words[lineNum].equals(" ")&&!words[lineNum].equals(""))
            {
                WordController wc = new WordController();
                wc.importWord(new String[]{type,rateInput.getText().toString(),words[lineNum]," "," "," "," ","0"});
                list.add(wc);
         }
            lineNum++;
        }

        CustomAdapter adapter = new CustomAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
    }


    private View.OnClickListener endListener = new View.OnClickListener() {
        public void onClick(View v) {
            System.out.println("finish adding");
            //readInput2();
            //new Reading().execute(text.getText().toString());
            new translateasy(list).execute();
        }
    };

    class Reading extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute(){
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... strings) {

            String plainText = strings[0];
            String[] words = plainText.split("\n");
            int lineNum = 0;
            String type = " ";
            while (words.length>lineNum){
                if(!words[lineNum].equals(" ")&&!words[lineNum].equals(""))
                    new translateasync(words[lineNum],type,rateInput.getText().toString()).execute();
                    //mDatabaseHelper.addData(type,Integer.parseInt(rateInput.getText().toString()),words[lineNum]," "," "," "," ");
                lineNum++;
            }
            mDatabaseHelper.exportDB();
            mDatabaseHelper.exportImportedDB();
            mDatabaseHelper.close();
            String[] finish = new String[4];
            finish[0] = "finished";
            return finish;
        }
        @Override
        protected void onPostExecute(String[] feed){
            if(feed[0] == "finished"){
                pb.setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().popBackStack();
            }

        }

    }


    class translateasy extends AsyncTask<String, Void, String[]> {

        private final ArrayList<WordController> words;

        translateasy(ArrayList words){
            this.words = words;

        }

        protected String translate(String lang, String word) throws Exception{
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
            System.out.println("pre execute");
            pb.setVisibility(View.VISIBLE);
        }



        protected String[] doInBackground(String... urls) {

            int lineNum = 0;
            while (words.size()>lineNum){
                if(!words.get(lineNum).equals(" ")&&!words.get(lineNum).equals("")){
                    String en = "";
                    String gr = "";
                    String hr = "";
                    String sr = "";
                    String type = words.get(lineNum).getType();
                    String de = words.get(lineNum).getFullWordText();
                    try {
                        en = translate("en",de);
                        gr = translate("el",de);
                        hr = translate("hr",de);
                        sr = translate("sr",de);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    en = en.replace("we ","");
                    en = en.replace("the ","");
                    gr = gr.replace("το ","");
                    hr = hr.replace("mi ","");
                    sr = sr.replace("ми ","");

                    mDatabaseHelper.addData(type,Integer.parseInt(rateInput.getText().toString()),de,en,gr,hr,sr);
                    Log.e("translate", "de:  " + de);
                    Log.e("translate", "en:  " + en);
                    Log.e("translate", "el:  " + gr);
                    Log.e("translate", "hr:  " + hr);
                    Log.e("translate", "sr:  " + sr);
                }
                lineNum++;
            }

            return null;

        }
        protected void onPostExecute(String[] feed) {
            pb.setVisibility(View.GONE);
            mDatabaseHelper.exportDB();
            mDatabaseHelper.exportImportedDB();
            mDatabaseHelper.close();
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    class translateasync extends AsyncTask<String, Void, String[]> {

        private final String word;
        private final String type;
        private final String rate;
        private String en;
        private String gr;
        private String hr;
        private String sr;

        translateasync(String word, String type, String rate){
            this.word = word;
            this.type = type;
            this.rate = rate;
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
            System.out.println("pre execute");
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
            en = feed[0];
            gr = feed[1];
            hr = feed[2];
            sr = feed[3];

            en = en.replace("we ","");
            en = en.replace("the ","");
            gr = gr.replace("το ","");
            hr = hr.replace("mi ","");
            sr = sr.replace("ми ","");

            //wc.importWord(new String[]{type,rate,word,en,gr,hr,sr,"0"});
            mDatabaseHelper.addData(type,Integer.parseInt(rate),word,en,gr,hr,sr);


            Log.e("translate", "de:  " + word);
            Log.e("translate", "en:  " + en);
            Log.e("translate", "el:  " + gr);
            Log.e("translate", "hr:  " + hr);
            Log.e("translate", "sr:  " + sr);



        }
    }
    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        private List<WordController> mData;
        private LayoutInflater mInflater;


        // data is passed into the constructor
        public CustomAdapter(Context context, List<WordController> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
        }

        // inflates the row layout from xml when needed
        @Override
        public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.import_item, parent, false);
            return new ViewHolder(view);
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(CustomAdapter.ViewHolder holder, int position) {

            String de = mData.get(position).getWordText();
            String ty = mData.get(position).getType();


            if(ty.equals("Nomen")){
                de = mData.get(position).getArticle()+" "+de+",-"+mData.get(position).getPlural();
            }

            //System.out.println(de);
            holder.des.setText(de);
            holder.types.setText(ty);


        }

        @Override
        public int getItemCount() {
            return mData.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView des;
            TextView types;

            ViewHolder(View itemView) {
                super(itemView);
                des = itemView.findViewById(R.id.de);
                types = itemView.findViewById(R.id.type);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

            }
        }

    }



}
