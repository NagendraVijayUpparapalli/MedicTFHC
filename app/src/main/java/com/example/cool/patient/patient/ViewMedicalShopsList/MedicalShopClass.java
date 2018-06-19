package com.example.cool.patient.patient.ViewMedicalShopsList;

/**
 * Created by Udayasri on 01-06-2018.
 */

class MedicalShopClass {

    public String ContactPerson,ShopName,MedicalID,addressId,address,hospitalName,state,city,mobile,Name,qualification,specialityName,medicImage,experience,
            latitude,longitude,emergencyService,consultationFee,consultationPrice,cashonHand,creditDebit,netBanking,paytm,LandlineNo;
    public String patientId,usermobileNumber;

    public MedicalShopClass( String MedicalID,String addressId,String patientId,String usermobileNumber,String mobile,String ShopName,String ContactPerson ,String LandlineNo
            , String medicImage, String latitude, String longitude, String emergencyService,
                         String cashonHand, String creditDebit, String netBanking, String paytm) {

        this.MedicalID = MedicalID;
        this.ShopName = ShopName;
        this.addressId = addressId;
        this.patientId = patientId;
        this.ContactPerson = ContactPerson;
        this.LandlineNo = LandlineNo;
        this.usermobileNumber = usermobileNumber;
        this.mobile = mobile;
        this.medicImage = medicImage;
        this.latitude = latitude;
        this.longitude = longitude;
        this.emergencyService = emergencyService;
        this.cashonHand = cashonHand;
        this.creditDebit = creditDebit;
        this.netBanking = netBanking;
        this.paytm = paytm;
    }

    public String getUsermobileNumber() {
        return usermobileNumber;
    }

    public void setUsermobileNumber(String usermobileNumber) {
        this.usermobileNumber = usermobileNumber;
    }

    public String getMedicImage() {
        return medicImage;
    }

    public void setMedicImage(String medicImage) {
        this.medicImage = medicImage;
    }

    public String getLandlineNo() {
        return LandlineNo;
    }

    public void setLandlineNo(String landlineNo) {
        LandlineNo = landlineNo;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        ContactPerson = contactPerson;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return MedicalID;
    }

    public String getConsultationPrice() {
        return consultationPrice;
    }

    public void setConsultationPrice(String consultationPrice) {
        this.consultationPrice = consultationPrice;
    }

    public void setDoctorId(String doctorId) {
        this.MedicalID = doctorId;
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
