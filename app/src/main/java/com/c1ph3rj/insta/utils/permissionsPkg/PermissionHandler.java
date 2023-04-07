package com.c1ph3rj.insta.utils.permissionsPkg;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionHandler {
    private final Activity activity;
    private PermissionResultListener listener;

    private final int requestCode = 1016;

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
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
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

    public interface PermissionResultListener {
        void onPermissionGranted(String permission);

        void onPermissionDenied(String permission);
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
}

