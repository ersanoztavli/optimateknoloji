/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.dao;

import com.oms.models.FIRMA;
import com.oms.models.MAIL;
import com.oms.models.MAIL_ADRES;
import com.oms.models.MUSTERI;
import com.oms.models.MailListSatinAlma;
import com.oms.models.TEKLIF_BASLIK;
import com.oms.models.TEKLIF_SATIR;
import com.oms.models.TEKLIF_SATIR_YANIT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author ersan
 */
public class SatinAlmaDao {
    
    public List<TEKLIF_BASLIK> getirTeklifBasliklari(String erpFirmaNumber) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT * "
                     + " FROM TEKLIF_BASLIK "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     + " AND SATIS_SATINALMA = 1 " //Satın alma olunca 1, satış olunca 0 olacak...
                     + " AND ERP_FIRMA_NUMBER = ? " 
                     + " ORDER BY TEKLIF_NUMARASI DESC ";
        
        preparedStatement = connection.prepareStatement(query);
        List<TEKLIF_BASLIK> teklifBasliklari = new ArrayList<>();
        
        try 
        {            
            preparedStatement.setString(1, erpFirmaNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {
                TEKLIF_BASLIK teklifBaslik = new TEKLIF_BASLIK();                          
                teklifBaslik.setTEKLIF_BASLIK_ID(resultSet.getInt("TEKLIF_BASLIK_ID"));                 
                teklifBaslik.setTEKLIF_NUMARASI(resultSet.getString("TEKLIF_NUMARASI")); 
                teklifBaslik.setTEKLIF_TARIHI(resultSet.getTimestamp("TEKLIF_TARIHI")); 
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
                               
                teklifBaslik.setSORUMLU_PERSONEL(resultSet.getString("SORUMLU_PERSONEL"));
                teklifBaslik.setBELGE_NO(resultSet.getString("BELGE_NO")); 
                
                MUSTERI musteri = getirMusteriRecorIdIle(teklifBaslik.getMUSTERI_RECORD_ID());
                teklifBaslik.setMusteriUnvani(musteri.getMUSTERI_UNVANI());
                teklifBaslik.setMusteriMail1(musteri.getMUSTERI_MAIL_ADRESI1());
                teklifBaslik.setMusteriMail2(musteri.getMUSTERI_MAIL_ADRESI2());  
                teklifBaslik.setMusteriKodu(musteri.getMUSTERI_KODU());
                
//                teklifBaslik.setMusteriUlke(musteri.getMUSTERI_ULKE());
//                teklifBaslik.setMusteriIl(musteri.getMUSTERI_IL());
//                teklifBaslik.setMusteriIlce(musteri.getMUSTERI_ILCE());
//                teklifBaslik.setMusteriAdres(musteri.getMUSTERI_ADRESI());
                
                teklifBasliklari.add(teklifBaslik);
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
            return teklifBasliklari;
        }
    }
    
    public List<TEKLIF_BASLIK> getirTeklifBasliklariGizlenen(String erpFirmaNumber) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT * "
                     + " FROM TEKLIF_BASLIK "
                     + " WHERE KULLANIM_DURUMU = 2 " // Gizlenenleri getir demiş olduk...
                     + " AND SATIS_SATINALMA = 1 " //Satın alma olunca 1, satış olunca 0 olacak...
                     + " AND ERP_FIRMA_NUMBER = ? " 
                     + " ORDER BY TEKLIF_NUMARASI DESC ";
        
        preparedStatement = connection.prepareStatement(query);
        List<TEKLIF_BASLIK> teklifBasliklari = new ArrayList<>();
        
        try 
        {            
            preparedStatement.setString(1, erpFirmaNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {
                TEKLIF_BASLIK teklifBaslik = new TEKLIF_BASLIK();                          
                teklifBaslik.setTEKLIF_BASLIK_ID(resultSet.getInt("TEKLIF_BASLIK_ID"));                 
                teklifBaslik.setTEKLIF_NUMARASI(resultSet.getString("TEKLIF_NUMARASI")); 
                teklifBaslik.setTEKLIF_TARIHI(resultSet.getTimestamp("TEKLIF_TARIHI")); 
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
                               
                teklifBaslik.setSORUMLU_PERSONEL(resultSet.getString("SORUMLU_PERSONEL"));
                teklifBaslik.setBELGE_NO(resultSet.getString("BELGE_NO")); 
                
                MUSTERI musteri = getirMusteriRecorIdIle(teklifBaslik.getMUSTERI_RECORD_ID());
                teklifBaslik.setMusteriUnvani(musteri.getMUSTERI_UNVANI());
                teklifBaslik.setMusteriMail1(musteri.getMUSTERI_MAIL_ADRESI1());
                teklifBaslik.setMusteriMail2(musteri.getMUSTERI_MAIL_ADRESI2());  
                teklifBaslik.setMusteriKodu(musteri.getMUSTERI_KODU());
                
//                teklifBaslik.setMusteriUlke(musteri.getMUSTERI_ULKE());
//                teklifBaslik.setMusteriIl(musteri.getMUSTERI_IL());
//                teklifBaslik.setMusteriIlce(musteri.getMUSTERI_ILCE());
//                teklifBaslik.setMusteriAdres(musteri.getMUSTERI_ADRESI());
                
                teklifBasliklari.add(teklifBaslik);
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
            return teklifBasliklari;
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
                musteri.setMUSTERI_MAIL_ADRESI1(resultSet.getString("MUSTERI_MAIL_ADRESI1"));
                musteri.setMUSTERI_MAIL_ADRESI2(resultSet.getString("MUSTERI_MAIL_ADRESI2"));
                musteri.setMUSTERI_KODU(resultSet.getString("MUSTERI_KODU"));
                
//                musteri.setMUSTERI_ULKE(resultSet.getString("MUSTERI_ULKE"));  
//                musteri.setMUSTERI_IL(resultSet.getString("MUSTERI_IL"));
//                musteri.setMUSTERI_ILCE(resultSet.getString("MUSTERI_ILCE"));
//                musteri.setMUSTERI_ADRESI(resultSet.getString("MUSTERI_ADRESI"));
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
    
    public List<MailListSatinAlma> getirMailListSatinAlma(String ERPFirmaNo
                                                         ,List<TEKLIF_SATIR> teklifSatirlari) throws Exception
    {
        List<MailListSatinAlma> mailListSatinAlmaList = new ArrayList<>();
        
        DbConnection dbConnection = new DbConnection();
        Connection connectionERP = dbConnection.baglantiAcERP();
        PreparedStatement preparedStatementERP = null;
        
        String ERPFirmaNoTemp = "";
        
        if(ERPFirmaNo.length() == 1)
                ERPFirmaNoTemp = "00" + ERPFirmaNo;
            else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        String items = "";
        for (TEKLIF_SATIR teklifSatir : teklifSatirlari)
            if(teklifSatir.getMALZEME_HIZMET_MASRAF_KODU() != null)
            items = items + ",'" + teklifSatir.getMALZEME_HIZMET_MASRAF_KODU() + "'";

        //Birinci sıradaki virgülü silmek için...
        items = items.substring(1);
        
        String queryERP = " SELECT DISTINCT CLC.CODE AS ICUSTSUPCODE " +
                          " FROM LG_" + ERPFirmaNoTemp + "_ITEMS IT " +
                          " LEFT JOIN LG_" + ERPFirmaNoTemp + "_SUPPASGN SP ON SP.ITEMREF = IT.LOGICALREF " +
                          " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_CLCARD CLC ON CLC.LOGICALREF = SP.CLIENTREF " +
                          " WHERE IT.CODE IN (" + items + ") AND SP.CLCARDTYPE = 1 ";
        
        Connection connection = dbConnection.baglantiAc();
        PreparedStatement preparedStatement = null;
        
        try 
        {
            preparedStatementERP = connectionERP.prepareStatement(queryERP);
            ResultSet resultSetERP = preparedStatementERP.executeQuery();
            String tedarikciler = "";  
            
            while(resultSetERP.next())
            {
                if(resultSetERP.getString("ICUSTSUPCODE") != null)
                tedarikciler = tedarikciler + ",'" + resultSetERP.getString("ICUSTSUPCODE") + "'";
            }
            
            preparedStatementERP.close();
            
            //Birinci sıradaki virgülü silmek için...
            if(tedarikciler.length()>0)
            tedarikciler = tedarikciler.substring(1);
            
            String query = " SELECT "
                         + " * "
                         + " FROM MUSTERI "
                         + " WHERE KULLANIM_DURUMU = 1 "
                         + " AND MUSTERI_KODU IN (" + tedarikciler + ")";
                    
            preparedStatement = connection.prepareStatement(query);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                MailListSatinAlma mailListSatinAlma = new MailListSatinAlma();
                mailListSatinAlma.setMusteriRecordId(resultSet.getString("RECORD_ID"));
                mailListSatinAlma.setMusteriUnvan(resultSet.getString("MUSTERI_UNVANI"));  
                mailListSatinAlma.setMailAdres(resultSet.getString("MUSTERI_MAIL_ADRESI2"));
                mailListSatinAlmaList.add(mailListSatinAlma);
            }   
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnection.baglantiKapat(); 
            dbConnection.baglantiKapatERP();
            if(preparedStatement!=null)
            { 
              preparedStatement.close();
            }            
            return mailListSatinAlmaList;
        }
    }
    
    public String getirEnBuyukMusteriRecordId(String ERPFirmaNo) throws Exception
    {
        String enBuyukMusteriRecordId = "";
        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT "
                     + " MAX(CAST(RECORD_ID AS INT)) AS RECORD_ID "
                     + " FROM MUSTERI "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     + " AND ERP_FIRMA_NUMBER = '" + ERPFirmaNo + "'";
        
        preparedStatement = connection.prepareStatement(query);
        
        try 
        {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                enBuyukMusteriRecordId = resultSet.getString("RECORD_ID");  
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
            if(enBuyukMusteriRecordId == null)
                enBuyukMusteriRecordId = "";
            
            return enBuyukMusteriRecordId;
        }
    }
    
    
    public void gizleTeklifListesi(int kullaniciId, List<TEKLIF_BASLIK> teklifListesi) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String teklifNoListesi = "";
        for (TEKLIF_BASLIK teklifBaslik : teklifListesi)
            if(teklifBaslik.getTEKLIF_NUMARASI()!= null)
            teklifNoListesi = teklifNoListesi + ",'" + teklifBaslik.getTEKLIF_NUMARASI() + "'";

        //Birinci sıradaki virgülü silmek için...
        teklifNoListesi = teklifNoListesi.substring(1);
        
        String query = " UPDATE  "
                     + " TEKLIF_BASLIK "
                     + " SET KULLANIM_DURUMU = 2, "
                     + " GUNCELLEYEN_ID =  " + kullaniciId
                     + " WHERE TEKLIF_NUMARASI IN (" + teklifNoListesi + ") " ;
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
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
    
    public void geriGetirTeklifListesi(int kullaniciId, List<TEKLIF_BASLIK> teklifListesi) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String teklifNoListesi = "";
        for (TEKLIF_BASLIK teklifBaslik : teklifListesi)
            if(teklifBaslik.getTEKLIF_NUMARASI()!= null)
            teklifNoListesi = teklifNoListesi + ",'" + teklifBaslik.getTEKLIF_NUMARASI() + "'";

        //Birinci sıradaki virgülü silmek için...
        teklifNoListesi = teklifNoListesi.substring(1);
        
        String query = " UPDATE  "
                     + " TEKLIF_BASLIK "
                     + " SET KULLANIM_DURUMU = 1, "
                     + " GUNCELLEYEN_ID =  " + kullaniciId
                     + " WHERE TEKLIF_NUMARASI IN (" + teklifNoListesi + ") " ;
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
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
    
    
    public void kaydetMusterilerERPden(String enBuyukMusteriRecordId, 
                                       int olusturanId, 
                                       String ERPFirmaNo) throws Exception
    {
        String ERPFirmaNoTemp = "";
        
        if(ERPFirmaNo.length() == 1)
                ERPFirmaNoTemp = "00" + ERPFirmaNo;
            else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        DbConnection dbConnection = new DbConnection();
        Connection connectionERP = dbConnection.baglantiAcERP();
        
        PreparedStatement preparedStatement = null;
        
        String query =  "INSERT INTO " + dbConnection.getDbName() + ".[dbo].[MUSTERI] " +
                        " ( " +
                        " MUSTERI_KODU " +
                        " ,MUSTERI_UNVANI " +
                        " ,MUSTERI_VKNTCKN " +
                        " ,MUSTERI_VERGI_DAIRESI " +
                        " ,MUSTERI_ADRESI " +
                        " ,MUSTERI_IL " +
                        " ,MUSTERI_ILCE " +
                        " ,MUSTERI_ULKE " +
                        " ,MUSTERI_MAIL_ADRESI1 " +
                        " ,MUSTERI_MAIL_ADRESI2 " +
                        " ,MUSTERI_TELEFON1 " +
                        " ,MUSTERI_TELEFON2 " +
                        " ,MUSTERI_FAX " +
                        " ,RECORD_ID " +
                        " ,KULLANIM_DURUMU " +
                        " ,OLUSTURMA_TARIHI " +
                        " ,OLUSTURAN_ID " +
                        " ,ERP_FIRMA_NUMBER " +
                        " ) " +
                        " SELECT DISTINCT " +
                            " CLC.CODE AS MUSTERI_KODU, " +
                            " CLC.DEFINITION_ AS MUSTERI_UNVANI, " +
                                " CASE CLC.ISPERSCOMP " +
                                " WHEN 1 THEN CLC.TCKNO " +
                                " ELSE CLC.TAXNR " +
                                " END AS MUSTERI_VKNTCKN, " +
                            " CLC.TAXOFFICE AS MUSTERI_VERGI_DAIRESI, " +
                            " CLC.ADDR1+ ' ' + CLC.ADDR2 AS MUSTERI_ADRESI, " +
                            " CLC.CITY AS MUSTERI_IL, " +
                            " CLC.TOWN AS MUSTERI_ILCE, " +
                            " CLC.COUNTRY AS MUSTERI_ULKE, " +
                            " CLC.EMAILADDR AS MUSTERI_MAIL_ADRESI1, " +
                            " CLC.EMAILADDR2 AS MUSTERI_MAIL_ADRESI2, " +
                            " CLC.TELEXTNUMS1 AS MUSTERI_TELEFON1, " +
                            " CLC.TELEXTNUMS2 AS MUSTERI_TELEFON2, " +
                            " CLC.FAXNR AS MUSTERI_FAX, " +
                            " CLC.LOGICALREF AS RECORD_ID, " +
                            " 1 AS KULLANIM_DURUMU, " +
                            "'" + new java.sql.Timestamp(new java.util.Date().getTime())+ "'" + " AS OLUSTURMA_TARIHI, " +
                            olusturanId + " AS OLUSTURAN_ID, " +
                            ERPFirmaNo + " AS ERP_FIRMA_NUMBER " +
                            " FROM LG_" + ERPFirmaNoTemp + "_CLCARD CLC ";
        
                            //Yani İlk defa ERP ile temasa geçiliyorsa hesini çek demek...
                            if (enBuyukMusteriRecordId != "" )
                            {
                              query = query + " WHERE CLC.LOGICALREF > " + enBuyukMusteriRecordId ;
                            }
              try 
                {            
                    preparedStatement = connectionERP.prepareStatement(query);
                    preparedStatement.executeUpdate();
                }
                catch (Exception ex) 
                {
                    System.out.println(ex);
                }
                finally
                {
                    dbConnection.baglantiKapatERP();
                    if(preparedStatement!=null)
                    { 
                      preparedStatement.close();
                    }     
                }        
    }
    
    public String getirEnBuyukTeklifBaslikRecordId(String ERPFirmaNo) throws Exception
    {
        String enBuyukTeklifBaslikRecordId = "";
        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT "
                     + " MAX(CAST(RECORD_ID AS INT)) AS RECORD_ID "
                     + " FROM TEKLIF_BASLIK "
                     //+ " WHERE KULLANIM_DURUMU IN (1,2) " // Gizlenenleri de dikkate almak için
                     //+ " AND SATIS_SATINALMA = 1 " // Satışsa 0, satın alma ise 1 olacak...
                     + " WHERE SATIS_SATINALMA = 1 "
                     + " AND ERP_FIRMA_NUMBER = '" + ERPFirmaNo + "'";
        
        preparedStatement = connection.prepareStatement(query);
        
        try 
        {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                enBuyukTeklifBaslikRecordId = resultSet.getString("RECORD_ID");  
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
            if(enBuyukTeklifBaslikRecordId == null)
                enBuyukTeklifBaslikRecordId = "";
            
            return enBuyukTeklifBaslikRecordId;
        }
    }
    
    public void kaydetTeklifBaslikERPden(String enBuyukTeklifBaslikRecordId, 
                                         int olusturanId, 
                                         String ERPFirmaNo,
                                         int firmaId) throws Exception
    {
        String ERPFirmaNoTemp = "";
        
        if(ERPFirmaNo.length() == 1)
                ERPFirmaNoTemp = "00" + ERPFirmaNo;
            else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        
        DbConnection dbConnection = new DbConnection();
        Connection connectionERP = dbConnection.baglantiAcERP();
        
        PreparedStatement preparedStatement = null;
        
        String query =  "INSERT INTO " + dbConnection.getDbName() + ".[dbo].[TEKLIF_BASLIK] " +
                        " ( " +
                        " TEKLIF_NUMARASI " +
                        " ,TEKLIF_TARIHI " +
                        " ,REVIZYON_TARIHI " +
                        " ,GECERLI_TEKLIF_REVIZYON_NUMARASI " +
                        " ,REVIZYON_NUMARASI " +
                        " ,TEKLIF_BASLANGIC_TARIHI " +
                        " ,TEKLIF_BITIS_TARIHI " +
                        " ,TEKLIF_DURUM_KODU " +
                        " ,TEKLIF_DURUM_KODU_ACIKLAMA " +
                        " ,TEKLIF_ACIKLAMA1 " +
                        " ,TEKLIF_ACIKLAMA2 " +
                        " ,TEKLIF_ACIKLAMA3 " +
                        " ,SORUMLU_PERSONEL " +
                        " ,TESLIMAT_ZAMANI " +
                        " ,VADE_ZAMANI " +
                        " ,ODEME_SEKLI " +
                        " ,TEKLIF_TUTARI " +
                        " ,TEKLIF_TOPLAM_TUTARI " +
                        " ,TEKLIF_TOPLAM_KDV_TUTARI " +
                        " ,TEKLIF_TOPLAM_OTV_TUTARI " +
                        " ,TEKLIF_TOPLAM_OIV_TUTARI " +
                        " ,TEKLIF_TOPLAM_DIGER_VERGI1_TUTARI " +
                        " ,TEKLIF_TOPLAM_DIGER_VERGI2_TUTARI " +
                        " ,TEKLIF_TOPLAM_INDIRIM_TUTARI " +
                        " ,TEKLIF_TOPLAM_INDIRIM_ORANI " +
                        " ,TEKLIF_PARA_BIRIMI " +
                        " ,TEKLIF_KUR " +
                        " ,RECORD_ID " +
                        " ,MUSTERI_RECORD_ID " +
                        " ,TEKLIF_GUID " +
                        " ,OLUSTURAN_ID " +
                        " ,OLUSTURMA_TARIHI " +
                        " ,KULLANIM_DURUMU " +
                        " ,FIRMA_ID " +
                        " ,ERP_FIRMA_NUMBER " +
                        " ,SATIS_SATINALMA " +
                        " ,BELGE_NO " +
                        " ) " +
                        " SELECT DISTINCT " +    
                            " PUR.FICHENO AS TEKLIF_NUMARASI, " +
                            " PUR.DATE_ AS TEKLIF_TARIHI, " +
                            " PUR.APPROVEDATE AS REVIZYON_TARIHI, " +
                            " PUR.ALTNR AS GECERLI_TEKLIF_REVIZYON_NUMARASI, " +
                            " PUR.REVISNR AS REVIZYON_NUMARASI, " +
                            " PUR.POFFERBEGDT AS TEKLIF_BASLANGIC_TARIHI, " +
                            " PUR.POFFERENDDT AS TEKLIF_BITIS_TARIHI, " +
                            " PUR.STATUS AS TEKLIF_DURUM_KODU, " + 
                                        " CASE PUR.STATUS " + 
                                        " WHEN 1 THEN 'ONAY BEKLİYOR' " +  
                                        " WHEN 2 THEN 'ONAYLANMADI' " + 
                                        " WHEN 3 THEN 'ONAYLANDI' " +  
                                        " WHEN 4 THEN 'ONAYLANDI' " +    
                                        " ELSE 'DİĞER' " + 
                                        " END AS TEKLIF_DURUM_KODU_ACIKLAMA, " +  
                            " PUR.GENEXP1 AS TEKLIF_ACIKLAMA1, " +
                            " PUR.GENEXP2 AS TEKLIF_ACIKLAMA2, " +
                            " PUR.GENEXP3 AS TEKLIF_ACIKLAMA3, " +
                            " USR.USERNAME + ' ' + USR.USERSURNAME AS SORUMLU_PERSONEL, " +
                            " PUR.GENEXP5 AS TESLIMAT_ZAMANI, " +
                            " PUR.GENEXP6 AS VADE_ZAMANI, " +
                            " PUR.PAYMENTTYPE AS ODEME_SEKLI, " +
                            " PUR.GROSSTOTAL AS TEKLIF_TUTARI, " +
                            " PUR.NETTOTAL AS TEKLIF_TOPLAM_TUTARI, " +
                            " PUR.TOTALVAT AS TEKLIF_TOPLAM_KDV_TUTARI, " +
                            " PUR.ADDEXPENSESVAT AS TEKLIF_TOPLAM_OTV_TUTARI, " +
                            " 0 AS TEKLIF_TOPLAM_OIV_TUTARI, " +
                            " 0 AS TEKLIF_TOPLAM_DIGER_VERGI1_TUTARI, " +
                            " 0 AS TEKLIF_TOPLAM_DIGER_VERGI2_TUTARI, " +
                            " PUR.TOTALDISCOUNTS  AS TEKLIF_TOPLAM_INDIRIM_TUTARI, " +
                            " NULLIF(PUR.TOTALDISCOUNTED,0) / NULLIF(PUR.TOTALDISCOUNTS,0) AS TEKLIF_TOPLAM_INDIRIM_ORANI, " +    
                            " PUR.TRCURR AS TEKLIF_PARA_BIRIMI, " +
                            " PUR.TRRATE AS TEKLIF_KUR, " +
                            " PUR.LOGICALREF AS RECORD_ID, " +
                            " CLC.LOGICALREF AS MUSTERI_RECORD_ID, " +
                            " PUR.GUID AS TEKLIF_GUID, " +
                            olusturanId + " AS OLUSTURAN_ID, " +
                            "'" + new java.sql.Timestamp(new java.util.Date().getTime())+ "'" + " AS OLUSTURMA_TARIHI, " +
                            " 1 AS KULLANIM_DURUMU, " +
                            firmaId + " AS FIRMA_ID, " +
                            ERPFirmaNo + " AS ERP_FIRMA_NUMBER, " +
                            " 1 AS SATIS_SATINALMA, " + // 0'SA SATIS 1'SE SATIN ALMA
                            " PUR.DOCODE AS BELGE_NO " +
                        " FROM LG_" + ERPFirmaNoTemp + "_PURCHOFFER PUR " + 
                        " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_CLCARD CLC ON CLC.LOGICALREF = PUR.CLIENTREF " +
                        " LEFT OUTER JOIN LG_SLSACTIV SLSAC WITH(NOLOCK) ON (PUR.SLSACTREF  =  SLSAC.LOGICALREF) " +
                        " LEFT OUTER JOIN LG_CSTVND CSTVND  WITH(NOLOCK) ON (CSTVND .LOGICALREF  =  SLSAC.CSTVNDREF) " +
                        " LEFT OUTER JOIN L_CAPIUSER USR ON USR.NR = PUR.CAPIBLOCK_CREATEDBY " +
                        " WHERE PUR.TRCODE = 2 " + 
                          " AND PUR.CANCELLED = 0 " + 
                         // " AND PUR.TYP = 1 " +
                          " AND PUR.STATUS = 1 " + //Sadece Onay bekleyenleri çek demek...
                          //"";
                        " AND (PUR.DATE_ >= DATEADD(DAY, -10, GETDATE())) " ; //Son on günlük verileri çek demek...
                
                            //Yani İlk defa ERP ile temasa geçiliyorsa hesini çek demek...
                            if (enBuyukTeklifBaslikRecordId != "" )
                            {
                              query = query + " AND PUR.LOGICALREF > " + enBuyukTeklifBaslikRecordId ;
                            }
              try 
                {            
                    preparedStatement = connectionERP.prepareStatement(query);
                    preparedStatement.executeUpdate();
                }
                catch (Exception ex) 
                {
                    System.out.println(ex);
                }
                finally
                {
                    dbConnection.baglantiKapatERP();
                    if(preparedStatement!=null)
                    { 
                      preparedStatement.close();
                    }     
                }        
    }
    
    public void kaydetTeklifSatirERPden(int olusturanId, 
                                         String ERPFirmaNo,
                                         String enBuyukTeklifBaslikRecordId) throws Exception
    {
        String ERPFirmaNoTemp = "";
        
        if(ERPFirmaNo.length() == 1)
                ERPFirmaNoTemp = "00" + ERPFirmaNo;
            else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;        
        
        DbConnection dbConnection = new DbConnection();
        Connection connectionERP = dbConnection.baglantiAcERP();
        
        PreparedStatement preparedStatement = null;
        
        String query =  "INSERT INTO " + dbConnection.getDbName() + ".[dbo].[TEKLIF_SATIR] " +
                        " ( " +
                        " MALZEME_HIZMET_MASRAF_KODU " +
                        " ,MALZEME_HIZMET_MASRAF_ADI " +
                        " ,MALZEME_HIZMET_MASRAF_ADI2 " +
                        " ,MALZEME_HIZMET_MASRAF_ACIKLAMASI " +
                        " ,MALZEME_HIZMET_MASRAF_BIRIM_KODU " +
                        " ,MALZEME_HIZMET_MASRAF_MIKTARI " +
                        " ,MALZEME_HIZMET_MASRAF_BIRIM_FIYATI " +
                        " ,MALZEME_HIZMET_MASRAF_INDIRIM_ORANI " +
                        " ,MALZEME_HIZMET_MASRAF_INDIRIM_TUTARI " +
                        " ,MALZEME_HIZMET_MASRAF_KDV_ORANI " +
                        " ,MALZEME_HIZMET_MASRAF_KDV_TUTARI " +
                        " ,MALZEME_HIZMET_MASRAF_OTV_TUTARI " +
                        " ,MALZEME_HIZMET_MASRAF_TUTARI " +
                        " ,MALZEME_HIZMET_MASRAF_TOPLAM_TUTARI " +
                        " ,TEKLIF_BASLIK_RECORD_ID " +
                        " ,RECORD_ID " +
                        " ,OLUSTURAN_ID " +
                        " ,OLUSTURMA_TARIHI " +
                        " ,KULLANIM_DURUMU " +
                        " ,ERP_FIRMA_NUMBER " +
                        " ,SATIR_TURU " +
                        " ,BIRIM_CEVRIM_1 " +
                        " ,BIRIM_CEVRIM_2 " +
                        " ,PARA_BIRIMI " +
                        " ,KUR " +
                        " ,TESLIM_TARIHI " +
                        " ) " +
                        //" SELECT DISTINCT " + 
                        " SELECT " + 
                            " CASE POFFERLN.LINETYPE " + 
                            " WHEN 4 THEN SRV.CODE " + 
                            " WHEN 3 THEN 'MASRAF' " + 
                            " WHEN 0 THEN ITM.CODE " + 
                            " END AS MALZEME_HIZMET_MASRAF_KODU, " +
                                    " CASE POFFERLN.LINETYPE " + 
                                    " WHEN 4 THEN SRV.DEFINITION_ " +
                                    " WHEN 3 THEN 'MASRAF' " +  
                                    " WHEN 0 THEN ITM.NAME " + 
                                    " END AS MALZEME_HIZMET_MASRAF_ADI, " +
                        " ITM.NAME3 AS MALZEME_HIZMET_MASRAF_ADI2, " +
                        " POFFERLN.LINEEXP AS MALZEME_HIZMET_MASRAF_ACIKLAMASI, " +
                        " UNL.CODE + ' (' + UNF.CODE + ')' AS MALZEME_HIZMET_MASRAF_BIRIM_KODU, " +
                        " POFFERLN.AMOUNT AS MALZEME_HIZMET_MASRAF_MIKTARI, " +
                        " POFFERLN.PRICE AS MALZEME_HIZMET_MASRAF_BIRIM_FIYATI, " +
                        " POFFERLN.DISTDISC AS MALZEME_HIZMET_MASRAF_INDIRIM_ORANI, " +
                        " POFFERLN.DISTCOST AS MALZEME_HIZMET_MASRAF_INDIRIM_TUTARI, " +
                        " POFFERLN.VAT AS MALZEME_HIZMET_MASRAF_KDV_ORANI, " +
                        " POFFERLN.VATAMNT AS MALZEME_HIZMET_MASRAF_KDV_TUTARI, " +
                        " POFFERLN.ADDTAXAMOUNT AS MALZEME_HIZMET_MASRAF_OTV_TUTARI, " +  
                        " POFFERLN.TOTAL AS MALZEME_HIZMET_MASRAF_TUTARI, " +
                        " POFFERLN.LINENET AS MALZEME_HIZMET_MASRAF_TOPLAM_TUTARI, " +  
                        " PUR.LOGICALREF AS TEKLIF_BASLIK_RECORD_ID, " +
                        " POFFERLN.LOGICALREF AS RECORD_ID, " +
                        olusturanId + " AS OLUSTURAN_ID, " +
                        "'" + new java.sql.Timestamp(new java.util.Date().getTime())+ "'" + " AS OLUSTURMA_TARIHI, " +
                        " 1 AS KULLANIM_DURUMU, " +
                        ERPFirmaNo + " AS ERP_FIRMA_NUMBER, " +
                        " POFFERLN.LINETYPE AS SATIR_TURU, " +
                        " POFFERLN.UINFO1 AS BIRIM_CEVRIM_1, " +
                        " POFFERLN.UINFO2 AS BIRIM_CEVRIM_2, " +
                        " POFFERLN.TRCURR AS PARA_BIRIMI, " +
                        " POFFERLN.TRRATE AS KUR, " +
                        " POFFERLN.ORGDUEDATE AS TESLIM_TARIHI " +                
                    " FROM LG_" + ERPFirmaNoTemp + "_PURCHOFFER PUR " +
                    " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_PURCHOFFERLN POFFERLN ON POFFERLN.ORDFICHEREF = PUR.LOGICALREF " + 
                               // " AND POFFERLN.FCTYP = 1 " +
                    " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_ITEMS ITM ON ITM.LOGICALREF = POFFERLN.STOCKREF " + 
                                " AND POFFERLN.LINETYPE IN (0,8,9) " +
                    " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_SRVCARD SRV ON SRV.LOGICALREF = POFFERLN.STOCKREF " + 
                                " AND POFFERLN.LINETYPE = 4 " +
                    " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_ITMUNITA ITMU ON ITMU.ITEMREF = ITM.LOGICALREF " +
                                " AND POFFERLN.UOMREF = ITMU.UNITLINEREF " +
                    " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_UNITSETL UNL ON UNL.LOGICALREF = ITMU.UNITLINEREF " + 
                    " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_UNITSETF UNF ON UNF.LOGICALREF = UNL.UNITSETREF  " + 
                    " WHERE PUR.TRCODE = 2 " + //Satın alma olduğu için 2 oldu.
                      " AND PUR.CANCELLED = 0 " +
                      //" AND PUR.TYP = 1 " +
                      " AND PUR.STATUS = 1 " + //Sadece Onay bekleyenleri çek demek...
                      " AND POFFERLN.PURCHOFFNR IN (SELECT MAX(PURCHOFFNR) FROM LG_" + ERPFirmaNoTemp + "_PURCHOFFERLN WHERE ORDFICHEREF = PUR.LOGICALREF) " +
                      " AND (PUR.DATE_ >= DATEADD(DAY, -10, GETDATE())) " + // Son 10 günlük verileri çek demek...
                      " AND PUR.LOGICALREF IN (SELECT RECORD_ID FROM " 
                        + dbConnection.getDbName() + ".[dbo].[TEKLIF_BASLIK] WHERE KULLANIM_DURUMU = 1 AND SATIS_SATINALMA = 1  "  ;
        
                    //Yani İlk defa ERP ile temasa geçiliyorsa hesini çek demek...
                    if (enBuyukTeklifBaslikRecordId != "" )
                    {
                      query = query + " AND RECORD_ID > " + enBuyukTeklifBaslikRecordId ;
                    }
                    
                    query = query +  " ) "  ;
                    query = query +  " ORDER BY POFFERLN.LINENO_ ASC "  ; //teklif satırlarını logo'daki ile aynı sırada
                                                                          // getirebilmek için..
                      
                try 
                {            
                    preparedStatement = connectionERP.prepareStatement(query);
                    preparedStatement.executeUpdate();
                }
                catch (Exception ex) 
                {
                    System.out.println(ex);
                }
                finally
                {
                    dbConnection.baglantiKapatERP();
                    if(preparedStatement!=null)
                    { 
                      preparedStatement.close();
                    }     
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
                     + " AND ERP_FIRMA_NUMBER = ? "
                     + " ORDER BY TEKLIF_SATIR_ID ASC ";
        
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
                teklifSatir.setSATIR_TURU(resultSet.getInt("SATIR_TURU")); 
                teklifSatir.setMALZEME_HIZMET_MASRAF_KODU(resultSet.getString("MALZEME_HIZMET_MASRAF_KODU")); 
                teklifSatir.setMALZEME_HIZMET_MASRAF_ADI(resultSet.getString("MALZEME_HIZMET_MASRAF_ADI")); 
                teklifSatir.setMALZEME_HIZMET_MASRAF_ADI2(resultSet.getString("MALZEME_HIZMET_MASRAF_ADI2"));
                teklifSatir.setMALZEME_HIZMET_MASRAF_MIKTARI(resultSet.getDouble("MALZEME_HIZMET_MASRAF_MIKTARI"));
                teklifSatir.setBIRIM_CEVRIM_1(resultSet.getInt("BIRIM_CEVRIM_1")); 
                teklifSatir.setBIRIM_CEVRIM_2(resultSet.getInt("BIRIM_CEVRIM_2")); 
                teklifSatir.setMALZEME_HIZMET_MASRAF_ACIKLAMASI(resultSet.getString("MALZEME_HIZMET_MASRAF_ACIKLAMASI")); 
                teklifSatir.setMALZEME_HIZMET_MASRAF_BIRIM_KODU(resultSet.getString("MALZEME_HIZMET_MASRAF_BIRIM_KODU"));
                teklifSatir.setMALZEME_HIZMET_MASRAF_BIRIM_FIYATI(resultSet.getInt("MALZEME_HIZMET_MASRAF_BIRIM_FIYATI"));
                teklifSatir.setMALZEME_HIZMET_MASRAF_KDV_ORANI(resultSet.getDouble("MALZEME_HIZMET_MASRAF_KDV_ORANI")); 
                teklifSatir.setMALZEME_HIZMET_MASRAF_KDV_TUTARI(resultSet.getDouble("MALZEME_HIZMET_MASRAF_KDV_TUTARI"));
                teklifSatir.setMALZEME_HIZMET_MASRAF_TOPLAM_TUTARI(resultSet.getDouble("MALZEME_HIZMET_MASRAF_TOPLAM_TUTARI")); 
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
    
    public List<MAIL> getirGonderilenSatinAlmaMailler(int teklifBaslikId) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT * "
                     + " FROM MAIL "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     + " AND TEKLIF_BASLIK_ID = ? " ;
        
        preparedStatement = connection.prepareStatement(query);
        List<MAIL> mailler = new ArrayList<>();
        
        try 
        {            
            preparedStatement.setInt(1, teklifBaslikId);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {
                MAIL mail = new MAIL();                          
                mail.setMAIL_ID(resultSet.getInt("MAIL_ID")); 
                mail.setMAIL_GONDEREN(resultSet.getString("MAIL_GONDEREN")); 
                mail.setMAIL_KONU(resultSet.getString("MAIL_KONU")); 
                mail.setMAIL_ICERIK(resultSet.getString("MAIL_ICERIK")); 
                mail.setMAIL_GONDERIM_TARIHI(resultSet.getTimestamp("MAIL_GONDERIM_TARIHI")); 
                
                mail.setMailAdresler(getirMailAdresler(mail.getMAIL_ID()));

                mailler.add(mail);
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
            return mailler;
        }
    }
    
    public List<MAIL_ADRES> getirMailAdresler(int mailId) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT * "
                     + " FROM MAIL_ADRES "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     + " AND MAIL_ID = ? " ;
        
        preparedStatement = connection.prepareStatement(query);
        List<MAIL_ADRES> mailAdresler = new ArrayList<>();
        
        try 
        {            
            preparedStatement.setInt(1, mailId);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {
                MAIL_ADRES mailAdres = new MAIL_ADRES();                          
                mailAdres.setMAIL_ADRESI(resultSet.getString("MAIL_ADRESI")); 
                mailAdres.setMAIL_ADRES_ID(resultSet.getInt("MAIL_ADRES_ID"));
                mailAdres.setMUSTERI_RECORD_ID(resultSet.getString("MUSTERI_RECORD_ID")); 
                mailAdres.setTAMAMLANDI_MI(resultSet.getInt("TAMAMLANDI_MI"));
                if(mailAdres.getTAMAMLANDI_MI() > 0)
                    mailAdres.setTamamlandiMiIsim("TAMAMLANDI");
                else
                    mailAdres.setTamamlandiMiIsim("TAMAMLANMADI");
                
                mailAdres.setMusteriUnvan(getirMusteriRecorIdIle(mailAdres.getMUSTERI_RECORD_ID()).getMUSTERI_UNVANI());

                mailAdresler.add(mailAdres);
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
            return mailAdresler;
        }
    }
    
     
    public void kaydetMailSatinAlma(String gonderen, 
                                    String konu,
                                    String icerik,
                                    int teklifBaslikId,
                                    List<MailListSatinAlma> mailAdresleri,
                                    int olusturanId) throws Exception
    {
        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        
        String query =  "INSERT INTO " + dbConnection.getDbName() + ".[dbo].[MAIL] " +
                        " ( " +
                        " MAIL_GONDEREN " +
                        " ,MAIL_KONU " +
                        " ,MAIL_ICERIK " +
                        " ,TEKLIF_BASLIK_ID " +
                        " ,KULLANIM_DURUMU " +
                        " ,OLUSTURMA_TARIHI " +
                        " ,MAIL_GONDERIM_TARIHI " +
                        " ,OLUSTURAN_ID " +
                        " ) " +
                        " VALUES(?,?,?,?,?,?,?,?)" ;
              try 
                {            
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, gonderen);
                    preparedStatement.setString(2, konu);
                    preparedStatement.setString(3, icerik);
                    preparedStatement.setInt(4, teklifBaslikId);
                    preparedStatement.setInt(5, 1);
                    preparedStatement.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
                    preparedStatement.setTimestamp(7, new java.sql.Timestamp(new java.util.Date().getTime()));
                    preparedStatement.setInt(8, olusturanId);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    
                    //////////////////////////////////////////////////////////////
                    
                    String query2 = " SELECT "
                                    + " MAX(MAIL_ID) AS MAIL_ID "
                                    + " FROM MAIL "
                                    + " WHERE KULLANIM_DURUMU = 1 " 
                                    + " AND TEKLIF_BASLIK_ID = ?";
                    
                    preparedStatement2 = connection.prepareStatement(query2);
                    preparedStatement2.setInt(1, teklifBaslikId);
                    ResultSet resultSet = preparedStatement2.executeQuery();
                    resultSet.next();
                    int mailId = resultSet.getInt("MAIL_ID");
                    preparedStatement2.close();
                    
                    //////////////////////////////////////////////////////////////
                    
                    String query3 = "INSERT INTO " + dbConnection.getDbName() + ".[dbo].[MAIL_ADRES] " +
                                    " ( " +
                                    "  MAIL_ADRESI " +
                                    " ,MAIL_ID " +
                                    " ,MUSTERI_RECORD_ID " +
                                    " ,KULLANIM_DURUMU " +
                                    " ,OLUSTURMA_TARIHI " +
                                    " ,OLUSTURAN_ID " +
                                    " ) " +
                                    " VALUES(?,?,?,?,?,?)" ;
                    
                    preparedStatement3 = connection.prepareStatement(query3);
                    preparedStatement3.setInt(2, mailId);
                    preparedStatement3.setInt(4, 1);
                    preparedStatement3.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
                    preparedStatement3.setInt(6, olusturanId);
                    
                    for(MailListSatinAlma mailListSatinAlma : mailAdresleri)
                    {
                        preparedStatement3.setString(1, mailListSatinAlma.getMailAdres());
                        preparedStatement3.setString(3, mailListSatinAlma.getMusteriRecordId());
                        preparedStatement3.executeUpdate();
                    }
                    
                    preparedStatement3.close();
                    
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
    
    public void kaydetTeklifSatirYanit(List<MAIL_ADRES> mailAdresleri,
                                       List<TEKLIF_SATIR> teklifSatirlari,
                                       int olusturanId,
                                       String ERPFirmaNo) throws Exception
    {
        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query =  "INSERT INTO " + dbConnection.getDbName() + ".[dbo].[TEKLIF_SATIR_YANIT] " +
                        " ( " +
                        " MAIL_ADRES_ID " +
                        " ,TEKLIF_SATIR_ID " +
                        " ,MIKTAR " +
                        " ,STOKTA_VAR_YOK " +                
                        " ,SONMALIYET " +
                        " ,BIRIM " +
                        " ,KULLANIM_DURUMU " +
                        " ,OLUSTURMA_TARIHI " +
                        " ,OLUSTURAN_ID " +
                        " ) " +
                        " VALUES(?,?,?,?,?,?,?,?,?)" ;
              try 
                {            
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(7, 1);
                    preparedStatement.setTimestamp(8, new java.sql.Timestamp(new java.util.Date().getTime()));
                    preparedStatement.setInt(9, olusturanId);
                    
                    for(MAIL_ADRES mailAdres : mailAdresleri)
                    {
                        for(TEKLIF_SATIR teklifSatir : teklifSatirlari)
                        {
                            preparedStatement.setInt(1, mailAdres.getMAIL_ADRES_ID());
                            preparedStatement.setInt(2, teklifSatir.getTEKLIF_SATIR_ID());
                            preparedStatement.setDouble(3, teklifSatir.getMALZEME_HIZMET_MASRAF_MIKTARI());
                            preparedStatement.setInt(4, 1);
                            preparedStatement.setDouble(5, getirSonMaliyet(mailAdres.getMUSTERI_RECORD_ID()
                                                                        ,teklifSatir.getMALZEME_HIZMET_MASRAF_KODU()
                                                                        ,ERPFirmaNo));
                            preparedStatement.setString(6, teklifSatir.getMALZEME_HIZMET_MASRAF_BIRIM_KODU());
                            preparedStatement.executeUpdate();
                        }
                    }
                    
                    preparedStatement.close();
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
    
    public double getirSonMaliyet(String musteriRecordId, String malzemeKodu, String ERPFirmaNo) throws Exception
    {
        if(malzemeKodu == null)
            return new Double(0);
        
        String ERPFirmaNoTemp = "";
        
        if(ERPFirmaNo.length() == 1)
                ERPFirmaNoTemp = "00" + ERPFirmaNo;
            else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        DbConnection dbConnectionERP = new DbConnection();
        Connection connectionERP = dbConnectionERP.baglantiAcERP();
        
        PreparedStatement preparedStatement = null;
        
        String query =  " SELECT TOP 1 PRICE " +
                        " FROM LG_" + ERPFirmaNoTemp + "_PURCHOFFERLN LN " +
                        " LEFT JOIN LG_" + ERPFirmaNoTemp + "_PURCHOFFER FC ON LN.ORDFICHEREF = FC.LOGICALREF " +
                        " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_ITEMS ITM ON ITM.LOGICALREF = LN.STOCKREF " +
                        " WHERE " +
                        "   ITM.CODE = '" + malzemeKodu + "' AND " +
                        "   FC.CLIENTREF = " + musteriRecordId + " AND " +
                        "   FC.TRCODE = 2 " +
                        " ORDER BY LN.DATE_ DESC ";
                
                
                
        preparedStatement = connectionERP.prepareStatement(query);
        double sonMaliyet = new Double(0);
        
        try 
        {            
            //preparedStatement.setString(1, musteriRecordId);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {
              sonMaliyet = resultSet.getDouble("PRICE");
              //sonMaliyet = resultSet.getDouble("SonMaliyet");
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
            return sonMaliyet;
        }
    }
    
    
    public int getirMailAdresId(String musteriRecordId) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT MAX(MAIL_ADRES_ID) AS MAIL_ADRES_ID "
                     + " FROM MAIL_ADRES "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     + " AND MUSTERI_RECORD_ID = ? " ;
        
        preparedStatement = connection.prepareStatement(query);
        int mailAdresId = 0;
        
            try 
            {            
                preparedStatement.setString(1, musteriRecordId);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                mailAdresId = resultSet.getInt("MAIL_ADRES_ID");
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
                return mailAdresId;
            }
    }
    
    public int getirMailIdTeklifBaslikIdIle(int teklifBaslikId) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT MAX(MAIL_ID) AS MAIL_ID "
                     + " FROM MAIL "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     + " AND TEKLIF_BASLIK_ID = ? " ;
        
        preparedStatement = connection.prepareStatement(query);
        int mailId = 0;
        
            try 
            {            
                preparedStatement.setInt(1, teklifBaslikId);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                mailId = resultSet.getInt("MAIL_ID");
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
                return mailId;
            }
    }
    
    public int getirBirimIdBirimIle(String ERPFirmaNo, String birim) throws Exception
    {
        String ERPFirmaNoTemp = "";
        
        if(ERPFirmaNo.length() == 1)
                ERPFirmaNoTemp = "00" + ERPFirmaNo;
            else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        DbConnection dbConnectionERP = new DbConnection();
        Connection connectionERP = dbConnectionERP.baglantiAcERP();
        
        PreparedStatement preparedStatement = null;
        
        String query =  " SELECT UNL.LOGICALREF " +
                        " FROM LG_" + ERPFirmaNoTemp + "_UNITSETL UNL " +
                        " INNER JOIN LG_" + ERPFirmaNoTemp + "_UNITSETF UNF ON UNL.UNITSETREF = UNF.LOGICALREF " +
                        " WHERE UNL.CODE + ' (' + UNF.CODE + ')' = '" + birim + "'" ;
        
        preparedStatement = connectionERP.prepareStatement(query);
        int birimId = 0;
        
            try 
            {            
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                birimId = resultSet.getInt("LOGICALREF");
            }            
            catch (Exception ex) 
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Birim getirilirken hata oluştu", ex.toString()));
            }
            finally
            {
                dbConnectionERP.baglantiKapatERP();
                if(preparedStatement!=null)
                { 
                  preparedStatement.close();
                }     
                return birimId;
            }
    }
    
    public TEKLIF_BASLIK getirTeklifBaslikSatinAlma(int mailAdresId) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        PreparedStatement preparedStatement = null;
        
        String query2 = " SELECT MAIL_ID, MUSTERI_RECORD_ID, PARA_BIRIMI "
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
            if(resultSet2.getString("PARA_BIRIMI") != null)
                if(!resultSet2.getString("PARA_BIRIMI").isEmpty())
                    teklifBaslik.setSatinAlmaTedarikciParaBirimi(resultSet2.getString("PARA_BIRIMI")); 
                    //null değil ve boş da değilse atama yap
                else
                    teklifBaslik.setSatinAlmaTedarikciParaBirimi("TL"); //Eğer boş kalmışsa (eski kayıtlar için)
            else                                                            //Otomatik olarak TL yap...
                teklifBaslik.setSatinAlmaTedarikciParaBirimi("TL");//Eğer null ise TL atansın...
            
            ResultSet resultSet3 = preparedStatement3.executeQuery();
            resultSet3.next();
            
            preparedStatement.setInt(1, resultSet3.getInt("TEKLIF_BASLIK_ID"));
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
                teklifBaslik.setMusteriKodu(musteri.getMUSTERI_KODU());
                
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
                teklifSatirYanit.setSTOKTA_VAR_YOK(resultSet.getInt("STOKTA_VAR_YOK"));
                teklifSatirYanit.setMIKTAR(resultSet.getDouble("MIKTAR"));
                teklifSatirYanit.setBIRIM(resultSet.getString("BIRIM"));
                
                if(teklifSatirYanit.getSTOKTA_VAR_YOK()==0)
                    teklifSatirYanit.setStoktaVarYokBoolean(false);
                else
                    teklifSatirYanit.setStoktaVarYokBoolean(true);
                ////////////////////////////////////////////////////////////////////////////////////
                //Teklif satırındaki bilgilerin setleme işlemleri yapılmakta...
                preparedStatement2.setInt(1, teklifSatirYanit.getTEKLIF_SATIR_ID());
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                resultSet2.next();
                teklifSatirYanit.setMalzemeHizmetMasrafKodu(resultSet2.getString("MALZEME_HIZMET_MASRAF_KODU"));
                teklifSatirYanit.setMalzemeHizmetMasrafAdi(resultSet2.getString("MALZEME_HIZMET_MASRAF_ADI"));
                teklifSatirYanit.setMalzemeHizmetMasrafAciklamasi(resultSet2.getString("MALZEME_HIZMET_MASRAF_ACIKLAMASI"));
                teklifSatirYanit.setMalzemeHizmetMasrafBirimKodu(resultSet2.getString("MALZEME_HIZMET_MASRAF_BIRIM_KODU"));
                teklifSatirYanit.setMalzemeHizmetMasrafMiktari(resultSet2.getDouble("MALZEME_HIZMET_MASRAF_MIKTARI"));
                teklifSatirYanit.setMalzemeHizmetMasrafRecordId(resultSet2.getString("RECORD_ID"));
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
    
    public void veriGirisineAcMailAdresIdIle(int mailAdresId) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " UPDATE  "
                     + " MAIL_ADRES "
                     + " SET TAMAMLANDI_MI = 0 "
                     + " WHERE MAIL_ADRES_ID = ? " ;
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, mailAdresId);
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

}
