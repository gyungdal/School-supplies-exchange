package com.gyungdal.schooluniform_student.internet;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gyungdal.schooluniform_student.Config;
import com.gyungdal.schooluniform_student.R;

import org.jsoup.Jsoup;
import org.jsoup.Connection.*;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;


/**
 * Created by GyungDal on 2016-09-07.
 */
public class Login extends AsyncTask<String, Integer, Config.State> {
    private static final String TAG = Login.class.getName();

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
        publishProgress(0);
    }

    @Override
    protected Config.State doInBackground(String... params) {
        try {
            if(isOnline()) {
                Log.i(TAG, "Maybe... Server error...");
                return Config.State.OFFLINE;
            }
            Log.i(TAG, "Start Login");
                publishProgress(1);
            String url = Config.SERVER_URL + Config.LOGIN_PATH;
            if(!url.contains(Config.SERVER_PROTOCAL))
                url = Config.SERVER_PROTOCAL + url;
            Log.i(TAG, url);
            Response response = Jsoup.connect(url)
                    .header("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.5,en;q=0.3")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .data("url", "/" +
                            Config.SERVER_URL.split("/")[Config.SERVER_URL.split("/").length - 1] + "/")
                    .data("mb_id", id)
                    .data("mb_password", pw)
                    .userAgent(Config.USER_AGENT)
                    .method(Method.POST)
                    .execute();
                publishProgress(2);
            Document doc = response.parse();
            Log.i(TAG, doc.toString());
            if(doc.title().contains("오류안내"))
                return Config.State.FAIL_AUTH;

            CookieStore.getInstance().setCookies(response.cookies());
            for( String key : response.cookies().keySet() )
                Log.i(TAG, String.format("키 : %s, 값 : %s", key, response.cookies().get(key)) );

                publishProgress(3);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return Config.State.ERROR;
        }
        return Config.State.SUCCESS;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (context != null) {
            progressBar.setProgress(values[0]);
            switch (values[0]) {
                case 0:
                    textView.setText("Init…");
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
                default:
                    Log.wtf(TAG, String.valueOf(values[0]));
                    break;
            }
        }
    }

    private boolean isOnline() throws IOException, InterruptedException {
        String url = Config.SERVER_URL;
        if(!url.contains(Config.SERVER_PROTOCAL))
            url = Config.SERVER_PROTOCAL + url;
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec("ping "
                + new URL(url).getHost() + " -c 1"); // other servers, for example
        proc.waitFor();
        int exit = proc.exitValue();
        if (exit == 0)
            return true;
        return false;
    }

}
