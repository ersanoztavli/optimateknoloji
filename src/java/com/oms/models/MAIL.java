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
public class MAIL {
    
    private int MAIL_ID;
    private int OLUSTURAN_ID;
    private java.util.Date OLUSTURMA_TARIHI;
    private int KULLANIM_DURUMU;
    private int GUNCELLEYEN_ID;
    private java.util.Date GUNCELLEME_TARIHI;
    private String MAIL_GONDEREN;
    private String MAIL_KONU;
    private String MAIL_ICERIK;
    private java.util.Date MAIL_GONDERIM_TARIHI;
    private int TEKLIF_BASLIK_ID;
    private int HATIRLATMA_MI;
    
    //Sanal Kolon
    private List<MAIL_ADRES> mailAdresler;

    public int getMAIL_ID() {
        return MAIL_ID;
    }

    public void setMAIL_ID(int MAIL_ID) {
        this.MAIL_ID = MAIL_ID;
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

    public String getMAIL_GONDEREN() {
        return MAIL_GONDEREN;
    }

    public void setMAIL_GONDEREN(String MAIL_GONDEREN) {
        this.MAIL_GONDEREN = MAIL_GONDEREN;
    }

    public String getMAIL_KONU() {
        return MAIL_KONU;
    }

    public void setMAIL_KONU(String MAIL_KONU) {
        this.MAIL_KONU = MAIL_KONU;
    }

    public String getMAIL_ICERIK() {
        return MAIL_ICERIK;
    }

    public void setMAIL_ICERIK(String MAIL_ICERIK) {
        this.MAIL_ICERIK = MAIL_ICERIK;
    }

    public Date getMAIL_GONDERIM_TARIHI() {
        return MAIL_GONDERIM_TARIHI;
    }

    public void setMAIL_GONDERIM_TARIHI(Date MAIL_GONDERIM_TARIHI) {
        this.MAIL_GONDERIM_TARIHI = MAIL_GONDERIM_TARIHI;
    }

    public int getTEKLIF_BASLIK_ID() {
        return TEKLIF_BASLIK_ID;
    }

    public void setTEKLIF_BASLIK_ID(int TEKLIF_BASLIK_ID) {
        this.TEKLIF_BASLIK_ID = TEKLIF_BASLIK_ID;
    }

    public int getHATIRLATMA_MI() {
        return HATIRLATMA_MI;
    }

    public void setHATIRLATMA_MI(int HATIRLATMA_MI) {
        this.HATIRLATMA_MI = HATIRLATMA_MI;
    }    

    public List<MAIL_ADRES> getMailAdresler() {
        return mailAdresler;
    }

    public void setMailAdresler(List<MAIL_ADRES> mailAdresler) {
        this.mailAdresler = mailAdresler;
    }

    
    
    
}
