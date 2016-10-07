package com.gyungdal.schooluniform_student.activity.board.list.recycler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyungdal.schooluniform_student.R;
import com.gyungdal.schooluniform_student.activity.board.detail.SingleThread;
import com.gyungdal.schooluniform_student.activity.board.list.ThreadList;
import com.squareup.picasso.Picasso;

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
    public void onBindViewHolder(Holder holder, final int position) {
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
                Intent intent = new Intent(context, SingleThread.class);
                intent.putExtra("url", items.get(position).getUrl());
                context.startActivity(intent);
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
