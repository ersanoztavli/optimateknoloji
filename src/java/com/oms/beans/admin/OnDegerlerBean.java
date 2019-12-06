/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.beans.admin;

import com.oms.dao.OnDegerlerDao;
import com.oms.models.ERP_FIRMA;
import com.oms.models.FIRMA;
import com.oms.models.KULLANICI;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author ersan
 */
@ManagedBean(name="onDegerlerBean", eager = true)
@SessionScoped
public class OnDegerlerBean implements Serializable{
    private static final long serialVersionUID = 1L;
    
    //Kullanıcı ile ilgli alanlar...
    private List<KULLANICI> kullanicilar;
    private List<FIRMA> kullaniciFirmalari;
    private KULLANICI eklenecekYeniKullanici;
    private KULLANICI guncellenecekKullanici;
    private KULLANICI silinecekKullanici;
    private List<ERP_FIRMA> erpFirmalari;
    
    //Firma ile ilgili alanlar...
    private List<FIRMA> firmalar;
    private FIRMA eklenecekYeniFirma;
    private FIRMA silinecekFirma;
    private FIRMA guncellenecekFirma;
     
    private OnDegerlerDao onDegerlerDao;
    
    @ManagedProperty(value="#{sessionBean}")
    private SessionBean sessionBean;
    
