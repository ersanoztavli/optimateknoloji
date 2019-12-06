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
public class SATISTAN_TEDARIKCI_KAYITLARI 
{
    private int SATISTAN_TEDARIKCI_KAYITLARI_ID;
    private int OLUSTURAN_ID;
    private java.util.Date OLUSTURMA_TARIHI;
    private int KULLANIM_DURUMU;
    private int GUNCELLEYEN_ID;
    private java.util.Date GUNCELLEME_TARIHI;
    private int TEKLIF_SATIR_ID;
    private int TEKLIF_SATIR_YANIT_ID;
    private int SIRA_NO;
    private int SECILDI_MI;
    
    
    //Sanal Kolonlar
    //MAIL_ADRES tablosundan
    private String mailAdresParaBirimi;
    private int mailAdresTamamlandiMi;
    private boolean mailAdresTamamlandiMiBoolean;
    private String mailAdresMailAdresi;
    private String mailAdresYanitlayanAdSoyad;
    private String mailAdresMusteriRecordId;
    private double mailAdresKur;
    private int mailAdresMailId;
    
    //TEKLIF_SATIR_YANIT tablosundan
    private String teklifSatirYanitAciklama;
    private double teklifSatirYanitBirimFiyat;
    private int teklifSatirYanitStoktaVarYok;
    private boolean teklifSatirYanitStoktaVarYokBoolean;
    private double teklifSatirYanitMiktar;
    private String teklifSatirYanitBirim;
    private int teklifSatirYanitMailAdresId;
    
    //MUSTERI tablosundan
    private String musteriMusteriKodu;
    private String musteriMusteriUnvani;
    private String musteriMusteriKoduVeUnvani;



    public int getSATISTAN_TEDARIKCI_KAYITLARI_ID() {
        return SATISTAN_TEDARIKCI_KAYITLARI_ID;
    }

