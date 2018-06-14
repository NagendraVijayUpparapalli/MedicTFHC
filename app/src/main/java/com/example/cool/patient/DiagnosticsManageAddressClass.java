package com.example.cool.patient;

/**
 * Created by Udayasri on 23-05-2018.
 */

public class DiagnosticsManageAddressClass {

    public String mobileNumeber,diagnosticId,cityName,stateName,centerName,address1,comment,emergencyContactNumber,stateId,cityId,
            pincode,district,landLineNo,contactPerson,latitude,longitude,addressId,fromTime,toTime,deleteReason,registerdMobileNumber;

    public boolean emergencyservice;

    public DiagnosticsManageAddressClass() {
    }

    public DiagnosticsManageAddressClass(String mobileNumeber, String diagnosticId, String cityName, String stateName, String centerName,
                                         String address1, String comment, String emergencyContactNumber, String stateId, String cityId,
                                         String pincode, String district, String landLineNo, String contactPerson, String latitude,
                                         String longitude, String addressId, String fromTime, String toTime, String deleteReason,
                                         boolean emergencyservice,String registerdMobileNumber) {
        this.mobileNumeber = mobileNumeber;
        this.diagnosticId = diagnosticId;
        this.cityName = cityName;
        this.stateName = stateName;
        this.centerName = centerName;
        this.address1 = address1;
        this.comment = comment;
        this.emergencyContactNumber = emergencyContactNumber;
        this.stateId = stateId;
        this.cityId = cityId;
        this.pincode = pincode;
        this.district = district;
        this.landLineNo = landLineNo;
        this.contactPerson = contactPerson;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addressId = addressId;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.deleteReason = deleteReason;
        this.emergencyservice = emergencyservice;
        this.registerdMobileNumber = registerdMobileNumber;
    }

    public String getRegisterdMobileNumber() {
        return registerdMobileNumber;
    }

    public void setRegisterdMobileNumber(String registerdMobileNumber) {
        this.registerdMobileNumber = registerdMobileNumber;
    }

    public String getMobileNumeber() {
        return mobileNumeber;
    }

    public void setMobileNumeber(String mobileNumeber) {
        this.mobileNumeber = mobileNumeber;
    }

    public String getDiagnosticId() {
        return diagnosticId;
    }

    public void setDiagnosticId(String diagnosticId) {
        this.diagnosticId = diagnosticId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public void setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLandLineNo() {
        return landLineNo;
    }

    public void setLandLineNo(String landLineNo) {
        this.landLineNo = landLineNo;
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

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public boolean isEmergencyservice() {
        return emergencyservice;
    }

    public void setEmergencyservice(boolean emergencyservice) {
        this.emergencyservice = emergencyservice;
    }
}
