package activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.DatabaseHelper;
import com.example.marmi.cardschool.data.Word;
import com.example.marmi.cardschool.normal.MyRecyclerViewAdapter;

import java.util.ArrayList;

import fragments.EditBtn;

public class PrintFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;
    DatabaseHelper mDatabaseHelper;
    ArrayList<Word> words;
    Context context;

    public interface FragmentListener {
        void onFragmentListener(Word Word, String mode);
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


        mDatabaseHelper = new DatabaseHelper(context);
        String query = " WHERE rate >= " + 0 + " AND rate <= " + 20 +" ORDER BY rate";
        Cursor cursor = mDatabaseHelper.getData(query);
        words = new ArrayList<>();
        if (cursor.moveToFirst()) {

            do {
                words.add(new Word(cursor));
            } while (cursor.moveToNext());

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
        Toast.makeText(context, "You clicked " + adapter.getItem(position).getWordText() + " on row number " + position, Toast.LENGTH_SHORT).show();
        listener.onFragmentListener(adapter.getItem(position),"Edit");




//        new AlertDialog.Builder(view.getContext())
//                .setTitle("Delete")
//                .setMessage("Do you really want to delete?\n\n"+adapter.getItem(position)+"\n\n")
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        System.out.println("click");
//                        mDatabaseHelper.deleteName(adapter.getItem(position));
//                        mDatabaseHelper.exportDB();
//
////                        System.out.println(words.get(position).text);
////                        System.out.println("Words size  "+words.size());
////                        System.out.println("position    "+position);
//
//                        words.remove(position);
//                        adapter.notifyItemRemoved(position);
//                        adapter.notifyItemRangeChanged(position,words.size());
//
//                    }})
//                .setNegativeButton(android.R.string.no, null).show();


    }
}