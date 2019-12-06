/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.beans.user;

import com.oms.dao.OfferUserDao;
import com.oms.models.EMAIL_AYAR;
import com.oms.models.MAIL;
import com.oms.models.MAIL_ADRES;
import com.oms.models.TEKLIF_BASLIK;
import com.oms.models.TEKLIF_SATIR_YANIT;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ersan
 */
@ManagedBean(name="offerUserSatinAlmaBean")
@SessionScoped
public class OfferUserSatinAlmaBean {
    
    private int mailAdresId;
    private TEKLIF_BASLIK teklifBaslik;
    private OfferUserDao offerUserDao;
    private List<TEKLIF_SATIR_YANIT> teklifSatirYanitlari;
    private TEKLIF_SATIR_YANIT guncellenecekTeklifSatirYanit;
    private List<String> birimler;
    private boolean disabled;
    private double toplamTutar;
    
    private MAIL_ADRES mailAdres;
    
    private String mailAdresiTedarikci;
    
    public void ilkCalisan(ComponentSystemEvent event)
    {
        if (!FacesContext.getCurrentInstance().isPostback())
        {
            try 
            {
                guncellenecekTeklifSatirYanit = new TEKLIF_SATIR_YANIT();
                offerUserDao = new OfferUserDao();
                teklifBaslik = offerUserDao.getirTeklifBaslikSatinAlma(mailAdresId);
                                
                //Tamamlanıp tamamlanmadığını öğrenmek için...
                mailAdres = offerUserDao.getirMailAdresMailAdresIdIle(mailAdresId);
                
                //Daha önce yanıt verildiyse buton pasif hale gelsin
                if(mailAdres.getTAMAMLANDI_MI()>0)
                    disabled = true;
                else 
                    disabled = false;
                
                teklifSatirYanitlari = offerUserDao.getirTeklifSatirYanitlari(mailAdresId); 
                
                toplamTutar = 0.0;
                for(TEKLIF_SATIR_YANIT teklifSatirYanit : teklifSatirYanitlari)
                {
                   teklifSatirYanit.setBirimListesi(
                                offerUserDao.getirBirimlerERPden(
                                        "22",teklifSatirYanit.getMalzemeHizmetMasrafKodu()));
                
                    //Çaktırmadan toplamTutarı da hesaplamış oldum.
                    toplamTutar = toplamTutar + (teklifSatirYanit.getMIKTAR() * teklifSatirYanit.getBIRIM_FIYATI());
                }
                
                mailAdresiTedarikci = "";
            } 
            catch (Exception ex) 
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sayfa başlatılırken hata oluştu.", ""));
            }
        }
    }
   
    public void guncelleTeklifSatirYanit(CellEditEvent event) 
    {
        try 
        {
//            guncellenecekTeklifSatirYanit = teklifSatirYanitlari.get(event.getRowIndex());
//            
//           offerUserDao.guncelleTeklifSatirYanit(guncellenecekTeklifSatirYanit);
            
            //Güncelleme işleminden sonra tabloyu tekrar çağırıyoruz...
//            teklifSatirYanitlari = offerUserDao.getirTeklifSatirYanitlari(mailAdresId);
//            for(TEKLIF_SATIR_YANIT teklifSatirYanit : teklifSatirYanitlari)
//            {
//               teklifSatirYanit.setBirimListesi(
//                            offerUserDao.getirBirimlerERPden(
//                                    "22",teklifSatirYanit.getMalzemeHizmetMasrafKodu()));
//            }
            //Güncellenen satırın içerisini boşaltıyoruz...
            guncellenecekTeklifSatirYanit = new TEKLIF_SATIR_YANIT();
             
            toplamTutar = 0.0;
            //Toplam tutarı tekrar hesaplıyoruz...
            for(TEKLIF_SATIR_YANIT teklifSatirYanit : teklifSatirYanitlari)
            {
              toplamTutar = toplamTutar + (teklifSatirYanit.getMIKTAR() * teklifSatirYanit.getBIRIM_FIYATI());
            }
        }
        catch (Exception ex) 
        {
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklif Satırı güncelleme işleminde hata meydana geldi.", ""));
        }
    }
    
    public void guncelleKur() 
    {
        try 
        {   
            if(mailAdres.getPARA_BIRIMI()!=null)
            {                
                if(mailAdres.getPARA_BIRIMI().equals("TL"))
                    mailAdres.setKUR(1.0000);
                else
                {
                    
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(new URL("https://www.tcmb.gov.tr/kurlar/today.xml").openStream());

                    doc.getDocumentElement().normalize();

                    NodeList nList = doc.getElementsByTagName("Currency");

                    for (int temp = 0; temp < nList.getLength(); temp++) 
                    {
                        Node nNode = nList.item(temp);				

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) 
                        {
                            Element eElement = (Element) nNode;

                            if(mailAdres.getPARA_BIRIMI().equals(eElement.getAttribute("CurrencyCode")))
                            {
                                mailAdres.setKUR(
                                            Double.valueOf(eElement.getElementsByTagName("BanknoteSelling").item(0).getTextContent())
                                                                            );
                                break;
                            }
                        }
                    }
                }
            }            
        } 
        
        catch (Exception ex) 
        {
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kur güncelleme işleminde hata meydana geldi.", ""));
        }
    }
        
    public void teklifiTamamla() 
    {
      try 
        {   
            for(TEKLIF_SATIR_YANIT teklifSatirYanit : teklifSatirYanitlari)
            {
               offerUserDao.guncelleTeklifSatirYanit(teklifSatirYanit);
            }            
            
            offerUserDao.tamamlaMailAdres(mailAdresId, 
                                          mailAdres.getTeklifYanitlayanAdSoyad(),
                                          mailAdres.getPARA_BIRIMI(),
                                          mailAdres.getKUR());            
            
            //MAIL_ADRES mailAdres = offerUserDao.getirMailAdresMailAdresIdIle(mailAdresId);
            MAIL mail = offerUserDao.getirMailMailIdIle(mailAdres.getMAIL_ID());
            EMAIL_AYAR emailAyar = offerUserDao.getirEmailAyar();            
            
            //////////////////////////////////
            //Firmaya mail gönderme kısmı (tedarikçiye değil)...
            
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");        
            props.put("mail.smtp.host", emailAyar.getEMAIL_SUNUCU_ADI());
            props.put("mail.smtp.port", emailAyar.getEMAIL_SUNUCU_PORT_NUMARASI());

            Session session = Session.getInstance(props,
                                                    new javax.mail.Authenticator() 
                                                    {
                                                       protected PasswordAuthentication getPasswordAuthentication() 
                                                       {
                                                         return new PasswordAuthentication(emailAyar.getEMAIL_ADRES(), emailAyar.getEMAIL_SIFRE());
                                                       }
                                                    }
                                                  );

            Message messageFirma = new MimeMessage(session);
            messageFirma.setFrom(new InternetAddress(mailAdres.getMAIL_ADRESI()));
            messageFirma.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mail.getMAIL_GONDEREN()));
            messageFirma.setSubject("TEDARİKÇİ TEKLİF GİRİŞİNİ TAMAMLADI!");
            messageFirma.setText(" Tedarikçi: " +teklifBaslik.getMusteriUnvani() + "\n"
                            + " Teklif No.: " + teklifBaslik.getTEKLIF_NUMARASI() + "\n"
                            + " Teklif Girişi Yapan: " + mailAdres.getTeklifYanitlayanAdSoyad() + "\n\n"
                            + " Tedarikçiye gönderdiğiniz mail'in konusu ve içeriği aşağıdadır.\n"
                            + " Konu: " + mail.getMAIL_KONU() + "\n"
                            + " İçerik: " + mail.getMAIL_ICERIK());

            Transport.send(messageFirma);            
            
            disabled = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Teklif tamamlama isteğiniz başarıyla gönderilmiştir. İlginiz için teşekkür ederiz.", ""));
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklif yanıtınız gönderilirken hata oluştu.", ""));
        }
    }
    
    
    public void mailGonderTedarikci() 
    {
      try 
        { 
            MAIL mail = offerUserDao.getirMailMailIdIle(mailAdres.getMAIL_ID());
            EMAIL_AYAR emailAyar = offerUserDao.getirEmailAyar();      
            
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");        
            props.put("mail.smtp.host", emailAyar.getEMAIL_SUNUCU_ADI());
            props.put("mail.smtp.port", emailAyar.getEMAIL_SUNUCU_PORT_NUMARASI());

            Session session = Session.getInstance(props,
                                                    new javax.mail.Authenticator() 
                                                    {
                                                       protected PasswordAuthentication getPasswordAuthentication() 
                                                       {
                                                         return new PasswordAuthentication(emailAyar.getEMAIL_ADRES(), emailAyar.getEMAIL_SIFRE());
                                                       }
                                                    }
                                                  );
            //Tedarikçiye mail gönderme kısmı pdf ve excel'i ek olarak koyacağız...
            List<File> pdfVeXlsx = offerUserDao.olusturDokuman( mailAdresId,
                                                                teklifBaslik.getTEKLIF_NUMARASI(),
                                                                teklifBaslik.getMusteriUnvani(),
                                                                teklifBaslik.getMusteriAdres(),
                                                                toplamTutar,
                                                                mailAdres.getPARA_BIRIMI());
            
            
            Message messageTedarikci = new MimeMessage(session);
            messageTedarikci.setFrom(new InternetAddress(mail.getMAIL_GONDEREN()));
            messageTedarikci.setSubject("TEKLİF GİRİŞİNİ TAMAMLADINIZ!");
            
            MimeMultipart multipart = new MimeMultipart();
            
            //Mail'in attachment kısmı
            for(File file : pdfVeXlsx)
            {
                MimeBodyPart attachment = new MimeBodyPart();

                FileDataSource source = new FileDataSource(file.getAbsolutePath());

                attachment.setDataHandler(new DataHandler(source));
                attachment.setDisposition(javax.mail.Part.ATTACHMENT);
                attachment.setFileName(file.getName());
                multipart.addBodyPart(attachment);
            }
            
            messageTedarikci.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mailAdresiTedarikci));            
            String mailLink = " Firma : " +teklifBaslik.getFirmaUnvan() + "\n"
                              + " Teklif No.: " + teklifBaslik.getTEKLIF_NUMARASI() + "\n"
                              + " Teklif Girişi Yapan: " + mailAdres.getTeklifYanitlayanAdSoyad() + ".\n\n"
                              + " İlginiz için teşekkür ederiz.\n";

            BodyPart icerik = new MimeBodyPart();  
            icerik.setText(mailLink);
            multipart.addBodyPart(icerik);
            messageTedarikci.setContent(multipart);
            Transport.send(messageTedarikci);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mail başarıyla gönderilmiştir.", ""));
            //////////////////////////////////
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mail gönderilirken hata oluştu.", ""));
        }
    }
        
    
    public void downloadPdf()
    {
        try {
                offerUserDao.download(mailAdresId,
                                      teklifBaslik.getTEKLIF_NUMARASI(),
                                      teklifBaslik.getMusteriUnvani(),
                                      teklifBaslik.getMusteriAdres(),
                                      toplamTutar,
                                      mailAdres.getPARA_BIRIMI(),
                                      "pdf");
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pdf indirilirken hata oluştu.", ""));
        }
    }
    
    public void downloadXlsx()
    {
        try {
                offerUserDao.download(mailAdresId,
                                      teklifBaslik.getTEKLIF_NUMARASI(),
                                      teklifBaslik.getMusteriUnvani(),
                                      teklifBaslik.getMusteriAdres(),
                                      toplamTutar,
                                      mailAdres.getPARA_BIRIMI(),
                                      "xlsx");
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Xlsx indirilirken hata oluştu.", ""));
        }
    }
    
    public void onRowCancel(RowEditEvent event) {
        guncellenecekTeklifSatirYanit = new TEKLIF_SATIR_YANIT();
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

    public List<TEKLIF_SATIR_YANIT> getTeklifSatirYanitlari() {
        return teklifSatirYanitlari;
    }

    public void setTeklifSatirYanitlari(List<TEKLIF_SATIR_YANIT> teklifSatirYanitlari) {
        this.teklifSatirYanitlari = teklifSatirYanitlari;
    }

    public int getMailAdresId() {
        return mailAdresId;
    }

    public void setMailAdresId(int mailAdresId) {
        this.mailAdresId = mailAdresId;
    }

    public TEKLIF_SATIR_YANIT getGuncellenecekTeklifSatirYanit() {
        return guncellenecekTeklifSatirYanit;
    }

    public void setGuncellenecekTeklifSatirYanit(TEKLIF_SATIR_YANIT guncellenecekTeklifSatirYanit) {
        this.guncellenecekTeklifSatirYanit = guncellenecekTeklifSatirYanit;
    }

    public List<String> getBirimler() {
        return birimler;
    }

    public void setBirimler(List<String> birimler) {
        this.birimler = birimler;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public MAIL_ADRES getMailAdres() {
        return mailAdres;
    }

    public void setMailAdres(MAIL_ADRES mailAdres) {
        this.mailAdres = mailAdres;
    }

    public double getToplamTutar() {
        return toplamTutar;
    }

    public void setToplamTutar(double toplamTutar) {
        this.toplamTutar = toplamTutar;
    }

    public String getMailAdresiTedarikci() {
        return mailAdresiTedarikci;
    }

    public void setMailAdresiTedarikci(String mailAdresiTedarikci) {
        this.mailAdresiTedarikci = mailAdresiTedarikci;
    }
    
}
