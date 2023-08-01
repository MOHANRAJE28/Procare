package com.mohan.procare;

public class Request {

    String CustomerTime,CustomerDate,CustomerDescription,CustomerEmail,CustomerName,CustomerNumber,CProfile,ServiceType,BKey,RKey;
    Double Clatitude,Clongitude;

    public Request(){}
    public Request(String customerTime, String customerDate, String customerDescription, String customerEmail, String customerName, String customerNumber, String CProfile, String serviceType, String BKey, String RKey, Double clatitude, Double clongitude) {
        CustomerTime = customerTime;
        CustomerDate = customerDate;
        CustomerDescription = customerDescription;
        CustomerEmail = customerEmail;
        CustomerName = customerName;
        CustomerNumber = customerNumber;
        this.CProfile = CProfile;
        ServiceType = serviceType;
        this.BKey = BKey;
        this.RKey = RKey;
        Clatitude = clatitude;
        Clongitude = clongitude;
    }

    public String getBKey() {
        return BKey;
    }

    public void setBKey(String BKey) {
        this.BKey = BKey;
    }

    public String getRKey() {
        return RKey;
    }

    public void setRKey(String RKey) {
        this.RKey = RKey;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getCProfile() {
        return CProfile;
    }

    public void setCProfile(String CProfile) {
        this.CProfile = CProfile;
    }

    public Double getClatitude() {
        return Clatitude;
    }

    public void setClatitude(Double clatitude) {
        Clatitude = clatitude;
    }

    public Double getClongitude() {
        return Clongitude;
    }

    public void setClongitude(Double clongitude) {
        Clongitude = clongitude;
    }

    public String getCustomerTime() {
        return CustomerTime;
    }

    public void setCustomerTime(String customerTime) {
        CustomerTime = customerTime;
    }

    public String getCustomerDate() {
        return CustomerDate;
    }

    public void setCustomerDate(String customerDate) {
        CustomerDate = customerDate;
    }

    public String getCustomerDescription() {
        return CustomerDescription;
    }

    public void setCustomerDescription(String customerDescription) {
        CustomerDescription = customerDescription;
    }

    public String getCustomerEmail() {
        return CustomerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        CustomerEmail = customerEmail;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        CustomerNumber = customerNumber;
    }
}
