package activities;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.WordController;
import com.example.marmi.cardschool.normal.MyRecyclerViewAdapter;

import java.util.ArrayList;

public class PrintFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;
    ArrayList<WordController> words;
    Context context;
    String nfrom = "0";
    String nto = "20";
    String mode = "";
    Cursor dtb;


    public interface FragmentListener {
        void onFragmentListener(WordController Word, String mode);
    }
    FragmentListener listener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        listener = (FragmentListener) context;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_db, container, false);

        if (getArguments() != null) {
            nfrom = getArguments().getString("nfrom");
            nto = getArguments().getString("nto");
            mode =getArguments().getString("mode");
        }

        String query = " WHERE rate >= " + nfrom + " AND rate <= " + nto +" "+ mode + " ORDER BY rate";

        DatabaseHelper mDatabaseHelper = new DatabaseHelper(getContext());
        System.out.println("nfrom "+nfrom);
        System.out.println("nto "+nto);
        dtb = mDatabaseHelper.getData(query);
        if(dtb==null){
            System.out.println("Reading Database cause Null");
            mDatabaseHelper.readData(mDatabaseHelper, getContext());
            dtb = mDatabaseHelper.getData(query);
        }
        mDatabaseHelper.close();
        System.out.println("init db");
        words = new ArrayList<>();
        if (dtb.moveToFirst()) {

            do {
                WordController wc = new WordController();
                wc.importWord(dtb);
                words.add(wc);
            } while (dtb.moveToNext());

        }
        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MyRecyclerViewAdapter(context, words);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onItemClick(View view, final int position) {
        //Toast.makeText(context, "You clicked " + adapter.getItem(position).getWordText() + " on row number " + position, Toast.LENGTH_SHORT).show();
        listener.onFragmentListener(adapter.getItem(position),"Edit");




    }
}