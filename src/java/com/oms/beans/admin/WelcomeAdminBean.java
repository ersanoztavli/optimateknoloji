/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.beans.admin;

import com.oms.dao.WelcomeAdminDao;
import com.oms.models.AyBazindaLineChartRawData;
import com.oms.models.AyBazindaLineChart;
import com.oms.models.GunBazindaPieChart;
import com.oms.models.GunBazindaPieChartRawData;
import com.oms.models.SonDetayliHareketler;
import com.oms.models.SonHareketler;
import com.oms.models.YilBazindaBarChart;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author ersan
 */
@ManagedBean(name="welcomeAdminBean" , eager = true)
@SessionScoped
public class WelcomeAdminBean implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private BarChartModel barModel1;
    private BarChartModel barModel2;
    private LineChartModel lineModel1; // 2018 Teklif adeti
    private LineChartModel lineModel2; // 2018 diğerleri
    private LineChartModel lineModel3; // 2019 Teklif adeti
    private LineChartModel lineModel4; // 2018 diğerleri
    private PieChartModel pieModel1; // 2018 Teklif adeti
    private PieChartModel pieModel3; // 2019 Teklif adeti
    private BarChartModel pieModelToBarModel2; // 2018 Diğerleri
    private BarChartModel pieModelToBarModel4; // 2019 Diğerleri
    private WelcomeAdminDao welcomeAdminDao; 
    private List<YilBazindaBarChart> yilBazindaBarChartList;
    
    private List<AyBazindaLineChartRawData> ayBazindaLineChartRawDataList;
    private List<AyBazindaLineChart> ayBazindaLineChart2018List;
    private List<AyBazindaLineChart> ayBazindaLineChart2019List;
    
    private List<GunBazindaPieChartRawData> gunBazindaPieChartRawDataList;
    private List<GunBazindaPieChart> gunBazindaPieChart2018List;
    private List<GunBazindaPieChart> gunBazindaPieChart2019List;
    
    private List<SonHareketler> sonHareketlerList;
    private List<SonDetayliHareketler> sonDetayliHareketlerList;
    /**
     * Creates a new instance of WelcomeAdminBean
     */
    public WelcomeAdminBean() {
        
    }
    
    
    @ManagedProperty(value="#{sessionBean}")
    private SessionBean sessionBean;  
    
    @PostConstruct
    public void init() {
        try 
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            sessionBean = (SessionBean)facesContext.getApplication().createValueBinding("#{sessionBean}").getValue(facesContext);
            welcomeAdminDao = new WelcomeAdminDao();

            ///////////////////////////////////////////////////////////////////////////////////
            //Yıl bazında
            yilBazindaBarChartList = new ArrayList<>();
            yilBazindaBarChartList = welcomeAdminDao.yilBazinda(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
            createBarModel(yilBazindaBarChartList);            
            
            ///////////////////////////////////////////////////////////////////////////////////
            //Ay bazında
            List<AyBazindaLineChartRawData> ayBazindaLineChartRawDataList = new ArrayList<>();
            List<AyBazindaLineChartRawData> ayBazindaLineChartRawData2018List = new ArrayList<>();
            List<AyBazindaLineChartRawData> ayBazindaLineChartRawData2019List = new ArrayList<>();            
            List<AyBazindaLineChart> ayBazindaLineChartList = new ArrayList<>();            
            ayBazindaLineChart2018List = new ArrayList<>();
            ayBazindaLineChart2019List = new ArrayList<>();

            ayBazindaLineChartRawDataList = welcomeAdminDao.ayBazinda(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
            
            for(AyBazindaLineChartRawData ayBazindaLineChartRawData: ayBazindaLineChartRawDataList)
            {                
                if(ayBazindaLineChartRawData.getYil().equals("2018"))
                    ayBazindaLineChartRawData2018List.add(ayBazindaLineChartRawData);                
                if(ayBazindaLineChartRawData.getYil().equals("2019"))
                    ayBazindaLineChartRawData2019List.add(ayBazindaLineChartRawData);
            }
            
            AyBazindaLineChart ayBazindaLineChart1 = new AyBazindaLineChart();//Adet
            ayBazindaLineChart1.setAybazindaTip(0);
            ayBazindaLineChart1.setYil("2018");
            AyBazindaLineChart ayBazindaLineChart2 = new AyBazindaLineChart();//Miktar
            ayBazindaLineChart2.setAybazindaTip(1);
            ayBazindaLineChart2.setYil("2018");
            AyBazindaLineChart ayBazindaLineChart3 = new AyBazindaLineChart();//Dönüşmüş
            ayBazindaLineChart3.setAybazindaTip(2);
            ayBazindaLineChart3.setYil("2018");
            AyBazindaLineChart ayBazindaLineChart4 = new AyBazindaLineChart();//Dönüşmemiş
            ayBazindaLineChart4.setAybazindaTip(3);
            ayBazindaLineChart4.setYil("2018");
            
            AyBazindaLineChart ayBazindaLineChart5 = new AyBazindaLineChart();//Adet
            ayBazindaLineChart5.setAybazindaTip(0);
            ayBazindaLineChart5.setYil("2019");
            AyBazindaLineChart ayBazindaLineChart6 = new AyBazindaLineChart();//Miktar
            ayBazindaLineChart6.setAybazindaTip(1);
            ayBazindaLineChart6.setYil("2019");
            AyBazindaLineChart ayBazindaLineChart7 = new AyBazindaLineChart();//Dönüşmüş
            ayBazindaLineChart7.setAybazindaTip(2);
            ayBazindaLineChart7.setYil("2019");
            AyBazindaLineChart ayBazindaLineChart8 = new AyBazindaLineChart();//Dönüşmemiş
            ayBazindaLineChart8.setAybazindaTip(3);
            ayBazindaLineChart8.setYil("2019");
            
            //2018
            for(AyBazindaLineChartRawData ayBazindaLineChartRawData: ayBazindaLineChartRawData2018List)
            {                 
                if(ayBazindaLineChartRawData.getAy().equals("1"))
                {
                   ayBazindaLineChart1.setOcak(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart2.setOcak(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart3.setOcak(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart4.setOcak(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("2"))
                {
                   ayBazindaLineChart1.setSubat(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart2.setSubat(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart3.setSubat(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart4.setSubat(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("3"))
                {
                   ayBazindaLineChart1.setMart(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart2.setMart(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart3.setMart(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart4.setMart(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("4"))
                {
                   ayBazindaLineChart1.setNisan(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart2.setNisan(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart3.setNisan(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart4.setNisan(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("5"))
                {
                   ayBazindaLineChart1.setMayis(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart2.setMayis(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart3.setMayis(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart4.setMayis(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("6"))
                {
                   ayBazindaLineChart1.setHaziran(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart2.setHaziran(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart3.setHaziran(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart4.setHaziran(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("7"))
                {
                   ayBazindaLineChart1.setTemmuz(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart2.setTemmuz(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart3.setTemmuz(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart4.setTemmuz(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("8"))
                {
                   ayBazindaLineChart1.setAgustos(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart2.setAgustos(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart3.setAgustos(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart4.setAgustos(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("9"))
                {
                   ayBazindaLineChart1.setEylul(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart2.setEylul(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart3.setEylul(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart4.setEylul(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("10"))
                {
                   ayBazindaLineChart1.setEkim(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart2.setEkim(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart3.setEkim(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart4.setEkim(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("11"))
                {
                   ayBazindaLineChart1.setKasim(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart2.setKasim(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart3.setKasim(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart4.setKasim(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("12"))
                {
                   ayBazindaLineChart1.setAralik(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart2.setAralik(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart3.setAralik(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart4.setAralik(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
            }
            
            ///2019
            for(AyBazindaLineChartRawData ayBazindaLineChartRawData: ayBazindaLineChartRawData2019List)
            {                 
                if(ayBazindaLineChartRawData.getAy().equals("1"))
                {
                   ayBazindaLineChart5.setOcak(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart6.setOcak(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart7.setOcak(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart8.setOcak(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("2"))
                {
                   ayBazindaLineChart5.setSubat(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart6.setSubat(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart7.setSubat(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart8.setSubat(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("3"))
                {
                   ayBazindaLineChart5.setMart(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart6.setMart(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart7.setMart(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart8.setMart(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("4"))
                {
                   ayBazindaLineChart5.setNisan(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart6.setNisan(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart7.setNisan(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart8.setNisan(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("5"))
                {
                   ayBazindaLineChart5.setMayis(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart6.setMayis(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart7.setMayis(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart8.setMayis(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("6"))
                {
                   ayBazindaLineChart5.setHaziran(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart6.setHaziran(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart7.setHaziran(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart8.setHaziran(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("7"))
                {
                   ayBazindaLineChart5.setTemmuz(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart6.setTemmuz(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart7.setTemmuz(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart8.setTemmuz(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("8"))
                {
                   ayBazindaLineChart5.setAgustos(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart6.setAgustos(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart7.setAgustos(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart8.setAgustos(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("9"))
                {
                   ayBazindaLineChart5.setEylul(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart6.setEylul(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart7.setEylul(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart8.setEylul(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("10"))
                {
                   ayBazindaLineChart5.setEkim(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart6.setEkim(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart7.setEkim(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart8.setEkim(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("11"))
                {
                   ayBazindaLineChart5.setKasim(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart6.setKasim(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart7.setKasim(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart8.setKasim(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
                if(ayBazindaLineChartRawData.getAy().equals("12"))
                {
                   ayBazindaLineChart5.setAralik(ayBazindaLineChartRawData.getTeklifAdeti());
                   ayBazindaLineChart6.setAralik(ayBazindaLineChartRawData.getToplamTeklifMiktari());
                   ayBazindaLineChart7.setAralik(ayBazindaLineChartRawData.getSipariseDonusmus());
                   ayBazindaLineChart8.setAralik(ayBazindaLineChartRawData.getSipariseDonusmemis());
                }
            }          
            
            ayBazindaLineChartList.add(ayBazindaLineChart1);
            ayBazindaLineChartList.add(ayBazindaLineChart2);
            ayBazindaLineChartList.add(ayBazindaLineChart3);
            ayBazindaLineChartList.add(ayBazindaLineChart4);
            ayBazindaLineChartList.add(ayBazindaLineChart5);
            ayBazindaLineChartList.add(ayBazindaLineChart6);
            ayBazindaLineChartList.add(ayBazindaLineChart7);
            ayBazindaLineChartList.add(ayBazindaLineChart8);
            
            createLineModel(ayBazindaLineChartList);
            
            ///////////////////////////////////////////////////////////////////////////////////
            //Gün bazında
            
            List<GunBazindaPieChartRawData> gunBazindaPieChartRawDataList = new ArrayList<>();
            List<GunBazindaPieChartRawData> gunBazindaPieChartRawData2018List = new ArrayList<>();
            List<GunBazindaPieChartRawData> gunBazindaPieChartRawData2019List = new ArrayList<>();            
            List<GunBazindaPieChart> gunBazindaPieChartList = new ArrayList<>();            
            gunBazindaPieChart2018List = new ArrayList<>();
            gunBazindaPieChart2019List = new ArrayList<>();
            
            gunBazindaPieChartRawDataList = welcomeAdminDao.gunBazinda(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());

            for(GunBazindaPieChartRawData gunBazindaPieChartRawData: gunBazindaPieChartRawDataList)
            {                
                if(gunBazindaPieChartRawData.getYil().equals("2018"))
                    gunBazindaPieChartRawData2018List.add(gunBazindaPieChartRawData);                
                if(gunBazindaPieChartRawData.getYil().equals("2019"))
                    gunBazindaPieChartRawData2019List.add(gunBazindaPieChartRawData);
            }
            
            GunBazindaPieChart gunBazindaPieChart1 = new GunBazindaPieChart();//Adet
            gunBazindaPieChart1.setGunBazindaTip(0);
            gunBazindaPieChart1.setYil("2018");
            GunBazindaPieChart gunBazindaPieChart2 = new GunBazindaPieChart();//Miktar
            gunBazindaPieChart2.setGunBazindaTip(1);
            gunBazindaPieChart2.setYil("2018");
            GunBazindaPieChart gunBazindaPieChart3 = new GunBazindaPieChart();//Dönüşmüş
            gunBazindaPieChart3.setGunBazindaTip(2);
            gunBazindaPieChart3.setYil("2018");
            GunBazindaPieChart gunBazindaPieChart4 = new GunBazindaPieChart();//Dönüşmemiş
            gunBazindaPieChart4.setGunBazindaTip(3);
            gunBazindaPieChart4.setYil("2018");
            
            GunBazindaPieChart gunBazindaPieChart5 = new GunBazindaPieChart();//Adet
            gunBazindaPieChart5.setGunBazindaTip(0);
            gunBazindaPieChart5.setYil("2019");
            GunBazindaPieChart gunBazindaPieChart6 = new GunBazindaPieChart();//Miktar
            gunBazindaPieChart6.setGunBazindaTip(1);
            gunBazindaPieChart6.setYil("2019");
            GunBazindaPieChart gunBazindaPieChart7 = new GunBazindaPieChart();//Dönüşmüş
            gunBazindaPieChart7.setGunBazindaTip(2);
            gunBazindaPieChart7.setYil("2019");
            GunBazindaPieChart gunBazindaPieChart8 = new GunBazindaPieChart();//Dönüşmemiş
            gunBazindaPieChart8.setGunBazindaTip(3);
            gunBazindaPieChart8.setYil("2019");
            
            //2018
            for(GunBazindaPieChartRawData gunBazindaPieChartRawData: gunBazindaPieChartRawData2018List)
            {                 
                if(gunBazindaPieChartRawData.getGun().equals("1"))
                {
                   gunBazindaPieChart1.setPazartesi(gunBazindaPieChartRawData.getTeklifAdeti());
                   gunBazindaPieChart2.setPazartesi(gunBazindaPieChartRawData.getToplamTeklifMiktari());
                   gunBazindaPieChart3.setPazartesi(gunBazindaPieChartRawData.getSipariseDonusmus());
                   gunBazindaPieChart4.setPazartesi(gunBazindaPieChartRawData.getSipariseDonusmemis());
                }
                if(gunBazindaPieChartRawData.getGun().equals("2"))
                {
                   gunBazindaPieChart1.setSali(gunBazindaPieChartRawData.getTeklifAdeti());
                   gunBazindaPieChart2.setSali(gunBazindaPieChartRawData.getToplamTeklifMiktari());
                   gunBazindaPieChart3.setSali(gunBazindaPieChartRawData.getSipariseDonusmus());
                   gunBazindaPieChart4.setSali(gunBazindaPieChartRawData.getSipariseDonusmemis());
                }
                if(gunBazindaPieChartRawData.getGun().equals("3"))
                {
                   gunBazindaPieChart1.setCarsamba(gunBazindaPieChartRawData.getTeklifAdeti());
                   gunBazindaPieChart2.setCarsamba(gunBazindaPieChartRawData.getToplamTeklifMiktari());
                   gunBazindaPieChart3.setCarsamba(gunBazindaPieChartRawData.getSipariseDonusmus());
                   gunBazindaPieChart4.setCarsamba(gunBazindaPieChartRawData.getSipariseDonusmemis());
                }
                if(gunBazindaPieChartRawData.getGun().equals("4"))
                {
                   gunBazindaPieChart1.setPersembe(gunBazindaPieChartRawData.getTeklifAdeti());
                   gunBazindaPieChart2.setPersembe(gunBazindaPieChartRawData.getToplamTeklifMiktari());
                   gunBazindaPieChart3.setPersembe(gunBazindaPieChartRawData.getSipariseDonusmus());
                   gunBazindaPieChart4.setPersembe(gunBazindaPieChartRawData.getSipariseDonusmemis());
                }
                if(gunBazindaPieChartRawData.getGun().equals("5"))
                {
                   gunBazindaPieChart1.setCuma(gunBazindaPieChartRawData.getTeklifAdeti());
                   gunBazindaPieChart2.setCuma(gunBazindaPieChartRawData.getToplamTeklifMiktari());
                   gunBazindaPieChart3.setCuma(gunBazindaPieChartRawData.getSipariseDonusmus());
                   gunBazindaPieChart4.setCuma(gunBazindaPieChartRawData.getSipariseDonusmemis());
                }
                if(gunBazindaPieChartRawData.getGun().equals("6"))
                {
                   gunBazindaPieChart1.setCumartesi(gunBazindaPieChartRawData.getTeklifAdeti());
                   gunBazindaPieChart2.setCumartesi(gunBazindaPieChartRawData.getToplamTeklifMiktari());
                   gunBazindaPieChart3.setCumartesi(gunBazindaPieChartRawData.getSipariseDonusmus());
                   gunBazindaPieChart4.setCumartesi(gunBazindaPieChartRawData.getSipariseDonusmemis());
                }
                if(gunBazindaPieChartRawData.getGun().equals("7"))
                {
                   gunBazindaPieChart1.setPazar(gunBazindaPieChartRawData.getTeklifAdeti());
                   gunBazindaPieChart2.setPazar(gunBazindaPieChartRawData.getToplamTeklifMiktari());
                   gunBazindaPieChart3.setPazar(gunBazindaPieChartRawData.getSipariseDonusmus());
                   gunBazindaPieChart4.setPazar(gunBazindaPieChartRawData.getSipariseDonusmemis());
                }                
            }
            
            //2019
            for(GunBazindaPieChartRawData gunBazindaPieChartRawData: gunBazindaPieChartRawData2019List)
            {                 
                if(gunBazindaPieChartRawData.getGun().equals("1"))
                {
                   gunBazindaPieChart5.setPazartesi(gunBazindaPieChartRawData.getTeklifAdeti());
                   gunBazindaPieChart6.setPazartesi(gunBazindaPieChartRawData.getToplamTeklifMiktari());
                   gunBazindaPieChart7.setPazartesi(gunBazindaPieChartRawData.getSipariseDonusmus());
                   gunBazindaPieChart8.setPazartesi(gunBazindaPieChartRawData.getSipariseDonusmemis());
                }
                if(gunBazindaPieChartRawData.getGun().equals("2"))
                {
                   gunBazindaPieChart5.setSali(gunBazindaPieChartRawData.getTeklifAdeti());
                   gunBazindaPieChart6.setSali(gunBazindaPieChartRawData.getToplamTeklifMiktari());
                   gunBazindaPieChart7.setSali(gunBazindaPieChartRawData.getSipariseDonusmus());
                   gunBazindaPieChart8.setSali(gunBazindaPieChartRawData.getSipariseDonusmemis());
                }
                if(gunBazindaPieChartRawData.getGun().equals("3"))
                {
                   gunBazindaPieChart5.setCarsamba(gunBazindaPieChartRawData.getTeklifAdeti());
                   gunBazindaPieChart6.setCarsamba(gunBazindaPieChartRawData.getToplamTeklifMiktari());
                   gunBazindaPieChart7.setCarsamba(gunBazindaPieChartRawData.getSipariseDonusmus());
                   gunBazindaPieChart8.setCarsamba(gunBazindaPieChartRawData.getSipariseDonusmemis());
                }
                if(gunBazindaPieChartRawData.getGun().equals("4"))
                {
                   gunBazindaPieChart5.setPersembe(gunBazindaPieChartRawData.getTeklifAdeti());
                   gunBazindaPieChart6.setPersembe(gunBazindaPieChartRawData.getToplamTeklifMiktari());
                   gunBazindaPieChart7.setPersembe(gunBazindaPieChartRawData.getSipariseDonusmus());
                   gunBazindaPieChart8.setPersembe(gunBazindaPieChartRawData.getSipariseDonusmemis());
                }
                if(gunBazindaPieChartRawData.getGun().equals("5"))
                {
                   gunBazindaPieChart5.setCuma(gunBazindaPieChartRawData.getTeklifAdeti());
                   gunBazindaPieChart6.setCuma(gunBazindaPieChartRawData.getToplamTeklifMiktari());
                   gunBazindaPieChart7.setCuma(gunBazindaPieChartRawData.getSipariseDonusmus());
                   gunBazindaPieChart8.setCuma(gunBazindaPieChartRawData.getSipariseDonusmemis());
                }
                if(gunBazindaPieChartRawData.getGun().equals("6"))
                {
                   gunBazindaPieChart5.setCumartesi(gunBazindaPieChartRawData.getTeklifAdeti());
                   gunBazindaPieChart6.setCumartesi(gunBazindaPieChartRawData.getToplamTeklifMiktari());
                   gunBazindaPieChart7.setCumartesi(gunBazindaPieChartRawData.getSipariseDonusmus());
                   gunBazindaPieChart8.setCumartesi(gunBazindaPieChartRawData.getSipariseDonusmemis());
                }
                if(gunBazindaPieChartRawData.getGun().equals("7"))
                {
                   gunBazindaPieChart5.setPazar(gunBazindaPieChartRawData.getTeklifAdeti());
                   gunBazindaPieChart6.setPazar(gunBazindaPieChartRawData.getToplamTeklifMiktari());
                   gunBazindaPieChart7.setPazar(gunBazindaPieChartRawData.getSipariseDonusmus());
                   gunBazindaPieChart8.setPazar(gunBazindaPieChartRawData.getSipariseDonusmemis());
                }                
            }
            
            gunBazindaPieChartList.add(gunBazindaPieChart1);
            gunBazindaPieChartList.add(gunBazindaPieChart2);
            gunBazindaPieChartList.add(gunBazindaPieChart3);
            gunBazindaPieChartList.add(gunBazindaPieChart4);
            gunBazindaPieChartList.add(gunBazindaPieChart5);
            gunBazindaPieChartList.add(gunBazindaPieChart6);
            gunBazindaPieChartList.add(gunBazindaPieChart7);
            gunBazindaPieChartList.add(gunBazindaPieChart8);
            
            createPieModel(gunBazindaPieChartList);
            
            ///////////////////////////////////////////////////////////////////////////////////
            //Son hareketler (7 gün)
            sonHareketlerList = new ArrayList<>();
            sonHareketlerList = welcomeAdminDao.sonHareketler(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
                        
            ///////////////////////////////////////////////////////////////////////////////////
            //Son detaylı hareketler (10 gün)
            sonDetayliHareketlerList= new ArrayList<>();
            sonDetayliHareketlerList = welcomeAdminDao.sonDetayliHareketler(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());

            
        }
        catch (Exception ex) 
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sayfa başlatılırken hata oluştu.", ""));
        }
    }
    
    public void createBarModel(List<YilBazindaBarChart> yilBazindaBarChartList) 
    {
        List<BarChartModel> barChartModelList = initBarModel(yilBazindaBarChartList);
        barModel1 = barChartModelList.get(0); // Teklif miktarı
        barModel1.setLegendPosition("ne");
        
        barModel2 = barChartModelList.get(1); // diğerleri
        barModel2.setLegendPosition("ne");
    }
    
    public void createLineModel(List<AyBazindaLineChart> ayBazindaLineChartList) 
    {
        List<LineChartModel> lineChartModelList = initLinearModel(ayBazindaLineChartList);
        lineModel1 = lineChartModelList.get(0);
        lineModel1.setTitle("2018");
        lineModel1.setLegendPosition("se");
        lineModel1.getAxes().put(AxisType.X, new CategoryAxis(""));
        lineModel1.setShowPointLabels(true);    
        
        lineModel2 = lineChartModelList.get(1);
        lineModel2.setTitle("2018");
        lineModel2.setLegendPosition("se");
        lineModel2.getAxes().put(AxisType.X, new CategoryAxis(""));
        lineModel2.setShowPointLabels(true);
        
        lineModel3 = lineChartModelList.get(2);
        lineModel3.setTitle("2019");
        lineModel3.setLegendPosition("se");
        lineModel3.getAxes().put(AxisType.X, new CategoryAxis("Aylar"));
        lineModel3.setShowPointLabels(true);
        
        lineModel4 = lineChartModelList.get(3);
        lineModel4.setTitle("2019");
        lineModel4.setLegendPosition("se");
        lineModel4.getAxes().put(AxisType.X, new CategoryAxis("Aylar"));
        lineModel4.setShowPointLabels(true);
    }
    
     public void createPieModel(List<GunBazindaPieChart> gunBazindaPieChartList) 
    {        
        pieModel1 = new PieChartModel();
        pieModel3 = new PieChartModel();
        
        pieModel1 = initPieModel(gunBazindaPieChartList.get(0), "2018 - Teklif Adeti");//2018 Tekliflerin sayısı
        pieModel3 = initPieModel(gunBazindaPieChartList.get(4), "2019 - Teklif Adeti");//2019 Tekliflerin sayısı
    
        List<BarChartModel> barChartModelList = initPieModelToBarModel(gunBazindaPieChartList);
        pieModelToBarModel2 = barChartModelList.get(0); //2018 diğerleri
        pieModelToBarModel2.setLegendPosition("ne");
        pieModelToBarModel2.setTitle("2018");
        pieModelToBarModel4= barChartModelList.get(1);  //2019 diğerleri
        pieModelToBarModel4.setLegendPosition("ne");
        pieModelToBarModel4.setTitle("2019");
    }
    
    public List<BarChartModel> initPieModelToBarModel(List<GunBazindaPieChart> gunBazindaPieChartList) 
    {
        List<BarChartModel> barChartModelList = new ArrayList<>();
        BarChartModel model1 = new BarChartModel();
        BarChartModel model2 = new BarChartModel();
        
        model1.setAnimate(true);
        model2.setAnimate(true);
        
        ChartSeries chartSeries1 = new ChartSeries();//Miktar
        ChartSeries chartSeries2 = new ChartSeries();//Dönüşmüş
        ChartSeries chartSeries3 = new ChartSeries();//Dönüşmemiş
        
        ChartSeries chartSeries4 = new ChartSeries();//Miktar
        ChartSeries chartSeries5 = new ChartSeries();//Dönüşmüş
        ChartSeries chartSeries6 = new ChartSeries();//Dönüşmemiş

        chartSeries1.setLabel("Teklif Miktarı");
        chartSeries1.set("Pazartesi", gunBazindaPieChartList.get(1).getPazartesi());
        chartSeries1.set("Salı", gunBazindaPieChartList.get(1).getSali());
        chartSeries1.set("Çarşamba", gunBazindaPieChartList.get(1).getCarsamba());
        chartSeries1.set("Perşembe", gunBazindaPieChartList.get(1).getPersembe());
        chartSeries1.set("Cuma", gunBazindaPieChartList.get(1).getCuma());
        chartSeries1.set("Cumartesi", gunBazindaPieChartList.get(1).getCumartesi());
        chartSeries1.set("Pazar", gunBazindaPieChartList.get(1).getPazar());
        
        chartSeries2.setLabel("Siparişe Dönüşmüş");
        chartSeries2.set("Pazartesi", gunBazindaPieChartList.get(2).getPazartesi());
        chartSeries2.set("Salı", gunBazindaPieChartList.get(2).getSali());
        chartSeries2.set("Çarşamba", gunBazindaPieChartList.get(2).getCarsamba());
        chartSeries2.set("Perşembe", gunBazindaPieChartList.get(2).getPersembe());
        chartSeries2.set("Cuma", gunBazindaPieChartList.get(2).getCuma());
        chartSeries2.set("Cumartesi", gunBazindaPieChartList.get(2).getCumartesi());
        chartSeries2.set("Pazar", gunBazindaPieChartList.get(2).getPazar());
        
        chartSeries3.setLabel("Siparişe Dönüşmemiş");
        chartSeries3.set("Pazartesi", gunBazindaPieChartList.get(3).getPazartesi());
        chartSeries3.set("Salı", gunBazindaPieChartList.get(3).getSali());
        chartSeries3.set("Çarşamba", gunBazindaPieChartList.get(3).getCarsamba());
        chartSeries3.set("Perşembe", gunBazindaPieChartList.get(3).getPersembe());
        chartSeries3.set("Cuma", gunBazindaPieChartList.get(3).getCuma());
        chartSeries3.set("Cumartesi", gunBazindaPieChartList.get(3).getCumartesi());
        chartSeries3.set("Pazar", gunBazindaPieChartList.get(3).getPazar());
        
        chartSeries4.setLabel("Teklif Miktarı");
        chartSeries4.set("Pazartesi", gunBazindaPieChartList.get(5).getPazartesi());
        chartSeries4.set("Salı", gunBazindaPieChartList.get(5).getSali());
        chartSeries4.set("Çarşamba", gunBazindaPieChartList.get(5).getCarsamba());
        chartSeries4.set("Perşembe", gunBazindaPieChartList.get(5).getPersembe());
        chartSeries4.set("Cuma", gunBazindaPieChartList.get(5).getCuma());
        chartSeries4.set("Cumartesi", gunBazindaPieChartList.get(5).getCumartesi());
        chartSeries4.set("Pazar", gunBazindaPieChartList.get(5).getPazar());
        
        chartSeries5.setLabel("Siparişe Dönüşmüş");
        chartSeries5.set("Pazartesi", gunBazindaPieChartList.get(6).getPazartesi());
        chartSeries5.set("Salı", gunBazindaPieChartList.get(6).getSali());
        chartSeries5.set("Çarşamba", gunBazindaPieChartList.get(6).getCarsamba());
        chartSeries5.set("Perşembe", gunBazindaPieChartList.get(6).getPersembe());
        chartSeries5.set("Cuma", gunBazindaPieChartList.get(6).getCuma());
        chartSeries5.set("Cumartesi", gunBazindaPieChartList.get(6).getCumartesi());
        chartSeries5.set("Pazar", gunBazindaPieChartList.get(6).getPazar());
        
        chartSeries6.setLabel("Siparişe Dönüşmemiş");
        chartSeries6.set("Pazartesi", gunBazindaPieChartList.get(7).getPazartesi());
        chartSeries6.set("Salı", gunBazindaPieChartList.get(7).getSali());
        chartSeries6.set("Çarşamba", gunBazindaPieChartList.get(7).getCarsamba());
        chartSeries6.set("Perşembe", gunBazindaPieChartList.get(7).getPersembe());
        chartSeries6.set("Cuma", gunBazindaPieChartList.get(7).getCuma());
        chartSeries6.set("Cumartesi", gunBazindaPieChartList.get(7).getCumartesi());
        chartSeries6.set("Pazar", gunBazindaPieChartList.get(7).getPazar());


        model1.addSeries(chartSeries1);   
        model1.addSeries(chartSeries2);  
        model1.addSeries(chartSeries3);  
        
        model2.addSeries(chartSeries4);  
        model2.addSeries(chartSeries5);  
        model2.addSeries(chartSeries6);  
        
        barChartModelList.add(model1);
        barChartModelList.add(model2);
 
        return barChartModelList;
    }
     
    public PieChartModel initPieModel(GunBazindaPieChart gunBazindaPieChart, String title) 
    {      
        PieChartModel model = new PieChartModel();
        model.set("Pazartesi", gunBazindaPieChart.getPazartesi());
        model.set("Salı", gunBazindaPieChart.getSali());
        model.set("Çarşamba", gunBazindaPieChart.getCarsamba());
        model.set("Perşembe", gunBazindaPieChart.getPersembe());
        model.set("Cuma", gunBazindaPieChart.getCuma());
        model.set("Cumartesi", gunBazindaPieChart.getCumartesi());
        model.set("Pazar", gunBazindaPieChart.getPazar());
        model.setTitle(title);
        model.setLegendPosition("w");
        model.setShadow(false);
                
        return model;
    } 
    
    public List<LineChartModel>  initLinearModel(List<AyBazindaLineChart> ayBazindaLineChartList) 
    {
        List<LineChartModel> lineChartModelList = new ArrayList<>();
        
        LineChartModel model1 = new LineChartModel(); //2018 teklif miktarı
        LineChartModel model2 = new LineChartModel(); //2018 diğerleri
        LineChartModel model3 = new LineChartModel(); //2019 teklif miktarı
        LineChartModel model4 = new LineChartModel(); //2019 diğerleri
        
        model1.setAnimate(true); 
        model2.setAnimate(true);
        model3.setAnimate(true); 
        model4.setAnimate(true);
        
        ChartSeries chartSeries1 = new ChartSeries(); //2018 adet
        ChartSeries chartSeries2 = new ChartSeries(); //2018 miktar
        ChartSeries chartSeries3 = new ChartSeries(); //2018 dönüşmüş
        ChartSeries chartSeries4 = new ChartSeries(); //2018 dönüşmemiş
        ChartSeries chartSeries5 = new ChartSeries(); //2019 adet
        ChartSeries chartSeries6 = new ChartSeries(); //2019 miktar
        ChartSeries chartSeries7 = new ChartSeries(); //2019 dönüşmüş
        ChartSeries chartSeries8 = new ChartSeries(); //2019 dönüşmemiş
        
        for(AyBazindaLineChart ayBazindaLineChart : ayBazindaLineChartList)
        {        
             if(ayBazindaLineChart.getAybazindaTip()==0)
             {
                if(ayBazindaLineChart.getYil().equals("2018"))
                {
                    //2018 teklif miktari
                    chartSeries1.setLabel("Teklif Adeti");                      
                    chartSeries1.set("Ocak", ayBazindaLineChart.getOcak());
                    chartSeries1.set("Şubat", ayBazindaLineChart.getSubat());
                    chartSeries1.set("Mart", ayBazindaLineChart.getMart());
                    chartSeries1.set("Nisan", ayBazindaLineChart.getNisan());
                    chartSeries1.set("Mayis", ayBazindaLineChart.getMayis());
                    chartSeries1.set("Haziran", ayBazindaLineChart.getHaziran());
                    chartSeries1.set("Temmuz", ayBazindaLineChart.getTemmuz());
                    chartSeries1.set("Ağustos", ayBazindaLineChart.getAgustos());
                    chartSeries1.set("Eylül", ayBazindaLineChart.getEylul());
                    chartSeries1.set("Ekim", ayBazindaLineChart.getEkim());
                    chartSeries1.set("Kasım", ayBazindaLineChart.getKasim());
                    chartSeries1.set("Aralık", ayBazindaLineChart.getAralik());
                }
                else if(ayBazindaLineChart.getYil().equals("2019"))
                {
                    //2019 teklif miktari
                    chartSeries5.setLabel("Teklif Adeti");                      
                    chartSeries5.set("Ocak", ayBazindaLineChart.getOcak());
                    chartSeries5.set("Şubat", ayBazindaLineChart.getSubat());
                    chartSeries5.set("Mart", ayBazindaLineChart.getMart());
                    chartSeries5.set("Nisan", ayBazindaLineChart.getNisan());
                    chartSeries5.set("Mayis", ayBazindaLineChart.getMayis());
                    chartSeries5.set("Haziran", ayBazindaLineChart.getHaziran());
                    chartSeries5.set("Temmuz", ayBazindaLineChart.getTemmuz());
                    chartSeries5.set("Ağustos", ayBazindaLineChart.getAgustos());
                    chartSeries5.set("Eylül", ayBazindaLineChart.getEylul());
                    chartSeries5.set("Ekim", ayBazindaLineChart.getEkim());
                    chartSeries5.set("Kasım", ayBazindaLineChart.getKasim());
                    chartSeries5.set("Aralık", ayBazindaLineChart.getAralik());
                }
             }
             
             else if(ayBazindaLineChart.getAybazindaTip()==1)
             {
                if(ayBazindaLineChart.getYil().equals("2018"))
                {
                    //2018 teklif miktari
                    chartSeries2.setLabel("Teklif Miktarı");                      
                    chartSeries2.set("Ocak", ayBazindaLineChart.getOcak());
                    chartSeries2.set("Şubat", ayBazindaLineChart.getSubat());
                    chartSeries2.set("Mart", ayBazindaLineChart.getMart());
                    chartSeries2.set("Nisan", ayBazindaLineChart.getNisan());
                    chartSeries2.set("Mayis", ayBazindaLineChart.getMayis());
                    chartSeries2.set("Haziran", ayBazindaLineChart.getHaziran());
                    chartSeries2.set("Temmuz", ayBazindaLineChart.getTemmuz());
                    chartSeries2.set("Ağustos", ayBazindaLineChart.getAgustos());
                    chartSeries2.set("Eylül", ayBazindaLineChart.getEylul());
                    chartSeries2.set("Ekim", ayBazindaLineChart.getEkim());
                    chartSeries2.set("Kasım", ayBazindaLineChart.getKasim());
                    chartSeries2.set("Aralık", ayBazindaLineChart.getAralik());
                }
                else if(ayBazindaLineChart.getYil().equals("2019"))
                {
                    //2019 teklif miktari
                    chartSeries6.setLabel("Teklif Miktarı");                      
                    chartSeries6.set("Ocak", ayBazindaLineChart.getOcak());
                    chartSeries6.set("Şubat", ayBazindaLineChart.getSubat());
                    chartSeries6.set("Mart", ayBazindaLineChart.getMart());
                    chartSeries6.set("Nisan", ayBazindaLineChart.getNisan());
                    chartSeries6.set("Mayis", ayBazindaLineChart.getMayis());
                    chartSeries6.set("Haziran", ayBazindaLineChart.getHaziran());
                    chartSeries6.set("Temmuz", ayBazindaLineChart.getTemmuz());
                    chartSeries6.set("Ağustos", ayBazindaLineChart.getAgustos());
                    chartSeries6.set("Eylül", ayBazindaLineChart.getEylul());
                    chartSeries6.set("Ekim", ayBazindaLineChart.getEkim());
                    chartSeries6.set("Kasım", ayBazindaLineChart.getKasim());
                    chartSeries6.set("Aralık", ayBazindaLineChart.getAralik());
                }
             }
             
             else if(ayBazindaLineChart.getAybazindaTip()==2)
             {
                if(ayBazindaLineChart.getYil().equals("2018"))
                {
                    //2018 teklif miktari
                    chartSeries3.setLabel("Siparişe Dönüşmüş");                      
                    chartSeries3.set("Ocak", ayBazindaLineChart.getOcak());
                    chartSeries3.set("Şubat", ayBazindaLineChart.getSubat());
                    chartSeries3.set("Mart", ayBazindaLineChart.getMart());
                    chartSeries3.set("Nisan", ayBazindaLineChart.getNisan());
                    chartSeries3.set("Mayis", ayBazindaLineChart.getMayis());
                    chartSeries3.set("Haziran", ayBazindaLineChart.getHaziran());
                    chartSeries3.set("Temmuz", ayBazindaLineChart.getTemmuz());
                    chartSeries3.set("Ağustos", ayBazindaLineChart.getAgustos());
                    chartSeries3.set("Eylül", ayBazindaLineChart.getEylul());
                    chartSeries3.set("Ekim", ayBazindaLineChart.getEkim());
                    chartSeries3.set("Kasım", ayBazindaLineChart.getKasim());
                    chartSeries3.set("Aralık", ayBazindaLineChart.getAralik());
                }
                else if(ayBazindaLineChart.getYil().equals("2019"))
                {
                    //2019 teklif miktari
                    chartSeries7.setLabel("Siparişe Dönüşmüş");                      
                    chartSeries7.set("Ocak", ayBazindaLineChart.getOcak());
                    chartSeries7.set("Şubat", ayBazindaLineChart.getSubat());
                    chartSeries7.set("Mart", ayBazindaLineChart.getMart());
                    chartSeries7.set("Nisan", ayBazindaLineChart.getNisan());
                    chartSeries7.set("Mayis", ayBazindaLineChart.getMayis());
                    chartSeries7.set("Haziran", ayBazindaLineChart.getHaziran());
                    chartSeries7.set("Temmuz", ayBazindaLineChart.getTemmuz());
                    chartSeries7.set("Ağustos", ayBazindaLineChart.getAgustos());
                    chartSeries7.set("Eylül", ayBazindaLineChart.getEylul());
                    chartSeries7.set("Ekim", ayBazindaLineChart.getEkim());
                    chartSeries7.set("Kasım", ayBazindaLineChart.getKasim());
                    chartSeries7.set("Aralık", ayBazindaLineChart.getAralik());
                }
             }
             
             else if(ayBazindaLineChart.getAybazindaTip()==3)
             {
                if(ayBazindaLineChart.getYil().equals("2018"))
                {
                    //2018 teklif miktari
                    chartSeries4.setLabel("Siparişe Dönüşmemiş");                      
                    chartSeries4.set("Ocak", ayBazindaLineChart.getOcak());
                    chartSeries4.set("Şubat", ayBazindaLineChart.getSubat());
                    chartSeries4.set("Mart", ayBazindaLineChart.getMart());
                    chartSeries4.set("Nisan", ayBazindaLineChart.getNisan());
                    chartSeries4.set("Mayis", ayBazindaLineChart.getMayis());
                    chartSeries4.set("Haziran", ayBazindaLineChart.getHaziran());
                    chartSeries4.set("Temmuz", ayBazindaLineChart.getTemmuz());
                    chartSeries4.set("Ağustos", ayBazindaLineChart.getAgustos());
                    chartSeries4.set("Eylül", ayBazindaLineChart.getEylul());
                    chartSeries4.set("Ekim", ayBazindaLineChart.getEkim());
                    chartSeries4.set("Kasım", ayBazindaLineChart.getKasim());
                    chartSeries4.set("Aralık", ayBazindaLineChart.getAralik());
                }
                else if(ayBazindaLineChart.getYil().equals("2019"))
                {
                    //2019 teklif miktari
                    chartSeries8.setLabel("Siparişe Dönüşmemiş");                      
                    chartSeries8.set("Ocak", ayBazindaLineChart.getOcak());
                    chartSeries8.set("Şubat", ayBazindaLineChart.getSubat());
                    chartSeries8.set("Mart", ayBazindaLineChart.getMart());
                    chartSeries8.set("Nisan", ayBazindaLineChart.getNisan());
                    chartSeries8.set("Mayis", ayBazindaLineChart.getMayis());
                    chartSeries8.set("Haziran", ayBazindaLineChart.getHaziran());
                    chartSeries8.set("Temmuz", ayBazindaLineChart.getTemmuz());
                    chartSeries8.set("Ağustos", ayBazindaLineChart.getAgustos());
                    chartSeries8.set("Eylül", ayBazindaLineChart.getEylul());
                    chartSeries8.set("Ekim", ayBazindaLineChart.getEkim());
                    chartSeries8.set("Kasım", ayBazindaLineChart.getKasim());
                    chartSeries8.set("Aralık", ayBazindaLineChart.getAralik());
                }
             }
        }               
            model1.addSeries(chartSeries1); 
            model2.addSeries(chartSeries2); 
            model2.addSeries(chartSeries3);                 
            model2.addSeries(chartSeries4);
            model3.addSeries(chartSeries5); 
            model4.addSeries(chartSeries6); 
            model4.addSeries(chartSeries7);                 
            model4.addSeries(chartSeries8);
               
            lineChartModelList.add(model1); //2018 teklif miktarı
            lineChartModelList.add(model2); //2018 diğerleri
            lineChartModelList.add(model3); //2019 teklif miktarı
            lineChartModelList.add(model4); //2019 diğerleri
                
        return lineChartModelList;
    }
    
    public List<BarChartModel> initBarModel(List<YilBazindaBarChart> yilBazindaBarChartList) 
    {
        List<BarChartModel> barChartModelList = new ArrayList<>();
        BarChartModel model1 = new BarChartModel();
        BarChartModel model2 = new BarChartModel();
        
        model1.setAnimate(true); 
        model2.setAnimate(true);
        
        for(YilBazindaBarChart yilBazindaBarChart : yilBazindaBarChartList)
        {
            ChartSeries chartSeries1 = new ChartSeries();
            ChartSeries chartSeries2 = new ChartSeries();
            
            chartSeries1.setLabel(yilBazindaBarChart.getYil());
            chartSeries1.set("Teklif Miktarı", yilBazindaBarChart.getToplamTeklifMiktari());
            chartSeries1.set("Siparişe Dönüşmüş", yilBazindaBarChart.getSipariseDonusmus());
            chartSeries1.set("Siparişe Dönüşmemiş", yilBazindaBarChart.getSipariseDonusmemis());
            
            chartSeries2.setLabel(yilBazindaBarChart.getYil());
            chartSeries2.set("Teklif Adeti", yilBazindaBarChart.getTeklifAdeti());
            
            model1.addSeries(chartSeries1); 
            model2.addSeries(chartSeries2);   
        }
        
        barChartModelList.add(model1);
        barChartModelList.add(model2);
 
        return barChartModelList;
    }

    public BarChartModel getBarModel1() {
        return barModel1;
    }

    public void setBarModel1(BarChartModel barModel1) {
        this.barModel1 = barModel1;
    }

    public BarChartModel getBarModel2() {
        return barModel2;
    }

    public void setBarModel2(BarChartModel barModel2) {
        this.barModel2 = barModel2;
    }
    
    public WelcomeAdminDao getWelcomeAdminDao() {
        return welcomeAdminDao;
    }

    public void setWelcomeAdminDao(WelcomeAdminDao welcomeAdminDao) {
        this.welcomeAdminDao = welcomeAdminDao;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public List<YilBazindaBarChart> getYilBazindaBarChartList() {
        return yilBazindaBarChartList;
    }

    public void setYilBazindaBarChartList(List<YilBazindaBarChart> yilBazindaBarChartList) {
        this.yilBazindaBarChartList = yilBazindaBarChartList;
    }

    public List<AyBazindaLineChartRawData> getAyBazindaLineChartRawDataList() {
        return ayBazindaLineChartRawDataList;
    }

    public void setAyBazindaLineChartRawDataList(List<AyBazindaLineChartRawData> ayBazindaLineChartRawDataList) {
        this.ayBazindaLineChartRawDataList = ayBazindaLineChartRawDataList;
    }    

    public LineChartModel getLineModel1() {
        return lineModel1;
    }

    public void setLineModel1(LineChartModel lineModel1) {
        this.lineModel1 = lineModel1;
    }

    public LineChartModel getLineModel2() {
        return lineModel2;
    }

    public void setLineModel2(LineChartModel lineModel2) {
        this.lineModel2 = lineModel2;
    }

    public LineChartModel getLineModel3() {
        return lineModel3;
    }

    public void setLineModel3(LineChartModel lineModel3) {
        this.lineModel3 = lineModel3;
    }

    public LineChartModel getLineModel4() {
        return lineModel4;
    }

    public void setLineModel4(LineChartModel lineModel4) {
        this.lineModel4 = lineModel4;
    }

    public List<AyBazindaLineChart> getAyBazindaLineChart2018List() {
        return ayBazindaLineChart2018List;
    }

    public void setAyBazindaLineChart2018List(List<AyBazindaLineChart> ayBazindaLineChart2018List) {
        this.ayBazindaLineChart2018List = ayBazindaLineChart2018List;
    }

    public List<AyBazindaLineChart> getAyBazindaLineChart2019List() {
        return ayBazindaLineChart2019List;
    }

    public void setAyBazindaLineChart2019List(List<AyBazindaLineChart> ayBazindaLineChart2019List) {
        this.ayBazindaLineChart2019List = ayBazindaLineChart2019List;
    }

    public List<GunBazindaPieChartRawData> getGunBazindaPieChartRawDataList() {
        return gunBazindaPieChartRawDataList;
    }

    public void setGunBazindaPieChartRawDataList(List<GunBazindaPieChartRawData> gunBazindaPieChartRawDataList) {
        this.gunBazindaPieChartRawDataList = gunBazindaPieChartRawDataList;
    }

    public List<GunBazindaPieChart> getGunBazindaPieChart2018List() {
        return gunBazindaPieChart2018List;
    }

    public void setGunBazindaPieChart2018List(List<GunBazindaPieChart> gunBazindaPieChart2018List) {
        this.gunBazindaPieChart2018List = gunBazindaPieChart2018List;
    }

    public List<GunBazindaPieChart> getGunBazindaPieChart2019List() {
        return gunBazindaPieChart2019List;
    }

    public void setGunBazindaPieChart2019List(List<GunBazindaPieChart> gunBazindaPieChart2019List) {
        this.gunBazindaPieChart2019List = gunBazindaPieChart2019List;
    }

    public PieChartModel getPieModel1() {
        return pieModel1;
    }

    public void setPieModel1(PieChartModel pieModel1) {
        this.pieModel1 = pieModel1;
    }

    public PieChartModel getPieModel3() {
        return pieModel3;
    }

    public void setPieModel3(PieChartModel pieModel3) {
        this.pieModel3 = pieModel3;
    }

    public BarChartModel getPieModelToBarModel2() {
        return pieModelToBarModel2;
    }

    public void setPieModelToBarModel2(BarChartModel pieModelToBarModel2) {
        this.pieModelToBarModel2 = pieModelToBarModel2;
    }

    public BarChartModel getPieModelToBarModel4() {
        return pieModelToBarModel4;
    }

    public void setPieModelToBarModel4(BarChartModel pieModelToBarModel4) {
        this.pieModelToBarModel4 = pieModelToBarModel4;
    }

    public List<SonHareketler> getSonHareketlerList() {
        return sonHareketlerList;
    }

    public void setSonHareketlerList(List<SonHareketler> sonHareketlerList) {
        this.sonHareketlerList = sonHareketlerList;
    }

    public List<SonDetayliHareketler> getSonDetayliHareketlerList() {
        return sonDetayliHareketlerList;
    }

    public void setSonDetayliHareketlerList(List<SonDetayliHareketler> sonDetayliHareketlerList) {
        this.sonDetayliHareketlerList = sonDetayliHareketlerList;
    }
    
    
    
}
