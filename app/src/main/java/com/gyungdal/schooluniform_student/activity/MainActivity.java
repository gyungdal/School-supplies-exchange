package com.gyungdal.schooluniform_student.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.gyungdal.schooluniform_student.Config;
import com.gyungdal.schooluniform_student.R;
import com.gyungdal.schooluniform_student.activity.board.ArticleList;
import com.gyungdal.schooluniform_student.activity.signup.SetSchool;
import com.gyungdal.schooluniform_student.helper.Permission;
import com.gyungdal.schooluniform_student.helper.SharedHelper;
import com.gyungdal.schooluniform_student.internet.Login;
import com.gyungdal.schooluniform_student.school.SchoolData;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private EditText id;
    private EditText pw;
    private Button login;
    private TextView signUp;
    private CheckBox autoLogin;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        SharedHelper sharedHelper = new SharedHelper(getApplicationContext());
        if (sharedHelper.getValue("auto_login").equals("true")) {
            Intent intent = getIntent();
            if(intent != null && !intent.getBooleanExtra("fail", false)){
                startActivity(new Intent(MainActivity.this, AutoLoginSplash.class));
                MainActivity.this.finish();
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        id = (EditText) findViewById(R.id.id);
        pw = (EditText) findViewById(R.id.pw);
        login = (Button) findViewById(R.id.login_button);
        signUp = (TextView) findViewById(R.id.sign_up);
        autoLogin = (CheckBox) findViewById(R.id.auto_login);
        if (SchoolData.getInstance() == null) {
            SchoolData.setInstance(getApplicationContext());
            SchoolData.getInstance().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SetSchool.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedHelper shared = new SharedHelper(getApplicationContext());
                shared.setValue("id", id.getText().toString());
                shared.setValue("pw", pw.getText().toString());
                shared.setValue("auto_login", autoLogin.isChecked() ? "true" : "false");
                Login login = new Login(id.getText().toString(), pw.getText().toString());
                try {
                    Config.State state = login.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
                    Toast.makeText(MainActivity.this, state.toString(), Toast.LENGTH_SHORT).show();
                    switch (state) {
                        case SUCCESS:
                            startActivity(new Intent(MainActivity.this, ArticleList.class));
                            MainActivity.this.finish();
                            break;

                        case OFFLINE:
                            ActivityCompat.finishAffinity(MainActivity.this);
                            break;

                        case ERROR:
                            break;

                        case FAIL_AUTH:
                            break;
                    }
                } catch (InterruptedException e) {
                    Log.e(TAG, e.getMessage());
                } catch (ExecutionException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        Permission.request(getApplicationContext(), Manifest.permission.INTERNET);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
