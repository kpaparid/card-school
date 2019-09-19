package com.example.marmi.cardschool.data;

import android.database.Cursor;

import java.util.ArrayList;

public class WordModel {
    private String text = "";
    private String type = "";
    private String en_translated = "";
    private String gr_translated = "";
    private String hr_translated = "";
    private String sr_translated = "";
    private String plural = "";
    private String article = "";
    private String rate = "";
    private int color = 0;
    private String wiki = "";





    public void setWordText(String text) {
        this.text = text;
    }
    public void setType(String type){
        this.type = type;
    }
    public void setRate(String rate) {
        this.rate = rate;
    }
    public void setArticle(String article) {
        this.article = article;
    }
    public void setWiki(String wiki) {
        this.wiki = wiki;
    }
    public void setPlural(String plural) {
        this.plural = plural;
    }
    public void setColor(int color) {
        this.color = color;
    }
    public void setEn_translated(String text){ this.en_translated = text; }
    public void setGr_translated(String text){ this.gr_translated = text; }
    public void setHr_translated(String text){ this.hr_translated = text; }
    public void setSr_translated(String text){ this.sr_translated = text; }



    public String getWordText() { return text; }
    public String getType(){
        return type;
    }
    public String getRate() {
        return rate.toString();
    }
    public String getArticle() {
        return article;
    }
    public String getWiki() {
        return wiki;
    }
    public String getPlural() { return plural; }
    public int getColor() {
        return color;
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



}
