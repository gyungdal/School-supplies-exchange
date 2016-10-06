package com.gyungdal.schooluniform_student.activity.board.list.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyungdal.schooluniform_student.R;

import java.util.ArrayList;

/**
 * Created by GyungDal on 2016-10-06.
 */

public class Adapter extends RecyclerView.Adapter<Holder>{
    private ArrayList<ThreadItem> items;

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.article_card, null);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.author.setText(items.get(position).getAuthor());
        holder.date.setText(items.get(position).getDate());
        holder.preview.setImageBitmap(items.get(position).getPreview());
        if(items.get(position).getStatus()){
            //판매중
        }else{
            //판매중이 아닐 때
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void swapItems(ArrayList<ThreadItem> items){
        this.items.retainAll(items);
        this.notifyDataSetChanged();
    }

    public void addItems(ArrayList<ThreadItem> items){
        this.items.addAll(items);
        this.notifyDataSetChanged();
    }

}
