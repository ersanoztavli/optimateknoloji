/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.beans.admin;

import com.oms.models.MAIL;
import com.oms.models.MAIL_DOSYA;
import com.oms.models.MailList;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
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
import org.primefaces.event.ToggleEvent;

/**
 *
 * @author ersan
 */

@ManagedBean(name="satisDetayGonderBean", eager = true)
@SessionScoped
public class SatisDetayGonderBean implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @ManagedProperty(value="#{satisBean}")
    private SatisBean satisBean;  
    
    private List<MailList> mailCcList;
    private List<MailList> mailBccList;
    
    private MAIL gonderilecekMail;   
    
    private MailList silinecekMailTo;
    private MailList silinecekMailCc;
    private MailList silinecekMailBcc;
    
    private MailList eklenecekMailTo;
    private MailList eklenecekMailCc;
    private MailList eklenecekMailBcc;
    
    private int language;
    
    @PostConstruct
    public void init() 
    {        
        try 
        {        
            mailCcList = new ArrayList<>();
            mailBccList = new ArrayList<>(); 
            
            eklenecekMailTo = new MailList();
            eklenecekMailCc = new MailList();
            eklenecekMailBcc = new MailList();

            gonderilecekMail = new MAIL();    
            //gonderilecekMail.setMAIL_GONDEREN(satisBean.getAyarlarBean().getEmailAyar().getEMAIL_ADRES());
            gonderilecekMail.setMAIL_GONDEREN(satisBean.getSessionBean().getKullanici().getMAIL_ADRESI());
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
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            //props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", satisBean.getAyarlarBean().getEmailAyar().getEMAIL_SUNUCU_ADI());
            props.put("mail.smtp.port", satisBean.getAyarlarBean().getEmailAyar().getEMAIL_SUNUCU_PORT_NUMARASI());

            Session session = Session.getInstance(props,
              new javax.mail.Authenticator() 
              {
                    protected PasswordAuthentication getPasswordAuthentication() 
                    {
                       return new PasswordAuthentication(satisBean.getAyarlarBean().getEmailAyar().getEMAIL_ADRES()
                                                        , satisBean.getAyarlarBean().getEmailAyar().getEMAIL_SIFRE());
                    }
              });

            //Önce tmp klasörün içerisi temizleniyor...
            //Daha önce aynı dosya varsa, "daha önce böyle bir dosya var" uyarısı veriyor çünkü
            FileUtils.cleanDirectory(
                    new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") 
                                    + "resources/tmp") );
            
            
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(gonderilecekMail.getMAIL_GONDEREN()));
            message.setSubject(gonderilecekMail.getMAIL_KONU());
            
            MimeMultipart multipart = new MimeMultipart();
            
            //Mail'in attachment kısmı
            for(MAIL_DOSYA mailDosya : satisBean.getMailDosyaList())
            {
                MimeBodyPart attachment = new MimeBodyPart();
                //Dosyayı klasöre kopyalıyorum...
                Files.copy(mailDosya.getIcerik(), new File(
                        FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") 
                                + "resources/tmp", mailDosya.getMAIL_DOSYA_ADI()).toPath());

                //Daha sonra klasörden dosyayı okuyorum...
                FileDataSource source = new FileDataSource(
                        FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") 
                                + "resources/tmp/" + mailDosya.getMAIL_DOSYA_ADI());
                
                attachment.setDataHandler(new DataHandler(source));
                attachment.setDisposition(Part.ATTACHMENT);
                attachment.setFileName(mailDosya.getMAIL_DOSYA_ADI());
                multipart.addBodyPart(attachment);
            }
           
            String mailAlacaklarTo = "";
            String mailAlacaklarCc = "";
            String mailAlacaklarBcc = "";
            
            for (MailList mailTo : satisBean.getMailToList())  
            { 
                mailAlacaklarTo = mailAlacaklarTo + mailTo.getMailAdress() + ",";
            }
            for (MailList mailCc : satisBean.getMailCcList())  
            { 
                mailAlacaklarCc = mailAlacaklarCc + mailCc.getMailAdress() + ",";
            }
            for (MailList mailBcc : satisBean.getMailBccList())  
            { 
                mailAlacaklarBcc = mailAlacaklarBcc + mailBcc.getMailAdress() + ",";
            }
                        
            InternetAddress[] parseMailTo = InternetAddress.parse(mailAlacaklarTo , true);
            InternetAddress[] parseMailCc = InternetAddress.parse(mailAlacaklarCc , true);
            InternetAddress[] parseMailBcc = InternetAddress.parse(mailAlacaklarBcc , true);
            
            //Kime
            message.setRecipients(javax.mail.Message.RecipientType.TO,  parseMailTo);
            //Carbon Copy
            message.setRecipients(javax.mail.Message.RecipientType.CC,  parseMailCc);
            //Blind Carbon Copy
            message.setRecipients(javax.mail.Message.RecipientType.BCC,  parseMailBcc);
            
            String mailLink = gonderilecekMail.getMAIL_ICERIK()
                            + "\n\n"
                            + "http://" + satisBean.getAyarlarBean().getSessionBean().getServerConfig().getString("serverNameOrIp") 
                            + ":" 
                            + satisBean.getAyarlarBean().getSessionBean().getServerConfig().getString("serverPort") 
                            + "/OMS/user/";
            
            if(language == 0)
                mailLink = mailLink + "offerUser.jsf";
            else if(language == 1)            
                mailLink = mailLink + "offerUserEnglish.jsf";        
                mailLink = mailLink + "?guid=" 
                                + satisBean.getSecilenTeklifBaslik().getTEKLIF_GUID();
            
            //message.setText(mailLink);
            //Mail'in içerik kısmı
            BodyPart icerik = new MimeBodyPart();  
            icerik.setText(mailLink);
            multipart.addBodyPart(icerik); 
            message.setContent(multipart);

            Transport.send(message);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Teklif mail'i başarıyla gönderilmiştir.", ""));
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklif mail'i gönderilirken hata oluştu.", ""));
        }
    }
    
    
    
    public void silMailTo() 
    {
         try 
         {            
            satisBean.getMailToList().remove(silinecekMailTo);   
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Adres başarıyla silinmiştir.", ""));
         } 
         catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Adres silme işleminde hata meydana geldi.", ""));
         }
    }
    
    public void silMailCc() 
    {
         try 
         {            
            satisBean.getMailCcList().remove(silinecekMailCc);   
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Adres başarıyla silinmiştir.", ""));
         } 
         catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Adres silme işleminde hata meydana geldi.", ""));
         }
    }
    
    public void silMailBcc() 
    {
         try 
         {            
            satisBean.getMailBccList().remove(silinecekMailBcc);   
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Adres başarıyla silinmiştir.", ""));
         } 
         catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Adres silme işleminde hata meydana geldi.", ""));
         }
    }
    
    public void silMailDosya() 
    {
         try 
         {            
            mailBccList.remove(silinecekMailBcc);   
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Adres başarıyla silinmiştir.", ""));
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
            satisBean.getMailToList().add(eklenecekMailTo);   
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Adres başarıyla eklenmiştir.", ""));
            eklenecekMailTo = new MailList();
         } 
         catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Adres ekleme işleminde hata meydana geldi.", ""));
         }
    }   
    
    public void kaydetYeniMailCc()
    {
         try 
         {  
            eklenecekMailCc.setMailNumber(eklenecekMailCc.getMailAdress()); 
            satisBean.getMailCcList().add(eklenecekMailCc);   
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Adres başarıyla eklenmiştir.", ""));
            eklenecekMailCc = new MailList();
         } 
         catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Adres ekleme işleminde hata meydana geldi.", ""));
         }
    }
    
    public void kaydetYeniMailBcc()
    {
         try 
         {  
            eklenecekMailBcc.setMailNumber(eklenecekMailBcc.getMailAdress()); 
            satisBean.getMailBccList().add(eklenecekMailBcc);   
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Adres başarıyla eklenmiştir.", ""));
            eklenecekMailBcc = new MailList();
         } 
         catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Adres ekleme işleminde hata meydana geldi.", ""));
         }
    }   
    
    
    public void handleToggleMailCcEkle(ToggleEvent event) 
    {
        eklenecekMailCc = new MailList();
    }
    
    public void handleToggleMailBccEkle(ToggleEvent event) 
    {
        eklenecekMailBcc = new MailList();
    }
    
    public SatisBean getSatisBean() {
        return satisBean;
    }

    public void setSatisBean(SatisBean satisBean) {
        this.satisBean = satisBean;
    }
    
    public List<MailList> getMailCcList() {
        return mailCcList;
    }

    public void setMailCcList(List<MailList> mailCcList) {
        this.mailCcList = mailCcList;
    }

    public List<MailList> getMailBccList() {
        return mailBccList;
    }

    public void setMailBccList(List<MailList> mailBccList) {
        this.mailBccList = mailBccList;
    }

    public MAIL getGonderilecekMail() {
        return gonderilecekMail;
    }

    public void setGonderilecekMail(MAIL gonderilecekMail) {
        this.gonderilecekMail = gonderilecekMail;
    }

    public MailList getSilinecekMailTo() {
        return silinecekMailTo;
    }

    public void setSilinecekMailTo(MailList silinecekMailTo) {
        this.silinecekMailTo = silinecekMailTo;
    }

    public MailList getSilinecekMailCc() {
        return silinecekMailCc;
    }

    public void setSilinecekMailCc(MailList silinecekMailCc) {
        this.silinecekMailCc = silinecekMailCc;
    }

    public MailList getSilinecekMailBcc() {
        return silinecekMailBcc;
    }

    public void setSilinecekMailBcc(MailList silinecekMailBcc) {
        this.silinecekMailBcc = silinecekMailBcc;
    }

    public MailList getEklenecekMailTo() {
        return eklenecekMailTo;
    }

    public void setEklenecekMailTo(MailList eklenecekMailTo) {
        this.eklenecekMailTo = eklenecekMailTo;
    }

    public MailList getEklenecekMailCc() {
        return eklenecekMailCc;
    }

    public void setEklenecekMailCc(MailList eklenecekMailCc) {
        this.eklenecekMailCc = eklenecekMailCc;
    }

    public MailList getEklenecekMailBcc() {
        return eklenecekMailBcc;
    }

    public void setEklenecekMailBcc(MailList eklenecekMailBcc) {
        this.eklenecekMailBcc = eklenecekMailBcc;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }
    
}
