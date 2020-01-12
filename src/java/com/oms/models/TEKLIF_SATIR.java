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
public class TEKLIF_SATIR {
    
    private int TEKLIF_SATIR_ID;
    private int OLUSTURAN_ID;
    private java.util.Date OLUSTURMA_TARIHI;
    private int KULLANIM_DURUMU;
    private int GUNCELLEYEN_ID;
    private java.util.Date GUNCELLEME_TARIHI;
    private String MALZEME_HIZMET_MASRAF_KODU;
    private String MALZEME_HIZMET_MASRAF_ADI;
    private String MALZEME_HIZMET_MASRAF_ACIKLAMASI;
    private String MALZEME_HIZMET_MASRAF_BIRIM_KODU;
    private double MALZEME_HIZMET_MASRAF_MIKTARI;
    private double MUSTERI_SATIS_MIKTAR_YANIT;
    private double MALZEME_HIZMET_MASRAF_BIRIM_FIYATI;
    private double MALZEME_HIZMET_MASRAF_INDIRIM_TUTARI;
    private double MALZEME_HIZMET_MASRAF_INDIRIM_ORANI;
    private double MALZEME_HIZMET_MASRAF_KDV_TUTARI;
    private double MALZEME_HIZMET_MASRAF_KDV_ORANI;
    private double MALZEME_HIZMET_MASRAF_OTV_TUTARI;
    private double MALZEME_HIZMET_MASRAF_OTV_ORANI;
    private double MALZEME_HIZMET_MASRAF_OIV_TUTARI;
    private double MALZEME_HIZMET_MASRAF_OIV_ORANI;
    private double MALZEME_HIZMET_MASRAF_DIGER_VERGI1_TUTARI;
    private double MALZEME_HIZMET_MASRAF_DIGER_VERGI1_ORANI;
    private double MALZEME_HIZMET_MASRAF_DIGER_VERGI2_TUTARI;
    private double MALZEME_HIZMET_MASRAF_DIGER_VERGI2_ORANI;
    private double MALZEME_HIZMET_MASRAF_TUTARI;
    private double MALZEME_HIZMET_MASRAF_TOPLAM_TUTARI;
    private String MALZEME_HIZMET_MASRAF_ISTISNA_KODU;
    private String MALZEME_HIZMET_MASRAF_ISTISNA_KODU_ACIKLAMA;
    private String MALZEME_HIZMET_MASRAF_TEVKIFAT_KODU;
    private String MALZEME_HIZMET_MASRAF_TEVKIFAT_KODU_ACIKLAMA;
    private int MALZEME_HIZMET_MASRAF_TEVKIFAT_ORANI_PAY;
    private int MALZEME_HIZMET_MASRAF_TEVKIFAT_ORANI_PAYDA;
    private String RECORD_ID;
    private String TEKLIF_BASLIK_RECORD_ID;
    private String ERP_FIRMA_NUMBER;
    private int SATIR_TURU;
    private int BIRIM_CEVRIM_1;
    private int BIRIM_CEVRIM_2;
    private int PARA_BIRIMI;
    private double KUR;
    private java.util.Date TESLIM_TARIHI;
    private String MALZEME_HIZMET_MASRAF_ADI2; //İngilizce olan ismi çekebilmek için...    
    private int SECILEN_TEDARIKCI_ID;//Satış teklifi satırını tedarik ediyorsak; ilgili tedarikçiyi kaydemek için...
    private String SECILEN_TEDARIKCI_ACIKLAMA;
    private double MUSTERI_MARJ;
    private double MALIYET_BIRIM_FIYAT;//Maliyeti hesap edebilmek için...
       
    
    //Sanal Kolon ama Gerçeğe de dönüştürülebilir
    private double malzemeHizmetMasrafTutariCarpiKur;
    
    //Sanal Kolon satış teklifindeki herbir satırla 
    //ilgili tedarikçi listesini tutabilmek için
    private List<TedarikciListSatisSatirBazinda> tedarikciListesi;
    
    //Sanal Kolon , tedarikçiler listelendikten sonra seçilenleri tutabilmek için...
    private List<TedarikciListSatisSatirBazinda> secilenTedarikciListesi;
    
