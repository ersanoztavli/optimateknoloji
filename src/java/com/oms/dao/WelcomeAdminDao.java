/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.dao;

import com.oms.models.AyBazindaLineChartRawData;
import com.oms.models.GunBazindaPieChartRawData;
import com.oms.models.SonDetayliHareketler;
import com.oms.models.SonHareketler;
import com.oms.models.YilBazindaBarChart;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ersan
 */
public class WelcomeAdminDao {
    
    public List<YilBazindaBarChart> yilBazinda(String ERPFirmaNo) throws Exception
    {
        List<YilBazindaBarChart> yilBazindaBarChartList = new ArrayList<>();
        
        DbConnection dbConnection = new DbConnection();
        Connection connectionERP = dbConnection.baglantiAcERP();
        PreparedStatement preparedStatementERP = null;
        
        String ERPFirmaNoTemp = "";
        
        if(ERPFirmaNo.length() == 1)
                ERPFirmaNoTemp = "00" + ERPFirmaNo;
            else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        String queryERP =    " SELECT TEKLIF_YIL, " +
                                    " SUM(TEKLIF_ADETI) AS TEKLIF_ADETI, " +
                                    " SUM(TOPLAM_TEKLIF_MIKTARI) AS TOPLAM_TEKLIF_MIKTARI,  " +
                                    " SUM(SIPARISE_DONUSMEMIS) AS SIPARISE_DONUSMEMIS,  " +
                                    " SUM(SIPARISE_DONUSMUS) AS SIPARISE_DONUSMUS " + 
                             " FROM ( " +
                                             " SELECT TEKLIF_YIL, " +
                                                    " SUM(TEKLIF_ADET) AS TEKLIF_ADETI, " +
                                                        " SUM(TEKLIF_MIKTARI) AS TOPLAM_TEKLIF_MIKTARI, " +
                                                                " CASE " + 
                                                                " WHEN SIPARIS_DURUMU = 1 THEN SUM(TEKLIF_MIKTARI) " + 
                                                                " ELSE 0 " + 
                                                                " END AS SIPARISE_DONUSMEMIS, " +
                                                                     " CASE " + 
                                                                     " WHEN SIPARIS_DURUMU = 2 THEN SUM(TEKLIF_MIKTARI) " + 
                                                                     " ELSE 0  " +
                                                                     " END  AS SIPARISE_DONUSMUS, " +
                                                        " SUM(SIPARIS_MIKTARI) AS SIPARIS_MIKTARI " +
                                             " FROM ( " +
                                                             " SELECT  COUNT(PRFL.LOGICALREF) AS TEKLIF_ADET, " +
                                                                             " SUM(PRFL.AMOUNT) AS TEKLIF_MIKTARI, " + 
                                                                             " SUM(ISNULL(ORFL.AMOUNT,0)) AS SIPARIS_MIKTARI, " +
                                                                                     " CASE " + 
                                                                                     " WHEN ISNULL(ORF.FICHENO,'')='' THEN 1  " +
                                                                                     " ELSE 2  " +
                                                                                     " END AS SIPARIS_DURUMU, " +
                                                                             " MONTH(PRF.DATE_) AS TEKLIF_AY, " +
                                                                             " YEAR(PRF.DATE_) AS TEKLIF_YIL " +
                                                                             " FROM  LG_" + ERPFirmaNoTemp + "_PURCHOFFER PRF " +
                                                                             " LEFT JOIN LG_" + ERPFirmaNoTemp + "_PURCHOFFERLN PRFL ON PRFL.ORDFICHEREF = PRF.LOGICALREF " +
                                                                             " LEFT JOIN LG_" + ERPFirmaNoTemp + "_CLCARD CR ON PRF.CLIENTREF = CR.LOGICALREF " +
                                                                             " LEFT JOIN LG_" + ERPFirmaNoTemp + "_ITEMS I ON PRFL.STOCKREF = I.LOGICALREF " +
                                                                             " LEFT JOIN LG_" + ERPFirmaNoTemp + "_01_ORFLINE ORFL ON PRFL.LOGICALREF = ORFL.OFFTRANSREF " +
                                                                             " LEFT JOIN LG_" + ERPFirmaNoTemp + "_01_ORFICHE ORF ON ORFL.ORDFICHEREF = ORF.LOGICALREF " +
                                                                             " LEFT JOIN LG_" + ERPFirmaNoTemp + "_PROJECT PRO ON PRO.LOGICALREF = PRF.PROJECTREF " +
                                                                             " LEFT JOIN LG_" + ERPFirmaNoTemp + "_CLCARD CLT ON CLT.LOGICALREF = ORF.CLIENTREF " +
                                                                             " LEFT JOIN LG_" + ERPFirmaNoTemp + "_CLCARD CLGRP ON CLGRP.LOGICALREF = CLT.PARENTCLREF " +
                                                                             " LEFT JOIN LG_CVARPASG CVAR ON CVAR.ARPREF = PRF.CLIENTREF  " +
                                                                                   " AND CVAR.FIRMNO = '" + ERPFirmaNoTemp + "' " +
                                                                             " LEFT JOIN LG_CSTVND CS ON CS.LOGICALREF = CVAR.CSTVNDREF " +
                                                                             " WHERE YEAR(PRF.DATE_) > '2017' " +
                                                                             " GROUP BY ORF.FICHENO, PRF.DATE_, ORF.DATE_, PRFL.AMOUNT, ORFL.AMOUNT, PRFL.LOGICALREF " +
                                                      " ) TMP " +
                             " GROUP BY TEKLIF_YIL, SIPARIS_DURUMU " +
                             " ) TMP2  " +
                             " GROUP BY TEKLIF_YIL ";
               
        try 
        {
            preparedStatementERP = connectionERP.prepareStatement(queryERP);
            ResultSet resultSetERP = preparedStatementERP.executeQuery();
            
            while(resultSetERP.next())
            {
                YilBazindaBarChart yilBazindaBarChart = new YilBazindaBarChart();
                yilBazindaBarChart.setYil(resultSetERP.getString("TEKLIF_YIL"));
                yilBazindaBarChart.setTeklifAdeti(resultSetERP.getInt("TEKLIF_ADETI"));
                yilBazindaBarChart.setToplamTeklifMiktari(resultSetERP.getDouble("TOPLAM_TEKLIF_MIKTARI"));
                yilBazindaBarChart.setSipariseDonusmemis(resultSetERP.getDouble("SIPARISE_DONUSMEMIS"));
                yilBazindaBarChart.setSipariseDonusmus(resultSetERP.getDouble("SIPARISE_DONUSMUS"));
                
                yilBazindaBarChartList.add(yilBazindaBarChart);
            }            
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapatERP();
            if(preparedStatementERP!=null)
            { 
              preparedStatementERP.close();
            }            
            return yilBazindaBarChartList;
        }
    }
    
