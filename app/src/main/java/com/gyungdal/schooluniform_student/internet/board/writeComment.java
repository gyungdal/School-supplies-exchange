package com.gyungdal.schooluniform_student.internet.board;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gyungdal.schooluniform_student.Config;
import com.gyungdal.schooluniform_student.activity.board.upload.UploadThread;
import com.gyungdal.schooluniform_student.internet.store.CookieStore;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by GyungDal on 2016-10-25.
 */

public class writeComment extends AsyncTask<Void, Void, Void> {
    private static final String TAG = writeComment.class.getName();
    private String comment, url;
    private Context context;
    public writeComment(String url, String comment, Context context){
        this.url = url;
        this.comment = comment;
        this.context = context;
    }

    @SuppressWarnings("WrongThread")
    @Override
    protected Void doInBackground(Void... params) {
        try{
            String url = Config.SERVER_URL + Config.WRITE_COMMENT_PATH;
            if(!url.contains(Config.SERVER_PROTOCAL))
                url = Config.SERVER_PROTOCAL + url;
            WebView web = new WebView(context);
            CookieSyncManager.createInstance(context);
            CookieSyncManager.getInstance().startSync();
            CookieManager.getInstance().setAcceptCookie(true);

            web.getSettings().setJavaScriptEnabled(true);
            web.getSettings().setSaveFormData(true);
            web.getSettings().setSupportMultipleWindows(true);
            web.getSettings().setAppCacheEnabled(true);
            web.getSettings().setDatabaseEnabled(true);
            web.getSettings().setDomStorageEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            Map<String, String> extraHeaders = new HashMap<String, String>();
            extraHeaders.put("User-Agent", Config.USER_AGENT);
            web.setWebViewClient(new webClient());
            web.loadUrl(url, extraHeaders);

        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    private String getCookie(){
        String cookie = "";
        for (Object aKey : CookieStore.getInstance().getCookies().keySet()) {
            String name = (String) aKey;
            String value = CookieStore.getInstance().getCookies().get(name);
            Log.i(TAG, "name : " + name);
            Log.i(TAG, "value : " + value);
            cookie += name + "=" + value + ";";
        }
        return cookie;
    }

    class webClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            CookieSyncManager.getInstance().sync();
            if (url.contains("write")){
                return true;
            }else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView v, String url){
            String Script = "var id = document.getElementById(\"wr_content\");id.value = \"" + comment + "\";" +
                    "var button = document.getElementsByClassName(\"btn_submit\");" +
                    "for (var i=0;i<button.length; i++) {\n" +
                    "    button[i].click();\n" +
                    "};";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                v.evaluateJavascript(Script, null);
            }

        }
    }
}
