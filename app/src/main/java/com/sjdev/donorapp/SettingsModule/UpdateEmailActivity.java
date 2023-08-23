package com.sjdev.donorapp.SettingsModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.sjdev.donorapp.R;

public class UpdateEmailActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;
    private TextView textAuthenticated;
    private String userOldEmail, userNewEmail, userPassword;
    private Button btnUpdateEmailAddress;
    private EditText updateNewEmail, updateEmailPassword, updateOldEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        progressBar = findViewById(R.id.progressBar);
        updateEmailPassword = findViewById(R.id.update_email_password);
        updateNewEmail = findViewById(R.id.update_new_email);

        textAuthenticated = findViewById(R.id.text_authenticated);
        btnUpdateEmailAddress = findViewById(R.id.updateEmailBtn);

        btnUpdateEmailAddress.setEnabled(false); //Button Disabled
        updateNewEmail.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        //Set Old Email ID Textview
        userOldEmail = firebaseUser.getEmail();
        updateOldEmail = findViewById(R.id.update_current_email);

        if (firebaseUser.equals("")) {
            Toast.makeText(this, "Something went Wrong! User's details not Available.", Toast.LENGTH_SHORT).show();
        } else {
            reAuthenticate(firebaseUser);
        }

    }

    //ReAuthenticate/ verify user before updating email
    private void reAuthenticate(FirebaseUser firebaseUser) {
        Button btnVerifyUser = findViewById(R.id.emailAuthenticateBtn);
        btnVerifyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Obtain password for authenticate
                userPassword = updateEmailPassword.getText().toString();

                if (TextUtils.isEmpty(userPassword)) {
                    Toast.makeText(UpdateEmailActivity.this, "Password is needed to Continue", Toast.LENGTH_SHORT).show();
                    updateEmailPassword.setError("Please enter your Password");
                    updateEmailPassword.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    AuthCredential credential = EmailAuthProvider.getCredential(userOldEmail, userPassword);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(UpdateEmailActivity.this, "You can update your Email Address now", Toast.LENGTH_SHORT).show();

                                //Set Textview to show authenticated
                                textAuthenticated.setText("Your are authenticated. You can update your Email now");

                                //editTExt and Button
                                updateNewEmail.setEnabled(true);
                                updateEmailPassword.setEnabled(false);
                                updateOldEmail.setEnabled(false);
                                btnVerifyUser.setEnabled(false);
                                btnUpdateEmailAddress.setEnabled(true);

                                //change color of update email button
                                btnUpdateEmailAddress.setBackgroundTintList(ContextCompat.getColorStateList(UpdateEmailActivity.this, R.color.dark_green));

                                btnUpdateEmailAddress.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        userNewEmail = updateNewEmail.getText().toString();
                                        if (TextUtils.isEmpty(userNewEmail)) {
                                            Toast.makeText(UpdateEmailActivity.this, "New Email is Required", Toast.LENGTH_SHORT).show();
                                            updateNewEmail.setError("Please enter new Email");
                                            updateNewEmail.requestFocus();
                                        } else if (!Patterns.EMAIL_ADDRESS.matcher(userNewEmail).matches()) {
                                            Toast.makeText(UpdateEmailActivity.this, "Please enter valid Email", Toast.LENGTH_SHORT).show();
                                            updateNewEmail.setError("Please provide Valid Email");
                                            updateNewEmail.requestFocus();
                                        } else if (userOldEmail.matches(userNewEmail)) {
                                            Toast.makeText(UpdateEmailActivity.this, "New Email cannot be same as old Email", Toast.LENGTH_SHORT).show();
                                            updateNewEmail.setError("Please Enter New Email");
                                            updateNewEmail.requestFocus();
                                        } else {
                                            progressBar.setVisibility(View.VISIBLE);
                                            updateEmail(firebaseUser);
                                        }
                                    }
                                });
                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(UpdateEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateEmail(FirebaseUser firebaseUser) {

        firebaseUser.updateEmail(userNewEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                 if (task.isComplete()){

                     //Verify Email
                     firebaseUser.sendEmailVerification();

                     Toast.makeText(UpdateEmailActivity.this, "Email has been Updated. Please verify your new Email", Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(UpdateEmailActivity.this, MyProfileActivity.class);
                     startActivity(intent);
                     finish();
                 } else {
                     try {
                         throw task.getException();
                     } catch (Exception e){
                         Toast.makeText(UpdateEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 }
                 progressBar.setVisibility(View.GONE);
            }
        });
    }
}