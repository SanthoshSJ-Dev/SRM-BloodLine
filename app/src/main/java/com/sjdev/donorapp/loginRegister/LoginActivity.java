package com.sjdev.donorapp.loginRegister;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.sjdev.donorapp.MainActivity;
import com.sjdev.donorapp.R;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail, loginPassword;

    private ProgressBar progressBar;

    private FirebaseAuth authProfile;

    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Open Register Activity
        TextView buttonRegister= findViewById(R.id.btn_register_now);
        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();

        //Reset Password
        Button btnForgotPassword = findViewById(R.id.btn_forgot_password);
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "You can reset your password", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        //Login User
        Button btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(v -> {
            String txtEmail = loginEmail.getText().toString();
            String txtPassword = loginPassword.getText().toString();

            if (TextUtils.isEmpty(txtEmail)) {
                Toast.makeText(LoginActivity.this, "Please Enter your Email ID", Toast.LENGTH_LONG).show();
                loginEmail.setError("Email is Required");
                loginEmail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
                Toast.makeText(LoginActivity.this, "Please Re-Enter your Email ID", Toast.LENGTH_LONG).show();
                loginEmail.setError("Valid Email is Required");
                loginEmail.requestFocus();
            } else if (TextUtils.isEmpty(txtPassword)) {
                Toast.makeText(LoginActivity.this, "Please Enter your Password", Toast.LENGTH_LONG).show();
                loginPassword.setError("Password is Required");
                loginPassword.requestFocus();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                loginUser(txtEmail, txtPassword);
            }
        });
    }

    private void loginUser(String email, String password) {

        authProfile.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
            if (task.isSuccessful()) {
                //Toast.makeText(LoginActivity.this, "You are logged in now", Toast.LENGTH_LONG).show();

                //Get instance of the current user
                FirebaseUser firebaseUser = authProfile.getCurrentUser();

                //check if Email is verified before user can access their profile
                if (firebaseUser.isEmailVerified()) {
                    //Toast.makeText(this, "You are logged in now", Toast.LENGTH_SHORT).show();

                    //Open MainActivity
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    firebaseUser.sendEmailVerification();
                    authProfile.signOut();
                    showAlertDialog();
                }
            } else {
                try {
                    throw task.getException();
                } catch (FirebaseAuthInvalidUserException e) {
                    loginEmail.setError("User Does not Exist");
                    loginEmail.requestFocus();
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    loginEmail.setError("Invalid Credentials. Kindly, Check and Re-Enter");
                    loginEmail.requestFocus();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    private void showAlertDialog() {
        //Setup Alert Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Please Verify Email now. You cannot login without Email Verification");

        //Open Email App if user click Continue button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // To Email app in the new window and not within our app
                startActivity(intent);
            }
        });

        //Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        //Show the AlertBuilder
        alertDialog.show();
    }

    //Check if user is already login
    @Override
    protected void onStart() {
        super.onStart();
        if (authProfile.getCurrentUser() != null) {
            //Toast.makeText(this, "Already Logged In!", Toast.LENGTH_SHORT).show();

            //Start Home Activity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish(); // Close Login Activity

        } else {
            //Toast.makeText(this, "You are login Now", Toast.LENGTH_SHORT).show();
        }
    }
}