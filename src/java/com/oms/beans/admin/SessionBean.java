/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.beans.admin;

import com.oms.dao.SessionDao;
import com.oms.models.KULLANICI;
import com.oms.models.VERI_TABANI_AYAR;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.sql.*;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author ersan
 */
@ManagedBean(name="sessionBean", eager = true)
@SessionScoped
public class SessionBean implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private KULLANICI kullanici;
    
    private VERI_TABANI_AYAR OMSVeriTabaniAyar;
    private SessionDao sessionDao;
   
    @ManagedProperty("#{dbConfig}")
    private ResourceBundle dbConfig;   
    
    @ManagedProperty("#{serverConfig}")
    private ResourceBundle serverConfig;
    
    
    @PostConstruct
    public void init() 
    {
        try 
        {   
            sessionDao = new SessionDao();
            kullanici = new KULLANICI();
            
            OMSVeriTabaniAyar = new VERI_TABANI_AYAR();            
            //dbConfig içerisindeki parametreleri otomatik olarak çekiyoruz...
            OMSVeriTabaniAyar.setSUNUCU_ADI(dbConfig.getString("dbHost"));
            OMSVeriTabaniAyar.setSUNUCU_PORT_NUMARASI(dbConfig.getString("dbPort"));
            OMSVeriTabaniAyar.setVERI_TABANI_ADI(dbConfig.getString("dbName"));
            OMSVeriTabaniAyar.setKULLANICI_ADI(dbConfig.getString("dbUser"));
            OMSVeriTabaniAyar.setKULLANICI_SIFRE(dbConfig.getString("dbPassword"));   
        } 
        catch (Exception ex) 
        {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sayfa başlatılırken hata meydana geldi.", ""));
        }
    }
    
    
    public SessionBean() {
        
    }   
    
    public String action()
    {
        try 
        {            
            KULLANICI kayitliKullanici = new KULLANICI();            
            kayitliKullanici = sessionDao.getirKullanici(kullanici);
            
            if(kayitliKullanici.getKULLANICI_ID()>0)
            {                
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("validUser", kayitliKullanici);
                kullanici.setAD(kayitliKullanici.getAD());
                kullanici.setSOYAD(kayitliKullanici.getSOYAD());
                kullanici.setMAIL_ADRESI(kayitliKullanici.getMAIL_ADRESI());
                kullanici.setERP_ON_DEGER_FIRMA_NUMBER(kayitliKullanici.getERP_ON_DEGER_FIRMA_NUMBER());
                kullanici.setERP_ON_DEGER_FIRMA_UNVAN(kayitliKullanici.getERP_ON_DEGER_FIRMA_UNVAN());
                kullanici.setON_DEGER_FIRMA_UNVAN(kayitliKullanici.getON_DEGER_FIRMA_UNVAN());
                kullanici.setON_DEGER_FIRMA_ID(kayitliKullanici.getON_DEGER_FIRMA_ID());
                kullanici.setKULLANICI_ID(kayitliKullanici.getKULLANICI_ID());
                kullanici.setFirmaMailAdresi(kayitliKullanici.getFirmaMailAdresi());
                kullanici.setAktifPasifTutarBoolean(kayitliKullanici.isAktifPasifTutarBoolean());
                
                return "/admin/pages/welcomeAdmin.jsf?faces-redirect=true";
            }
            else
            {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("validUser", null);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Kullanıcı adınız veya şifreniz hatalı", ""));
                
                return "";
            }
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OMS veri tabanı bağlantısı hatalı", ""));
            return "";
        }
    }
    
    public String logout()
    {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("validUser");
        final HttpServletRequest httpServletRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        httpServletRequest.getSession(false).invalidate();
        return "/admin/login.jsf?faces-redirect=true";
    }    
    
    public void baglantiTestEtOMS()
    {       
        try 
        {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");            
            String connectionString = "jdbc:sqlserver://" + OMSVeriTabaniAyar.getSUNUCU_ADI() + ":" 
                                      + OMSVeriTabaniAyar.getSUNUCU_PORT_NUMARASI() 
                                      + ";databaseName=" + OMSVeriTabaniAyar.getVERI_TABANI_ADI();

            Connection connection = 
                    DriverManager.getConnection(connectionString, OMSVeriTabaniAyar.getKULLANICI_ADI(), OMSVeriTabaniAyar.getKULLANICI_SIFRE());

            if (connection!= null) 
            {
                connection.close();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OMS veri tabanı bağlantısı başarılı.", ""));
            }              
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OMS veri tabanı bağlantısı hatalı.", ""));
        }
    }
    
    public void kaydetBaglantiOMS()
    {
       try 
        {
            //İlk başta DbConnection parametreleri setlenmediği için 
            //İşlemlerden önce doldurmak zorundayız...
            kaydetDbConnectionBaslangicParametreler(OMSVeriTabaniAyar);            
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OMS bağlantısı başarıyla kaydedilmiştir.", ""));
        } 
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OMS bağlantısı kaydetme işlemi başarısız.", ""));
        }
    }
    
    public void kaydetDbConnectionBaslangicParametreler(VERI_TABANI_AYAR veriTabaniAyar)
    {
       try 
        { 
            //Normalde burada dbConfig dosyasına setleme yapabilmem lazım amam yapamadım.
//            dbConfig.getString("dbHost")
//            dbConfig.getString("dbPort")
//            dbConfig.getString("dbName")
//            dbConfig.getString("dbUser")
//            dbConfig.getString("dbPassword")
//                    
//            OMSVeriTabaniAyar.getSUNUCU_ADI();
//            OMSVeriTabaniAyar.getSUNUCU_PORT_NUMARASI();
//            OMSVeriTabaniAyar.getVERI_TABANI_ADI();
//            OMSVeriTabaniAyar.getKULLANICI_ADI();
//            OMSVeriTabaniAyar.getKULLANICI_SIFRE();             
        }
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OMS bağlantısı kaydetme işlemi başarısız.", ""));
        }
    }
    
    public VERI_TABANI_AYAR getOMSVeriTabaniAyar() {
        return OMSVeriTabaniAyar;
    }

    public void setOMSVeriTabaniAyar(VERI_TABANI_AYAR OMSVeriTabaniAyar) {
        this.OMSVeriTabaniAyar = OMSVeriTabaniAyar;
    }

    public ResourceBundle getDbConfig() {
        return dbConfig;
    }

    public void setDbConfig(ResourceBundle dbConfig) {
        this.dbConfig = dbConfig;
    }

    public KULLANICI getKullanici() {
        return kullanici;
    }

    public void setKullanici(KULLANICI kullanici) {
        this.kullanici = kullanici;
    }

    public SessionDao getSessionDao() {
        return sessionDao;
    }

    public void setSessionDao(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    public ResourceBundle getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ResourceBundle serverConfig) {
        this.serverConfig = serverConfig;
    }  
    
    
}
