package com.example.jobapplication;

public class Aday {

    String ad, soyad, email, cepTel, dogumTarihi, egitimSeviyesi, ingilizceSeviyesi, okul, aciklama;

    public Aday(){

    }

    public Aday(String ad, String soyad, String email, String cepTel, String dogumTarihi, String okul, String egitimSeviyesi, String ingilizceSeviyesi, String aciklama) {

        this.ad = ad;
        this.soyad = soyad;
        this.email = email;
        this.cepTel = cepTel;
        this.dogumTarihi = dogumTarihi;
        this.okul = okul;
        this.egitimSeviyesi = egitimSeviyesi;
        this.ingilizceSeviyesi = ingilizceSeviyesi;
        this.aciklama = aciklama;
    }


    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getCepTel() {
        return cepTel;
    }

    public void setCepTel(String cepTel) {
        this.cepTel = cepTel;
    }

    public String getDogumTarihi() {
        return dogumTarihi;
    }

    public void setDogumTarihi(String dogumTarihi) {
        this.dogumTarihi = dogumTarihi;
    }

    public String getOkul() {
        return okul;
    }

    public void setOkul(String okul) {
        this.okul = okul;
    }

    public String getEgitimSeviyesi() {
        return egitimSeviyesi;
    }

    public void setEgitimSeviyesi(String egitimSeviyesi) {
        this.egitimSeviyesi = egitimSeviyesi;
    }

    public String getIngilizceSeviyesi() {
        return ingilizceSeviyesi;
    }

    public void setIngilizceSeviyesi(String ingilizceSeviyesi) {
        this.ingilizceSeviyesi = ingilizceSeviyesi;
    }


    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }
}
