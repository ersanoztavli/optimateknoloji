/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.beans.admin;

import com.oms.dao.AyarlarDao;
import com.oms.models.EMAIL_AYAR;
import com.oms.models.REST_SERVIS_AYAR;
import com.oms.models.VERI_TABANI_AYAR;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Base64;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author ersan
 */
@ManagedBean(name="ayarlarBean", eager = true)
@SessionScoped
public class AyarlarBean implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private VERI_TABANI_AYAR ERPVeriTabaniAyar;
    private EMAIL_AYAR emailAyar;
    private REST_SERVIS_AYAR restServisAyar;
    private AyarlarDao ayarlarDao;     

    @ManagedProperty(value="#{sessionBean}")
    private SessionBean sessionBean;
    
    @PostConstruct
    public void init() 
    {          
        try 
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            sessionBean = (SessionBean)facesContext.getApplication().createValueBinding("#{sessionBean}").getValue(facesContext);
        
            restServisAyar = new REST_SERVIS_AYAR();
            ERPVeriTabaniAyar = new VERI_TABANI_AYAR();
            emailAyar = new EMAIL_AYAR();
            ayarlarDao = new AyarlarDao();
            
            setERPVeriTabaniAyar(ayarlarDao.getirVeriTabaniAyar(0));
            setEmailAyar(ayarlarDao.getirEmailAyar());  
            setRestServisAyar(ayarlarDao.getirRestServisAyar());
        } 
        catch (Exception ex) 
        {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sayfa başlatılırken hata meydana geldi.", ""));
        }
    }
    
    /**
     * 
     */
    public void baglantiTestEtERP()
    {       
        try 
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");            
            String connectionString = "jdbc:sqlserver://" + ERPVeriTabaniAyar.getSUNUCU_ADI() + ":" 
                                      + ERPVeriTabaniAyar.getSUNUCU_PORT_NUMARASI() 
                                      + ";databaseName=" + ERPVeriTabaniAyar.getVERI_TABANI_ADI();

            Connection connection = 
                    DriverManager.getConnection(connectionString, ERPVeriTabaniAyar.getKULLANICI_ADI(), ERPVeriTabaniAyar.getKULLANICI_SIFRE());

            if (connection!= null) 
            {
                connection.close();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ERP veri tabanı bağlantısı başarılı.", ""));
            }              
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERP veri tabanı bağlantısı hatalı.", ""));
        }
    }   
    
    public void kaydetBaglantiERP()
    {
        try 
        {
            //0'sa ERP, 1'se OMS için kayıt yapılacak...
            //Eğer kayıt yoksa yeni kayıt açılacak.
            //Eğer kayı varsa güncellenecek.
            if(ERPVeriTabaniAyar.getVERI_TABANI_AYAR_ID()>0)
                ayarlarDao.guncelleVeriTabaniAyar(ERPVeriTabaniAyar, sessionBean.getKullanici().getKULLANICI_ID());
            else
            {
                ayarlarDao.kaydetVeriTabaniAyar(ERPVeriTabaniAyar, 0, sessionBean.getKullanici().getKULLANICI_ID());
                //Kaydettikten sonra kaydedileni çekiyoruz...
                setERPVeriTabaniAyar(ayarlarDao.getirVeriTabaniAyar(0));
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ERP bağlantısı başarıyla kaydedilmiştir.", ""));
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERP bağlantısı kaydetme işlemi başarısız.", ""));
        }
    }
    
    public void kaydetEmailAyar()
    {
       try 
        {
            //0'sa ERP, 1'se OMS için kayıt yapılacak...
            //Eğer kayıt yoksa yeni kayıt açılacak.
            //Eğer kayı varsa güncellenecek.
            if(emailAyar.getEMAIL_AYAR_ID()>0)
                ayarlarDao.guncelleEmailAyar(emailAyar, sessionBean.getKullanici().getKULLANICI_ID());
            else
            {
                ayarlarDao.kaydetEmailAyar(emailAyar, sessionBean.getKullanici().getKULLANICI_ID());
                //Kaydettikten sonra kaydedileni çekiyoruz...
                setEmailAyar(ayarlarDao.getirEmailAyar());
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "E-mail ayarı başarıyla kaydedilmiştir.", ""));
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "E-mail ayarı kaydetme işlemi başarısız.", ""));
        }
    }
    
    public void kaydetRestServisAyar()
    {
       try 
        {
            //Eğer kayıt yoksa yeni kayıt açılacak.
            //Eğer kayıt varsa güncellenecek.
            if(restServisAyar.getREST_SERVIS_AYAR_ID()>0)
                ayarlarDao.guncelleRestServisAyar(restServisAyar, sessionBean.getKullanici().getKULLANICI_ID());
            else
            {
                ayarlarDao.kaydetRestServisAyar(restServisAyar, sessionBean.getKullanici().getKULLANICI_ID());
                //Kaydettikten sonra kaydedileni çekiyoruz...
                setRestServisAyar(ayarlarDao.getirRestServisAyar());
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Rest servis ayarı başarıyla kaydedilmiştir.", ""));
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Rest servis ayarı kaydetme işlemi başarısız.", ""));
        }
    }
    
    public void gonderTestEmail()
    {
       try 
        {
//        final String username = "ersan.oztavli@erkoteknoloji.com";
//        final String password = "123ersan.123";

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
          });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(emailAyar.getEMAIL_ADRES()));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(emailAyar.getEMAIL_ADRES()));
                message.setSubject("OMS E-MAİL TESTİ");
                message.setText("BU E-MAİL OMS TEKLİF YÖNETİM YAZILIMI'NDAN TEST AMAÇLI GÖNDERİLMİŞTİR.");

                Transport.send(message);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Test e-mail'i başarıyla gönderilmiştir.\n Lütfen e-mail'inizi kontrol ediniz", ""));

        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Test e-mail'i gönderme işlemi başarısız.", ""));
        }
    }

    public void restServisTestEt()
    {
       try 
        {
            URL url = new URL("http://" + restServisAyar.getREST_SERVIS_SUNUCU_ADI_IP() + ":" + restServisAyar.getREST_SERVIS_SUNUCU_PORT() + "/api/v1/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            
            String clientIdSecret = restServisAyar.getREST_SERVIS_CLIENT_ID() + ":" + restServisAyar.getREST_SERVIS_CLIENT_SECRET();
            String encodeBytes = Base64.getEncoder().encodeToString(clientIdSecret.getBytes());
            conn.setRequestProperty("Authorization", "Basic " + encodeBytes);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	    conn.setRequestProperty("Accept", "application/json");
            
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            writer.write("grant_type=password&username=" + 
                          restServisAyar.getREST_SERVIS_USERNAME() + 
                          "&firmno=" + restServisAyar.getREST_SERVIS_FIRMNO() + 
                          "&password=" + restServisAyar.getREST_SERVIS_PASSWORD() );
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
        
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Rest servis bağlantısı başarılı.", ""));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, token, ""));
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Rest servis bağlantısı başarısız.", ""));
        }
    }
    
    
    
    public VERI_TABANI_AYAR getERPVeriTabaniAyar() {
        return ERPVeriTabaniAyar;
    }

    public void setERPVeriTabaniAyar(VERI_TABANI_AYAR ERPVeriTabaniAyar) {
        this.ERPVeriTabaniAyar = ERPVeriTabaniAyar;
    }

    public EMAIL_AYAR getEmailAyar() {
        return emailAyar;
    }

    public void setEmailAyar(EMAIL_AYAR emailAyar) {
        this.emailAyar = emailAyar;
    }

    public AyarlarDao getAyarlarDao() {
        return ayarlarDao;
    }

    public void setAyarlarDao(AyarlarDao ayarlarDao) {
        this.ayarlarDao = ayarlarDao;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public REST_SERVIS_AYAR getRestServisAyar() {
        return restServisAyar;
    }

    public void setRestServisAyar(REST_SERVIS_AYAR restServisAyar) {
        this.restServisAyar = restServisAyar;
    }
    
}
