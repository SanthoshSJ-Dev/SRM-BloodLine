package com.sjdev.donorapp;

public class ReadWriteUserDetails {

    public String fullName, dob, gender, mobile, email, bloodGroup, status;

    public ReadWriteUserDetails() {
    }

    public ReadWriteUserDetails( String txtFullName, String txtDob, String txtGender, String txtMobile, String txtBloodGroup, String txtStatus) {
        this.fullName = txtFullName;
        this.dob = txtDob;
        this.gender = txtGender;
        this.mobile = txtMobile;
        this.bloodGroup = txtBloodGroup;
        this.status = txtStatus;
    }
}
