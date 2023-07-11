package com.example.procare;

public class User {

    String FullName,PhoneNumber,Profilephoto,Qualification,UserEmail;
    public User(){}

    public User(String fullName, String phoneNumber, String profilephoto ,String qualification,String userEmail) {
        FullName = fullName;
        PhoneNumber = phoneNumber;
        Profilephoto = profilephoto;
        Qualification=qualification;
        UserEmail=userEmail;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getQualification() {
        return Qualification;
    }

    public void setQualification(String qualification) {
        Qualification = qualification;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getProfilephoto() {
        return Profilephoto;
    }

    public void setProfilephoto(String profilephoto) {

        Profilephoto = profilephoto;
    }
}
