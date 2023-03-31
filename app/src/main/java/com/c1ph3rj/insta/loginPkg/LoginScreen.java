package com.c1ph3rj.insta.loginPkg;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.c1ph3rj.insta.databinding.ActivityLoginScreenBinding;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class LoginScreen extends AppCompatActivity {
    ActivityLoginScreenBinding loginScreenBinding;
    TextInputLayout userNameLayout, passwordLayout;
    TextInputEditText userNameField, passwordField;
    MaterialButton loginBtn;
    TextView signUpBtn;
    CardView loginWithGoogleBtn;
    ProgressBar loginProgress;
    FirebaseAuth firebaseAuth;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
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

    void init() {
        try {
            signUpBtn = loginScreenBinding.signUpBtn;
            loginBtn = loginScreenBinding.loginBtn;
            loginWithGoogleBtn = loginScreenBinding.loginInWithGoogleBtn;
            loginProgress = loginScreenBinding.loginProgress;
            userNameLayout = loginScreenBinding.userNameLayout;
            passwordLayout = loginScreenBinding.passwordLayout;
            userNameField = loginScreenBinding.userNameField;
            passwordField = loginScreenBinding.passwordField;
            firebaseAuth = FirebaseAuth.getInstance();
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("433538863198-6j5tukbsmls87juoop85rn71vl3on53s.apps.googleusercontent.com")
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            loginBtn.setOnClickListener(onClickLoginBtn -> {
                try {
                    if (userNameLayout.getError() != null) {
                        userNameLayout.setError(null);
                    }
                    if (passwordLayout.getError() != null) {
                        passwordLayout.setError(null);
                    }
                    String userName = Objects.requireNonNull(userNameField.getText()).toString().trim();
                    String password = Objects.requireNonNull(passwordField.getText()).toString().trim();
                    if (userName.isEmpty()) {
                        userNameLayout.setError("Please Enter Email Address To Continue!");
                        return;
                    }
                    if (!userName.matches("^[_A-Za-z\\d-]+(\\.[_A-Za-z\\d-]+)*@[A-Za-z\\d]+(\\.[A-Za-z\\d]+)*(\\.[A-Za-z]{2,})$")) {
                        userNameLayout.setError("Please Enter a Valid Email Address!");
                        return;
                    }
                    if (password.isEmpty()) {
                        passwordLayout.setError("Please Enter Password To Continue!");
                        return;
                    }
                    signInWithEmail(userName, password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });


            loginWithGoogleBtn.setOnClickListener(onClickLoginWithGoogle -> {
                try {
                    if (loginProgress.getVisibility() == View.GONE) {
                        loginProgress.setVisibility(View.VISIBLE);
                        signInWithGoogleIntent();
                    } else {
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

    private void signInWithGoogleIntent() {
        try {
            firebaseAuth.signOut();
            mGoogleSignInClient.signOut();
            mGoogleSignInClient.revokeAccess();
            Intent signInWithGoogleIntent = mGoogleSignInClient.getSignInIntent();
            getResult.launch(signInWithGoogleIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public ActivityResultLauncher<Intent> getResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                loginProgress.setVisibility(View.GONE);
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    GoogleSignInAccount userAccount = task.getResult();
                    if(userAccount.getIdToken() != null){
                        Toast.makeText(this, " to Login.", Toast.LENGTH_SHORT).show();

                        AuthCredential userCredentials = GoogleAuthProvider.getCredential(userAccount.getIdToken(),null);
                        firebaseAuth.signInWithCredential(userCredentials)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(LoginScreen.this, Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName() + " ", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(LoginScreen.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(Throwable::printStackTrace);
                    }else {
                        Toast.makeText(this, "Failed to Login.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    if(task.isCanceled()){
                        task.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        }).addOnCanceledListener(new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                task.getException().printStackTrace();
                            }
                        });
                    }else if(task.isSuccessful()){
                        task.getException().printStackTrace();
                    }else  if(task.isComplete()){
                        task.getException().printStackTrace();
                    }
                }
            });
    private void signInWithEmail(String userName, String password) {
        try {
            firebaseAuth.signInWithEmailAndPassword(userName, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Logged In.", Toast.LENGTH_SHORT).show();
                        } else {

                            String errorCode = ((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode();

                            switch (errorCode) {

                                case "ERROR_INVALID_CUSTOM_TOKEN":
                                    Toast.makeText(LoginScreen.this, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                    Toast.makeText(LoginScreen.this, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_INVALID_CREDENTIAL":
                                    Toast.makeText(LoginScreen.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_INVALID_EMAIL":
                                    Toast.makeText(LoginScreen.this, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                    userNameLayout.setError("The email address is badly formatted.");
                                    userNameLayout.requestFocus();
                                    break;

                                case "ERROR_WRONG_PASSWORD":
                                    Toast.makeText(LoginScreen.this, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                    passwordLayout.setError("password is incorrect ");
                                    passwordLayout.requestFocus();
                                    passwordField.setText("");
                                    break;

                                case "ERROR_USER_MISMATCH":
                                    Toast.makeText(LoginScreen.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_REQUIRES_RECENT_LOGIN":
                                    Toast.makeText(LoginScreen.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                    Toast.makeText(LoginScreen.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    Toast.makeText(LoginScreen.this, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                                    userNameLayout.setError("The email address is already in use by another account.");
                                    userNameLayout.requestFocus();
                                    break;

                                case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                    Toast.makeText(LoginScreen.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_DISABLED":
                                    Toast.makeText(LoginScreen.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_TOKEN_EXPIRED":

                                case "ERROR_INVALID_USER_TOKEN":
                                    Toast.makeText(LoginScreen.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_NOT_FOUND":
                                    Toast.makeText(LoginScreen.this, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_OPERATION_NOT_ALLOWED":
                                    Toast.makeText(LoginScreen.this, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_WEAK_PASSWORD":
                                    Toast.makeText(LoginScreen.this, "The given password is invalid.", Toast.LENGTH_LONG).show();
                                    passwordLayout.setError("The password is invalid it must 6 characters at least");
                                    passwordLayout.requestFocus();
                                    break;

                            }
                        }
                    })
                    .addOnFailureListener(Throwable::printStackTrace);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}