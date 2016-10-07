package com.gyungdal.schooluniform_student.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by GyungDal on 2016-09-07.
 */
public class Permission {
    private static int id;
    public static void request(Activity activity, String permission){
            if (ContextCompat.checkSelfPermission(activity.getApplication(),
                    permission)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        permission)) {
                } else {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{permission},
                            id++);
                }
        }
    }
}
