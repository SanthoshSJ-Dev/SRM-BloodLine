package com.sjdev.donorapp.loginRegister;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sjdev.donorapp.R;
import com.sjdev.donorapp.MainActivity;
import com.sjdev.donorapp.ReadWriteUserDetails;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerFullName, registerEmail, registerDob, registerMobile, registerPassword, registerConfirmPassword;
    private CheckBox registerActive;
    private ProgressBar progressBar;
    private RadioGroup registerGender;
    private RadioButton registerGenderSelected;
    private DatePickerDialog picker;
    //Blood Group Spinner
    private String txtBloodGroup;
    private Spinner bloodGroupSpinner;
    private ArrayAdapter<CharSequence>  bloodGroupAdapter;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Toast.makeText(this, "You can Register now", Toast.LENGTH_LONG).show();

        progressBar = findViewById(R.id.progressBar);

        registerFullName = findViewById(R.id.register_full_name);
        registerEmail = findViewById(R.id.register_email);
        registerDob = findViewById(R.id.register_dob);
        registerMobile = findViewById(R.id.register_mobile);

        //CheckBox
        registerActive = findViewById(R.id.register_active);

        registerActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Show the dialog
                    showDialog();
                }
            }
        });

        registerConfirmPassword = findViewById(R.id.register_confirm_password);
        registerPassword = findViewById(R.id.register_password);

        //Radio Button
        registerGender = findViewById(R.id.register_gender);
        registerGender.clearCheck();

        //Setting up Date Picker on EditText
        registerDob.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -18); // Subtract 18 years from current date

            int date = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            // Date Picker Dialog with minimum date set
            picker = new DatePickerDialog(RegisterActivity.this, (view, year1, month1, dayOfMonth) -> registerDob.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1), year, month, date);
            picker.getDatePicker().setMaxDate(calendar.getTimeInMillis()); // Set maximum allowed date
            picker.show();
        });

        // Initialize the Spinner view
        bloodGroupSpinner = findViewById(R.id.register_spinner_blood_group);

        bloodGroupAdapter = ArrayAdapter.createFromResource(RegisterActivity.this,R.array.array_blood_group, R.layout.spinner_layout);
        bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(bloodGroupAdapter);

        Button btnRegister = findViewById(R.id.registerBtn);

        btnRegister.setOnClickListener(v -> {

            //Obtain the entered Data
            String txtFullName = registerFullName.getText().toString();
            String txtEmail = registerEmail.getText().toString();
            String txtDob = registerDob.getText().toString();
            String txtMobile = registerMobile.getText().toString();

            String txtPassword = registerPassword.getText().toString();
            String txtConfirmPassword = registerConfirmPassword.getText().toString();


            //Validate Mobile Number using Matcher and Pattern
            String mobileRegex = "[6-9][0-9]{9}";
            Matcher mobileMatcher;
            Pattern mobilePattern = Pattern.compile(mobileRegex);
            mobileMatcher = mobilePattern.matcher(txtMobile);

            String txtGender;

            int selectedGenderId = registerGender.getCheckedRadioButtonId();
            registerGenderSelected = findViewById(selectedGenderId);

            // Define the regular expression to check if the email is valid
            String emailPattern = "[a-zA-Z0-9._-]+@srmist\\.edu\\.in";

            // Define the regular expression to check if the password is valid
            String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";

            if (TextUtils.isEmpty(txtFullName)) {
                //Toast.makeText(RegisterActivity.this, "Please Enter Your Name", Toast.LENGTH_LONG).show();
                registerFullName.setError("Full Name is required");
                registerFullName.requestFocus();
            } else if (TextUtils.isEmpty(txtEmail)) {
                //Toast.makeText(RegisterActivity.this, "Please Enter Your Email", Toast.LENGTH_LONG).show();
                registerEmail.setError("Email is required");
                registerEmail.requestFocus();
            } else if (!txtEmail.matches(emailPattern)) {
                //Toast.makeText(RegisterActivity.this, "Please enter a valid SRMIST email address", Toast.LENGTH_LONG).show();
                registerEmail.setError("SRMIST Email is required");
                registerEmail.requestFocus();
            } else if (TextUtils.isEmpty(txtDob)) {
                //Toast.makeText(RegisterActivity.this, "Please Enter Your Date of Birth", Toast.LENGTH_LONG).show();
                registerDob.setError("DOB is required");
                registerDob.requestFocus();
            } else if (registerGender.getCheckedRadioButtonId() == -1) {
                //Toast.makeText(RegisterActivity.this, "Please Select Your gender", Toast.LENGTH_LONG).show();
                registerGenderSelected.setError("Gender is required");
                registerGenderSelected.requestFocus();
            } else if (TextUtils.isEmpty(txtMobile)) {
                //Toast.makeText(RegisterActivity.this, "Please Enter your Mobile Number", Toast.LENGTH_LONG).show();
                registerMobile.setError("Mobile Number is Required");
                registerMobile.requestFocus();
            } else if (txtMobile.length() != 10) {
                //Toast.makeText(RegisterActivity.this, "Please Re-Enter your Mobile Number", Toast.LENGTH_LONG).show();
                registerMobile.setError("Mobile Number should be 10 Digits");
                registerMobile.requestFocus();
            } else if (!mobileMatcher.find()) {
                //Toast.makeText(RegisterActivity.this, "Please Enter valid Mobile Number", Toast.LENGTH_LONG).show();
                registerMobile.setError("Mobile Number is Not Valid");
                registerMobile.requestFocus();
            } else if (TextUtils.isEmpty(txtPassword)) {
                Toast.makeText(RegisterActivity.this, "Please Enter your Password", Toast.LENGTH_LONG).show();
                registerPassword.setError("Password is Required");
                registerPassword.requestFocus();
            } else if (!txtPassword.matches(passwordPattern)) {
                Toast.makeText(RegisterActivity.this, "Password is weak", Toast.LENGTH_LONG).show();
                registerPassword.setError("Password length is between 8 and 20 characters\n" +
                        "Password contains at least one uppercase letter\n" +
                        "Password contains at least one lowercase letter\n" +
                        "Password contains at least one digit\n" +
                        "Password contains at least one special character (such as !@#$%^&*)");
                registerPassword.requestFocus();
            } else if (TextUtils.isEmpty(txtConfirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Please Confirm the Password", Toast.LENGTH_LONG).show();
                registerConfirmPassword.setError("Password Confirmation is required");
                registerConfirmPassword.requestFocus();
            } else if (!txtPassword.equals(txtConfirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Enter same Confirm the Password", Toast.LENGTH_LONG).show();
                registerConfirmPassword.setError("Password Confirmation is required");
                registerConfirmPassword.requestFocus();

                //Clear the Entered Passwords
                registerPassword.clearComposingText();
                registerConfirmPassword.clearComposingText();
            } else if (!registerActive.isChecked()) {
                Toast.makeText(RegisterActivity.this, "Please Accept the Terms and Privacy", Toast.LENGTH_LONG).show();
                registerActive.setError("Accept the Terms and Privacy");
                registerActive.requestFocus();
            } else {

                //Select Blood group
                bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                      //  txtBloodGroup = bloodGroupSpinner.getSelectedItem().toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                txtBloodGroup = bloodGroupSpinner.getSelectedItem().toString();

                txtGender = registerGenderSelected.getText().toString();

                //CheckBox Active and InActive
                String txtStatus = registerActive.isChecked() ? "Active" : "Inactive";

                progressBar.setVisibility(View.VISIBLE);

                registerUser(txtFullName, txtEmail, txtDob, txtGender, txtMobile, txtPassword, txtBloodGroup, txtStatus);

            }
        });
    }

    // Register User using the Credentials Given
    private void registerUser( String txtFullName, String txtEmail, String txtDob, String txtGender, String txtMobile , String txtPassword, String txtBloodGroup, String txtStatus) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //Create User Profile
        auth.createUserWithEmailAndPassword(txtEmail, txtPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser firebaseUser = auth.getCurrentUser();


                    //Update Display name of User
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(txtFullName).build();
                    firebaseUser.updateProfile(profileChangeRequest);

                    //Enter User Data into the Firebase Realtime Database
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(txtFullName, txtDob, txtGender, txtMobile, txtBloodGroup, txtStatus);

                    //Extracting User Reference from Database for "users"
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("users");

                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                //Send Verification Email
                                firebaseUser.sendEmailVerification();

                                Toast.makeText(RegisterActivity.this, "User is Successfully Registered. Please Verify your Email", Toast.LENGTH_LONG).show();

                                //Open User Profile after successful registration
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

                                //To prevent User from returning back to Register Activity on pressing back button after registration
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish(); // to close Register Activity

                            } else {
                                Toast.makeText(RegisterActivity.this, "User Registration Failed", Toast.LENGTH_LONG).show();
                            }
                            //Hide ProgressBar
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        registerPassword.setError("Your Password is Too Weak. Kindly use mix of alphabets, numbers and Special Characters");
                        registerPassword.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        registerEmail.setError("Your Email is invalid or already in use. Kindly re-enter");
                        registerEmail.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e) {
                        registerEmail.setError("User is Already Exist, Use Another Mail ID");
                        registerEmail.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    //Hide ProgressBar
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_terms_privacy, null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();

        Button acceptButton = dialogView.findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the accept button click
                // Check the checkbox
                registerActive.setChecked(true);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
