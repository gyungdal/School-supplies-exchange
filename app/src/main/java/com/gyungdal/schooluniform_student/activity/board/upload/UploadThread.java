package com.gyungdal.schooluniform_student.activity.board.upload;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.gyungdal.schooluniform_student.Config;
import com.gyungdal.schooluniform_student.R;
import com.gyungdal.schooluniform_student.activity.SetSchool;
import com.gyungdal.schooluniform_student.activity.board.detail.SingleThread;
import com.gyungdal.schooluniform_student.activity.board.list.ThreadList;
import com.gyungdal.schooluniform_student.helper.Permission;
import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.Provider;
import java.util.concurrent.ExecutionException;

/**
 * Created by GyungDal on 2016-10-06.
 */

public class UploadThread extends AppCompatActivity
        implements View.OnClickListener {
    private static final String TAG = UploadThread.class.getName();
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    private ImageView image;
    private Bitmap photo;
    private Uri captureUri;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_upload_thread);
        image = (ImageView) findViewById(R.id.thread_upload_image);
        image.setOnClickListener(UploadThread.this);

        Permission.request(UploadThread.this, Manifest.permission.CAMERA);
        Permission.request(UploadThread.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        //menu.add(0, Config.MENU.SET_SCHOOL, Menu.NONE, getString(R.string.set_school));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    private void storeImage(Bitmap bitmap, String filePath) {
        File copyFile = new File(filePath.substring(0, filePath.lastIndexOf(File.separator)));
        if(!copyFile.exists())
            copyFile.mkdirs();
        copyFile = new File(filePath);

        BufferedOutputStream out = null;
        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();
            addImageToGallery(copyFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addImageToGallery(final String filePath) {
        final Context context = UploadThread.this;
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
        values.put(MediaStore.MediaColumns.DATA, filePath);
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case Config.MENU.SET_SCHOOL :
                startActivity(new Intent(UploadThread.this, SetSchool.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //사진 촬영
    private void takePhoto(){
        Permission.request(UploadThread.this, Manifest.permission.CAMERA);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //임시 사용 파일 경로 생성

        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    //앨범에서 고르기
    private void takeAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int request, int result, Intent data){
        super.onActivityResult(request, result, data);
        if(result != RESULT_OK) {
            Log.wtf(TAG, "request : " + request + ", result : " + result);
            return;
        }
        switch(request){
            case PICK_FROM_ALBUM :
            case PICK_FROM_CAMERA : {

                captureUri = data.getData();
                Log.i(TAG, "select : " + captureUri.getPath());
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(captureUri, "image/*");

                Log.i(TAG, filePath);
/*
                photo =  (Bitmap) data.getExtras().get("data");
                storeImage(photo, filePath);
                Picasso.with(UploadThread.this)
                        .load(Uri.fromFile(new File(filePath)))
                        .fit()
                        .into(image);*/
                startActivityForResult(intent, CROP_FROM_IMAGE);
                break;
            }
            case CROP_FROM_IMAGE : {
                Log.i(TAG, "DONE CROP IMAGE");
                //잘린 이미지 넘겨 받음
                Log.i(TAG, data.getData() != null ? "Return success" : "Return Fail");
                if (data.getData() != null) {
                    captureUri = data.getData();
                    //storeImage(photo, filePath);

                    Log.i(TAG, filePath);
                    Picasso.with(UploadThread.this)
                            .load(captureUri)
                            .fit()
                            .into(image);

                    try{
                        photo = Picasso.with(UploadThread.this)
                                .load(data.getData())
                                .get();
                        //storeImage(photo, filePath);
                    }catch(Exception e){
                        Log.e(TAG, e.getMessage());
                    }
                    //image.setImageBitmap(photo);
                    //image.setImageBitmap(photo);
                    //image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
                break;
            }
            default :

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.thread_upload_image : { //임시값
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        takePhoto();
                    }
                };

                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        takeAlbum();
                    }
                };

                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                };

                filePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + File.separator + "SchoolExchange" + File.separator
                        + System.currentTimeMillis() + ".jpg";
                new AlertDialog.Builder(UploadThread.this)
                        .setTitle("업로드 사진 선택")
                        .setPositiveButton("사진 촬영", cameraListener)
                        .setNeutralButton("앨범에서 선택", albumListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();
            }
        }
    }
}