    @PostConstruct
    public void init() 
    {          
        try 
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            sessionBean = (SessionBean)facesContext.getApplication().createValueBinding("#{sessionBean}").getValue(facesContext);
        
            kullanicilar = new ArrayList<>();
            firmalar = new ArrayList<>();
            //Sadece firma Id'si ve firma unvanını çekiyoruz... 
            kullaniciFirmalari = new ArrayList<>();
            erpFirmalari= new ArrayList<>();
            
            eklenecekYeniKullanici = new KULLANICI();
            eklenecekYeniKullanici.setAktifPasifTutarBoolean(false);
            
            eklenecekYeniFirma = new FIRMA();
            guncellenecekKullanici = new KULLANICI();
            silinecekKullanici = new KULLANICI();
            silinecekFirma = new FIRMA();
            //Veri tabanından kullanıcılar, ön değer firmaları ile birlikte getirilirler... 
            onDegerlerDao = new OnDegerlerDao();
            
            //Kullanıcı ile ilgili alanlar...
            setKullaniciFirmalari(onDegerlerDao.getirKullaniciFirmalari());
            setErpFirmalari(onDegerlerDao.getirERPFirmalari());
            setKullanicilar(onDegerlerDao.getirKullanicilar());
            
            
            //Firma ile ilgili alanlar...
            setFirmalar(onDegerlerDao.getirFirmalar());
        } 
        catch (Exception ex) 
        {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sayfa başlatılırken hata meydana geldi.", ""));
        }
    }
    
    public void kaydetYeniKullanici()
    {
        try 
        {
            onDegerlerDao.kaydetYeniKullanici(eklenecekYeniKullanici, sessionBean.getKullanici().getKULLANICI_ID());
            //Kayıt işleminden sonra tabloyu yeniden çağırıyoruz...
            setKullanicilar(onDegerlerDao.getirKullanicilar());
            //Yeni eklenen kullanıcının içerisini boşaltıyoruz...
            eklenecekYeniKullanici = new KULLANICI();
            eklenecekYeniKullanici.setAktifPasifTutarBoolean(false);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Kullanıcı başarıyla kaydedilmiştir.", ""));

        } 
        catch (Exception ex) 
        {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kullanıcı kaydetme işleminde meydana geldi.", ""));
        }        
    }        
    
    public void kaydetYeniFirma()
    {
        try 
        {
            onDegerlerDao.kaydetYeniFirma(eklenecekYeniFirma, sessionBean.getKullanici().getKULLANICI_ID());
            //Kayıt işleminden sonra tabloyu yeniden çağırıyoruz...
            setFirmalar(onDegerlerDao.getirFirmalar());
            //Yeni eklenen kullanıcının içerisini boşaltıyoruz...
            eklenecekYeniFirma = new FIRMA();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Firma başarıyla kaydedilmiştir.", ""));

        } 
        catch (Exception ex) 
        {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Firma kaydetme işleminde hata meydana geldi.", ""));
        }        
    }
    
        
    public void onTabChange(TabChangeEvent event) {
        try 
        {
            setKullaniciFirmalari(onDegerlerDao.getirKullaniciFirmalari());
        } 
        catch (Exception ex) 
        {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tab geçişi sırasında hata meydana geldi.", ""));
        }
    }
      

    public void guncelleKullanici(RowEditEvent event) 
    {
          try 
         {
            guncellenecekKullanici = (KULLANICI) event.getObject();
            onDegerlerDao.guncelleKullanici(guncellenecekKullanici, sessionBean.getKullanici().getKULLANICI_ID());
            
            //Güncelleme işleminden sonra tabloyu tekrar çağırıyoruz...
            setKullanicilar(onDegerlerDao.getirKullanicilar());
            //Güncellenen kullanıcının içerisini boşaltıyoruz...
            guncellenecekKullanici = new KULLANICI();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Kullanıcı başarıyla güncellenmiştir.", ""));

         } 
          catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kullanıcı güncelleme işleminde hata meydana geldi.", ""));
         }
    }
    
    public void guncelleFirma(RowEditEvent event) 
    {
          try 
         {
            guncellenecekFirma = (FIRMA) event.getObject();
            onDegerlerDao.guncelleFirma(guncellenecekFirma, sessionBean.getKullanici().getKULLANICI_ID());            
            //Güncelleme işleminden sonra tabloyu tekrar çağırıyoruz...
            setFirmalar(onDegerlerDao.getirFirmalar());
            //Güncellenen firmanın içerisini boşaltıyoruz...
            guncellenecekFirma = new FIRMA();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Firma başarıyla güncellenmiştir.", ""));

         } catch (Exception ex) 
         {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Firma güncelleme işleminde hata meydana geldi.", ""));
         }
    }
     
    public void silKullanici() 
    {
          try 
         {            
            onDegerlerDao.silKullanici(silinecekKullanici, sessionBean.getKullanici().getKULLANICI_ID());            
            //Silme işleminden sonra taloyu tekrar çağırıyoruz...
            setKullanicilar(onDegerlerDao.getirKullanicilar());
            //Silinen kullanıcının içerisini boşaltıyoruz...
            silinecekKullanici = new KULLANICI();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Kullanıcı başarıyla silinmiştir.", ""));

         } catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kullanıcı silme işleminde hata meydana geldi.", ""));
         }
    }
    
    public void silFirma() 
    {
          try 
         {            
            onDegerlerDao.silFirma(silinecekFirma, sessionBean.getKullanici().getKULLANICI_ID());            
            //Silme işleminden sonra taloyu tekrar çağırıyoruz...
            setFirmalar(onDegerlerDao.getirFirmalar());
            //Silinen kullanıcının içerisini boşaltıyoruz...
            silinecekFirma = new FIRMA();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Firma başarıyla silinmiştir.", ""));

         } catch (Exception ex) 
         {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Firma silme işleminde hata meydana geldi.", ""));
         }
    }
      
    public void onRowCancel(RowEditEvent event) {
        guncellenecekKullanici = new KULLANICI();
    } 
    
    public void onRowCancel1(RowEditEvent event) {
        guncellenecekFirma = new FIRMA();
    }   
     
    public List<KULLANICI> getKullanicilar() {
        return kullanicilar;
    }

    public void setKullanicilar(List<KULLANICI> kullanicilar) {
        this.kullanicilar = kullanicilar;
    }    

    public List<FIRMA> getKullaniciFirmalari() {
        return kullaniciFirmalari;
    }

    public void setKullaniciFirmalari(List<FIRMA> kullaniciFirmalari) {
        this.kullaniciFirmalari = kullaniciFirmalari;
    }    

    public KULLANICI getEklenecekYeniKullanici() {
        return eklenecekYeniKullanici;
    }

    public void setEklenecekYeniKullanici(KULLANICI eklenecekYeniKullanici) {
        this.eklenecekYeniKullanici = eklenecekYeniKullanici;
    }

    public OnDegerlerDao getOnDegerlerDao() {
        return onDegerlerDao;
    }

    public void setOnDegerlerDao(OnDegerlerDao onDegerlerDao) {
        this.onDegerlerDao = onDegerlerDao;
    }

    public KULLANICI getGuncellenecekKullanici() {
        return guncellenecekKullanici;
    }

    public void setGuncellenecekKullanici(KULLANICI guncellenecekKullanici) {
        this.guncellenecekKullanici = guncellenecekKullanici;
    }

    public KULLANICI getSilinecekKullanici() {
        return silinecekKullanici;
    }

    public void setSilinecekKullanici(KULLANICI silinecekKullanici) {
        this.silinecekKullanici = silinecekKullanici;
    }

    public List<FIRMA> getFirmalar() {
        return firmalar;
    }

    public void setFirmalar(List<FIRMA> firmalar) {
        this.firmalar = firmalar;
    }

    public FIRMA getEklenecekYeniFirma() {
        return eklenecekYeniFirma;
    }

    public void setEklenecekYeniFirma(FIRMA eklenecekYeniFirma) {
        this.eklenecekYeniFirma = eklenecekYeniFirma;
    }

    public FIRMA getSilinecekFirma() {
        return silinecekFirma;
    }

    public void setSilinecekFirma(FIRMA silinecekFirma) {
        this.silinecekFirma = silinecekFirma;
    }

    public FIRMA getGuncellenecekFirma() {
        return guncellenecekFirma;
    }

    public void setGuncellenecekFirma(FIRMA guncellenecekFirma) {
        this.guncellenecekFirma = guncellenecekFirma;
    }

    public List<ERP_FIRMA> getErpFirmalari() {
        return erpFirmalari;
    }

    public void setErpFirmalari(List<ERP_FIRMA> erpFirmalari) {
        this.erpFirmalari = erpFirmalari;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }
    
    
    
}
