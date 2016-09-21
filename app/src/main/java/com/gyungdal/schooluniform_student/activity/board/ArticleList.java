package com.gyungdal.schooluniform_student.activity.board;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.gyungdal.schooluniform_student.R;
import com.gyungdal.schooluniform_student.school.SchoolData;

/**
 * Created by GyungDal on 2016-09-08.
 */
public class ArticleList extends AppCompatActivity {
    private static final String TAG = ArticleList.class.getName();

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_article_list);
        SchoolData.clearItems();
    }
}
