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
public class SonDetayliHareketler {
    
    private String teklifNo;
    private java.util.Date teklifTarihi;
    private String teklifCariKod;
    private String teklifCariUnvan;
    private double teklifMiktari;
    private double siparisMiktari;
    private double teklifTutari;
    private String doviz;
    private String siparisDurumu;

    public String getTeklifNo() {
        return teklifNo;
    }

    public void setTeklifNo(String teklifNo) {
        this.teklifNo = teklifNo;
    }

    public Date getTeklifTarihi() {
        return teklifTarihi;
    }

    public void setTeklifTarihi(Date teklifTarihi) {
        this.teklifTarihi = teklifTarihi;
    }

    public String getTeklifCariKod() {
        return teklifCariKod;
    }

    public void setTeklifCariKod(String teklifCariKod) {
        this.teklifCariKod = teklifCariKod;
    }

    public String getTeklifCariUnvan() {
        return teklifCariUnvan;
    }

    public void setTeklifCariUnvan(String teklifCariUnvan) {
        this.teklifCariUnvan = teklifCariUnvan;
    }

    public double getTeklifMiktari() {
        return teklifMiktari;
    }

    public void setTeklifMiktari(double teklifMiktari) {
        this.teklifMiktari = teklifMiktari;
    }

    public double getSiparisMiktari() {
        return siparisMiktari;
    }

    public void setSiparisMiktari(double siparisMiktari) {
        this.siparisMiktari = siparisMiktari;
    }

    public double getTeklifTutari() {
        return teklifTutari;
    }

    public void setTeklifTutari(double teklifTutari) {
        this.teklifTutari = teklifTutari;
    }

    public String getDoviz() {
        return doviz;
    }

    public void setDoviz(String doviz) {
        this.doviz = doviz;
    }

    public String getSiparisDurumu() {
        return siparisDurumu;
    }

    public void setSiparisDurumu(String siparisDurumu) {
        this.siparisDurumu = siparisDurumu;
    }
    
    
    
}
