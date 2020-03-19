package com.example.project.DATAS;

public class Statistique {

    private Integer comptes,   gift1, gift2, gift3, produit1, produit2, produit3, produit4, produit5;

    public Statistique(){}


    private Statistique(Integer c, Integer g1, Integer g2, Integer g3,
                        Integer p1, Integer p2, Integer p3, Integer p4, Integer p5){
        comptes = c;

        produit1 = p1;
        produit2 = p2;
        produit3 = p3;
        gift1 = g1;
        gift2 = g2;
        gift3 = g3;
        produit4 = p4;
        produit5 = p5;
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

    public Integer getProduit4() {
        return produit4;
    }

    public Integer getProduit5() {
        return produit5;
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

    public void setProduit4(Integer produit4) {
        this.produit4 = produit4;
    }

    public void setProduit5(Integer produit5) {
        this.produit5 = produit5;
    }


    public Integer getComptes() {
        return comptes;
    }

    public Integer getProduit1() {
        return produit1;
    }

    public Integer getProduit2() {
        return produit2;
    }

    public Integer getProduit3() {
        return produit3;
    }

    public void setComptes(Integer comptes) {
        this.comptes = comptes;
    }


    public void setProduit1(Integer produit1) {
        this.produit1 = produit1;
    }

    public void setProduit2(Integer produit2) {
        this.produit2 = produit2;
    }

    public void setProduit3(Integer produit3) {
        this.produit3 = produit3;
    }


}
