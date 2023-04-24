package com.c1ph3rj.insta.utils.permissionsPkg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionHandler {
    private final Activity activity;
    private final int requestCode = 1016;
    private PermissionResultListener listener;

    public PermissionHandler(Activity activity) {
        this.activity = activity;
    }

    public boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean hasPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (!hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    public void requestPermission(String permission) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
    }

    public void requestPermissions(String[] permissions) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("permissionCountRef", Context.MODE_PRIVATE);
        SharedPreferences.Editor permissionCountRef = sharedPreferences.edit();
        int permissionCount = sharedPreferences.getInt("PermissionCount", 0);
        permissionCount += 1;
        permissionCountRef.putInt("PermissionCount", permissionCount);
        permissionCountRef.apply();
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED && (permissionCount > 2)) {
                showPermissionExplanationDialog();
                return;
            } else {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsToRequest.toArray(new String[0]), requestCode);
        }
    }

    public String[] getMissingPermissions(String[] permissions) {
        List<String> missingPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (!hasPermission(permission)) {
                missingPermissions.add(permission);
            }
        }
        return missingPermissions.toArray(new String[0]);
    }

    public void setPermissionResultListener(PermissionResultListener listener) {
        this.listener = listener;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                if (listener != null) {
                    listener.onPermissionGranted(permissions[i]);
                }
            } else {
                if (listener != null) {
                    listener.onPermissionDenied(permissions[i]);
                }
            }
        }
    }

    public void showPermissionExplanationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("We do not use your camera, location, phone, etc. without your consent. You can manage app permissions in the app's settings. Please allow required permissions to continue!")
                .setPositiveButton("OK", (dialog, id) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                    intent.setData(uri);
                    activity.startActivity(intent);
                });
        builder.create().show();
    }

    public interface PermissionResultListener {
        void onPermissionGranted(String permission);

        void onPermissionDenied(String permission);
    }
}

