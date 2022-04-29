package com.example.marmi.cardschool.normal;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.marmi.cardschool.data.DatabaseHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CSVReader {
    Context context;
    DatabaseHelper Database;
    File file;

    public CSVReader(Context context, DatabaseHelper Database) throws IOException {
        this.context = context;
        this.Database = Database;

        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        file = new File(exportDir, "exportedFile.txt");
        if (file.exists()) {
            readLocal();
        } else {
            readAsset();
        }


    }
    public void readAsset() throws IOException {

        Log.e("Database ", "Reading Asset");
        InputStream is = context.getAssets().open("prototypeFile.txt");
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        String csvSplitBy = ";";


        while ((line = br.readLine()) != null) {

            String[] row = line.split(csvSplitBy);

            /**
             * @param row0 german
             * @param row1 english
             * @param row2 greek
             * @param row3 sector
             * @param row4 verb
             * @param row5 difficulty
             */

            Database.addData(row[0], Integer.parseInt(row[1]), row[2], row[3], row[4], row[5], row[6]);
            Log.e(TAG, "row0:  " + row[0]);
            Log.e(TAG, "row1:  " + row[1]);
            Log.e(TAG, "row2:  " + row[2]);
            Log.e(TAG, "row3:  " + row[3]);
            Log.e(TAG, "row4:  " + row[4]);
            Log.e(TAG, "row5:  " + row[5]);

        }

        br.close();
        /**
         * export Database in local csv
         */

        Database.exportDB();
    }

    public void readLocal() throws IOException {

        Log.e("Database ", "Reading Local");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String csvSplitBy = ";";

        while ((line = br.readLine()) != null) {
            String[] row = line.split(csvSplitBy);
            /**
             * @param row0 german
             * @param row1 english
             * @param row2 greek
             * @param row3 sector
             * @param row4 verb
             * @param row5 difficulty
             */
            Log.e("e", "row0:  " + row[0]);
            Log.e("e", "row1:  " + row[1]);
            Log.e("e", "row2:  " + row[2]);
            Log.e("e", "row3:  " + row[3]);
            Log.e("e", "row4:  " + row[4]);
            Log.e("e", "row5:  " + row[5]);
            Database.addData(row[0], Integer.parseInt(row[1]), row[2], row[3], row[4], row[5], row[6]);
        }
        br.close();


    }

}


