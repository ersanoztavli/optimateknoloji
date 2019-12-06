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
public class KULLANICI {
    
    private int KULLANICI_ID;
    private int OLUSTURAN_ID;
    private java.util.Date OLUSTURMA_TARIHI;
    private int KULLANIM_DURUMU;
    private int GUNCELLEYEN_ID;
    private java.util.Date GUNCELLEME_TARIHI;
    private String KULLANICI_ADI;
    private String SIFRE;
    private String AD;
    private String SOYAD;
    private int ON_DEGER_FIRMA_ID;
    private java.util.Date SON_GIRIS_ZAMANI;
    private int BASARISIZ_DENEME_SAYISI;
    private String ERP_ON_DEGER_FIRMA_NUMBER;    
    private String MAIL_ADRESI;
    private int AKTIF_PASIF_TUTAR;
    
    //Sanal Kolon
    private boolean aktifPasifTutarBoolean;
    
    //Sanal Kolon
    private String ON_DEGER_FIRMA_UNVAN;    
    //Sanal Kolon
    private FIRMA ON_DEGER_FIRMA;
    //Sanal Kolon
    private String ERP_ON_DEGER_FIRMA_UNVAN;
    //Sanal Kolon
    private ERP_FIRMA ERP_ON_DEGER_FIRMA;
    //Sanal Kolon
    private String firmaMailAdresi;
    
    public int getKULLANICI_ID() {
        return KULLANICI_ID;
    }

    public void setKULLANICI_ID(int KULLANICI_ID) {
        this.KULLANICI_ID = KULLANICI_ID;
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

    public String getKULLANICI_ADI() {
        return KULLANICI_ADI;
    }

    public void setKULLANICI_ADI(String KULLANICI_ADI) {
        this.KULLANICI_ADI = KULLANICI_ADI;
    }

    public String getSIFRE() {
        return SIFRE;
    }

    public void setSIFRE(String SIFRE) {
        this.SIFRE = SIFRE;
    }

    public String getAD() {
        return AD;
    }

    public void setAD(String AD) {
        this.AD = AD;
    }

    public String getSOYAD() {
        return SOYAD;
    }

    public void setSOYAD(String SOYAD) {
        this.SOYAD = SOYAD;
    }

    public int getON_DEGER_FIRMA_ID() {
        return ON_DEGER_FIRMA_ID;
    }

    public void setON_DEGER_FIRMA_ID(int ON_DEGER_FIRMA_ID) {
        this.ON_DEGER_FIRMA_ID = ON_DEGER_FIRMA_ID;
    }

    public Date getSON_GIRIS_ZAMANI() {
        return SON_GIRIS_ZAMANI;
    }

    public void setSON_GIRIS_ZAMANI(Date SON_GIRIS_ZAMANI) {
        this.SON_GIRIS_ZAMANI = SON_GIRIS_ZAMANI;
    }

    public int getBASARISIZ_DENEME_SAYISI() {
        return BASARISIZ_DENEME_SAYISI;
    }

    public void setBASARISIZ_DENEME_SAYISI(int BASARISIZ_DENEME_SAYISI) {
        this.BASARISIZ_DENEME_SAYISI = BASARISIZ_DENEME_SAYISI;
    }

    public String getON_DEGER_FIRMA_UNVAN() {
        return ON_DEGER_FIRMA_UNVAN;
    }

    public void setON_DEGER_FIRMA_UNVAN(String ON_DEGER_FIRMA_UNVAN) {
        this.ON_DEGER_FIRMA_UNVAN = ON_DEGER_FIRMA_UNVAN;
    }

    public FIRMA getON_DEGER_FIRMA() {
        return ON_DEGER_FIRMA;
    }

    public void setON_DEGER_FIRMA(FIRMA ON_DEGER_FIRMA) {
        this.ON_DEGER_FIRMA = ON_DEGER_FIRMA;
    }

    public String getERP_ON_DEGER_FIRMA_NUMBER() {
        return ERP_ON_DEGER_FIRMA_NUMBER;
    }

    public void setERP_ON_DEGER_FIRMA_NUMBER(String ERP_ON_DEGER_FIRMA_NUMBER) {
        this.ERP_ON_DEGER_FIRMA_NUMBER = ERP_ON_DEGER_FIRMA_NUMBER;
    }

    public String getERP_ON_DEGER_FIRMA_UNVAN() {
        return ERP_ON_DEGER_FIRMA_UNVAN;
    }

    public void setERP_ON_DEGER_FIRMA_UNVAN(String ERP_ON_DEGER_FIRMA_UNVAN) {
        this.ERP_ON_DEGER_FIRMA_UNVAN = ERP_ON_DEGER_FIRMA_UNVAN;
    }

    public ERP_FIRMA getERP_ON_DEGER_FIRMA() {
        return ERP_ON_DEGER_FIRMA;
    }

    public void setERP_ON_DEGER_FIRMA(ERP_FIRMA ERP_ON_DEGER_FIRMA) {
        this.ERP_ON_DEGER_FIRMA = ERP_ON_DEGER_FIRMA;
    }

    public String getFirmaMailAdresi() {
        return firmaMailAdresi;
    }

    public void setFirmaMailAdresi(String firmaMailAdresi) {
        this.firmaMailAdresi = firmaMailAdresi;
    }

    public String getMAIL_ADRESI() {
        return MAIL_ADRESI;
    }

    public void setMAIL_ADRESI(String MAIL_ADRESI) {
        this.MAIL_ADRESI = MAIL_ADRESI;
    }

    public int getAKTIF_PASIF_TUTAR() {
        return AKTIF_PASIF_TUTAR;
    }

    public void setAKTIF_PASIF_TUTAR(int AKTIF_PASIF_TUTAR) {
        this.AKTIF_PASIF_TUTAR = AKTIF_PASIF_TUTAR;
    }

    public boolean isAktifPasifTutarBoolean() {
        return aktifPasifTutarBoolean;
    }

    public void setAktifPasifTutarBoolean(boolean aktifPasifTutarBoolean) {
        this.aktifPasifTutarBoolean = aktifPasifTutarBoolean;
    }
    
    
    
}
