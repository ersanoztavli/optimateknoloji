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
public class EMAIL_AYAR {
    
    private int EMAIL_AYAR_ID;
    private int OLUSTURAN_ID;
    private java.util.Date OLUSTURMA_TARIHI;
    private int KULLANIM_DURUMU;
    private int GUNCELLEYEN_ID;
    private java.util.Date GUNCELLEME_TARIHI;
    private String EMAIL_SUNUCU_ADI;
    private String EMAIL_SUNUCU_PORT_NUMARASI;
    private String EMAIL_ADRES;
    private String EMAIL_SIFRE;

    public int getEMAIL_AYAR_ID() {
        return EMAIL_AYAR_ID;
    }

    public void setEMAIL_AYAR_ID(int EMAIL_AYAR_ID) {
        this.EMAIL_AYAR_ID = EMAIL_AYAR_ID;
    }

    public int getOLUSTURAN_ID() {
        return OLUSTURAN_ID;
    }

    public void setOLUSTURAN_ID(int OLUSTURAN_ID) {
        this.OLUSTURAN_ID = OLUSTURAN_ID;
    }

    public Date getOLUSTURMA_TARIHI() {
        return OLUSTURMA_TARIHI;
    }

    public void setOLUSTURMA_TARIHI(Date OLUSTURMA_TARIHI) {
        this.OLUSTURMA_TARIHI = OLUSTURMA_TARIHI;
    }

    public int getKULLANIM_DURUMU() {
        return KULLANIM_DURUMU;
    }

    public void setKULLANIM_DURUMU(int KULLANIM_DURUMU) {
        this.KULLANIM_DURUMU = KULLANIM_DURUMU;
    }

    public int getGUNCELLEYEN_ID() {
        return GUNCELLEYEN_ID;
    }

    public void setGUNCELLEYEN_ID(int GUNCELLEYEN_ID) {
        this.GUNCELLEYEN_ID = GUNCELLEYEN_ID;
    }

    public Date getGUNCELLEME_TARIHI() {
        return GUNCELLEME_TARIHI;
    }

    public void setGUNCELLEME_TARIHI(Date GUNCELLEME_TARIHI) {
        this.GUNCELLEME_TARIHI = GUNCELLEME_TARIHI;
    }

    public String getEMAIL_SUNUCU_ADI() {
        return EMAIL_SUNUCU_ADI;
    }

    public void setEMAIL_SUNUCU_ADI(String EMAIL_SUNUCU_ADI) {
        this.EMAIL_SUNUCU_ADI = EMAIL_SUNUCU_ADI;
    }

    public String getEMAIL_SUNUCU_PORT_NUMARASI() {
        return EMAIL_SUNUCU_PORT_NUMARASI;
    }

    public void setEMAIL_SUNUCU_PORT_NUMARASI(String EMAIL_SUNUCU_PORT_NUMARASI) {
        this.EMAIL_SUNUCU_PORT_NUMARASI = EMAIL_SUNUCU_PORT_NUMARASI;
    }

    public String getEMAIL_ADRES() {
        return EMAIL_ADRES;
    }

    public void setEMAIL_ADRES(String EMAIL_ADRES) {
        this.EMAIL_ADRES = EMAIL_ADRES;
    }

    public String getEMAIL_SIFRE() {
        return EMAIL_SIFRE;
    }

    public void setEMAIL_SIFRE(String EMAIL_SIFRE) {
        this.EMAIL_SIFRE = EMAIL_SIFRE;
    }
    
    
}
