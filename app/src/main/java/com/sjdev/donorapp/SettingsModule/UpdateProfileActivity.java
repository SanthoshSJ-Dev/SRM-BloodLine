package com.sjdev.donorapp.SettingsModule;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sjdev.donorapp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText updateFullName, updateDob, updateMobile;
    private TextView updateEmail, updateBloodGroup;
    private RadioGroup updateGenderGroup;
    private RadioButton updateGenderSelected;
    private String txtFullName, txtDob, txtGender, txtMobile, txtEmail, txtBloodGroup;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        updateEmail = findViewById(R.id.update_email);
        updateBloodGroup = findViewById(R.id.update_blood_group);


        progressBar = findViewById(R.id.progressBar);
        updateFullName = findViewById(R.id.update_full_name);
        updateDob = findViewById(R.id.update_dob);
        updateMobile = findViewById(R.id.update_mobile);
        updateGenderGroup = findViewById(R.id.update_gender_group);

        authProfile =FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        //Show profile Data
      //  showProfile(firebaseUser);

        //Setting up Date Picker on EditText
        updateDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Extract saved date
                String txtSADob[] = txtDob.split("/");

                int date = Integer.parseInt(txtSADob[0]);
                int month = Integer.parseInt(txtSADob[1]) -1;
                int year = Integer.parseInt(txtSADob[2]);

                DatePickerDialog picker;

                //Date Picker Dialog
                picker = new DatePickerDialog(UpdateProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        updateDob.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, date);
                picker.show();
            }
        });

        //Update Profile
        Button btnUpdateProfile =  findViewById(R.id.updateProfileBtn);

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(firebaseUser);
            }
        });
    }

    private void updateProfile(FirebaseUser firebaseUser) {
        int selectedGenderID = updateGenderGroup.getCheckedRadioButtonId();
        updateGenderSelected = findViewById(selectedGenderID);

        //Validate Mobile Number using Matcher and Pattern
        String mobileRegex = "[6-9][0-9]{9}";
        Matcher mobileMatcher;
        Pattern mobilePattern = Pattern.compile(mobileRegex);
        mobileMatcher = mobilePattern.matcher(txtMobile);

        if (TextUtils.isEmpty(txtFullName)) {
            Toast.makeText(UpdateProfileActivity.this, "Please Enter Your Name", Toast.LENGTH_LONG).show();
            updateFullName.setError("Full Name is required");
            updateFullName.requestFocus();
        } else if (TextUtils.isEmpty(txtDob)) {
            Toast.makeText(UpdateProfileActivity.this, "Please Enter Your Date of Birth", Toast.LENGTH_LONG).show();
            updateDob.setError("DOB is required");
            updateDob.requestFocus();
        } else if (TextUtils.isEmpty(updateGenderSelected.getText())) {
            Toast.makeText(UpdateProfileActivity.this, "Please Select Your gender", Toast.LENGTH_LONG).show();
            updateGenderSelected.setError("Gender is required");
            updateGenderSelected.requestFocus();
        } else if (TextUtils.isEmpty(txtMobile)) {
            Toast.makeText(UpdateProfileActivity.this, "Please Enter your Mobile Number", Toast.LENGTH_LONG).show();
            updateMobile.setError("Mobile Number is Required");
            updateMobile.requestFocus();
        } else if (txtMobile.length() != 10) {
            Toast.makeText(UpdateProfileActivity.this, "Please Re-Enter your Mobile Number", Toast.LENGTH_LONG).show();
            updateMobile.setError("Mobile Number should be 10 Digits");
            updateMobile.requestFocus();
        } else if (!mobileMatcher.find()) {
            Toast.makeText(UpdateProfileActivity.this, "Please Enter valid Mobile Number", Toast.LENGTH_LONG).show();
            updateMobile.setError("Mobile Number is Not Valid");
            updateMobile.requestFocus();
        } else {

            //Obtain the data from entered by user
            txtGender = updateGenderSelected.getText().toString();
            txtFullName = updateFullName.getText().toString();
            txtDob = updateDob.getText().toString();
            txtMobile = updateMobile.getText().toString();

            txtEmail = updateEmail.getText().toString();
            txtBloodGroup = updateBloodGroup.getText().toString();
//
//            //Enter user data
//            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(txtFullName, txtDob, txtGender,txtMobile,txtBloodGroup);
//
//            //Extract user Reference from database
//            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("users");
//            String userID = firebaseUser.getUid();
//
//            progressBar.setVisibility(View.VISIBLE);
//
//            referenceProfile.child(userID).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()){
//                        //Setting new display Name
//                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(txtFullName).build();
//                        firebaseUser.updateProfile(profileUpdates);
//
//                        Toast.makeText(UpdateProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
//
//                        //Stop user from returning back Button
//                        Intent intent = new Intent(UpdateProfileActivity.this, MyProfileActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        try {
//                            throw task.getException();
//                        } catch (Exception e){
//                            Toast.makeText(UpdateProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                    progressBar.setVisibility(View.GONE);
//                }
//            });
//
//        }
//
//    }
//
//    private void showProfile(FirebaseUser firebaseUser) {
//        String userIDofRegistered = firebaseUser.getUid();
//
//        //Extract User Reference from Database for "users"
//        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("users");
//
//        progressBar.setVisibility(View.VISIBLE);
//
//        referenceProfile.child(userIDofRegistered).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
//                if (readUserDetails != null){
//                    txtFullName = firebaseUser.getDisplayName();
//                    txtDob = readUserDetails.dob;
//                    txtGender = readUserDetails.gender;
//                    txtMobile = readUserDetails.mobile;
//                    txtEmail = firebaseUser.getEmail();
//                    txtBloodGroup = readUserDetails.bloodGroup;
//
//                    updateFullName.setText(txtFullName);
//                    updateDob.setText(txtDob);
//                    updateMobile.setText(txtMobile);
//                    updateEmail.setText(txtEmail);
//                    updateBloodGroup.setText(txtBloodGroup);
//
//                    //Show Gender through Radio Button
//                    if (txtGender.equals("Male")){
//                        updateGenderSelected = findViewById(R.id.Male);
//                    } else if (txtGender.equals("Female")){
//                        updateGenderSelected = findViewById(R.id.Female);
//                    } else {
//                        updateGenderSelected = findViewById(R.id.Transgender);
//                    }
//                    updateGenderSelected.setChecked(true);
//                } else {
//                    Toast.makeText(UpdateProfileActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
//                }
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(UpdateProfileActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
//            }
//        });

        }}}
