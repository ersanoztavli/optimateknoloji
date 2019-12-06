/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.beans.admin;

import com.oms.dao.SatinAlmaDao;
import com.oms.models.MAIL;
import com.oms.models.MAIL_ADRES;
import com.oms.models.MAIL_DOSYA;
import com.oms.models.MailListSatinAlma;
import com.oms.models.TEKLIF_BASLIK;
import com.oms.models.TEKLIF_SATIR;
import com.oms.models.TEKLIF_SATIR_YANIT;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.Part;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

/**
 *
 * @author ersan
 */
@ManagedBean(name="satinAlmaBean", eager = true)
@SessionScoped
public class SatinAlmaBean implements Serializable{
    
    /**
     * Creates a new instance of SatinAlmaBean
     */
    public SatinAlmaBean() {       
    }
    
    private static final long serialVersionUID = 1L;    
    
    private List<TEKLIF_BASLIK> teklifBasliklari;
    private List<TEKLIF_BASLIK> teklifBasliklariFiltreleme;
    
    private List<TEKLIF_BASLIK> gizlenenTeklifBasliklari;
    private List<TEKLIF_BASLIK> gizlenenTeklifBasliklariFiltreleme;
    private List<TEKLIF_BASLIK> secilenGizlenmisTeklifBaslikListesi;
    
    private List<TEKLIF_BASLIK> secilenTeklifBaslikListesi;
    private TEKLIF_BASLIK secilenTeklifBaslik = new TEKLIF_BASLIK();
    
    private List<TEKLIF_SATIR> teklifSatirlari;    
    private MAIL_ADRES secilenMailAdres = new MAIL_ADRES();       
    private List<TEKLIF_SATIR_YANIT> teklifSatirYanitlari;
    
    private SatinAlmaDao satinAlmaDao;
     
    @ManagedProperty(value="#{sessionBean}")
    private SessionBean sessionBean;   
    
    @ManagedProperty(value="#{ayarlarBean}")
    private AyarlarBean ayarlarBean; 
    
    private List<MailListSatinAlma> mailListSatinAlmaListSource;
    private List<MailListSatinAlma> mailListSatinAlmaListTarget;    
    private DualListModel<MailListSatinAlma> mailListSatinAlmaList;
    
    private List<MAIL> gonderilenMailler;
    
    private List<MAIL_DOSYA> mailDosyaList;    
    private MAIL_DOSYA silinecekMailDosya;
    private Part yuklenecekDosya;
    
