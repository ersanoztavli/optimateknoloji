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
public class SonHareketler {
    
    private java.util.Date teklifGun;
    private int teklifAdeti;
    private double toplamTeklifMiktari;
    private double sipariseDonusmemis;
    private double sipariseDonusmus;
    
    public int getTeklifAdeti() {
        return teklifAdeti;
    }

    public void setTeklifAdeti(int teklifAdeti) {
        this.teklifAdeti = teklifAdeti;
    }


    public double getToplamTeklifMiktari() {
        return toplamTeklifMiktari;
    }

    public void setToplamTeklifMiktari(double toplamTeklifMiktari) {
        this.toplamTeklifMiktari = toplamTeklifMiktari;
    }

    public double getSipariseDonusmemis() {
        return sipariseDonusmemis;
    }

    public void setSipariseDonusmemis(double sipariseDonusmemis) {
        this.sipariseDonusmemis = sipariseDonusmemis;
    }

    public double getSipariseDonusmus() {
        return sipariseDonusmus;
    }

    public void setSipariseDonusmus(double sipariseDonusmus) {
        this.sipariseDonusmus = sipariseDonusmus;
    }

    public Date getTeklifGun() {
        return teklifGun;
    }

    public void setTeklifGun(Date teklifGun) {
        this.teklifGun = teklifGun;
    }
    
}
