package com.sjdev.donorapp.SettingsModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sjdev.donorapp.MainActivity;
import com.sjdev.donorapp.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private EditText updateCurrentPwd, updateNewPwd, updateConNewPwd;
    private TextView textAuthenticated;
    private Button btnChangePassword, btnAuthenticate;
    private ProgressBar progressBar;
    private String userCurrentPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        updateCurrentPwd = findViewById(R.id.updatePwd_old_password);
        updateNewPwd = findViewById(R.id.updatePwd_new_password);
        updateConNewPwd = findViewById(R.id.updatePwd_confirm_new_password);
        textAuthenticated = findViewById(R.id.text_authenticated);
        progressBar = findViewById(R.id.progressBar);

        btnAuthenticate = findViewById(R.id.pwdAuthenticateBtn);
        btnChangePassword = findViewById(R.id.updatePwdBtn);

        //Disable Button and Edit Text
        updateNewPwd.setEnabled(false);
        updateConNewPwd.setEnabled(false);
        btnChangePassword.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser.equals("")) {
            Toast.makeText(this, "Something went Wrong! User's Details not Available", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            ReAuthenticateUser(firebaseUser);
        }
    }

    private void ReAuthenticateUser(FirebaseUser firebaseUser) {
        btnAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCurrentPwd = updateCurrentPwd.getText().toString();

                if (TextUtils.isEmpty(userCurrentPwd)) {
                    Toast.makeText(ChangePasswordActivity.this, "Password is needed", Toast.LENGTH_SHORT).show();
                    updateCurrentPwd.setError("Please Enter your current Password to Authenticate");
                    updateCurrentPwd.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    //ReAuthenticate
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), userCurrentPwd);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);

                                //Disable Button and Edit text and Enable tooo
                                updateCurrentPwd.setEnabled(false);
                                btnAuthenticate.setEnabled(false);

                                updateNewPwd.setEnabled(true);
                                updateConNewPwd.setEnabled(true);
                                btnChangePassword.setEnabled(true);

                                //TextView to Authenticated message
                                textAuthenticated.setText("You are authenticated. You can change password now");

                                Toast.makeText(ChangePasswordActivity.this, "Password has been Verified." + "Change Password Now", Toast.LENGTH_LONG).show();

                                //Change Password Button
                                btnChangePassword.setBackgroundTintList(ContextCompat.getColorStateList(ChangePasswordActivity.this, R.color.dark_green));

                                btnChangePassword.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changePwd(firebaseUser);
                                    }
                                });
                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private void changePwd(FirebaseUser firebaseUser) {

        String userPwdNew = updateNewPwd.getText().toString();
        String userConPwdNew = updateConNewPwd.getText().toString();

        if (TextUtils.isEmpty(userPwdNew)) {
            Toast.makeText(this, "New Password is Needed", Toast.LENGTH_SHORT).show();
            updateNewPwd.setError("Please enter your New Password");
            updateNewPwd.requestFocus();
        } else if (TextUtils.isEmpty(userConPwdNew)) {
            Toast.makeText(this, "Confirm Password is Needed", Toast.LENGTH_SHORT).show();
            updateConNewPwd.setError("Please Re-Enter your New Password");
            updateConNewPwd.requestFocus();
        } else if (!userPwdNew.matches(userConPwdNew)) {
            Toast.makeText(this, "Password did not match", Toast.LENGTH_SHORT).show();
            updateConNewPwd.setError("Please Re-Enter same Password");
            updateConNewPwd.requestFocus();
        } else if (userCurrentPwd.matches(userPwdNew)) {
            Toast.makeText(this, "New Password cannot be same as old password", Toast.LENGTH_SHORT).show();
            updateNewPwd.setError("Please enter New Password");
            updateNewPwd.requestFocus();
        } else {
            progressBar.setVisibility(View.VISIBLE);

            firebaseUser.updatePassword(userPwdNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ChangePasswordActivity.this, "Password has been changed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            throw task.getException();
                        } catch (Exception e) {
                            Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

    }
}
