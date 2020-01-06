package com.example.marmi.cardschool.data;

import android.graphics.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;

public class WordController implements Serializable {

    private WordModel wordModel;

    private ArrayList<WordModel> dtb;
    private int next = 0;


    public WordController(){
            this.wordModel = new WordModel();
            dtb = new ArrayList<>();
    }

    public WordController(WordModel wm){
        this.wordModel = wm;
        setWordAndWiki(wm.getWordText());
    }

    public void setList(ArrayList dtb){

        ListIterator iterator = dtb.listIterator();
        while (iterator.hasNext()){
            importWord((String[]) iterator.next());
        }


        System.out.println(wordModel.getWordText());
    }
    public ArrayList<WordModel> getList(){

        return dtb;
    }
    public void shuffle(Boolean bool){
        if(bool){
            Collections.shuffle(dtb);

        }else {
            Collections.sort(dtb,new CustomComparator());
        }
        wordModel = this.dtb.get(0);
    }


    public void importWord(String[] row){
        wordModel = new WordModel();
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
        setWiki();


        setColor();
        //System.out.println("model "+wordModel.getWordText());
        dtb.add(wordModel);

    }

    private void setWordAndWiki(String Word) {
        setWordText(Word);
        setWiki();
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
    public WordModel getWordModel(){
        return wordModel;
    }
    public String getFullWordText(){
        return wordModel.getFullWordText();
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
                        String subString = getWordText().substring(0,3).toLowerCase();
                        //System.out.println("w:"+getWordText()+"     "+subString);
                        switch (subString){
                            case "das":
                            case "die":
                            case "der": {
                                article = subString;
                                type = "Nomen";

                                setWordText(getWordText().substring(4));
                                //System.out.println("a:"+getWordText()+"     "+subString);
                                int index = getWordText().indexOf(",");
                                if(index == -1){
                                    index = getWordText().indexOf("-");
                                }
                                if (index == -1){
                                    index = getWordText().indexOf("(S");
                                }
                                if (index == -1){
                                    index = getWordText().indexOf("(s");
                                }
                                if(index == -1){
                                    index = getWordText().indexOf("(P");
                                }
                                if(index == -1){
                                    index = getWordText().indexOf("(p");
                                }
                                //System.out.println("index "+index);
                                if(index!= -1){
                                    plural = getWordText().substring(index+1);
                                    //System.out.println("plural pre edit:"+plural);
                                    plural = plural.replace("-","");
                                    plural = plural.replace("(","");
                                    plural = plural.replace(")","");
                                    plural = plural.replace(".","");
                                    plural = plural.replace(",","");
                                    plural = plural.replace(" ","");
                                    //System.out.println("plural after edit:"+plural);
                                    setWordText(getWordText().substring(0,index));
                                    //System.out.println("text after edit:"+getWordText());
                                }
                                break;
                            }
                        }
                    }
                }
            }
            if(type.equals(" ")){
                String text = getWordText();
                System.out.println("test "+text.length());

                if(text.length()>4){
                    text = text.substring(text.length()-4);

                    System.out.println("test "+text);
                    if (text.contains("lich")||text.contains("isch")){
                        System.out.println("Adjektiv3");
                        type = "Adjektiv";
                    }
                }
                if(text.length()>3){
                    text = text.substring(text.length()-3);
                    if (text.contains("nd")||text.contains("ig")){
                        System.out.println("Adjektiv3");
                        type = "Adjektiv";
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
    public void setWiki(){
        String wiki = getWordText();
        if(wiki.contains("(")&&wiki.contains("+")){
            wiki = wiki.substring(0,wiki.indexOf("(")-1);
        }

        wiki = wiki.replace("/"," ");
        wiki = wiki.replace("("," ");
        wiki = wiki.replace(")"," ");
        wiki = wiki.replace("."," ");
        if(wordModel.getType().equals("Nomen")){
            int index = wiki.indexOf(",");
            if(index !=-1 ){
                wiki.substring(0,index);
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



    public void moveToNext() {

        if(dtb.size()!=next){
            next +=1;
            wordModel = dtb.get(next);

        }else {
            next = 0;
            wordModel = dtb.get(next);
        }



    }

    public void moveToPrevious() {
        next -=1;
        System.out.println("previous "+next);
        if(next!=-1){

            wordModel = dtb.get(next);
        }else {
            next = 0;
            wordModel = dtb.get(next);
        }


    }
    public class CustomComparator implements Comparator<WordModel> {

        @Override
        public int compare(WordModel t0, WordModel t1) {
            //System.out.println("comparing "+t0.getWordText()+"      "+t1.getWordText());
            if(t0.getRate().equals( t1.getRate())){
                if(t0.getType().equals(t1.getType())){
                    int i = 0;
                    String text0 = t0.getWordText();
                    String text1 = t1.getWordText();
                    while (text0.charAt(i) == text1.charAt(i)){
                        i++;

                        if(text0.length()<=i||text1.length()<=i){
                            i--;
                            break;
                        }else {
                            //System.out.println("REEEEE "+text0.charAt(i)+"       "+text1.charAt(i));
                        }
                    }
                   // System.out.println("break while");
                    if(text0.charAt(i)>text1.charAt(i)){
                      //  System.out.println("return 1");
                        return 1;
                    }else if(text0.charAt(i)<text1.charAt(i)){
                     //   System.out.println("return -1");
                        return -1;
                    }else {
                       // System.out.println("return 0");
                        return 0;
                    }

                }
               // System.out.println("return type");
               return t0.getType().compareTo(t1.getType());
            }
            int t0r = Integer.parseInt(t0.getRate());
            int t1r = Integer.parseInt(t1.getRate());
            if(t0r>t1r){
                return 1;
            }else {
                return -1;
            }

        }
    }

}
