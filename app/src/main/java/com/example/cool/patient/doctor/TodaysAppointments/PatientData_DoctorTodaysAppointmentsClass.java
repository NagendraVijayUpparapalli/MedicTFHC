package com.example.cool.patient.doctor.TodaysAppointments;

/**
 * Created by Udayasri on 17-05-2018.
 */

public class PatientData_DoctorTodaysAppointmentsClass {

    int dstatus;
    String appointmentID,patientID,doctorId,docMobile,status1,patientName,emailID,mobileNo,prescription,comments,reasonAppointments,aadharNumber,timeSlots,age;

    public PatientData_DoctorTodaysAppointmentsClass(String doctorId,String docMobile,int dstatus, String status1, String appointmentID, String patientName, String emailID, String mobileNo, String patientID, String comments, String reasonAppointments,String timeSlots,String age) {

        this.doctorId = doctorId;
        this.docMobile = docMobile;
        this.dstatus=dstatus;
        this.status1=status1;
        this.appointmentID=appointmentID;
        this.patientName=patientName;
        this.emailID=emailID;
        this.mobileNo=mobileNo;
        //this.prescription=prescription;
        this.patientID=patientID;
        this.comments=comments;
        this.reasonAppointments=reasonAppointments;
//        this.aadharNumber=aadharNumber;
        this.timeSlots=timeSlots;
        this.age=age;

    }

    public String getDocMobile() {
        return docMobile;
    }

    public void setDocMobile(String docMobile) {
        this.docMobile = docMobile;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public int getDstatus() {
        return dstatus;
    }

    public String getStatus1() {
        return status1;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getEmailID() {
        return emailID;
    }

    public String getMobileNo() {
        return mobileNo;
    }

//    public String getPrescription() {
//        return prescription;
//    }

    public String getPatientID() {
        return patientID;
    }

    public String getComments() {
        return comments;
    }

    public String getReasonAppointments() {
        return reasonAppointments;
    }

//    public String getAadharNumber() {
//        return aadharNumber;
//    }

    public String getTimeSlots() {
        return timeSlots;
    }

    public String getAge() {
        return age;
    }

    public void setDstatus(int dstatus) {
        this.dstatus = dstatus;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

//    public void setPrescription(String prescription) {
//        this.prescription = prescription;
//    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setReasonAppointments(String reasonAppointments) {
        this.reasonAppointments = reasonAppointments;
    }

//    public void setAadharNumber(String aadharNumber) {
//        this.aadharNumber = aadharNumber;
//    }

    public void setTimeSlots(String timeSlots) {
        this.timeSlots = timeSlots;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
