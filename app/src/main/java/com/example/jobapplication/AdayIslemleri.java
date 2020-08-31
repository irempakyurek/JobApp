package com.example.jobapplication;

public interface AdayIslemleri {

    void adayGuncelle(String strId, String strAd, String strSoyad, String strEmail, String strCep, String strDogumTarihi,
                      String strMezunOkul, String strIngilizceSeviye, String adayEgitimSeviyesi, String strAciklama);
    void adaySil(String id);
}
