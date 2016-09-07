package com.gyungdal.schooluniform_student.internet;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gyungdal.schooluniform_student.R;
import com.gyungdal.schooluniform_student.activity.AutoLoginSplash;
import com.gyungdal.schooluniform_student.activity.MainActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by GyungDal on 2016-09-07.
 */
public class Login extends AsyncTask<String, Integer, Boolean> {
    private static final String TAG = Login.class.getName();
    private static final String LOGIN_URL = "http://gyungdal.xyz/school/bbs/login.php";
    private static final String URL_SET = "url=%252Fschool%252F";
    private static final String ID_SET = "&mb_id=";
    private static final String PW_SET = "&mb_password=";
    public static final String HEADER_CONTYPE = "header_ConType = \"application/x-www-form-urlencoded\";";
    public static final String HEADER_UA = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0";
    public static final String HEADER_REFERERURI = "http://gyungdal.xyz/school/";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String CONTENT_TYPE = "Content-Type";

    private ProgressBar progressBar;
    private TextView textView;
    private Context context;
    private String id, pw;

    public Login(String id, String pw) {
        this.progressBar = null;
        this.textView = null;
        this.context = null;
        this.id = id;
        this.pw = pw;
    }

    public Login(ProgressBar progressBar, TextView textView, Context context
            , String id, String pw) {
        this.progressBar = progressBar;
        this.textView = textView;
        this.context = context;
        this.id = id;
        this.pw = pw;
        progressBar.setMax(3);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            if(context != null)
                publishProgress(1);

            final String requestData = URL_SET + ID_SET + id + PW_SET + pw;
            URL url = new URL(LOGIN_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
                System.setProperty("http.keepAlive", "false");
            }
            conn.setRequestProperty("Referer", HEADER_REFERERURI);
            //conn.setRequestProperty(CONTENT_TYPE, HEADER_CONTYPE);
            conn.setRequestProperty("User-Agent",HEADER_UA);
            conn.setRequestProperty("Host", "gyungdal.xyz");
            Log.i(TAG, requestData);
            Log.i(TAG, "Size : " + String.valueOf(40 + id.length() + pw.length() + "&auto_login=1".length()));
            conn.setConnectTimeout(5000);
            conn.setInstanceFollowRedirects(true);
            conn.setUseCaches(true);
            conn.setDefaultUseCaches(true);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            if(context != null)
                publishProgress(2);

            String temp = "Content-Type: application/x-www-form-urlencoded\n" +
                    "Content-Length: 64\n" +
                    "\n" +
                    "url=%252Fschool%252F&mb_id=admin&mb_password=aa1003&auto_login=1";
            OutputStream wr = conn.getOutputStream();
            wr.write(temp.getBytes());
            wr.flush();
            wr.close();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String buffer = "", inputLine;
            while ((inputLine = in.readLine()) != null) {
                buffer += inputLine;
            }
            in.close();
            Log.i(TAG, buffer);
            if(context != null)
                publishProgress(3);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
        return true;
    }

    //publishProgress()에 의해 호출되는 메소드
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (context != null) {
            progressBar.setProgress(values[0]);
            switch (values[0]) {
                case 1:
                    textView.setText(context.getString(R.string.access_login_server));
                    break;
                case 2:
                    textView.setText(context.getString(R.string.request_login));
                    break;
                case 3:
                    textView.setText(context.getString(R.string.login_success));
                    Toast.makeText(context, context.getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
