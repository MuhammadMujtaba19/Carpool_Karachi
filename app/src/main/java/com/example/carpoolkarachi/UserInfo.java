package com.example.carpoolkarachi;

import com.google.android.gms.maps.model.LatLng;

public class UserInfo {
    public String fullName;
    public String phone;
    public String sex;
//    private gender;
    public UserInfo(){

    }

    public UserInfo(String fullName, String phone, String sex){
        this.fullName = fullName;
        this.phone = phone;
        this.sex = sex;
    }
}

class RideInfo{
 String destination, source, date, time , rate;
 LatLng sourceLatLng,destLatLng;

    public RideInfo(String source, String destination, String date, String time, String rate) {
        this.destination = destination;
        this.source = source;
        this.date = date;
        this.time = time;
        this.rate = rate;
    }

    public RideInfo(String destination, String source, String date, String time, String rate, LatLng sourceLatLng, LatLng destLatLng) {
        this.destination = destination;
        this.source = source;
        this.date = date;
        this.time = time;
        this.rate = rate;
        this.sourceLatLng = sourceLatLng;
        this.destLatLng = destLatLng;
    }
}