    public List<AyBazindaLineChartRawData> ayBazinda(String ERPFirmaNo) throws Exception
    {
        List<AyBazindaLineChartRawData> ayBazindaLineChartRawDataList = new ArrayList<>();
        
        DbConnection dbConnection = new DbConnection();
        Connection connectionERP = dbConnection.baglantiAcERP();
        PreparedStatement preparedStatementERP = null;
        
        String ERPFirmaNoTemp = "";
        
        if(ERPFirmaNo.length() == 1)
                ERPFirmaNoTemp = "00" + ERPFirmaNo;
            else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        String queryERP =   " SELECT  TEKLIF_YIL, " +
                                    " TEKLIF_AY, " +
                                    " SUM(TEKLIF_ADET) AS TEKLIF_ADET, " +
                                    " SUM(TEKLIF_MIKTARI) AS TEKLIF_MIKTARI, " +
                                    " SUM(SIPARIS_MIKTARI) AS SIPARISE_DONUSMUS, " +
                                    " SUM(TEKLIF_MIKTARI) - SUM(SIPARIS_MIKTARI) AS SIPARISE_DONUSMEMIS " +
                            " FROM ( SELECT COUNT(PRFL.LOGICALREF) AS TEKLIF_ADET, " +
                                                  " SUM(PRFL.AMOUNT) AS TEKLIF_MIKTARI, " + 
                                                  " SUM(ISNULL(ORFL.AMOUNT,0)) AS SIPARIS_MIKTARI, " +
                                                  " MONTH(PRF.DATE_) AS TEKLIF_AY, " +
                                                  " YEAR(PRF.DATE_) AS TEKLIF_YIL " +
                                    " FROM  LG_022_PURCHOFFER PRF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_PURCHOFFERLN PRFL ON PRFL.ORDFICHEREF = PRF.LOGICALREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_CLCARD CR ON PRF.CLIENTREF = CR.LOGICALREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_ITEMS I ON PRFL.STOCKREF = I.LOGICALREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_01_ORFLINE ORFL ON PRFL.LOGICALREF = ORFL.OFFTRANSREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_01_ORFICHE ORF ON ORFL.ORDFICHEREF = ORF.LOGICALREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_PROJECT PRO ON PRO.LOGICALREF = PRF.PROJECTREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_CLCARD CLT ON CLT.LOGICALREF = ORF.CLIENTREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_CLCARD CLGRP ON CLGRP.LOGICALREF = CLT.PARENTCLREF " +
                                    " LEFT JOIN LG_CVARPASG CVAR ON CVAR.ARPREF = PRF.CLIENTREF AND CVAR.FIRMNO = '" + ERPFirmaNoTemp + "' " +
                                    " LEFT JOIN LG_CSTVND CS ON CS.LOGICALREF = CVAR.CSTVNDREF " +
                                        " WHERE YEAR(PRF.DATE_) > '2017' " +
                                        " GROUP BY ORF.FICHENO, PRF.DATE_, ORF.DATE_, PRFL.AMOUNT, ORFL.AMOUNT, PRFL.LOGICALREF " +
                                   " ) TMP " +
                            " GROUP BY TEKLIF_YIL, TEKLIF_AY " +
                            " ORDER BY TEKLIF_AY, TEKLIF_YIL ASC " ;
               
        try 
        {
            preparedStatementERP = connectionERP.prepareStatement(queryERP);
            ResultSet resultSetERP = preparedStatementERP.executeQuery();
            
            while(resultSetERP.next())
            {
                AyBazindaLineChartRawData ayBazindaLineChartRawData = new AyBazindaLineChartRawData();
                ayBazindaLineChartRawData.setYil(resultSetERP.getString("TEKLIF_YIL"));
                ayBazindaLineChartRawData.setAy(resultSetERP.getString("TEKLIF_AY"));
                ayBazindaLineChartRawData.setTeklifAdeti(resultSetERP.getInt("TEKLIF_ADET"));
                ayBazindaLineChartRawData.setToplamTeklifMiktari(resultSetERP.getDouble("TEKLIF_MIKTARI"));
                ayBazindaLineChartRawData.setSipariseDonusmemis(resultSetERP.getDouble("SIPARISE_DONUSMEMIS"));
                ayBazindaLineChartRawData.setSipariseDonusmus(resultSetERP.getDouble("SIPARISE_DONUSMUS"));
                
                ayBazindaLineChartRawDataList.add(ayBazindaLineChartRawData);
            }            
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapatERP();
            if(preparedStatementERP!=null)
            { 
              preparedStatementERP.close();
            }            
            return ayBazindaLineChartRawDataList;
        }
    }
    
