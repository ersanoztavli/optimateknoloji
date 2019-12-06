/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.models;

import java.util.Date;
import java.util.List;

/**
 *
 * @author ersan
 */
public class TEKLIF_SATIR_YANIT {
    
    private int TEKLIF_SATIR_YANIT_ID;
    private int OLUSTURAN_ID;
    private java.util.Date OLUSTURMA_TARIHI;
    private int KULLANIM_DURUMU;
    private int GUNCELLEYEN_ID;
    private java.util.Date GUNCELLEME_TARIHI;
    private int TEKLIF_SATIR_ID;
    private int MAIL_ADRES_ID;
    private String ACIKLAMA;
    private double BIRIM_FIYATI;
    private int STOKTA_VAR_YOK;
    private double MIKTAR;
    private double SONMALIYET;
    private String BIRIM;
    
    //Sanal Kolonlar...
    private String malzemeHizmetMasrafKodu;
    private String malzemeHizmetMasrafAdi;
    private String malzemeHizmetMasrafAdi2;
    private String malzemeHizmetMasrafAciklamasi;
    private String malzemeHizmetMasrafBirimKodu;
    private String malzemeHizmetMasrafRecordId;
    private double malzemeHizmetMasrafMiktari;
    private boolean stoktaVarYokBoolean;
    private java.util.Date malzemeTeslimTarihi;
    
    private List<String> birimListesi;
    
    public String getMalzemeHizmetMasrafKodu() {
        return malzemeHizmetMasrafKodu;
    }

    public void setMalzemeHizmetMasrafKodu(String malzemeHizmetMasrafKodu) {
        this.malzemeHizmetMasrafKodu = malzemeHizmetMasrafKodu;
    }

    public String getMalzemeHizmetMasrafAdi() {
        return malzemeHizmetMasrafAdi;
    }

    public void setMalzemeHizmetMasrafAdi(String malzemeHizmetMasrafAdi) {
        this.malzemeHizmetMasrafAdi = malzemeHizmetMasrafAdi;
    }

    public String getMalzemeHizmetMasrafAciklamasi() {
        return malzemeHizmetMasrafAciklamasi;
    }

    public void setMalzemeHizmetMasrafAciklamasi(String malzemeHizmetMasrafAciklamasi) {
        this.malzemeHizmetMasrafAciklamasi = malzemeHizmetMasrafAciklamasi;
    }

    public String getMalzemeHizmetMasrafBirimKodu() {
        return malzemeHizmetMasrafBirimKodu;
    }

    public void setMalzemeHizmetMasrafBirimKodu(String malzemeHizmetMasrafBirimKodu) {
        this.malzemeHizmetMasrafBirimKodu = malzemeHizmetMasrafBirimKodu;
    }

    public double getMalzemeHizmetMasrafMiktari() {
        return malzemeHizmetMasrafMiktari;
    }

    public void setMalzemeHizmetMasrafMiktari(double malzemeHizmetMasrafMiktari) {
        this.malzemeHizmetMasrafMiktari = malzemeHizmetMasrafMiktari;
    }
    
    public int getTEKLIF_SATIR_YANIT_ID() {
        return TEKLIF_SATIR_YANIT_ID;
    }

    public void setTEKLIF_SATIR_YANIT_ID(int TEKLIF_SATIR_YANIT_ID) {
        this.TEKLIF_SATIR_YANIT_ID = TEKLIF_SATIR_YANIT_ID;
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

    public int getTEKLIF_SATIR_ID() {
        return TEKLIF_SATIR_ID;
    }

    public void setTEKLIF_SATIR_ID(int TEKLIF_SATIR_ID) {
        this.TEKLIF_SATIR_ID = TEKLIF_SATIR_ID;
    }

    public int getMAIL_ADRES_ID() {
        return MAIL_ADRES_ID;
    }

    public void setMAIL_ADRES_ID(int MAIL_ADRES_ID) {
        this.MAIL_ADRES_ID = MAIL_ADRES_ID;
    }

    public String getACIKLAMA() {
        return ACIKLAMA;
    }

    public void setACIKLAMA(String ACIKLAMA) {
        this.ACIKLAMA = ACIKLAMA;
    }

    public double getBIRIM_FIYATI() {
        return BIRIM_FIYATI;
    }

    public void setBIRIM_FIYATI(double BIRIM_FIYATI) {
        this.BIRIM_FIYATI = BIRIM_FIYATI;
    }

    public int getSTOKTA_VAR_YOK() {
        return STOKTA_VAR_YOK;
    }

    public void setSTOKTA_VAR_YOK(int STOKTA_VAR_YOK) {
        this.STOKTA_VAR_YOK = STOKTA_VAR_YOK;
    }

    public boolean isStoktaVarYokBoolean() {
        return stoktaVarYokBoolean;
    }

    public void setStoktaVarYokBoolean(boolean stoktaVarYokBoolean) {
        this.stoktaVarYokBoolean = stoktaVarYokBoolean;
    }

    public String getMalzemeHizmetMasrafAdi2() {
        return malzemeHizmetMasrafAdi2;
    }

    public void setMalzemeHizmetMasrafAdi2(String malzemeHizmetMasrafAdi2) {
        this.malzemeHizmetMasrafAdi2 = malzemeHizmetMasrafAdi2;
    }

    public double getMIKTAR() {
        return MIKTAR;
    }

    public void setMIKTAR(double MIKTAR) {
        this.MIKTAR = MIKTAR;
    }

    public double getSONMALIYET() {
        return SONMALIYET;
    }

    public void setSONMALIYET(double SONMALIYET) {
        this.SONMALIYET = SONMALIYET;
    }

    public String getBIRIM() {
        return BIRIM;
    }

    public void setBIRIM(String BIRIM) {
        this.BIRIM = BIRIM;
    }

    public String getMalzemeHizmetMasrafRecordId() {
        return malzemeHizmetMasrafRecordId;
    }

    public void setMalzemeHizmetMasrafRecordId(String malzemeHizmetMasrafRecordId) {
        this.malzemeHizmetMasrafRecordId = malzemeHizmetMasrafRecordId;
    }

    public Date getMalzemeTeslimTarihi() {
        return malzemeTeslimTarihi;
    }

    public void setMalzemeTeslimTarihi(Date malzemeTeslimTarihi) {
        this.malzemeTeslimTarihi = malzemeTeslimTarihi;
    }

    public List<String> getBirimListesi() {
        return birimListesi;
    }

    public void setBirimListesi(List<String> birimListesi) {
        this.birimListesi = birimListesi;
    }    
}
