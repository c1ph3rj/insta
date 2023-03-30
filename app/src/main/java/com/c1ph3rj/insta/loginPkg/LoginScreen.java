package com.c1ph3rj.insta.loginPkg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.c1ph3rj.insta.R;
import com.c1ph3rj.insta.databinding.ActivityLoginScreenBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginScreen extends AppCompatActivity {
    ActivityLoginScreenBinding loginScreenBinding;
    TextInputLayout userNameLayout, passwordLayout;
    TextInputEditText userNameField, passwordField;
    MaterialButton loginBtn;
    TextView signUpBtn;
    CardView loginWithGoogleBtn;
    ProgressBar loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginScreenBinding = ActivityLoginScreenBinding.inflate(getLayoutInflater());
        setContentView(loginScreenBinding.getRoot());
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void init(){
        try {
            signUpBtn = loginScreenBinding.signUpBtn;
            loginBtn = loginScreenBinding.loginBtn;
            loginWithGoogleBtn = loginScreenBinding.loginInWithGoogleBtn;
            loginProgress = loginScreenBinding.loginProgress;
            userNameLayout = loginScreenBinding.userNameLayout;
            passwordLayout = loginScreenBinding.passwordLayout;
            userNameField = loginScreenBinding.userNameField;
            passwordField = loginScreenBinding.passwordField;

            loginBtn.setOnClickListener(onClickLoginBtn->{
                try {
                    if(userNameLayout.isErrorEnabled()){
                        userNameLayout.setErrorEnabled(false);
                    }
                    if(passwordLayout.isErrorEnabled()){
                        passwordLayout.setErrorEnabled(false);
                    }
                    String userName = Objects.requireNonNull(userNameField.getText()).toString().trim();
                    String password = Objects.requireNonNull(passwordField.getText()).toString().trim();
                    if(userName.isEmpty()){
                        userNameLayout.setErrorEnabled(true);
                        userNameLayout.setError("Please Enter UserName To Continue!");
                        return;
                    }
                    if(password.isEmpty()){
                        passwordLayout.setErrorEnabled(true);
                        passwordLayout.setError("Please Enter Password To Continue!");
                        return;
                    }
                    signInWithEmail(userName, password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            loginWithGoogleBtn.setOnClickListener(onClickLoginWithGoogle ->{
                try {
                    if(loginProgress.getVisibility() == View.GONE){
                        loginProgress.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(() ->
                                runOnUiThread(()->
                                        loginProgress.setVisibility(View.GONE)), 3000);
                    }else{
                        Toast.makeText(this, "Please Wait ...", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void signInWithEmail(String userName, String password) {
        try {
            Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show();
            //TODO Firebase Sign in With Email and Password.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}