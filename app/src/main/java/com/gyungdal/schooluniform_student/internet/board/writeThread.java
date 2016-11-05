package com.gyungdal.schooluniform_student.internet.board;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import com.android.internal.http.multipart.MultipartEntity;
import com.gyungdal.schooluniform_student.Config;
import com.gyungdal.schooluniform_student.internet.store.CookieStore;
import com.loopj.android.http.BlackholeHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.ContentBody;
import cz.msebera.android.httpclient.entity.mime.content.InputStreamBody;
import cz.msebera.android.httpclient.entity.mime.content.StringBody;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * Created by GyungDal on 2016-10-14.
 */

public class writeThread extends AsyncTask<Void, Integer, Boolean> {
    private static final String TAG = writeThread.class.getName();
    private Bitmap photo;
    private String title, desc;
    private Context context;

    public writeThread(Context contxt, String title, Bitmap photo, String desc){
        this.context = contxt;
        this.title = title;
        this.photo = photo;
        this.desc = desc;
    }
    //TODO : Multipart 로 묶어서 업로드
    @SuppressWarnings("WrongThread")
    @Override
    protected Boolean doInBackground(Void... params) {
        try{
            String url = Config.SERVER_URL + Config.WRITE_PATH;
            if(!url.contains(Config.SERVER_PROTOCAL))
                url = Config.SERVER_PROTOCAL + url;
            SyncHttpClient post = new SyncHttpClient();
            post.setEnableRedirects(true);
            InputStreamBody photoStream = new InputStreamBody
                    (new ByteArrayInputStream(bitmapToBytes()), "Photo.png");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create() //객체 생성...
                    .setCharset(Charset.forName("UTF-8")) //인코딩을 UTF-8로.. 다들 UTF-8쓰시죠?
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.setBoundary("-----------------------------" + System.currentTimeMillis());
            builder.addPart("wr_subject", new StringBody(title));
            builder.addPart("wr_content", new StringBody(desc));
            builder.addPart("bf_file", photoStream);
            PersistentCookieStore cookieStore = new PersistentCookieStore(context);
            String cookie = "";
            for (Object aKey : CookieStore.getInstance().getCookies().keySet()) {
                String name = (String) aKey;
                String value = CookieStore.getInstance().getCookies().get(name);
                Log.i(TAG, "name : " + name);
                Log.i(TAG, "value : " + value);
                cookieStore.addCookie(new BasicClientCookie(name, value));
                cookie += name + "=" + value + ";";
            }
            post.setCookieStore(cookieStore);
            getWriteInformation info = new getWriteInformation();
            HashMap<String, String> data = info.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            if(data == null) {
                Log.e(TAG, "Fail get data");
                return false;
            }else{
                Set key = data.keySet();
                for (Object aKey : key) {
                    String name = (String) aKey;
                    String value = data.get(name);
                    Log.i(TAG, "name : " + name);
                    Log.i(TAG, "value : " + value);
                    builder.addPart(name, new StringBody(value));
                }
            }
            post.setUserAgent(Config.USER_AGENT);
            Header[] headers = new Header[5];
            headers[0] = (new BasicHeader("Connection", "Keep-Alive"));
            headers[1] = (new BasicHeader("Accept-Charset", "UTF-8"));
            headers[2] = new BasicHeader("enctype", "multipart/form-data");
            headers[3] = (new BasicHeader("Referer", "http://gyungdal.xyz/school/bbs/write.php?bo_table=exchange"));
            headers[4] = new BasicHeader("Cookie", cookie);
            for(Header header : headers)
                post.addHeader(header.getName(), header.getValue());
            post.setUserAgent(Config.USER_AGENT);
            post.setEnableRedirects(true);
            post.post(url, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e(TAG, responseString);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.e(TAG, responseString);
                }
            });
            return true;
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        return false;
    }
    private byte[] bitmapToBytes(){
        Bitmap bmp = photo;
        if(photo == null) {
            byte[] b = new byte[1];
            b[0] = 0x00;
            return b;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

}
