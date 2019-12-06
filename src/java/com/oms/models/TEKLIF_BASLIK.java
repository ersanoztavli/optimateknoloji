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
public class TEKLIF_BASLIK {
    
    
    private int TEKLIF_BASLIK_ID;
    private int OLUSTURAN_ID;
    private java.util.Date OLUSTURMA_TARIHI;
    private int KULLANIM_DURUMU;
    private int GUNCELLEYEN_ID;
    private java.util.Date GUNCELLEME_TARIHI;
    private String TEKLIF_NUMARASI;
    private java.util.Date TEKLIF_TARIHI;
    private String GECERLI_TEKLIF_REVIZYON_NUMARASI;
    private String REVIZYON_NUMARASI;
    private java.util.Date REVIZYON_TARIHI;
    private java.util.Date TEKLIF_BASLANGIC_TARIHI;
    private java.util.Date TEKLIF_BITIS_TARIHI;
    private int TEKLIF_DURUM_KODU;
    private String TEKLIF_DURUM_KODU_ACIKLAMA;
    private String TEKLIF_ACIKLAMA1;
    private String TEKLIF_ACIKLAMA2;
    private String TEKLIF_ACIKLAMA3;
    private String SORUMLU_PERSONEL;
    private String TESLIMAT_ZAMANI;
    private String VADE_ZAMANI;
    private int ODEME_SEKLI;
    private double TEKLIF_TUTARI;
    private double TEKLIF_TOPLAM_TUTARI;
    private double TEKLIF_TOPLAM_KDV_TUTARI;
    private double TEKLIF_TOPLAM_OTV_TUTARI;
    private double TEKLIF_TOPLAM_OIV_TUTARI;
    private double TEKLIF_TOPLAM_INDIRIM_TUTARI;
    private double TEKLIF_TOPLAM_INDIRIM_ORANI;
    private double TEKLIF_TOPLAM_DIGER_VERGI1_TUTARI;
    private double TEKLIF_TOPLAM_DIGER_VERGI2_TUTARI;
    private int TEKLIF_PARA_BIRIMI;
    private double TEKLIF_KUR;
    private int FIRMA_ID;
    private String MUSTERI_RECORD_ID;
    private String RECORD_ID;
    private String TEKLIF_GUID;
    private String ERP_FIRMA_NUMBER;    
    private String BELGE_NO;
    private String PROJE_KODU;
    private String SATIS_ELEMANI;
    private String MUSTERI_YANIT;
    private String PARA_BIRIMI;
    private double MUSTERI_MARJ;
    
    //Sanal Kolon
    private String musteriUnvani;    
    //Sanal Kolon
    private String musteriMail1;    
    //Sanal Kolon
    private String musteriMail2;    
     //Sanal Kolon
    private String musteriUlke;    
    //Sanal Kolon
    private String musteriIl;    
    //Sanal Kolon
    private String musteriIlce;    
    //Sanal Kolon
    private String musteriAdres;
    //Sanal Kolon
    private String musteriKodu;
    //Sanal Kolon
    private String firmaUnvan;
    //Sanal Kolon
    private String satinAlmaTedarikciParaBirimi;
    
    //Sanal Kolon ama Gerçeğe de dönüştürülebilir
    private double teklifTutariCarpiKur;
    
    //Sanal kolon, tedarikçiden alıp, müşteriye sattığımızda fiyata kar marjı eklerken kullanacağız 
    //ERP'den dinamik olarak getirildiği için sanal kolon oldu.
    private double musteriKarMarji;

    private boolean oncelikliTedarikcilerGelsinMi;
   
    public double getMusteriKarMarji() {
        return musteriKarMarji;
    }

    public void setMusteriKarMarji(double musteriKarMarji) {
        this.musteriKarMarji = musteriKarMarji;
    }
    
    public int getTEKLIF_BASLIK_ID() {
        return TEKLIF_BASLIK_ID;
    }

