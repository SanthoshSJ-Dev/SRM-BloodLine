package com.sjdev.donorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sjdev.donorapp.SearchView.SearchResults;
import com.sjdev.donorapp.SettingsModule.MyProfileActivity;


public class HomeFragment extends Fragment {

    private ProgressBar progressBar;

    private CardView searchDonor, myProfile;

    private TextView showFullName, showStatus;

    private ImageView showBloodGroup;

    private SwitchMaterial changeStatus;

    private String fullName, bloodGroup, status;

    private FirebaseAuth authProfile;

    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        progressBar = view.findViewById(R.id.progressBar);

        showFullName = view.findViewById(R.id.showUserName);
        showStatus = view.findViewById(R.id.showStatus);
        changeStatus = view.findViewById(R.id.changeStatus);

        // Get reference to your ImageView in your activity
        showBloodGroup = view.findViewById(R.id.showBloodGroup);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        // Initialize swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData(); // Implement your refresh logic here
        });

        // Get the Firebase database reference
        String userID = firebaseUser.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference activeRef = database.getReference("users").child(userID).child("status");

        // Attach an OnClickListener to the switch to show a confirmation dialog when tapped
        changeStatus.setOnClickListener(v -> {

            // Check if the switch is checked or not
            boolean isChecked = changeStatus.isChecked();

            // Create a confirmation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to change the status state?");
            builder.setPositiveButton("Okay", (dialog, which) -> {
                // User confirmed, update the switch state in Firebase
                String status = isChecked ? "Active" : "Inactive";
                activeRef.setValue(status);

                // Dismiss the confirmation dialog
                dialog.dismiss();
            });

            // Set cancelable to false so dialog cannot be dismissed by back button press
            builder.setCancelable(false);

            // Show the confirmation dialog
            builder.create().show();
        });

        // Attach an OnCheckedChangeListener to the switch to update the value in Firebase when checked
        changeStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Do nothing here
        });


        if (firebaseUser == null) {
            Toast.makeText(getContext(), "Something went wrong, User details not available", Toast.LENGTH_LONG).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }

        searchDonor = view.findViewById(R.id.searchDonor);
        myProfile = view.findViewById(R.id.myProfile);

        searchDonor.setOnClickListener(v ->

        {
            Intent intent = new Intent(getActivity(), SearchResults.class);
            startActivity(intent);
        });

        myProfile.setOnClickListener(v ->

        {
            Intent intent = new Intent(getActivity(), MyProfileActivity.class);
            startActivity(intent);
        });


        // Show the ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        // Placeholder method for data refresh logic

        return view;
    }

    // Placeholder method for data refresh logic
    private void refreshData() {
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(getContext(), "Something went wrong, User details not available", Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false); // Stop refreshing
        } else {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //Extract user reference from users
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null) {
                    fullName = firebaseUser.getDisplayName();
                    bloodGroup = readUserDetails.bloodGroup;
                    status = readUserDetails.status;

                    showFullName.setText(fullName);
                    showStatus.setText(status + " Donor");

                    // Attach a ValueEventListener to the database reference to update the switch state
                    if (status.equals("Active")) {
                        changeStatus.setChecked(true);
                    } else {
                        changeStatus.setChecked(false);
                    }

                    switch (bloodGroup) {
                        case "A+ve":
                            showBloodGroup.setImageResource(R.drawable.a_pos);
                            break;
                        case "A-ve":
                            showBloodGroup.setImageResource(R.drawable.a_neg);
                            break;
                        case "A1+ve":
                            showBloodGroup.setImageResource(R.drawable.a1_pos);
                            break;
                        case "A1-ve":
                            showBloodGroup.setImageResource(R.drawable.a1_neg);
                            break;
                        case "A1B+ve":
                            showBloodGroup.setImageResource(R.drawable.a1b_pos);
                            break;
                        case "A1B-ve":
                            showBloodGroup.setImageResource(R.drawable.a1b_neg);
                            break;
                        case "A2+ve":
                            showBloodGroup.setImageResource(R.drawable.a2_pos);
                            break;
                        case "A2-ve":
                            showBloodGroup.setImageResource(R.drawable.a2_neg);
                            break;
                        case "A2B+ve":
                            showBloodGroup.setImageResource(R.drawable.a2b_pos);
                            break;
                        case "A2B-ve":
                            showBloodGroup.setImageResource(R.drawable.a2b_neg);
                            break;
                        case "AB+ve":
                            showBloodGroup.setImageResource(R.drawable.ab_pos);
                            break;
                        case "AB-ve":
                            showBloodGroup.setImageResource(R.drawable.ab_neg);
                            break;
                        case "B+ve":
                            showBloodGroup.setImageResource(R.drawable.b_pos);
                            break;
                        case "B-ve":
                            showBloodGroup.setImageResource(R.drawable.b_neg);
                            break;
                        case "O+ve":
                            showBloodGroup.setImageResource(R.drawable.o_pos);
                            break;
                        case "O-ve":
                            showBloodGroup.setImageResource(R.drawable.o_neg);
                            break;
                        case "Rh+ve":
                            showBloodGroup.setImageResource(R.drawable.rh_pos);
                            break;
                        case "Rh-ve":
                            showBloodGroup.setImageResource(R.drawable.rh_neg);
                            break;
                    }
                }
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false); // Stop refreshing
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}