    @PostConstruct
    public void init() 
    {        
        try 
        {
            secilenTeklifBaslik = new TEKLIF_BASLIK();
            secilenMailAdres = new MAIL_ADRES();
            teklifSatirlari = new ArrayList<>(); 
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
                FacesContext facesContext = FacesContext.getCurrentInstance();
                sessionBean = (SessionBean)facesContext.getApplication().createValueBinding("#{sessionBean}").getValue(facesContext);
            
                satinAlmaDao = new SatinAlmaDao();
                if(teklifBasliklari == null)
                    teklifBasliklari = satinAlmaDao.getirTeklifBasliklari(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
                                
                teklifBasliklariFiltreleme = null;
                gizlenenTeklifBasliklariFiltreleme = null;
            } 
            catch (Exception ex) 
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sayfa başlatılırken hata oluştu.", ""));
            }
        }
    }
    
    
    public void satinAlmaTeklifleriniKaydetERPden() 
    {
        try 
        {
            String ERPFirmaNoTemp = sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER();
            
            //MÜŞTERİLERİN ÇEKİLDİĞİ YER...
            ////////////////////////////////////////////////////////////////////////////////////////////
            String enBuyukMusteriRecordId = satinAlmaDao.getirEnBuyukMusteriRecordId(ERPFirmaNoTemp);
            satinAlmaDao.kaydetMusterilerERPden(enBuyukMusteriRecordId, 
                                            sessionBean.getKullanici().getKULLANICI_ID(),
                                            sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
            ////////////////////////////////////////////////////////////////////////////////////////////
            
            //TEKLİF BAŞLIKLARININ ÇEKİLDİĞİ YER...
            ////////////////////////////////////////////////////////////////////////////////////////////                       
            
            String enBuyukTeklifBaslikRecordId = satinAlmaDao.getirEnBuyukTeklifBaslikRecordId(ERPFirmaNoTemp);
            satinAlmaDao.kaydetTeklifBaslikERPden(enBuyukTeklifBaslikRecordId, 
                                              sessionBean.getKullanici().getKULLANICI_ID(),
                                              sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER(),
                                              sessionBean.getKullanici().getON_DEGER_FIRMA_ID());
            ////////////////////////////////////////////////////////////////////////////////////////////
            
            //TEKLİF SATIRLARININ ÇEKİLDİĞİ YER...
            ////////////////////////////////////////////////////////////////////////////////////////////
            satinAlmaDao.kaydetTeklifSatirERPden(sessionBean.getKullanici().getKULLANICI_ID(),
                                                sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER(),
                                                enBuyukTeklifBaslikRecordId);
            ////////////////////////////////////////////////////////////////////////////////////////////
            
            teklifBasliklari = satinAlmaDao.getirTeklifBasliklari(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
        
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklifler ERP'den getirilirken hata oluştu.", ""));
        }        
    }
    
    public void satinAlmaTeklifleriniGizle() 
    {
        try 
        {
            if(secilenTeklifBaslikListesi!=null)
                if(!secilenTeklifBaslikListesi.isEmpty()) 
                {                
                    satinAlmaDao.gizleTeklifListesi(sessionBean.getKullanici().getKULLANICI_ID(),
                                                    secilenTeklifBaslikListesi);
                    teklifBasliklari = satinAlmaDao.getirTeklifBasliklari(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, String.valueOf(secilenTeklifBaslikListesi.size()) + " adet teklif başarıyla gizlenmiştir.", ""));
                }
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklifler gizlenirken hata oluştu.", ""));
        }        
    }
    
    public void satinAlmaTeklifleriniGeriGetir() 
    {
        try 
        {
            if(secilenGizlenmisTeklifBaslikListesi!=null)
                if(!secilenGizlenmisTeklifBaslikListesi.isEmpty()) 
                {                
                    satinAlmaDao.geriGetirTeklifListesi(sessionBean.getKullanici().getKULLANICI_ID(),
                                                        secilenGizlenmisTeklifBaslikListesi);
                    gizlenenTeklifBasliklari = satinAlmaDao.getirTeklifBasliklariGizlenen(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
                    teklifBasliklari = satinAlmaDao.getirTeklifBasliklari(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
                    gizlenenTeklifBasliklariFiltreleme = null;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, String.valueOf(secilenGizlenmisTeklifBaslikListesi.size()) + " adet teklif başarıyla geri getirilmiştir.", ""));
                }
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklifler geri getirilirken hata oluştu.", ""));
        }        
    }
    
    public void gizlenenSatinAlmaTeklifleriniGetir() 
    {
        try 
        {
           gizlenenTeklifBasliklari = satinAlmaDao.getirTeklifBasliklariGizlenen(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
           gizlenenTeklifBasliklariFiltreleme = null;
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Gizlenen satın alma teklifleri listelenirken hata oluştu.", ""));
        }        
    }
    
    public void gizlenenTeklifBasliklariFiltrelemeTemizle() 
    {
        try 
        {
           gizlenenTeklifBasliklariFiltreleme = null;
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Gizlenen satın alma teklifleri filtresi temizlenirken hata oluştu.", ""));
        }        
    }
    
    public void satinAlmaTeklifleriniGizleOnIsleme() 
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
    
    public void satinAlmaTeklifleriniGeriGetirOnIsleme() 
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
    
    public String satinAlmaTeklifDetaylariniGetirERPden() 
    {
        try 
        {            
            teklifSatirlari = satinAlmaDao.getirTeklifSatirlari(secilenTeklifBaslik.getRECORD_ID(),
                                                                secilenTeklifBaslik.getERP_FIRMA_NUMBER());  
            
            return "/admin/pages/satinAlmaDetay.jsf";
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklif detayları getirilirken hata oluştu.", ""));
            return "";
        }        
    }
    
    public void satinAlmaTeklifDetaylariniGetirERPdenYeni() 
    {
        try 
        {            
            teklifSatirlari = satinAlmaDao.getirTeklifSatirlari(secilenTeklifBaslik.getRECORD_ID(),
                                                                secilenTeklifBaslik.getERP_FIRMA_NUMBER()); 
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklif detayları getirilirken hata oluştu.", ""));
        }        
    }
    
    public void satinAlmaYanitlariniGetir() 
    {
        try 
        {            
            gonderilenMailler = satinAlmaDao.getirGonderilenSatinAlmaMailler(secilenTeklifBaslik.getTEKLIF_BASLIK_ID());
            
            //return "/admin/pages/satinAlmaGonderilenler.jsf?faces-redirect=true";
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Yanıtlar getirilirken hata oluştu.", ""));
            //return "";
        }        
    }
    
    public void satinAlmaYanitlariniGetirYeni() 
    {
        try 
        {            
            gonderilenMailler = satinAlmaDao.getirGonderilenSatinAlmaMailler(secilenTeklifBaslik.getTEKLIF_BASLIK_ID());            
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Yanıtlar getirilirken hata oluştu.", ""));
        }        
    }
    
    public String satinAlmaYanitDetaylariniGetir() 
    {
        try 
        {            
            secilenTeklifBaslik = satinAlmaDao.getirTeklifBaslikSatinAlma(secilenMailAdres.getMAIL_ADRES_ID());
            teklifSatirYanitlari = satinAlmaDao.getirTeklifSatirYanitlari(secilenMailAdres.getMAIL_ADRES_ID());
            
            return "/admin/pages/satinAlmaGonderilenDetay.jsf?faces-redirect=true";
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Yanıtlar getirilirken hata oluştu.", ""));
            return "";
        }        
    }
    
    public void satinAlmaYanitDetaylariniGetirYeni() 
    {
        try 
        {            
            secilenTeklifBaslik = satinAlmaDao.getirTeklifBaslikSatinAlma(secilenMailAdres.getMAIL_ADRES_ID());
            teklifSatirYanitlari = satinAlmaDao.getirTeklifSatirYanitlari(secilenMailAdres.getMAIL_ADRES_ID());
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Yanıtlar getirilirken hata oluştu.", ""));
        }        
    }
    
    
    public String mailPrepare() 
    {
        try 
        {
            mailListSatinAlmaListTarget = new ArrayList<>();   
            
            //müşterileri çekip mail listesine çeviriyoruz... 
            mailListSatinAlmaListSource = satinAlmaDao.getirMailListSatinAlma(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER()
                                                                              ,teklifSatirlari);
            
            mailListSatinAlmaList = new DualListModel<>(mailListSatinAlmaListSource, mailListSatinAlmaListTarget);
            mailDosyaList = new ArrayList<>();
            
            //Önceden kalan dosya varsa diye temizliyoruz...
            return "/admin/pages/satinAlmaDetayGonder.jsf?faces-redirect=true";
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mail gönderilirken hata oluştu.", ""));
            return "";
        }
    }
    
    public void mailPrepareYeni() 
    {
        try 
        {
            mailListSatinAlmaListTarget = new ArrayList<>();   
            
            //müşterileri çekip mail listesine çeviriyoruz... 
            mailListSatinAlmaListSource = satinAlmaDao.getirMailListSatinAlma(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER()
                                                                              ,teklifSatirlari);
            
            mailListSatinAlmaList = new DualListModel<>(mailListSatinAlmaListSource, mailListSatinAlmaListTarget);
            mailDosyaList = new ArrayList<>(); //Önceden kalan dosya varsa diye temizliyoruz...
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mail gönderilirken hata oluştu.", ""));
        }
    }
        
    public void onTransfer(TransferEvent event) 
    {
       if (event.isAdd()) 
        for(Object item : event.getItems()) 
        {
            mailListSatinAlmaListTarget.add((MailListSatinAlma) item);
        }
       
       if (event.isRemove()) 
        for(Object item : event.getItems()) 
        {
            mailListSatinAlmaListTarget.remove((MailListSatinAlma) item);
        }       
    }
    
    public void erpyeAktar() 
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
            //Başlık ve satır bilgilerinin değiştirildiği kısım...            
          
            URL url1 = new URL("http://" + ayarlarBean.getRestServisAyar().getREST_SERVIS_SUNUCU_ADI_IP() + ":" + ayarlarBean.getRestServisAyar().getREST_SERVIS_SUNUCU_PORT() 
                             + "/api/v1/purchaseProposalOffers/" + secilenTeklifBaslik.getRECORD_ID() + "?expandLevel=full");
                   
            HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();            
            conn1.setDoInput(true);
            conn1.setDoOutput(true);
            conn1.setRequestMethod("GET");            
            conn1.setRequestProperty("Authorization", "Bearer " + token);
	    conn1.setRequestProperty("Accept", "application/json");
            conn1.setRequestProperty("Content-Type", "application/json");
            BufferedReader br1 = new BufferedReader(new InputStreamReader(conn1.getInputStream()));
            StringBuffer jsonString1 = new StringBuffer();
            String line1;
            
             while ((line1 = br1.readLine()) != null) 
            {
                    jsonString1.append(line1);
            }
            br1.close();
            conn1.disconnect();

            JSONParser parse1 = new JSONParser();
            JSONObject jobj1 = (JSONObject)parse1.parse(jsonString1.toString()); 
            jobj1.remove("Meta");
            jobj1.remove("INTERNAL_REFERENCE");
            jobj1.remove("FICHENO");            
            jobj1.remove("CLIENTREF");
            jobj1.remove("ARP_CODE");
            jobj1.put("ARP_CODE",secilenTeklifBaslik.getMusteriKodu());

            jobj1.remove("ADDDISCOUNTS");
            jobj1.remove("TOTALDISCOUNTS");
            jobj1.remove("TOTALDISCOUNTED");
            jobj1.remove("ADDEXPENSES");
            jobj1.remove("TOTALEXPENSES");
            jobj1.remove("TOTALPROMOTIONS");
            jobj1.remove("TOTALVAT");
            jobj1.remove("GROSSTOTAL");
            jobj1.remove("NETTOTAL");

            /////////////////////////////
            //Tedarikçi USD veya EUR kestiğinde çalışacak kısım...
            if(secilenTeklifBaslik.getSatinAlmaTedarikciParaBirimi()!=null)
                if(!secilenTeklifBaslik.getSatinAlmaTedarikciParaBirimi().isEmpty())//null değil, boş da değilse...
                {
                    if(!secilenTeklifBaslik.getSatinAlmaTedarikciParaBirimi().equals("TL"))//TL ise hiç dokunma...
                    {
                        jobj1.remove("TRCURR"); // Para Birimi
                        if(secilenTeklifBaslik.getSatinAlmaTedarikciParaBirimi().equals("USD"))                            
                            jobj1.put("TRCURR","1"); // 0:TL, 1:USD, 20:EUR
                        else if(secilenTeklifBaslik.getSatinAlmaTedarikciParaBirimi().equals("EUR"))                            
                            jobj1.put("TRCURR","20"); // 0:TL, 1:USD, 20:EUR

                        jobj1.remove("GENEXCTYP"); // Genel
                        jobj1.put("GENEXCTYP","2"); // İşlem Dövizi
                        jobj1.remove("LINEEXCTYP"); // Satırlar
                        jobj1.put("LINEEXCTYP","2"); // İşlem Dövizi
                        jobj1.remove("TRRATE"); // Döviz Kuru
                        jobj1.put("TRRATE","1.00"); // otomatik 1.00 atadık...  
                    }                                 
                }                    
            /////////////////////////////            
            
            JSONObject jobjTRANSACTIONS = (JSONObject)jobj1.get("TRANSACTIONS");
            jobj1.remove("TRANSACTIONS");
            
            JSONArray jarrayTRANSACTIONSItems = (JSONArray)jobjTRANSACTIONS.get("items");
            jobjTRANSACTIONS.remove("items");            
            
            for (int i = 0; i < jarrayTRANSACTIONSItems.size(); i++) 
            {
                JSONObject jsonobject = (JSONObject)jarrayTRANSACTIONSItems.get(i);
                jsonobject.remove("Meta");    
                
                for (TEKLIF_SATIR_YANIT teklifSatirYanit : teklifSatirYanitlari)
                {
                    if (teklifSatirYanit.getMalzemeHizmetMasrafRecordId().
                            equals(String.valueOf(jsonobject.get("INTERNAL_REFERENCE"))))
                    {
                        jsonobject.remove("INTERNAL_REFERENCE"); // Logicalref
                        jsonobject.remove("ORDFICHEREF"); // Telif Başlık Id'si
                        jsonobject.remove("VATAMNT"); // KDV ile ilgili
                        jsonobject.remove("VATMATRAH"); // KDV ile ilgili
                        jsonobject.remove("TOTAL"); // Toplam               
                        jsonobject.remove("QUANTITY"); // Miktar...
                        jsonobject.remove("PRICE");  // Birim Fiyat...
                        jsonobject.remove("UOMREF"); // Birim...
                        jsonobject.remove("USREF"); // Birim...
                        jsonobject.remove("UEDIT"); // Birim...                        

                        jsonobject.put("QUANTITY",teklifSatirYanit.getMIKTAR()); // Miktar...
                        jsonobject.put("PRICE",teklifSatirYanit.getBIRIM_FIYATI()); // Birim Fiyat...
                        
                        jsonobject.put("UOMREF",satinAlmaDao.getirBirimIdBirimIle(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER(),
                                                                    teklifSatirYanit.getBIRIM())); // Birim...                        
                    
                        String prefix = "(";
                        String noPrefixStr = teklifSatirYanit.getBIRIM().
                                             substring(0,teklifSatirYanit.getBIRIM().indexOf(prefix)-1);
                        
                        jsonobject.put("UEDIT",noPrefixStr); // Birim...
                        
                        break;
                    } 
                   }
            }
            
            
            jobjTRANSACTIONS.put("items",jarrayTRANSACTIONSItems);
            jobj1.put("TRANSACTIONS", jobjTRANSACTIONS);
            
            ////////////////////////////
            //ERP'ye kayıt açılma işlemi...
 
            URL url2 = new URL("http://" + ayarlarBean.getRestServisAyar().getREST_SERVIS_SUNUCU_ADI_IP() + ":" + ayarlarBean.getRestServisAyar().getREST_SERVIS_SUNUCU_PORT() 
                             + "/api/v1/purchaseProposalOffers");
            HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();            
            
            conn2.setDoInput(true);
            conn2.setDoOutput(true);
            conn2.setRequestMethod("POST");
            conn2.setRequestProperty("Authorization", "Bearer " + token);
	    conn2.setRequestProperty("Accept", "application/json");
            conn2.setRequestProperty("Content-Type", "application/json");
            
            OutputStreamWriter writer2 = new OutputStreamWriter(conn2.getOutputStream(), "UTF-8");
            writer2.write(jobj1.toString());
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
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ERP'ye aktarma işlemi başarıyla tamamlanmıştır.", ""));
        
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.toString(), ex.getMessage()));
        }        
    }
    
    public void teklifGirisineAc() 
    {
        try 
        {    
            satinAlmaDao.veriGirisineAcMailAdresIdIle(secilenMailAdres.getMAIL_ADRES_ID());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Teklif girişine açma işlemi başarıyla tamamlanmıştır.\n Tedarikçi teklif girişine devam edebilir.", ""));
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.toString(), ex.getMessage()));
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
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dosya ekleme işleminde hata meydana geldi.", ""));
         }
    }        
    
     
     
    public List<TEKLIF_BASLIK> getTeklifBasliklari() {
        return teklifBasliklari;
    }

    public void setTeklifBasliklari(List<TEKLIF_BASLIK> teklifBasliklari) {
        this.teklifBasliklari = teklifBasliklari;
    }

    public List<TEKLIF_SATIR> getTeklifSatirlari() {
        return teklifSatirlari;
    }

    public void setTeklifSatirlari(List<TEKLIF_SATIR> teklifSatirlari) {
        this.teklifSatirlari = teklifSatirlari;
    }

    public TEKLIF_BASLIK getSecilenTeklifBaslik() {
        return secilenTeklifBaslik;
    }

    public void setSecilenTeklifBaslik(TEKLIF_BASLIK secilenTeklifBaslik) {
        this.secilenTeklifBaslik = secilenTeklifBaslik;
    }

    public List<TEKLIF_BASLIK> getSecilenTeklifBaslikListesi() {
        return secilenTeklifBaslikListesi;
    }

    public void setSecilenTeklifBaslikListesi(List<TEKLIF_BASLIK> secilenTeklifBaslikListesi) {
        this.secilenTeklifBaslikListesi = secilenTeklifBaslikListesi;
    }

    public SatinAlmaDao getSatinAlmaDao() {
        return satinAlmaDao;
    }

    public void setSatinAlmaDao(SatinAlmaDao satinAlmaDao) {
        this.satinAlmaDao = satinAlmaDao;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public AyarlarBean getAyarlarBean() {
        return ayarlarBean;
    }

    public void setAyarlarBean(AyarlarBean ayarlarBean) {
        this.ayarlarBean = ayarlarBean;
    }

    public List<MailListSatinAlma> getMailListSatinAlmaListSource() {
        return mailListSatinAlmaListSource;
    }

    public void setMailListSatinAlmaListSource(List<MailListSatinAlma> mailListSatinAlmaListSource) {
        this.mailListSatinAlmaListSource = mailListSatinAlmaListSource;
    }

    public List<MailListSatinAlma> getMailListSatinAlmaListTarget() {
        return mailListSatinAlmaListTarget;
    }

    public void setMailListSatinAlmaListTarget(List<MailListSatinAlma> mailListSatinAlmaListTarget) {
        this.mailListSatinAlmaListTarget = mailListSatinAlmaListTarget;
    }

    public DualListModel<MailListSatinAlma> getMailListSatinAlmaList() {
        return mailListSatinAlmaList;
    }

    public void setMailListSatinAlmaList(DualListModel<MailListSatinAlma> mailListSatinAlmaList) {
        this.mailListSatinAlmaList = mailListSatinAlmaList;
    }

    public List<MAIL> getGonderilenMailler() {
        return gonderilenMailler;
    }

    public void setGonderilenMailler(List<MAIL> gonderilenMailler) {
        this.gonderilenMailler = gonderilenMailler;
    }

    public MAIL_ADRES getSecilenMailAdres() {
        return secilenMailAdres;
    }

    public void setSecilenMailAdres(MAIL_ADRES secilenMailAdres) {
        this.secilenMailAdres = secilenMailAdres;
    }

    public List<TEKLIF_SATIR_YANIT> getTeklifSatirYanitlari() {
        return teklifSatirYanitlari;
    }

    public void setTeklifSatirYanitlari(List<TEKLIF_SATIR_YANIT> teklifSatirYanitlari) {
        this.teklifSatirYanitlari = teklifSatirYanitlari;
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

    public List<TEKLIF_BASLIK> getGizlenenTeklifBasliklari() {
        return gizlenenTeklifBasliklari;
    }

    public void setGizlenenTeklifBasliklari(List<TEKLIF_BASLIK> gizlenenTeklifBasliklari) {
        this.gizlenenTeklifBasliklari = gizlenenTeklifBasliklari;
    }

    public List<TEKLIF_BASLIK> getGizlenenTeklifBasliklariFiltreleme() {
        return gizlenenTeklifBasliklariFiltreleme;
    }

    public void setGizlenenTeklifBasliklariFiltreleme(List<TEKLIF_BASLIK> gizlenenTeklifBasliklariFiltreleme) {
        this.gizlenenTeklifBasliklariFiltreleme = gizlenenTeklifBasliklariFiltreleme;
    }

    public List<TEKLIF_BASLIK> getSecilenGizlenmisTeklifBaslikListesi() {
        return secilenGizlenmisTeklifBaslikListesi;
    }

    public void setSecilenGizlenmisTeklifBaslikListesi(List<TEKLIF_BASLIK> secilenGizlenmisTeklifBaslikListesi) {
        this.secilenGizlenmisTeklifBaslikListesi = secilenGizlenmisTeklifBaslikListesi;
    }
    
    
}