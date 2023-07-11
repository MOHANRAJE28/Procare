package com.example.procare;

public class Booking {

    String CustomerTime,CustomerDate,CustomerDescription,ProviderEmail,ProviderName,ProviderNumber;

    public Booking(){}
    public Booking(String customerTime, String customerDate, String customerDescription, String providerEmail, String providerName, String providerNumber) {
        CustomerTime = customerTime;
        CustomerDate = customerDate;
        CustomerDescription = customerDescription;
        ProviderEmail = providerEmail;
        ProviderName = providerName;
        ProviderNumber = providerNumber;
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