    public void setSATISTAN_TEDARIKCI_KAYITLARI_ID(int SATISTAN_TEDARIKCI_KAYITLARI_ID) {
        this.SATISTAN_TEDARIKCI_KAYITLARI_ID = SATISTAN_TEDARIKCI_KAYITLARI_ID;
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

    public int getTEKLIF_SATIR_YANIT_ID() {
        return TEKLIF_SATIR_YANIT_ID;
    }

    public void setTEKLIF_SATIR_YANIT_ID(int TEKLIF_SATIR_YANIT_ID) {
        this.TEKLIF_SATIR_YANIT_ID = TEKLIF_SATIR_YANIT_ID;
    }

    public int getSIRA_NO() {
        return SIRA_NO;
    }

    public void setSIRA_NO(int SIRA_NO) {
        this.SIRA_NO = SIRA_NO;
    }

    public int getSECILDI_MI() {
        return SECILDI_MI;
    }

    public void setSECILDI_MI(int SECILDI_MI) {
        this.SECILDI_MI = SECILDI_MI;
    }

    public String getMailAdresParaBirimi() {
        return mailAdresParaBirimi;
    }

    public void setMailAdresParaBirimi(String mailAdresParaBirimi) {
        this.mailAdresParaBirimi = mailAdresParaBirimi;
    }

    public int getMailAdresTamamlandiMi() {
        return mailAdresTamamlandiMi;
    }

    public void setMailAdresTamamlandiMi(int mailAdresTamamlandiMi) {
        this.mailAdresTamamlandiMi = mailAdresTamamlandiMi;
    }

    public String getMailAdresMailAdresi() {
        return mailAdresMailAdresi;
    }

    public void setMailAdresMailAdresi(String mailAdresMailAdresi) {
        this.mailAdresMailAdresi = mailAdresMailAdresi;
    }

    public String getMailAdresYanitlayanAdSoyad() {
        return mailAdresYanitlayanAdSoyad;
    }

    public void setMailAdresYanitlayanAdSoyad(String mailAdresYanitlayanAdSoyad) {
        this.mailAdresYanitlayanAdSoyad = mailAdresYanitlayanAdSoyad;
    }

    public String getTeklifSatirYanitAciklama() {
        return teklifSatirYanitAciklama;
    }

    public void setTeklifSatirYanitAciklama(String teklifSatirYanitAciklama) {
        this.teklifSatirYanitAciklama = teklifSatirYanitAciklama;
    }

    public double getTeklifSatirYanitBirimFiyat() {
        return teklifSatirYanitBirimFiyat;
    }

    public void setTeklifSatirYanitBirimFiyat(double teklifSatirYanitBirimFiyat) {
        this.teklifSatirYanitBirimFiyat = teklifSatirYanitBirimFiyat;
    }

    public int getTeklifSatirYanitStoktaVarYok() {
        return teklifSatirYanitStoktaVarYok;
    }

    public void setTeklifSatirYanitStoktaVarYok(int teklifSatirYanitStoktaVarYok) {
        this.teklifSatirYanitStoktaVarYok = teklifSatirYanitStoktaVarYok;
    }

    public double getTeklifSatirYanitMiktar() {
        return teklifSatirYanitMiktar;
    }

    public void setTeklifSatirYanitMiktar(double teklifSatirYanitMiktar) {
        this.teklifSatirYanitMiktar = teklifSatirYanitMiktar;
    }

    public String getTeklifSatirYanitBirim() {
        return teklifSatirYanitBirim;
    }

    public void setTeklifSatirYanitBirim(String teklifSatirYanitBirim) {
        this.teklifSatirYanitBirim = teklifSatirYanitBirim;
    }

    public String getMusteriMusteriKoduVeUnvani() {
        return musteriMusteriKoduVeUnvani;
    }

    public void setMusteriMusteriKoduVeUnvani(String musteriMusteriKoduVeUnvani) {
        this.musteriMusteriKoduVeUnvani = musteriMusteriKoduVeUnvani;
    }

    public boolean isTeklifSatirYanitStoktaVarYokBoolean() {
        return teklifSatirYanitStoktaVarYokBoolean;
    }

    public void setTeklifSatirYanitStoktaVarYokBoolean(boolean teklifSatirYanitStoktaVarYokBoolean) {
        this.teklifSatirYanitStoktaVarYokBoolean = teklifSatirYanitStoktaVarYokBoolean;
    }

    public int getTeklifSatirYanitMailAdresId() {
        return teklifSatirYanitMailAdresId;
    }

    public void setTeklifSatirYanitMailAdresId(int teklifSatirYanitMailAdresId) {
        this.teklifSatirYanitMailAdresId = teklifSatirYanitMailAdresId;
    }

    public boolean isMailAdresTamamlandiMiBoolean() {
        return mailAdresTamamlandiMiBoolean;
    }

    public void setMailAdresTamamlandiMiBoolean(boolean mailAdresTamamlandiMiBoolean) {
        this.mailAdresTamamlandiMiBoolean = mailAdresTamamlandiMiBoolean;
    }

    public String getMailAdresMusteriRecordId() {
        return mailAdresMusteriRecordId;
    }

    public void setMailAdresMusteriRecordId(String mailAdresMusteriRecordId) {
        this.mailAdresMusteriRecordId = mailAdresMusteriRecordId;
    }

    public double getMailAdresKur() {
        return mailAdresKur;
    }

    public void setMailAdresKur(double mailAdresKur) {
        this.mailAdresKur = mailAdresKur;
    }

    public String getMusteriMusteriKodu() {
        return musteriMusteriKodu;
    }

    public void setMusteriMusteriKodu(String musteriMusteriKodu) {
        this.musteriMusteriKodu = musteriMusteriKodu;
    }

    public String getMusteriMusteriUnvani() {
        return musteriMusteriUnvani;
    }

    public void setMusteriMusteriUnvani(String musteriMusteriUnvani) {
        this.musteriMusteriUnvani = musteriMusteriUnvani;
    }

    public int getMailAdresMailId() {
        return mailAdresMailId;
    }

    public void setMailAdresMailId(int mailAdresMailId) {
        this.mailAdresMailId = mailAdresMailId;
    }
    
}
