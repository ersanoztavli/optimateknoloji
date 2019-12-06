/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.beans.admin;

import com.oms.models.MAIL;
import com.oms.models.MAIL_DOSYA;
import com.oms.models.MailListSatinAlma;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.BodyPart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.tomcat.util.http.fileupload.FileUtils;

/**
 *
 * @author ersan
 */
@ManagedBean(name="satinAlmaDetayGonderBean", eager = true)
@SessionScoped
public class SatinAlmaDetayGonderBean implements Serializable{

    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new instance of SatinAlmaDetayBean
     */
    public SatinAlmaDetayGonderBean() {
    }
    
    @ManagedProperty(value="#{satinAlmaBean}")
    private SatinAlmaBean satinAlmaBean; 
    
    private MAIL gonderilecekMail;
    
    @PostConstruct
    public void init() 
    {        
        try 
        {        
            gonderilecekMail = new MAIL();    
            //gonderilecekMail.setMAIL_GONDEREN(satinAlmaBean.getAyarlarBean().getEmailAyar().getEMAIL_ADRES());
            gonderilecekMail.setMAIL_GONDEREN(satinAlmaBean.getSessionBean().getKullanici().getMAIL_ADRESI());
        }
        catch (Exception ex) 
        {            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sayfa başlatılırken hata oluştu.", ""));
        }
    }
    
