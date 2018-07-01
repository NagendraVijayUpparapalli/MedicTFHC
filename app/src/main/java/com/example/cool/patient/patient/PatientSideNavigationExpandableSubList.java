package com.example.cool.patient.patient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Udayasri on 16-05-2018.
 */

public class PatientSideNavigationExpandableSubList {

    public static HashMap<String, List<String>> getData() {
        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<String, List<String>>();
        List<String> menu4 = new ArrayList<String>();
        List<String> Aboutus = new ArrayList<String>();
        List<String> Contactus = new ArrayList<String>();
        List<String> EditProfile = new ArrayList<String>();
        List<String> list1 = new ArrayList<String>();
        list1.add("Doctor Appointment");
        list1.add("Diagnostic center");
        list1.add("Medical store");
        list1.add("Find Hospitals");
        list1.add("Blood Bank");
        list1.add("Ambulance Services");

        List<String> list2 = new ArrayList<String>();
        list2.add("My Doctor Appointment");
        list2.add("My Diagnostic center");
        list2.add("My Medical store");
        list2.add("Find Hospital");
        list2.add("Blood Bank");
        list2.add("Ambulance Services");

        List<String> company = new ArrayList<String>();
        company.add("Change password");
        company.add("Change mobileNumber");

        expandableListDetail.put("Services", list1);
        expandableListDetail.put("History", list2);
        expandableListDetail.put("Settings", company);
        expandableListDetail.put("Edit Profile", EditProfile);

        expandableListDetail.put("Offers", Aboutus);
        expandableListDetail.put("Contact us",Contactus);
        expandableListDetail.put("Logout", menu4);
        return expandableListDetail;

    }
}