    //Teklif almadan doğrudan tedarik edilecek ürünler için...
    private TedarikciListSatisSatirBazinda secilenTedarikci;
    
    //Teklif almadan doğrudan tedarik edilecek ürünler için...
    //Filtreleme için
    private List<TedarikciListSatisSatirBazinda> tedarikciListesiFiltreleme;
    
    //Sanal Kolon (ERP'den çekilen) ama Gerçeğe de dönüştürülebilir 
    private double fiiliStok;
    
    //Sanal Kolon; true = Stok, false = tedarik...
    private boolean teminTuru; //TedarikciList disabled değişkeni için
    private boolean teminTuruTersi; // Temin türü selectBooleanButton için
                                    //TedarikciList disabled değişkeni ile
                                    //Temin türü selectBooleanButton birbirinin tersi olması gerekiyor...
    
    //Sanal Kolon; daha önce bu satır ile ilgili 
    //tedarikçilerle bir hareket olduysa göstermek için
    private List<SATISTAN_TEDARIKCI_KAYITLARI> satistanTedarikciKayitlariList;
    
    //Excelden yükleme yaparken 0 yerine TL yazsın diye vb.
    private String birimIsim;
    private String paraBirimiIsim;
    
    //Satır bazında müşteriye tanımlanan ve listede olan satış fiyatları
    //Müşteriye tanımlı bir tane gelecek ama listede olan birden fazla olabilir...
    private List<TanimliSatisFiyati> tanimliSatisFiyatiList;   
    
    private List<TanimliSatinAlmaFiyati> tanimliSatinAlmaFiyatiList;  
    
    //İlgili satır için son teklif ve satin alma bilgisini tutmak için...
    private SonTeklifVeSatinAlmaSatisSatiri sonTeklifVeSatinAlmaSatisSatiri;
    
    //Sanal Kolon; satış satırını temin edeceğimiz tedarikçiyi kayıt altına almak için...
    private String secilenTedarikciKodu;
    private String secilenTedarikciUnvani;
    
    //revizyon yaparken kullanacağımız alan...
    private int yeniTeklifSatirId;
    
    public int getTEKLIF_SATIR_ID() {
        return TEKLIF_SATIR_ID;
    }

