package mainFragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.normal.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TestFragment extends Fragment  {



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.testo, container, false);
        super.onCreate(savedInstanceState);

        DatabaseHelper mDatabaseHelper = new DatabaseHelper(getContext());

        try {
            new CSVReader(getContext(),mDatabaseHelper,"input");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDatabaseHelper.close();





    return v;
    }
}
