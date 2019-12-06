/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.beans.user;

import com.oms.dao.OfferUserDao;
import com.oms.dao.WhatsappSender;
import com.oms.models.TEKLIF_BASLIK;
import com.oms.models.TEKLIF_SATIR;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author ersan
 */
@ManagedBean(name="offerUserEnglishBean")
@SessionScoped
public class OfferUserEnglishBean {
    
    private String guid="";
    private TEKLIF_BASLIK teklifBaslik;
    private OfferUserDao offerUserDao;
    private List<TEKLIF_SATIR> teklifSatirlari;
    private boolean disabled;
    private TEKLIF_SATIR guncellenecekTeklifSatir;
    
    public void ilkCalisan(ComponentSystemEvent event)
    {
        if (!FacesContext.getCurrentInstance().isPostback())
        {
            try 
            {
                offerUserDao = new OfferUserDao();
                teklifBaslik = offerUserDao.getirTeklifBaslik(guid);
                
                if(teklifBaslik.getTEKLIF_DURUM_KODU() == 99)
                    teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("ACCEPTED");
                else if(teklifBaslik.getTEKLIF_DURUM_KODU() == 98)
                    teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("DENIED");
                else if(teklifBaslik.getTEKLIF_DURUM_KODU() == 97)
                    teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("REVISION REQUESTED");
                else if(teklifBaslik.getTEKLIF_DURUM_KODU() == 96)
                    teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("ACCEPTED (REVISED)");
                else if(teklifBaslik.getTEKLIF_DURUM_KODU() == 1)
                    teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("WAITING FOR APPROVAL");
                
                if(teklifBaslik.getTEKLIF_DURUM_KODU()>1)
                    disabled = true;
                else 
                    disabled = false;
                
                teklifSatirlari = offerUserDao.getirTeklifSatirlari(teklifBaslik.getRECORD_ID(),
                                                                    teklifBaslik.getERP_FIRMA_NUMBER()); 
                
            } 
            catch (Exception ex) 
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occured while starting page.", ""));
            }
        }
    }
    
    public void kabulEt() 
    {
      try 
        {   
            if(satirlardaRevizyonVarMi())
                offerUserDao.yanıtlaTeklif(96, "REVİZELİ KABUL EDİLDİ", guid);
            else
                offerUserDao.yanıtlaTeklif(99, "KABUL EDİLDİ", guid);
            
            /////////////////////////////////////////////////////////////
            //Teklif Satırlarının ve Başlığın Güncellendiği Bölüm...
            offerUserDao.guncelleTeklifSatirTumu(teklifSatirlari);
            offerUserDao.guncelleTeklifBaslik(teklifBaslik);
            //////////////////////////////////////////////////////////////
            
            offerUserDao.teklifeMusteriAciklamasiGir(teklifBaslik.getMUSTERI_YANIT(), guid);            
            teklifBaslik = offerUserDao.getirTeklifBaslik(guid);
            
            if(teklifBaslik.getTEKLIF_DURUM_KODU() == 99)
                teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("ACCEPTED");
            else if(teklifBaslik.getTEKLIF_DURUM_KODU() == 98)
                teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("DENIED");
            else if(teklifBaslik.getTEKLIF_DURUM_KODU() == 97)
                teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("REVISION REQUESTED");   
            else if(teklifBaslik.getTEKLIF_DURUM_KODU() == 96)
                teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("ACCEPTED (REVISED)");
            else if(teklifBaslik.getTEKLIF_DURUM_KODU() == 1)
                teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("WAITING FOR APPROVAL");
            
            teklifSatirlari = offerUserDao.getirTeklifSatirlari(teklifBaslik.getRECORD_ID(),
                                                                teklifBaslik.getERP_FIRMA_NUMBER());
            disabled = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Your response has been submitted successfully. Thanks for your response.", ""));
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occured while sending your offer response.", ""));
        }
    }
    
    public void reddet() 
    {
      try 
        {   
            offerUserDao.yanıtlaTeklif(98, "REDDEDİLDİ", guid);
            offerUserDao.teklifeMusteriAciklamasiGir(teklifBaslik.getMUSTERI_YANIT(), guid);
                        
            /////////////////////////////////////////////////////////////
            //Teklif Satırlarının ve Başlığın Güncellendiği Bölüm...
            offerUserDao.guncelleTeklifSatirTumu(teklifSatirlari);
            offerUserDao.guncelleTeklifBaslik(teklifBaslik);
            //////////////////////////////////////////////////////////////
            
            teklifBaslik = offerUserDao.getirTeklifBaslik(guid);
            
            if(teklifBaslik.getTEKLIF_DURUM_KODU() == 99)
                teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("ACCEPTED");
            else if(teklifBaslik.getTEKLIF_DURUM_KODU() == 98)
                teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("DENIED");
            else if(teklifBaslik.getTEKLIF_DURUM_KODU() == 97)
                teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("REVISION REQUESTED");  
            else if(teklifBaslik.getTEKLIF_DURUM_KODU() == 96)
                teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("ACCEPTED (REVISED)");
            else if(teklifBaslik.getTEKLIF_DURUM_KODU() == 1)
                teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("WAITING FOR APPROVAL");
            
            
            teklifSatirlari = offerUserDao.getirTeklifSatirlari(teklifBaslik.getRECORD_ID(),
                                                                teklifBaslik.getERP_FIRMA_NUMBER());
            disabled = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Your response has been submitted successfully. Thanks for your response.", ""));
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occured while sending your offer response.", ""));
        }
    }
    
    public void revizyonTalepEt() 
    {
      try 
        {   
            offerUserDao.yanıtlaTeklif(97, "REVİZYON TALEP EDİLDİ", guid);            
            offerUserDao.teklifeMusteriAciklamasiGir(teklifBaslik.getMUSTERI_YANIT(), guid);
            
            /////////////////////////////////////////////////////////////
            //Teklif Satırlarının ve Başlığın Güncellendiği Bölüm...
            offerUserDao.guncelleTeklifSatirTumu(teklifSatirlari);
            offerUserDao.guncelleTeklifBaslik(teklifBaslik);
            //////////////////////////////////////////////////////////////
            
            teklifBaslik = offerUserDao.getirTeklifBaslik(guid);
            
            if(teklifBaslik.getTEKLIF_DURUM_KODU() == 99)
                teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("ACCEPTED");
            else if(teklifBaslik.getTEKLIF_DURUM_KODU() == 98)
                teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("DENIED");
            else if(teklifBaslik.getTEKLIF_DURUM_KODU() == 97)
                teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("REVISION REQUESTED");
            else if(teklifBaslik.getTEKLIF_DURUM_KODU() == 96)
                teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("ACCEPTED (REVISED)");
            else if(teklifBaslik.getTEKLIF_DURUM_KODU() == 1)
                teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA("WAITING FOR APPROVAL");       
            
            teklifSatirlari = offerUserDao.getirTeklifSatirlari(teklifBaslik.getRECORD_ID(),
                                                                teklifBaslik.getERP_FIRMA_NUMBER());
            disabled = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Your response has been submitted successfully. Thanks for your response.", ""));
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occured while sending your offer response.", ""));
        }
    }
    
    public boolean satirlardaRevizyonVarMi()
    {
        try 
        {
            for(TEKLIF_SATIR teklifSatir : teklifSatirlari)
                if(teklifSatir.getMUSTERI_SATIS_MIKTAR_YANIT() != teklifSatir.getMALZEME_HIZMET_MASRAF_MIKTARI())
                    return true;
            
            //Eğer hepsi aynıysa...
            return false;
        } 
        catch (Exception e) 
        {
            return false;
        }
    }
    
    
    public void downloadPdf()
    {
        try {
          offerUserDao.downloadEnglish(teklifBaslik.getRECORD_ID(),
                                teklifBaslik.getTEKLIF_NUMARASI(),
                                teklifBaslik.getMusteriUnvani(),
                                teklifBaslik.getMusteriAdres(),
                                teklifBaslik.getMusteriUlke(),
                                teklifBaslik.getMusteriIl(),
                                teklifBaslik.getMusteriIlce(),
                                teklifBaslik.getTEKLIF_TUTARI(),
                                teklifBaslik.getTEKLIF_TOPLAM_INDIRIM_TUTARI(),
                                teklifBaslik.getTEKLIF_TOPLAM_TUTARI(),
                                teklifBaslik.getTEKLIF_PARA_BIRIMI(),
                                 "pdf");
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Raporlama sırasında hata oluştu.", ""));
        }
    }
    
    public void downloadXlsx()
    {
        try {
          offerUserDao.downloadEnglish(teklifBaslik.getRECORD_ID(),
                                    teklifBaslik.getTEKLIF_NUMARASI(),
                                    teklifBaslik.getMusteriUnvani(),
                                    teklifBaslik.getMusteriAdres(),
                                    teklifBaslik.getMusteriUlke(),
                                    teklifBaslik.getMusteriIl(),
                                    teklifBaslik.getMusteriIlce(),
                                    teklifBaslik.getTEKLIF_TUTARI(),
                                    teklifBaslik.getTEKLIF_TOPLAM_INDIRIM_TUTARI(),
                                    teklifBaslik.getTEKLIF_TOPLAM_TUTARI(),
                                    teklifBaslik.getTEKLIF_PARA_BIRIMI(),
                                     "xlsx");
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Raporlama sırasında hata oluştu.", ""));
        }
    }
    
    public void guncelleTeklifSatir(CellEditEvent event) 
    {
          try 
         {
            guncellenecekTeklifSatir = teklifSatirlari.get(event.getRowIndex());
            
            offerUserDao.guncelleTeklifSatir(guncellenecekTeklifSatir);
            
            //Güncelleme işleminden sonra tabloyu tekrar çağırıyoruz...
            teklifSatirlari = offerUserDao.getirTeklifSatirlari(teklifBaslik.getRECORD_ID(),
                                                                    teklifBaslik.getERP_FIRMA_NUMBER());
            //Güncellenen teklif satırının içerisini boşaltıyoruz...
            guncellenecekTeklifSatir = new TEKLIF_SATIR();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Teklif satırı başarıyla güncellenmiştir.", ""));
         } 
          catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklif satırı güncelleme işleminde hata meydana geldi.", ""));
         }
    }
    
    public void musteriMiktarDegisti(TEKLIF_SATIR teklifSatir)
    {
         teklifSatir.setMALZEME_HIZMET_MASRAF_TUTARI(
                    teklifSatir.getMALZEME_HIZMET_MASRAF_BIRIM_FIYATI() 
                  * teklifSatir.getMUSTERI_SATIS_MIKTAR_YANIT());
         
         if(teklifSatir.getKUR() <= 0.0)
                teklifSatir.setKUR(1.0);
            
         double tutar = 0.0;
            
        for(TEKLIF_SATIR teklifSatir1 :teklifSatirlari)
        {
            tutar = tutar + (teklifSatir1.getMALZEME_HIZMET_MASRAF_BIRIM_FIYATI() 
                             * teklifSatir1.getMUSTERI_SATIS_MIKTAR_YANIT() 
                             * teklifSatir1.getKUR());
        }

        teklifBaslik.setTEKLIF_TUTARI(tutar / teklifBaslik.getTEKLIF_KUR());
        teklifBaslik.setTeklifTutariCarpiKur(tutar);
    }
    
    
    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public TEKLIF_BASLIK getTeklifBaslik() {
        return teklifBaslik;
    }

    public void setTeklifBaslik(TEKLIF_BASLIK teklifBaslik) {
        this.teklifBaslik = teklifBaslik;
    }

    public OfferUserDao getOfferUserDao() {
        return offerUserDao;
    }

    public void setOfferUserDao(OfferUserDao offerUserDao) {
        this.offerUserDao = offerUserDao;
    }

    public List<TEKLIF_SATIR> getTeklifSatirlari() {
        return teklifSatirlari;
    }

    public void setTeklifSatirlari(List<TEKLIF_SATIR> teklifSatirlari) {
        this.teklifSatirlari = teklifSatirlari;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public TEKLIF_SATIR getGuncellenecekTeklifSatir() {
        return guncellenecekTeklifSatir;
    }

    public void setGuncellenecekTeklifSatir(TEKLIF_SATIR guncellenecekTeklifSatir) {
        this.guncellenecekTeklifSatir = guncellenecekTeklifSatir;
    }
}
