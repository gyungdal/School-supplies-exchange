package com.gyungdal.schooluniform_student.internet.school;

import android.os.AsyncTask;
import android.util.Log;

import com.gyungdal.schooluniform_student.Config;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
            if(response.statusCode() != 200)
                return null;
            Document doc = response.parse();

            //만약 결과값이 없으면 null 리턴
            if(doc.select("#id").get(0).text().isEmpty())
                return null;

            result.id = doc.select("#id").get(0).text();
            result.name = doc.select("#name").get(0).text();
            result.number = Integer.valueOf(doc.select("#number").get(0).text());
            result.grade = Integer.valueOf(doc.select("#grade").get(0).text());
            result.Class = Integer.valueOf(doc.select("#class").get(0).text());
            result.year = Integer.valueOf(doc.select("#year").get(0).text());
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        return result;
    }
}
