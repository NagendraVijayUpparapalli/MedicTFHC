package com.example.cool.patient;

/**
 * Created by Udayasri on 26-04-2018.
 */

public class ApiBaseUrl {

    public ApiBaseUrl() {
    }

    public String getUrl() {
        return "https://meditfhc.com/mapi/";
    }

    public String getImageUrl() {
        return "https://meditfhc.com";
    }

    public String getLink() {
        return "https://meditfhc.com/";
    }

    public String getSmsUrl() {
        return "https://www.mgage.solutions/SendSMS/sendmsg.php?";
    }

    public String getEmailUrl() {
        return "https://ejson.mgage.solutions/sendEmail";
    }

    public String getTokenUrl()
    {
        return "https://meditfhc.com/token";
    }


}
