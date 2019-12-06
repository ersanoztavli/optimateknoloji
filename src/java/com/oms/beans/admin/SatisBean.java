/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.beans.admin;


import com.oms.dao.SatisDao;
import com.oms.models.FiiliStokSatirBazinda;
import com.oms.models.MAIL_DOSYA;
import com.oms.models.MUSTERI;
import com.oms.models.MailList;
import com.oms.models.SATISTAN_TEDARIKCI_KAYITLARI;
import com.oms.models.SatisSatiriAtanmisTedarikci;
import com.oms.models.SonTeklifVeSatinAlmaSatisSatiri;
import com.oms.models.TEKLIF_BASLIK;
import com.oms.models.TEKLIF_SATIR;
import com.oms.models.TanimliSatisFiyati;
import com.oms.models.TedarikciListSatisSatirBazinda;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.mail.BodyPart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.Part;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;


/**
 *
 * @author ersan
 */
@ManagedBean(name="satisBean", eager = true)
@SessionScoped
public class SatisBean implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private List<TEKLIF_BASLIK> teklifBasliklari;
    private List<TEKLIF_BASLIK> teklifBasliklariFiltreleme;
    
    private List<TEKLIF_BASLIK> gizlenenTeklifBasliklari;
    private List<TEKLIF_BASLIK> gizlenenTeklifBasliklariFiltreleme;
    private List<TEKLIF_BASLIK> secilenGizlenmisTeklifBaslikListesi;
    
    private List<TEKLIF_BASLIK> secilenTeklifBaslikListesi;
    private TEKLIF_BASLIK secilenTeklifBaslik = new TEKLIF_BASLIK();
    
    private List<TEKLIF_SATIR> teklifSatirlari;
    
    private SatisDao satisDao;      
    
    /////////////////////////////////////////
    
    private List<MailList> mailToList;
    private List<MailList> mailCcList;
    private List<MailList> mailBccList;
    
    /////////////////////////////////////////
    
    private List<MAIL_DOSYA> mailDosyaList;    
    private MAIL_DOSYA silinecekMailDosya;
    private Part yuklenecekDosya;
    
    /////////////////////////////////////////
    
    private SATISTAN_TEDARIKCI_KAYITLARI secilenSatistanTedarikciKayitlari;  
    private SATISTAN_TEDARIKCI_KAYITLARI teklifGirisineAcilacakSatistanTedarikciKayitlari;
    private TanimliSatisFiyati secilenTanimliSatisFiyati;
    private List<SatisSatiriAtanmisTedarikci> satisSatiriAtanmisTedarikciList;
    private SatisSatiriAtanmisTedarikci islemYapilanSatisSatiriAtanmisTedarikci;
    private MailList islemYapilanMailTo;
    private MailList eklenecekMailTo;
    private MAIL_DOSYA islemYapilanMailDosya;
    
    //Temin trünü
    private TEKLIF_SATIR secilenTeklifSatir = new TEKLIF_SATIR();
    
    //Revizyon için kullanılacak...
    private TEKLIF_BASLIK revizeEdilecekTeklifBaslik = new TEKLIF_BASLIK();
    private List<TEKLIF_SATIR> revizeEdilecekTeklifSatirlari;
    
    @ManagedProperty(value="#{sessionBean}")
    private SessionBean sessionBean;  
    
    @ManagedProperty(value="#{satisRevizyonBean}")
    private SatisRevizyonBean satisRevizyonBean;  
    
    @ManagedProperty(value="#{ayarlarBean}")
    private AyarlarBean ayarlarBean; 
    
    @PostConstruct
    public void init() 
    {        
        try 
        {
            secilenTeklifBaslik = new TEKLIF_BASLIK();
            revizeEdilecekTeklifBaslik = new TEKLIF_BASLIK();
            revizeEdilecekTeklifSatirlari = new ArrayList<>(); 
            teklifSatirlari = new ArrayList<>();   
            secilenTanimliSatisFiyati = new TanimliSatisFiyati();
            satisSatiriAtanmisTedarikciList = new ArrayList<>();
            
            FacesContext facesContext = FacesContext.getCurrentInstance();
            sessionBean = (SessionBean)facesContext.getApplication().createValueBinding("#{sessionBean}").getValue(facesContext);
            satisRevizyonBean = (SatisRevizyonBean)facesContext.getApplication().createValueBinding("#{satisRevizyonBean}").getValue(facesContext);

            satisDao = new SatisDao();
        } 
        catch (Exception ex) 
        {            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sayfa başlatılırken hata oluştu.", ""));
        }
    }
    
    public void ilkCalisan(ComponentSystemEvent event)
    {
        if (!FacesContext.getCurrentInstance().isPostback())
        {
            try 
            {                
                teklifBasliklari = satisDao.getirTeklifBasliklari(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
                
                secilenTeklifBaslikListesi = new ArrayList<>();
                
                teklifBasliklariFiltreleme = null; 
                secilenTanimliSatisFiyati = new TanimliSatisFiyati();
                satisSatiriAtanmisTedarikciList = new ArrayList<>();
            } 
            catch (Exception ex) 
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sayfa başlatılırken hata oluştu.", ""));
            }
        }
    }
    
     
    public void guncelleMailler()
    {
        try 
        {
            //Burada mailleri tekrar çekiyoruz ki yeni eklenen mailler de buraya gelsin...
            satisDao.guncelleMailler(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());  
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cari mailleri başarıyla güncellenmiştir.", ""));
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cari mailleri güncellenirken hata oluştu.", ""));
        }
        
    }
    
    public void satisTeklifleriniKaydetERPden() 
    {
        try 
        {
            String ERPFirmaNoTemp = sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER();
            
            //MÜŞTERİLERİN ÇEKİLDİĞİ YER...
            ////////////////////////////////////////////////////////////////////////////////////////////
            String enBuyukMusteriRecordId = satisDao.getirEnBuyukMusteriRecordId(ERPFirmaNoTemp);
            satisDao.kaydetMusterilerERPden(enBuyukMusteriRecordId, 
                                            sessionBean.getKullanici().getKULLANICI_ID(),
                                            sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
            ////////////////////////////////////////////////////////////////////////////////////////////
            
            //TEKLİF BAŞLIKLARININ ÇEKİLDİĞİ YER...
            ////////////////////////////////////////////////////////////////////////////////////////////                       
            
            String enBuyukTeklifBaslikRecordId = satisDao.getirEnBuyukTeklifBaslikRecordId(ERPFirmaNoTemp);
            satisDao.kaydetTeklifBaslikERPden(enBuyukTeklifBaslikRecordId, 
                                              sessionBean.getKullanici().getKULLANICI_ID(),
                                              sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER(),
                                              sessionBean.getKullanici().getON_DEGER_FIRMA_ID());
            ////////////////////////////////////////////////////////////////////////////////////////////
            
            //TEKLİF SATIRLARININ ÇEKİLDİĞİ YER...
            ////////////////////////////////////////////////////////////////////////////////////////////
            satisDao.kaydetTeklifSatirERPden(sessionBean.getKullanici().getKULLANICI_ID(),
                                             sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER(),
                                             enBuyukTeklifBaslikRecordId);
            ////////////////////////////////////////////////////////////////////////////////////////////
        
            teklifBasliklari = satisDao.getirTeklifBasliklari(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
           
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklifler ERP'den getirilirken hata oluştu.", ""));
        }        
    }
    
    public void satisTeklifleriniYenile() 
    {
        try 
        {            
            teklifBasliklari = satisDao.getirTeklifBasliklari(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());           
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklifler ERP'den getirilirken hata oluştu.", ""));
        }        
    }
    
    public String revizeEtSatisTeklifi() 
    {
        try 
        {       
            int revizeyonNumarasi = Integer.parseInt(revizeEdilecekTeklifBaslik.getGECERLI_TEKLIF_REVIZYON_NUMARASI());
            revizeyonNumarasi++;

            revizeEdilecekTeklifBaslik
                    .setGECERLI_TEKLIF_REVIZYON_NUMARASI(String.valueOf(revizeyonNumarasi));   

            revizeEdilecekTeklifSatirlari = satisDao.getirTeklifSatirlari(revizeEdilecekTeklifBaslik.getRECORD_ID(),
                                                        revizeEdilecekTeklifBaslik.getERP_FIRMA_NUMBER(),
                                                        sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
            
            List<SATISTAN_TEDARIKCI_KAYITLARI> satistanTedarikciKayitlariList = 
                    satisDao.getirSatistanTedarikciKayitlariSatirTumu(secilenTeklifBaslik.getRECORD_ID());

            for(TEKLIF_SATIR teklifSatir : teklifSatirlari)
            {
                for(SATISTAN_TEDARIKCI_KAYITLARI satistanTedarikciKayitlari : satistanTedarikciKayitlariList)
                {   
                    if(teklifSatir.getTEKLIF_SATIR_ID() == satistanTedarikciKayitlari.getTEKLIF_SATIR_ID()
                            && satistanTedarikciKayitlari.getSATISTAN_TEDARIKCI_KAYITLARI_ID() != 0)
                      teklifSatir.getSatistanTedarikciKayitlariList().add(satistanTedarikciKayitlari);
                }
            }
            
            satisRevizyonBean.setRevizeEdilecekTeklifBaslik(revizeEdilecekTeklifBaslik);
            satisRevizyonBean.setRevizeEdilecekTeklifSatirlari(revizeEdilecekTeklifSatirlari);
            
            return "/admin/pages/satisRevizyon.jsf?faces-redirect=true";
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Revizyon için teklif getirilirken hata oluştu.", ""));
            return "";
        }        
    }
    
 
    public String satisTeklifDetaylariniGetirERPden() 
    {
        try 
        {            
            secilenTeklifBaslik.setMusteriKarMarji(
                    satisDao.getirMusteriKarMarji(secilenTeklifBaslik.getERP_FIRMA_NUMBER(), 
                                                  secilenTeklifBaslik.getRECORD_ID()));
            
            if(secilenTeklifBaslik.getMUSTERI_MARJ()==0.0)
                secilenTeklifBaslik.setMUSTERI_MARJ(secilenTeklifBaslik.getMusteriKarMarji());
                
            
            teklifSatirlari = satisDao.getirTeklifSatirlari(secilenTeklifBaslik.getRECORD_ID(),
                                                            secilenTeklifBaslik.getERP_FIRMA_NUMBER(),
                                                            sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
            
            for(TEKLIF_SATIR teklifSatir : teklifSatirlari)
            {
                if(teklifSatir.getMUSTERI_MARJ()==0.0)
                    teklifSatir.setMUSTERI_MARJ(secilenTeklifBaslik.getMusteriKarMarji());
            }
            
            
            List<TedarikciListSatisSatirBazinda> tedarikciListSatisSatirBazindaList = 
                    satisDao.getirTedarikciListSatisSatirTumu(secilenTeklifBaslik.getERP_FIRMA_NUMBER(), secilenTeklifBaslik.getRECORD_ID());
            
            List<FiiliStokSatirBazinda> fiiliStokSatirBazindaList = 
                    satisDao.getirFiiliStokSatirTumu(secilenTeklifBaslik.getERP_FIRMA_NUMBER(), secilenTeklifBaslik.getRECORD_ID());
                       
            List<SATISTAN_TEDARIKCI_KAYITLARI> satistanTedarikciKayitlariList = 
                    satisDao.getirSatistanTedarikciKayitlariSatirTumu(secilenTeklifBaslik.getRECORD_ID());
            
            List<TanimliSatisFiyati> listeTanimliSatisFiyatiList = 
                    satisDao.getirListeTanimliSatisFiyati(secilenTeklifBaslik.getERP_FIRMA_NUMBER(), 
                                                          secilenTeklifBaslik.getRECORD_ID());
            
            List<TanimliSatisFiyati> musteriyeTanimliSatisFiyatiList = 
                    satisDao.getirMusteriyeTanimliSatisFiyati(secilenTeklifBaslik.getERP_FIRMA_NUMBER(), 
                                                              secilenTeklifBaslik.getRECORD_ID());
            
            List<TanimliSatisFiyati> listeTanimliVeMusteriyeTanimliSatisFiyatiList = new ArrayList<>();
            listeTanimliVeMusteriyeTanimliSatisFiyatiList.addAll(listeTanimliSatisFiyatiList);
            listeTanimliVeMusteriyeTanimliSatisFiyatiList.addAll(musteriyeTanimliSatisFiyatiList);
            
            int sayac = 0;
            
            for(TanimliSatisFiyati tanimliSatisFiyati : listeTanimliVeMusteriyeTanimliSatisFiyatiList)
            {
                tanimliSatisFiyati.setSanalId(++sayac);
            }
            
            List<SonTeklifVeSatinAlmaSatisSatiri> sonTeklifVeSatinAlmaSatisSatiriList = 
                    satisDao.getirSonTeklifVeSatinAlmaSatisSatiriIcin(secilenTeklifBaslik.getERP_FIRMA_NUMBER()
                                                                      ,secilenTeklifBaslik.getRECORD_ID());
            
                        
            for(TEKLIF_SATIR teklifSatir : teklifSatirlari)
            {
                teklifSatir.setTanimliSatisFiyatiList(new ArrayList<>());
                teklifSatir.setTedarikciListesi(new ArrayList<>());
                teklifSatir.setSatistanTedarikciKayitlariList(new ArrayList<>());
                teklifSatir.setSonTeklifVeSatinAlmaSatisSatiri(new SonTeklifVeSatinAlmaSatisSatiri());
                teklifSatir.setSecilenTedarikciListesi(new ArrayList<>());
                
                
                for(TedarikciListSatisSatirBazinda tedarikciListSatisSatirBazinda : tedarikciListSatisSatirBazindaList)
                {                
                    if(teklifSatir.getTEKLIF_SATIR_ID() == tedarikciListSatisSatirBazinda.getTeklifSatirId())
                    {
                        teklifSatir.getTedarikciListesi().add(tedarikciListSatisSatirBazinda);
                        
                        //Eğer öncelik bilgisi var ise seçili gelsin.
                        if(secilenTeklifBaslik.isOncelikliTedarikcilerGelsinMi())
                        {
                            if(tedarikciListSatisSatirBazinda.getOncelik()==1)
                            {
                              teklifSatir.getSecilenTedarikciListesi().add(tedarikciListSatisSatirBazinda);
                            }    
                        }                                               
                    }                      
                }
                
                for(FiiliStokSatirBazinda fiiliStokSatirBazinda : fiiliStokSatirBazindaList)
                {
                    if(teklifSatir.getTEKLIF_SATIR_ID() == fiiliStokSatirBazinda.getTeklifSatirId())
                    {
                        teklifSatir.setFiiliStok(fiiliStokSatirBazinda.getFiiliStok());
                        
                        //Stoktan karşılanacak demek ki...
                        if(teklifSatir.getFiiliStok() >= teklifSatir.getMALZEME_HIZMET_MASRAF_MIKTARI())
                        {
                            teklifSatir.setTeminTuru(true);
                            teklifSatir.setTeminTuruTersi(false);
                            
                            //Stoktan karşılanacağı için önceliği 
                            //olan tedarikçiler de gelmesin
                            teklifSatir.setSecilenTedarikciListesi(new ArrayList<>());
                        }
                        //Temin edilecek demek ki...
                        else
                        {
                            teklifSatir.setTeminTuru(false);
                            teklifSatir.setTeminTuruTersi(true);
                        }
                        
                        break;
                    }
                }
                
                for(SATISTAN_TEDARIKCI_KAYITLARI satistanTedarikciKayitlari : satistanTedarikciKayitlariList)
                {   
                    if(teklifSatir.getTEKLIF_SATIR_ID() == satistanTedarikciKayitlari.getTEKLIF_SATIR_ID()
                            && satistanTedarikciKayitlari.getSATISTAN_TEDARIKCI_KAYITLARI_ID() != 0)
                    {
                        teklifSatir.getSatistanTedarikciKayitlariList().add(satistanTedarikciKayitlari);
                    }                      
                }
                
                for(TanimliSatisFiyati tanimliSatisFiyati:listeTanimliVeMusteriyeTanimliSatisFiyatiList)
                {
                    if(teklifSatir.getTEKLIF_SATIR_ID() == tanimliSatisFiyati.getTeklifSatirId())
                        teklifSatir.getTanimliSatisFiyatiList().add(tanimliSatisFiyati);
                }
                
                for(SonTeklifVeSatinAlmaSatisSatiri sonTeklifVeSatinAlmaSatisSatiri : sonTeklifVeSatinAlmaSatisSatiriList)
                {
                    if(teklifSatir.getTEKLIF_SATIR_ID() == sonTeklifVeSatinAlmaSatisSatiri.getTeklifSatirId())
                    {
                        teklifSatir.setSonTeklifVeSatinAlmaSatisSatiri(sonTeklifVeSatinAlmaSatisSatiri);
                            break;
                    }                        
                }
            }
            
            return "/admin/pages/satisDetay.jsf?faces-redirect=true";
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklif detayları getirilirken hata oluştu.", ""));
            return "";
        }        
    }
    
    
    public void maliyetDegisti(TEKLIF_SATIR teklifSatir)
    {
        
         //maliyeti elle girdiğimiz için seçilen tedarikçi bilgisi silinecek
         teklifSatir.setSecilenTedarikciKodu(null);
         teklifSatir.setSecilenTedarikciUnvani(null);
         
         //////////////////////////////////////////////
         //Maliyeti hesaplıyoruz...
         //Eğer marj yok ise direk maliyet birim fiyata eşittir...
         if(teklifSatir.getMUSTERI_MARJ()==0.0)
            teklifSatir.setMALZEME_HIZMET_MASRAF_BIRIM_FIYATI(teklifSatir.getMALIYET_BIRIM_FIYAT()); 
         //Marj sıfır değil ise birimFiyat = maliyet*marj/100
         else
             teklifSatir
                     .setMALZEME_HIZMET_MASRAF_BIRIM_FIYATI(
                             teklifSatir.getMALIYET_BIRIM_FIYAT() +
                             (teklifSatir.getMALIYET_BIRIM_FIYAT()*teklifSatir.getMUSTERI_MARJ()/100)
                                            );         
         //////////////////////////////////////////////
            
         teklifSatir.setMALZEME_HIZMET_MASRAF_TUTARI(
                    teklifSatir.getMALZEME_HIZMET_MASRAF_BIRIM_FIYATI() 
                  * teklifSatir.getMUSTERI_SATIS_MIKTAR_YANIT());
         
         if(teklifSatir.getKUR() <= 0.0)
                teklifSatir.setKUR(1.0);
            
            teklifSatir.setMalzemeHizmetMasrafTutariCarpiKur(
                    teklifSatir.getMALZEME_HIZMET_MASRAF_TUTARI() * teklifSatir.getKUR());
            
         double tutar = 0.0;     
         
        for(TEKLIF_SATIR teklifSatir1 :teklifSatirlari)
        {
            tutar = tutar + (teklifSatir1.getMalzemeHizmetMasrafTutariCarpiKur());
        }

        secilenTeklifBaslik.setTEKLIF_TUTARI(tutar / secilenTeklifBaslik.getTEKLIF_KUR());
        secilenTeklifBaslik.setTeklifTutariCarpiKur(tutar);
    }
    
    public void tanimliSatisFiyatiDegisti(SelectEvent event) 
    {
        for(TEKLIF_SATIR teklifSatir :teklifSatirlari)
        {
          if(teklifSatir.getTEKLIF_SATIR_ID() == 
                  ((TanimliSatisFiyati) event.getObject()).getTeklifSatirId())
          {
            //Listeden seçim yaptığımız için tedarikçi kolonu bilgisini kaldırıyoruz...
            teklifSatir.setSecilenTedarikciKodu(null);
            teklifSatir.setSecilenTedarikciUnvani(null);
            
            teklifSatir.setMALIYET_BIRIM_FIYAT(((TanimliSatisFiyati) event.getObject()).getFiyat()) ;
            //////////////////////////////////////////////
            //Maliyeti hesaplıyoruz...
            //Eğer marj yok ise direk maliyet birim fiyata eşittir...
            if(teklifSatir.getMUSTERI_MARJ()==0.0)
               teklifSatir.setMALZEME_HIZMET_MASRAF_BIRIM_FIYATI(teklifSatir.getMALIYET_BIRIM_FIYAT()); 
            //Marj sıfır değil ise birimFiyat = maliyet*marj/100
            else
                teklifSatir
                        .setMALZEME_HIZMET_MASRAF_BIRIM_FIYATI(
                                teklifSatir.getMALIYET_BIRIM_FIYAT() +
                                (teklifSatir.getMALIYET_BIRIM_FIYAT()*teklifSatir.getMUSTERI_MARJ()/100)
                                               );         
            //////////////////////////////////////////////
            
            //teklifSatir.setMALZEME_HIZMET_MASRAF_BIRIM_FIYATI(((TanimliSatisFiyati) event.getObject()).getFiyat()) ;
            teklifSatir.setMALZEME_HIZMET_MASRAF_TUTARI(
                    teklifSatir.getMALZEME_HIZMET_MASRAF_BIRIM_FIYATI() 
                  * teklifSatir.getMUSTERI_SATIS_MIKTAR_YANIT());
            
            if(teklifSatir.getKUR() <= 0.0)
                teklifSatir.setKUR(1.0);
            
            teklifSatir.setMalzemeHizmetMasrafTutariCarpiKur(
                    teklifSatir.getMALZEME_HIZMET_MASRAF_TUTARI() * teklifSatir.getKUR());
            
            double tutar = 0.0;
            
            for(TEKLIF_SATIR teklifSatir1 :teklifSatirlari)
            {
                tutar = tutar + (teklifSatir1.getMalzemeHizmetMasrafTutariCarpiKur());
            }
            
            secilenTeklifBaslik.setTEKLIF_TUTARI(tutar / secilenTeklifBaslik.getTEKLIF_KUR());
            secilenTeklifBaslik.setTeklifTutariCarpiKur(tutar);
            
            break;
          }
        }
    }
    
    public void contextMenu(SelectEvent event) 
    {
        teklifGirisineAcilacakSatistanTedarikciKayitlari = (SATISTAN_TEDARIKCI_KAYITLARI) event.getObject();
    }
    
    public void satistanTedarikciKayitlariDegisti(SelectEvent event) 
    {
        for(TEKLIF_SATIR teklifSatir :teklifSatirlari)
        {
          if(teklifSatir.getTEKLIF_SATIR_ID() == 
                  ((SATISTAN_TEDARIKCI_KAYITLARI) event.getObject()).getTEKLIF_SATIR_ID())
          {
            teklifSatir.setSecilenTedarikciKodu(
                    ((SATISTAN_TEDARIKCI_KAYITLARI) event.getObject()).getMusteriMusteriKodu());
            teklifSatir.setSecilenTedarikciUnvani(
                    ((SATISTAN_TEDARIKCI_KAYITLARI) event.getObject()).getMusteriMusteriUnvani());
            
            if(teklifSatir.getKUR() <= 0.0)
                teklifSatir.setKUR(1.0);
            
//            teklifSatir
//                    .setMALZEME_HIZMET_MASRAF_BIRIM_FIYATI(
//                            (
//                            (teklifSatir.getMUSTERI_MARJ() / 100
//                            * ((SATISTAN_TEDARIKCI_KAYITLARI) event.getObject()).getTeklifSatirYanitBirimFiyat()
//                            + ((SATISTAN_TEDARIKCI_KAYITLARI) event.getObject()).getTeklifSatirYanitBirimFiyat())
//                            * ((SATISTAN_TEDARIKCI_KAYITLARI) event.getObject()).getMailAdresKur()
//                            ) / teklifSatir.getKUR());
            
            //Tedarikçinin verdiği fiyatı kur ile çarpıp TL'ye çeviriyoruz...
            //Daha sonra kendi kurumuza bölüp maliyeti hesaplıyoruz...
            teklifSatir
                    .setMALIYET_BIRIM_FIYAT(
                            ((SATISTAN_TEDARIKCI_KAYITLARI) event.getObject()).getTeklifSatirYanitBirimFiyat()
                            * ((SATISTAN_TEDARIKCI_KAYITLARI) event.getObject()).getMailAdresKur()
                            / teklifSatir.getKUR());
            
            //////////////////////////////////////////////
            //Maliyeti hesaplıyoruz...
            //Eğer marj yok ise direk maliyet birim fiyata eşittir...
            if(teklifSatir.getMUSTERI_MARJ()==0.0)
               teklifSatir.setMALZEME_HIZMET_MASRAF_BIRIM_FIYATI(teklifSatir.getMALIYET_BIRIM_FIYAT()); 
            //Marj sıfır değil ise birimFiyat = maliyet*marj/100
            else
                teklifSatir
                        .setMALZEME_HIZMET_MASRAF_BIRIM_FIYATI(
                                teklifSatir.getMALIYET_BIRIM_FIYAT() +
                                (teklifSatir.getMALIYET_BIRIM_FIYAT()*teklifSatir.getMUSTERI_MARJ()/100)
                                               );         
            //////////////////////////////////////////////
            
            teklifSatir
                    .setMALZEME_HIZMET_MASRAF_TUTARI(
                    (
                        (
                        (teklifSatir.getMUSTERI_MARJ() / 100
                        * ((SATISTAN_TEDARIKCI_KAYITLARI) event.getObject()).getTeklifSatirYanitBirimFiyat()
                        + ((SATISTAN_TEDARIKCI_KAYITLARI) event.getObject()).getTeklifSatirYanitBirimFiyat())
                        * ((SATISTAN_TEDARIKCI_KAYITLARI) event.getObject()).getMailAdresKur()
                        ) / teklifSatir.getKUR()
                    )
                    * teklifSatir.getMUSTERI_SATIS_MIKTAR_YANIT());
            
            teklifSatir.setMalzemeHizmetMasrafTutariCarpiKur(
                    teklifSatir.getMALZEME_HIZMET_MASRAF_TUTARI() * teklifSatir.getKUR());
                    
            double tutar = 0.0;
            
            for(TEKLIF_SATIR teklifSatir1 :teklifSatirlari)
            {
                tutar = tutar + (teklifSatir1.getMalzemeHizmetMasrafTutariCarpiKur());
            }
            
            secilenTeklifBaslik.setTEKLIF_TUTARI(tutar / secilenTeklifBaslik.getTEKLIF_KUR());
            secilenTeklifBaslik.setTeklifTutariCarpiKur(tutar);
            
            break;
          }
        }
    }
    
    public String mailPrepare() 
    {
        try 
        {            
            mailToList = new ArrayList<>();
            mailCcList = new ArrayList<>();
            mailBccList = new ArrayList<>();
            
//            if(secilenTeklifBaslik.getMusteriMail1() != "" &&
//                secilenTeklifBaslik.getMusteriMail1() != null)
//            {
//                String[] mails = secilenTeklifBaslik.getMusteriMail1().split(";");
//                for(String mail : mails)
//                {
//                    if(!mail.equals(""))
//                    {
//                        MailList mailList1 = new MailList(); 
//                        mailList1.setMailNumber(mail.trim());
//                        mailList1.setMailAdress(mail.trim());
//                        mailToList.add(mailList1);
//                    }
//                }
//            }
            
            if(secilenTeklifBaslik.getMusteriMail2() != "" &&
                secilenTeklifBaslik.getMusteriMail2() != null)
            {
                String[] mails2 = secilenTeklifBaslik.getMusteriMail2().split(";");
                for(String mail : mails2)
                {
                    if(!mail.equals(""))
                    {
                        MailList mailList1 = new MailList(); 
                        mailList1.setMailNumber(mail.trim());
                        mailList1.setMailAdress(mail.trim());
                        mailToList.add(mailList1);
                    }
                }
            }    
            
            //Bcc kısmı kendi mailimizi ekliyoruz ki kayıt bize de düşsün...
            MailList mailListBcc = new MailList(); 
            
            mailListBcc.setMailNumber(sessionBean.getKullanici().getMAIL_ADRESI());
            mailListBcc.setMailAdress(sessionBean.getKullanici().getMAIL_ADRESI());
            mailBccList.add(mailListBcc);
            
            //Önceden kalan dosya varsa diye temizliyoruz...
            mailDosyaList = new ArrayList<>();            
            return "/admin/pages/satisDetayGonder.jsf?faces-redirect=true";
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(SatisBean.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }    
    
    public void downloadPdf()
    {
        try {
          satisDao.download(secilenTeklifBaslik.getRECORD_ID(),
                            secilenTeklifBaslik.getTEKLIF_NUMARASI(),
                            secilenTeklifBaslik.getMusteriUnvani(),
                            secilenTeklifBaslik.getMusteriAdres(),
                            secilenTeklifBaslik.getMusteriUlke(),
                            secilenTeklifBaslik.getMusteriIl(),
                            secilenTeklifBaslik.getMusteriIlce(),
                            secilenTeklifBaslik.getTEKLIF_TUTARI(),
                            secilenTeklifBaslik.getTEKLIF_TOPLAM_INDIRIM_TUTARI(),
                            secilenTeklifBaslik.getTEKLIF_TOPLAM_TUTARI(),
                            secilenTeklifBaslik.getTEKLIF_PARA_BIRIMI(),
                            "pdf");
        } catch (Exception ex) {
            Logger.getLogger(SatisBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void downloadXlsx()
    {
        try {
          satisDao.download(secilenTeklifBaslik.getRECORD_ID(),
                            secilenTeklifBaslik.getTEKLIF_NUMARASI(),
                            secilenTeklifBaslik.getMusteriUnvani(),
                            secilenTeklifBaslik.getMusteriAdres(),
                            secilenTeklifBaslik.getMusteriUlke(),
                            secilenTeklifBaslik.getMusteriIl(),
                            secilenTeklifBaslik.getMusteriIlce(),
                            secilenTeklifBaslik.getTEKLIF_TUTARI(),
                            secilenTeklifBaslik.getTEKLIF_TOPLAM_INDIRIM_TUTARI(),
                            secilenTeklifBaslik.getTEKLIF_TOPLAM_TUTARI(),
                            secilenTeklifBaslik.getTEKLIF_PARA_BIRIMI(),
                            "xlsx");
        } catch (Exception ex) {
            Logger.getLogger(SatisBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void silMailDosya() 
    {
         try 
         {            
            mailDosyaList.remove(silinecekMailDosya);   
         } 
         catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dosya silme işleminde hata meydana geldi.", ""));
         }
    }
            
    public void temizleTedarikciDogrudanTemin()
    {
         try 
         {  
            for(TEKLIF_SATIR teklifSatir :teklifSatirlari)
            {
              if(teklifSatir.getTEKLIF_SATIR_ID() == secilenTeklifSatir.getTEKLIF_SATIR_ID())
              {
                teklifSatir.setSecilenTedarikciKodu(null);
                teklifSatir.setSecilenTedarikciUnvani(null);
              }
            } 
         } 
         catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tedarikçi temizlenirken hata oluştu.", ""));
         }
    }
        
    public void guncelleSatirTedarikcileri()
    {
         try 
         {              
            List<TedarikciListSatisSatirBazinda> tedarikciListSatisSatirBazindaList = 
                    satisDao.getirTedarikciListSatisSatiriTek(secilenTeklifBaslik.getERP_FIRMA_NUMBER(), secilenTeklifSatir.getTEKLIF_SATIR_ID());
            
            //Seçilen Listeyi önce boşaltıyoruz...
            secilenTeklifSatir.setSecilenTedarikciListesi(new ArrayList<>());
            //Daha sonra tekrar seçebilmesi için yeni açılabilir listeyi atıyoruz...
            secilenTeklifSatir.setTedarikciListesi(tedarikciListSatisSatirBazindaList);
              
         } 
         catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tedarikçi temizlenirken hata oluştu.", ""));
         }
    }
    
    
    public void onIslemeDogrudanTemin()
    {
         try 
         {  
             secilenTeklifSatir.setTedarikciListesiFiltreleme(null);
         } 
         catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tedarikçiler listelenirken hata oluştu.", ""));
         }
    }
             
    public void kaydetYeniDosya()
    {
         try 
         {  
            MAIL_DOSYA mailDosya = new MAIL_DOSYA();
            mailDosya.setMAIL_DOSYA_ADI(yuklenecekDosya.getSubmittedFileName().substring(
                                                        yuklenecekDosya.getSubmittedFileName().lastIndexOf("\\") + 1));
            //mailDosya.setMailDosyaAdiUzun(yuklenecekDosya.getSubmittedFileName());
            mailDosya.setBoyut(String.valueOf(yuklenecekDosya.getSize() / 1000) + " KB");
            mailDosya.setIcerik(yuklenecekDosya.getInputStream());
            
            mailDosyaList.add(mailDosya); 
         } 
         catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Adres ekleme işleminde hata meydana geldi.", ""));
         }
    }
    
    public void kaydetYeniDosyaSatinAlma(FileUploadEvent event)
    {
         try 
         {  
            MAIL_DOSYA mailDosya = new MAIL_DOSYA();
            mailDosya.setMAIL_DOSYA_ADI(event.getFile().getFileName());
            //mailDosya.setMailDosyaAdiUzun(event.getFile().get);
            mailDosya.setBoyut(String.valueOf(event.getFile().getSize() / 1000) + " KB");
            mailDosya.setIcerik((InputStream)event.getFile().getInputstream());
            
            SatisSatiriAtanmisTedarikci satisSatiriAtanmisTedarikci = (SatisSatiriAtanmisTedarikci)event.getComponent().getAttributes().get("satisSatiriAtanmisTedarikci");
            satisSatiriAtanmisTedarikci.getMailDosyaList().add(mailDosya);
         } 
         catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Adres ekleme işleminde hata meydana geldi.", ""));
         }
    }
    
    public void silMailDosyaSatinAlma() 
    {
         try 
         {            
            islemYapilanSatisSatiriAtanmisTedarikci.getMailDosyaList().remove(islemYapilanMailDosya);   
         
            islemYapilanSatisSatiriAtanmisTedarikci = new SatisSatiriAtanmisTedarikci();
            islemYapilanMailDosya = new MAIL_DOSYA();   
         } 
         catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dosya silme işleminde hata meydana geldi.", ""));
         }
    }
    
    public void degistirSatirTeminTuru(TEKLIF_SATIR teklifSatir) 
    {
         try 
         {          
            int index = teklifSatirlari.indexOf(teklifSatir);
            TEKLIF_SATIR teklifSatirTemp = teklifSatir;
            
            teklifSatirlari.remove(index); 
             
            if(teklifSatirTemp.isTeminTuru() == true)
                {
                    teklifSatirTemp.setTeminTuru(false);    
                    teklifSatirTemp.setTeminTuruTersi(true);
                }
            else 
                {
                    teklifSatirTemp.setTeminTuru(true);    
                    teklifSatirTemp.setTeminTuruTersi(false);
                }
            
            teklifSatirlari.add(index, teklifSatirTemp);
            
         } 
         catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Temin türü değiştirme işleminde hata meydana geldi.", ""));
         }
    }
    
    public String satistanSatinAlmaTeklifiOlustur() 
    {
         try 
         {   
             int kontrol = 0;
             //Burada tedarikçi seçilip seçilmediğini kontrol ediyoruz...
             for(TEKLIF_SATIR teklifSatir : teklifSatirlari)
               if(teklifSatir.getSecilenTedarikciListesi() != null)
                   if(teklifSatir.getSecilenTedarikciListesi().size()>0)
                      kontrol++;
             
             
             if(kontrol>0)
             {
                    List<String> musteriRecordIdList = new ArrayList<>();
                    satisSatiriAtanmisTedarikciList = new ArrayList<>();

                    //Burada önce tedarikçileri seçiyoruz...
                    //Distinct gibi bir şey yani
                    for(TEKLIF_SATIR teklifSatir : teklifSatirlari)
                    {
                        if(teklifSatir.getSecilenTedarikciListesi() != null) 
                           {
                               for(TedarikciListSatisSatirBazinda tedarikci : teklifSatir.getSecilenTedarikciListesi())
                               {
                                   if(!musteriRecordIdList.contains(tedarikci.getMusteriRecordId()))
                                       musteriRecordIdList.add(tedarikci.getMusteriRecordId());
                               }
                           }
                    }

                    //Daha sonra tüm tedarikçiler için satırları dolaşıyoruz
                    //ki mail gönderirken hangi teklifi hangi ürünlerle hangi tedarikçiye birlikte göndereceğimizi anlayalım...
                    for(String musteriRecordId : musteriRecordIdList)
                    {
                        //Herbir tedarikçi için yenileme yapıyoruz...
                        SatisSatiriAtanmisTedarikci satisSatiriAtanmisTedarikci = new SatisSatiriAtanmisTedarikci();
                        satisSatiriAtanmisTedarikci.setTeklifSatirIdList(new ArrayList<>());

                         for(TEKLIF_SATIR teklifSatir : teklifSatirlari)
                        {
                           if(teklifSatir.getSecilenTedarikciListesi() != null) 
                           {
                               for(TedarikciListSatisSatirBazinda tedarikci : teklifSatir.getSecilenTedarikciListesi())
                               {
                                   //Eğer eşleşme varsa listeye satırı ekle
                                   if(tedarikci.getMusteriRecordId().equals(musteriRecordId))
                                   {                             
                                       satisSatiriAtanmisTedarikci.setMusteriRecordId(musteriRecordId);                          
                                       satisSatiriAtanmisTedarikci.getTeklifSatirIdList().add(teklifSatir.getTEKLIF_SATIR_ID());

                                   }
                               }
                           }                     
                        }

                       //Yani bu tedarikçiye satırlar içerisinde en az bir kere rastlanmışsa
                       //listeye atama yap...
                       if(!satisSatiriAtanmisTedarikci.getMusteriRecordId().isEmpty())
                       satisSatiriAtanmisTedarikciList.add(satisSatiriAtanmisTedarikci);
                    }   


                    //Her bir tedarikçi için ayrı ayrı setleme yapıyoruz...
                    for(SatisSatiriAtanmisTedarikci satisSatiriAtanmisTedarikci  :satisSatiriAtanmisTedarikciList)
                    {
                        satisSatiriAtanmisTedarikci.setMailToList(new ArrayList<>());
                        satisSatiriAtanmisTedarikci.setMailDosyaList(new ArrayList<>());

                        satisSatiriAtanmisTedarikci.setMailGonderen(sessionBean.getKullanici().getMAIL_ADRESI());

                        //Mail adreslerini alabilmek için çektim...
                        MUSTERI musteri = satisDao.getirMusteriRecorIdIle(satisSatiriAtanmisTedarikci.getMusteriRecordId());                 
                        satisSatiriAtanmisTedarikci.setTedarikciUnvan(musteri.getMUSTERI_UNVANI());
       //                 if(musteri.getMUSTERI_MAIL_ADRESI1()!= "" && musteri.getMUSTERI_MAIL_ADRESI1() != null)
       //                  {
       //                    String[] mails = musteri.getMUSTERI_MAIL_ADRESI1().split(";");
       //                    for(String mail : mails)
       //                    {
       //                        if(!mail.equals(""))
       //                        {
       //                            MailList mailList1 = new MailList(); 
       //                            mailList1.setMailNumber(mail.trim());
       //                            mailList1.setMailAdress(mail.trim());
       //                            satisSatiriAtanmisTedarikci.getMailToList().add(mailList1);
       //                        }
       //                    }
       //                  }

                        if(musteri.getMUSTERI_MAIL_ADRESI2() != "" && musteri.getMUSTERI_MAIL_ADRESI2() != null)
                        {
                           String[] mails2 = musteri.getMUSTERI_MAIL_ADRESI2().split(";");
                           for(String mail : mails2)
                           {
                               if(!mail.equals(""))
                               {
                                   MailList mailList1 = new MailList(); 
                                   mailList1.setMailNumber(mail.trim());
                                   mailList1.setMailAdress(mail.trim());
                                   satisSatiriAtanmisTedarikci.getMailToList().add(mailList1);
                               }
                           }
                        }
                    }

                    islemYapilanSatisSatiriAtanmisTedarikci = new SatisSatiriAtanmisTedarikci();
                    islemYapilanMailTo = new MailList();
                    eklenecekMailTo = new MailList();
                    eklenecekMailTo.setMailAdress("");
                    islemYapilanMailDosya = new MAIL_DOSYA();

                    //mailGonderTedarikcilere(satisSatiriAtanmisTedarikciList);

                    return "/admin/pages/satistanSatinAlma.jsf?faces-redirect=true";
             }
             
             else
             {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Lütfen en az bir tane tedarikçi seçiniz.", ""));
                return "";
             }   
         } 
         catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Satın alma teklifi oluşturma işleminde hata meydana geldi.", ""));
           return "";
         }
    }    
    
    public void silMailTo() 
    {
         try 
         {            
            islemYapilanSatisSatiriAtanmisTedarikci.getMailToList().remove(islemYapilanMailTo);   
         
            islemYapilanSatisSatiriAtanmisTedarikci = new SatisSatiriAtanmisTedarikci();
            islemYapilanMailTo = new MailList();
         } 
         catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Adres silme işleminde hata meydana geldi.", ""));
         }
    }
    
    public void kaydetYeniMailTo()
    {
         try 
         {  
            eklenecekMailTo.setMailNumber(eklenecekMailTo.getMailAdress());
            satisSatiriAtanmisTedarikciList
                    .get(satisSatiriAtanmisTedarikciList.indexOf(islemYapilanSatisSatiriAtanmisTedarikci))
                    .getMailToList().add(eklenecekMailTo);   

            islemYapilanSatisSatiriAtanmisTedarikci = new SatisSatiriAtanmisTedarikci();
            eklenecekMailTo = new MailList();
         } 
         catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Adres ekleme işleminde hata meydana geldi.", ""));
         }
    }
    
    
    
    public void mailGonderTedarikcilere() 
    {
      try 
        {  
                int kontrol = 0;
                
                for(SatisSatiriAtanmisTedarikci satisSatiriAtanmisTedarikci :satisSatiriAtanmisTedarikciList)
                {
                    //Gönderen kısmı boş mu diye bakıyoruz...
                    if(satisSatiriAtanmisTedarikci.getMailGonderen()==null)
                        kontrol++;                    
                    if(satisSatiriAtanmisTedarikci.getMailGonderen()!=null)
                        if(satisSatiriAtanmisTedarikci.getMailGonderen().isEmpty())
                            kontrol++;
                    
                    //Konu kısmı boş mu diye bakıyoruz...
                    if(satisSatiriAtanmisTedarikci.getMailKonu()==null)
                        kontrol++;                    
                    if(satisSatiriAtanmisTedarikci.getMailKonu()!=null)
                        if(satisSatiriAtanmisTedarikci.getMailKonu().isEmpty())
                            kontrol++;
                    
                    //İçerik kısmı boş mu diye bakıyoruz...
                    if(satisSatiriAtanmisTedarikci.getMailIcerik()==null)
                        kontrol++;                    
                    if(satisSatiriAtanmisTedarikci.getMailIcerik()!=null)
                        if(satisSatiriAtanmisTedarikci.getMailIcerik().isEmpty())
                            kontrol++;
                    
                    //En az bir tane tedarikçi maili seçilmesini istiyoruz...
                    if(satisSatiriAtanmisTedarikci.getMailToList()==null)
                        kontrol++;
                    if(satisSatiriAtanmisTedarikci.getMailToList()!=null)
                        if(satisSatiriAtanmisTedarikci.getMailToList().size()==0)
                             kontrol++;
                }
                
                if(kontrol==0)
                {
                    for(SatisSatiriAtanmisTedarikci satisSatiriAtanmisTedarikci :satisSatiriAtanmisTedarikciList)
                    {
                        //Önce veri tabanına mail'i ve adresleri kaydediyoruz...
                        satisDao.kaydetMailSatinAlma(satisSatiriAtanmisTedarikci.getMailGonderen()
                                                    , satisSatiriAtanmisTedarikci.getMailKonu()
                                                    , satisSatiriAtanmisTedarikci.getMailIcerik()
                                                    , secilenTeklifBaslik.getTEKLIF_BASLIK_ID()
                                                    , satisSatiriAtanmisTedarikci.getMusteriRecordId()
                                                    , sessionBean.getKullanici().getKULLANICI_ID());


                        //Daha sonra her bir tedarikçi için TEKLIF_SATIR_YANIT tablosuna kayıt açıyoruz ki
                        //Tedarikçiler kendi değişkenlerini satırlara girebilsin...
                        satisDao.kaydetTeklifSatirYanitTedarikci(satisDao.getirMailAdreslerTedarikci(
                                                                     satisDao.getirMailIdTeklifBaslikIdIleTedarikci(
                                                                         secilenTeklifBaslik.getTEKLIF_BASLIK_ID())
                                                                                                    )
                                                                ,satisSatiriAtanmisTedarikci.getTeklifSatirIdList()
                                                                ,sessionBean.getKullanici().getKULLANICI_ID()
                                                                ,sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());


                        //////////////////////////////////////////////////////////////////////////
                        //Daha sonra veri tabanından mailAdresId'yi çekip link ile gönderiyoruz...

                        Properties props = new Properties();
                        props.put("mail.smtp.auth", "true");
                       // props.put("mail.smtp.starttls.enable", "true");
                        props.put("mail.smtp.host", ayarlarBean.getEmailAyar().getEMAIL_SUNUCU_ADI());
                        props.put("mail.smtp.port", ayarlarBean.getEmailAyar().getEMAIL_SUNUCU_PORT_NUMARASI());

                        Session session = Session.getInstance(props,
                          new javax.mail.Authenticator() 
                          {
                                protected PasswordAuthentication getPasswordAuthentication() 
                                {
                                   return new PasswordAuthentication(ayarlarBean.getEmailAyar().getEMAIL_ADRES()
                                                                    , ayarlarBean.getEmailAyar().getEMAIL_SIFRE());
                                }
                          });



                        MimeMessage message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(satisSatiriAtanmisTedarikci.getMailGonderen()));
                        message.setSubject(satisSatiriAtanmisTedarikci.getMailKonu());   

                        MimeMultipart multipart = new MimeMultipart();
                        /////////////////////////////////////////

                        //Mail'in attachment kısmı
                        for(MAIL_DOSYA mailDosya : satisSatiriAtanmisTedarikci.getMailDosyaList())
                        {
                            MimeBodyPart attachment = new MimeBodyPart();

                            DataSource source=new ByteArrayDataSource(mailDosya.getIcerik(),"application/octet-stream");

                            attachment.setDataHandler(new DataHandler(source));
                            attachment.setDisposition(javax.mail.Part.ATTACHMENT);
                            attachment.setFileName(mailDosya.getMAIL_DOSYA_ADI());
                            multipart.addBodyPart(attachment);
                        }


                        String mailAlacaklarTo = "";
                        String mailAlacaklarBcc = "";

                        for (MailList mailTo : satisSatiriAtanmisTedarikci.getMailToList())  
                        { 
                            mailAlacaklarTo = mailAlacaklarTo + mailTo.getMailAdress() + ",";
                        }

                        mailAlacaklarBcc = mailAlacaklarBcc + satisSatiriAtanmisTedarikci.getMailGonderen() + ",";

                        InternetAddress[] parseMailTo = InternetAddress.parse(mailAlacaklarTo , true);
                        InternetAddress[] parseMailBcc = InternetAddress.parse(mailAlacaklarBcc , true);

                        //Kime
                        message.setRecipients(javax.mail.Message.RecipientType.TO,  parseMailTo);
                        //Blind Carbon Copy
                        message.setRecipients(javax.mail.Message.RecipientType.BCC,  parseMailBcc);

                        String mailLink = "";
                        String mailLinkBase = satisSatiriAtanmisTedarikci.getMailIcerik()
                                            + "\n\n"
                                            + "http://" 
                                            + ayarlarBean.getSessionBean().getServerConfig().getString("serverNameOrIp") 
                                            + ":" 
                                            + ayarlarBean.getSessionBean().getServerConfig().getString("serverPort") 
                                            + "/OMS/user/offerUserSatinAlma.jsf";

                        mailLink = mailLinkBase + "?mailAdresId=" 
                                                + satisDao.getirMailAdresId(satisSatiriAtanmisTedarikci.getMusteriRecordId());

                        //Mail'in içerik kısmı
                        BodyPart icerik = new MimeBodyPart();  
                        icerik.setText(mailLink);
                        multipart.addBodyPart(icerik); 
                        message.setContent(multipart);

                        Transport.send(message);
                    }            
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Tedarikçilere mailler başarıyla gönderilmiştir.", ""));
                }                
                
                else
                   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Lütfen mail bilgilerini kontrol ediniz", ""));
                
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklif mail'i gönderilirken hata oluştu.", ""));
        }
    }
    
    public void digerlerineUygula(SatisSatiriAtanmisTedarikci secilenSatisSatiriAtanmisTedarikci) 
    {
        try 
        {     
            for(SatisSatiriAtanmisTedarikci satisSatiriAtanmisTedarikci : satisSatiriAtanmisTedarikciList)
            {
                satisSatiriAtanmisTedarikci.setMailGonderen(secilenSatisSatiriAtanmisTedarikci.getMailGonderen());
                satisSatiriAtanmisTedarikci.setMailKonu(secilenSatisSatiriAtanmisTedarikci.getMailKonu());
                satisSatiriAtanmisTedarikci.setMailIcerik(secilenSatisSatiriAtanmisTedarikci.getMailIcerik());
            }
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Diğerlerine uygulama işleminde hata oluştu.", ""));
        }        
    }
    
    public String excel() 
    {
        try 
        {     
            
            return "/admin/pages/excelSatisTeklifi.jsf?faces-redirect=true";
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Excel'den yükleme sayfasına yönlendirirken hata oluştu.", ""));
            return "";
        }        
    }
    
    public void satisTeklifleriniGizle() 
    {
        try 
        {
            if(secilenTeklifBaslikListesi!=null)
                if(!secilenTeklifBaslikListesi.isEmpty()) 
                {                
                    satisDao.gizleTeklifListesi(sessionBean.getKullanici().getKULLANICI_ID(),
                                                    secilenTeklifBaslikListesi);
                    teklifBasliklari = satisDao.getirTeklifBasliklari(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, String.valueOf(secilenTeklifBaslikListesi.size()) + " adet teklif başarıyla gizlenmiştir.", ""));
                }
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklifler gizlenirken hata oluştu.", ""));
        }        
    }
    
    public void satisTeklifleriniGizleOnIsleme() 
    {
        try 
        {
            if(secilenTeklifBaslikListesi!=null)
                if(secilenTeklifBaslikListesi.isEmpty())    
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Lütfen gizlenecek teklif/teklifleri seçiniz!", ""));

        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklifler gizlenirken hata oluştu.", ""));
        }        
    }
    
    public void gizlenenSatisTeklifleriniGetir() 
    {
        try 
        {
           gizlenenTeklifBasliklari = satisDao.getirTeklifBasliklariGizlenen(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
           gizlenenTeklifBasliklariFiltreleme = null;
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Gizlenen satın alma teklifleri listelenirken hata oluştu.", ""));
        }        
    }
    
    public void gizlenenSatinAlmaTeklifleriniGetir() 
    {
        try 
        {
           gizlenenTeklifBasliklari = satisDao.getirTeklifBasliklariGizlenen(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
           gizlenenTeklifBasliklariFiltreleme = null;
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Gizlenen satın alma teklifleri listelenirken hata oluştu.", ""));
        }        
    }
    
    public void tedarikciSecildiDogrudanTemin(SelectEvent event) 
    {
          try 
        {            
            for(TEKLIF_SATIR teklifSatir :teklifSatirlari)
            {
              if(teklifSatir.getTEKLIF_SATIR_ID() == 
                      ((TedarikciListSatisSatirBazinda) event.getObject()).getTeklifSatirId())
              {
                teklifSatir.setSecilenTedarikciKodu(
                        ((TedarikciListSatisSatirBazinda) event.getObject()).getMusteriKodu());
                teklifSatir.setSecilenTedarikciUnvani(
                        ((TedarikciListSatisSatirBazinda) event.getObject()).getMusteriUnvani());
              }
            }            
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Gizlenen satın alma teklifleri listelenirken hata oluştu.", ""));
        }   
    }
    
    public void satisTeklifleriniGeriGetirOnIsleme() 
    {
        try 
        {
            if(secilenGizlenmisTeklifBaslikListesi!=null)
                if(secilenGizlenmisTeklifBaslikListesi.isEmpty())    
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Lütfen geri getirilecek teklif/teklifleri seçiniz!", ""));

        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklifler geri getirilirken hata oluştu.", ""));
        }        
    }
    
    public void satisTeklifleriniGeriGetir() 
    {
        try 
        {
            if(secilenGizlenmisTeklifBaslikListesi!=null)
                if(!secilenGizlenmisTeklifBaslikListesi.isEmpty()) 
                {                
                    satisDao.geriGetirTeklifListesi(sessionBean.getKullanici().getKULLANICI_ID(),
                                                        secilenGizlenmisTeklifBaslikListesi);
                    gizlenenTeklifBasliklari = satisDao.getirTeklifBasliklariGizlenen(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
                    teklifBasliklari = satisDao.getirTeklifBasliklari(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
                    gizlenenTeklifBasliklariFiltreleme = null;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, String.valueOf(secilenGizlenmisTeklifBaslikListesi.size()) + " adet teklif başarıyla geri getirilmiştir.", ""));
                }
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklifler geri getirilirken hata oluştu.", ""));
        }        
    }
    
    public void teklifGirisineAc() 
    {
        try 
        {    
            satisDao.veriGirisineAcMailAdresIdIle(teklifGirisineAcilacakSatistanTedarikciKayitlari.getTeklifSatirYanitMailAdresId());
            teklifGirisineAcilacakSatistanTedarikciKayitlari.setMailAdresTamamlandiMiBoolean(false);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Teklif girişine açma işlemi başarıyla tamamlanmıştır.\n Tedarikçi teklif girişine devam edebilir.", ""));
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.toString(), ex.getMessage()));
        }        
    }
    
    public void kaydetSatisTeklifi() 
    {
        try 
        {    
            satisDao.kaydetSatisTeklifi(secilenTeklifBaslik,teklifSatirlari,sessionBean.getKullanici().getKULLANICI_ID());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Kaydetme işlemi başarıyla tamamlanmıştır.", ""));
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kaydetme işleminde hata meydana geldi.", ""));
        }        
    }
    
    public void guncelleMusteriMarjiBaslik() 
    {
        try 
        {   
            //başlık kısmındaki tutarı hesaplamak için...
            double tutar = 0.0;
            
            for(TEKLIF_SATIR teklifSatir :teklifSatirlari)
            {
                teklifSatir.setMUSTERI_MARJ(secilenTeklifBaslik.getMUSTERI_MARJ());
                
                //////////////////////////////////////////////
                //Maliyeti hesaplıyoruz...
                //Eğer marj yok ise direk maliyet birim fiyata eşittir...
                if(teklifSatir.getMUSTERI_MARJ()==0.0)
                   teklifSatir.setMALZEME_HIZMET_MASRAF_BIRIM_FIYATI(teklifSatir.getMALIYET_BIRIM_FIYAT()); 
                //Marj sıfır değil ise birimFiyat = maliyet*marj/100
                else
                    teklifSatir
                            .setMALZEME_HIZMET_MASRAF_BIRIM_FIYATI(
                                    teklifSatir.getMALIYET_BIRIM_FIYAT() +
                                    (teklifSatir.getMALIYET_BIRIM_FIYAT()*teklifSatir.getMUSTERI_MARJ()/100)
                                                   );         
                //////////////////////////////////////////////

                teklifSatir.setMALZEME_HIZMET_MASRAF_TUTARI(
                           teklifSatir.getMALZEME_HIZMET_MASRAF_BIRIM_FIYATI() 
                         * teklifSatir.getMUSTERI_SATIS_MIKTAR_YANIT());

                if(teklifSatir.getKUR() <= 0.0)
                       teklifSatir.setKUR(1.0);

                   teklifSatir.setMalzemeHizmetMasrafTutariCarpiKur(
                           teklifSatir.getMALZEME_HIZMET_MASRAF_TUTARI() * teklifSatir.getKUR());                   
            
            
                //Başlık kısmındaki tutar bilgisi...
                tutar = tutar + (teklifSatir.getMalzemeHizmetMasrafTutariCarpiKur());
            }
            

           secilenTeklifBaslik.setTEKLIF_TUTARI(tutar / secilenTeklifBaslik.getTEKLIF_KUR());
           secilenTeklifBaslik.setTeklifTutariCarpiKur(tutar);
            
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Başlık marjı kaydetme işleminde hata meydana geldi.", ""));
        }        
    }
    
    public void guncelleMusteriMarjiSatir(TEKLIF_SATIR teklifSatir) 
    {
        try 
        {    
            //////////////////////////////////////////////
            //Maliyeti hesaplıyoruz...
            //Eğer marj yok ise direk maliyet birim fiyata eşittir...
            if(teklifSatir.getMUSTERI_MARJ()==0.0)
               teklifSatir.setMALZEME_HIZMET_MASRAF_BIRIM_FIYATI(teklifSatir.getMALIYET_BIRIM_FIYAT()); 
            //Marj sıfır değil ise birimFiyat = maliyet*marj/100
            else
                teklifSatir
                        .setMALZEME_HIZMET_MASRAF_BIRIM_FIYATI(
                                teklifSatir.getMALIYET_BIRIM_FIYAT() +
                                (teklifSatir.getMALIYET_BIRIM_FIYAT()*teklifSatir.getMUSTERI_MARJ()/100)
                                               );         
            //////////////////////////////////////////////

            teklifSatir.setMALZEME_HIZMET_MASRAF_TUTARI(
                       teklifSatir.getMALZEME_HIZMET_MASRAF_BIRIM_FIYATI() 
                     * teklifSatir.getMUSTERI_SATIS_MIKTAR_YANIT());

            if(teklifSatir.getKUR() <= 0.0)
                   teklifSatir.setKUR(1.0);

               teklifSatir.setMalzemeHizmetMasrafTutariCarpiKur(
                       teklifSatir.getMALZEME_HIZMET_MASRAF_TUTARI() * teklifSatir.getKUR());

           double tutar = 0.0;     

           for(TEKLIF_SATIR teklifSatir1 :teklifSatirlari)
           {
               tutar = tutar + (teklifSatir1.getMalzemeHizmetMasrafTutariCarpiKur());
           }

           secilenTeklifBaslik.setTEKLIF_TUTARI(tutar / secilenTeklifBaslik.getTEKLIF_KUR());
           secilenTeklifBaslik.setTeklifTutariCarpiKur(tutar);
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Başlık marjı kaydetme işleminde hata meydana geldi.", ""));
        }        
    }
    
    public void siparisOlarakAktar() 
    {
        try 
        {    
            
            ////////////////////////////
            //Token alma işlemi...
            URL url = new URL("http://" + ayarlarBean.getRestServisAyar().getREST_SERVIS_SUNUCU_ADI_IP() + ":" + ayarlarBean.getRestServisAyar().getREST_SERVIS_SUNUCU_PORT() + "/api/v1/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            
            String clientIdSecret = ayarlarBean.getRestServisAyar().getREST_SERVIS_CLIENT_ID() + ":" + ayarlarBean.getRestServisAyar().getREST_SERVIS_CLIENT_SECRET();
            String encodeBytes = Base64.getEncoder().encodeToString(clientIdSecret.getBytes());
            conn.setRequestProperty("Authorization", "Basic " + encodeBytes);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	    conn.setRequestProperty("Accept", "application/json");
            
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            writer.write("grant_type=password&username=" + 
                          ayarlarBean.getRestServisAyar().getREST_SERVIS_USERNAME() + 
                          "&firmno=" + ayarlarBean.getRestServisAyar().getREST_SERVIS_FIRMNO() + 
                          "&password=" + ayarlarBean.getRestServisAyar().getREST_SERVIS_PASSWORD() );
            writer.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer jsonString = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) 
            {
                    jsonString.append(line);
            }
            br.close();
            conn.disconnect();
            
            JSONParser parse = new JSONParser();
            JSONObject jobj = (JSONObject)parse.parse(jsonString.toString()); 
            String token = (String)jobj.get("access_token");
                      
            ////////////////////////////
            //ERP'ye kayıt açılma işlemi...
 
            URL url2 = new URL("http://" + ayarlarBean.getRestServisAyar().getREST_SERVIS_SUNUCU_ADI_IP() + ":" + ayarlarBean.getRestServisAyar().getREST_SERVIS_SUNUCU_PORT() 
                             + "/api/v1/salesOrders");
            HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();            
            
            conn2.setDoInput(true);
            conn2.setDoOutput(true);
            conn2.setRequestMethod("POST");
            conn2.setRequestProperty("Authorization", "Bearer " + token);
	    conn2.setRequestProperty("Accept", "application/json");
            conn2.setRequestProperty("Content-Type", "application/json");
            
            OutputStreamWriter writer2 = new OutputStreamWriter(conn2.getOutputStream(), "UTF-8");
            
            //Bugünün tarihini alıyoruz...
            LocalDateTime ldt = LocalDateTime.now();
            String tarih = DateTimeFormatter.ofPattern("MM-dd-yyyy", Locale.ENGLISH).format(ldt);
            
            String baslik =  " { " +
                               " \"INTERNAL_REFERENCE\":0, " +
                               " \"NUMBER\":\"" + "~" + "\", " +
                               " \"DATE\":\"" + tarih + "\", " +
                               " \"ARP_CODE\":\"" + secilenTeklifBaslik.getMusteriKodu() + "\", ";
                            if(secilenTeklifBaslik.getTEKLIF_PARA_BIRIMI() == 1 || secilenTeklifBaslik.getTEKLIF_PARA_BIRIMI() == 20)
                                baslik = baslik + " \"TC_RATE\": " + secilenTeklifBaslik.getTEKLIF_KUR() + ", " +          // Kur
                                                  " \"CURR_TRANSACTIN\": " + secilenTeklifBaslik.getTEKLIF_PARA_BIRIMI() + ", " + //Para birimi 
                                                  " \"CURRSEL_TOTAL\": 2, " +    //Başlık bilgisi işlem dövizi oldu
                                                  " \"CURRSEL_DETAILS\": 2, ";   //Satır bilgisi işlem dövizi oldu
                                
                            else if(secilenTeklifBaslik.getTEKLIF_PARA_BIRIMI() == 0)
                               baslik = baslik + " \"CURRSEL_TOTAL\":1, ";
                            
             baslik = baslik + " \"TRANSACTIONS\": " +
                               " { " +
                                 " \"items\":[ ";   
                    
            for(TEKLIF_SATIR teklifSatir : teklifSatirlari)
            {
                String noPrefixStr = teklifSatir.getMALZEME_HIZMET_MASRAF_BIRIM_KODU();
                if(noPrefixStr.contains("("))
                {
                    String prefix = "(";
                    noPrefixStr = teklifSatir.getMALZEME_HIZMET_MASRAF_BIRIM_KODU().
                                             substring(0,teklifSatir.getMALZEME_HIZMET_MASRAF_BIRIM_KODU().indexOf(prefix)-1);
                }                
                
                String item = " { " +
                                " \"MASTER_CODE\":\"" + teklifSatir.getMALZEME_HIZMET_MASRAF_KODU() + "\", " +
                                " \"QUANTITY\":" + teklifSatir.getMUSTERI_SATIS_MIKTAR_YANIT() + ", " +
                                " \"PRICE\":" + teklifSatir.getMALZEME_HIZMET_MASRAF_BIRIM_FIYATI() * teklifSatir.getKUR()  + ", " +
                                " \"UNIT_CODE\": \"" + noPrefixStr + "\" " +
                              " }," ;
                
                baslik = baslik + item;
            }
            
            //Sondaki virgülü silmek için...
            baslik = baslik.substring(0, baslik.length() - 1);
            
            baslik = baslik + "]}}";
                    
            writer2.write(baslik);
            writer2.close();            

            BufferedReader br2 = new BufferedReader(new InputStreamReader(conn2.getInputStream(), Charset.forName("UTF-8"))); 
            
            StringBuffer jsonString2 = new StringBuffer();
            String line2;
            while ((line2 = br2.readLine()) != null) 
            {
                    jsonString2.append(line2);
            }
            br2.close();
            conn2.disconnect();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sipariş olarak aktarma işlemi başarıyla tamamlanmıştır. Lütfen ERP'den kontrol ediniz.", ""));
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sipariş olarak aktarma işleminde hata meydana gelmiştir. Lütfen ERP'den kontrol ediniz.", ""));
        }        
    }
    
    
    public List<TEKLIF_BASLIK> getSecilenTeklifBaslikListesi() {
        return secilenTeklifBaslikListesi;
    }

    public void setSecilenTeklifBaslikListesi(List<TEKLIF_BASLIK> secilenTeklifBaslikListesi) {
        this.secilenTeklifBaslikListesi = secilenTeklifBaslikListesi;
    }
    
    public List<TEKLIF_BASLIK> getTeklifBasliklari() {
        return teklifBasliklari;
    }

    public void setTeklifBasliklari(List<TEKLIF_BASLIK> teklifBasliklari) {
        this.teklifBasliklari = teklifBasliklari;
    }

    public TEKLIF_BASLIK getSecilenTeklifBaslik() {
        return secilenTeklifBaslik;
    }

    public void setSecilenTeklifBaslik(TEKLIF_BASLIK secilenTeklifBaslik) {
        this.secilenTeklifBaslik = secilenTeklifBaslik;
    }

    public SatisDao getSatisDao() {
        return satisDao;
    }

    public void setSatisDao(SatisDao satisDao) {
        this.satisDao = satisDao;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public List<TEKLIF_SATIR> getTeklifSatirlari() {
        return teklifSatirlari;
    }

    public void setTeklifSatirlari(List<TEKLIF_SATIR> teklifSatirlari) {
        this.teklifSatirlari = teklifSatirlari;
    }

    public AyarlarBean getAyarlarBean() {
        return ayarlarBean;
    }

    public void setAyarlarBean(AyarlarBean ayarlarBean) {
        this.ayarlarBean = ayarlarBean;
    }

    public List<MailList> getMailToList() {
        return mailToList;
    }

    public void setMailToList(List<MailList> mailToList) {
        this.mailToList = mailToList;
    }

    public List<TEKLIF_BASLIK> getTeklifBasliklariFiltreleme() {
        return teklifBasliklariFiltreleme;
    }

    public void setTeklifBasliklariFiltreleme(List<TEKLIF_BASLIK> teklifBasliklariFiltreleme) {
        this.teklifBasliklariFiltreleme = teklifBasliklariFiltreleme;
    }

    public List<MAIL_DOSYA> getMailDosyaList() {
        return mailDosyaList;
    }

    public void setMailDosyaList(List<MAIL_DOSYA> mailDosyaList) {
        this.mailDosyaList = mailDosyaList;
    }

    public MAIL_DOSYA getSilinecekMailDosya() {
        return silinecekMailDosya;
    }

    public void setSilinecekMailDosya(MAIL_DOSYA silinecekMailDosya) {
        this.silinecekMailDosya = silinecekMailDosya;
    }

    public Part getYuklenecekDosya() {
        return yuklenecekDosya;
    }

    public void setYuklenecekDosya(Part yuklenecekDosya) {
        this.yuklenecekDosya = yuklenecekDosya;
    }

    public List<MailList> getMailBccList() {
        return mailBccList;
    }

    public void setMailBccList(List<MailList> mailBccList) {
        this.mailBccList = mailBccList;
    }

    public List<MailList> getMailCcList() {
        return mailCcList;
    }

    public void setMailCcList(List<MailList> mailCcList) {
        this.mailCcList = mailCcList;
    }

    public TEKLIF_SATIR getSecilenTeklifSatir() {
        return secilenTeklifSatir;
    }

    public void setSecilenTeklifSatir(TEKLIF_SATIR secilenTeklifSatir) {
        this.secilenTeklifSatir = secilenTeklifSatir;
    }

    public SATISTAN_TEDARIKCI_KAYITLARI getSecilenSatistanTedarikciKayitlari() {
        return secilenSatistanTedarikciKayitlari;
    }

    public void setSecilenSatistanTedarikciKayitlari(SATISTAN_TEDARIKCI_KAYITLARI secilenSatistanTedarikciKayitlari) {
        this.secilenSatistanTedarikciKayitlari = secilenSatistanTedarikciKayitlari;
    }

    public TanimliSatisFiyati getSecilenTanimliSatisFiyati() {
        return secilenTanimliSatisFiyati;
    }

    public void setSecilenTanimliSatisFiyati(TanimliSatisFiyati secilenTanimliSatisFiyati) {
        this.secilenTanimliSatisFiyati = secilenTanimliSatisFiyati;
    }

    public List<SatisSatiriAtanmisTedarikci> getSatisSatiriAtanmisTedarikciList() {
        return satisSatiriAtanmisTedarikciList;
    }

    public void setSatisSatiriAtanmisTedarikciList(List<SatisSatiriAtanmisTedarikci> satisSatiriAtanmisTedarikciList) {
        this.satisSatiriAtanmisTedarikciList = satisSatiriAtanmisTedarikciList;
    }

    public SatisSatiriAtanmisTedarikci getIslemYapilanSatisSatiriAtanmisTedarikci() {
        return islemYapilanSatisSatiriAtanmisTedarikci;
    }

    public void setIslemYapilanSatisSatiriAtanmisTedarikci(SatisSatiriAtanmisTedarikci islemYapilanSatisSatiriAtanmisTedarikci) {
        this.islemYapilanSatisSatiriAtanmisTedarikci = islemYapilanSatisSatiriAtanmisTedarikci;
    }

    public MailList getIslemYapilanMailTo() {
        return islemYapilanMailTo;
    }

    public void setIslemYapilanMailTo(MailList islemYapilanMailTo) {
        this.islemYapilanMailTo = islemYapilanMailTo;
    }

    public MailList getEklenecekMailTo() {
        return eklenecekMailTo;
    }

    public void setEklenecekMailTo(MailList eklenecekMailTo) {
        this.eklenecekMailTo = eklenecekMailTo;
    }

    public MAIL_DOSYA getIslemYapilanMailDosya() {
        return islemYapilanMailDosya;
    }

    public void setIslemYapilanMailDosya(MAIL_DOSYA islemYapilanMailDosya) {
        this.islemYapilanMailDosya = islemYapilanMailDosya;
    }

    public List<TEKLIF_BASLIK> getGizlenenTeklifBasliklari() {
        return gizlenenTeklifBasliklari;
    }

    public void setGizlenenTeklifBasliklari(List<TEKLIF_BASLIK> gizlenenTeklifBasliklari) {
        this.gizlenenTeklifBasliklari = gizlenenTeklifBasliklari;
    }

    public List<TEKLIF_BASLIK> getSecilenGizlenmisTeklifBaslikListesi() {
        return secilenGizlenmisTeklifBaslikListesi;
    }

    public void setSecilenGizlenmisTeklifBaslikListesi(List<TEKLIF_BASLIK> secilenGizlenmisTeklifBaslikListesi) {
        this.secilenGizlenmisTeklifBaslikListesi = secilenGizlenmisTeklifBaslikListesi;
    }

    public List<TEKLIF_BASLIK> getGizlenenTeklifBasliklariFiltreleme() {
        return gizlenenTeklifBasliklariFiltreleme;
    }

    public void setGizlenenTeklifBasliklariFiltreleme(List<TEKLIF_BASLIK> gizlenenTeklifBasliklariFiltreleme) {
        this.gizlenenTeklifBasliklariFiltreleme = gizlenenTeklifBasliklariFiltreleme;
    }

    public SATISTAN_TEDARIKCI_KAYITLARI getTeklifGirisineAcilacakSatistanTedarikciKayitlari() {
        return teklifGirisineAcilacakSatistanTedarikciKayitlari;
    }

    public void setTeklifGirisineAcilacakSatistanTedarikciKayitlari(SATISTAN_TEDARIKCI_KAYITLARI teklifGirisineAcilacakSatistanTedarikciKayitlari) {
        this.teklifGirisineAcilacakSatistanTedarikciKayitlari = teklifGirisineAcilacakSatistanTedarikciKayitlari;
    }

    public TEKLIF_BASLIK getRevizeEdilecekTeklifBaslik() {
        return revizeEdilecekTeklifBaslik;
    }

    public void setRevizeEdilecekTeklifBaslik(TEKLIF_BASLIK revizeEdilecekTeklifBaslik) {
        this.revizeEdilecekTeklifBaslik = revizeEdilecekTeklifBaslik;
    }

    public List<TEKLIF_SATIR> getRevizeEdilecekTeklifSatirlari() {
        return revizeEdilecekTeklifSatirlari;
    }

    public void setRevizeEdilecekTeklifSatirlari(List<TEKLIF_SATIR> revizeEdilecekTeklifSatirlari) {
        this.revizeEdilecekTeklifSatirlari = revizeEdilecekTeklifSatirlari;
    }

    public SatisRevizyonBean getSatisRevizyonBean() {
        return satisRevizyonBean;
    }

    public void setSatisRevizyonBean(SatisRevizyonBean satisRevizyonBean) {
        this.satisRevizyonBean = satisRevizyonBean;
    }
    
}
    

