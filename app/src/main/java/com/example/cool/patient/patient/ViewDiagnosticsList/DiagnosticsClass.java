package com.example.cool.patient.patient.ViewDiagnosticsList;

/**
 * Created by Udayasri on 25-05-2018.
 */

public class DiagnosticsClass {
    String mobileNumber,diagId,userId,centerName,cashOnHand,creditDebit,paytm,netBanking,landLineNumber,contactPerson,
            latitude,longitude,distance,emergencyService,addressId,centerImage;

    public DiagnosticsClass() {
    }

    public DiagnosticsClass(String mobileNumber, String diagId,String userId, String centerName, String cashOnHand,
                            String creditDebit, String paytm, String netBanking, String landLineNumber, String contactPerson,
                            String latitude, String longitude,String distance, String emergencyService, String addressId, String centerImage) {
        this.mobileNumber = mobileNumber;
        this.diagId = diagId;
        this.userId = userId;
        this.centerName = centerName;
        this.cashOnHand = cashOnHand;
        this.creditDebit = creditDebit;
        this.paytm = paytm;
        this.netBanking = netBanking;
        this.landLineNumber = landLineNumber;
        this.contactPerson = contactPerson;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.emergencyService = emergencyService;
        this.addressId = addressId;
        this.centerImage = centerImage;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDiagId() {
        return diagId;
    }

    public void setDiagId(String diagId) {
        this.diagId = diagId;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getCashOnHand() {
        return cashOnHand;
    }

    public void setCashOnHand(String cashOnHand) {
        this.cashOnHand = cashOnHand;
    }

    public String getCreditDebit() {
        return creditDebit;
    }

    public void setCreditDebit(String creditDebit) {
        this.creditDebit = creditDebit;
    }

    public String getPaytm() {
        return paytm;
    }

    public void setPaytm(String paytm) {
        this.paytm = paytm;
    }

    public String getNetBanking() {
        return netBanking;
    }

    public void setNetBanking(String netBanking) {
        this.netBanking = netBanking;
    }

    public String getLandLineNumber() {
        return landLineNumber;
    }

    public void setLandLineNumber(String landLineNumber) {
        this.landLineNumber = landLineNumber;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getEmergencyService() {
        return emergencyService;
    }

    public void setEmergencyService(String emergencyService) {
        this.emergencyService = emergencyService;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCenterImage() {
        return centerImage;
    }

    public void setCenterImage(String centerImage) {
        this.centerImage = centerImage;
    }
}
