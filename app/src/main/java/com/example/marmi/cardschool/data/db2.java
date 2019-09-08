//package com.example.marmi.cardschool.data;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.os.Environment;
//import android.util.Log;
//
//import mainFragments.Menu;
//
//import com.example.marmi.cardschool.normal.CSVReader;
//import com.example.marmi.cardschool.normal.CSVWriter;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//
//import static android.content.ContentValues.TAG;
//
//public class DatabaseHelper extends SQLiteOpenHelper {
//
//
//
//
//    private static final String TABLE_NAME = "words_table";
//
//
//
//    private static final String COL_DE = "de_text";
//    private static final String COL_EN = "en_text";
//    private static final String COL_GR = "gr_text";
//    private static final String COL_TH = "theme";
//    private static final String COL_TY = "type";
//    private static final String COL_RA = "rate";
//
//    public static final String DATABASE_CREATE = "create table "
//            + TABLE_NAME + " ("
//            // SQL -> String
//
//            + COL_DE + " text not null,"
//            + COL_EN + " text not null,"
//            + COL_GR + " text not null,"
//
//            + COL_TH + " text not null,"
//            + COL_TY + " text not null,"
//            + COL_RA + " Int not null"
//
//
//            + ")";
//
//    public DatabaseHelper(Context context) {
//        super(context, TABLE_NAME, null, 6);
//
//
//    }
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//
//
//        System.out.println("Database created");
//
//
//        //db.execSQL("DROP TABLE IF EXISTS words_table");
//        db.execSQL(DATABASE_CREATE);
//
//    }
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
//        // TODO Auto-generated method stub
//        // db.execSQL("DROP TABLE IF EXISTS words_table");
//        onCreate(db);
//    }
//    public boolean addData(String dtext, String entext, String grtext, String theme,String type, Integer rate ) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        //contentValues.put(COL_ID, sID);
//
//        contentValues.put(COL_DE, dtext);     //1
//        contentValues.put(COL_EN, entext);  //2
//        contentValues.put(COL_GR, grtext);  //3
//        contentValues.put(COL_TH, theme);   //4
//        contentValues.put(COL_TY, type);    //5
//        contentValues.put(COL_RA, rate);    //5
//
//
//
//
//
////        Log.e(TAG, "addData: dtext "   + " to " + dtext );
////        Log.e(TAG, "addData: entext "   + " to " + entext);
////        Log.e(TAG, "addData: grtext "   + " to " + grtext);
////        Log.e(TAG, "addData: theme "   + " to " + theme);
////        Log.e(TAG, "addData: type "   + " to " + type);
////        Log.e(TAG, "addData: rate "   + " to " + rate);
//        long result ;//= db.insert(TABLE_NAME, null, contentValues);
//
//
////        String query = "UPDATE " + TABLE_NAME + " SET " + COL_EN +
////                " = '" + entext + "' WHERE " + COL_DE + " = '" + dtext + "'";
////
////        db.execSQL(query);
//        result = db.update(TABLE_NAME, contentValues, COL_DE+"=?", new String[]{dtext});
//
//        if (result == 0){
//            db.insertWithOnConflict(TABLE_NAME,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
//        }
//        return true;
////        Log.e(TAG, "addData: result " + result );
////        //if date as inserted incorrectly it will return -1
////        if (result == -1) {
////            Log.e(TAG, "addData: Epese " + dtext + " to " + TABLE_NAME);
////            return false;
////        } else {
////            return true;
////
////        }
//    }
//
//    /**
//     * Returns all the data from database
//     *
//     * @return
//     */
//    public Cursor getData(String query) {
//
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        String q ="SELECT * FROM " + TABLE_NAME +  query;
//        Cursor data = db.rawQuery(q, null);
//        data.moveToFirst();
//        return data;
//    }
//    public Cursor getRandom(Integer n, String query) {
//
//        // Log.e(TAG, "getRandom");
//        SQLiteDatabase db = this.getWritableDatabase();
//        String q ="SELECT * FROM " + TABLE_NAME +  query;
//
//        Cursor data = db.rawQuery(q, null);
//        data.moveToFirst();
//
//        return data;
//    }
//    public Cursor shuffle(String query) {
//
//        Log.e(TAG, "Shuffle");
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        String q ="SELECT * FROM " + TABLE_NAME +  query;
//
//        Cursor data = db.rawQuery(q, null);
//        data.moveToFirst();
//
//
//
//
//
//
//        //data.close();
//        return data;
//    }
//
//    /**
//     * Returns only the ID that matches the name passed in
//     *
//     * @param name
//     * @return
//     */
//    public Cursor getItemID(String name) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT " + COL_TY + " FROM " + TABLE_NAME +
//                " WHERE " + COL_DE + " = '" + name + "'";
//        Cursor data = db.rawQuery(query, null);
//        return data;
//    }
//
//    /**
//     * Updates the name field
//     *
//     * @param newName
//     * @param id
//     * @param oldName
//     */
//    public void updateName(String newName, int id, String oldName) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "UPDATE " + TABLE_NAME + " SET " + COL_DE +
//                " = '" + newName + "' WHERE " + COL_TY + " = '" + id + "'" +
//                " AND " + COL_DE + " = '" + oldName + "'";
//        Log.d(TAG, "updateName: query: " + query);
//        Log.d(TAG, "updateName: Setting name to " + newName);
//        db.execSQL(query);
//    }
//
//    /**
//     * Delete from database
//     *
//     * @param name
//     */
//    public void deleteName(String name) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
//
//                + COL_DE + " = '" + name + "'";
//        Log.d(TAG, "deleteName: query: " + query);
//        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
//        db.execSQL(query);
//    }
//    public void delete() {
//        Log.e("DATABASE","Dropping Database");
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("delete from " + TABLE_NAME);
//    }
//    public boolean checkDataBase(Context context) {
//
//        Boolean rowExists = Boolean.FALSE;
//
//
//        Log.e("DATABASE", "Checking Database" );
//        File dbFile = context.getDatabasePath(TABLE_NAME);
//        if (dbFile.exists()==true){
//
//            SQLiteDatabase db = this.getWritableDatabase();
//
//            Cursor mCursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//
//            System.out.println("finishing check");
//            if (mCursor.moveToFirst())
//            {
//                // DO SOMETHING WITH CURSOR
//                rowExists = true;
//
//            } else
//            {
//                // I AM EMPTY
//                rowExists = false;
//                //Log.e(TAG, "Row DOESNT Exists  " );
//            }
//            mCursor.close();
//
//
//        }
//        return rowExists;
//    }
//    public void exportDB() {
//
//
//        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
//        exportDir.mkdirs();
//        if (!exportDir.exists())
//        {
//            exportDir.mkdirs();
//        }
//
//        File file = new File(exportDir, "csvname.txt");
//        try
//        {
//            file.createNewFile();
//            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
//            SQLiteDatabase db = this.getWritableDatabase();
//            Cursor curCSV = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
//
//            while(curCSV.moveToNext())
//            {
//
//
//                String c0 = curCSV.getString(0);
//                String c1 = curCSV.getString(1);
//                String c2 = curCSV.getString(2);
//                String c3 = curCSV.getString(3);
//                String c4 = curCSV.getString(4);
//                String c5 = curCSV.getString(5);
//
////                System.out.println("Tyrionc "+c0);
////                System.out.println("Tyrionc "+c1);
////                System.out.println("Tyrionc "+c2);
////                System.out.println("Tyrionc "+c3);
////                System.out.println("Tyrionc "+c4);
////                System.out.println("Tyrionc "+c5);
//
//                //Which column you want to export
////                String arrStr[] ={curCSV.getString(0),curCSV.getString(1),curCSV.getString(2), curCSV.getString(3),curCSV.getString(4),String.valueOf(curCSV.getString(5))};
//                String arrStr[] ={c0,c1,c2,c3,c4,c5};
//
//
//                csvWrite.writeNext(arrStr);
//            }
//            csvWrite.close();
//            curCSV.close();
//        }
//        catch(Exception sqlEx)
//        {
//            Log.e("Menu", sqlEx.getMessage(), sqlEx);
//        }
//    }
//    public void readData(DatabaseHelper mDatabaseHelper, Context context){
//        /**
//         * if DB is empty then read from local
//         * else create from asset storage
//         */
//        if(checkDataBase(context)){
//
//            try {
//                /**
//                 * create database from Local
//                 */
//                new CSVReader(context,mDatabaseHelper,"Local");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        else{
//            try {
//                /**
//                 * create database from Asset
//                 */
//                new CSVReader(context ,mDatabaseHelper,"Asset");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    public boolean emptyCheck(Context context, Cursor dtb){
//        if (!dtb.moveToFirst()){
//            Log.e("ERROR ","No Data Found");
//            Intent myIntent = new Intent(context, Menu.class);
//            context.startActivity(myIntent);
//            return false;
//        }
//        else {
//            return true;
//        }
//    }
//
//
//
//
//}
