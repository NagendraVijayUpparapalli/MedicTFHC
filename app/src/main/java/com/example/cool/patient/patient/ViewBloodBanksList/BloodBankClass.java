package com.example.cool.patient.patient.ViewBloodBanksList;

/**
 * Created by Udayasri on 22-03-2018.
 */

public class BloodBankClass {
    private String name;
    private String location;
    private String availability;
    private String distance;
    private String image;
    private String lati;
    private String longi;
    private String contact_person;
    private String mobile;


    public BloodBankClass()
    {}

//    public BloodBankClass(String name, String mobile, String location, String lat, String lng, String availability, String distance, String image) {
//        super();
//        this.name = name;
//        this.mobile = mobile;
//        this.location = location;
//        this.availability = availability;
//        this.distance = distance;
//        this.image = image;
//        this.lat = lat;
//        this.lng = lng;
//
//    }

    public BloodBankClass(String name,String mobile, String lati,String location,String longi, String distance, String image,String contact_person) {
        super();
        this.name = name;
        this.distance = distance;
        this.image = image;
        this.lati = lati;
        this.longi = longi;
        this.contact_person = contact_person;
        this.mobile = mobile;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact_person() {
        return contact_person;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLati() {
        return lati;
    }

    public void setLat(String lat) {
        this.lati = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLng(String lng) {
        this.longi = lng;
    }

//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
//
//    public String getMobile() {
//        return mobile;
//    }
//
//    public void setMobile(String mobile) {
//        this.mobile = mobile;
//    }
//
//    public String getAvailability() {
//        return availability;
//    }
//
//    public void setAvailability(String availability) {
//        this.availability = availability;
//    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
