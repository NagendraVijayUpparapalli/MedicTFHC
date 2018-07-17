package com.example.cool.patient.diagnostic.DashBoardCalendar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Udayasri on 29-05-2018.
 */

public class MyPatientDataClassInDiagnostics {

    int dstatus,rdTestID;
    String diagnosticId,diagmobile,addressId,payment,patientName,comments,centerName,emailID,mobileNo,prescription,
            amount,aadharnumber,status,appointmentDate;
    ArrayList<String> speciality;

    public MyPatientDataClassInDiagnostics(String diagnosticId,String diagmobile,String addressId,int dstatus,
                                           String payment, int rdTestID, String patientName, String comments,
                                           String centerName, String emailID, String mobileNo, String prescription,
                                           String amount, String aadharnumber, ArrayList<String> speciality, String status,
                                           String appointmentDate) {

        this.diagnosticId = diagnosticId;
        this.diagmobile = diagmobile;
        this.addressId = addressId;
        this.dstatus=dstatus;
        this.payment=payment;
        this.rdTestID=rdTestID;
        this.patientName=patientName;
        this.comments=comments;
        this.centerName=centerName;
        this.emailID=emailID;
        this.mobileNo=mobileNo;
        this.prescription=prescription;
        this.amount=amount;
        this.aadharnumber=aadharnumber;
        this.status=status;
        this.appointmentDate = appointmentDate;
        this.speciality=speciality;

    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getDiagmobile() {
        return diagmobile;
    }

    public void setDiagmobile(String diagmobile) {
        this.diagmobile = diagmobile;
    }

    public String getDiagnosticId() {
        return diagnosticId;
    }

    public void setDiagnosticId(String diagnosticId) {
        this.diagnosticId = diagnosticId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public int getDstatus() {
        return dstatus;
    }

    public void setDstatus(int dstatus) {
        this.dstatus = dstatus;
    }

    public int getRdTestID() {
        return rdTestID;
    }

    public void setRdTestID(int rdTestID) {
        this.rdTestID = rdTestID;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAadharnumber() {
        return aadharnumber;
    }

    public void setAadharnumber(String aadharnumber) {
        this.aadharnumber = aadharnumber;
    }

    public ArrayList<String> getSpeciality() {
        return speciality;
    }

    public void setSpeciality(ArrayList<String> speciality) {
        this.speciality = speciality;
    }

}
