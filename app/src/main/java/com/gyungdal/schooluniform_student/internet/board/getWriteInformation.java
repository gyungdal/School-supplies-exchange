package com.gyungdal.schooluniform_student.internet.board;

import android.os.AsyncTask;
import android.util.Log;

import com.gyungdal.schooluniform_student.Config;
import com.gyungdal.schooluniform_student.internet.store.CookieStore;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by GyungDal on 2016-10-24.
 */

public class getWriteInformation
        extends AsyncTask<Void, Void, HashMap<String,String>>{
    private static final String TAG = getWriteInformation.class.getName();
    @Override
    protected HashMap<String, String> doInBackground(Void... params) {
        HashMap<String, String> result = new HashMap<>();
        try{
            String url = Config.SERVER_URL + Config.WRITE_PATH;
            if(!url.contains(Config.SERVER_PROTOCAL))
                url = Config.SERVER_PROTOCAL + url;
            Connection.Response res = Jsoup.connect(url)
                    .userAgent(Config.USER_AGENT)
                    .header("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.5,en;q=0.3")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .cookies(CookieStore.getInstance().getCookies())
                    .method(Connection.Method.GET)
                    .execute();
            Log.i(TAG, res.statusMessage());
            Document doc = res.parse();
            Log.i(TAG, doc.toString());
            String temp = doc.select("#fwrite").get(0).toString();
            Log.i("Write Info", temp);
            temp = temp.substring(temp.indexOf("<input"));
            temp = temp.substring(0, temp.indexOf("<div")).trim();
            Log.i("Write Info trim", temp);
            while(true){
                temp = temp.substring(temp.indexOf("type"));
                String name =
                        getValue(temp.substring(temp.indexOf("name=")
                                        + "name=".length()));
                String value =
                        getValue(temp.substring(temp.indexOf("value=")
                                        + "value=".length()));
                Log.i("Write info name", name);
                Log.i("Write info value", value);

                if((!name.contains("wr_subject")) && (!name.contains("wr_content")))
                    result.put(name, value);

                if(temp.contains("<input"))
                    temp = temp.substring(temp.indexOf("<input"));
                else {
                    break;
                }
            }
            printAll(result);
            return result;
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    private String getValue(String value){
        if(!value.contains("\""))
            return "";
        Log.i(TAG, "value : " + value);
        value = value.substring(value.indexOf("\"") + "\"".length());
        return value.substring(0, value.indexOf("\""));
    }

    private void printAll(HashMap<String, String> data){
        Set key = data.keySet();
        for (Object aKey : key) {
            String name = (String) aKey;
            String value = data.get(name);
            Log.i(TAG, "name : " + name);
            Log.i(TAG, "value : " + value);
        }
    }
}
