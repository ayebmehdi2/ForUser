package com.example.project.DATAS;

public class GIFT {

    private String uid, username, type, status, qr_code, qr_img, time;

    public GIFT() {
    }

    public GIFT(String uid, String username , String type, String status, String qr_code, String qr_img, String time) {
        this.username = username;
        this.type = type;
        this.status = status;
        this.qr_code = qr_code;
        this.qr_img = qr_img;
        this.time = time;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }


    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public void setQr_img(String qr_img) {
        this.qr_img = qr_img;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getQr_code() {
        return qr_code;
    }

    public String getQr_img() {
        return qr_img;
    }
}
