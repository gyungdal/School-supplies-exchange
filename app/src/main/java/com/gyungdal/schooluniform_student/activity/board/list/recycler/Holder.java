package com.gyungdal.schooluniform_student.activity.board.list.recycler;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyungdal.schooluniform_student.R;

/**
 * Created by GyungDal on 2016-10-06.
 */

public class Holder extends RecyclerView.ViewHolder {
    protected CardView card;
    protected ImageView preview;
    protected TextView title, author, date;
    public Holder(View view) {
        super(view);
        card = (CardView)view.findViewById(R.id.card_view);
        preview = (ImageView) view.findViewById(R.id.thread_preview_image);
        title = (TextView) view.findViewById(R.id.thread_title);
        author = (TextView) view.findViewById(R.id.thread_author);
        date = (TextView)view.findViewById(R.id.thread_date);
    }
}
