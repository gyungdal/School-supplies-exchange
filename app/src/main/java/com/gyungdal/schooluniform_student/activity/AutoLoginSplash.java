package com.gyungdal.schooluniform_student.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gyungdal.schooluniform_student.R;
import com.gyungdal.schooluniform_student.helper.SharedHelper;
import com.gyungdal.schooluniform_student.internet.Login;
import com.gyungdal.schooluniform_student.school.SchoolData;

import java.util.concurrent.ExecutionException;

/**
 * Created by GyungDal on 2016-09-07.
 */
public class AutoLoginSplash extends AppCompatActivity {
    private static final String TAG = AutoLoginSplash.class.getName();
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_auto_login_splash);
        SharedHelper sharedHelper = new SharedHelper(getApplicationContext());
        Login login = new Login((ProgressBar)findViewById(R.id.loading_progress),
            (TextView)findViewById(R.id.loading_progress_text),
            getApplicationContext(),
            sharedHelper.getValue("id"),
            sharedHelper.getValue("pw"));
        try {
            if(login.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get()){
                startActivity(new Intent(AutoLoginSplash.this, MainActivity.class));
                AutoLoginSplash.this.finish();
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage());
        } catch (ExecutionException e) {
            Log.e(TAG, e.getMessage());
        }

    }
}
