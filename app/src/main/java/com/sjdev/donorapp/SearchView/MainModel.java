package com.sjdev.donorapp.SearchView;

public class MainModel {

    String fullName;
    String dob;
    String mobile;
    String bloodGroup;
    String gender;

    MainModel() {

    }

    public MainModel(String fullName, String dob, String mobile, String bloodGroup, String gender) {
        this.fullName = fullName;
        this.dob = dob;
        this.mobile = mobile;
        this.bloodGroup = bloodGroup;
        this.gender = gender;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
