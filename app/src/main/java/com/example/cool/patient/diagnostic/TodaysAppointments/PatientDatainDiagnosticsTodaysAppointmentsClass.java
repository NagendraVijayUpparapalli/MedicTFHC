package com.example.cool.patient.diagnostic.TodaysAppointments;

/**
 * Created by Udayasri on 29-05-2018.
 */

class PatientDatainDiagnosticsTodaysAppointmentsClass {

    int dstatus, appointmentID, rdTestID;
    String patientName,centerName, emailID, mobileNo,aadharnumebr,diagnosticId,diagMobile;

    PatientDatainDiagnosticsTodaysAppointmentsClass(String diagnosticId,String diagMobile,int dstatus, int rdTestID, int appointmentID, String patientName, String centerName, String emailID, String mobileNo, String aadharnumber) {

        this.diagMobile = diagMobile;
        this.diagnosticId = diagnosticId;
        this.dstatus=dstatus;
        this.rdTestID=rdTestID;
        this.appointmentID=appointmentID;
        this.patientName=patientName;
        this.centerName=centerName;
        this.emailID=emailID;
        this.mobileNo=mobileNo;
        this.aadharnumebr=aadharnumber;


    }

    public String getDiagnosticId() {
        return diagnosticId;
    }

    public String getDiagMobile() {
        return diagMobile;
    }

    public void setDiagMobile(String diagMobile) {
        this.diagMobile = diagMobile;
    }

    public void setDiagnosticId(String diagnosticId) {
        this.diagnosticId = diagnosticId;
    }

    public int getDstatus() {
        return dstatus;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public String getAadharnumebr() {
        return aadharnumebr;
    }

    public void setAadharnumebr(String aadharnumebr) {
        this.aadharnumebr = aadharnumebr;
    }

    public String getPatientName() {
        return patientName;
    }


    public String getCenterName() {
        return centerName;
    }

    public int getRdTestID() {
        return rdTestID;
    }

    public String getEmailID() {
        return emailID;
    }

    public String getMobileNo() {
        return mobileNo;
    }


    public void setDstatus(int dstatus) {
        this.dstatus = dstatus;
    }


    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }


    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public void setRdTestID(int rdTestID) {
        this.rdTestID = rdTestID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

}