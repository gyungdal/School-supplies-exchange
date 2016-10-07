package com.gyungdal.schooluniform_student.activity.board.list;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.gyungdal.schooluniform_student.Config;
import com.gyungdal.schooluniform_student.R;
import com.gyungdal.schooluniform_student.activity.MainActivity;
import com.gyungdal.schooluniform_student.activity.SetSchool;
import com.gyungdal.schooluniform_student.activity.board.list.recycler.Adapter;
import com.gyungdal.schooluniform_student.activity.board.list.recycler.ThreadItem;
import com.gyungdal.schooluniform_student.activity.board.upload.UploadThread;
import com.gyungdal.schooluniform_student.internet.store.ExtraInfoStore;
import com.gyungdal.schooluniform_student.school.SchoolListData;

import java.util.ArrayList;

/**
 * Created by GyungDal on 2016-09-08.
 */
public class ThreadList extends AppCompatActivity{
    private static final String TAG = ThreadList.class.getName();
    private FloatingSearchView search;
    private FloatingActionButton fab;
    private RecyclerView threadRecycler;
    private Adapter threadRecyclerAdapter;
    private ArrayList<ThreadItem> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_thread_list);
        if(getSupportActionBar() != null)
            setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        SchoolListData.setInstance(ThreadList.this);
        getWindow().setTitle(ExtraInfoStore.getInstance().getValue(Config.NICK_NAME_STORE));

        items = new ArrayList<>();
        threadRecycler = (RecyclerView) findViewById(R.id.thread_list_recycler);
        fab = (FloatingActionButton) findViewById(R.id.thread_write);
        search = (FloatingSearchView) findViewById(R.id.thread_search);

        threadRecycler.setLayoutManager(new GridLayoutManager(this, getGridScale()));
        threadRecycler.setHasFixedSize(false);
        test();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThreadList.this, UploadThread.class));
            }
        });
        fab.setImageBitmap(colorChange(android.R.drawable.ic_input_add));
        search.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                Log.i(TAG, search.getQuery());
            }

            @Override
            public void onSearchAction(String currentQuery) {
                Log.i(TAG, search.getQuery());
            }
        });
        search.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.set_school :
                        startActivity(new Intent(ThreadList.this, SetSchool.class));
                        break;
                }
            }
        });
    }
    private int getGridScale(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if((size.x / size.y >= 9 / 16 && size.x / size.y <= 10 / 16)
                || (size.x / size.y >= 6 / 10 || size.x / size.y <= 6 / 9))
            return 2;
        else
            return 3;
    }
    private void test(){
        items.add(new ThreadItem(true, "2016-10-07", "테스트", "GyungDal"
            , "GyungDal", "http://memozang.com/xe/files/attach/images/123/517/008/57b37b344cfc1cecdc01ebacd99a2d3c.jpg"));
        items.add(new ThreadItem(true, "2016-10-07", "테스트", "GyungDal"
                , "GyungDal", "http://memozang.com/xe/files/attach/images/123/517/008/57b37b344cfc1cecdc01ebacd99a2d3c.jpg"));
        threadRecyclerAdapter = new Adapter(items, ThreadList.this);
        threadRecycler.setAdapter(threadRecyclerAdapter);
    }

    private Bitmap colorChange(int mRes){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mRes);
        int sW = bitmap.getWidth();
        int sH = bitmap.getHeight();
        int[] pixels = new int[sW*sH];
        bitmap.getPixels(pixels,0,sW,0,0,sW,sH);

        for(int i = 0; i<pixels.length; i++){

            if(pixels[i] >= 0xf0000000 && pixels[i] <= -1 ){
                //pixels[i]= Color.parseColor("White");
                pixels[i] = 0xffffffff;
            }
        }
        for(int i = 0;i<pixels.length;i++){
            if((pixels[i] & 0xff) != 0xff)
                pixels[i] = 0x00000000;
        }
        Bitmap b  = Bitmap.createBitmap(pixels, 0, sW, sW, sH,
                Bitmap.Config.ARGB_8888);
        return b;
    }
}
