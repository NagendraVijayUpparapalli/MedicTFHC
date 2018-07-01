package com.example.cool.patient.doctor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by Udayasri on 17-05-2018.
 */

public class DoctorSideNavigatioExpandableSubList {


    public static HashMap<String, List<String>> getData() {
        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<String, List<String>>();
        List<String> menu4 = new ArrayList<String>();
        List<String> Subscription = new ArrayList<String>();
        List<String> Aboutus = new ArrayList<String>();
        List<String> Contactus = new ArrayList<String>();
        List<String> list1 = new ArrayList<String>();
        list1.add("Appointment Calendar");
//        list1.add("My Patient");
        list1.add("Todays Appointments");
//        list1.add("Find Hospital");
//        list1.add("Blood Bank");
//        list1.add("Ambulance Services");

        List<String> list2 = new ArrayList<String>();
        list2.add("Add Address");
        list2.add("Manage Address");
//        list2.add("Medical store");
//        list2.add("Find Hospital");
//        list2.add("Blood Bank");
//        list2.add("Ambulance Services");

        List<String> company = new ArrayList<String>();
        company.add("Change password");
        company.add("Change mobileNumber");
        List<String> editProfile = new ArrayList<String>();
        expandableListDetail.put("Services", list1);
        expandableListDetail.put("Address", list2);
        expandableListDetail.put("Settings", company);
        expandableListDetail.put("EditProfile", editProfile);
        expandableListDetail.put("Subscription",Subscription);
        expandableListDetail.put("Offers", Aboutus);
        expandableListDetail.put("Contact us",Contactus);
        expandableListDetail.put("Logout", menu4);
        return expandableListDetail;

    }
}
