package test;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marmi.cardschool.R;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<ItemData> {
    int groupid;
    Activity context;
    ArrayList<ItemData> list;
    LayoutInflater inflater;
    TextView textView;
    ImageView imageView;
    View itemView;
    View s;


    public SpinnerAdapter(Context context, int groupid, ArrayList<ItemData> list) {
        super(context,groupid,list);
        this.list=list;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid=groupid;
        s = inflater.inflate(groupid, null, false);
    }

    public View getView(int position, View convertView, ViewGroup parent ){
//        itemView=inflater.inflate(groupid,parent,false);
//        ImageView imageView=(ImageView)itemView.findViewById(R.id.flag_img);
//        imageView.setImageResource(list.get(position).getImageId());
//
//
//        textView= itemView.findViewById(R.id.txt);
//        textView.setText("");
//        System.out.println("view "+position);
//
//
//        return textView;

//        return itemView;
        return createItemView(position,convertView,parent);
    }

    public View getDropDownView(int position, View convertView, ViewGroup
            parent){

//        //textView.setVisibility(View.VISIBLE);
//
            itemView=inflater.inflate(groupid,null);
            ImageView imageView= itemView.findViewById(R.id.flag_img);
            textView= itemView.findViewById(R.id.txt);


            String txt = list.get(position).text;
            System.out.println(position);
            imageView.setImageResource(list.get(position).getImageId());
            textView.setText(list.get(position).getText());




//        return itemView;


        //return getView(position,convertView,parent);
//        String txt = list.get(position).text;
        System.out.println(position);
        return imageView;

    }
    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = s;
        imageView= view.findViewById(R.id.flag_img);
        TextView textView= itemView.findViewById(R.id.txt);
        imageView.setImageResource(list.get(position).getImageId());
            textView.setText(list.get(position).getText());



        return view;
    }
}
