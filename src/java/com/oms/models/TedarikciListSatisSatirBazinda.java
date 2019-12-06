/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.models;

/**
 * Satış faturalarındaki herbir satırın
 * ilgili tedarikçilerini listeleyebilmek sanal model oluşturulmuştur...
 * @author ersan
 */
public class TedarikciListSatisSatirBazinda {
    
    private int teklifSatirId;
    private String musteriKodu;
    private String musteriRecordId;
    private String musteriUnvani;
    private String musteriKoduVeUnvani;//Kodu ve adını birlikte gösterebilmek için
    private int secildiMi;
    private int oncelik;

    public String getMusteriKodu() {
        return musteriKodu;
    }

    public void setMusteriKodu(String musteriKodu) {
        this.musteriKodu = musteriKodu;
    }

    public String getMusteriRecordId() {
        return musteriRecordId;
    }

    public void setMusteriRecordId(String musteriRecordId) {
        this.musteriRecordId = musteriRecordId;
    }

    public String getMusteriUnvani() {
        return musteriUnvani;
    }

    public void setMusteriUnvani(String musteriUnvani) {
        this.musteriUnvani = musteriUnvani;
    }

    public int getSecildiMi() {
        return secildiMi;
    }

    public void setSecildiMi(int secildiMi) {
        this.secildiMi = secildiMi;
    }

    public String getMusteriKoduVeUnvani() {
        return musteriKoduVeUnvani;
    }

    public void setMusteriKoduVeUnvani(String musteriKoduVeUnvani) {
        this.musteriKoduVeUnvani = musteriKoduVeUnvani;
    }

    public int getTeklifSatirId() {
        return teklifSatirId;
    }

    public void setTeklifSatirId(int teklifSatirId) {
        this.teklifSatirId = teklifSatirId;
    }

    public int getOncelik() {
        return oncelik;
    }

    public void setOncelik(int oncelik) {
        this.oncelik = oncelik;
    }
}
