package com.example.cool.patient.patient.ViewDoctorsList;

/**
 * Created by KIIT SEZ on 20-02-2018.
 */

class DoctorClass {


    public String doctorId,addressId,address,hospitalName,state,city,mobile,Name,qualification,specialityName,doctorImage,experience,
    latitude,longitude,emergencyService,consultationFee,consultationPrice,cashonHand,creditDebit,netBanking,paytm;
    public String patientId;

//    public DoctorClass()
//    {
//
//    }


    public DoctorClass() {
    }

//    public DoctorClass(String doctorId, String addressId, String address, String hospitalName, String state, String city, String mobile, String Name, String qualification, String specialityName, String doctorImage, String experience, String latitude, String longitude, String emergencyService, String consultationFee, String consultationPrice, String cashonHand, String creditDebit, String netBanking, String paytm) {

    public DoctorClass(String doctorId, String addressId, String patientId, String mobile, String Name, String qualification, String specialityName, String doctorImage, String experience, String latitude, String longitude, String emergencyService, String consultationFee, String consultationPrice, String cashonHand, String creditDebit, String netBanking, String paytm) {
        this.doctorId = doctorId;
        this.addressId = addressId;
        this.patientId = patientId;
//        this.address = address;
//        this.hospitalName = hospitalName;
//        this.state = state;
//        this.city = city;
        this.mobile = mobile;
        this.Name = Name;
        this.qualification = qualification;
        this.specialityName = specialityName;
        this.doctorImage = doctorImage;
        this.experience = experience;
        this.latitude = latitude;
        this.longitude = longitude;
        this.emergencyService = emergencyService;
        this.consultationFee = consultationFee;
        this.consultationPrice = consultationPrice;
        this.cashonHand = cashonHand;
        this.creditDebit = creditDebit;
        this.netBanking = netBanking;
        this.paytm = paytm;
    }

//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public String getHospitalName() {
//        return hospitalName;
//    }
//
//    public void setHospitalName(String hospitalName) {
//        this.hospitalName = hospitalName;
//    }
//
//    public String getState() {
//        return state;
//    }
//
//    public void setState(String state) {
//        this.state = state;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }


    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getConsultationPrice() {
        return consultationPrice;
    }

    public void setConsultationPrice(String consultationPrice) {
        this.consultationPrice = consultationPrice;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }

    public String getDoctorImage() {
        return doctorImage;
    }

    public void setDoctorImage(String doctorImage) {
        this.doctorImage = doctorImage;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
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

    public String getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(String consultationFee) {
        this.consultationFee = consultationFee;
    }

    public String getCashonHand() {
        return cashonHand;
    }

    public void setCashonHand(String cashonHand) {
        this.cashonHand = cashonHand;
    }

    public String getCreditDebit() {
        return creditDebit;
    }

    public void setCreditDebit(String creditDebit) {
        this.creditDebit = creditDebit;
    }

    public String getNetBanking() {
        return netBanking;
    }

    public void setNetBanking(String netBanking) {
        this.netBanking = netBanking;
    }

    public String getPaytm() {
        return paytm;
    }

    public void setPaytm(String paytm) {
        this.paytm = paytm;
    }
}
