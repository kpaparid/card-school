package com.example.marmi.cardschool.data;

import android.database.Cursor;
import android.graphics.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class WordController implements Serializable {

    private WordModel wordModel;
    //private Cursor dtb;
    private ArrayList<WordModel> dtb2;


//    public WordController(WordModel wordModel, WordView view){
    public WordController(){
            this.wordModel = new WordModel();
    }

    public void setList(ArrayList dtb){

        ListIterator iterator = dtb.listIterator();
        while (iterator.hasNext()){
            importWord2((String[]) iterator.next());
        }
    }
    public ArrayList getList(){

    }

    public WordController(Cursor row){
        this.wordModel = new WordModel();
        importWord(row);
    }


//    public void initDB(String query, Context context){
//        DatabaseHelper mDatabaseHelper = new DatabaseHelper(context);
//        dtb = mDatabaseHelper.getData(query);
//        if(dtb==null){
//            System.out.println("Reading Database cause Null");
//            mDatabaseHelper.readData(mDatabaseHelper, context);
//            dtb = mDatabaseHelper.getData(query);
//        }
//        mDatabaseHelper.close();
//        importWord(dtb);
//    }


    public void importWord(Cursor row){
        String type = row.getString(0);
        int rate = row.getInt(1);
        String text = row.getString(2);
        String en_translated = row.getString(3);
        String gr_translated = row.getString(4);
        String hr_translated = row.getString(5);
        String sr_translated = row.getString(6);
        int id = row.getInt(7);

        System.out.println(id + ": name " + text);

        setWordText(text);
        setType(type);
        setID(id);

        setRate(Integer.toString(rate));
        setEn_translated(en_translated);
        setGr_translated(gr_translated);
        setHr_translated(hr_translated);
        setSr_translated(sr_translated);
        setWiki(wordModel.getWordText());

        setColor();

    }

    public void importWord2(String[] row){
        String type = row[0];
        int rate = Integer.parseInt(row[1]);
        String text = row[2];
        String en_translated = row[3];
        String gr_translated = row[4];
        String hr_translated = row[5];
        String sr_translated = row[6];
        int id = Integer.parseInt(row[7]);
        setWordText(text);
        setType(type);
        setID(id);

        setRate(Integer.toString(rate));
        setEn_translated(en_translated);
        setGr_translated(gr_translated);
        setHr_translated(hr_translated);
        setSr_translated(sr_translated);
        setWiki(wordModel.getWordText());

        setColor();

    }






    public int getID(){return wordModel.getID();}
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
    public String getTranslated(String target) {


        switch (target) {
            case "en": {
                return getEn_translated();

            }
            case "el": {
                return getGr_translated();

            }
            case "hr": {
                return getHr_translated();

            }
            case "sr": {
                return getSr_translated();

            }
        }

        return "ERROR";
    }







    public void setID(int id){wordModel.setID(id);}
    public void setWordText(String text) {
        wordModel.setWordText(text);
    }
    public void setType(String type){
        String plural = "";
        String article = "";


            switch (type){
                case "Nomen":
                case " ":{
                    if(getWordText().length()>4){
                        String subString = getWordText().substring(0,3);
                        //System.out.println("w:"+getWordText()+"     "+subString+"s");
                        switch (subString){
                            case "das":
                            case "die":
                            case "der": {
                                article = subString;
                                type = "Nomen";
                                int index = getWordText().indexOf("-");
                                if(index!= -1){
                                    //System.out.println("index "+index);
                                    plural = getWordText().substring(index+1);

                                }
                                if(getWordText().contains("(Sg.)")){
                                    plural = "Sg";
                                    setWordText(getWordText().replace("(Sg.)",""));
                                    System.out.println(plural);
                                }

                                break;
                            }
                        }
                    }
                }

            }

        setArticle(article);
        setPlural(plural);
        //System.out.println("Type "+type);
        wordModel.setType(type);
    }
    public void setRate(String rate) {
        wordModel.setRate(rate);
    }
    public void setArticle(String article){
        //System.out.println("Article "+article);
        wordModel.setArticle(article);
    }
    public void setWiki(String word){
        String wiki = word;
        if(wiki.contains("(")&&wiki.contains("+")){
            System.out.println(word);
            wiki = wiki.substring(0,wiki.indexOf("(")-1);
        }

        wiki = wiki.replace("/"," ");
        wiki = wiki.replace("("," ");
        wiki = wiki.replace(")"," ");
        wiki = wiki.replace("."," ");
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
        else if(getType().equals("Verb")){

                ArrayList<String> refl = new ArrayList<>();
            refl.add("etwas");
            refl.add("etw");
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
                refl.add("lassen");

                String[] parts = wiki.split(" ");
                int i = 0;

                while (i<parts.length){
                    if(refl.contains(parts[i])){
                        wiki = wiki.replace(parts[i],"");
                        System.out.println("removing "+parts[i]);
                    }
                    i++;
                }



        }
        String[] split = wiki.split(" ");
        wiki = split[split.length-1];

        wordModel.setWiki(wiki);
    }
    public void setPlural(String plural) {
        //System.out.println("Plural "+plural);

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





//    public void moveToNext() {
//        if (!dtb.moveToNext()) {
//            System.out.println("move to first");
//            dtb.moveToFirst();
//        }
//        importWord(dtb);
//    }
//
//    public void moveToPrevious() {
//        if (!dtb.moveToFirst()) {
//            System.out.println("move to first");
//            dtb.moveToFirst();
//        }
//        importWord(dtb);
//    }
}