    public void setTEKLIF_SATIR_ID(int TEKLIF_SATIR_ID) {
        this.TEKLIF_SATIR_ID = TEKLIF_SATIR_ID;
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

    public String getMALZEME_HIZMET_MASRAF_KODU() {
        return MALZEME_HIZMET_MASRAF_KODU;
    }

    public void setMALZEME_HIZMET_MASRAF_KODU(String MALZEME_HIZMET_MASRAF_KODU) {
        this.MALZEME_HIZMET_MASRAF_KODU = MALZEME_HIZMET_MASRAF_KODU;
    }

    public String getMALZEME_HIZMET_MASRAF_ADI() {
        return MALZEME_HIZMET_MASRAF_ADI;
    }

    public void setMALZEME_HIZMET_MASRAF_ADI(String MALZEME_HIZMET_MASRAF_ADI) {
        this.MALZEME_HIZMET_MASRAF_ADI = MALZEME_HIZMET_MASRAF_ADI;
    }

    public String getMALZEME_HIZMET_MASRAF_ACIKLAMASI() {
        return MALZEME_HIZMET_MASRAF_ACIKLAMASI;
    }

    public void setMALZEME_HIZMET_MASRAF_ACIKLAMASI(String MALZEME_HIZMET_MASRAF_ACIKLAMASI) {
        this.MALZEME_HIZMET_MASRAF_ACIKLAMASI = MALZEME_HIZMET_MASRAF_ACIKLAMASI;
    }

    public String getMALZEME_HIZMET_MASRAF_BIRIM_KODU() {
        return MALZEME_HIZMET_MASRAF_BIRIM_KODU;
    }

    public void setMALZEME_HIZMET_MASRAF_BIRIM_KODU(String MALZEME_HIZMET_MASRAF_BIRIM_KODU) {
        this.MALZEME_HIZMET_MASRAF_BIRIM_KODU = MALZEME_HIZMET_MASRAF_BIRIM_KODU;
    }

    public double getMALZEME_HIZMET_MASRAF_MIKTARI() {
        return MALZEME_HIZMET_MASRAF_MIKTARI;
    }

    public void setMALZEME_HIZMET_MASRAF_MIKTARI(double MALZEME_HIZMET_MASRAF_MIKTARI) {
        this.MALZEME_HIZMET_MASRAF_MIKTARI = MALZEME_HIZMET_MASRAF_MIKTARI;
    }

    public double getMALZEME_HIZMET_MASRAF_BIRIM_FIYATI() {
        return MALZEME_HIZMET_MASRAF_BIRIM_FIYATI;
    }

    public void setMALZEME_HIZMET_MASRAF_BIRIM_FIYATI(double MALZEME_HIZMET_MASRAF_BIRIM_FIYATI) {
        this.MALZEME_HIZMET_MASRAF_BIRIM_FIYATI = MALZEME_HIZMET_MASRAF_BIRIM_FIYATI;
    }

    public double getMALZEME_HIZMET_MASRAF_INDIRIM_TUTARI() {
        return MALZEME_HIZMET_MASRAF_INDIRIM_TUTARI;
    }

    public void setMALZEME_HIZMET_MASRAF_INDIRIM_TUTARI(double MALZEME_HIZMET_MASRAF_INDIRIM_TUTARI) {
        this.MALZEME_HIZMET_MASRAF_INDIRIM_TUTARI = MALZEME_HIZMET_MASRAF_INDIRIM_TUTARI;
    }

    public double getMALZEME_HIZMET_MASRAF_INDIRIM_ORANI() {
        return MALZEME_HIZMET_MASRAF_INDIRIM_ORANI;
    }

    public void setMALZEME_HIZMET_MASRAF_INDIRIM_ORANI(double MALZEME_HIZMET_MASRAF_INDIRIM_ORANI) {
        this.MALZEME_HIZMET_MASRAF_INDIRIM_ORANI = MALZEME_HIZMET_MASRAF_INDIRIM_ORANI;
    }

    public double getMALZEME_HIZMET_MASRAF_KDV_TUTARI() {
        return MALZEME_HIZMET_MASRAF_KDV_TUTARI;
    }

    public void setMALZEME_HIZMET_MASRAF_KDV_TUTARI(double MALZEME_HIZMET_MASRAF_KDV_TUTARI) {
        this.MALZEME_HIZMET_MASRAF_KDV_TUTARI = MALZEME_HIZMET_MASRAF_KDV_TUTARI;
    }

    public double getMALZEME_HIZMET_MASRAF_KDV_ORANI() {
        return MALZEME_HIZMET_MASRAF_KDV_ORANI;
    }

    public void setMALZEME_HIZMET_MASRAF_KDV_ORANI(double MALZEME_HIZMET_MASRAF_KDV_ORANI) {
        this.MALZEME_HIZMET_MASRAF_KDV_ORANI = MALZEME_HIZMET_MASRAF_KDV_ORANI;
    }

    public double getMALZEME_HIZMET_MASRAF_OTV_TUTARI() {
        return MALZEME_HIZMET_MASRAF_OTV_TUTARI;
    }

    public void setMALZEME_HIZMET_MASRAF_OTV_TUTARI(double MALZEME_HIZMET_MASRAF_OTV_TUTARI) {
        this.MALZEME_HIZMET_MASRAF_OTV_TUTARI = MALZEME_HIZMET_MASRAF_OTV_TUTARI;
    }

    public double getMALZEME_HIZMET_MASRAF_OTV_ORANI() {
        return MALZEME_HIZMET_MASRAF_OTV_ORANI;
    }

    public void setMALZEME_HIZMET_MASRAF_OTV_ORANI(double MALZEME_HIZMET_MASRAF_OTV_ORANI) {
        this.MALZEME_HIZMET_MASRAF_OTV_ORANI = MALZEME_HIZMET_MASRAF_OTV_ORANI;
    }

    public double getMALZEME_HIZMET_MASRAF_OIV_TUTARI() {
        return MALZEME_HIZMET_MASRAF_OIV_TUTARI;
    }

    public void setMALZEME_HIZMET_MASRAF_OIV_TUTARI(double MALZEME_HIZMET_MASRAF_OIV_TUTARI) {
        this.MALZEME_HIZMET_MASRAF_OIV_TUTARI = MALZEME_HIZMET_MASRAF_OIV_TUTARI;
    }

    public double getMALZEME_HIZMET_MASRAF_OIV_ORANI() {
        return MALZEME_HIZMET_MASRAF_OIV_ORANI;
    }

    public void setMALZEME_HIZMET_MASRAF_OIV_ORANI(double MALZEME_HIZMET_MASRAF_OIV_ORANI) {
        this.MALZEME_HIZMET_MASRAF_OIV_ORANI = MALZEME_HIZMET_MASRAF_OIV_ORANI;
    }

    public double getMALZEME_HIZMET_MASRAF_DIGER_VERGI1_TUTARI() {
        return MALZEME_HIZMET_MASRAF_DIGER_VERGI1_TUTARI;
    }

    public void setMALZEME_HIZMET_MASRAF_DIGER_VERGI1_TUTARI(double MALZEME_HIZMET_MASRAF_DIGER_VERGI1_TUTARI) {
        this.MALZEME_HIZMET_MASRAF_DIGER_VERGI1_TUTARI = MALZEME_HIZMET_MASRAF_DIGER_VERGI1_TUTARI;
    }

    public double getMALZEME_HIZMET_MASRAF_DIGER_VERGI1_ORANI() {
        return MALZEME_HIZMET_MASRAF_DIGER_VERGI1_ORANI;
    }

    public void setMALZEME_HIZMET_MASRAF_DIGER_VERGI1_ORANI(double MALZEME_HIZMET_MASRAF_DIGER_VERGI1_ORANI) {
        this.MALZEME_HIZMET_MASRAF_DIGER_VERGI1_ORANI = MALZEME_HIZMET_MASRAF_DIGER_VERGI1_ORANI;
    }

    public double getMALZEME_HIZMET_MASRAF_DIGER_VERGI2_TUTARI() {
        return MALZEME_HIZMET_MASRAF_DIGER_VERGI2_TUTARI;
    }

    public void setMALZEME_HIZMET_MASRAF_DIGER_VERGI2_TUTARI(double MALZEME_HIZMET_MASRAF_DIGER_VERGI2_TUTARI) {
        this.MALZEME_HIZMET_MASRAF_DIGER_VERGI2_TUTARI = MALZEME_HIZMET_MASRAF_DIGER_VERGI2_TUTARI;
    }

    public double getMALZEME_HIZMET_MASRAF_DIGER_VERGI2_ORANI() {
        return MALZEME_HIZMET_MASRAF_DIGER_VERGI2_ORANI;
    }

    public void setMALZEME_HIZMET_MASRAF_DIGER_VERGI2_ORANI(double MALZEME_HIZMET_MASRAF_DIGER_VERGI2_ORANI) {
        this.MALZEME_HIZMET_MASRAF_DIGER_VERGI2_ORANI = MALZEME_HIZMET_MASRAF_DIGER_VERGI2_ORANI;
    }

    public double getMALZEME_HIZMET_MASRAF_TUTARI() {
        return MALZEME_HIZMET_MASRAF_TUTARI;
    }

    public void setMALZEME_HIZMET_MASRAF_TUTARI(double MALZEME_HIZMET_MASRAF_TUTARI) {
        this.MALZEME_HIZMET_MASRAF_TUTARI = MALZEME_HIZMET_MASRAF_TUTARI;
    }

    public double getMALZEME_HIZMET_MASRAF_TOPLAM_TUTARI() {
        return MALZEME_HIZMET_MASRAF_TOPLAM_TUTARI;
    }

    public void setMALZEME_HIZMET_MASRAF_TOPLAM_TUTARI(double MALZEME_HIZMET_MASRAF_TOPLAM_TUTARI) {
        this.MALZEME_HIZMET_MASRAF_TOPLAM_TUTARI = MALZEME_HIZMET_MASRAF_TOPLAM_TUTARI;
    }

    public String getMALZEME_HIZMET_MASRAF_ISTISNA_KODU() {
        return MALZEME_HIZMET_MASRAF_ISTISNA_KODU;
    }

    public void setMALZEME_HIZMET_MASRAF_ISTISNA_KODU(String MALZEME_HIZMET_MASRAF_ISTISNA_KODU) {
        this.MALZEME_HIZMET_MASRAF_ISTISNA_KODU = MALZEME_HIZMET_MASRAF_ISTISNA_KODU;
    }

    public String getMALZEME_HIZMET_MASRAF_ISTISNA_KODU_ACIKLAMA() {
        return MALZEME_HIZMET_MASRAF_ISTISNA_KODU_ACIKLAMA;
    }

    public void setMALZEME_HIZMET_MASRAF_ISTISNA_KODU_ACIKLAMA(String MALZEME_HIZMET_MASRAF_ISTISNA_KODU_ACIKLAMA) {
        this.MALZEME_HIZMET_MASRAF_ISTISNA_KODU_ACIKLAMA = MALZEME_HIZMET_MASRAF_ISTISNA_KODU_ACIKLAMA;
    }

    public String getMALZEME_HIZMET_MASRAF_TEVKIFAT_KODU() {
        return MALZEME_HIZMET_MASRAF_TEVKIFAT_KODU;
    }

    public void setMALZEME_HIZMET_MASRAF_TEVKIFAT_KODU(String MALZEME_HIZMET_MASRAF_TEVKIFAT_KODU) {
        this.MALZEME_HIZMET_MASRAF_TEVKIFAT_KODU = MALZEME_HIZMET_MASRAF_TEVKIFAT_KODU;
    }

    public String getMALZEME_HIZMET_MASRAF_TEVKIFAT_KODU_ACIKLAMA() {
        return MALZEME_HIZMET_MASRAF_TEVKIFAT_KODU_ACIKLAMA;
    }

    public void setMALZEME_HIZMET_MASRAF_TEVKIFAT_KODU_ACIKLAMA(String MALZEME_HIZMET_MASRAF_TEVKIFAT_KODU_ACIKLAMA) {
        this.MALZEME_HIZMET_MASRAF_TEVKIFAT_KODU_ACIKLAMA = MALZEME_HIZMET_MASRAF_TEVKIFAT_KODU_ACIKLAMA;
    }

    public int getMALZEME_HIZMET_MASRAF_TEVKIFAT_ORANI_PAY() {
        return MALZEME_HIZMET_MASRAF_TEVKIFAT_ORANI_PAY;
    }

    public void setMALZEME_HIZMET_MASRAF_TEVKIFAT_ORANI_PAY(int MALZEME_HIZMET_MASRAF_TEVKIFAT_ORANI_PAY) {
        this.MALZEME_HIZMET_MASRAF_TEVKIFAT_ORANI_PAY = MALZEME_HIZMET_MASRAF_TEVKIFAT_ORANI_PAY;
    }

    public int getMALZEME_HIZMET_MASRAF_TEVKIFAT_ORANI_PAYDA() {
        return MALZEME_HIZMET_MASRAF_TEVKIFAT_ORANI_PAYDA;
    }

    public void setMALZEME_HIZMET_MASRAF_TEVKIFAT_ORANI_PAYDA(int MALZEME_HIZMET_MASRAF_TEVKIFAT_ORANI_PAYDA) {
        this.MALZEME_HIZMET_MASRAF_TEVKIFAT_ORANI_PAYDA = MALZEME_HIZMET_MASRAF_TEVKIFAT_ORANI_PAYDA;
    }

    public String getRECORD_ID() {
        return RECORD_ID;
    }

    public void setRECORD_ID(String RECORD_ID) {
        this.RECORD_ID = RECORD_ID;
    }

    public String getTEKLIF_BASLIK_RECORD_ID() {
        return TEKLIF_BASLIK_RECORD_ID;
    }

    public void setTEKLIF_BASLIK_RECORD_ID(String TEKLIF_BASLIK_RECORD_ID) {
        this.TEKLIF_BASLIK_RECORD_ID = TEKLIF_BASLIK_RECORD_ID;
    }

    public String getERP_FIRMA_NUMBER() {
        return ERP_FIRMA_NUMBER;
    }

    public void setERP_FIRMA_NUMBER(String ERP_FIRMA_NUMBER) {
        this.ERP_FIRMA_NUMBER = ERP_FIRMA_NUMBER;
    }

    public int getSATIR_TURU() {
        return SATIR_TURU;
    }

    public void setSATIR_TURU(int SATIR_TURU) {
        this.SATIR_TURU = SATIR_TURU;
    }

    public int getBIRIM_CEVRIM_1() {
        return BIRIM_CEVRIM_1;
    }

    public void setBIRIM_CEVRIM_1(int BIRIM_CEVRIM_1) {
        this.BIRIM_CEVRIM_1 = BIRIM_CEVRIM_1;
    }

    public int getBIRIM_CEVRIM_2() {
        return BIRIM_CEVRIM_2;
    }

    public void setBIRIM_CEVRIM_2(int BIRIM_CEVRIM_2) {
        this.BIRIM_CEVRIM_2 = BIRIM_CEVRIM_2;
    }

    public int getPARA_BIRIMI() {
        return PARA_BIRIMI;
    }

    public void setPARA_BIRIMI(int PARA_BIRIMI) {
        this.PARA_BIRIMI = PARA_BIRIMI;
    }

    public double getKUR() {
        return KUR;
    }

    public void setKUR(double KUR) {
        this.KUR = KUR;
    }

    public Date getTESLIM_TARIHI() {
        return TESLIM_TARIHI;
    }

    public void setTESLIM_TARIHI(Date TESLIM_TARIHI) {
        this.TESLIM_TARIHI = TESLIM_TARIHI;
    }

    public String getMALZEME_HIZMET_MASRAF_ADI2() {
        return MALZEME_HIZMET_MASRAF_ADI2;
    }

    public void setMALZEME_HIZMET_MASRAF_ADI2(String MALZEME_HIZMET_MASRAF_ADI2) {
        this.MALZEME_HIZMET_MASRAF_ADI2 = MALZEME_HIZMET_MASRAF_ADI2;
    }

    public double getMUSTERI_SATIS_MIKTAR_YANIT() {
        return MUSTERI_SATIS_MIKTAR_YANIT;
    }

    public void setMUSTERI_SATIS_MIKTAR_YANIT(double MUSTERI_SATIS_MIKTAR_YANIT) {
        this.MUSTERI_SATIS_MIKTAR_YANIT = MUSTERI_SATIS_MIKTAR_YANIT;
    }

    public double getMalzemeHizmetMasrafTutariCarpiKur() {
        return malzemeHizmetMasrafTutariCarpiKur;
    }

    public void setMalzemeHizmetMasrafTutariCarpiKur(double malzemeHizmetMasrafTutariCarpiKur) {
        this.malzemeHizmetMasrafTutariCarpiKur = malzemeHizmetMasrafTutariCarpiKur;
    }

    public List<TedarikciListSatisSatirBazinda> getTedarikciListesi() {
        return tedarikciListesi;
    }

    public void setTedarikciListesi(List<TedarikciListSatisSatirBazinda> tedarikciListesi) {
        this.tedarikciListesi = tedarikciListesi;
    }

    public List<TedarikciListSatisSatirBazinda> getSecilenTedarikciListesi() {
        return secilenTedarikciListesi;
    }

    public void setSecilenTedarikciListesi(List<TedarikciListSatisSatirBazinda> secilenTedarikciListesi) {
        this.secilenTedarikciListesi = secilenTedarikciListesi;
    }

    public double getFiiliStok() {
        return fiiliStok;
    }

    public void setFiiliStok(double fiiliStok) {
        this.fiiliStok = fiiliStok;
    }

    public boolean isTeminTuru() {
        return teminTuru;
    }

    public void setTeminTuru(boolean teminTuru) {
        this.teminTuru = teminTuru;
    }

    public List<SATISTAN_TEDARIKCI_KAYITLARI> getSatistanTedarikciKayitlariList() {
        return satistanTedarikciKayitlariList;
    }

    public void setSatistanTedarikciKayitlariList(List<SATISTAN_TEDARIKCI_KAYITLARI> satistanTedarikciKayitlariList) {
        this.satistanTedarikciKayitlariList = satistanTedarikciKayitlariList;
    }

    public boolean isTeminTuruTersi() {
        return teminTuruTersi;
    }

    public void setTeminTuruTersi(boolean teminTuruTersi) {
        this.teminTuruTersi = teminTuruTersi;
    }

    public String getBirimIsim() {
        return birimIsim;
    }

    public void setBirimIsim(String birimIsim) {
        this.birimIsim = birimIsim;
    }

    public String getParaBirimiIsim() {
        return paraBirimiIsim;
    }

    public void setParaBirimiIsim(String paraBirimiIsim) {
        this.paraBirimiIsim = paraBirimiIsim;
    }

    public List<TanimliSatisFiyati> getTanimliSatisFiyatiList() {
        return tanimliSatisFiyatiList;
    }

    public void setTanimliSatisFiyatiList(List<TanimliSatisFiyati> tanimliSatisFiyatiList) {
        this.tanimliSatisFiyatiList = tanimliSatisFiyatiList;
    }

    public SonTeklifVeSatinAlmaSatisSatiri getSonTeklifVeSatinAlmaSatisSatiri() {
        return sonTeklifVeSatinAlmaSatisSatiri;
    }

    public void setSonTeklifVeSatinAlmaSatisSatiri(SonTeklifVeSatinAlmaSatisSatiri sonTeklifVeSatinAlmaSatisSatiri) {
        this.sonTeklifVeSatinAlmaSatisSatiri = sonTeklifVeSatinAlmaSatisSatiri;
    }

    public int getSECILEN_TEDARIKCI_ID() {
        return SECILEN_TEDARIKCI_ID;
    }

    public void setSECILEN_TEDARIKCI_ID(int SECILEN_TEDARIKCI_ID) {
        this.SECILEN_TEDARIKCI_ID = SECILEN_TEDARIKCI_ID;
    }

    public String getSecilenTedarikciKodu() {
        return secilenTedarikciKodu;
    }

    public void setSecilenTedarikciKodu(String secilenTedarikciKodu) {
        this.secilenTedarikciKodu = secilenTedarikciKodu;
    }

    public String getSecilenTedarikciUnvani() {
        return secilenTedarikciUnvani;
    }

    public void setSecilenTedarikciUnvani(String secilenTedarikciUnvani) {
        this.secilenTedarikciUnvani = secilenTedarikciUnvani;
    }

    public double getMUSTERI_MARJ() {
        return MUSTERI_MARJ;
    }

    public void setMUSTERI_MARJ(double MUSTERI_MARJ) {
        this.MUSTERI_MARJ = MUSTERI_MARJ;
    }

    public int getYeniTeklifSatirId() {
        return yeniTeklifSatirId;
    }

    public void setYeniTeklifSatirId(int yeniTeklifSatirId) {
        this.yeniTeklifSatirId = yeniTeklifSatirId;
    }
    
    public double getMALIYET_BIRIM_FIYAT() {
        return MALIYET_BIRIM_FIYAT;
    }

    public void setMALIYET_BIRIM_FIYAT(double MALIYET_BIRIM_FIYAT) {
        this.MALIYET_BIRIM_FIYAT = MALIYET_BIRIM_FIYAT;
    }

    public TedarikciListSatisSatirBazinda getSecilenTedarikci() {
        return secilenTedarikci;
    }

    public void setSecilenTedarikci(TedarikciListSatisSatirBazinda secilenTedarikci) {
        this.secilenTedarikci = secilenTedarikci;
    }    

    public List<TedarikciListSatisSatirBazinda> getTedarikciListesiFiltreleme() {
        return tedarikciListesiFiltreleme;
    }

    public void setTedarikciListesiFiltreleme(List<TedarikciListSatisSatirBazinda> tedarikciListesiFiltreleme) {
        this.tedarikciListesiFiltreleme = tedarikciListesiFiltreleme;
    }    

    public String getSECILEN_TEDARIKCI_ACIKLAMA() {
        return SECILEN_TEDARIKCI_ACIKLAMA;
    }

    public void setSECILEN_TEDARIKCI_ACIKLAMA(String SECILEN_TEDARIKCI_ACIKLAMA) {
        this.SECILEN_TEDARIKCI_ACIKLAMA = SECILEN_TEDARIKCI_ACIKLAMA;
    }    

    public List<TanimliSatinAlmaFiyati> getTanimliSatinAlmaFiyatiList() {
        return tanimliSatinAlmaFiyatiList;
    }

    public void setTanimliSatinAlmaFiyatiList(List<TanimliSatinAlmaFiyati> tanimliSatinAlmaFiyatiList) {
        this.tanimliSatinAlmaFiyatiList = tanimliSatinAlmaFiyatiList;
    }
    
}
