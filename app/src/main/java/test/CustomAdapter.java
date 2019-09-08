//package test;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.SpinnerAdapter;
//import android.widget.TextView;
//
//import com.example.marmi.cardschool.R;
//
//import java.util.ArrayList;
//
//import fragments.FragmentLanguage;
//
//public class CustomAdapter  extends BaseAdapter implements SpinnerAdapter {
//
//    ArrayList<ItemData> list;
//    Context context;
//    String[] colors = {"#13edea","#e20ecd","#15ea0d"};
//    String[] colorsback = {"#FF000000","#FFF5F1EC","#ea950d"};
//
//    public CustomAdapter(Context context, ArrayList<ItemData> list) {
//        this.list = list;
//        this.context = context;
//    }
//
//    @Override
//    public int getCount() {
//        return 4;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return list[position];
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view =  View.inflate(context, R.layout.flag, null);
//        TextView textView = (TextView) view.findViewById(R.id.txt);
//        textView.setText("s");
//        return textView;
//    }
//
//    @Override
//    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//
//        View view;
//        view =  View.inflate(context, R.layout.flag, null);
//        final TextView textView = (TextView) view.findViewById(R.id.txt);
//        textView.setText("s");
//
//        textView.setTextColor(Color.parseColor(colors[position]));
//        textView.setBackgroundColor(Color.parseColor(colorsback[position]));
//
//
//        return view;
//    }
//}