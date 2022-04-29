package com.example.marmi.cardschool.normal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marmi.cardschool.R;
import com.example.marmi.cardschool.data.WordModel;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private final List<WordModel> mData;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, List<WordModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row2, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String tr = mData.get(position).getEn_translated();
        String de = mData.get(position).getWordText();
        String gr = mData.get(position).getGr_translated();
        String rate = mData.get(position).getRate();
        String ty = mData.get(position).getType();

        if (ty.equals("Nomen")) {
            de = mData.get(position).getArticle() + " " + de + ",-" + mData.get(position).getPlural();
        }
        holder.de.setText(de);
        holder.en.setText(tr);
        holder.type.setText(ty);
        holder.gr.setText(gr);
        holder.rate.setText(rate);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    public WordModel getItem(int id) {
        return mData.get(id);

    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView de;
        TextView en;
        TextView gr;
        TextView type;
        TextView rate;

        ViewHolder(View itemView) {
            super(itemView);
            de = itemView.findViewById(R.id.de);
            en = itemView.findViewById(R.id.ent);
            gr = itemView.findViewById(R.id.gr);
            type = itemView.findViewById(R.id.type);
            rate = itemView.findViewById(R.id.rate);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());


        }
    }
}