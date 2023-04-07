package com.c1ph3rj.insta;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.c1ph3rj.insta.common.model.FriendsModel;
import com.c1ph3rj.insta.common.model.PostModel;
import com.c1ph3rj.insta.common.model.UserModel;
import com.c1ph3rj.insta.dashboardPkg.DashboardScreen;
import com.c1ph3rj.insta.loginPkg.LoginScreen;
import com.c1ph3rj.insta.utils.LocationPkg.LocationTrack;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/*
TODO Tomorrow tasks.
  working on POST feature.
  Add new feature to add post.
  fetch all the images and video files form the local storage and show it in grid view.
  add camera feature to capture and record video.
  method to upload the post data to the firebase storage and add the details to the post array in user model (Not yet created).
  fetch the post and display it in the feeds page of other users.
  fetch the post and display it in the user profile section.
*/

public class MainActivity extends AppCompatActivity {
    public static double latitude;
    public static double longitude;
    public static String deviceLocation;
    public static UserModel userModelDetails;
    public static ArrayList<FriendsModel> listOfFollowers;
    public static ArrayList<FriendsModel> listOfFollowing;
    public static ArrayList<String> listOfFollowersUuid;
    public static ArrayList<String> listOfFollowingUuid;
    public static ArrayList<PostModel> listOfPosts;

    FirebaseUser currentUser;
    FirebaseFirestore fireStoreDb;
    DocumentReference documentReference;
    DocumentSnapshot userDoc;
    public static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    public static final String[] LOCATION_PERMISSION = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
    };
    public static final String[] STORAGE_PERMISSION = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final String[] ALL_PERMISSIONS = new String[]{
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.RECEIVE_SMS,
            android.Manifest.permission.RECEIVE_WAP_PUSH,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        try {
            listOfFollowersUuid = new ArrayList<>();
            listOfFollowingUuid = new ArrayList<>();
            fireStoreDb = FirebaseFirestore.getInstance();
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                documentReference = fireStoreDb.collection("Users").document(currentUser.getUid());
                Task<DocumentSnapshot> getUserDetails = documentReference.get().addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                userDoc = task.getResult();
                                userModelDetails = userDoc.toObject(UserModel.class);
                            } else {
                                displayToast("Something went Wrong!", MainActivity.this);
                            }
                        }
                );

                Task<QuerySnapshot> getUserFollowersDetails = fireStoreDb.collection("Users")
                        .document(currentUser.getUid())
                        .collection("followers")
                        .get()
                        .addOnCompleteListener(task -> {
                            listOfFollowers = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                listOfFollowers.add(documentSnapshot.toObject(FriendsModel.class));
                                listOfFollowersUuid.add(Objects.requireNonNull(documentSnapshot.get("uuid")).toString());
                            }
                        })
                        .addOnFailureListener(e -> {
                            e.printStackTrace();
                            listOfFollowers = new ArrayList<>();
                        });
                Task<QuerySnapshot> getUserFollowingDetails = fireStoreDb.collection("Users")
                        .document(currentUser.getUid())
                        .collection("following")
                        .get()
                        .addOnCompleteListener(task -> {
                            listOfFollowing = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                listOfFollowing.add(documentSnapshot.toObject(FriendsModel.class));
                                listOfFollowingUuid.add(Objects.requireNonNull(documentSnapshot.get("uuid")).toString());
                            }
                        })
                        .addOnFailureListener(e -> {
                            e.printStackTrace();
                            listOfFollowing = new ArrayList<>();
                        });
                Task<QuerySnapshot> getUserPostDetails = fireStoreDb.collection("Users")
                        .document(currentUser.getUid())
                        .collection("posts")
                        .get()
                        .addOnCompleteListener(task -> {
                            listOfPosts = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                listOfPosts.add(documentSnapshot.toObject(PostModel.class));
                            }
                        })
                        .addOnFailureListener(e -> {
                            e.printStackTrace();
                            listOfPosts = new ArrayList<>();
                        });

                List<Task<?>> listOfTasks = new ArrayList<>();
                listOfTasks.add(getUserDetails);
                listOfTasks.add(getUserFollowersDetails);
                listOfTasks.add(getUserFollowingDetails);
                listOfTasks.add(getUserPostDetails);
                Tasks.whenAll(listOfTasks)
                        .addOnCompleteListener(task -> new Handler().postDelayed(() ->
                                startActivity(new Intent(MainActivity.this, DashboardScreen.class)), 3000));

            } else {
                new Handler().postDelayed(() ->
                        startActivity(new Intent(MainActivity.this, LoginScreen.class)), 3000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}