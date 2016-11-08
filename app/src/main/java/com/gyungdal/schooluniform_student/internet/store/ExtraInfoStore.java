package com.gyungdal.schooluniform_student.internet.store;

import java.util.HashMap;

/**
 * Created by GyungDal on 2016-09-23.
 */

public class ExtraInfoStore {
    private static final ExtraInfoStore instance;
    private HashMap<String, String> data;

    static {
        instance = new ExtraInfoStore();
        instance.data = new HashMap<>();
    }

    public static ExtraInfoStore getInstance(){
        return instance;
    }

    public void setValue(String name, String value){
        instance.data.put(name, value);
    }

    public String getValue(String name){
        return instance.data.get(name);
    }

}