    public void mailGonder() 
    {
      try 
        {
            int adreslerTam = 1;
            
            //Hiç adres girilmemişse mail göndermesin...
            if(satinAlmaBean.getMailListSatinAlmaListTarget().isEmpty())
                adreslerTam = 0;
            else//Adres var ama boş mu diye, adresler kontrol edilsin...
            {
                for(MailListSatinAlma mailListSatinAlma :satinAlmaBean.getMailListSatinAlmaListTarget())
                {
                    //Adres boşsa hiç mail göndermesin...
                    if(mailListSatinAlma.getMailAdres().isEmpty())
                    adreslerTam = 0;
                }
            }
            
            if(adreslerTam == 1)
            {
                //Önce veri tabanına mail'i ve adresleri kaydediyoruz...
                satinAlmaBean.getSatinAlmaDao().kaydetMailSatinAlma(gonderilecekMail.getMAIL_GONDEREN()
                                                                  , gonderilecekMail.getMAIL_KONU()
                                                                  , gonderilecekMail.getMAIL_ICERIK()
                                                                  , satinAlmaBean.getSecilenTeklifBaslik().getTEKLIF_BASLIK_ID()
                                                                  , satinAlmaBean.getMailListSatinAlmaListTarget()
                                                                  , satinAlmaBean.getSessionBean().getKullanici().getKULLANICI_ID());

                //Daha sonra her bir müşteri için TEKLIF_SATIR_YANIT tablosuna kayıt açıyoruz ki
                //Müşteriler kendi değişkenlerini satırlara girebilsin...
                satinAlmaBean.getSatinAlmaDao().kaydetTeklifSatirYanit(satinAlmaBean.getSatinAlmaDao().getirMailAdresler(
                                                                            satinAlmaBean.getSatinAlmaDao().getirMailIdTeklifBaslikIdIle(
                                                                                    satinAlmaBean.getSecilenTeklifBaslik().getTEKLIF_BASLIK_ID()))
                                                                      ,satinAlmaBean.getTeklifSatirlari()
                                                                      ,satinAlmaBean.getSessionBean().getKullanici().getKULLANICI_ID()
                                                                      ,satinAlmaBean.getSessionBean().getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());


                //////////////////////////////////////////////////////////////////////////
                //Daha sonra veri tabanından mailAdresId'yi çekip link ile gönderiyoruz...

                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
               // props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", satinAlmaBean.getAyarlarBean().getEmailAyar().getEMAIL_SUNUCU_ADI());
                props.put("mail.smtp.port", satinAlmaBean.getAyarlarBean().getEmailAyar().getEMAIL_SUNUCU_PORT_NUMARASI());

                Session session = Session.getInstance(props,
                  new javax.mail.Authenticator() 
                  {
                        protected PasswordAuthentication getPasswordAuthentication() 
                        {
                           return new PasswordAuthentication(satinAlmaBean.getAyarlarBean().getEmailAyar().getEMAIL_ADRES()
                                                            , satinAlmaBean.getAyarlarBean().getEmailAyar().getEMAIL_SIFRE());
                        }
                  });
                
                
                //Önce tmp klasörün içerisi tamizleniyor...
                //Daha önce aynı dosya varsa, "daha önce böyle bir dosya var" uyarısı veriyor çünkü
                FileUtils.cleanDirectory(
                        new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") 
                                        + "resources/tmp"));
                
                for(MailListSatinAlma mailListSatinAlma :satinAlmaBean.getMailListSatinAlmaListTarget())
                {                    
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(gonderilecekMail.getMAIL_GONDEREN()));
                    message.setSubject(gonderilecekMail.getMAIL_KONU());   
                                  
                    MimeMultipart multipart = new MimeMultipart();
                    /////////////////////////////////////////
                    
                    //Mail'in attachment kısmı
                    for(MAIL_DOSYA mailDosya : satinAlmaBean.getMailDosyaList())
                    {
                        MimeBodyPart attachment = new MimeBodyPart();
                        
                        //Dosyanın varlığını test ediyoruz..
                        //Eğer dosya var ise olanı kopyalıyoruz...
                        //Eğer yok ise önce kopyalıyoruz daha sonra aktarıyoruz...
                        File tempFile = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") 
                                                 + "resources/tmp" , mailDosya.getMAIL_DOSYA_ADI());                        
                        FileDataSource source = null;
                        
                        //Eğer var ise...
                        if(tempFile.exists())
                        {
                            //Daha sonra klasörden dosyayı okuyorum...
                            source = new FileDataSource(
                                    FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") 
                                            + "resources/tmp/" + mailDosya.getMAIL_DOSYA_ADI());
                        }
                        
                        //Eğer yok ise...
                        else
                        {
                            //Dosyayı klasöre kopyalıyorum...
                            Files.copy(mailDosya.getIcerik(), new File(
                                    FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") 
                                            + "resources/tmp" , mailDosya.getMAIL_DOSYA_ADI()).toPath());

                            //Daha sonra klasörden dosyayı okuyorum...
                            source = new FileDataSource(
                                    FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") 
                                            + "resources/tmp/" + mailDosya.getMAIL_DOSYA_ADI());
                        }
                        
                        
                        attachment.setDataHandler(new DataHandler(source));
                        attachment.setDisposition(Part.ATTACHMENT);
                        attachment.setFileName(mailDosya.getMAIL_DOSYA_ADI());
                        multipart.addBodyPart(attachment);
                    }

                    String mailLink = "";
                    String mailLinkBase = gonderilecekMail.getMAIL_ICERIK()
                                        + "\n\n"
                                        + "http://" 
                                        + satinAlmaBean.getAyarlarBean().getSessionBean().getServerConfig().getString("serverNameOrIp") 
                                        + ":" 
                                        + satinAlmaBean.getAyarlarBean().getSessionBean().getServerConfig().getString("serverPort") 
                                        + "/OMS/user/offerUserSatinAlma.jsf";
                    
                    mailLink = mailLinkBase + "?mailAdresId=" 
                                            + satinAlmaBean.getSatinAlmaDao().getirMailAdresId(mailListSatinAlma.getMusteriRecordId());
                    
                    //Mail'in içerik kısmı
                    BodyPart icerik = new MimeBodyPart();  
                    icerik.setText(mailLink);
                    multipart.addBodyPart(icerik);  
                    
                    message.setContent(multipart);

                    InternetAddress[] parseMailSatinAlmaTo = InternetAddress.parse(mailListSatinAlma.getMailAdres() , true);
                    message.setRecipients(javax.mail.Message.RecipientType.TO,  parseMailSatinAlmaTo);
                    
                    InternetAddress[] parseMailSatinAlmaBcc = InternetAddress.parse(gonderilecekMail.getMAIL_GONDEREN(), true);
                    
                    message.setRecipients(javax.mail.Message.RecipientType.BCC,  parseMailSatinAlmaBcc);
                    
                    Transport.send(message);
                }

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Teklif mail'i başarıyla gönderilmiştir.", ""));
            }
            
            else 
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Lütfen mail adreslerini kontrol ediniz.", ""));
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklif mail'i gönderilirken hata oluştu.", ""));
        }
    }

    public SatinAlmaBean getSatinAlmaBean() {
        return satinAlmaBean;
    }

    public void setSatinAlmaBean(SatinAlmaBean satinAlmaBean) {
        this.satinAlmaBean = satinAlmaBean;
    }

    public MAIL getGonderilecekMail() {
        return gonderilecekMail;
    }

    public void setGonderilecekMail(MAIL gonderilecekMail) {
        this.gonderilecekMail = gonderilecekMail;
    }
    
}
