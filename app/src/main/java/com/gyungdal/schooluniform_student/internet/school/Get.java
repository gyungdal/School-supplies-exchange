package com.gyungdal.schooluniform_student.internet.school;

import android.os.AsyncTask;
import android.util.Log;

import com.gyungdal.schooluniform_student.Config;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by GyungDal on 2016-09-22.
 */

public class Get extends AsyncTask<Void, Void, Item> {
    private static final String TAG = Get.class.getName();
    private String id;

    public Get(String id){
        this.id = id;
    }

    @Override
    protected Item doInBackground(Void... params) {
        Item result = new Item();
        try{
            String url = Config.SERVER_URL + Config.GET_SHCOOL_PATH;
            if(!url.contains(Config.SERVER_PROTOCAL))
                url = Config.SERVER_PROTOCAL + url;
            Log.i(TAG, url);
            Connection.Response response = Jsoup.connect(url)
                    .data("id", id)
                    .method(Connection.Method.GET)
                    .execute();
            if(response.statusCode() != 200) {
                Log.i(TAG, response.statusMessage() + response.statusCode());
                return null;
            }
            Document doc = response.parse();
            Log.i(TAG, doc.toString());
            //만약 결과값이 없으면 null 리턴
            if(doc.select("#id").get(0).text().trim().isEmpty()) {
                Log.i(TAG, doc.select("#id").get(0).text().trim());
                return null;
            }

            result.id = doc.select("#id").get(0).text();
            result.name = doc.select("#name").get(0).text();
            result.number = Integer.valueOf(doc.select("#number").get(0).text().trim());
            result.grade = Integer.valueOf(doc.select("#grade").get(0).text().trim());
            result.Class = Integer.valueOf(doc.select("#class").get(0).text().trim());
            result.year = Integer.valueOf(doc.select("#year").get(0).text().trim());
            Log.i(TAG, "Now Year... : " + Calendar.getInstance().get(Calendar.YEAR));
            if(result.year < Calendar.getInstance().get(Calendar.YEAR)) {
                Log.i(TAG, "Before setting...");
                return null;
            }
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        return result;
    }
}