    public List<GunBazindaPieChartRawData> gunBazinda(String ERPFirmaNo) throws Exception
    {
        List<GunBazindaPieChartRawData> gunBazindaPieChartRawDataList = new ArrayList<>();
        
        DbConnection dbConnection = new DbConnection();
        Connection connectionERP = dbConnection.baglantiAcERP();
        PreparedStatement preparedStatementERP = null;
        
        String ERPFirmaNoTemp = "";
        
        if(ERPFirmaNo.length() == 1)
                ERPFirmaNoTemp = "00" + ERPFirmaNo;
            else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        String queryERP =   " SELECT  TEKLIF_YIL, " +
                                    " TEKLIF_GUN, " +
                                    " SUM(TEKLIF_ADET) AS TEKLIF_ADET, " +
                                    " SUM(TEKLIF_MIKTARI) AS TEKLIF_MIKTARI, " +
                                    " SUM(SIPARIS_MIKTARI) AS SIPARISE_DONUSMUS, " +
                                    " SUM(TEKLIF_MIKTARI) - SUM(SIPARIS_MIKTARI) AS SIPARISE_DONUSMEMIS " +
                            " FROM (  SELECT  COUNT(PRFL.LOGICALREF) AS TEKLIF_ADET, " +
                                            " SUM(PRFL.AMOUNT) AS TEKLIF_MIKTARI,  " +
                                            " SUM(ISNULL(ORFL.AMOUNT,0)) AS SIPARIS_MIKTARI, " +
                                            " DATEPART(WEEKDAY,PRF.DATE_) AS TEKLIF_GUN, " +
                                            " YEAR(PRF.DATE_) AS TEKLIF_YIL " +
                                    " FROM LG_" + ERPFirmaNoTemp + "_PURCHOFFER PRF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_PURCHOFFERLN PRFL ON PRFL.ORDFICHEREF = PRF.LOGICALREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_CLCARD CR ON PRF.CLIENTREF = CR.LOGICALREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_ITEMS I ON PRFL.STOCKREF = I.LOGICALREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_01_ORFLINE ORFL ON PRFL.LOGICALREF = ORFL.OFFTRANSREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_01_ORFICHE ORF ON ORFL.ORDFICHEREF = ORF.LOGICALREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_PROJECT PRO ON PRO.LOGICALREF = PRF.PROJECTREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_CLCARD CLT ON CLT.LOGICALREF = ORF.CLIENTREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_CLCARD CLGRP ON CLGRP.LOGICALREF = CLT.PARENTCLREF " +
                                    " LEFT JOIN LG_CVARPASG CVAR ON CVAR.ARPREF = PRF.CLIENTREF AND CVAR.FIRMNO = '" + ERPFirmaNoTemp + "' " +
                                    " LEFT JOIN LG_CSTVND CS ON CS.LOGICALREF = CVAR.CSTVNDREF " +
                                    " WHERE YEAR(PRF.DATE_) > '2017' " +
                                    " GROUP BY ORF.FICHENO, PRF.DATE_, ORF.DATE_, PRFL.AMOUNT, ORFL.AMOUNT, PRFL.LOGICALREF " +
                              " ) TMP " +
                            " GROUP BY TEKLIF_YIL, TEKLIF_GUN " +
                            " ORDER BY TEKLIF_GUN, TEKLIF_YIL ASC ";
               
        try 
        {
            preparedStatementERP = connectionERP.prepareStatement(queryERP);
            ResultSet resultSetERP = preparedStatementERP.executeQuery();
            
            while(resultSetERP.next())
            {
                GunBazindaPieChartRawData gunBazindaPieChartRawData = new GunBazindaPieChartRawData();
                gunBazindaPieChartRawData.setYil(resultSetERP.getString("TEKLIF_YIL"));
                gunBazindaPieChartRawData.setGun(resultSetERP.getString("TEKLIF_GUN"));
                gunBazindaPieChartRawData.setTeklifAdeti(resultSetERP.getInt("TEKLIF_ADET"));
                gunBazindaPieChartRawData.setToplamTeklifMiktari(resultSetERP.getDouble("TEKLIF_MIKTARI"));
                gunBazindaPieChartRawData.setSipariseDonusmemis(resultSetERP.getDouble("SIPARISE_DONUSMEMIS"));
                gunBazindaPieChartRawData.setSipariseDonusmus(resultSetERP.getDouble("SIPARISE_DONUSMUS"));
                
                gunBazindaPieChartRawDataList.add(gunBazindaPieChartRawData);
            }            
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapatERP();
            if(preparedStatementERP!=null)
            { 
              preparedStatementERP.close();
            }            
            return gunBazindaPieChartRawDataList;
        }
    }
    
