package com.gyungdal.schooluniform_student.internet.board;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.gyungdal.schooluniform_student.Config;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;

/**
 * Created by GyungDal on 2016-10-14.
 */

public class writeThread extends AsyncTask<Void, Integer, Boolean> {
    private static final String TAG = writeThread.class.getName();
    private Bitmap photo;
    private String title, desc;

    public writeThread(String title, Bitmap photo, String desc){
        this.title = title;
        this.photo = photo;
        this.desc = desc;
    }
    //TODO : Multipart 로 묶어서 업로드
    @SuppressWarnings("WrongThread")
    @Override
    protected Boolean doInBackground(Void... params) {
        try{
            String url = Config.SERVER_URL + Config.WRITE_PATH;
            if(!url.contains(Config.SERVER_PROTOCAL))
                url = Config.SERVER_PROTOCAL + url;
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("wr_subject", title);
            requestParams.put("wr_content", desc);
            requestParams.put("bf_file[]", photo);

            getWriteInformation info = new getWriteInformation();
            HashMap<String, String> data = info.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            if(data == null) {
                Log.e(TAG, "Fail get data");
                return false;
            }else{
                Set key = data.keySet();
                for (Object aKey : key) {
                    String name = (String) aKey;
                    String value = data.get(name);
                    Log.i(TAG, "name : " + name);
                    Log.i(TAG, "value : " + value);
                    requestParams.put(name, value);
                }
            }

            client.post(url, requestParams, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers
                        , String responseString, Throwable throwable) {
                    //실패
                    Log.i("Request Code", String.valueOf(statusCode));
                    Log.i("Request String", responseString);
                    for(Header header : headers)
                        Log.i("Request Headers", header.toString());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    //성공
                    Log.i("Request Code", String.valueOf(statusCode));
                    Log.i("Request String", responseString);
                    for(Header header : headers)
                        Log.i("Request Headers", header.toString());
                }
            });
            return true;
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        return false;
    }

    private byte[] bitmapToString(){
        if(photo != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }else
            return null;
    }
}
