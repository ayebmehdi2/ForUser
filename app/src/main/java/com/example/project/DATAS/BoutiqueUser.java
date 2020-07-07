package com.example.project.DATAS;

public class BoutiqueUser {

    private String uid, name, photo, des, lon, lat;

    public BoutiqueUser(String uid, String name, String photo, String des, String lat, String lon) {
        this.uid = uid;
        this.name = name;
        this.photo = photo;
        this.des = des;
        this.lat = lat;
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public BoutiqueUser() {
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public void setDes(String des) {
        this.des = des;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }


    public String getDes() {
        return des;
    }
}
