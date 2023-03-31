package com.c1ph3rj.insta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.c1ph3rj.insta.loginPkg.LoginScreen;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class MainActivity extends AppCompatActivity {

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
}