package activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.WordController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ImportsFragment extends Fragment {


    private String en;
    private String gr;
    private String hr;
    private String sr;
    private DatabaseHelper mDatabaseHelper;

    private String type = " ";
    private Integer rate;
    private String de;
    //private Context context;
    private View v;
    private EditText text;

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


        return v;
    }

    private void readInput2() {

        String plainText = text.getText().toString();
        String[] words = plainText.split("\n");
        int c = 0;
        while (words.length>c){
            System.out.println("c "+words[c]);
            if(!words[c].equals(" "))
            mDatabaseHelper.addData(" ",200,words[c]," "," "," "," ");
            c++;
        }
    }


    private View.OnClickListener endListener = new View.OnClickListener() {
        public void onClick(View v) {
            System.out.println("finish adding");
            //mDatabaseHelper.exportDB();
            //mDatabaseHelper.exportImportedDB();
            readInput2();
            //getActivity().getSupportFragmentManager().popBackStack();
        }
    };


    class translateasync extends AsyncTask<String, Void, String[]> {



        private String word;
        private String type;
        translateasync(String word, String type){
            if(word.contains("(")&&word.contains("+")){
                System.out.println(word);
                word = word.substring(0,word.indexOf("(")-1);
            }
            this.word = word;
            this.type = type;
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

            //System.out.println("pre execute");
        }



        protected String[] doInBackground(String... urls) {

            String[] wiki = new String[5];
            try {
                wiki[0] = word;
//                wiki[1] = translate("en");
//                wiki[2] = translate("el");
//                wiki[3] = translate("hr");
//                wiki[4] = translate("sr");

                wiki[1] = " ";
                wiki[2] = " ";
                wiki[3] = " ";
                wiki[4] = " ";


            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(word);
            return wiki;

        }
        protected void onPostExecute(String[] feed) {

            System.out.println("post execute");
            de = feed[0];
            en = feed[1];
            gr = feed[2];
            hr = feed[3];
            sr = feed[4];

            en = en.replace("we ","");
            en = en.replace("the ","");
            gr = gr.replace("το ","");
            hr = hr.replace("mi ","");
            sr = sr.replace("ми ","");
            mDatabaseHelper.addData(type,rate,de,en,gr,hr,sr);
            Log.e("translate", "////////");
            Log.e("translate","rate:    "+rate.toString());
            Log.e("translate", "type:    "+type);
            Log.e("translate", "de:  " + de);
            Log.e("translate", "en:  " + en);
            Log.e("translate", "el:  " + gr);
            Log.e("translate", "hr:  " + hr);
            Log.e("translate", "sr:  " + sr);

        }
    }
    public void readInput() throws IOException {

        Log.e("Database ", "Reading Input");
        InputStream is = getContext().getAssets().open("insert.txt");

        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);



        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, "imports.docx");
        br = new BufferedReader(new FileReader(file));


        String line;
        String csvSplitBy = ";";


        System.out.println("translating");
        Integer c = 1;
        while ((line = br.readLine()) != null) {
       // while (c<10){
            //line = br.readLine();
            type = "";
            de = "";
            //System.out.println("Line "+c);
            c++;
            String[] row = line.split(csvSplitBy);
            System.out.println("s"+row[0]);
            de= row[0];
            rate = 200;
            if(row.length>1){
                //System.out.println("parts "+row.length);
                type = row[1];

            }
            String dev = de;
            switch (type) {
                case "Verb":
                    dev = "wir " + dev;
                    System.out.println(de);
                    break;
                case "Nomen":
                    dev = de.substring(0, de.indexOf(","));
                    break;
                default:
                    //System.out.println("empty");
                    type = " ";
                    String article;
                    if(de.length()>4){
                        article = de.substring(0,4);
                        //System.out.println("nomen "+article);
                        if (article.equals("der ")||article.equals("die ")||article.equals("das ")){
                            type = "Nomen";
                        }
                    }
            }
            new translateasync(dev,type).execute();
        }
        br.close();
        /**
         * export Database in local csv
         */

    }



}
