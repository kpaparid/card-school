package com.example.marmi.cardschool.data;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.marmi.cardschool.normal.CSVReader;
import com.example.marmi.cardschool.normal.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper implements Serializable {

    private static final String TABLE_NAME = "words_table";
    private static final String COL_TY = "type";
    private static final String COL_RA = "rate";
    private static final String COL_DE = "de_text";
    private static final String COL_EN = "en_text";
    private static final String COL_GR = "gr_text";
    private static final String COL_HR = "hr_text";
    private static final String COL_SR = "sr_text";

    private static final String COL_ID = "id";
    public static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + " ("
            // SQL -> String
            + COL_TY + " text not null,"
            + COL_RA + " Int not null,"
            + COL_DE + " text not null,"
            + COL_EN + " text not null,"
            + COL_GR + " text not null,"
            + COL_HR + " text not null,"
            + COL_SR + " text not null,"
            + COL_ID + " integer primary key autoincrement"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 9);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS words_table");
        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        onCreate(db);
    }

    public void addData(String type, Integer rate, String deText, String enText, String grText, String hrText, String srText) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TY, type);    //5
        contentValues.put(COL_RA, rate);    //5
        contentValues.put(COL_DE, deText);     //1
        contentValues.put(COL_EN, enText);  //2
        contentValues.put(COL_GR, grText);  //3
        contentValues.put(COL_HR, hrText);
        contentValues.put(COL_SR, srText);

        long result;
        result = db.update(TABLE_NAME, contentValues, COL_DE + "=?", new String[]{deText});

        if (result == 0) {
            db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    /**
     * Returns all the data from database
     */

    public ArrayList getData(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "SELECT * FROM " + TABLE_NAME + query;
        Cursor data = db.rawQuery(q, null);
        ArrayList items = new ArrayList<>();
        if (data.moveToFirst()) {
            do {
                String[] row = new String[8];
                row[0] = data.getString(0);
                row[1] = Integer.toString(data.getInt(1));
                row[2] = data.getString(2);
                row[3] = data.getString(3);
                row[4] = data.getString(4);
                row[5] = data.getString(5);
                row[6] = data.getString(6);
                row[7] = Integer.toString(data.getInt(7));
                items.add(row);
            } while (data.moveToNext());
        }

        data.close();
        return items;
    }

    public WordController getRandom(String query) {

        SQLiteDatabase db = this.getWritableDatabase();
        String q = "SELECT * FROM " + TABLE_NAME + query;

        Cursor data = db.rawQuery(q, null);
        data.moveToFirst();

        WordController randoms = new WordController();
        if (data.moveToFirst()) {
            do {
                String[] row = new String[8];
                row[0] = data.getString(0);
                row[1] = Integer.toString(data.getInt(1));
                row[2] = data.getString(2);
                row[3] = data.getString(3);
                row[4] = data.getString(4);
                row[5] = data.getString(5);
                row[6] = data.getString(6);
                row[7] = Integer.toString(data.getInt(7));
                randoms.importWord(row);
            } while (data.moveToNext());
        }
        data.close();
        return randoms;
    }

    public Cursor shuffle(String query) {

        SQLiteDatabase db = this.getWritableDatabase();
        String q = "SELECT * FROM " + TABLE_NAME + query;
        Cursor data = db.rawQuery(q, null);
        data.moveToFirst();
        return data;
    }

    /**
     * Returns only the ID that matches the name passed in
     *
     * @param name
     * @return
     */
    public Cursor getItemID(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL_TY + " FROM " + TABLE_NAME +
                " WHERE " + COL_DE + " = '" + name + "'";
        return db.rawQuery(query, null);
    }

    /**
     * Updates the name field
     *
     * @param newName
     * @param id
     * @param oldName
     */
    public void updateName(String newName, int id, String oldName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL_DE +
                " = '" + newName + "' WHERE " + COL_TY + " = '" + id + "'" +
                " AND " + COL_DE + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);
        db.execSQL(query);
    }

    /**
     * Delete from database
     *
     * @param id
     */
    public void deleteID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "

                + COL_ID + " = '" + id + "'";
        Log.d(TAG, "deleteID: query: " + query);
        Log.d(TAG, "deleteID: Deleting " + COL_DE + " from database.");
        db.execSQL(query);
    }

    public void delete() {
        Log.e("DATABASE", "Dropping Database");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }

    public boolean checkDataBase(Context context) {

        Boolean rowExists = Boolean.FALSE;
        Log.e("DATABASE", "Checking Database");
        File dbFile = context.getDatabasePath(TABLE_NAME);
        if (dbFile.exists()) {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            rowExists = mCursor.moveToFirst();
            mCursor.close();


        }
        return rowExists;
    }

    public void exportDB() {

        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        exportDir.mkdirs();
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "exportedFile.txt");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            while (curCSV.moveToNext()) {
                String c0 = curCSV.getString(0);
                String c1 = curCSV.getString(1);
                String c2 = curCSV.getString(2);
                String c3 = curCSV.getString(3);
                String c4 = curCSV.getString(4);
                String c5 = curCSV.getString(5);
                String c6 = curCSV.getString(6);
                String[] arrStr = {c0, c1, c2, c3, c4, c5, c6};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("Menu", sqlEx.getMessage(), sqlEx);
        }
    }

    public void exportImportedDB() {

        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        exportDir.mkdirs();
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "importedWords.txt");
        if (!file.exists()) {
            exportDir.mkdirs();
        }
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE rate = 200", null);

            while (curCSV.moveToNext()) {


                String c0 = curCSV.getString(0);
                String c1 = curCSV.getString(1);
                String c2 = curCSV.getString(2);
                String c3 = curCSV.getString(3);
                String c4 = curCSV.getString(4);
                String c5 = curCSV.getString(5);
                String c6 = curCSV.getString(6);


                String[] arrStr = {c0, c1, c2, c3, c4, c5, c6};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("Menu", sqlEx.getMessage(), sqlEx);
        }
    }


    public void exportCopyDB() {

        exportDB();

        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        exportDir.mkdirs();
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "backupDB.txt");
        try {
            file.createNewFile();

            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            while (curCSV.moveToNext()) {
                String c0 = curCSV.getString(0);
                String c1 = curCSV.getString(1);
                String c2 = curCSV.getString(2);
                String c3 = curCSV.getString(3);
                String c4 = curCSV.getString(4);
                String c5 = curCSV.getString(5);
                String c6 = curCSV.getString(6);
                String[] arrStr = {c0, c1, c2, c3, c4, c5, c6};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("Menu", sqlEx.getMessage(), sqlEx);
        }
    }


    public void readData(DatabaseHelper mDatabaseHelper, Context context) {
        /**
         * if DB is empty then read from local
         * else create from asset storage
         */
        if (!checkDataBase(context)) {
            try {
                new CSVReader(context, mDatabaseHelper);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
