/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.models;

import java.util.Date;

/**
 *
 * @author ersan
 */
public class SonTeklifVeSatinAlmaSatisSatiri {
    
    private java.util.Date sonSatinAlmaTeklifTarihi;
    private double sonSatinAlmaTeklifFiyati;
    private String sonSatinAlmaParaBirimi;
    private String sonTeklifParaBirimi;
    private java.util.Date sonSatinAlmaTarihi;
    private double sonSatinAlmaFiyati;
    private int teklifSatirId;
    private String sonSatinAlmaCari;
    private String sonTeklifCari;

    public Date getSonSatinAlmaTeklifTarihi() {
        return sonSatinAlmaTeklifTarihi;
    }

    public void setSonSatinAlmaTeklifTarihi(Date sonSatinAlmaTeklifTarihi) {
        this.sonSatinAlmaTeklifTarihi = sonSatinAlmaTeklifTarihi;
    }

    public double getSonSatinAlmaTeklifFiyati() {
        return sonSatinAlmaTeklifFiyati;
    }

    public void setSonSatinAlmaTeklifFiyati(double sonSatinAlmaTeklifFiyati) {
        this.sonSatinAlmaTeklifFiyati = sonSatinAlmaTeklifFiyati;
    }

    public Date getSonSatinAlmaTarihi() {
        return sonSatinAlmaTarihi;
    }

    public void setSonSatinAlmaTarihi(Date sonSatinAlmaTarihi) {
        this.sonSatinAlmaTarihi = sonSatinAlmaTarihi;
    }

    public double getSonSatinAlmaFiyati() {
        return sonSatinAlmaFiyati;
    }

    public void setSonSatinAlmaFiyati(double sonSatinAlmaFiyati) {
        this.sonSatinAlmaFiyati = sonSatinAlmaFiyati;
    }

    public int getTeklifSatirId() {
        return teklifSatirId;
    }

    public void setTeklifSatirId(int teklifSatirId) {
        this.teklifSatirId = teklifSatirId;
    }

    public String getSonSatinAlmaCari() {
        return sonSatinAlmaCari;
    }

    public void setSonSatinAlmaCari(String sonSatinAlmaCari) {
        this.sonSatinAlmaCari = sonSatinAlmaCari;
    }

    public String getSonTeklifCari() {
        return sonTeklifCari;
    }

    public void setSonTeklifCari(String sonTeklifCari) {
        this.sonTeklifCari = sonTeklifCari;
    }    

    public String getSonSatinAlmaParaBirimi() {
        return sonSatinAlmaParaBirimi;
    }

    public void setSonSatinAlmaParaBirimi(String sonSatinAlmaParaBirimi) {
        this.sonSatinAlmaParaBirimi = sonSatinAlmaParaBirimi;
    }

    public String getSonTeklifParaBirimi() {
        return sonTeklifParaBirimi;
    }

    public void setSonTeklifParaBirimi(String sonTeklifParaBirimi) {
        this.sonTeklifParaBirimi = sonTeklifParaBirimi;
    }    
    
}
