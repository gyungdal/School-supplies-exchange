package com.gyungdal.schooluniform_student.activity.board.detail.comment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyungdal.schooluniform_student.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by GyungDal on 2016-10-20.
 */

public class CommentAdapter extends BaseAdapter{
    private ArrayList<CommentItem> items;
    private Context context;
    public CommentAdapter(Context context,
                          ArrayList<CommentItem> items){
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return (items != null) ? items.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return (items != null && items.size() > position)
                ? items.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.view_comment, parent, false);
        ViewHolder holder = new ViewHolder();
        holder.id = (TextView) convertView.findViewById(R.id.comment_id);
        holder.date = (TextView) convertView.findViewById(R.id.comment_time);
        holder.text = (TextView) convertView.findViewById(R.id.comment_text);
        holder.id.setText(items.get(position).id);
        holder.date.setText(items.get(position).date);
        holder.text.setText(items.get(position).comment);
        return convertView;
    }

    public void addComment(CommentItem item){
        this.items.add(item);
        notifyDataSetChanged();
    }

    private class ViewHolder{
        public TextView id, date, text;
    }
}
