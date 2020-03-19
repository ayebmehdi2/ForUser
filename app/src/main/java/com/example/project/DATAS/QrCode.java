package com.example.project.DATAS;


public class QrCode {

    private  String code, username, dat;
    private Integer ustiliser;

    public QrCode(String code, String username, String dat, Integer ustiliser) {
        this.code = code;
        this.username = username;
        this.dat = dat;
        this.ustiliser = ustiliser;
    }

    public QrCode() {
    }

    public String getCode() {
        return code;
    }

    public String getUsername() {
        return username;
    }

    public String getDat() {
        return dat;
    }

    public Integer getUstiliser() {
        return ustiliser;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDat(String dat) {
        this.dat = dat;
    }

    public void setUstiliser(Integer ustiliser) {
        this.ustiliser = ustiliser;
    }
}