    public List<SonHareketler> sonHareketler(String ERPFirmaNo) throws Exception
    {
        List<SonHareketler> sonHareketlerList = new ArrayList<>();
        
        DbConnection dbConnection = new DbConnection();
        Connection connectionERP = dbConnection.baglantiAcERP();
        PreparedStatement preparedStatementERP = null;
        
        String ERPFirmaNoTemp = "";
        
        if(ERPFirmaNo.length() == 1)
                ERPFirmaNoTemp = "00" + ERPFirmaNo;
            else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        String queryERP =   " SELECT  TEKLIF_GUN, " +
                                    " SUM(TEKLIF_ADET) AS TEKLIF_ADET, " +
                                    " SUM(TEKLIF_MIKTARI) AS TEKLIF_MIKTARI, " +
                                    " SUM(SIPARIS_MIKTARI) AS SIPARISE_DONUSMUS, " +
                                    " SUM(TEKLIF_MIKTARI) - SUM(SIPARIS_MIKTARI) AS SIPARISE_DONUSMEMIS " +
                            " FROM (  SELECT  COUNT(PRFL.LOGICALREF) AS TEKLIF_ADET, " +
                                            " SUM(PRFL.AMOUNT) AS TEKLIF_MIKTARI,  " +
                                            " SUM(ISNULL(ORFL.AMOUNT,0)) AS SIPARIS_MIKTARI, " +
                                            " PRF.DATE_ AS TEKLIF_GUN, " +
                                            " YEAR(PRF.DATE_) AS TEKLIF_YIL " +
                                    " FROM LG_022_PURCHOFFER PRF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_PURCHOFFERLN PRFL ON PRFL.ORDFICHEREF = PRF.LOGICALREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_CLCARD CR ON PRF.CLIENTREF = CR.LOGICALREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_ITEMS I ON PRFL.STOCKREF = I.LOGICALREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_01_ORFLINE ORFL ON PRFL.LOGICALREF = ORFL.OFFTRANSREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_01_ORFICHE ORF ON ORFL.ORDFICHEREF = ORF.LOGICALREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_PROJECT PRO ON PRO.LOGICALREF = PRF.PROJECTREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_CLCARD CLT ON CLT.LOGICALREF = ORF.CLIENTREF " +
                                    " LEFT JOIN LG_" + ERPFirmaNoTemp + "_CLCARD CLGRP ON CLGRP.LOGICALREF = CLT.PARENTCLREF " +
                                    " LEFT JOIN LG_CVARPASG CVAR ON CVAR.ARPREF = PRF.CLIENTREF  " +
                                                  " AND CVAR.FIRMNO = '" + ERPFirmaNoTemp + "' " +
                                    " LEFT JOIN LG_CSTVND CS ON CS.LOGICALREF = CVAR.CSTVNDREF " +
                                    " WHERE PRF.DATE_ > GETDATE() - 7 " +
                                    " GROUP BY ORF.FICHENO,PRF.DATE_,ORF.DATE_,PRFL.AMOUNT,ORFL.AMOUNT,PRFL.LOGICALREF " +
                                 " ) TMP " +
                            " GROUP BY TEKLIF_YIL, TEKLIF_GUN " +
                            " ORDER BY TEKLIF_GUN, TEKLIF_YIL ASC ";
               
        try 
        {
            preparedStatementERP = connectionERP.prepareStatement(queryERP);
            ResultSet resultSetERP = preparedStatementERP.executeQuery();
            
            while(resultSetERP.next())
            {
                SonHareketler sonHareketler = new SonHareketler();
                sonHareketler.setTeklifGun(resultSetERP.getDate("TEKLIF_GUN"));
                sonHareketler.setTeklifAdeti(resultSetERP.getInt("TEKLIF_ADET"));
                sonHareketler.setToplamTeklifMiktari(resultSetERP.getDouble("TEKLIF_MIKTARI"));
                sonHareketler.setSipariseDonusmemis(resultSetERP.getDouble("SIPARISE_DONUSMEMIS"));
                sonHareketler.setSipariseDonusmus(resultSetERP.getDouble("SIPARISE_DONUSMUS"));
                
                sonHareketlerList.add(sonHareketler);
            }            
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapatERP();
            if(preparedStatementERP!=null)
            { 
              preparedStatementERP.close();
            }            
            return sonHareketlerList;
        }
    }
    
