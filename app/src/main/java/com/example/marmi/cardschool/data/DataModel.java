package com.example.marmi.cardschool.data;

import android.content.Context;
import android.database.Cursor;

import java.io.Serializable;

public class DataModel implements Serializable {

    private Cursor dtb;
    private WordController wc;
    private String from;
    private String to;
    private String mode;
    private String target = "en";
    private Context context;


    public void setContext(Context context){
        this.context = context;
    }

    public DataModel(String query){
        initDB(" WHERE rate >= " + from + " AND rate <= " + to +" "+ mode + " ORDER BY RANDOM()");
    }
    public DataModel(Cursor dtb){
        this.dtb = dtb;
    }
    public String getText(){
        return dtb.getString(2);
    }

    public void initDB(String query){
        DatabaseHelper mDatabaseHelper = new DatabaseHelper(context);
        dtb = mDatabaseHelper.getData(query);
        if(dtb==null){
            System.out.println("Reading Database cause Null");
            mDatabaseHelper.readData(mDatabaseHelper, context);
            dtb = mDatabaseHelper.getData(query);
        }
        mDatabaseHelper.close();
    }
    public void dtbMoveNext(){
        dtb.moveToNext();
    }
    public void dtbMovePrevious(){
        dtb.moveToPrevious();
    }
    public void dtbMoveFirst(){
        dtb.moveToFirst();
    }


    public String getTranslated(String target) {
        return wc.getTranslated(target);
    }
}
