package com.example.marmi.cardschool.normal;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.Word;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class CSVReader {
    Context context;
    String fileName;
    DatabaseHelper Database;
    List<Word> rows = new ArrayList<>();

    public CSVReader(Context context,DatabaseHelper Database, String Path) throws IOException {
        this.context = context;
        this.fileName = fileName;
        this.Database = Database;

        if(Path.equals("Asset"))
            readAsset();
        else if (Path.equals("Local")){
            readLocal();
        }else if (Path.equals("input")){
            try {
                readInput();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
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
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }
    private String translate(String lang, String word) {
//        String wiki = getWiki(c2,c0);
        String wiki = word;
        System.out.println(wiki);
        try {

            String urlStr = "https://script.google.com/macros/s/AKfycbyrvkKW6iSQtj4F7zLSknEJlQYAU-mdis60YcOp2dJJS1iIlBsD/exec" +
                    "?q=" + URLEncoder.encode(wiki, "UTF-8") +
                    "&target=" + "en" +
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
    public void readInput() throws IOException, ExecutionException, InterruptedException {

    Log.e("Database ", "Reading Input");
    InputStream is = context.getAssets().open("insert.txt");
    InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
    BufferedReader br = new BufferedReader(isr);
    String line;
    String csvSplitBy = ";";


    System.out.println("translating");
    Integer c = 1;
    while ((line = br.readLine()) != null) {
        System.out.println("Count "+c);
        c++;
        String[] row = line.split(csvSplitBy);
        //System.out.println(row[0]);

        /**
         * @param row0 german
         * @param row1 english
         * @param row2 greek
         * @param row3 sector
         * @param row4 verb
         * @param row5 difficulty
         */


        String type= row[0];
        Integer rate = Integer.parseInt(row[1]);
        String de = row[2];
        String dev = de;

         if(type.equals("Verb")) {
             dev = "wir " + dev;
             System.out.println(de);
         }
         else if (type.equals("Nomen")){
             dev = de.substring(0,de.indexOf(","));
         }


        String en = new translateasync("en",dev).execute().get();
        en = en.replace("we ","");
        en = en.replace("the ","");
        String gr = new translateasync("el",dev).execute().get();
        gr = gr.replace("το ","");
        String hr = new translateasync("hr",dev).execute().get();
        hr = hr.replace("mi ","");
        String sr = new translateasync("sr",dev).execute().get();
        sr = sr.replace("ми ","");





        Database.addData(type,rate,de,en,gr,hr,sr);
        Log.e("translate", "de:  " + dev);
        Log.e("translate", "en:  " + en);
        Log.e("translate", "el:  " + gr);
        Log.e("translate", "hr:  " + hr);
        Log.e("translate", "sr:  " + sr);

    }
    System.out.println("finish adding");
    Database.exportDB();

    br.close();
    /**
     * export Database in local csv
     */

}
    public void readAsset() throws IOException {

        Log.e("Database ", "Reading Asset");
        InputStream is = context.getAssets().open("prototypeFile.txt");
        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String line;
        String csvSplitBy = ";";


        while ((line = br.readLine()) != null) {

            String[] row = line.split(csvSplitBy);
            //System.out.println(row[0]);

            /**
             * @param row0 german
             * @param row1 english
             * @param row2 greek
             * @param row3 sector
             * @param row4 verb
             * @param row5 difficulty
             */

            Database.addData(row[0],Integer.parseInt(row[1]),row[2],row[3],row[4],row[5],row[6]);
        Log.e(TAG, "row0:  " + row[0]);
        Log.e(TAG, "row1:  " + row[1]);
        Log.e(TAG, "row2:  " + row[2]);
        Log.e(TAG, "row3:  " + row[3]);
        Log.e(TAG, "row4:  " + row[4]);
        Log.e(TAG, "row5:  " + row[5]);

        }

        br.close();
        /**
         * export Database in local csv
         */

        Database.exportDB();
    }
    public void readLocal() {

    Log.e("Database ", "Reading Local");


    File exportDir = new File(Environment.getExternalStorageDirectory(), "");
    if (!exportDir.exists())
    {
        exportDir.mkdirs();
    }

    File file = new File(exportDir, "exportedFile.txt");


    StringBuilder text = new StringBuilder();

    try {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String csvSplitBy = ";";

        while ((line = br.readLine()) != null) {
            String[] row = line.split(csvSplitBy);


            /**
             * @param row0 german
             * @param row1 english
             * @param row2 greek
             * @param row3 sector
             * @param row4 verb
             * @param row5 difficulty
             */
                    Log.e("e", "row0:  " + row[0]);
        Log.e("e", "row1:  " + row[1]);
        Log.e("e", "row2:  " + row[2]);
        Log.e("e", "row3:  " + row[3]);
        Log.e("e", "row4:  " + row[4]);
        Log.e("e", "row5:  " + row[5]);
            Database.addData(row[0],Integer.parseInt(row[1]),row[2],row[3],row[4],row[5],row[6]);
        }
        br.close();
    }
    catch (IOException e) {
        //You'll need to add proper error handling here
    }


    }
}