    public List<SonDetayliHareketler> sonDetayliHareketler(String ERPFirmaNo) throws Exception
    {
        List<SonDetayliHareketler> sonDetayliHareketlerList = new ArrayList<>();
        
        DbConnection dbConnection = new DbConnection();
        Connection connectionERP = dbConnection.baglantiAcERP();
        PreparedStatement preparedStatementERP = null;
        
        String ERPFirmaNoTemp = "";
        
        if(ERPFirmaNo.length() == 1)
                ERPFirmaNoTemp = "00" + ERPFirmaNo;
            else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        String queryERP =    " SELECT TEKLIF_NUMARASI, " +
                                    " TEKLIF_TARIHI, " +
                                    " TEKLIF_CARI_KODU, " +
                                    " TEKLIF_CARI_UNVAN, " +
                                    " SUM(TEKLIF_MIKTARI) AS TEKLIF_MIKTARI, " + 
                                    " SUM(SIPARIS_MIKTARI) AS SIPARIS_MIKTARI, " +
                                    " SUM(TEKLIF_TUTARI) AS TEKLIF_TUTARI, " +
                                    " DOVIZ, " +
                                    " SIPARIS_DURUMU " +
                             " FROM ( SELECT  PRF.FICHENO AS TEKLIF_NUMARASI, " +
                                            " PRF.DATE_ AS TEKLIF_TARIHI, " +
                                            " CLT.CODE AS SIPARIS_CARI_KODU, " +
                                            " CLT.DEFINITION_ AS SIPARIS_CARI_UNVANI, " +
                                            " CR.CODE AS TEKLIF_CARI_KODU, " +
                                            " CR.DEFINITION_ AS TEKLIF_CARI_UNVAN, " +
                                            " PRFL.AMOUNT AS TEKLIF_MIKTARI, " + 
                                            " ISNULL(ORFL.AMOUNT,0) AS SIPARIS_MIKTARI, " +
                                                   " CASE " + 
                                                   " WHEN PRFL.TRCURR IN (0,160) THEN 'TL' " +  
                                                   " WHEN PRFL.TRCURR IN (1) THEN 'USD' " + 
                                                   " WHEN PRFL.TRCURR IN (20) THEN 'EUR' " +  
                                                   " ELSE '' " + 
                                                   " END AS DOVIZ, " +
                                                          " CASE " + 
                                                          " WHEN  ISNULL(ORF.FICHENO,'') = '' THEN 'SİPARİŞ OLMADI' " + 
                                                          " ELSE 'SİPARİŞ' " + 
                                                          " END AS SIPARIS_DURUMU, " +
                                            " PRFL.VATMATRAH AS TEKLIF_TUTARI, " +
                                            " ORFL.VATMATRAH AS SIPARIS_TUTARI, " +
                                            " MONTH(PRF.DATE_) AS TEKLIF_AY, " +
                                            " CASE " + 
                                            " WHEN ISNULL(PRFL.AMOUNT,0) - ISNULL(ORFL.AMOUNT,0) <> 0 THEN 'TESLİM EDİLMEDİ' " + 
                                            " ELSE 'TESLİM' " + 
                                            " END AS SEVK_DURUMU " +
                                    " FROM LG_022_PURCHOFFER PRF " +
                                    " LEFT JOIN LG_022_PURCHOFFERLN PRFL ON PRFL.ORDFICHEREF = PRF.LOGICALREF " +
                                    " LEFT JOIN LG_022_CLCARD CR ON PRF.CLIENTREF = CR.LOGICALREF " +
                                    " LEFT JOIN LG_022_ITEMS I ON PRFL.STOCKREF = I.LOGICALREF " +
                                    " LEFT JOIN LG_022_01_ORFLINE ORFL ON PRFL.LOGICALREF = ORFL.OFFTRANSREF " +
                                    " LEFT JOIN LG_022_01_ORFICHE ORF ON ORFL.ORDFICHEREF = ORF.LOGICALREF " +
                                    " LEFT JOIN LG_022_PROJECT PRO ON PRO.LOGICALREF = PRF.PROJECTREF " +
                                    " LEFT JOIN LG_022_CLCARD CLT ON CLT.LOGICALREF = ORF.CLIENTREF " +
                                    " LEFT JOIN LG_022_CLCARD CLGRP ON CLGRP.LOGICALREF = CLT.PARENTCLREF " +
                                    " LEFT JOIN LG_CVARPASG CVAR ON CVAR.ARPREF = PRF.CLIENTREF " + 
                                                          " AND CVAR.FIRMNO = '022' " +
                                    " LEFT JOIN LG_CSTVND CS ON CS.LOGICALREF = CVAR.CSTVNDREF " +
                                        " WHERE PRF.DATE_ > GETDATE() - 10 " +
                                " ) TMP " +
                              " GROUP BY TEKLIF_NUMARASI, " + 
                                       " TEKLIF_TARIHI, " + 
                                       " TEKLIF_CARI_KODU, " + 
                                       " TEKLIF_CARI_UNVAN, " + 
                                       " DOVIZ, " + 
                                       " SIPARIS_DURUMU " ;
               
        try 
        {
            preparedStatementERP = connectionERP.prepareStatement(queryERP);
            ResultSet resultSetERP = preparedStatementERP.executeQuery();
            
            while(resultSetERP.next())
            {
                SonDetayliHareketler sonDetayliHareketler = new SonDetayliHareketler();
                sonDetayliHareketler.setTeklifNo(resultSetERP.getString("TEKLIF_NUMARASI"));
                sonDetayliHareketler.setTeklifTarihi(resultSetERP.getDate("TEKLIF_TARIHI"));
                sonDetayliHareketler.setTeklifCariKod(resultSetERP.getString("TEKLIF_CARI_KODU"));
                sonDetayliHareketler.setTeklifCariUnvan(resultSetERP.getString("TEKLIF_CARI_UNVAN"));
                sonDetayliHareketler.setTeklifMiktari(resultSetERP.getDouble("TEKLIF_MIKTARI"));
                sonDetayliHareketler.setSiparisMiktari(resultSetERP.getDouble("SIPARIS_MIKTARI"));
                sonDetayliHareketler.setTeklifTutari(resultSetERP.getDouble("TEKLIF_TUTARI"));
                sonDetayliHareketler.setDoviz(resultSetERP.getString("DOVIZ"));
                sonDetayliHareketler.setSiparisDurumu(resultSetERP.getString("SIPARIS_DURUMU"));
                
                sonDetayliHareketlerList.add(sonDetayliHareketler);
            }            
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapatERP();
            if(preparedStatementERP!=null)
            { 
              preparedStatementERP.close();
            }            
            return sonDetayliHareketlerList;
        }
    }
    
    
}
