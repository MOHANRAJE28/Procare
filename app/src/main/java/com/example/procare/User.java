package com.example.procare;

public class User {

    String FullName,PhoneNumber,Profilephoto;
    public User(){}

    public User(String fullName, String phoneNumber, String profilephoto) {
        FullName = fullName;
        PhoneNumber = phoneNumber;
        Profilephoto = profilephoto;
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

    public String getProfilephoto() {
        return Profilephoto;
    }

    public void setProfilephoto(String profilephoto) {
        Profilephoto = profilephoto;
    }
}
