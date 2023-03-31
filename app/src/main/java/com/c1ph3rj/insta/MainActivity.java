package com.c1ph3rj.insta;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.c1ph3rj.insta.loginPkg.LoginScreen;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.JsonObject;
import com.google.type.LatLng;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static LatLng latLng;
    public static String deviceLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        startActivity(new Intent(MainActivity.this, LoginScreen.class));
    }

    public static void displayToast(String toastMessage, Context context){
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("HardwareIds")
    public static String deviceInfo(Context context){
        String deviceInfoVal = "Nil";
        try{
            String uniqueDeviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID
            );
            JsonObject deviceInfoJson = new JsonObject();
            deviceInfoJson.addProperty("deviceUniqueId", uniqueDeviceId);
            deviceInfoJson.addProperty("deviceBrand", Build.BRAND);
            deviceInfoJson.addProperty("deviceModel", Build.MODEL);
            deviceInfoJson.addProperty("deviceVersion", Build.VERSION.RELEASE);
            deviceInfoJson.addProperty("deviceId", Build.ID);
        }catch (Exception e){
            e.printStackTrace();
        }
        return deviceInfoVal;
    }

    void getDeviceLocation(){

    }
}