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
    public static void request(Context context, String permission){
            if (ContextCompat.checkSelfPermission(context,
                    permission)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,
                        permission)) {
                } else {
                    ActivityCompat.requestPermissions((Activity)context,
                            new String[]{permission},
                            id++);
                }
        }
    }
}
