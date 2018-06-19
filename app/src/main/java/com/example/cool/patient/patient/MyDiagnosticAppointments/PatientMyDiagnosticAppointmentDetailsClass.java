package com.example.cool.patient.patient.MyDiagnosticAppointments;

/**
 * Created by Udayasri on 03-06-2018.
 */

class PatientMyDiagnosticAppointmentDetailsClass {
    String appointmentID,DiagAddressId;
    String userId,mobileNumber,requestDate,patientName,centerName,testName,diagnosticsStatus,diagnosticReport,paymentmode,amount,comment,date;

    public PatientMyDiagnosticAppointmentDetailsClass(String DiagAddressId,String userId,String mobileNumber,String appointmentID, String requestDate, String patientName, String centerName, String testName, String diagnosticsStatus, String diagnosticReport, String paymentmode, String amount, String comment, String date) {

        this.DiagAddressId = DiagAddressId;
        this.mobileNumber = mobileNumber;
        this.userId = userId;
        this.appointmentID=appointmentID;
        this.requestDate=requestDate;
        this.patientName=patientName;
        this.centerName=centerName;
        this.testName=testName;
        this.diagnosticsStatus=diagnosticsStatus;
        this.diagnosticReport=diagnosticReport;
        this.paymentmode=paymentmode;
        this.amount=amount;
        this.comment=comment;
        this.date=date;
    }

    public String getDiagAddressId() {
        return DiagAddressId;
    }

    public void setDiagAddressId(String diagAddressId) {
        DiagAddressId = diagAddressId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getDiagnosticsStatus() {
        return diagnosticsStatus;
    }

    public void setDiagnosticsStatus(String diagnosticsStatus) {
        this.diagnosticsStatus = diagnosticsStatus;
    }

    public String getDiagnosticReport() {
        return diagnosticReport;
    }

    public void setDiagnosticReport(String diagnosticReport) {
        this.diagnosticReport = diagnosticReport;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
