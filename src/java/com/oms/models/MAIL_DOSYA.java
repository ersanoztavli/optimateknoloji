/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.models;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

/**
 *
 * @author ersan
 */
public class MAIL_DOSYA {
    private int MAIL_DOSYA_ID;
    private int OLUSTURAN_ID;
    private java.util.Date OLUSTURMA_TARIHI;
    private int KULLANIM_DURUMU;
    private int GUNCELLEYEN_ID;
    private java.util.Date GUNCELLEME_TARIHI;
    private String MAIL_DOSYA_ADI;
    private byte[] MAIL_DOSYA_ICERIGI;
    private String MAIL_DOSYA_HASH_DEGERI;
    private int MAIL_ID;
    
    //Sanal Kolon
    private String mailDosyaAdiUzun;
    //Sanal Kolon
    private InputStream icerik;
    //Sanal Kolon
    private String boyut;

    public int getMAIL_DOSYA_ID() {
        return MAIL_DOSYA_ID;
    }

    public void setMAIL_DOSYA_ID(int MAIL_DOSYA_ID) {
        this.MAIL_DOSYA_ID = MAIL_DOSYA_ID;
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

    public String getMAIL_DOSYA_ADI() {
        return MAIL_DOSYA_ADI;
    }

    public void setMAIL_DOSYA_ADI(String MAIL_DOSYA_ADI) {
        this.MAIL_DOSYA_ADI = MAIL_DOSYA_ADI;
    }

    public byte[] getMAIL_DOSYA_ICERIGI() {
        return MAIL_DOSYA_ICERIGI;
    }

    public void setMAIL_DOSYA_ICERIGI(byte[] MAIL_DOSYA_ICERIGI) {
        this.MAIL_DOSYA_ICERIGI = MAIL_DOSYA_ICERIGI;
    }

    public String getMAIL_DOSYA_HASH_DEGERI() {
        return MAIL_DOSYA_HASH_DEGERI;
    }

    public void setMAIL_DOSYA_HASH_DEGERI(String MAIL_DOSYA_HASH_DEGERI) {
        this.MAIL_DOSYA_HASH_DEGERI = MAIL_DOSYA_HASH_DEGERI;
    }

    public int getMAIL_ID() {
        return MAIL_ID;
    }

    public void setMAIL_ID(int MAIL_ID) {
        this.MAIL_ID = MAIL_ID;
    }

    public String getMailDosyaAdiUzun() {
        return mailDosyaAdiUzun;
    }

    public void setMailDosyaAdiUzun(String mailDosyaAdiUzun) {
        this.mailDosyaAdiUzun = mailDosyaAdiUzun;
    }    

    public InputStream getIcerik() {
        return icerik;
    }

    public void setIcerik(InputStream icerik) {
        this.icerik = icerik;
    }

    public String getBoyut() {
        return boyut;
    }

    public void setBoyut(String boyut) {
        this.boyut = boyut;
    }
}
