package com.mohan.procare;

public class Booking {

    String CustomerTime,CustomerDate,CustomerDescription,ProviderEmail,ProviderName,ProviderNumber,SProfile,Request;

    public Booking(){}
    public Booking(String customerTime, String customerDate, String customerDescription, String providerEmail, String providerName, String providerNumber, String SProfile, String request) {
        CustomerTime = customerTime;
        CustomerDate = customerDate;
        CustomerDescription = customerDescription;
        ProviderEmail = providerEmail;
        ProviderName = providerName;
        ProviderNumber = providerNumber;
        this.SProfile = SProfile;
        Request = request;
    }

    public String getRequest() {
        return Request;
    }

    public void setRequest(String request) {
        Request = request;
    }

    public String getSProfile() {
        return SProfile;
    }

    public void setSProfile(String SProfile) {
        this.SProfile = SProfile;
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

    public String getProviderEmail() {
        return ProviderEmail;
    }

    public void setProviderEmail(String providerEmail) {
        ProviderEmail = providerEmail;
    }

    public String getProviderName() {
        return ProviderName;
    }

    public void setProviderName(String providerName) {
        ProviderName = providerName;
    }

    public String getProviderNumber() {
        return ProviderNumber;
    }

    public void setProviderNumber(String providerNumber) {
        ProviderNumber = providerNumber;
    }

}
