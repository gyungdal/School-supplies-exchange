package com.gyungdal.schooluniform_student.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gyungdal.schooluniform_student.Config;
import com.gyungdal.schooluniform_student.R;
import com.gyungdal.schooluniform_student.activity.board.list.ThreadList;
import com.gyungdal.schooluniform_student.helper.SharedHelper;
import com.gyungdal.schooluniform_student.internet.Login;
import com.gyungdal.schooluniform_student.internet.store.SchoolStore;

import java.util.concurrent.ExecutionException;

/**
 * Created by GyungDal on 2016-09-07.
 */
public class AutoLoginSplash extends AppCompatActivity {
    private static final String TAG = AutoLoginSplash.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
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
            Config.State state = login.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            Toast.makeText(AutoLoginSplash.this, state.toString(), Toast.LENGTH_SHORT).show();
            switch (state) {
                case SUCCESS:
                    SchoolStore.getInstance().setId(sharedHelper.getValue("id"));
                    startActivity(new Intent(AutoLoginSplash.this, ThreadList.class));
                    AutoLoginSplash.this.finish();
                    break;

                case OFFLINE:
                    ActivityCompat.finishAffinity(AutoLoginSplash.this);
                    break;

                case ERROR:
                    break;

                case FAIL_AUTH:{
                    Intent intent = new Intent(AutoLoginSplash.this, MainActivity.class);
                    intent.putExtra("fail", true);
                    startActivity(intent);
                    AutoLoginSplash.this.finish();
                    break;
                }

                case NOT_FOUND_SCHOOL_DATA:
                    startActivity(new Intent(AutoLoginSplash.this, SetSchool.class));
                    break;
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage());
        } catch (ExecutionException e) {
            Log.e(TAG, e.getMessage());
        }

    }
}