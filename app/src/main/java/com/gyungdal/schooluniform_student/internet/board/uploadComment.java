package com.gyungdal.schooluniform_student.internet.board;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.gyungdal.schooluniform_student.Config;
import com.gyungdal.schooluniform_student.R;
import com.gyungdal.schooluniform_student.activity.board.detail.SingleThread;
import com.gyungdal.schooluniform_student.activity.board.detail.SingleThreadData;
import com.gyungdal.schooluniform_student.internet.store.CookieStore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by GyungDal on 2016-11-07.
 */

public class uploadComment extends AppCompatActivity {
    private static final String TAG = uploadComment.class.getName();
    private String url, comment;
    private WebView web;
    private ProgressDialog dialog;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.comment_upload_hide);
        dialog = ProgressDialog.show(uploadComment.this, "Wait...", "Comment Write...", true);
        CookieSyncManager.createInstance(uploadComment.this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().setAcceptCookie(true);
        if(getIntent() != null){
            url = getIntent().getStringExtra("url");
            comment = getIntent().getStringExtra("comment");
        }else{
            Toast.makeText(uploadComment.this, "Wrong!", Toast.LENGTH_SHORT).show();
        }
        web = (WebView)findViewById(R.id.upload_comment_web);
        web.setVisibility(View.INVISIBLE);
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
        CookieManager.getInstance().setCookie(url, getCookie());
        web.setWebViewClient(new ViewClient());
        web.loadUrl(url, extraHeaders);
    }


    @Override
    public void onResume(){
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }

    @Override
    public void onPause(){
        super.onPause();
        CookieSyncManager.getInstance().stopSync();
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

    protected class ViewClient extends WebViewClient {
        private int stack = 0;
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String ur) {
            CookieSyncManager.getInstance().sync();
            Log.i(TAG, ur);
            view.loadUrl(ur);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            dialog.dismiss();
            Log.e(TAG, "ERROR CODE : " + errorCode);
            Log.e(TAG, description);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPageFinished(WebView v, final String url) {
            if (stack++ == 0) {
                String Script =
                        "var comment = document.getElementById(\"wr_content\");" +
                                "comment.value = \"" + comment + "\";" +
                                "var button = document.getElementById(\"btn_submit\");" +
                                "button.click();";
                Log.i("SCRIPT", Script);
                v.evaluateJavascript(Script, null);
            }

            if(stack == 3){
                new Thread(){
                    @Override
                    public void run(){
                        try {
                            SingleThreadData.doc = Jsoup.connect(SingleThreadData.url).get();
                        } catch (IOException e) {
                            Log.e("PAGE RELOAD", e.getMessage());
                        }
                        dialog.dismiss();
                        startActivity(new Intent(uploadComment.this, SingleThread.class));
                        uploadComment.this.finish();
                    }
                }.start();
            }
        }
    }
}
