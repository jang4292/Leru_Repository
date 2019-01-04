package com.bpm.bpm_ver4.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class Permission {

    public static final int PERMISSION_ALL_REQUEST_CODE = 100;
    public static final int PERMISSION_CAMERA_REQUEST_CODE = 101;
    public static final int PERMISSION_GALLERY_REQUEST_CODE = 102;

    private static final String[] PERMISSION = {
        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static boolean permissionAll(Activity activity) {
        for (String permission : PERMISSION) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSION, PERMISSION_ALL_REQUEST_CODE);
                return false;
            }
        }

        return true;
    }


    public static boolean requestPermission(Activity activity, int request_code) {

        if (request_code == PERMISSION_GALLERY_REQUEST_CODE) {
            // 파일 읽기/쓰기 권한
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_GALLERY_REQUEST_CODE);
                return false;
            }
        }
        else if (request_code == PERMISSION_CAMERA_REQUEST_CODE) {
            // 카메라 권한
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CAMERA_REQUEST_CODE);
                return false;
            }
        }

        return true;
    }
}
