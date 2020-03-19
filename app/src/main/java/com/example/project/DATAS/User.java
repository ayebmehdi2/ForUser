package com.example.project.DATAS;

public class User {

    private String Uid, name, prenom, email, photoUrl, tele, dateBirth, location;
    private Integer score;
    private Integer prod1;
    private Integer prod2;
    private Integer prod3;
    private Integer prod4;
    private Integer prod5;
    private Integer gifts;
    private Integer gift1;
    private Integer gift2;
    private Integer gift3;
    private String dateInsc;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public void setProd1(Integer prod1) {
        this.prod1 = prod1;
    }

    public void setProd2(Integer prod2) {
        this.prod2 = prod2;
    }

    public void setProd3(Integer prod3) {
        this.prod3 = prod3;
    }

    public void setProd4(Integer prod4) {
        this.prod4 = prod4;
    }

    public void setProd5(Integer prod5) {
        this.prod5 = prod5;
    }

    public void setGift1(Integer gift1) {
        this.gift1 = gift1;
    }

    public void setGift2(Integer gift2) {
        this.gift2 = gift2;
    }

    public void setGift3(Integer gift3) {
        this.gift3 = gift3;
    }

    public Integer getProd1() {
        return prod1;
    }

    public Integer getProd2() {
        return prod2;
    }

    public Integer getProd3() {
        return prod3;
    }

    public Integer getProd4() {
        return prod4;
    }

    public Integer getProd5() {
        return prod5;
    }

    public Integer getGift1() {
        return gift1;
    }

    public Integer getGift2() {
        return gift2;
    }

    public Integer getGift3() {
        return gift3;
    }

    public User(String uid, String name, String pre, String email, String photoUrl, String tele,
                String dateBirth, String dateInsc, Integer score, String location, Integer prod1,
                Integer prod2, Integer prod3, Integer prod4, Integer prod5, Integer gifts,
                Integer gift1, Integer gift2, Integer gift3) {
        Uid = uid;
        this.name = name;
        prenom = pre;
        this.email = email;
        this.photoUrl = photoUrl;
        this.tele = tele;
        this.dateBirth = dateBirth;
        this.dateInsc = dateInsc;
        this.score = score;
        this.location = location;
        this.prod1 = prod1;
        this.prod2 = prod2;
        this.prod3 = prod3;
        this.prod4 = prod4;
        this.prod5 = prod5;
        this.gifts = gifts;
        this.gift1 = gift1;
        this.gift2 = gift2;
        this.gift3 = gift3;
    }


    public User() {
    }

    public Integer getGifts() {
        return gifts;
    }

    public void setGifts(Integer gifts) {
        this.gifts = gifts;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public void setDateInsc(String dateInsc) {
        this.dateInsc = dateInsc;
    }

    public String getTele() {
        return tele;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public String getDateInsc() {
        return dateInsc;
    }

    public Integer getScore() {
        return score;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getUid() {
        return Uid;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }


    public void setUid(String uid) {
        Uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
