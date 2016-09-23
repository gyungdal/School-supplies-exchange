package com.gyungdal.schooluniform_student.internet.school;

import android.os.AsyncTask;
import android.util.Log;

import com.gyungdal.schooluniform_student.Config;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by GyungDal on 2016-09-22.
 */

public class Set extends AsyncTask<Void, Void, Config.State> {
    private static final String TAG = Set.class.getName();
    private Item item;

    public Set(Item item){
        this.item = item;
    }

    @Override
    protected Config.State doInBackground(Void... params) {
        try {
            String url = Config.SERVER_URL + Config.SET_SCHOOL_PATH;
            if (!url.contains(Config.SERVER_PROTOCAL))
                url = Config.SERVER_PROTOCAL + url;
            Log.i(TAG, url);
            Connection.Response response = Jsoup.connect(url)
                    .data("school_name", item.name)
                    .data("school_grade", String.valueOf(item.grade))
                    .data("school_class", String.valueOf(item.Class))
                    .data("school_number", String.valueOf(item.number))
                    .data("id", item.id)
                    .method(Connection.Method.GET)
                    .execute();
            if(response.statusCode() != 200)
                return Config.State.ERROR;
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        return Config.State.SUCCESS;
    }
}
