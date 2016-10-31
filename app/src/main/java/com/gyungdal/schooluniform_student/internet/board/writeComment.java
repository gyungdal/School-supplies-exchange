package com.gyungdal.schooluniform_student.internet.board;

import android.os.AsyncTask;
import android.util.Log;

import com.gyungdal.schooluniform_student.Config;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by GyungDal on 2016-10-25.
 */

public class writeComment extends AsyncTask<Void, Void, Void> {
    private static final String TAG = writeComment.class.getName();
    private String comment;

    public writeComment(String comment){
        this.comment = comment;
    }

    @SuppressWarnings("WrongThread")
    @Override
    protected Void doInBackground(Void... params) {
        try{
            String url = Config.SERVER_URL + Config.WRITE_COMMENT_PATH;
            if(!url.contains(Config.SERVER_PROTOCAL))
                url = Config.SERVER_PROTOCAL + url;

            HashMap<String, String> connParams = new getWriteInformation()
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            Connection connection = Jsoup.connect(url)
                    .userAgent(Config.USER_AGENT)
                    .header("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.5,en;q=0.3")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .followRedirects(true);
            connection.data("wr_content", comment);
            Set key = connParams.keySet();
            for(Iterator iterator = key.iterator(); iterator.hasNext();) {
                String name = (String) iterator.next();
                String value = connParams.get(name);
                connection.data(name, value);
            }
            Connection.Response response
                    = connection.method(Connection.Method.POST).execute();
            Log.i(TAG, response.toString());
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
}
