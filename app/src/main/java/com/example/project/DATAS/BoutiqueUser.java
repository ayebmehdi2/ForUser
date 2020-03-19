package com.example.project.DATAS;


public class BoutiqueUser {

    private String uid, name, photo, wilaya, des;

    public BoutiqueUser(String uid, String name, String photo, String wilaya, String des) {
        this.uid = uid;
        this.name = name;
        this.photo = photo;
        this.wilaya = wilaya;
        this.des = des;
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

    public void setWilaya(String wilaya) {
        this.wilaya = wilaya;
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

    public String getWilaya() {
        return wilaya;
    }

    public String getDes() {
        return des;
    }
}
