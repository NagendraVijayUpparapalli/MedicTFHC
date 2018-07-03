package com.example.cool.patient.doctor.DashBoardCalendar;

/**
 * Created by gayathri on 7/5/18.
 */

class MyPatientDataClassInDoctor {
    int dstatus;

    String  appointmentID,patientID,DoctorId,doctorMobile,DoctorAddressId,status1,patientName,emailID,mobileNo,prescription,comments,
            reasonAppointments,aadharNumber,timeSlots,appointmentDate,Amount,PaymentMode;

    public MyPatientDataClassInDoctor(String DoctorId,String doctorMobile,String DoctorAddressId,int dstatus,
                                      String status1, String appointmentID, String patientName, String emailID,
                                      String mobileNo, String prescription,String Amount,String PaymentMode, String patientID, String comments,
                                      String reasonAppointments, String aadharNumber, String timeSlots,String appointmentDate) {

              this.doctorMobile = doctorMobile;
              this.DoctorAddressId = DoctorAddressId;
              this.appointmentDate = appointmentDate;
              this.DoctorId = DoctorId;
              this.dstatus=dstatus;
              this.status1=status1;
              this.appointmentID=appointmentID;
              this.patientName=patientName;
              this.emailID=emailID;
              this.mobileNo=mobileNo;
              this.prescription=prescription;
              this.Amount = Amount;
              this.PaymentMode = PaymentMode;
              this.patientID=patientID;
              this.comments=comments;
              this.reasonAppointments=reasonAppointments;
              this.aadharNumber=aadharNumber;
              this.timeSlots=timeSlots;

    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getDoctorMobile() {
        return doctorMobile;
    }

    public void setDoctorMobile(String doctorMobile) {
        this.doctorMobile = doctorMobile;
    }

    public String getDoctorAddressId() {
        return DoctorAddressId;
    }

    public void setDoctorAddressId(String doctorAddressId) {
        DoctorAddressId = doctorAddressId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(String doctorId) {
        DoctorId = doctorId;
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

    public String getPrescription() {
        return prescription;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getComments() {
        return comments;
    }

    public String getReasonAppointments() {
        return reasonAppointments;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public String getTimeSlots() {
        return timeSlots;
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

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setReasonAppointments(String reasonAppointments) {
        this.reasonAppointments = reasonAppointments;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public void setTimeSlots(String timeSlots) {
        this.timeSlots = timeSlots;
    }
}
