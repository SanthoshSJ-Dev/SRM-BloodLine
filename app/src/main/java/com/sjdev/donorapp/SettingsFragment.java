package com.sjdev.donorapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sjdev.donorapp.SettingsModule.ChangePasswordActivity;
import com.sjdev.donorapp.SettingsModule.UpdateEmailActivity;
import com.sjdev.donorapp.loginRegister.LoginActivity;

public class SettingsFragment extends Fragment {

    private TextView showFullName, showEmail, showDob, showMobile, showGender, showBloodGroup;

    private ProgressBar progressBar;

    private String fullName, email, dob, mobile, gender, bloodGroup;

    private FirebaseAuth authProfile;

    private Button btnUpdateEmail, btnChangePassword, btnLogout,btnAbout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        //btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnUpdateEmail = view.findViewById(R.id.btnUpdateEmail);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnAbout = view.findViewById(R.id.btnAbout);

        showFullName = view.findViewById(R.id.txtFullName);
        showEmail = view.findViewById(R.id.txtEmail);
        showGender = view.findViewById(R.id.txtGender);
        showMobile = view.findViewById(R.id.txtMobile);
        showDob = view.findViewById(R.id.txtDob);
        showBloodGroup = view.findViewById(R.id.txtBloodGroup);

        progressBar = view.findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(getActivity(), "Something went wrong, User details not available", Toast.LENGTH_LONG).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }
        return view;
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //Extract user reference from users
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null) {
                    fullName = firebaseUser.getDisplayName();
                    email = firebaseUser.getEmail();
                    dob = readUserDetails.dob;
                    gender = readUserDetails.gender;
                    mobile = readUserDetails.mobile;
                    bloodGroup =readUserDetails.bloodGroup;

                    showFullName.setText(fullName);
                    showEmail.setText(email);
                    showDob.setText(dob);
                    showGender.setText(gender);
                    showMobile.setText(mobile);
                    showBloodGroup.setText(bloodGroup);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });

        btnUpdateEmail.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UpdateEmailActivity.class);
            startActivity(intent);
        });

        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AboutActivity.class);
            startActivity(intent);
        });


        btnLogout.setOnClickListener(v -> {
            authProfile.signOut();
            Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);

            //Clear Stack to prevent user coming to user
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        });
    }
}