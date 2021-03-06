package com.gyungdal.schooluniform_student.activity.signup;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.gyungdal.schooluniform_student.Config;
import com.gyungdal.schooluniform_student.R;

/**
 * Created by GyungDal on 2016-09-06.
 */
public class SignUp extends AppCompatActivity {
    private static final String TAG = SignUp.class.getName();
    private String registerPage = Config.SERVER_URL + Config.REGISTER_PATH;
    private String schoolArea, schoolName;
    private WebView webView;

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_sign_up);
        Intent intent = getIntent();
        if(intent != null){
            schoolName = intent.getStringExtra("name");
            schoolArea = intent.getStringExtra("area");
            Log.i(TAG, "School Name : " + schoolName);
            Log.i(TAG, "School Area : " + schoolArea);
        }
        webView = (WebView)findViewById(R.id.signWebView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        webView.setWebViewClient(new ViewClient());
        if(!registerPage.contains(Config.SERVER_PROTOCAL))
            registerPage = Config.SERVER_PROTOCAL + registerPage;
        webView.loadUrl(registerPage);

    }

    protected class ViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("Browse", url);
            Log.i(TAG, Config.SERVER_URL
                    + Config.REGISTER_PATH.substring(0, Config.REGISTER_PATH.indexOf("/")));
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.equals(Config.SERVER_URL + Config.REGISTER_RESULT_PATH)) {
                Log.i(TAG, "Register Done!!!");
                Log.i(TAG, "Back to main page");
                Toast.makeText(SignUp.this, getString(R.string.sign_up_success)
                        , Toast.LENGTH_SHORT).show();
                SignUp.this.finish();
            }
            /*if(url.contains(Config.SERVER_URL
                    + Config.REGISTER_PATH.substring(0, Config.REGISTER_PATH.indexOf("/")))) {
             */
            view.loadUrl(url);
            /*} else{
                Toast.makeText(getApplicationContext()
                        , getString(R.string.wrong_access), Toast.LENGTH_SHORT).show();
                view.loadUrl(registerPage);
            }*/
            return true;
        }
    }
}