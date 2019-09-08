package com.example.marmi.cardschool.data;

public class Language {
    public String en = "en";

    public String source = "de";
    public String target = "en";
    public Word word;

     public Language(Word word,String source, String target){
        this.word = word;
        this.target = target;
    }
}
