package com.example.procare;

public class User {

    String FullName,PhoneNumber,Profilephoto,Qualification,UserEmail, uid;;
    public User(){}

    public User(String fullName, String phoneNumber, String profilephoto, String qualification, String userEmail, String uid) {
        FullName = fullName;
        PhoneNumber = phoneNumber;
        Profilephoto = profilephoto;
        Qualification = qualification;
        UserEmail = userEmail;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

//    public Object getUid() {
//        return null;
//    }
}
