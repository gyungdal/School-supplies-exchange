package com.gyungdal.schooluniform_student.activity.board.upload;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.gyungdal.schooluniform_student.R;
import com.gyungdal.schooluniform_student.activity.board.detail.SingleThread;

import java.io.File;
import java.net.URL;

/**
 * Created by GyungDal on 2016-10-06.
 */

public class UploadThread extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;
    private static final String TAG = UploadThread.class.getName();
    private ImageView image;
    private Uri captureUri;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_thread_list);
        image.setOnClickListener(this);
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

    //사진 촬영
    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //임시 사용 파일 경로 생성
        String path = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        captureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), path));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, captureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    //앨범에서 고르기
    private void takeAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int request, int result, Intent data){
        super.onActivityResult(request, result, data);
        if(result != RESULT_OK)
            return;
        switch(request){
            case PICK_FROM_ALBUM :{
                captureUri = data.getData();
                Log.i(TAG, "select : " + captureUri.getPath());
                //결국 잘라내기를 거쳐야됨
            }
            case PICK_FROM_CAMERA : {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(captureUri, "image/*");

                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_IMAGE);
                break;
            }
            case CROP_FROM_IMAGE : {
                //잘린 이미지 넘겨 받음
                final Bundle extras = data.getExtras();
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "SchoolUniform" + File.separator + System.currentTimeMillis() + ".jpg";

                if(extras != null){
                    Bitmap photo = extras.getParcelable("data");
                }

            }

        }
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.camera : { //임시값

            }
        }
    }
}
