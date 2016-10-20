package com.gyungdal.schooluniform_student.internet.store;

import java.util.Map;

/**
 * Created by GyungDal on 2016-09-08.
 */
public class CookieStore {
    private Map<String, String> cookies;
    private static CookieStore instance;

    static {
        instance = new CookieStore();

    }

    public void setCookies(Map<String, String> cookies){
        this.cookies = cookies;
    }

    public Map<String, String> getCookies(){
        return this.cookies;
    }

    public static CookieStore getInstance(){
        return instance;
    }
}
