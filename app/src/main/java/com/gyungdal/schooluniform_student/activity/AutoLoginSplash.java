package com.gyungdal.schooluniform_student.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gyungdal.schooluniform_student.R;
import com.gyungdal.schooluniform_student.helper.SharedHelper;
import com.gyungdal.schooluniform_student.internet.Login;

import java.util.concurrent.ExecutionException;

/**
 * Created by GyungDal on 2016-09-07.
 */
public class AutoLoginSplash extends AppCompatActivity {
    private static final String TAG = AutoLoginSplash.class.getName();
    private static final int SUCCESS = 0;
    private static final int OFFLINE = 1;
    private static final int ERROR = 2;
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

            switch (login.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get()){
                case SUCCESS :
                    startActivity(new Intent(AutoLoginSplash.this, MainActivity.class));
                    AutoLoginSplash.this.finish();
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    break;

                case OFFLINE :
                    Toast.makeText(getApplicationContext(), "OFFLINE", Toast.LENGTH_LONG).show();
                    ActivityCompat.finishAffinity(AutoLoginSplash.this);
                    break;

                case ERROR :
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage());
        } catch (ExecutionException e) {
            Log.e(TAG, e.getMessage());
        }

    }
}
