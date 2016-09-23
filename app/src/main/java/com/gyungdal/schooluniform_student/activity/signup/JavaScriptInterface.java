package com.gyungdal.schooluniform_student.activity.signup;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.gyungdal.schooluniform_student.internet.store.SchoolStore;

/**
 * Created by GyungDal on 2016-09-22.
 */

public class JavaScriptInterface{
    private static final String TAG = JavaScriptInterface.class.getName();

    @JavascriptInterface
    public void getHtml(String html){
        Log.i(TAG, html);
        SchoolStore.getInstance().setId(html);
    }
}