package com.gyungdal.schooluniform_student.activity.board.list.recycler;

import org.jsoup.nodes.Document;

/**
 * Created by GyungDal on 2016-10-06.
 */

public class ThreadItem {
    private String date, title, author ,url, previewUrl;
    Document doc;

    public ThreadItem(String date, String title, Document doc
                        , String author, String url, String previewUrl){
        this.date = date;
        this.title = title;
        this.author = author;
        this.doc = doc;
        this.url = url;
        this.previewUrl = previewUrl;
    }

    public void setDoc(Document doc){
        this.doc = doc;
    }

    public void setUrl(String url){
        this.url = url;
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

    public void setPreviewUrl(String previewUrl){
        this.previewUrl = previewUrl;
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

    public String getPreviewUrl(){
        return this.previewUrl;
    }

    public String getUrl(){
        return this.url;
    }

    public Document getDoc(){
        return this.doc;
    }
}
