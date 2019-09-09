package com.example.marmi.cardschool.data;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import com.example.marmi.cardschool.normal.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.ArrayList;

public class Word implements Serializable {
    private String text;
    private String type;            //1 verb; 2 Nomen; 3 Adj;
    private String en_translated;
    private String gr_translated;
    private String hr_translated;
    private String sr_translated;
    private String plural;
    private String theme;
    private String ID;
    private Integer rate;
    private boolean flag;
    private int color;
    private boolean en;
    private boolean gr;
    private boolean hr;
    private boolean se;
    private ArrayList<String> archivedLanguages;


    private String translated;


    public Word(Cursor row){

        flag = Boolean.FALSE;
        archivedLanguages = new ArrayList<>();

        type = row.getString(0);
        rate = row.getInt(1);
        text = row.getString(2);
        en_translated = row.getString(3);
        gr_translated = row.getString(4);
        hr_translated = row.getString(5);
        sr_translated = row.getString(6);
        if(type.equals("Nomen")){
            plural = text.substring(text.indexOf("-")+1);
          //  System.out.println(" Plural " +plural);
        }


       setNewWord();
    }
    public Word(String type,Integer rate, String text, String en_translated, String gr_translated, String hr_translated, String sr_translated){
        this.text = text;
        this.rate = rate;
        this.type = type;
        this.en_translated = en_translated;
        this.gr_translated = gr_translated;
        this.hr_translated = hr_translated;
        this.sr_translated = sr_translated;

        if(type.equals("Nomen")){
            plural = text.substring(text.indexOf("-")+1);

        }
        setNewWord();



    }
    void setNewWord(){
        flag = Boolean.FALSE;
        archivedLanguages = new ArrayList<>();
        checkTranslated();
//        getArticle();
        if (type.equals("Adjektiv")){
            color = Color.parseColor("#f4b942");
//            Log.e("Word mpika  ",type);
        }
        else if (type.equals("Nomen")){
            //Log.e("Word mpika  ",type);
            color = Color.parseColor("#4286f4");
        }else if (type.equals("Verb")){
            //Log.e("Word mpika  ",type);
            color = Color.parseColor("#03A287");
        }else {

          //  Log.e("Type","Cant identify");
        }


//        Log.e("Word de ",getWordText());
//        Log.e("Word en_translated ",en_translated);
//        Log.e("Word gr_translated ",gr_translated);
//        Log.e("Word hr_translated ",hr_translated);
//        Log.e("Word sr_translated ",sr_translated);
//        Log.e("Word type ",type);
//        Log.e("Word rate ",rate.toString());
//        //Log.e("Word wiki ",getWiki());
//        Log.i("","///////////////////////////////////");



        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        exportDir.mkdirs();
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "translator.txt");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));




            String c0 = type;
            String c1 = rate.toString();
            String c2 = text;
            String c3 = en_translated;
            String c4 = gr_translated;
            String c5 = hr_translated;
            String c6 = sr_translated;


            String arrStr[] ={c0,c1,c2,c3,c4,c5,c6};


            csvWrite.writeNext(arrStr);

        }
        catch(Exception sqlEx)
        {
            Log.e("Menu", sqlEx.getMessage(), sqlEx);
        }

    }

    public String getArticle(){
        String article = "";
        if (type.equals("Nomen")){
            article = text.substring(0,3);
        }


//        System.out.println(article);
        return article;
    }
    public void setTranslated(String translated) {
        this.translated = translated;
    }
    public String getTranslated(String target) {

        if (archivedLanguages.contains(target)){
            switch (target) {
                case "en": {
                    return en_translated;

                }
                case "el": {
                    return gr_translated;

                }
                case "hr": {
//                    System.out.println("HRRRRRRRRRRRRRRRRRRR RE");
                    return hr_translated;

                }
                case "sr": {
                    return sr_translated;

                }
            }
        }


        return translated;
    }
    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }
    public String getWordText() {

        return text;
    }
    public String getPlural() {

        return plural;
    }
    public String getEn_translated(){
        return en_translated;
    }
    public String getGr_translated(){
        return gr_translated;
    }
    public String getHr_translated(){
        return hr_translated;
    }
    public String getSr_translated(){
        return sr_translated;
    }



    public void setWordText(String text) {
        this.text = text;
    }
    public int getRate() {
        return rate;
    }
    public void setRate(Integer rate) {
        this.rate = rate;
    }
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getWiki(){
        String wiki = getWordText();
//        wiki = "die Arbeitserlaubnis";

//

        if(type.equals("Nomen")){
            //System.out.println("GetWordText "+wiki);
            int index = wiki.indexOf(",");
            if(index ==-1 ){
                wiki = wiki.substring(4);
            }
            else {
                wiki = wiki.substring(4,index);
            }

        }
        else
            if(type.equals("Verb")){
//            wiki.replace(".","");
//            wiki.replace("jdn","");
//            wiki.replace("etw","");
//            wiki.replace("um","");
                System.out.println("wiki "+wiki);
                if(wiki.contains("|")){
                    wiki = wiki.replace("|","");
                }


//            wiki.replace("jdm","");
//            wiki.replace("etwas","");

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
//            etw. schadet jdm.
//            sich anmelden


        }
       // System.out.println(wiki);
        return wiki;
    }




    public boolean getIAmTranslated(String target) {
        if (archivedLanguages.contains(target)){
            return true;

        }
        return false;
    }
    public boolean checkTranslated(){

        if(!en_translated.equals("")){
            archivedLanguages.add("en");
            en = false;
        }
        if(!gr_translated.equals("")){
            gr = false;
            archivedLanguages.add("el");

        }
        if(!hr_translated.equals("")){
            hr = false;
            archivedLanguages.add("hr");

        }
        if(!sr_translated.equals("")){
            se = false;
            archivedLanguages.add("sr");

        }



        return true;
    }





}
