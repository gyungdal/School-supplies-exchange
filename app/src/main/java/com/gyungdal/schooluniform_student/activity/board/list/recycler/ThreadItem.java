package com.gyungdal.schooluniform_student.activity.board.list.recycler;

import android.graphics.Bitmap;

/**
 * Created by GyungDal on 2016-10-06.
 */

public class ThreadItem {
    private String date, title, author, url;
    private boolean status;
    private Bitmap preview;

    public ThreadItem(boolean status, String date, String title
                        , String author, String url, Bitmap preview){
        this.status = status;
        this.date = date;
        this.title = title;
        this.author = author;
        this.url = url;
        this.preview = preview;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setStatus(boolean status){
        this.status = status;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public void setPreview(Bitmap preview){
        this.preview = preview;
    }

    public boolean getStatus(){
        return this.status;
    }

    public String getDate(){
        return this.date;
    }

    public String getTitle(){
        return this.title;
    }

    public String getAuthor(){
        return this.author;
    }

    public Bitmap getPreview(){
        return this.preview;
    }

    public String getUrl(){
        return this.url;
    }
}
