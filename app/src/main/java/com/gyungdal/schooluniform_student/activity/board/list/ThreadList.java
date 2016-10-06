package com.gyungdal.schooluniform_student.activity.board.list;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.gyungdal.schooluniform_student.Config;
import com.gyungdal.schooluniform_student.R;
import com.gyungdal.schooluniform_student.activity.SetSchool;
import com.gyungdal.schooluniform_student.internet.store.ExtraInfoStore;
import com.gyungdal.schooluniform_student.school.SchoolListData;

/**
 * Created by GyungDal on 2016-09-08.
 */
public class ThreadList extends AppCompatActivity{
    private static final String TAG = ThreadList.class.getName();

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_thread_list);
        getWindow().setTitle(ExtraInfoStore.getInstance().getValue(Config.NICK_NAME_STORE));
        SchoolListData.clearItems();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        menu.add(0, Config.MENU.SET_SCHOOL, Menu.NONE, getString(R.string.set_school));
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // doing something
                return true;
            case Config.MENU.SET_SCHOOL :
                startActivity(new Intent(ThreadList.this, SetSchool.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
