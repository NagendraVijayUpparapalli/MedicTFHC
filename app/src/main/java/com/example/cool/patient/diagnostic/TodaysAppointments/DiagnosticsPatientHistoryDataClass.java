package com.example.cool.patient.diagnostic.TodaysAppointments;

/**
 * Created by Udayasri on 29-05-2018.
 */

class DiagnosticsPatientHistoryDataClass {
    String aadharNumber,patientName,mobileno,centername,testname,comments,date;
    int rdId;
    String patientid,diagId,diagMobile;

    public DiagnosticsPatientHistoryDataClass(String patientid,String diagId,String diagMobile,String aadharNumber, String patientName, String mobileno, String centername, String testname, String comments, String date, int rdId) {

        this.diagId = diagId;
        this.diagMobile = diagMobile;
        this.patientid = patientid;
        this.aadharNumber=aadharNumber;
        this.patientName=patientName;
        this.mobileno=mobileno;
        this.centername=centername;
        this.testname=testname;
        this.comments=comments;
        this.date=date;
    }

    public String getDiagId() {
        return diagId;
    }

    public void setDiagId(String diagId) {
        this.diagId = diagId;
    }

    public String getDiagMobile() {
        return diagMobile;
    }

    public void setDiagMobile(String diagMobile) {
        this.diagMobile = diagMobile;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getCentername() {
        return centername;
    }

    public void setCentername(String centername) {
        this.centername = centername;
    }

    public String getTestname() {
        return testname;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRdId() {
        return rdId;
    }

    public void setRdId(int rdId) {
        this.rdId = rdId;
    }


}
