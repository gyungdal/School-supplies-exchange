package com.gyungdal.schooluniform_student.internet.board;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;

/**
 * Created by GyungDal on 2016-10-14.
 */

public class upload extends AsyncTask<Void, Integer, Boolean> {
    private static final String TAG = upload.class.getName();
    private Bitmap photo;
    private String title, desc;

    public upload(String title, Bitmap photo, String desc){
        this.title = title;
        this.photo = photo;
        this.desc = desc;
    }
    //TODO : Multipart 로 묶어서 업로드
    @Override
    protected Boolean doInBackground(Void... params) {
        try{
            String uploadUrl = "";
            URL url = new URL(uploadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            MultipartEntityBuilder builder = MultipartEntityBuilder.create() //객체 생성...
                    .setCharset(Charset.forName("UTF-8")) //인코딩을 UTF-8로.. 다들 UTF-8쓰시죠?
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            HttpEntity httpEntity = builder.build();
            httpEntity.writeTo(conn.getOutputStream());
            return true;
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        return false;
    }
}
