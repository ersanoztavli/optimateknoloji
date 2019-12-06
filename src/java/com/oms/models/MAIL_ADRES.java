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
public class MAIL_ADRES {
    private int MAIL_ADRES_ID;
    private int OLUSTURAN_ID;
    private java.util.Date OLUSTURMA_TARIHI;
    private int KULLANIM_DURUMU;
    private int GUNCELLEYEN_ID;
    private java.util.Date GUNCELLEME_TARIHI;
    private String MAIL_ADRESI;
    private int TO_CC_BCC;
    private int DURUMU;
    private int MAIL_ID;
    private String MUSTERI_RECORD_ID;
    private int TAMAMLANDI_MI;
    private String PARA_BIRIMI;    
    private double KUR;
    
    //Sanal Kolon
    private String musteriUnvan;    
    private String tamamlandiMiIsim;
    private String teklifYanitlayanAdSoyad;//Aslında sanal kolon değil ama yanlış olmuş...olsun.

    public int getMAIL_ADRES_ID() {
        return MAIL_ADRES_ID;
    }

    public void setMAIL_ADRES_ID(int MAIL_ADRES_ID) {
        this.MAIL_ADRES_ID = MAIL_ADRES_ID;
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

    public String getMAIL_ADRESI() {
        return MAIL_ADRESI;
    }

    public void setMAIL_ADRESI(String MAIL_ADRESI) {
        this.MAIL_ADRESI = MAIL_ADRESI;
    }

    public int getTO_CC_BCC() {
        return TO_CC_BCC;
    }

    public void setTO_CC_BCC(int TO_CC_BCC) {
        this.TO_CC_BCC = TO_CC_BCC;
    }

    public int getDURUMU() {
        return DURUMU;
    }

    public void setDURUMU(int DURUMU) {
        this.DURUMU = DURUMU;
    }

    public int getMAIL_ID() {
        return MAIL_ID;
    }

    public void setMAIL_ID(int MAIL_ID) {
        this.MAIL_ID = MAIL_ID;
    }

    public String getMUSTERI_RECORD_ID() {
        return MUSTERI_RECORD_ID;
    }

    public void setMUSTERI_RECORD_ID(String MUSTERI_RECORD_ID) {
        this.MUSTERI_RECORD_ID = MUSTERI_RECORD_ID;
    }

    public String getMusteriUnvan() {
        return musteriUnvan;
    }

    public void setMusteriUnvan(String musteriUnvan) {
        this.musteriUnvan = musteriUnvan;
    }

    public int getTAMAMLANDI_MI() {
        return TAMAMLANDI_MI;
    }

    public void setTAMAMLANDI_MI(int TAMAMLANDI_MI) {
        this.TAMAMLANDI_MI = TAMAMLANDI_MI;
    }

    public String getTamamlandiMiIsim() {
        return tamamlandiMiIsim;
    }

    public void setTamamlandiMiIsim(String tamamlandiMiIsim) {
        this.tamamlandiMiIsim = tamamlandiMiIsim;
    }

    public String getTeklifYanitlayanAdSoyad() {
        return teklifYanitlayanAdSoyad;
    }

    public void setTeklifYanitlayanAdSoyad(String teklifYanitlayanAdSoyad) {
        this.teklifYanitlayanAdSoyad = teklifYanitlayanAdSoyad;
    }

    public String getPARA_BIRIMI() {
        return PARA_BIRIMI;
    }

    public void setPARA_BIRIMI(String PARA_BIRIMI) {
        this.PARA_BIRIMI = PARA_BIRIMI;
    }

    public double getKUR() {
        return KUR;
    }

    public void setKUR(double KUR) {
        this.KUR = KUR;
    }
    
}
