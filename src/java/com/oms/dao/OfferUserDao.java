/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.dao;

import com.oms.models.EMAIL_AYAR;
import com.oms.models.FIRMA;
import com.oms.models.MAIL;
import com.oms.models.MAIL_ADRES;
import com.oms.models.MUSTERI;
import com.oms.models.TEKLIF_BASLIK;
import com.oms.models.TEKLIF_SATIR;
import com.oms.models.TEKLIF_SATIR_YANIT;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;


/**
 *
 * @author ersan
 */
public class OfferUserDao {
    
    public TEKLIF_BASLIK getirTeklifBaslik(String guid) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT * "
                     + " FROM TEKLIF_BASLIK "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     + " AND TEKLIF_GUID = ? " ;
        
        preparedStatement = connection.prepareStatement(query);
        TEKLIF_BASLIK teklifBaslik = new TEKLIF_BASLIK();
        
        try 
        {            
            preparedStatement.setString(1, guid);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {                       
                teklifBaslik.setTEKLIF_BASLIK_ID(resultSet.getInt("TEKLIF_BASLIK_ID"));                 
                teklifBaslik.setTEKLIF_NUMARASI(resultSet.getString("TEKLIF_NUMARASI")); 
                teklifBaslik.setTEKLIF_BASLANGIC_TARIHI(resultSet.getTimestamp("TEKLIF_BASLANGIC_TARIHI")); 
                teklifBaslik.setTEKLIF_BITIS_TARIHI(resultSet.getTimestamp("TEKLIF_BITIS_TARIHI")); 
                teklifBaslik.setGECERLI_TEKLIF_REVIZYON_NUMARASI(resultSet.getString("GECERLI_TEKLIF_REVIZYON_NUMARASI")); 
                teklifBaslik.setREVIZYON_NUMARASI(resultSet.getString("REVIZYON_NUMARASI")); 
                teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA(resultSet.getString("TEKLIF_DURUM_KODU_ACIKLAMA"));
                teklifBaslik.setTEKLIF_DURUM_KODU(resultSet.getInt("TEKLIF_DURUM_KODU"));
                teklifBaslik.setTEKLIF_TUTARI(resultSet.getDouble("TEKLIF_TUTARI"));
                teklifBaslik.setTEKLIF_TOPLAM_INDIRIM_TUTARI(resultSet.getDouble("TEKLIF_TOPLAM_INDIRIM_TUTARI"));
                teklifBaslik.setTEKLIF_TOPLAM_TUTARI(resultSet.getDouble("TEKLIF_TOPLAM_TUTARI"));
                teklifBaslik.setTEKLIF_PARA_BIRIMI(resultSet.getInt("TEKLIF_PARA_BIRIMI"));
                teklifBaslik.setTEKLIF_KUR(resultSet.getDouble("TEKLIF_KUR")); 
                teklifBaslik.setMUSTERI_RECORD_ID(resultSet.getString("MUSTERI_RECORD_ID"));
                teklifBaslik.setTEKLIF_GUID(resultSet.getString("TEKLIF_GUID")); 
                teklifBaslik.setRECORD_ID(resultSet.getString("RECORD_ID"));
                teklifBaslik.setERP_FIRMA_NUMBER(resultSet.getString("ERP_FIRMA_NUMBER"));
                teklifBaslik.setFIRMA_ID(resultSet.getInt("FIRMA_ID"));
                
                teklifBaslik.setMUSTERI_YANIT(resultSet.getString("MUSTERI_YANIT"));
                
                MUSTERI musteri = getirMusteriRecorIdIle(teklifBaslik.getMUSTERI_RECORD_ID());
                teklifBaslik.setMusteriUnvani(musteri.getMUSTERI_UNVANI());
                teklifBaslik.setMusteriMail1(musteri.getMUSTERI_MAIL_ADRESI1());
                teklifBaslik.setMusteriMail2(musteri.getMUSTERI_MAIL_ADRESI2());                
                teklifBaslik.setMusteriUlke(musteri.getMUSTERI_ULKE());
                teklifBaslik.setMusteriIl(musteri.getMUSTERI_IL());
                teklifBaslik.setMusteriIlce(musteri.getMUSTERI_ILCE());
                teklifBaslik.setMusteriAdres(musteri.getMUSTERI_ADRESI());
                
                FIRMA firma = getirFirmaIdIle(teklifBaslik.getFIRMA_ID());
                teklifBaslik.setFirmaUnvan(firma.getFIRMA_UNVANI());
            }             
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapat();   
            if(preparedStatement!=null)
            { 
              preparedStatement.close();
            }     
            return teklifBaslik;
        }
    }    
    
    public TEKLIF_BASLIK getirTeklifBaslikSatinAlma(int mailAdresId) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        PreparedStatement preparedStatement = null;
        
        String query2 = " SELECT MAIL_ID, MUSTERI_RECORD_ID "
                      + " FROM MAIL_ADRES "
                      + " WHERE KULLANIM_DURUMU = 1 "
                      + " AND MAIL_ADRES_ID = ? " ;
        
        String query3 = " SELECT TEKLIF_BASLIK_ID "
                      + " FROM MAIL "
                      + " WHERE KULLANIM_DURUMU = 1 "
                      + " AND MAIL_ID = ? " ;
        
        String query = " SELECT * "
                     + " FROM TEKLIF_BASLIK "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     + " AND TEKLIF_BASLIK_ID = ? " ;
        
        preparedStatement2 = connection.prepareStatement(query2);
        preparedStatement3 = connection.prepareStatement(query3);
        preparedStatement = connection.prepareStatement(query);
        
        TEKLIF_BASLIK teklifBaslik = new TEKLIF_BASLIK();
        
        try 
        {            
            preparedStatement2.setInt(1, mailAdresId);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            resultSet2.next();
            
            preparedStatement3.setInt(1, resultSet2.getInt("MAIL_ID"));
            teklifBaslik.setMUSTERI_RECORD_ID(resultSet2.getString("MUSTERI_RECORD_ID"));
            ResultSet resultSet3 = preparedStatement3.executeQuery();
            resultSet3.next();
            
            preparedStatement.setInt(1, resultSet3.getInt("TEKLIF_BASLIK_ID"));
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {                       
                teklifBaslik.setTEKLIF_BASLIK_ID(resultSet.getInt("TEKLIF_BASLIK_ID"));                 
                teklifBaslik.setTEKLIF_NUMARASI(resultSet.getString("TEKLIF_NUMARASI")); 
//                teklifBaslik.setTEKLIF_BASLANGIC_TARIHI(resultSet.getTimestamp("TEKLIF_BASLANGIC_TARIHI")); 
//                teklifBaslik.setTEKLIF_BITIS_TARIHI(resultSet.getTimestamp("TEKLIF_BITIS_TARIHI")); 
                teklifBaslik.setTEKLIF_TARIHI(resultSet.getTimestamp("TEKLIF_TARIHI")); 
                teklifBaslik.setGECERLI_TEKLIF_REVIZYON_NUMARASI(resultSet.getString("GECERLI_TEKLIF_REVIZYON_NUMARASI")); 
                teklifBaslik.setREVIZYON_NUMARASI(resultSet.getString("REVIZYON_NUMARASI")); 
                teklifBaslik.setTEKLIF_DURUM_KODU_ACIKLAMA(resultSet.getString("TEKLIF_DURUM_KODU_ACIKLAMA"));
                teklifBaslik.setTEKLIF_DURUM_KODU(resultSet.getInt("TEKLIF_DURUM_KODU"));
                teklifBaslik.setTEKLIF_TUTARI(resultSet.getDouble("TEKLIF_TUTARI"));
                teklifBaslik.setTEKLIF_TOPLAM_INDIRIM_TUTARI(resultSet.getDouble("TEKLIF_TOPLAM_INDIRIM_TUTARI"));
                teklifBaslik.setTEKLIF_TOPLAM_TUTARI(resultSet.getDouble("TEKLIF_TOPLAM_TUTARI"));
                teklifBaslik.setTEKLIF_PARA_BIRIMI(resultSet.getInt("TEKLIF_PARA_BIRIMI"));
                teklifBaslik.setTEKLIF_KUR(resultSet.getDouble("TEKLIF_KUR")); 
                //teklifBaslik.setMUSTERI_RECORD_ID(resultSet.getString("MUSTERI_RECORD_ID"));
                teklifBaslik.setTEKLIF_GUID(resultSet.getString("TEKLIF_GUID")); 
                teklifBaslik.setRECORD_ID(resultSet.getString("RECORD_ID"));
                teklifBaslik.setERP_FIRMA_NUMBER(resultSet.getString("ERP_FIRMA_NUMBER"));
                teklifBaslik.setFIRMA_ID(resultSet.getInt("FIRMA_ID"));
                
                MUSTERI musteri = getirMusteriRecorIdIle(teklifBaslik.getMUSTERI_RECORD_ID());
                teklifBaslik.setMusteriUnvani(musteri.getMUSTERI_UNVANI());
                teklifBaslik.setMusteriMail1(musteri.getMUSTERI_MAIL_ADRESI1());
                teklifBaslik.setMusteriMail2(musteri.getMUSTERI_MAIL_ADRESI2());                
                teklifBaslik.setMusteriUlke(musteri.getMUSTERI_ULKE());
                teklifBaslik.setMusteriIl(musteri.getMUSTERI_IL());
                teklifBaslik.setMusteriIlce(musteri.getMUSTERI_ILCE());
                teklifBaslik.setMusteriAdres(musteri.getMUSTERI_ADRESI());
                
                FIRMA firma = getirFirmaIdIle(teklifBaslik.getFIRMA_ID());
                teklifBaslik.setFirmaUnvan(firma.getFIRMA_UNVANI());
            }             
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapat();   
            if(preparedStatement!=null)
            { 
              preparedStatement.close();
            }     
            return teklifBaslik;
        }
    }
    
    
    public List<TEKLIF_SATIR> getirTeklifSatirlari(String teklifBaslikRecordId,
                                                   String ERPFirmaNumber) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT * "
                     + " FROM TEKLIF_SATIR "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     + " AND TEKLIF_BASLIK_RECORD_ID = ? " 
                     + " AND ERP_FIRMA_NUMBER = ? ";
        
        preparedStatement = connection.prepareStatement(query);
        List<TEKLIF_SATIR> teklifSatirlari = new ArrayList<>();
        
        try 
        {            
            preparedStatement.setString(1, teklifBaslikRecordId);
            preparedStatement.setString(2, ERPFirmaNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {
                TEKLIF_SATIR teklifSatir = new TEKLIF_SATIR();                          
                teklifSatir.setTEKLIF_SATIR_ID(resultSet.getInt("TEKLIF_SATIR_ID"));                 
//                teklifSatir.setSATIR_TURU(resultSet.getInt("SATIR_TURU")); 
                teklifSatir.setMALZEME_HIZMET_MASRAF_KODU(resultSet.getString("MALZEME_HIZMET_MASRAF_KODU")); 
                teklifSatir.setMALZEME_HIZMET_MASRAF_ADI(resultSet.getString("MALZEME_HIZMET_MASRAF_ADI"));
                teklifSatir.setMALZEME_HIZMET_MASRAF_ADI2(resultSet.getString("MALZEME_HIZMET_MASRAF_ADI2"));
                teklifSatir.setMALZEME_HIZMET_MASRAF_MIKTARI(resultSet.getDouble("MALZEME_HIZMET_MASRAF_MIKTARI"));
                teklifSatir.setMUSTERI_SATIS_MIKTAR_YANIT(resultSet.getDouble("MUSTERI_SATIS_MIKTAR_YANIT"));
//                teklifSatir.setBIRIM_CEVRIM_1(resultSet.getInt("BIRIM_CEVRIM_1")); 
//                teklifSatir.setBIRIM_CEVRIM_2(resultSet.getInt("BIRIM_CEVRIM_2")); 
                teklifSatir.setMALZEME_HIZMET_MASRAF_ACIKLAMASI(resultSet.getString("MALZEME_HIZMET_MASRAF_ACIKLAMASI")); 
                teklifSatir.setMALZEME_HIZMET_MASRAF_BIRIM_KODU(resultSet.getString("MALZEME_HIZMET_MASRAF_BIRIM_KODU"));
                teklifSatir.setMALZEME_HIZMET_MASRAF_BIRIM_FIYATI(resultSet.getDouble("MALZEME_HIZMET_MASRAF_BIRIM_FIYATI"));
//                teklifSatir.setMALZEME_HIZMET_MASRAF_KDV_ORANI(resultSet.getDouble("MALZEME_HIZMET_MASRAF_KDV_ORANI")); 
//                teklifSatir.setMALZEME_HIZMET_MASRAF_KDV_TUTARI(resultSet.getDouble("MALZEME_HIZMET_MASRAF_KDV_TUTARI"));
                teklifSatir.setMALZEME_HIZMET_MASRAF_TUTARI(resultSet.getDouble("MALZEME_HIZMET_MASRAF_TUTARI"));
                teklifSatir.setMalzemeHizmetMasrafTutariCarpiKur(resultSet.getDouble("MALZEME_HIZMET_MASRAF_TUTARI") 
                                                                     * resultSet.getDouble("KUR"));
                teklifSatir.setPARA_BIRIMI(resultSet.getInt("PARA_BIRIMI"));
                teklifSatir.setKUR(resultSet.getDouble("KUR")); 
                teklifSatir.setTESLIM_TARIHI(resultSet.getTimestamp("TESLIM_TARIHI")); 

                teklifSatirlari.add(teklifSatir);
            }             
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapat();   
            if(preparedStatement!=null)
            { 
              preparedStatement.close();
            }     
            return teklifSatirlari;
        }
    }

    public List<TEKLIF_SATIR_YANIT> getirTeklifSatirYanitlari(int emailAdresId) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        
        String query = " SELECT * "
                     + " FROM TEKLIF_SATIR_YANIT "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     + " AND MAIL_ADRES_ID = ? " ;
        
        String query2 = " SELECT * "
                      + " FROM TEKLIF_SATIR "
                      + " WHERE KULLANIM_DURUMU = 1 "
                      + " AND TEKLIF_SATIR_ID = ? " ;
        
        preparedStatement = connection.prepareStatement(query);
        preparedStatement2 = connection.prepareStatement(query2);
        
        List<TEKLIF_SATIR_YANIT> teklifSatirYanitlari = new ArrayList<>();
        
        try 
        {            
            preparedStatement.setInt(1, emailAdresId);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {
                TEKLIF_SATIR_YANIT teklifSatirYanit = new TEKLIF_SATIR_YANIT();                          
                teklifSatirYanit.setTEKLIF_SATIR_YANIT_ID(resultSet.getInt("TEKLIF_SATIR_YANIT_ID"));                 
                teklifSatirYanit.setTEKLIF_SATIR_ID(resultSet.getInt("TEKLIF_SATIR_ID")); 
                teklifSatirYanit.setACIKLAMA(resultSet.getString("ACIKLAMA")); 
                teklifSatirYanit.setBIRIM_FIYATI(resultSet.getDouble("BIRIM_FIYATI"));
                teklifSatirYanit.setMIKTAR(resultSet.getDouble("MIKTAR"));
                teklifSatirYanit.setBIRIM(resultSet.getString("BIRIM"));
                teklifSatirYanit.setSTOKTA_VAR_YOK(resultSet.getInt("STOKTA_VAR_YOK"));
                
                if(teklifSatirYanit.getSTOKTA_VAR_YOK()==0)
                    teklifSatirYanit.setStoktaVarYokBoolean(false);
                else
                    teklifSatirYanit.setStoktaVarYokBoolean(true);
                ////////////////////////////////////////////////////////////////////////////////////
                //Teklif Satırındaki bilgilerin setleme işlemleri yapılmakta...
                preparedStatement2.setInt(1, teklifSatirYanit.getTEKLIF_SATIR_ID());
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                resultSet2.next();
                teklifSatirYanit.setMalzemeHizmetMasrafKodu(resultSet2.getString("MALZEME_HIZMET_MASRAF_KODU"));
                teklifSatirYanit.setMalzemeHizmetMasrafAdi(resultSet2.getString("MALZEME_HIZMET_MASRAF_ADI"));
                teklifSatirYanit.setMalzemeHizmetMasrafAdi2(resultSet2.getString("MALZEME_HIZMET_MASRAF_ADI2"));
                teklifSatirYanit.setMalzemeHizmetMasrafAciklamasi(resultSet2.getString("MALZEME_HIZMET_MASRAF_ACIKLAMASI"));
                teklifSatirYanit.setMalzemeHizmetMasrafBirimKodu(resultSet2.getString("MALZEME_HIZMET_MASRAF_BIRIM_KODU"));
                teklifSatirYanit.setMalzemeHizmetMasrafMiktari(resultSet2.getDouble("MALZEME_HIZMET_MASRAF_MIKTARI"));
                teklifSatirYanit.setMalzemeTeslimTarihi(resultSet2.getTimestamp("TESLIM_TARIHI"));
                ////////////////////////////////////////////////////////////////////////////////////
                
                teklifSatirYanitlari.add(teklifSatirYanit);
            }       
            preparedStatement2.close();
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapat();   
            if(preparedStatement!=null)
            { 
              preparedStatement.close();
            }     
            return teklifSatirYanitlari;
        }
    }
    
    public void yanıtlaTeklif(int teklifDurumKodu, 
                              String teklifDurumKoduAciklama,
                              String teklifGuid) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " UPDATE  "
                     + " TEKLIF_BASLIK "
                     + " SET TEKLIF_DURUM_KODU = ?, "
                     + "     TEKLIF_DURUM_KODU_ACIKLAMA = ? "
                     + " WHERE TEKLIF_GUID = ? " ;
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, teklifDurumKodu);
            preparedStatement.setString(2, teklifDurumKoduAciklama);
            preparedStatement.setString(3, teklifGuid);
            preparedStatement.executeUpdate();    
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapat();   
            if(preparedStatement!=null)
            { 
              preparedStatement.close();
            }     
        }
    }
    
    public void teklifeMusteriAciklamasiGir( String musteriTeklifAciklamasi,
                                             String teklifGuid) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " UPDATE  "
                     + " TEKLIF_BASLIK "
                     + " SET MUSTERI_YANIT = ? "
                     + " WHERE TEKLIF_GUID = ? " ;
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, musteriTeklifAciklamasi);
            preparedStatement.setString(2, teklifGuid);
            preparedStatement.executeUpdate();    
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapat();   
            if(preparedStatement!=null)
            { 
              preparedStatement.close();
            }     
        }
    }
    
    public void guncelleTeklifBaslik(TEKLIF_BASLIK teklifBaslik) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " UPDATE  "
                     + " TEKLIF_BASLIK "
                     + " SET TEKLIF_TUTARI = ? "
                     + " WHERE TEKLIF_GUID = ? " ;
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, teklifBaslik.getTEKLIF_TUTARI());
            preparedStatement.setString(2, teklifBaslik.getTEKLIF_GUID());
            preparedStatement.executeUpdate();    
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapat();   
            if(preparedStatement!=null)
            { 
              preparedStatement.close();
            }     
        }
    }

    public MUSTERI getirMusteriRecorIdIle(String musteriRecordId) throws Exception
    {
        MUSTERI musteri = new MUSTERI();
        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT "
                     + " * "
                     + " FROM MUSTERI "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     + " AND RECORD_ID = ? ";
        
        try 
        {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, musteriRecordId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                musteri.setMUSTERI_UNVANI(resultSet.getString("MUSTERI_UNVANI"));
                musteri.setMUSTERI_ADRESI(resultSet.getString("MUSTERI_ADRESI"));
            }   
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapat();   
            if(preparedStatement!=null)
            { 
              preparedStatement.close();
            }            
            return musteri;
        }
    }
    
    public FIRMA getirFirmaIdIle(int firmaId) throws Exception
    {
        FIRMA firma = new FIRMA();
        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT "
                     + " * "
                     + " FROM FIRMA "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     + " AND FIRMA_ID = ? ";
        
        try 
        {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, firmaId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                firma.setFIRMA_UNVANI(resultSet.getString("FIRMA_UNVANI"));
            }   
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapat();   
            if(preparedStatement!=null)
            { 
              preparedStatement.close();
            }            
            return firma;
        }
    }
    
    public void download(String teklifBaslikRecordId,
                      String teklifNo,
                      String musteriUnvan,
                      String musteriAdres,
                      String musteriIl,
                      String musteriIlce,
                      String musteriUlke,
                      double toplamTutar,
                      double toplamIndirim,
                      double netTutar,
                      int paraBirimi,
                      String pdfOrXlsx) throws Exception
    {
        try 
        {               
            
            if(musteriAdres == null)
            musteriAdres = "";
            if(musteriIl == null)
            musteriIl = "";
            if(musteriIlce == null)
            musteriIlce = "";
            if(musteriUlke == null)
            musteriUlke = "";
            
            JasperReport jasperReport = null;
            JasperPrint jasperPrint = null;
            jasperReport = JasperCompileManager.compileReport(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "resources/reports/Invoice.jrxml");
            
            HashMap parameters = new HashMap();
            parameters.put("teklifBaslikRecordId", teklifBaslikRecordId);
            parameters.put("teklifNo", teklifNo);
            parameters.put("musteriUnvan", musteriUnvan);
            parameters.put("musteriAdres", musteriAdres);
            parameters.put("musteriIl", musteriIl);
            parameters.put("musteriIlce", musteriIlce);
            parameters.put("musteriUlke", musteriUlke);
            parameters.put("toplamTutar", toplamTutar);
            parameters.put("toplamIndirim", toplamIndirim);
            parameters.put("netTutar", netTutar);
            if(paraBirimi == 0)
                parameters.put("paraBirimi", "₺");
            else if(paraBirimi == 1)
                parameters.put("paraBirimi", "$");
            else if(paraBirimi == 20)
                parameters.put("paraBirimi", "€");
            
            parameters.put("image", FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "resources/images/firmaLogo.png");
            
            DbConnection dbConnection = new DbConnection();
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dbConnection.baglantiAc());
            //JasperViewer.viewReport(jasperPrint,false);
            
             HttpServletResponse response = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
            
            if(pdfOrXlsx.equals("pdf"))
            {
                response.setContentType("application/x-download");
                response.addHeader("Content-disposition", "attachment; filename=" + teklifNo + ".pdf");
                OutputStream out = response.getOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint,out);
            }            
            else if(pdfOrXlsx.equals("xlsx"))
            {
                response.addHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.addHeader("Content-disposition", "attachment; filename=" + teklifNo + ".xlsx");
                ServletOutputStream outputStream = response.getOutputStream();
                net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter exporter = new net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,outputStream);
                exporter.exportReport();
            }
            FacesContext.getCurrentInstance().responseComplete();
            
        } 
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
    }
    
    public void downloadEnglish(String teklifBaslikRecordId,
                      String teklifNo,
                      String musteriUnvan,
                      String musteriAdres,
                      String musteriIl,
                      String musteriIlce,
                      String musteriUlke,
                      double toplamTutar,
                      double toplamIndirim,
                      double netTutar,
                      int paraBirimi,
                      String pdfOrXlsx) throws Exception
    {
        try 
        {               
            
            if(musteriAdres == null)
            musteriAdres = "";
            if(musteriIl == null)
            musteriIl = "";
            if(musteriIlce == null)
            musteriIlce = "";
            if(musteriUlke == null)
            musteriUlke = "";
            
            JasperReport jasperReport = null;
            JasperPrint jasperPrint = null;
            jasperReport = JasperCompileManager.compileReport(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "resources/reports/InvoiceEnglish.jrxml");
            
            HashMap parameters = new HashMap();
            parameters.put("teklifBaslikRecordId", teklifBaslikRecordId);
            parameters.put("teklifNo", teklifNo);
            parameters.put("musteriUnvan", musteriUnvan);
            parameters.put("musteriAdres", musteriAdres);
            parameters.put("musteriIl", musteriIl);
            parameters.put("musteriIlce", musteriIlce);
            parameters.put("musteriUlke", musteriUlke);
            parameters.put("toplamTutar", toplamTutar);
            parameters.put("toplamIndirim", toplamIndirim);
            parameters.put("netTutar", netTutar);
            
             if(paraBirimi == 0)
                parameters.put("paraBirimi", "₺");
            else if(paraBirimi == 1)
                parameters.put("paraBirimi", "$");
            else if(paraBirimi == 20)
                parameters.put("paraBirimi", "€");
            
            parameters.put("image", FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "resources/images/firmaLogo.png");
            
            DbConnection dbConnection = new DbConnection();
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dbConnection.baglantiAc());
            //JasperViewer.viewReport(jasperPrint,false);
            
             HttpServletResponse response = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
            
            if(pdfOrXlsx.equals("pdf"))
            {
                response.setContentType("application/x-download");
                response.addHeader("Content-disposition", "attachment; filename=" + teklifNo + ".pdf");
                OutputStream out = response.getOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint,out);
            }            
            else if(pdfOrXlsx.equals("xlsx"))
            {
                response.addHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.addHeader("Content-disposition", "attachment; filename=" + teklifNo + ".xlsx");
                ServletOutputStream outputStream = response.getOutputStream();
                net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter exporter = new net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,outputStream);
                exporter.exportReport();
            }
            FacesContext.getCurrentInstance().responseComplete();
            
        } 
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
    }

    public void guncelleTeklifSatirYanit(TEKLIF_SATIR_YANIT teklifSatirYanit)throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        PreparedStatement preparedStatement = null;
        
        String query = " UPDATE  TEKLIF_SATIR_YANIT "
                        + " SET ACIKLAMA = ?, "
                        + " BIRIM_FIYATI = ?, "
                        + " MIKTAR = ?, "
                        + " BIRIM = ?, "
                        + " STOKTA_VAR_YOK = ?, "
                        + " GUNCELLEME_TARIHI = ? "
                     + " WHERE TEKLIF_SATIR_YANIT_ID = ? " ;
        
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, teklifSatirYanit.getACIKLAMA());
            preparedStatement.setDouble(2, teklifSatirYanit.getBIRIM_FIYATI());
            preparedStatement.setDouble(3, teklifSatirYanit.getMIKTAR());
            preparedStatement.setString(4, teklifSatirYanit.getBIRIM());
            
            if(teklifSatirYanit.isStoktaVarYokBoolean() == true)
                preparedStatement.setInt(5, 1);
            else
                preparedStatement.setInt(5, 0);
            
            preparedStatement.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
            preparedStatement.setInt(7, teklifSatirYanit.getTEKLIF_SATIR_YANIT_ID());
            
            preparedStatement.executeUpdate();
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapat();
            if(preparedStatement!=null)
            { 
              preparedStatement.close();
            }     
        }
    }
    
    public void guncelleTeklifSatir(TEKLIF_SATIR teklifSatir)throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        PreparedStatement preparedStatement = null;
        
        String query = " UPDATE  TEKLIF_SATIR"
                        + " SET MUSTERI_SATIS_MIKTAR_YANIT = ?, "
                        + " GUNCELLEME_TARIHI = ? "
                     + " WHERE TEKLIF_SATIR_ID = ? " ;
        
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, teklifSatir.getMUSTERI_SATIS_MIKTAR_YANIT());
            preparedStatement.setTimestamp(2, new java.sql.Timestamp(new java.util.Date().getTime()));
            preparedStatement.setInt(3, teklifSatir.getTEKLIF_SATIR_ID());
            
            preparedStatement.executeUpdate();
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapat();
            if(preparedStatement!=null)
            { 
              preparedStatement.close();
            }     
        }
    }
    
    public void guncelleTeklifSatirTumu(List<TEKLIF_SATIR> teklifSatirList)throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        PreparedStatement preparedStatement = null;
        
        String query = " UPDATE  TEKLIF_SATIR"
                        + " SET MUSTERI_SATIS_MIKTAR_YANIT = ? "
                        + " ,GUNCELLEME_TARIHI = ? "
                        + " ,MALZEME_HIZMET_MASRAF_TUTARI = ?"
                     + " WHERE TEKLIF_SATIR_ID = ? " ;
        
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            for(TEKLIF_SATIR teklifSatir : teklifSatirList)
            {
                preparedStatement.setDouble(1, teklifSatir.getMUSTERI_SATIS_MIKTAR_YANIT());
                preparedStatement.setTimestamp(2, new java.sql.Timestamp(new java.util.Date().getTime()));
                preparedStatement.setDouble(3, teklifSatir.getMALZEME_HIZMET_MASRAF_BIRIM_FIYATI() 
                                               * teklifSatir.getMUSTERI_SATIS_MIKTAR_YANIT());
                preparedStatement.setInt(4, teklifSatir.getTEKLIF_SATIR_ID());            
                preparedStatement.executeUpdate();
            }            
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapat();
            if(preparedStatement!=null)
            { 
              preparedStatement.close();
            }     
        }
    }
    
    public List<String> getirBirimlerERPden(String ERPFirmaNo, String malzemeKodu) throws Exception
    {
        String ERPFirmaNoTemp = "";
        
        if(ERPFirmaNo.length() == 1)
                ERPFirmaNoTemp = "00" + ERPFirmaNo;
            else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        DbConnection dbConnectionERP = new DbConnection();
        Connection connectionERP = dbConnectionERP.baglantiAcERP();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT UNL.CODE + ' (' + UNF.CODE + ')' AS CODE"
                     + " FROM LG_" + ERPFirmaNoTemp + "_UNITSETL UNL "
                     + " INNER JOIN LG_" + ERPFirmaNoTemp + "_UNITSETF UNF ON UNL.UNITSETREF = UNF.LOGICALREF "
                     + " INNER JOIN LG_" + ERPFirmaNoTemp + "_ITMUNITA ITMU ON  ITMU.UNITLINEREF = UNL.LOGICALREF "
                     + " INNER JOIN LG_" + ERPFirmaNoTemp + "_ITEMS ITM ON  ITM.LOGICALREF = ITMU.ITEMREF"
                     + " WHERE ITM.CODE='" + malzemeKodu + "'";
        
        preparedStatement = connectionERP.prepareStatement(query);
        List<String> birimler = new ArrayList<>();
        
        try 
        {            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {
                String birim = new String();                          
                birim = resultSet.getString("CODE");
                birimler.add(birim);
            }             
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnectionERP.baglantiKapatERP();
            if(preparedStatement!=null)
            { 
              preparedStatement.close();
            }     
            return birimler;
        }
    }
    
    public MAIL_ADRES getirMailAdresMailAdresIdIle(int mailAdresId) throws Exception
    {
        MAIL_ADRES mailAdres = new MAIL_ADRES();
        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT "
                     + " * "
                     + " FROM MAIL_ADRES "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     + " AND MAIL_ADRES_ID = ? ";
        
        try 
        {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, mailAdresId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                mailAdres.setTAMAMLANDI_MI(resultSet.getInt("TAMAMLANDI_MI"));
                mailAdres.setMAIL_ADRESI(resultSet.getString("MAIL_ADRESI"));
                mailAdres.setMAIL_ID(resultSet.getInt("MAIL_ID"));   
                mailAdres.setTeklifYanitlayanAdSoyad(resultSet.getString("YANITLAYAN_AD_SOYAD"));
                mailAdres.setKUR(resultSet.getDouble("KUR"));
                
                if(resultSet.getString("PARA_BIRIMI")!=null)
                    if(resultSet.getString("PARA_BIRIMI").isEmpty())
                        mailAdres.setPARA_BIRIMI("TL");
                    else
                        mailAdres.setPARA_BIRIMI(resultSet.getString("PARA_BIRIMI")); //null değil, boş da değilse...
                else
                    mailAdres.setPARA_BIRIMI("TL");
            }           
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapat();   
            if(preparedStatement!=null)
            { 
              preparedStatement.close();
            }            
            return mailAdres;
        }
    }
    
    public MAIL getirMailMailIdIle(int mailId) throws Exception
    {
        MAIL mail = new MAIL();
        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT "
                     + " * "
                     + " FROM MAIL "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     + " AND MAIL_ID = ? ";
        
        try 
        {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, mailId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                mail.setMAIL_GONDEREN(resultSet.getString("MAIL_GONDEREN"));    
                mail.setMAIL_ICERIK(resultSet.getString("MAIL_ICERIK")); 
                mail.setMAIL_KONU(resultSet.getString("MAIL_KONU")); 
            }   
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapat();   
            if(preparedStatement!=null)
            { 
              preparedStatement.close();
            }            
            return mail;
        }
    }
    
    public void tamamlaMailAdres(int mailAdresId, 
                                String teklifiTamamlayanAdSoyad,
                                String paraBirimi,
                                Double kur) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " UPDATE  "
                     + " MAIL_ADRES "
                     + " SET TAMAMLANDI_MI = 1, "
                     + " YANITLAYAN_AD_SOYAD = ?, " 
                     + " PARA_BIRIMI = ?, " 
                     + " KUR = ? " 
                     + " WHERE MAIL_ADRES_ID = ? " ;
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, teklifiTamamlayanAdSoyad);
            preparedStatement.setString(2, paraBirimi);
            preparedStatement.setDouble(3, kur);
            preparedStatement.setInt(4, mailAdresId);
            
            preparedStatement.executeUpdate();    
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapat();   
            if(preparedStatement!=null)
            { 
              preparedStatement.close();
            }     
        }
    }
        
    public EMAIL_AYAR getirEmailAyar() throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT * "
                     + " FROM EMAIL_AYAR "
                     + " WHERE KULLANIM_DURUMU = 1 ";
        
        preparedStatement = connection.prepareStatement(query);
        EMAIL_AYAR emailAyar = new EMAIL_AYAR();
        
        try 
        {            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {                     
                emailAyar.setEMAIL_AYAR_ID(resultSet.getInt("EMAIL_AYAR_ID"));                 
                emailAyar.setEMAIL_SUNUCU_ADI(resultSet.getString("EMAIL_SUNUCU_ADI")); 
                emailAyar.setEMAIL_SUNUCU_PORT_NUMARASI(resultSet.getString("EMAIL_SUNUCU_PORT_NUMARASI")); 
                emailAyar.setEMAIL_ADRES(resultSet.getString("EMAIL_ADRES")); 
                emailAyar.setEMAIL_SIFRE(resultSet.getString("EMAIL_SIFRE"));  
            }             
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapat();   
            if(preparedStatement!=null)
            { 
              preparedStatement.close();
            }     
            return emailAyar;
        }
    }
    
    public void download(int mailAdresId,
                         String teklifNo,
                         String musteriUnvan,
                         String musteriAdres,
                         double toplamTutar,
                         String paraBirimi,
                         String pdfOrXlsx) throws Exception
    {
        try 
        {              
            
            JasperReport jasperReport = null;
            JasperPrint jasperPrint = null;
            jasperReport = JasperCompileManager.compileReport(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "resources/reports/offerUserSatinAlma.jrxml");
            
            HashMap parameters = new HashMap();
            parameters.put("mailAdresId", mailAdresId);
            parameters.put("teklifNo", teklifNo);
            parameters.put("musteriUnvan", musteriUnvan);
            parameters.put("musteriAdres", musteriAdres);
            parameters.put("toplamTutar", toplamTutar);
            
            if(paraBirimi.equals("TL"))
                parameters.put("paraBirimi", "₺");
            else if(paraBirimi.equals("USD"))
                parameters.put("paraBirimi", "$");
            else if(paraBirimi.equals("EUR"))
                parameters.put("paraBirimi", "€");
            
            parameters.put("image", FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "resources/images/firmaLogo.png");
            
            DbConnection dbConnection = new DbConnection();
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dbConnection.baglantiAc());
            //JasperViewer.viewReport(jasperPrint,false);
            
            HttpServletResponse response = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
            
            if(pdfOrXlsx.equals("pdf"))
            {
                response.setContentType("application/x-download");
                response.addHeader("Content-disposition", "attachment; filename=" + teklifNo + ".pdf");
                OutputStream out = response.getOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint,out);
            }            
            else if(pdfOrXlsx.equals("xlsx"))
            {
                response.addHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.addHeader("Content-disposition", "attachment; filename=" + teklifNo + ".xlsx");
                ServletOutputStream outputStream = response.getOutputStream();
                net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter exporter = new net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,outputStream);
                exporter.exportReport();
            }
            FacesContext.getCurrentInstance().responseComplete();
        } 
        
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
    }
    
    public List<File> olusturDokuman( int mailAdresId,
                                String teklifNo,
                                String musteriUnvan,
                                String musteriAdres,
                                double toplamTutar,
                                String paraBirimi) throws Exception
    {
        List<File> pdfVeXlsx = new ArrayList<>();
        
        try 
        {       
            JasperReport jasperReport = null;
            JasperPrint jasperPrint = null;
            jasperReport = JasperCompileManager.compileReport(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "resources/reports/offerUserSatinAlma.jrxml");
            
            HashMap parameters = new HashMap();
            parameters.put("mailAdresId", mailAdresId);
            parameters.put("teklifNo", teklifNo);
            parameters.put("musteriUnvan", musteriUnvan);
            parameters.put("musteriAdres", musteriAdres);
            parameters.put("toplamTutar", toplamTutar);
            
            if(paraBirimi.equals("TL"))
                parameters.put("paraBirimi", "₺");
            else if(paraBirimi.equals("USD"))
                parameters.put("paraBirimi", "$");
            else if(paraBirimi.equals("EUR"))
                parameters.put("paraBirimi", "€");
            
            parameters.put("image", FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "resources/images/firmaLogo.png");
            
            DbConnection dbConnection = new DbConnection();
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dbConnection.baglantiAc());
            
            File pdf = File.createTempFile("output.", ".pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));
              
            File xlsx = File.createTempFile("output.", ".xlsx");
            OutputStream outputXLSX = new FileOutputStream(xlsx); 
            net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter exporter = new net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,outputXLSX);
            exporter.exportReport();
            
            pdfVeXlsx.add(pdf);
            pdfVeXlsx.add(xlsx);
            
            return pdfVeXlsx;
            
        } 
        
        catch (Exception ex) 
        {
            System.out.println(ex);
            return pdfVeXlsx;
        }
    }
    
}
