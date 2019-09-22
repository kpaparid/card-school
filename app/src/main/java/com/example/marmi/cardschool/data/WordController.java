package com.example.marmi.cardschool.data;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.example.marmi.cardschool.normal.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.ArrayList;

public class WordController implements Serializable {

    private WordModel wordModel;
    private Cursor dtb;

//    public WordController(WordModel wordModel, WordView view){
        public WordController(){
            this.wordModel = new WordModel();

    }
    public WordController(Cursor row){
        this.wordModel = new WordModel();
        importWord(row);
    }

    public void initDB(String query, Context context){
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(context);
        dtb = mDatabaseHelper.getData(query);
        if(dtb==null){
            System.out.println("Reading Database cause Null");
            mDatabaseHelper.readData(mDatabaseHelper, context);
            dtb = mDatabaseHelper.getData(query);
        }
        mDatabaseHelper.close();
        importWord(dtb);
    }


    public void importWord(Cursor row){
        String type = row.getString(0);
        int rate = row.getInt(1);
        String text = row.getString(2);
        String en_translated = row.getString(3);
        String gr_translated = row.getString(4);
        String hr_translated = row.getString(5);
        String sr_translated = row.getString(6);
        String plural = "";
        if(type.equals("Nomen")){
            plural = text.substring(text.indexOf("-")+1);
            //  System.out.println(" Plural " +plural);
        }
        setWordText(text);
        setType(type);
        setRate(Integer.toString(rate));
        setEn_translated(en_translated);
        setGr_translated(gr_translated);
        setHr_translated(hr_translated);
        setSr_translated(sr_translated);
        setPlural(plural);
        setWiki(wordModel.getWordText());
        setArticle();
        setColor();

    }







    public String getWordText() { return wordModel.getWordText(); }
    public String getType(){
        return wordModel.getType();
    }
    public String getRate() {
        return wordModel.getRate();
    }
    public String getArticle(){return wordModel.getArticle();}
    public String getPlural() { return wordModel.getPlural(); }
    public String getWiki(){
        return wordModel.getWiki();
    }
    public int getColor() {
        return wordModel.getColor();
    }
    public String getEn_translated(){
        return wordModel.getEn_translated();
    }
    public String getGr_translated(){
        return wordModel.getGr_translated();
    }
    public String getHr_translated(){
        return wordModel.getHr_translated();
    }
    public String getSr_translated(){
        return wordModel.getSr_translated();
    }









    public void setWordText(String text) {
        wordModel.setWordText(text);
    }
    public void setType(String type){
        wordModel.setType(type);
    }
    public void setRate(String rate) {
        wordModel.setRate(rate);
    }
    public void setArticle(){
        String article = "";
        if (wordModel.getType().equals("Nomen")){
            article = wordModel.getWordText().substring(0,3);
        }
        wordModel.setArticle(article);
    }
    public void setWiki(String word){
        String wiki = word;

        if(wordModel.getType().equals("Nomen")){
            int index = wiki.indexOf(",");
            if(index ==-1 ){
                String article = wiki.substring(0,3);
                if (article.contains("der")||article.contains("die")||article.contains("das")){
                    wiki = wiki.substring(4);
                }

            }
            else {
                wiki = wiki.substring(4,index);
            }
        }
        else if(wordModel.getType().equals("Verb")){
            //System.out.println("wiki "+wiki);
            if(wiki.contains("|")){
                wiki = wiki.replace("|","");
            }
            ArrayList<String> refl = new ArrayList<String>();
            refl.add("sich");
            refl.add("sich");
            refl.add("sein");
            refl.add("über");
            refl.add("zu");
            refl.add("mit");
            refl.add("an");
            refl.add("bei");
            refl.add("für");
            refl.add("auf");
            refl.add("uf");
            refl.add("vor");
            refl.add("von");
            refl.add("um");
            refl.add("jdn");
            refl.add("jdm");
            String[] parts = wiki.split(" ");
            int i = parts.length-1;
            wiki = parts[i];

            while (wiki.contains(".")||wiki.contains("/")||refl.contains(wiki)){

                wiki = parts[parts.length-i];
                i++;
            }
        }
        wordModel.setWiki(wiki);
    }
    public void setPlural(String plural) {
        wordModel.setPlural(plural);
    }
    public void setColor() {
        if (wordModel.getType().equals("Adjektiv")){
            wordModel.setColor(Color.parseColor("#f4b942"));
        }
        else if (wordModel.getType().equals("Nomen")){
            wordModel.setColor(Color.parseColor("#4286f4"));
        }else if (wordModel.getType().equals("Verb")){
            wordModel.setColor(Color.parseColor("#03A287"));
        }
    }
    public void setEn_translated(String text){ wordModel.setEn_translated(text); }
    public void setGr_translated(String text){ wordModel.setGr_translated(text); }
    public void setHr_translated(String text){ wordModel.setHr_translated(text); }
    public void setSr_translated(String text){ wordModel.setSr_translated(text); }


        public String getTranslated(String target) {


                switch (target) {
                    case "en": {
                        return getEn_translated();

                    }
                    case "el": {
                        return getGr_translated();

                    }
                    case "hr": {
//                    System.out.println("HRRRRRRRRRRRRRRRRRRR RE");
                        return getHr_translated();

                    }
                    case "sr": {
                        return getSr_translated();

                    }
                }

            return "ERROR";
        }


    public void moveToNext() {
        if (!dtb.moveToNext()) {
            System.out.println("move to first");
            dtb.moveToFirst();
        }
        importWord(dtb);
    }

    public void moveToPrevious() {
        if (!dtb.moveToFirst()) {
            System.out.println("move to first");
            dtb.moveToFirst();
        }
        importWord(dtb);
    }
}
