package com.example.cool.patient;

/**
 * Created by Udayasri on 23-05-2018.
 */

public class PatientMyDoctorAppointmentDetailsClass {

    String doctorId,appointmentID,UserId,mobileNumber;
    String appointmentDate,doctorName,prescription,timeslot,patientName,appointmentStatus,reason,doctorComment,paymentmode,amount,date;

    //parameterised constructor
    PatientMyDoctorAppointmentDetailsClass(String UserId,String mobileNumber,String appointmentID,String doctorId, String appointmentDate, String doctorName, String prescription, String timeslot, String patientName, String appointmentStatus, String reason, String doctorComment, String paymentmode, String amount, String date) {

        this.UserId = UserId;
        this.mobileNumber = mobileNumber;
        this.doctorId = doctorId;
        this.appointmentID = appointmentID;
        this.appointmentDate=appointmentDate;
        this.doctorName=doctorName;
        this.prescription=prescription;
        this.timeslot=timeslot;
        this.patientName=patientName;
        this.appointmentStatus=appointmentStatus;
        this.reason=reason;
        this.doctorComment=doctorComment;
        this.paymentmode=paymentmode;
        this.amount=amount;
        this.date=date;
    }

    //default constructor

    public PatientMyDoctorAppointmentDetailsClass() {

    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getPrescription() {
        return prescription;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public String getReason() {
        return reason;
    }

    public String getDoctorComment() {
        return doctorComment;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public void setTimeslot(String timeslot) {
        this.timeslot = timeslot;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setDoctorComment(String doctorComment) {
        this.doctorComment = doctorComment;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
