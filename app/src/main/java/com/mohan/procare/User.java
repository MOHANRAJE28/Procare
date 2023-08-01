package com.mohan.procare;

public class User {

    String FullName,PhoneNumber,Profilephoto,Qualification,UserEmail, uid;

    private double Slatitude; // Service provider's latitude
    private double Slongitude;
    public User(){}

    public User(String fullName, String phoneNumber, String profilephoto, String qualification, String userEmail, String uid, double slatitude, double slongitude) {
        FullName = fullName;
        PhoneNumber = phoneNumber;
        Profilephoto = profilephoto;
        Qualification = qualification;
        UserEmail = userEmail;
        this.uid = uid;
        Slatitude = slatitude;
        Slongitude = slongitude;
    }

    public double getSlatitude() {
        return Slatitude;
    }

    public void setSlatitude(double slatitude) {
        Slatitude = slatitude;
    }

    public double getSlongitude() {
        return Slongitude;
    }

    public void setSlongitude(double slongitude) {
        Slongitude = slongitude;
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
