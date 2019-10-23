package com.example.marmi.cardschool.normal;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.marmi.cardschool.data.DatabaseHelper;

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

import static android.content.ContentValues.TAG;

public class CSVReader {
    Context context;
    DatabaseHelper Database;
    String en;
    String gr;
    String hr;
    String sr;
    String type;
    int rate;
    String de;
    File file;
    public CSVReader(Context context,DatabaseHelper Database) throws IOException {
        this.context = context;
        this.Database = Database;

        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }
        file = new File(exportDir, "exportedFile.txt");
        if(file.exists()){
            readLocal();
        }else {
            readAsset();
        }


    }
    class translateasync extends AsyncTask<String, Void, String[]> {

        private final String word;
        translateasync(String word){
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
            Database.addData(type,rate,de,en,gr,hr,sr);
            Log.e("translate", "de:  " + de);
            Log.e("translate", "en:  " + en);
            Log.e("translate", "el:  " + gr);
            Log.e("translate", "hr:  " + hr);
            Log.e("translate", "sr:  " + sr);



        }
    }
    public void readInput() throws IOException {

    Log.e("Database ", "Reading Input");
    InputStream is = context.getAssets().open("insert.txt");
    InputStreamReader isr = new InputStreamReader(is);
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


        type= row[0];
        rate = Integer.parseInt(row[1]);
        de = row[2];
        String dev = de;

         if(type.equals("Verb")) {
             dev = "wir " + dev;
             System.out.println(de);
         }
         else if (type.equals("Nomen")){
             dev = de.substring(0,de.indexOf(","));
         }

         new translateasync(dev).execute();



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
        InputStreamReader isr = new InputStreamReader(is);
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
    public void readLocal() throws IOException {

    Log.e("Database ", "Reading Local");

            BufferedReader br = new BufferedReader(new FileReader(file));
            System.out.println("file "+file.getName());
            String line;
            String csvSplitBy = ";";

            while ((line = br.readLine()) != null) {
                String[] row = line.split(csvSplitBy);
                System.out.println(line);


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








}


