package com.abrutsze.tableview.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;

import java.util.ArrayList;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Permissions {

    private static final String[] PERMISSIONS = {
            WRITE_EXTERNAL_STORAGE
    };
    private Activity activity;

    public Permissions(Activity activity) {
        this.activity = activity;
    }

    public void gimmePermission() {
        if (!canWork()) {
            ActivityCompat.requestPermissions(activity,
                    permissions(), 10);
        }
    }

    public boolean canWork() {
        return (hasPermission(WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String permission) {
        return (ContextCompat.checkSelfPermission(activity, permission) ==
                PackageManager.PERMISSION_GRANTED);
    }

    private String[] permissions() {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : Permissions.PERMISSIONS) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return (result.toArray(new String[result.size()]));
    }

    public String getPermissionLabel(String permission, Context context) {
        try {
            PermissionInfo permissionInfo = context.getPackageManager().getPermissionInfo(permission, 0);
            return permissionInfo.loadLabel(context.getPackageManager()).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
