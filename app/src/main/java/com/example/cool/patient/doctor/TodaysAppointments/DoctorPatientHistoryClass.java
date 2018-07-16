package com.example.cool.patient.doctor.TodaysAppointments;

public class DoctorPatientHistoryClass {

        String aaadharnumber,patientname,mobilenumber,reason,doctorname,speciality,appoinmentdate,doctorcomment,date,
                patientId,doctorId,doctorMobile,doctorImageUrl,prescription;

        public DoctorPatientHistoryClass(String patientId, String doctorId, String doctorMobile, String aaadharnumber,
                                         String patientname, String mobilenumber, String reason, String doctorname,
                                         String speciality, String appoinmentdate, String doctorcomment, String date,
                                         String doctorImageUrl,String prescription) {

            this.patientId=patientId;
            this.doctorId=doctorId;
            this.doctorMobile=doctorMobile;
            this.aaadharnumber=aaadharnumber;
            this.patientname=patientname;
            this.mobilenumber=mobilenumber;
            this.reason=reason;
            this.doctorname=doctorname;
            this.speciality=speciality;
            this.appoinmentdate=appoinmentdate;
            this.doctorcomment=doctorcomment;
            this.date=date;
            this.doctorImageUrl=doctorImageUrl;
            this.prescription = prescription;

        }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getAaadharnumber() {
            return aaadharnumber;
        }

        public void setAaadharnumber(String aaadharnumber) {
            this.aaadharnumber = aaadharnumber;
        }

        public String getPatientname() {
            return patientname;
        }

        public void setPatientname(String patientname) {
            this.patientname = patientname;
        }

        public String getMobilenumber() {
            return mobilenumber;
        }

        public void setMobilenumber(String mobilenumber) {
            this.mobilenumber = mobilenumber;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getDoctorname() {
            return doctorname;
        }

        public void setDoctorname(String doctorname) {
            this.doctorname = doctorname;
        }

        public String getSpeciality() {
            return speciality;
        }

        public void setSpeciality(String speciality) {
            this.speciality = speciality;
        }

        public String getAppoinmentdate() {
            return appoinmentdate;
        }

        public void setAppoinmentdate(String appoinmentdate) {
            this.appoinmentdate = appoinmentdate;
        }

        public String getDoctorcomment() {
            return doctorcomment;
        }

        public void setDoctorcomment(String doctorcomment) {
            this.doctorcomment = doctorcomment;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

        public String getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(String doctorId) {
            this.doctorId = doctorId;
        }

        public String getDoctorMobile() {
            return doctorMobile;
        }

        public void setDoctorMobile(String doctorMobile) {
            this.doctorMobile = doctorMobile;
        }

        public String getDoctorImageUrl() {
            return doctorImageUrl;
        }

        public void setDoctorImageUrl(String doctorImageUrl) {
            this.doctorImageUrl = doctorImageUrl;
        }
}



