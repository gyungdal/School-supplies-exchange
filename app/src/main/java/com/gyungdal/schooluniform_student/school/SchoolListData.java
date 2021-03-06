package com.gyungdal.schooluniform_student.school;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.gyungdal.schooluniform_student.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by GyungDal on 2016-09-02.
 */
public class SchoolListData extends AsyncTask<Void, Void, Void>{
    private static final String TAG = SchoolListData.class.getName();
    private static final int schoolLength = 5582;
    private static SchoolListData instance;
    private Context context;
    private ArrayList<Item> items;

    public static void setInstance(Context context){
        Log.i(TAG, "Set instance");
        instance = new SchoolListData();
        instance.items = new ArrayList<>(schoolLength);
        instance.context = context;
        instance.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static SchoolListData getInstance(){
        return instance;
    }

    @NonNull
    @Override
    protected Void doInBackground(Void... voids) {
        if(instance.context == null)
            return null;
        try {
            String[] data = instance.context.getResources().getStringArray(R.array.data);
            for(String temp : data){
                String area = temp.substring(0, temp.indexOf(','));
                temp = temp.substring(temp.indexOf(',') + 1, temp.length());
                String detailArea = temp.substring(0, temp.indexOf(','));
                temp = temp.substring(temp.indexOf(',') + 1, temp.length());
                String schoolType = temp.substring(0, temp.indexOf(','));
                temp = temp.substring(temp.indexOf(',') + 1, temp.length());
                String schoolName = temp;
                Item item = new Item(area, detailArea, schoolType, schoolName);
                instance.items.add(item);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        Log.i(TAG, "Make done");
        Log.i(TAG, "Before trim : " + instance.items.size());
        instance.items.trimToSize();
        Log.i(TAG, "After trim : " + instance.items.size());

        //for(Item item : instance.items)
        //    Log.i(TAG, item.schoolName);

        return null;
    }

    public ArrayList<String> getSchoolName(String state, String detailArea, String schoolType){
        HashMap<String, Character> list = new HashMap<>();
        ArrayList<String> result = new ArrayList<>();
        for(Item item : instance.items) {
            if(item.state.equals(state) && item.detailArea.equals(detailArea)
                    && item.schoolType.equals(schoolType))
                list.put(item.schoolName, '.');
        }
        Set key = list.keySet();
        for(Iterator iterator = key.iterator(); iterator.hasNext();)
            result.add((String)iterator.next());

        return result;


    }

    public ArrayList<String> getSchoolType(){
        HashMap<String, Character> list = new HashMap<>();
        ArrayList<String> result = new ArrayList<>();
        if(instance.items == null)
            return null;
        for(Item item : instance.items)
            list.put(item.schoolType, '.');

        Set key = list.keySet();
        for(Iterator iterator = key.iterator(); iterator.hasNext();)
            result.add((String)iterator.next());

        return result;
    }

    public ArrayList<String> getCity(String state){
        HashMap<String, Character> list = new HashMap<>();
        ArrayList<String> result = new ArrayList<>();
        for(Item item : instance.items) {
            if(item.state.equals(state))
                list.put(item.detailArea, '.');
        }
        Set key = list.keySet();
        for(Iterator iterator = key.iterator(); iterator.hasNext();)
            result.add((String)iterator.next());

        return result;
    }

    public ArrayList<String> getState(){
        HashMap<String, Character> list = new HashMap<>();
        ArrayList<String> result = new ArrayList<>();
        if(instance.items == null)
            return null;
        for(Item item : instance.items)
            list.put(item.state, '.');

        Set key = list.keySet();
        for(Iterator iterator = key.iterator(); iterator.hasNext();)
            result.add((String)iterator.next());

        return result;
    }
}
