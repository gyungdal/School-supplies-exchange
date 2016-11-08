package com.gyungdal.schooluniform_student.internet.store;

import android.util.Log;

import com.gyungdal.schooluniform_student.internet.school.Item;

/**
 * Created by GyungDal on 2016-09-22.
 */

public class SchoolStore {
    private static final String TAG = SchoolStore.class.getName();
    private static final SchoolStore instance;
    private Item item;

    static {
        instance = new SchoolStore();
        instance.item = new Item();
    }

    private void log(){
        Log.i(TAG, instance.item.id);
        Log.i(TAG, instance.item.name);
        Log.i(TAG, String.valueOf(instance.item.grade));
        Log.i(TAG, String.valueOf(instance.item.Class));
        Log.i(TAG, String.valueOf(instance.item.number));
        Log.i(TAG, String.valueOf(instance.item.year));
    }
    public static SchoolStore getInstance(){
        return instance;
    }

    public void setSchoolName(String name){
        instance.item.name = name;
        log();
    }

    public void setSchoolGrade(int grade){
        instance.item.grade = grade;
        log();
    }

    public void setSchoolClass(int Class){
        instance.item.Class = Class;
        log();
    }

    public void setSchoolNumber(int number){
        instance.item.number = number;
        log();
    }

    public void setId(String id){
        instance.item.id = id;
        log();
    }

    public void setItem(Item item){
        if(item.id != null && instance.item != null) {
            if (item.id.isEmpty() && (!instance.item.id.isEmpty()))
                item.id = instance.item.id;
        }
        instance.item = item;
    }

    public Item getItem(){
        return instance.item;
    }
}
