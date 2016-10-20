package com.gyungdal.schooluniform_student.internet.board;

import android.os.AsyncTask;
import android.util.Log;

import com.gyungdal.schooluniform_student.Config;
import com.gyungdal.schooluniform_student.activity.board.list.recycler.ThreadItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by GyungDal on 2016-10-08.
 */

public class getThreadList extends AsyncTask<Void, Integer, ArrayList<ThreadItem>> {
    private static final String TAG = getThreadList.class.getName();
    private ArrayList<ThreadItem> result;
    private static int no = 1;

    public getThreadList(){
        result = new ArrayList<>();
    }

    @Override
    protected ArrayList<ThreadItem> doInBackground(Void... params) {
        String path = Config.SERVER_PROTOCAL
                + Config.SERVER_URL + Config.BOARD_PATH;
        try {
            Log.i(TAG, "PATH : " + path);
            Document doc = Jsoup
                    .connect(path)
                    .userAgent(Config.USER_AGENT)
                    .header("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.5,en;q=0.3")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .data("url", "/" +
                            Config.SERVER_URL.split("/")[Config.SERVER_URL.split("/").length - 1] + "/")
                    .get();
            Log.i(TAG, doc.toString());
            Elements tables = doc.select(".tbl_head01 > table:nth-child(1) > tbody:nth-child(3)").select("tr");
            Log.i(TAG, "TABLE NUMBER : " + tables.size());
            for(Element table : tables) {
                //Element table = tables.get(0);
                String threadUrl = table.select("a:nth-child(1)").toString();
                threadUrl = threadUrl
                        .substring(threadUrl.indexOf("href=\"") + "href=\"".length(), threadUrl.lastIndexOf("\">"))
                        .replaceAll("amp;", "");
                //temp.views = table.select("td:nth-child(5)").text();
                String writer = table.getElementsByClass("sv_member").get(0).text();
                String title = table.select("a:nth-child(1)").text();
                if(title.contains("댓글") && title.contains("개")
                        && (title.lastIndexOf("댓글") < title.lastIndexOf("개"))){
                    title = title.substring(0, title.lastIndexOf("댓글"));
                }
                Document threadDoc = Jsoup.connect(threadUrl).get();
                //TODO : 이미지가 없는경우 처리
                String imageUrl;
                if(!threadDoc.select(".view_image > img:nth-child(1)")
                        .get(0).toString().trim().isEmpty()) {
                    imageUrl = threadDoc
                            .select(".view_image > img:nth-child(1)")
                            .get(0)
                            .toString();
                    imageUrl = imageUrl
                            .substring(imageUrl.indexOf("\"") + "\"".length());
                    imageUrl = imageUrl
                            .substring(0, imageUrl.indexOf("\""));
                } else
                    imageUrl = "https://weeblr.com/images/products/sh404sef/2015-10-14/medium/joomla-404-error-page.png";

                String date = "20" + threadDoc.select("#bo_v_info > strong:nth-child(4)").get(0).text();
                date = date.substring(0, 4)
                        + "\n" + date.substring(5,10)
                        + "\n" + date.substring(11);
                ThreadItem temp = new ThreadItem(date, title, threadDoc, writer, threadUrl, imageUrl);
                Log.i(TAG, temp.toString());
                result.add(temp);
            }
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
        return result;
    }

}
