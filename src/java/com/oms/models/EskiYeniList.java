/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.models;

import java.util.List;

/**
 *
 * @author ersan
 */
public class EskiYeniList {
    
    private List<EskiYeni> eskiYeniListMail;
    private List<EskiYeni> eskiYeniListMailAdres;
    private List<EskiYeni> eskiYeniListTeklifSatirYanit;

    public List<EskiYeni> getEskiYeniListMail() {
        return eskiYeniListMail;
    }

    public void setEskiYeniListMail(List<EskiYeni> eskiYeniListMail) {
        this.eskiYeniListMail = eskiYeniListMail;
    }

    public List<EskiYeni> getEskiYeniListMailAdres() {
        return eskiYeniListMailAdres;
    }

    public void setEskiYeniListMailAdres(List<EskiYeni> eskiYeniListMailAdres) {
        this.eskiYeniListMailAdres = eskiYeniListMailAdres;
    }

    public List<EskiYeni> getEskiYeniListTeklifSatirYanit() {
        return eskiYeniListTeklifSatirYanit;
    }

    public void setEskiYeniListTeklifSatirYanit(List<EskiYeni> eskiYeniListTeklifSatirYanit) {
        this.eskiYeniListTeklifSatirYanit = eskiYeniListTeklifSatirYanit;
    }
    
    
}
