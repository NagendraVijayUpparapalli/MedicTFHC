package com.example.cool.patient;

/**
 * Created by Udayasri on 07-05-2018.
 */

public class DoctorManageAddressClass {
    public String doctorId,addressId,address1,hospitalName,stateId,cityId,stateName,
            cityName,zipcode,landLineNo,contactPerson,latitude,longitude,comment,
            deleteReason,district,profileImage,emergencyContactNumber,consultationFee,registeredMobileNumber;
    public boolean emergencyservice;

    public DoctorManageAddressClass() {
    }

    public DoctorManageAddressClass(String doctorId, String addressId, String address1, String hospitalName, String stateId,
                                    String cityId, String stateName, String cityName, String zipcode, String landLineNo,
                                    String contactPerson, String latitude, String longitude, boolean emergencyservice,
                                    String comment, String deleteReason, String district,String profileImage,
                                    String emergencyContactNumber,String consultationFee,String registeredMobileNumber) {
        this.doctorId = doctorId;
        this.addressId = addressId;
        this.address1 = address1;
        this.hospitalName = hospitalName;
        this.stateId = stateId;
        this.cityId = cityId;
        this.stateName = stateName;
        this.cityName = cityName;
        this.zipcode = zipcode;
        this.landLineNo = landLineNo;
        this.contactPerson = contactPerson;
        this.latitude = latitude;
        this.longitude = longitude;
        this.emergencyservice = emergencyservice;
        this.comment = comment;
        this.deleteReason = deleteReason;
        this.district = district;
        this.profileImage = profileImage;
        this.emergencyContactNumber = emergencyContactNumber;
        this.consultationFee = consultationFee;
        this.registeredMobileNumber = registeredMobileNumber;
    }

    public String getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(String consultationFee) {
        this.consultationFee = consultationFee;
    }

    public String getRegisteredMobileNumber() {
        return registeredMobileNumber;
    }

    public void setRegisteredMobileNumber(String registeredMobileNumber) {
        this.registeredMobileNumber = registeredMobileNumber;
    }

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
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

    public boolean getEmergencyservice() {
        return emergencyservice;
    }

    public void setEmergencyservice(boolean emergencyservice) {
        this.emergencyservice = emergencyservice;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public void setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
    }
}
