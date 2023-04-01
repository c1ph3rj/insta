package com.c1ph3rj.insta;

import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.c1ph3rj.insta.common.model.User;
import com.c1ph3rj.insta.dashboardPkg.DashboardScreen;
import com.c1ph3rj.insta.loginPkg.LoginScreen;
import com.c1ph3rj.insta.utils.LocationTrack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public static double latitude;
    public static double longitude;
    public static String deviceLocation;
    public static User userDetails;
    FirebaseUser currentUser;
    FirebaseFirestore fireStoreDb;
    DocumentReference documentReference;
    DocumentSnapshot userDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        try {
            fireStoreDb = FirebaseFirestore.getInstance();
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                documentReference = fireStoreDb.collection("Users").document(currentUser.getUid());
                documentReference.get().addOnCompleteListener(
                        task -> {
                            if(task.isSuccessful()){
                                userDoc = task.getResult();
                                userDetails = userDoc.toObject(User.class);
                                new Handler().postDelayed(() ->
                                        startActivity(new Intent(MainActivity.this, DashboardScreen.class)), 3000);
                            }else{
                                displayToast("Something went Wrong!", MainActivity.this);
                            }
                        }
                );
            }else{
                new Handler().postDelayed(() ->
                        startActivity(new Intent(MainActivity.this, LoginScreen.class)), 3000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayToast(String toastMessage, Context context) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
    }

    public static String getTimeStamp() {
        return String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
    }

    @SuppressLint("HardwareIds")
    public static String deviceInfo(Context context) {
        String deviceInfoVal = "Nil";
        try {
            String uniqueDeviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID
            );
            getDeviceLocation(context);
            JsonObject deviceInfoJson = new JsonObject();
            deviceInfoJson.addProperty("deviceUniqueId", uniqueDeviceId);
            deviceInfoJson.addProperty("deviceBrand", Build.BRAND);
            deviceInfoJson.addProperty("deviceModel", Build.MODEL);
            deviceInfoJson.addProperty("deviceVersion", Build.VERSION.RELEASE);
            deviceInfoJson.addProperty("deviceId", Build.ID);
            deviceInfoJson.addProperty("deviceLocation", deviceLocation);
            deviceInfoJson.addProperty("deviceLat", latitude);
            deviceInfoJson.addProperty("deviceLong", longitude);
            deviceInfoJson.addProperty("deviceLoginTimeStamp", getTimeStamp());
            deviceInfoVal = deviceInfoJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceInfoVal;
    }

    public static Address getDeviceLocation(Context context) {
        Address deviceCurrentAddress = new Address(Locale.US);
        try {
            List<Address> addresses;
            LocationTrack locationTrack = new LocationTrack(context);
            if (locationTrack.canGetLocation()) {
                latitude = locationTrack.getLatitude();
                longitude = locationTrack.getLongitude();
                Geocoder geocoder = new Geocoder(context);
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                try {
                    if (addresses != null && addresses.size() > 0) {
                        deviceLocation = addresses.get(0).getAddressLine(0);
                        deviceCurrentAddress = addresses.get(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceCurrentAddress;
    }

    public static boolean isDeviceIsInNightMode(Context context){
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(UI_MODE_SERVICE);
        return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }
}