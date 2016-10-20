package com.gyungdal.schooluniform_student.activity.board.detail;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gyungdal.schooluniform_student.R;
import com.gyungdal.schooluniform_student.activity.board.detail.comment.CommentAdapter;
import com.gyungdal.schooluniform_student.activity.board.detail.comment.CommentItem;
import com.squareup.picasso.Picasso;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by GyungDal on 2016-10-06.
 */

public class SingleThread extends AppCompatActivity {
    private static final String TAG = SingleThread.class.getName();
    private ArrayList<CommentItem> comments;
    private TextView desc;
    private ImageView image;
    private ListView commentList;
    private CommentAdapter adapter;
    private String pageUrl, photoUrl;
    private Document doc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

        doc = SingleThreadData.doc;
        pageUrl = SingleThreadData.url;
        photoUrl = SingleThreadData.photo;
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(SingleThreadData.title);
        } else if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setTitle(SingleThreadData.title);
        }
        Log.i(TAG, pageUrl);
        Log.i(TAG, doc.toString());

        //이미지가 없으면 404 이미지 출력
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_detail_thread);
        image = (ImageView) findViewById(R.id.thread_image);
        desc = (TextView) findViewById(R.id.thread_desc);
        commentList = (ListView)findViewById(R.id.comment_list);
        Picasso.with(SingleThread.this)
                .load(SingleThreadData.photo)
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.error)
                .into(image);
        desc.setText(doc.select("#bo_v_con").get(0).text());
        if(SingleThreadData.doc.toString()
                .contains("<p id=\"bo_vc_empty\">등록된 댓글이 없습니다.</p>")){
            Log.i(TAG, "NOT FOUND COMMENT");
            Toast.makeText(SingleThread.this, "댓글이 없네", Toast.LENGTH_SHORT).show();
            commentList.setDividerHeight(0);
        }else{
            Log.i(TAG, "Catch comment!!!");
            Toast.makeText(SingleThread.this, "댓글발견", Toast.LENGTH_SHORT).show();
            initComment();
        }
    }

    private void initComment(){
        Elements elements = SingleThreadData.doc.select("#bo_vc");
        elements = elements.select("article");
        Log.i(TAG, "댓글 DOC : " + elements.toString());
        Log.i(TAG, "댓글 갯수 : " + elements.size());
        comments = new ArrayList<>(elements.size());
        for(Element element : elements){
            String id = element
                    .select("header:nth-child(1) > span:nth-child(2)").get(0).text();
            String time = "20" + element
                    .select("header:nth-child(1) > span:nth-child(3) > time:nth-child(1)").get(0).text();
            String comment = element
                    .select("p:nth-child(2)").get(0).text();
            CommentItem item = new CommentItem(id, time, comment);
            comments.add(item);
        }
        Log.i("Comment SIZE!", String.valueOf(comments.size()));
        for(CommentItem item :comments){
            Log.i("Comment DATA", comments.indexOf(item) + "번 댓글");
            Log.i("Comment ID", item.id);
            Log.i("Comment DATE", item.date);
            Log.i("Comment TEXT", item.comment);
        }
        adapter = new CommentAdapter(SingleThread.this, comments);
        commentList.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
