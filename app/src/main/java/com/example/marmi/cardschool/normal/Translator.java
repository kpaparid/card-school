package com.example.marmi.cardschool.normal;

import android.os.AsyncTask;
import android.util.Log;

import com.example.marmi.cardschool.data.Language;
import com.example.marmi.cardschool.data.Word;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class Translator extends AsyncTask<Language, String, String> {

    @Override
    protected String doInBackground(Language... languages) {
        // this might take a while ...
        String translated = "";
        String target = languages[0].target;
        String wordText = languages[0].word.getWiki();
        String source = languages[0].source;

        if (!languages[0].word.getIAmTranslated(target)){
            try {
                translated = translator(wordText,source,target);
                Log.e("Translated ",translated);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            translated = languages[0].word.getTranslated(target);
            Log.e("NON - Translated ",translated);
        }

        return translated;
    }




    private String translator(String text, String source, String target) throws IOException {

//        Log.e("hi","translator");
        //String text = "Hello world!";
        //Translated text: Hallo Welt!
        //String entr = translate("de","en",text);
//        int index = 0;
//        index = text.indexOf(",");
//
//        if(index!=-1){
//            //System.out.println(index);
//            text=text.substring(0,index);
//        }

        return translate(source,target,text);
    }
    private  String translate(String langFrom, String langTo, String text) throws IOException {
        // INSERT YOU URL HERE
        String urlStr = "https://script.google.com/macros/s/AKfycbyrvkKW6iSQtj4F7zLSknEJlQYAU-mdis60YcOp2dJJS1iIlBsD/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo +
                "&source=" + langFrom;
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
        return response.toString();
    }



}