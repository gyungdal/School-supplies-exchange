package com.gyungdal.schooluniform_student.activity.board.list.recycler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyungdal.schooluniform_student.R;
import com.gyungdal.schooluniform_student.activity.board.detail.SingleThread;
import com.gyungdal.schooluniform_student.activity.board.detail.SingleThreadData;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by GyungDal on 2016-10-06.
 */

public class Adapter extends RecyclerView.Adapter<Holder>{
    private ArrayList<ThreadItem> items;
    private Context context;

    public Adapter(ArrayList<ThreadItem> items, Context context){
        this.items = items;
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_card
                , parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.author.setText(items.get(position).getAuthor());
        holder.date.setText(items.get(position).getDate());
        Picasso.with(context).load(items.get(position).getPreviewUrl())
            .fit()
            .placeholder(R.drawable.loading_image)
            .fit()
            .error(R.drawable.error)
            .fit()
            .into(holder.preview);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleThreadData.doc = items.get(position).getDoc();
                SingleThreadData.url = items.get(position).getUrl();
                SingleThreadData.title = items.get(position).getTitle();
                SingleThreadData.photo = items.get(position).getPreviewUrl();
                Intent intent = new Intent(context, SingleThread.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    private void swapItems(ArrayList<ThreadItem> items){
        this.items.retainAll(items);
        this.notifyDataSetChanged();
    }
    private void removeMatchValue(){
        for(int i = 0;i<items.size();i++){
            for(int j = i + 1;j<items.size();j++){
                if((items.get(j).getAuthor().equals(items.get(i).getAuthor())
                        && items.get(j).getDate().equals(items.get(i).getDate()))) {
                    items.remove(j);
                    Log.i("REMOVE!!!", "Adapter i : " + i + ", j : " + j);
                }
            }
        }
        swapItems(items);
    }

    public void addItem(ThreadItem item){
        this.items.add(item);
        removeMatchValue();
    }
    public void addItems(ArrayList<ThreadItem> items){
        this.items.addAll(items);
        removeMatchValue();
    }

}
