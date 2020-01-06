package activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.WordController;
import com.example.marmi.cardschool.data.WordModel;
import com.example.marmi.cardschool.normal.MyRecyclerViewAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ImportsFragment extends Fragment {


    private String en;
    private String gr;
    private String hr;
    private String sr;
    private DatabaseHelper mDatabaseHelper;

    private String type = " ";
    private String de;
    //private Context context;
    private View v;
    private EditText text;
    private EditText rateInput;
    private ProgressBar pb;
    private RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        LayoutInflater inflator = inflater;
        v = inflater.inflate(R.layout.texts_import, container, false);
        mDatabaseHelper = new DatabaseHelper(getContext());

//        try {
//            readInput();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        text = v.findViewById(R.id.text);
        Button end = v.findViewById(R.id.end);
        end.setOnClickListener(endListener);

        rateInput = v.findViewById(R.id.rate);
        pb = v.findViewById(R.id.pB);
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<WordController> list = new ArrayList<>();
        WordController wc = new WordController();
        wc.importWord(new String[]{" ",rateInput.getText().toString(),"empty"," "," "," "," ","0"});
        CustomAdapter adapter = new CustomAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        text.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0){

                    readInput2();
                }

            }
        });


        return v;
    }

    private void readInput2() {

        String plainText = text.getText().toString();
        String[] words = plainText.split("\n");
        int lineNum = 0;

        String type = " ";
        ArrayList<WordController> list = new ArrayList<>();
        while (words.length>lineNum){
            //System.out.println("c "+words[lineNum]);

            if(!words[lineNum].equals(" ")&&!words[lineNum].equals(""))
            {
                WordController wc = new WordController();
                wc.importWord(new String[]{type,rateInput.getText().toString(),words[lineNum]," "," "," "," ","0"});
                list.add(wc);
         }
            lineNum++;
        }

        CustomAdapter adapter = new CustomAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
    }


    private View.OnClickListener endListener = new View.OnClickListener() {
        public void onClick(View v) {
            System.out.println("finish adding");
            readInput2();
            new Reading().execute(text.getText().toString());
        }
    };

    class Reading extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute(){
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... strings) {

            String plainText = strings[0];
            //System.out.println("s   "+plainText);
            String[] words = plainText.split("\n");
            int lineNum = 0;
            String type = " ";
            while (words.length>lineNum){
                if(!words[lineNum].equals(" ")&&!words[lineNum].equals(""))
                    mDatabaseHelper.addData(type,Integer.parseInt(rateInput.getText().toString()),words[lineNum]," "," "," "," ");
                lineNum++;
            }
            return new String[0];
        }
        @Override
        protected void onPostExecute(String[] feed){
            pb.setVisibility(View.GONE);
            mDatabaseHelper.exportDB();
            mDatabaseHelper.exportImportedDB();
            mDatabaseHelper.close();
            getActivity().getSupportFragmentManager().popBackStack();
        }

    }



    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        private List<WordController> mData;
        private LayoutInflater mInflater;


        // data is passed into the constructor
        public CustomAdapter(Context context, List<WordController> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
        }

        // inflates the row layout from xml when needed
        @Override
        public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.import_item, parent, false);
            return new ViewHolder(view);
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(CustomAdapter.ViewHolder holder, int position) {

            String de = mData.get(position).getWordText();
            String ty = mData.get(position).getType();


            if(ty.equals("Nomen")){
                de = mData.get(position).getArticle()+" "+de+",-"+mData.get(position).getPlural();
            }

            //System.out.println(de);
            holder.des.setText(de);
            holder.types.setText(ty);


        }

        @Override
        public int getItemCount() {
            return mData.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView des;
            TextView types;

            ViewHolder(View itemView) {
                super(itemView);
                des = itemView.findViewById(R.id.de);
                types = itemView.findViewById(R.id.type);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

            }
        }

    }



}
