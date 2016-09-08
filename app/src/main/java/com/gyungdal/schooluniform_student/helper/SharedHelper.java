package com.gyungdal.schooluniform_student.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by GyungDal on 2016-09-07.
 */
public class SharedHelper {
    private static final String PREFERENCE_NAME = "com.gyungdal.school.student";
    private Context context;
    public SharedHelper(Context context){
        this.context = context;
    }

    public void setValue(String key, String value) {
        SharedPreferences.Editor editor =
                context.getSharedPreferences(PREFERENCE_NAME,
                            Activity.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getValue(String key){
        return context.getSharedPreferences(PREFERENCE_NAME,
                    Activity.MODE_PRIVATE).getString(key, "");
    }
}