    public void setTEKLIF_BASLIK_ID(int TEKLIF_BASLIK_ID) {
        this.TEKLIF_BASLIK_ID = TEKLIF_BASLIK_ID;
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

    public String getTEKLIF_NUMARASI() {
        return TEKLIF_NUMARASI;
    }

    public void setTEKLIF_NUMARASI(String TEKLIF_NUMARASI) {
        this.TEKLIF_NUMARASI = TEKLIF_NUMARASI;
    }

    public Date getTEKLIF_TARIHI() {
        return TEKLIF_TARIHI;
    }

    public void setTEKLIF_TARIHI(Date TEKLIF_TARIHI) {
        this.TEKLIF_TARIHI = TEKLIF_TARIHI;
    }

    public String getGECERLI_TEKLIF_REVIZYON_NUMARASI() {
        return GECERLI_TEKLIF_REVIZYON_NUMARASI;
    }

    public void setGECERLI_TEKLIF_REVIZYON_NUMARASI(String GECERLI_TEKLIF_REVIZYON_NUMARASI) {
        this.GECERLI_TEKLIF_REVIZYON_NUMARASI = GECERLI_TEKLIF_REVIZYON_NUMARASI;
    }

    public String getREVIZYON_NUMARASI() {
        return REVIZYON_NUMARASI;
    }

    public void setREVIZYON_NUMARASI(String REVIZYON_NUMARASI) {
        this.REVIZYON_NUMARASI = REVIZYON_NUMARASI;
    }

    public Date getREVIZYON_TARIHI() {
        return REVIZYON_TARIHI;
    }

    public void setREVIZYON_TARIHI(Date REVIZYON_TARIHI) {
        this.REVIZYON_TARIHI = REVIZYON_TARIHI;
    }

    public Date getTEKLIF_BASLANGIC_TARIHI() {
        return TEKLIF_BASLANGIC_TARIHI;
    }

    public void setTEKLIF_BASLANGIC_TARIHI(Date TEKLIF_BASLANGIC_TARIHI) {
        this.TEKLIF_BASLANGIC_TARIHI = TEKLIF_BASLANGIC_TARIHI;
    }

    public Date getTEKLIF_BITIS_TARIHI() {
        return TEKLIF_BITIS_TARIHI;
    }

    public void setTEKLIF_BITIS_TARIHI(Date TEKLIF_BITIS_TARIHI) {
        this.TEKLIF_BITIS_TARIHI = TEKLIF_BITIS_TARIHI;
    }

    public int getTEKLIF_DURUM_KODU() {
        return TEKLIF_DURUM_KODU;
    }

    public void setTEKLIF_DURUM_KODU(int TEKLIF_DURUM_KODU) {
        this.TEKLIF_DURUM_KODU = TEKLIF_DURUM_KODU;
    }

    public String getTEKLIF_DURUM_KODU_ACIKLAMA() {
        return TEKLIF_DURUM_KODU_ACIKLAMA;
    }

    public void setTEKLIF_DURUM_KODU_ACIKLAMA(String TEKLIF_DURUM_KODU_ACIKLAMA) {
        this.TEKLIF_DURUM_KODU_ACIKLAMA = TEKLIF_DURUM_KODU_ACIKLAMA;
    }

    public String getTEKLIF_ACIKLAMA1() {
        return TEKLIF_ACIKLAMA1;
    }

    public void setTEKLIF_ACIKLAMA1(String TEKLIF_ACIKLAMA1) {
        this.TEKLIF_ACIKLAMA1 = TEKLIF_ACIKLAMA1;
    }

    public String getTEKLIF_ACIKLAMA2() {
        return TEKLIF_ACIKLAMA2;
    }

    public void setTEKLIF_ACIKLAMA2(String TEKLIF_ACIKLAMA2) {
        this.TEKLIF_ACIKLAMA2 = TEKLIF_ACIKLAMA2;
    }

    public String getTEKLIF_ACIKLAMA3() {
        return TEKLIF_ACIKLAMA3;
    }

    public void setTEKLIF_ACIKLAMA3(String TEKLIF_ACIKLAMA3) {
        this.TEKLIF_ACIKLAMA3 = TEKLIF_ACIKLAMA3;
    }

    public String getSORUMLU_PERSONEL() {
        return SORUMLU_PERSONEL;
    }

    public void setSORUMLU_PERSONEL(String SORUMLU_PERSONEL) {
        this.SORUMLU_PERSONEL = SORUMLU_PERSONEL;
    }

    public String getTESLIMAT_ZAMANI() {
        return TESLIMAT_ZAMANI;
    }

    public void setTESLIMAT_ZAMANI(String TESLIMAT_ZAMANI) {
        this.TESLIMAT_ZAMANI = TESLIMAT_ZAMANI;
    }

    public String getVADE_ZAMANI() {
        return VADE_ZAMANI;
    }

    public void setVADE_ZAMANI(String VADE_ZAMANI) {
        this.VADE_ZAMANI = VADE_ZAMANI;
    }

    public int getODEME_SEKLI() {
        return ODEME_SEKLI;
    }

    public void setODEME_SEKLI(int ODEME_SEKLI) {
        this.ODEME_SEKLI = ODEME_SEKLI;
    }

    public double getTEKLIF_TUTARI() {
        return TEKLIF_TUTARI;
    }

    public void setTEKLIF_TUTARI(double TEKLIF_TUTARI) {
        this.TEKLIF_TUTARI = TEKLIF_TUTARI;
    }

    public double getTEKLIF_TOPLAM_TUTARI() {
        return TEKLIF_TOPLAM_TUTARI;
    }

    public void setTEKLIF_TOPLAM_TUTARI(double TEKLIF_TOPLAM_TUTARI) {
        this.TEKLIF_TOPLAM_TUTARI = TEKLIF_TOPLAM_TUTARI;
    }

    public double getTEKLIF_TOPLAM_KDV_TUTARI() {
        return TEKLIF_TOPLAM_KDV_TUTARI;
    }

    public void setTEKLIF_TOPLAM_KDV_TUTARI(double TEKLIF_TOPLAM_KDV_TUTARI) {
        this.TEKLIF_TOPLAM_KDV_TUTARI = TEKLIF_TOPLAM_KDV_TUTARI;
    }

    public double getTEKLIF_TOPLAM_OTV_TUTARI() {
        return TEKLIF_TOPLAM_OTV_TUTARI;
    }

    public void setTEKLIF_TOPLAM_OTV_TUTARI(double TEKLIF_TOPLAM_OTV_TUTARI) {
        this.TEKLIF_TOPLAM_OTV_TUTARI = TEKLIF_TOPLAM_OTV_TUTARI;
    }

    public double getTEKLIF_TOPLAM_OIV_TUTARI() {
        return TEKLIF_TOPLAM_OIV_TUTARI;
    }

    public void setTEKLIF_TOPLAM_OIV_TUTARI(double TEKLIF_TOPLAM_OIV_TUTARI) {
        this.TEKLIF_TOPLAM_OIV_TUTARI = TEKLIF_TOPLAM_OIV_TUTARI;
    }

    public double getTEKLIF_TOPLAM_INDIRIM_TUTARI() {
        return TEKLIF_TOPLAM_INDIRIM_TUTARI;
    }

    public void setTEKLIF_TOPLAM_INDIRIM_TUTARI(double TEKLIF_TOPLAM_INDIRIM_TUTARI) {
        this.TEKLIF_TOPLAM_INDIRIM_TUTARI = TEKLIF_TOPLAM_INDIRIM_TUTARI;
    }

    public double getTEKLIF_TOPLAM_INDIRIM_ORANI() {
        return TEKLIF_TOPLAM_INDIRIM_ORANI;
    }

    public void setTEKLIF_TOPLAM_INDIRIM_ORANI(double TEKLIF_TOPLAM_INDIRIM_ORANI) {
        this.TEKLIF_TOPLAM_INDIRIM_ORANI = TEKLIF_TOPLAM_INDIRIM_ORANI;
    }

    public double getTEKLIF_TOPLAM_DIGER_VERGI1_TUTARI() {
        return TEKLIF_TOPLAM_DIGER_VERGI1_TUTARI;
    }

    public void setTEKLIF_TOPLAM_DIGER_VERGI1_TUTARI(double TEKLIF_TOPLAM_DIGER_VERGI1_TUTARI) {
        this.TEKLIF_TOPLAM_DIGER_VERGI1_TUTARI = TEKLIF_TOPLAM_DIGER_VERGI1_TUTARI;
    }

    public double getTEKLIF_TOPLAM_DIGER_VERGI2_TUTARI() {
        return TEKLIF_TOPLAM_DIGER_VERGI2_TUTARI;
    }

    public void setTEKLIF_TOPLAM_DIGER_VERGI2_TUTARI(double TEKLIF_TOPLAM_DIGER_VERGI2_TUTARI) {
        this.TEKLIF_TOPLAM_DIGER_VERGI2_TUTARI = TEKLIF_TOPLAM_DIGER_VERGI2_TUTARI;
    }

    public int getTEKLIF_PARA_BIRIMI() {
        return TEKLIF_PARA_BIRIMI;
    }

    public void setTEKLIF_PARA_BIRIMI(int TEKLIF_PARA_BIRIMI) {
        this.TEKLIF_PARA_BIRIMI = TEKLIF_PARA_BIRIMI;
    }

    public double getTEKLIF_KUR() {
        return TEKLIF_KUR;
    }

    public void setTEKLIF_KUR(double TEKLIF_KUR) {
        this.TEKLIF_KUR = TEKLIF_KUR;
    }

    public int getFIRMA_ID() {
        return FIRMA_ID;
    }

    public void setFIRMA_ID(int FIRMA_ID) {
        this.FIRMA_ID = FIRMA_ID;
    }

    public String getMUSTERI_RECORD_ID() {
        return MUSTERI_RECORD_ID;
    }

    public void setMUSTERI_RECORD_ID(String MUSTERI_RECORD_ID) {
        this.MUSTERI_RECORD_ID = MUSTERI_RECORD_ID;
    }

    public String getRECORD_ID() {
        return RECORD_ID;
    }

    public void setRECORD_ID(String RECORD_ID) {
        this.RECORD_ID = RECORD_ID;
    }

    public String getTEKLIF_GUID() {
        return TEKLIF_GUID;
    }

    public void setTEKLIF_GUID(String TEKLIF_GUID) {
        this.TEKLIF_GUID = TEKLIF_GUID;
    }

    public String getERP_FIRMA_NUMBER() {
        return ERP_FIRMA_NUMBER;
    }

    public void setERP_FIRMA_NUMBER(String ERP_FIRMA_NUMBER) {
        this.ERP_FIRMA_NUMBER = ERP_FIRMA_NUMBER;
    }

    public String getMusteriUnvani() {
        return musteriUnvani;
    }

    public void setMusteriUnvani(String musteriUnvani) {
        this.musteriUnvani = musteriUnvani;
    }

    public String getMusteriMail1() {
        return musteriMail1;
    }

    public void setMusteriMail1(String musteriMail1) {
        this.musteriMail1 = musteriMail1;
    }

    public String getMusteriMail2() {
        return musteriMail2;
    }

    public void setMusteriMail2(String musteriMail2) {
        this.musteriMail2 = musteriMail2;
    }
    
    public String getFirmaUnvan() {
        return firmaUnvan;
    }

    public void setFirmaUnvan(String firmaUnvan) {
        this.firmaUnvan = firmaUnvan;
    }

    public String getMusteriUlke() {
        return musteriUlke;
    }

    public void setMusteriUlke(String musteriUlke) {
        this.musteriUlke = musteriUlke;
    }

    public String getMusteriIl() {
        return musteriIl;
    }

    public void setMusteriIl(String musteriIl) {
        this.musteriIl = musteriIl;
    }

    public String getMusteriIlce() {
        return musteriIlce;
    }

    public void setMusteriIlce(String musteriIlce) {
        this.musteriIlce = musteriIlce;
    }

    public String getMusteriAdres() {
        return musteriAdres;
    }

    public void setMusteriAdres(String musteriAdres) {
        this.musteriAdres = musteriAdres;
    }

    public String getBELGE_NO() {
        return BELGE_NO;
    }

    public void setBELGE_NO(String BELGE_NO) {
        this.BELGE_NO = BELGE_NO;
    }

    public String getPROJE_KODU() {
        return PROJE_KODU;
    }

    public void setPROJE_KODU(String PROJE_KODU) {
        this.PROJE_KODU = PROJE_KODU;
    }

    public String getSATIS_ELEMANI() {
        return SATIS_ELEMANI;
    }

    public void setSATIS_ELEMANI(String SATIS_ELEMANI) {
        this.SATIS_ELEMANI = SATIS_ELEMANI;
    }

    public String getMusteriKodu() {
        return musteriKodu;
    }

    public void setMusteriKodu(String musteriKodu) {
        this.musteriKodu = musteriKodu;
    }

    public String getMUSTERI_YANIT() {
        return MUSTERI_YANIT;
    }

    public void setMUSTERI_YANIT(String MUSTERI_YANIT) {
        this.MUSTERI_YANIT = MUSTERI_YANIT;
    }    

    public String getPARA_BIRIMI() {
        return PARA_BIRIMI;
    }

    public void setPARA_BIRIMI(String PARA_BIRIMI) {
        this.PARA_BIRIMI = PARA_BIRIMI;
    }

    public String getSatinAlmaTedarikciParaBirimi() {
        return satinAlmaTedarikciParaBirimi;
    }

    public void setSatinAlmaTedarikciParaBirimi(String satinAlmaTedarikciParaBirimi) {
        this.satinAlmaTedarikciParaBirimi = satinAlmaTedarikciParaBirimi;
    }

    public double getTeklifTutariCarpiKur() {
        return teklifTutariCarpiKur;
    }

    public void setTeklifTutariCarpiKur(double teklifTutariCarpiKur) {
        this.teklifTutariCarpiKur = teklifTutariCarpiKur;
    }

    public double getMUSTERI_MARJ() {
        return MUSTERI_MARJ;
    }

    public void setMUSTERI_MARJ(double MUSTERI_MARJ) {
        this.MUSTERI_MARJ = MUSTERI_MARJ;
    }

    public boolean isOncelikliTedarikcilerGelsinMi() {
        return oncelikliTedarikcilerGelsinMi;
    }

    public void setOncelikliTedarikcilerGelsinMi(boolean oncelikliTedarikcilerGelsinMi) {
        this.oncelikliTedarikcilerGelsinMi = oncelikliTedarikcilerGelsinMi;
    }
    
}
