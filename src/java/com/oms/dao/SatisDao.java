/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.dao;

import com.oms.models.ERPItem;
import com.oms.models.EskiYeni;
import com.oms.models.EskiYeniList;
import com.oms.models.FiiliStokSatirBazinda;
import com.oms.models.MAIL_ADRES;
import com.oms.models.MUSTERI;
import com.oms.models.SATISTAN_TEDARIKCI_KAYITLARI;
import com.oms.models.SonTeklifVeSatinAlmaSatisSatiri;
import com.oms.models.TEKLIF_BASLIK;
import com.oms.models.TEKLIF_SATIR;
import com.oms.models.TanimliSatisFiyati;
import com.oms.models.TedarikciListSatisSatirBazinda;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
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
public class SatisDao {
    
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
    
    public String getirEnBuyukTeklifBaslikRecordId(String ERPFirmaNo) throws Exception
    {
        String enBuyukTeklifBaslikRecordId = "";
        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT "
                     + " MAX(RECORD_ID) AS RECORD_ID "
                     + " FROM TEKLIF_BASLIK "
                     //+ " WHERE KULLANIM_DURUMU = 1 " //Gizlenenleri de dikkate almak için kaldırdık...
                     //+ " AND SATIS_SATINALMA = 0 " // Satışsa 0, satın alma ise 1 olacak...
                     + " WHERE SATIS_SATINALMA = 0 "
                     + " AND ERP_FIRMA_NUMBER = '" + ERPFirmaNo + "'"
                     + " AND LEN(RECORD_ID) <> 36 ";
        
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
    
    
    public String kaydetTeklifBaslikManuel(TEKLIF_BASLIK teklifBaslik, 
                                           int olusturanId, 
                                           String ERPFirmaNo,
                                           int firmaId,
                                           String teklifEkleyen) throws Exception
    {        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query =  "INSERT INTO [TEKLIF_BASLIK] " +
                        " ( " +
                        " TEKLIF_NUMARASI " +
                        " ,TEKLIF_TARIHI " +
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
                        " VALUES(?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?" +
                               " )" ;
        
                UUID uuid = UUID.randomUUID();
                String randomUUIDString = uuid.toString();
        
              try 
                {            
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, teklifBaslik.getTEKLIF_NUMARASI());
                    preparedStatement.setDate(2, new java.sql.Date(teklifBaslik.getTEKLIF_TARIHI().getTime()));
                    preparedStatement.setString(3, "1");
                    preparedStatement.setString(4, "1");
                    preparedStatement.setDate(5, new java.sql.Date(teklifBaslik.getTEKLIF_BASLANGIC_TARIHI().getTime()));
                    preparedStatement.setDate(6, new java.sql.Date(teklifBaslik.getTEKLIF_BITIS_TARIHI().getTime()));
                    preparedStatement.setInt(7, 1);
                    preparedStatement.setString(8, "ONAY BEKLİYOR");
                    preparedStatement.setString(9, teklifBaslik.getTEKLIF_ACIKLAMA1());
                    preparedStatement.setString(10, teklifBaslik.getTEKLIF_ACIKLAMA2());
                    preparedStatement.setString(11,teklifBaslik.getTEKLIF_ACIKLAMA3());
                    preparedStatement.setString(12, teklifEkleyen);
                    if(teklifBaslik.getPARA_BIRIMI().equals("TL"))
                        preparedStatement.setInt(13, 0);
                    else if(teklifBaslik.getPARA_BIRIMI().equals("USD"))
                        preparedStatement.setInt(13, 1);
                    else if(teklifBaslik.getPARA_BIRIMI().equals("EUR"))
                        preparedStatement.setInt(13, 20);
                    preparedStatement.setDouble(14, teklifBaslik.getTEKLIF_KUR());                    
                    preparedStatement.setString(15, randomUUIDString);
                    MUSTERI musteri = getirMusteriMusteriKodIle(
                            teklifBaslik.getMUSTERI_RECORD_ID().substring(0 , teklifBaslik.getMUSTERI_RECORD_ID().indexOf("___")));
                    preparedStatement.setString(16, musteri.getRECORD_ID());
                    preparedStatement.setString(17, randomUUIDString);
                    preparedStatement.setInt(18, olusturanId);
                    preparedStatement.setTimestamp(19, new java.sql.Timestamp(new java.util.Date().getTime()));
                    preparedStatement.setInt(20, 1);
                    preparedStatement.setInt(21, firmaId);
                    preparedStatement.setString(22, ERPFirmaNo);
                    preparedStatement.setInt(23, 0);
                    preparedStatement.setString(24, teklifBaslik.getBELGE_NO());
                    preparedStatement.executeUpdate();
                    return randomUUIDString;
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
                    return randomUUIDString;
                }        
    }   
    
    public String revizeEtTeklifBaslik(TEKLIF_BASLIK teklifBaslik, 
                                       int olusturanId, 
                                       String ERPFirmaNo,
                                       int firmaId,
                                       String teklifEkleyen) throws Exception
    {        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query =  "INSERT INTO [TEKLIF_BASLIK] " +
                        " ( " +
                        " TEKLIF_NUMARASI " +
                        " ,TEKLIF_TARIHI " +
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
                        " ,TEKLIF_TUTARI " +
                        " ,MUSTERI_MARJ " +
                        " ,MUSTERI_YANIT " +
                        " ) " +
                        " VALUES(?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?," +
                               " ?" +
                               " )" ;
        
                UUID uuid = UUID.randomUUID();
                String randomUUIDString = uuid.toString();
        
              try 
                {            
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, teklifBaslik.getTEKLIF_NUMARASI());
                    preparedStatement.setDate(2, new java.sql.Date(teklifBaslik.getTEKLIF_TARIHI().getTime()));
                    preparedStatement.setString(3, teklifBaslik.getGECERLI_TEKLIF_REVIZYON_NUMARASI());
                    preparedStatement.setString(4, "1");
                    preparedStatement.setDate(5, new java.sql.Date(teklifBaslik.getTEKLIF_BASLANGIC_TARIHI().getTime()));
                    preparedStatement.setDate(6, new java.sql.Date(teklifBaslik.getTEKLIF_BITIS_TARIHI().getTime()));
                    preparedStatement.setInt(7, teklifBaslik.getTEKLIF_DURUM_KODU());
                    preparedStatement.setString(8, teklifBaslik.getTEKLIF_DURUM_KODU_ACIKLAMA());
                    preparedStatement.setString(9, teklifBaslik.getTEKLIF_ACIKLAMA1());
                    preparedStatement.setString(10, teklifBaslik.getTEKLIF_ACIKLAMA2());
                    preparedStatement.setString(11,teklifBaslik.getTEKLIF_ACIKLAMA3());
                    preparedStatement.setString(12, teklifEkleyen);
                    preparedStatement.setInt(13, teklifBaslik.getTEKLIF_PARA_BIRIMI());
                    preparedStatement.setDouble(14, teklifBaslik.getTEKLIF_KUR());                    
                    preparedStatement.setString(15, randomUUIDString);
                    preparedStatement.setString(16, teklifBaslik.getMUSTERI_RECORD_ID());
                    preparedStatement.setString(17, randomUUIDString);
                    preparedStatement.setInt(18, olusturanId);
                    preparedStatement.setTimestamp(19, new java.sql.Timestamp(new java.util.Date().getTime()));
                    preparedStatement.setInt(20, 1);
                    preparedStatement.setInt(21, firmaId);
                    preparedStatement.setString(22, ERPFirmaNo);
                    preparedStatement.setInt(23, 0);
                    preparedStatement.setString(24, teklifBaslik.getBELGE_NO());
                    preparedStatement.setDouble(25, teklifBaslik.getTEKLIF_TUTARI());
                    preparedStatement.setDouble(26, teklifBaslik.getMUSTERI_MARJ());
                    preparedStatement.setString(27, teklifBaslik.getMUSTERI_YANIT());
                    preparedStatement.executeUpdate();
                    return randomUUIDString;
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
                    return randomUUIDString;
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
                        " ,PROJE_KODU " +
                        " ,SATIS_ELEMANI " +
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
                            " 0 AS SATIS_SATINALMA, " + // 0'SA SATIS 1'SE SATIN ALMA
                            " PUR.DOCODE AS BELGE_NO, " +
                            " PRJ.CODE + ' ' + PRJ.NAME AS PROJE_KODU, " +
                            " SLS.DEFINITION_ AS SATIS_ELEMANI " +
                        " FROM LG_" + ERPFirmaNoTemp + "_PURCHOFFER PUR " + 
                        " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_CLCARD CLC ON CLC.LOGICALREF = PUR.CLIENTREF " +
                        " LEFT OUTER JOIN LG_SLSACTIV SLSAC WITH(NOLOCK) ON (PUR.SLSACTREF  =  SLSAC.LOGICALREF) " +
                        " LEFT OUTER JOIN LG_CSTVND CSTVND  WITH(NOLOCK) ON (CSTVND .LOGICALREF  =  SLSAC.CSTVNDREF) " +
                        " LEFT OUTER JOIN L_CAPIUSER USR ON USR.NR = PUR.CAPIBLOCK_CREATEDBY " +
                        " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_PROJECT PRJ ON PRJ.LOGICALREF = PUR.PROJECTREF " +
                        " LEFT OUTER JOIN LG_SLSMAN SLS ON SLS.LOGICALREF = PUR.SALESMANREF " +
                        " WHERE PUR.TRCODE = 1 " + 
                          " AND PUR.CANCELLED = 0 " + 
                          " AND PUR.TYP = 1 " +
                          " AND PUR.STATUS = 1 " + //Sadece Onay bekleyenleri çek demek...
                          //"";
                        " AND (PUR.DATE_ >= DATEADD(DAY, -10, GETDATE())) " ; //Son bir aylık verileri çek demek...
                
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
                                        String enBuyukTeklifBaslikRecordId ) throws Exception
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
                        " ,MUSTERI_SATIS_MIKTAR_YANIT " +
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
                        " ,ODEME_SEKLI_KOD" +
                        " ,ODEME_SEKLI" +
                        " ,KDV_DAHIL_HARIC_KOD" +
                        " ,KDV_DAHIL_HARIC" +
                        " ) " +
                        " SELECT DISTINCT " + 
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
                        " POFFERLN.AMOUNT AS MUSTERI_SATIS_MIKTAR_YANIT, " +
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
                        " POFFERLN.ORGDUEDATE AS TESLIM_TARIHI, " +
                        " PAY.CODE AS ODEME_SEKLI_KOD, " +
                        " ISNULL(PAY.DEFINITION_,'') AS ODEME_SEKLI, " +
                        " POFFERLN.VATINC AS KDV_DAHIL_HARIC_KOD, " +
                                " CASE POFFERLN.VATINC " + 
                                " WHEN 0 THEN 'HARİÇ' " + 
                                " WHEN 1 THEN 'DAHİL' " + 
                                " ELSE '' " + 
                                " END AS KDV_DAHIL_HARIC " +
                    " FROM LG_" + ERPFirmaNoTemp + "_PURCHOFFER PUR " +
                    " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_PURCHOFFERLN POFFERLN ON POFFERLN.ORDFICHEREF = PUR.LOGICALREF " + 
                                " AND POFFERLN.FCTYP = 1 " +
                    " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_ITEMS ITM ON ITM.LOGICALREF = POFFERLN.STOCKREF " + 
                                " AND POFFERLN.LINETYPE IN (0,8,9) " +
                    " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_SRVCARD SRV ON SRV.LOGICALREF = POFFERLN.STOCKREF " + 
                                " AND POFFERLN.LINETYPE = 4 " +
                    " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_ITMUNITA ITMU ON ITMU.ITEMREF = ITM.LOGICALREF " +
                                " AND POFFERLN.UOMREF = ITMU.UNITLINEREF " +
                    " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_UNITSETL UNL ON UNL.LOGICALREF = ITMU.UNITLINEREF " + 
                    " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_UNITSETF UNF ON UNF.LOGICALREF = UNL.UNITSETREF  " + 
                    " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_PAYPLANS PAY ON POFFERLN.PAYDEFREF = PAY.LOGICALREF " +    
                    " WHERE PUR.TRCODE = 1 " + //Satış olduğu için 1 oldu.
                      " AND PUR.CANCELLED = 0 " +
                      " AND PUR.TYP = 1 " +
                      " AND PUR.STATUS = 1 " + //Sadece Onay bekleyenleri çek demek...
                      " AND POFFERLN.PURCHOFFNR IN (SELECT MAX(PURCHOFFNR) FROM LG_" + ERPFirmaNoTemp + "_PURCHOFFERLN WHERE ORDFICHEREF = PUR.LOGICALREF) " +
                      " AND (PUR.DATE_ >= DATEADD(DAY, -10, GETDATE())) " + //Son bir aylık verileri çek demek...
                      " AND PUR.LOGICALREF IN (SELECT RECORD_ID FROM " 
                        + dbConnection.getDbName() + ".[dbo].[TEKLIF_BASLIK] WHERE SATIS_SATINALMA = 0 AND LEN(RECORD_ID) <> 36";
                    
                    //Yani İlk defa ERP ile temasa geçiliyorsa hesini çek demek...
                    if (enBuyukTeklifBaslikRecordId != "" )
                    {
                      query = query + " AND RECORD_ID > '" + enBuyukTeklifBaslikRecordId + "'" ;
                    }
                    
                    query = query +  " ) "  ;

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
    
    public void kaydetTeklifSatirManuel(String teklifBaslikGuid,
                                        TEKLIF_SATIR teklifSatir, 
                                        int olusturanId, 
                                        String ERPFirmaNo,
                                        String teklifEkleyen) throws Exception
    {
            DbConnection dbConnection = new DbConnection();
            Connection connection = dbConnection.baglantiAc();

            PreparedStatement preparedStatement = null;

            String query =  "INSERT INTO [TEKLIF_SATIR] " +
                            " ( " +
                            " MALZEME_HIZMET_MASRAF_KODU " +
                            " ,MALZEME_HIZMET_MASRAF_ADI " +
                            " ,MALZEME_HIZMET_MASRAF_ADI2 " +
                            " ,MALZEME_HIZMET_MASRAF_ACIKLAMASI " +
                            " ,MALZEME_HIZMET_MASRAF_BIRIM_KODU " +
                            " ,MALZEME_HIZMET_MASRAF_MIKTARI " +     
                            " ,TEKLIF_BASLIK_RECORD_ID " +
                            " ,RECORD_ID " +
                            " ,OLUSTURAN_ID " +
                            " ,OLUSTURMA_TARIHI " +
                            " ,KULLANIM_DURUMU " +
                            " ,ERP_FIRMA_NUMBER " +
                            " ,PARA_BIRIMI " +
                            " ,KUR " +
                            " ,MUSTERI_SATIS_MIKTAR_YANIT " +
                            " ) " +
                            " VALUES(?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?" +
                                   " )" ;

                UUID uuid = UUID.randomUUID();
                String randomUUIDString = uuid.toString();
            
            try 
            { 
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, teklifSatir.getMALZEME_HIZMET_MASRAF_KODU());
                preparedStatement.setString(2, teklifSatir.getMALZEME_HIZMET_MASRAF_ADI());
                preparedStatement.setString(3, teklifSatir.getMALZEME_HIZMET_MASRAF_ADI2());
                preparedStatement.setString(4, teklifSatir.getMALZEME_HIZMET_MASRAF_ACIKLAMASI());
                
                String birimAilesi = getirBirimAilesiKodIle(teklifSatir.getMALZEME_HIZMET_MASRAF_KODU(),ERPFirmaNo);
                birimAilesi = " (" + birimAilesi + ")";
                
                preparedStatement.setString(5, teklifSatir.getBirimIsim() + birimAilesi);
                preparedStatement.setDouble(6, teklifSatir.getMALZEME_HIZMET_MASRAF_MIKTARI());
                preparedStatement.setString(7, teklifBaslikGuid);
                preparedStatement.setString(8, randomUUIDString);
                preparedStatement.setInt(9, olusturanId);
                preparedStatement.setTimestamp(10, new java.sql.Timestamp(new java.util.Date().getTime()));
                preparedStatement.setInt(11, 1);
                preparedStatement.setString(12, ERPFirmaNo);                
                if(teklifSatir.getParaBirimiIsim().equals("TL"))
                    preparedStatement.setInt(13, 0);
                else if(teklifSatir.getParaBirimiIsim().equals("USD"))
                    preparedStatement.setInt(13, 1);
                else if(teklifSatir.getParaBirimiIsim().equals("EUR"))
                    preparedStatement.setInt(13, 20);
                
                if(teklifSatir.getKUR() <= 0.0)
                    teklifSatir.setKUR(1.0);
                
                preparedStatement.setDouble(14, teklifSatir.getKUR());
                
                preparedStatement.setDouble(15, teklifSatir.getMALZEME_HIZMET_MASRAF_MIKTARI());
                
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
    
    public int revizeEtTeklifSatir(String teklifBaslikGuid,
                                    TEKLIF_SATIR teklifSatir, 
                                    int olusturanId, 
                                    String ERPFirmaNo,
                                    String teklifEkleyen) throws Exception
    {
            DbConnection dbConnection = new DbConnection();
            Connection connection = dbConnection.baglantiAc();
            
            int yeniTeklifSatirId = 0;

            PreparedStatement preparedStatement = null;
            PreparedStatement preparedStatement1 = null;

            String query =  "INSERT INTO [TEKLIF_SATIR] " +
                            " ( " +
                            " MALZEME_HIZMET_MASRAF_KODU " +
                            " ,MALZEME_HIZMET_MASRAF_ADI " +
                            " ,MALZEME_HIZMET_MASRAF_ADI2 " +
                            " ,MALZEME_HIZMET_MASRAF_ACIKLAMASI " +
                            " ,MALZEME_HIZMET_MASRAF_BIRIM_KODU " +
                            " ,MALZEME_HIZMET_MASRAF_MIKTARI " +     
                            " ,TEKLIF_BASLIK_RECORD_ID " +
                            " ,RECORD_ID " +
                            " ,OLUSTURAN_ID " +
                            " ,OLUSTURMA_TARIHI " +
                            " ,KULLANIM_DURUMU " +
                            " ,ERP_FIRMA_NUMBER " +
                            " ,PARA_BIRIMI " +
                            " ,KUR " +
                            " ,MUSTERI_SATIS_MIKTAR_YANIT " +
                            " ,MALZEME_HIZMET_MASRAF_BIRIM_FIYATI " +
                            " ,MUSTERI_MARJ " +
                            " ,MALZEME_HIZMET_MASRAF_TUTARI "+
                            " ,SECILEN_TEDARIKCI_ID " +
                            " ) " +
                            " VALUES(?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?," +
                                   " ?" +
                                   " )" ;

                UUID uuid = UUID.randomUUID();
                String randomUUIDString = uuid.toString();
            
            try 
            { 
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, teklifSatir.getMALZEME_HIZMET_MASRAF_KODU());
                preparedStatement.setString(2, teklifSatir.getMALZEME_HIZMET_MASRAF_ADI());
                preparedStatement.setString(3, teklifSatir.getMALZEME_HIZMET_MASRAF_ADI2());
                preparedStatement.setString(4, teklifSatir.getMALZEME_HIZMET_MASRAF_ACIKLAMASI());
                
                String birimAilesi = getirBirimAilesiKodIle(teklifSatir.getMALZEME_HIZMET_MASRAF_KODU(),ERPFirmaNo);
                birimAilesi = " (" + birimAilesi + ")";
                
                preparedStatement.setString(5, teklifSatir.getMALZEME_HIZMET_MASRAF_BIRIM_KODU() + birimAilesi);
                preparedStatement.setDouble(6, teklifSatir.getMALZEME_HIZMET_MASRAF_MIKTARI());
                preparedStatement.setString(7, teklifBaslikGuid);
                preparedStatement.setString(8, randomUUIDString);
                preparedStatement.setInt(9, olusturanId);
                preparedStatement.setTimestamp(10, new java.sql.Timestamp(new java.util.Date().getTime()));
                preparedStatement.setInt(11, 1);
                preparedStatement.setString(12, ERPFirmaNo);                
                if(teklifSatir.getParaBirimiIsim().equals("TL"))
                    preparedStatement.setInt(13, 0);
                else if(teklifSatir.getParaBirimiIsim().equals("USD"))
                    preparedStatement.setInt(13, 1);
                else if(teklifSatir.getParaBirimiIsim().equals("EUR"))
                    preparedStatement.setInt(13, 20);
                
                if(teklifSatir.getKUR() <= 0.0)
                    teklifSatir.setKUR(1.0);
                
                preparedStatement.setDouble(14, teklifSatir.getKUR());
                //Eskiden kalan satırlarda eski veriyi kullanacağız...
                //Yeni eklenen satırlarda müşteri miktar yanıt alanına girilen miktar bilgisini yazacağız...
                if(teklifSatir.getTEKLIF_SATIR_ID() > 0)
                    preparedStatement.setDouble(15, teklifSatir.getMUSTERI_SATIS_MIKTAR_YANIT());
                else
                    preparedStatement.setDouble(15, teklifSatir.getMALZEME_HIZMET_MASRAF_MIKTARI());
                
                preparedStatement.setDouble(16, teklifSatir.getMALZEME_HIZMET_MASRAF_BIRIM_FIYATI());
                preparedStatement.setDouble(17, teklifSatir.getMUSTERI_MARJ()); 
                preparedStatement.setDouble(18, teklifSatir.getMALZEME_HIZMET_MASRAF_TUTARI());
                preparedStatement.setInt(19, teklifSatir.getSECILEN_TEDARIKCI_ID());
                
                preparedStatement.executeUpdate();    
                
                //////////////////////////////////////////////////////////////
                    
                String query2 = " SELECT "
                              + " TEKLIF_SATIR_ID "
                              + " FROM TEKLIF_SATIR "
                              + " WHERE RECORD_ID = ? ";

                preparedStatement1 = connection.prepareStatement(query2);
                preparedStatement1.setString(1, randomUUIDString);
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                resultSet1.next();

                yeniTeklifSatirId = resultSet1.getInt("TEKLIF_SATIR_ID");

                preparedStatement1.close();
                    
                //////////////////////////////////////////////////////////////   
                return yeniTeklifSatirId;
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
                
                if(preparedStatement1!=null)
                { 
                  preparedStatement1.close();
                }
                
                return yeniTeklifSatirId;
            }      
    }
    
    public String getirBirimAilesiKodIle(String kod, String ERPFirmaNo) throws Exception
    {
        String birimAilesi = "";
        
        DbConnection dbConnection = new DbConnection();
        Connection connectionERP = dbConnection.baglantiAcERP();
        PreparedStatement preparedStatementERP = null;
        
        try 
        {
            String ERPFirmaNoTemp = "";

            if(ERPFirmaNo.length() == 1)
                    ERPFirmaNoTemp = "00" + ERPFirmaNo;
                else if(ERPFirmaNo.length() == 2)
                ERPFirmaNoTemp = "0" + ERPFirmaNo;

            String queryERP =  " SELECT UNF.CODE  AS BIRIM_AILESI " +
                               " FROM " +
                               " LG_" + ERPFirmaNoTemp + "_ITEMS ITM " +
                               " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_ITMUNITA ITMU ON ITMU.ITEMREF = ITM.LOGICALREF " +           
                               " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_UNITSETL UNL ON UNL.LOGICALREF = ITMU.UNITLINEREF " +
                               " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_UNITSETF UNF ON UNF.LOGICALREF = UNL.UNITSETREF " +
                               " WHERE ITM.CODE = ?";
            
            preparedStatementERP = connectionERP.prepareStatement(queryERP);
            preparedStatementERP.setString(1, kod);
            
            ResultSet resultSet = preparedStatementERP.executeQuery();
            
            resultSet.next();
            birimAilesi = resultSet.getString("BIRIM_AILESI"); 
        }

        catch (Exception ex) 
        {
            System.out.println(ex);
        }

        finally
        {
            dbConnection.baglantiKapatERP();
            return birimAilesi;
        }      
    }
    
     public List<String> getirTumBirimler(String ERPFirmaNo) throws Exception
    {
        List<String> birimListesi = new ArrayList<>();
        
        DbConnection dbConnection = new DbConnection();
        Connection connectionERP = dbConnection.baglantiAcERP();
        PreparedStatement preparedStatementERP = null;
        
        try 
        {
            String ERPFirmaNoTemp = "";

            if(ERPFirmaNo.length() == 1)
                    ERPFirmaNoTemp = "00" + ERPFirmaNo;
                else if(ERPFirmaNo.length() == 2)
                ERPFirmaNoTemp = "0" + ERPFirmaNo;

            String queryERP = "SELECT DISTINCT UNI.CODE AS BIRIM FROM LG_" + ERPFirmaNoTemp + "_UNITSETL UNI";
            
            preparedStatementERP = connectionERP.prepareStatement(queryERP);
            
            ResultSet resultSet = preparedStatementERP.executeQuery();
            
            while(resultSet.next())
            {
              birimListesi.add(resultSet.getString("BIRIM"));  
            }
        }

        catch (Exception ex) 
        {
            System.out.println(ex);
        }

        finally
        {
            dbConnection.baglantiKapatERP();
            return birimListesi;
        }      
    }
    
    public void guncelleMailler(String ERPFirmaNo) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connectionERP = dbConnection.baglantiAcERP();
        PreparedStatement preparedStatementERP = null;
        
        try 
        {
            String ERPFirmaNoTemp = "";

            if(ERPFirmaNo.length() == 1)
                    ERPFirmaNoTemp = "00" + ERPFirmaNo;
                else if(ERPFirmaNo.length() == 2)
                ERPFirmaNoTemp = "0" + ERPFirmaNo;
            
            String queryERP =  " UPDATE " +
                                    " M " +
                               " SET " +
                                    " M.MUSTERI_MAIL_ADRESI1 = CLC.EMAILADDR, " +
                                    " M.MUSTERI_MAIL_ADRESI2 = CLC.EMAILADDR2 " +
                               " FROM " +
                                    " LG_" + ERPFirmaNoTemp + "_CLCARD CLC " +
                                    " INNER JOIN " + dbConnection.getDbName() + ".[dbo].[MUSTERI] M " +
                                    " ON M.RECORD_ID = CLC.LOGICALREF "; 
                               
            preparedStatementERP = connectionERP.prepareStatement(queryERP);            
            preparedStatementERP.executeUpdate();
            
        }

        catch (Exception ex) 
        {
            System.out.println(ex);
        }

        finally
        {
            if(preparedStatementERP!=null)
            { 
              preparedStatementERP.close();
            } 
            dbConnection.baglantiKapatERP();
        }      
        
    }
    
    public List<TEKLIF_BASLIK> getirTeklifBasliklari(String erpFirmaNumber) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT * "
                     + " FROM TEKLIF_BASLIK TB "
                     + " LEFT OUTER JOIN MUSTERI M ON TB.MUSTERI_RECORD_ID = M.RECORD_ID "
                                 + " AND M.KULLANIM_DURUMU = 1 "
                     + " WHERE TB.KULLANIM_DURUMU = 1 "
                     + " AND TB.SATIS_SATINALMA = 0 " //Satış olunca 0, satın alma olunca 1 olacak...
                     + " AND TB.ERP_FIRMA_NUMBER = ? " 
                     + " ORDER BY TB.TEKLIF_NUMARASI DESC, TB.TEKLIF_TARIHI DESC, TB.GECERLI_TEKLIF_REVIZYON_NUMARASI DESC";
        
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
                //Yeni eklendi. Sanal Kolon olarak geliyor ama gerçek kolon da olabilir.
                teklifBaslik.setTeklifTutariCarpiKur(
                        resultSet.getDouble("TEKLIF_TUTARI") * resultSet.getDouble("TEKLIF_KUR"));
                teklifBaslik.setMUSTERI_RECORD_ID(resultSet.getString("MUSTERI_RECORD_ID"));
                teklifBaslik.setTEKLIF_GUID(resultSet.getString("TEKLIF_GUID")); 
                teklifBaslik.setRECORD_ID(resultSet.getString("RECORD_ID"));
                teklifBaslik.setERP_FIRMA_NUMBER(resultSet.getString("ERP_FIRMA_NUMBER"));                
                teklifBaslik.setBELGE_NO(resultSet.getString("BELGE_NO")); 
                teklifBaslik.setPROJE_KODU(resultSet.getString("PROJE_KODU"));
                teklifBaslik.setSATIS_ELEMANI(resultSet.getString("SATIS_ELEMANI")); 
                teklifBaslik.setTEKLIF_ACIKLAMA1(resultSet.getString("TEKLIF_ACIKLAMA1"));
                teklifBaslik.setTEKLIF_ACIKLAMA2(resultSet.getString("TEKLIF_ACIKLAMA2"));
                teklifBaslik.setTEKLIF_ACIKLAMA3(resultSet.getString("TEKLIF_ACIKLAMA3"));
                teklifBaslik.setSORUMLU_PERSONEL(resultSet.getString("SORUMLU_PERSONEL"));                
                teklifBaslik.setMUSTERI_YANIT(resultSet.getString("MUSTERI_YANIT"));                
                teklifBaslik.setMusteriUnvani(resultSet.getString("MUSTERI_UNVANI"));
                teklifBaslik.setMusteriMail1(resultSet.getString("MUSTERI_MAIL_ADRESI1"));
                teklifBaslik.setMusteriMail2(resultSet.getString("MUSTERI_MAIL_ADRESI2"));    
                teklifBaslik.setMusteriKodu(resultSet.getString("MUSTERI_KODU"));
                teklifBaslik.setMUSTERI_MARJ(resultSet.getDouble("MUSTERI_MARJ"));
                
                //Başlangıçta öncelikli tedarikçilerin getirilmemesini istiyorum
                //Daha sonra isteyen true yaparak geitrebilir.
                teklifBaslik.setOncelikliTedarikcilerGelsinMi(false);
                
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
    
    public List<TEKLIF_SATIR> getirTeklifSatirlari(String teklifBaslikRecordId,
                                                   String ERPFirmaNumber,
                                                   String ERPFirmaNo) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT TS.* "
                     + " ,M.MUSTERI_KODU"
                     + " ,M.MUSTERI_UNVANI"
                     + " FROM TEKLIF_SATIR TS "
                     + " LEFT OUTER JOIN MUSTERI M ON M.MUSTERI_ID = TS.SECILEN_TEDARIKCI_ID "
                                 + " AND M.KULLANIM_DURUMU = 1"
                     + " WHERE TS.KULLANIM_DURUMU = 1 "
                     + " AND TS.TEKLIF_BASLIK_RECORD_ID = ? " 
                     + " AND TS.ERP_FIRMA_NUMBER = ? ";
        
        
        
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
                teklifSatir.setMALZEME_HIZMET_MASRAF_KODU(resultSet.getString("MALZEME_HIZMET_MASRAF_KODU")); 
                teklifSatir.setMALZEME_HIZMET_MASRAF_ADI(resultSet.getString("MALZEME_HIZMET_MASRAF_ADI"));
                teklifSatir.setMALZEME_HIZMET_MASRAF_ADI2(resultSet.getString("MALZEME_HIZMET_MASRAF_ADI2"));
                teklifSatir.setMALZEME_HIZMET_MASRAF_MIKTARI(resultSet.getDouble("MALZEME_HIZMET_MASRAF_MIKTARI"));
                teklifSatir.setMUSTERI_SATIS_MIKTAR_YANIT(resultSet.getDouble("MUSTERI_SATIS_MIKTAR_YANIT"));
                teklifSatir.setMALZEME_HIZMET_MASRAF_ACIKLAMASI(resultSet.getString("MALZEME_HIZMET_MASRAF_ACIKLAMASI")); 
                
                String prefix = "(";
                String noPrefixStr = resultSet.getString("MALZEME_HIZMET_MASRAF_BIRIM_KODU").
                                     substring(0,resultSet.getString("MALZEME_HIZMET_MASRAF_BIRIM_KODU").indexOf(prefix)-1);
                teklifSatir.setMALZEME_HIZMET_MASRAF_BIRIM_KODU(noPrefixStr);

                teklifSatir.setMALZEME_HIZMET_MASRAF_BIRIM_FIYATI(resultSet.getDouble("MALZEME_HIZMET_MASRAF_BIRIM_FIYATI"));
                teklifSatir.setMALZEME_HIZMET_MASRAF_TOPLAM_TUTARI(resultSet.getDouble("MALZEME_HIZMET_MASRAF_TOPLAM_TUTARI"));
                teklifSatir.setMALZEME_HIZMET_MASRAF_TUTARI(resultSet.getDouble("MALZEME_HIZMET_MASRAF_TUTARI"));
                teklifSatir.setMalzemeHizmetMasrafTutariCarpiKur(resultSet.getDouble("MALZEME_HIZMET_MASRAF_TUTARI") 
                                                                     * resultSet.getDouble("KUR"));
                teklifSatir.setPARA_BIRIMI(resultSet.getInt("PARA_BIRIMI"));
                teklifSatir.setKUR(resultSet.getDouble("KUR")); 
                teklifSatir.setTESLIM_TARIHI(resultSet.getTimestamp("TESLIM_TARIHI")); 
                
                teklifSatir.setSECILEN_TEDARIKCI_ID(resultSet.getInt("SECILEN_TEDARIKCI_ID"));
                teklifSatir.setSecilenTedarikciKodu(resultSet.getString("MUSTERI_KODU"));
                teklifSatir.setSecilenTedarikciUnvani(resultSet.getString("MUSTERI_UNVANI"));
                teklifSatir.setMUSTERI_MARJ(resultSet.getDouble("MUSTERI_MARJ"));
                
                teklifSatir.setMALIYET_BIRIM_FIYAT(resultSet.getDouble("MALIYET_BIRIM_FIYAT")); 
                                
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
    
    public TEKLIF_BASLIK getirTeklifBaslikRecordIdIle(String teklifBaslikRecordId) throws Exception
    {
        TEKLIF_BASLIK teklifBaslik = new TEKLIF_BASLIK();
        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT "
                     + " * "
                     + " FROM TEKLIF_BASLIK "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     + " AND RECORD_ID = ? ";
        
        try 
        {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, teklifBaslikRecordId);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {
                teklifBaslik.setTEKLIF_BASLIK_ID(resultSet.getInt("TEKLIF_BASLIK_ID"));  
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
    
    public MUSTERI getirMusteriMusteriKodIle(String musteriKod) throws Exception
    {
        MUSTERI musteri = new MUSTERI();
        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT "
                     + " * "
                     + " FROM MUSTERI "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     + " AND MUSTERI_KODU = ? ";
        
        try 
        {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, musteriKod);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                musteri.setRECORD_ID(resultSet.getString("RECORD_ID")); 
                musteri.setMUSTERI_ID(resultSet.getInt("MUSTERI_ID")); 
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
    
    public List<String> getirMusteriListUnvanVeyaKodIle(String unvanVeyaKod) throws Exception
    {
        List<String> musteriList = new ArrayList<>();
        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query =  " SELECT " +  
                        " * " +  
                        " FROM MUSTERI " +  
                        " WHERE KULLANIM_DURUMU = 1 " +  
                          " AND ((MUSTERI_KODU LIKE ?) OR (MUSTERI_UNVANI LIKE ?)) " +
                        " UNION ALL " +
                        " SELECT " + 
                        " * " +  
                        " FROM MUSTERI " +  
                        " WHERE KULLANIM_DURUMU = 1 " +  
                          " AND ( " +
                                   " (MUSTERI_KODU LIKE ? AND MUSTERI_KODU NOT LIKE ? AND MUSTERI_KODU NOT LIKE ?) " + 
                                        " OR " + 
                                   " (MUSTERI_UNVANI LIKE ? AND MUSTERI_UNVANI NOT LIKE ? AND MUSTERI_UNVANI NOT LIKE ?) " +
                              " ) " +
                        " UNION ALL " +
                        " SELECT " +  
                        " * " +  
                        " FROM MUSTERI " + 
                        " WHERE KULLANIM_DURUMU = 1 " +  
                          " AND ((MUSTERI_KODU LIKE ?) OR (MUSTERI_UNVANI LIKE ?)) ";
        
        try 
        {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, unvanVeyaKod + "%");
            preparedStatement.setString(2, unvanVeyaKod + "%");
            preparedStatement.setString(3, "%" + unvanVeyaKod + "%");
            preparedStatement.setString(4, unvanVeyaKod + "%");
            preparedStatement.setString(5, "%" + unvanVeyaKod);
            preparedStatement.setString(6, "%" + unvanVeyaKod + "%");
            preparedStatement.setString(7, unvanVeyaKod + "%");
            preparedStatement.setString(8, "%" + unvanVeyaKod);
            preparedStatement.setString(9, "%" + unvanVeyaKod);
            preparedStatement.setString(10, "%" + unvanVeyaKod);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                musteriList.add(resultSet.getString("MUSTERI_KODU") + "___" + resultSet.getString("MUSTERI_UNVANI"));
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
            return musteriList;
        }
    }
    
    
    
    public List<String> getirSatirListKodIle(String kod, String ERPFirmaNo) throws Exception
    {
        List<String> satirList = new ArrayList<>();
        
        DbConnection dbConnectionERP = new DbConnection();
        Connection connectionERP = dbConnectionERP.baglantiAcERP();
        
        PreparedStatement preparedStatementERP = null;
        
        String ERPFirmaNoTemp = "";

        if(ERPFirmaNo.length() == 1)
            ERPFirmaNoTemp = "00" + ERPFirmaNo;
        else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        String query = " SELECT "
                     + " CODE, NAME "
                     + " FROM LG_" + ERPFirmaNoTemp + "_ITEMS ITM "
                     + " WHERE ACTIVE = 0 "
                     + " AND (CODE LIKE ? )"
                     + " UNION ALL "
                     + " SELECT "
                     + " CODE, NAME "
                     + " FROM LG_" + ERPFirmaNoTemp + "_ITEMS ITM "
                     + " WHERE ACTIVE = 0 "
                     + " AND (CODE LIKE ? AND CODE NOT LIKE ? AND CODE NOT LIKE ?)"
                     + " UNION ALL "
                     + " SELECT "
                     + " CODE, NAME "
                     + " FROM LG_" + ERPFirmaNoTemp + "_ITEMS ITM "
                     + " WHERE ACTIVE = 0 "
                     + " AND (CODE LIKE ? )";
        
        try 
        {
            preparedStatementERP = connectionERP.prepareStatement(query);
            preparedStatementERP.setString(1, kod + "%");
            preparedStatementERP.setString(2, "%" + kod + "%");
            preparedStatementERP.setString(3, kod + "%");
            preparedStatementERP.setString(4, "%" + kod);
            preparedStatementERP.setString(5, "%" + kod);
            ResultSet resultSet = preparedStatementERP.executeQuery();
            while(resultSet.next())
            {                
                satirList.add(resultSet.getString("CODE") + "___" + resultSet.getString("NAME"));
            }   
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnectionERP.baglantiKapatERP();
            if(preparedStatementERP!=null)
            { 
              preparedStatementERP.close();
            }            
            return satirList;
        }
    }
    
    public boolean malzemeERPdeVarMi(String kod, String ERPFirmaNo) throws Exception
    {
        List<String> satirList = new ArrayList<>();
        boolean varMi = false;
        
        DbConnection dbConnectionERP = new DbConnection();
        Connection connectionERP = dbConnectionERP.baglantiAcERP();
        
        PreparedStatement preparedStatementERP = null;
        
        String ERPFirmaNoTemp = "";

        if(ERPFirmaNo.length() == 1)
            ERPFirmaNoTemp = "00" + ERPFirmaNo;
        else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        String query = " SELECT "
                     + " CODE "
                     + " FROM LG_" + ERPFirmaNoTemp + "_ITEMS ITM "
                     + " WHERE ACTIVE = 0 "
                     + " AND CODE = ? ";
        
        try 
        {
            preparedStatementERP = connectionERP.prepareStatement(query);
            preparedStatementERP.setString(1, kod );
            ResultSet resultSet = preparedStatementERP.executeQuery();
            while(resultSet.next())
            {                
                satirList.add(resultSet.getString("CODE"));
            }   
            
            if(satirList.size() > 0)
                varMi = true;
            else
                varMi = false;
            
            return varMi;
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
            return varMi;
        }
        finally
        {
            dbConnectionERP.baglantiKapatERP();
            if(preparedStatementERP!=null)
            { 
              preparedStatementERP.close();
            }            
            return varMi;
        }
    }
    
    public List<String> getirSatirListAdIle(String ad, String ERPFirmaNo) throws Exception
    {
        List<String> satirList = new ArrayList<>();
        
        DbConnection dbConnectionERP = new DbConnection();
        Connection connectionERP = dbConnectionERP.baglantiAcERP();
        
        PreparedStatement preparedStatementERP = null;
        
        String ERPFirmaNoTemp = "";

        if(ERPFirmaNo.length() == 1)
            ERPFirmaNoTemp = "00" + ERPFirmaNo;
        else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        
        String query = " SELECT "
                     + " CODE, NAME "
                     + " FROM LG_" + ERPFirmaNoTemp + "_ITEMS ITM "
                     + " WHERE ACTIVE = 0 "
                     + " AND (NAME LIKE ? )"
                     + " UNION ALL "
                     + " SELECT "
                     + " CODE, NAME "
                     + " FROM LG_" + ERPFirmaNoTemp + "_ITEMS ITM "
                     + " WHERE ACTIVE = 0 "
                     + " AND (NAME LIKE ? AND NAME NOT LIKE ? AND NAME NOT LIKE ?)"
                     + " UNION ALL "
                     + " SELECT "
                     + " CODE, NAME "
                     + " FROM LG_" + ERPFirmaNoTemp + "_ITEMS ITM "
                     + " WHERE ACTIVE = 0 "
                     + " AND (NAME LIKE ? )";
        
        try 
        {
            preparedStatementERP = connectionERP.prepareStatement(query);
            preparedStatementERP.setString(1, ad + "%");
            preparedStatementERP.setString(2, "%" + ad + "%");
            preparedStatementERP.setString(3, ad + "%");
            preparedStatementERP.setString(4, "%" + ad);
            preparedStatementERP.setString(5, "%" + ad);
            ResultSet resultSet = preparedStatementERP.executeQuery();
            while(resultSet.next())
            {                
                satirList.add(resultSet.getString("CODE") + "___" + resultSet.getString("NAME"));
            }   
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnectionERP.baglantiKapatERP();
            if(preparedStatementERP!=null)
            { 
              preparedStatementERP.close();
            }            
            return satirList;
        }
    }
    
    public List<String> getirSatirListAd2Ile(String ad2, String ERPFirmaNo) throws Exception
    {
        List<String> satirList = new ArrayList<>();
        
        DbConnection dbConnectionERP = new DbConnection();
        Connection connectionERP = dbConnectionERP.baglantiAcERP();
        
        PreparedStatement preparedStatementERP = null;
        
        String ERPFirmaNoTemp = "";

        if(ERPFirmaNo.length() == 1)
            ERPFirmaNoTemp = "00" + ERPFirmaNo;
        else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
                
        String query = " SELECT "
                     + " CODE, NAME3 "
                     + " FROM LG_" + ERPFirmaNoTemp + "_ITEMS ITM "
                     + " WHERE ACTIVE = 0 "
                     + " AND (NAME3 LIKE ? )"
                     + " UNION ALL "
                     + " SELECT "
                     + " CODE, NAME3 "
                     + " FROM LG_" + ERPFirmaNoTemp + "_ITEMS ITM "
                     + " WHERE ACTIVE = 0 "
                     + " AND (NAME3 LIKE ? AND NAME3 NOT LIKE ? AND NAME3 NOT LIKE ?)"
                     + " UNION ALL "
                     + " SELECT "
                     + " CODE, NAME3 "
                     + " FROM LG_" + ERPFirmaNoTemp + "_ITEMS ITM "
                     + " WHERE ACTIVE = 0 "
                     + " AND (NAME3 LIKE ? )";
        
        try 
        {
            preparedStatementERP = connectionERP.prepareStatement(query);
            preparedStatementERP.setString(1, ad2 + "%");
            preparedStatementERP.setString(2, "%" + ad2 + "%");
            preparedStatementERP.setString(3, ad2 + "%");
            preparedStatementERP.setString(4, "%" + ad2);
            preparedStatementERP.setString(5, "%" + ad2);
            ResultSet resultSet = preparedStatementERP.executeQuery();
            while(resultSet.next())
            {                
                satirList.add(resultSet.getString("CODE") + "___" + resultSet.getString("NAME3"));
            }   
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnectionERP.baglantiKapatERP();
            if(preparedStatementERP!=null)
            { 
              preparedStatementERP.close();
            }            
            return satirList;
        }
    }
    
    public ERPItem getirItemERPdenKodIle(String kod, String ERPFirmaNo) throws Exception
    {
        ERPItem ERPItm = new ERPItem();
        
        DbConnection dbConnectionERP = new DbConnection();
        Connection connectionERP = dbConnectionERP.baglantiAcERP();
        
        PreparedStatement preparedStatementERP = null;
        
        String ERPFirmaNoTemp = "";

        if(ERPFirmaNo.length() == 1)
            ERPFirmaNoTemp = "00" + ERPFirmaNo;
        else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        String query = " SELECT "
                     + " ITM.CODE, ITM.NAME, ITM.NAME3, UNI.CODE AS BIRIM "
                     + " FROM LG_" + ERPFirmaNoTemp + "_ITEMS ITM "
                     + " LEFT OUTER JOIN LG_022_UNITSETL UNI ON ITM.UNITSETREF = UNI.UNITSETREF"
                                 + " AND UNI.LINENR = 1 "
                     + " WHERE ITM.ACTIVE = 0 "
                     + " AND ITM.CODE = '" + kod + "'";
        
        try 
        {
            preparedStatementERP = connectionERP.prepareStatement(query);
            ResultSet resultSet = preparedStatementERP.executeQuery();
            
            while(resultSet.next())
            {   
                ERPItm.setKod(resultSet.getString("CODE"));
                ERPItm.setAd(resultSet.getString("NAME"));
                ERPItm.setAd2(resultSet.getString("NAME3"));
                ERPItm.setBirim(resultSet.getString("BIRIM"));
            }   
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnectionERP.baglantiKapatERP();
            if(preparedStatementERP!=null)
            { 
              preparedStatementERP.close();
            }            
            return ERPItm;
        }
    }
    
    public String getirEnBuyukTeklifNumarasi() throws Exception
    {        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        String number = "";
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT MAX(RIGHT(TEKLIF_NUMARASI, LEN(TEKLIF_NUMARASI)-1)) AS NUMBER"
                     + " FROM TEKLIF_BASLIK "
                     + " WHERE KULLANIM_DURUMU IN (1,2) "
                     + " AND SATIS_SATINALMA = 0 "
                     + " AND LEN(TEKLIF_NUMARASI) = 8 "
                     + " AND TEKLIF_NUMARASI LIKE '%T[0-9]%' ";
        
        try 
        {
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {   
                number = resultSet.getString("NUMBER");
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
            return number;
        }
    }
        
    public String getirMusteriParaBirimiMusteriKodIle(String musteriKodu, String ERPFirmaNo) throws Exception
    {
        String paraBirimi = "";
        
        DbConnection dbConnectionERP = new DbConnection();
        Connection connectionERP = dbConnectionERP.baglantiAcERP();
        
        PreparedStatement preparedStatementERP = null;
        
        String ERPFirmaNoTemp = "";

        if(ERPFirmaNo.length() == 1)
            ERPFirmaNoTemp = "00" + ERPFirmaNo;
        else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        String query =  " SELECT ISNULL(CURR.CURCODE,'TL') AS PARA_BIRIMI " +
                        " FROM LG_" + ERPFirmaNoTemp + "_CLCARD CLC  " +
                        " LEFT OUTER JOIN L_CURRENCYLIST CURR ON CLC.CCURRENCY = CURR.CURTYPE  " +
                                    " AND FIRMNR = '" + ERPFirmaNoTemp + "' " +
                        " WHERE CLC.CODE = '" + musteriKodu + "'";
        
        try 
        {
            preparedStatementERP = connectionERP.prepareStatement(query);
            ResultSet resultSet = preparedStatementERP.executeQuery();
            
            while(resultSet.next())
            {   
                paraBirimi = resultSet.getString("PARA_BIRIMI");
            }   
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            dbConnectionERP.baglantiKapatERP();
            if(preparedStatementERP!=null)
            { 
              preparedStatementERP.close();
            }            
            return paraBirimi;
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
            jasperReport = JasperCompileManager.compileReport(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "resources/reports/InvoiceFirma.jrxml");
            
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

    
    public List<TedarikciListSatisSatirBazinda> getirTedarikciListSatisSatirBazinda(String ERPFirmaNo
                                                                                   ,String itemCode) throws Exception
    {
        List<TedarikciListSatisSatirBazinda> tedarikciListSatisSatirBazindaList = new ArrayList<>();
        
        DbConnection dbConnection = new DbConnection();
        Connection connectionERP = dbConnection.baglantiAcERP();
        PreparedStatement preparedStatementERP = null;
        
        String ERPFirmaNoTemp = "";
        
        if(ERPFirmaNo.length() == 1)
                ERPFirmaNoTemp = "00" + ERPFirmaNo;
            else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        String queryERP = " SELECT CLC.CODE AS ICUSTSUPCODE " +
                          " FROM LG_" + ERPFirmaNoTemp + "_ITEMS IT " +
                          " LEFT JOIN LG_" + ERPFirmaNoTemp + "_SUPPASGN SP ON SP.ITEMREF = IT.LOGICALREF " +
                          " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_CLCARD CLC ON CLC.LOGICALREF = SP.CLIENTREF " +
                          " WHERE IT.CODE = '" + itemCode + "' AND SP.CLCARDTYPE = 1 ";
        
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
                TedarikciListSatisSatirBazinda tedarikciListSatisSatirBazinda = new TedarikciListSatisSatirBazinda();
                tedarikciListSatisSatirBazinda.setMusteriRecordId(resultSet.getString("RECORD_ID"));
                tedarikciListSatisSatirBazinda.setMusteriUnvani(resultSet.getString("MUSTERI_UNVANI"));  
                tedarikciListSatisSatirBazinda.setMusteriKodu(resultSet.getString("MUSTERI_KODU"));
                tedarikciListSatisSatirBazinda.setMusteriKoduVeUnvani(
                        tedarikciListSatisSatirBazinda.getMusteriKodu() + "-" + tedarikciListSatisSatirBazinda.getMusteriUnvani());
                tedarikciListSatisSatirBazinda.setSecildiMi(0);
                tedarikciListSatisSatirBazindaList.add(tedarikciListSatisSatirBazinda);
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
            return tedarikciListSatisSatirBazindaList;
        }
    }
    
    public List<TedarikciListSatisSatirBazinda> getirTedarikciListSatisSatirTumu(String ERPFirmaNo
                                                                                ,String teklifBaslikRecordId) throws Exception
    {
        List<TedarikciListSatisSatirBazinda> tedarikciListSatisSatirBazindaList = new ArrayList<>();
        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        PreparedStatement preparedStatement = null;
        
        String ERPFirmaNoTemp = "";
        
        if(ERPFirmaNo.length() == 1)
                ERPFirmaNoTemp = "00" + ERPFirmaNo;
            else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        String query =  " SELECT " +
                        " TS.TEKLIF_SATIR_ID " +
                        " ,CLC.CODE AS MUSTERI_KODU " +
                        " ,M.RECORD_ID AS MUSTERI_RECORD_ID " +
                        " ,M.MUSTERI_UNVANI " +
                        " ,SP.PRIORITY " +
                        " FROM TEKLIF_SATIR TS " +
                        " LEFT JOIN " + dbConnection.getDbNameERP() + ".dbo.LG_" + ERPFirmaNoTemp + "_ITEMS IT ON TS.MALZEME_HIZMET_MASRAF_KODU = IT.CODE " +
                        " LEFT JOIN " + dbConnection.getDbNameERP() + ".dbo.LG_" + ERPFirmaNoTemp + "_SUPPASGN SP ON SP.ITEMREF = IT.LOGICALREF " +
                        " LEFT OUTER JOIN " + dbConnection.getDbNameERP() + ".dbo.LG_" + ERPFirmaNoTemp + "_CLCARD CLC ON CLC.LOGICALREF = SP.CLIENTREF " +
                        " LEFT OUTER JOIN MUSTERI M ON M.MUSTERI_KODU = CLC.CODE " +
                        " WHERE TS.TEKLIF_BASLIK_RECORD_ID = '" + teklifBaslikRecordId + "' " +
                          " AND TS.ERP_FIRMA_NUMBER = '" + ERPFirmaNo + "' " +
                          " AND TS.KULLANIM_DURUMU = 1 ";
        
        try 
        {
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {
                if(resultSet.getString("MUSTERI_KODU") != null)
                {
                    TedarikciListSatisSatirBazinda tedarikciListSatisSatirBazinda = new TedarikciListSatisSatirBazinda();
                    
                    tedarikciListSatisSatirBazinda.setTeklifSatirId(resultSet.getInt("TEKLIF_SATIR_ID"));                     
                    tedarikciListSatisSatirBazinda.setMusteriKodu(resultSet.getString("MUSTERI_KODU"));
                    tedarikciListSatisSatirBazinda.setMusteriRecordId(resultSet.getString("MUSTERI_RECORD_ID"));
                    tedarikciListSatisSatirBazinda.setMusteriUnvani(resultSet.getString("MUSTERI_UNVANI"));  
                    tedarikciListSatisSatirBazinda.setMusteriKoduVeUnvani(
                            tedarikciListSatisSatirBazinda.getMusteriKodu() + "-" + tedarikciListSatisSatirBazinda.getMusteriUnvani());
                    tedarikciListSatisSatirBazinda.setSecildiMi(0);
                    tedarikciListSatisSatirBazinda.setOncelik(resultSet.getInt("PRIORITY"));
                    
                    tedarikciListSatisSatirBazindaList.add(tedarikciListSatisSatirBazinda);
                } 
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
            return tedarikciListSatisSatirBazindaList;
        }
    }
    
    public List<TedarikciListSatisSatirBazinda> getirTedarikciListSatisSatiriTek(String ERPFirmaNo
                                                                                ,int teklifSatirId) throws Exception
    {
        List<TedarikciListSatisSatirBazinda> tedarikciListSatisSatirBazindaList = new ArrayList<>();
        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        PreparedStatement preparedStatement = null;
        
        String ERPFirmaNoTemp = "";
        
        if(ERPFirmaNo.length() == 1)
                ERPFirmaNoTemp = "00" + ERPFirmaNo;
            else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        String query =  " SELECT " +
                        " TS.TEKLIF_SATIR_ID " +
                        " ,CLC.CODE AS MUSTERI_KODU " +
                        " ,M.RECORD_ID AS MUSTERI_RECORD_ID " +
                        " ,M.MUSTERI_UNVANI " +
                        " ,SP.PRIORITY " +
                        " FROM TEKLIF_SATIR TS " +
                        " LEFT JOIN " + dbConnection.getDbNameERP() + ".dbo.LG_" + ERPFirmaNoTemp + "_ITEMS IT ON TS.MALZEME_HIZMET_MASRAF_KODU = IT.CODE " +
                        " LEFT JOIN " + dbConnection.getDbNameERP() + ".dbo.LG_" + ERPFirmaNoTemp + "_SUPPASGN SP ON SP.ITEMREF = IT.LOGICALREF " +
                        " LEFT OUTER JOIN " + dbConnection.getDbNameERP() + ".dbo.LG_" + ERPFirmaNoTemp + "_CLCARD CLC ON CLC.LOGICALREF = SP.CLIENTREF " +
                        " LEFT OUTER JOIN MUSTERI M ON M.MUSTERI_KODU = CLC.CODE " +
                        " WHERE TS.TEKLIF_SATIR_ID = " + teklifSatirId + 
                          " AND TS.ERP_FIRMA_NUMBER = '" + ERPFirmaNo + "' " +
                          " AND TS.KULLANIM_DURUMU = 1 ";
        
        try 
        {
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {
                if(resultSet.getString("MUSTERI_KODU") != null)
                {
                    TedarikciListSatisSatirBazinda tedarikciListSatisSatirBazinda = new TedarikciListSatisSatirBazinda();
                    
                    tedarikciListSatisSatirBazinda.setTeklifSatirId(resultSet.getInt("TEKLIF_SATIR_ID"));                     
                    tedarikciListSatisSatirBazinda.setMusteriKodu(resultSet.getString("MUSTERI_KODU"));
                    tedarikciListSatisSatirBazinda.setMusteriRecordId(resultSet.getString("MUSTERI_RECORD_ID"));
                    tedarikciListSatisSatirBazinda.setMusteriUnvani(resultSet.getString("MUSTERI_UNVANI"));  
                    tedarikciListSatisSatirBazinda.setMusteriKoduVeUnvani(
                            tedarikciListSatisSatirBazinda.getMusteriKodu() + "-" + tedarikciListSatisSatirBazinda.getMusteriUnvani());
                    tedarikciListSatisSatirBazinda.setSecildiMi(0);
                    tedarikciListSatisSatirBazinda.setOncelik(resultSet.getInt("PRIORITY"));
                    
                    tedarikciListSatisSatirBazindaList.add(tedarikciListSatisSatirBazinda);
                } 
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
            return tedarikciListSatisSatirBazindaList;
        }
    }
    
    public double getirFiiliStokSatirBazinda(String ERPFirmaNo
                                             ,String itemCode) throws Exception
    {
        //Başlangıçta bilerek 0'a atadık...
        double fiiliStok = 0.0; 
        
        DbConnection dbConnection = new DbConnection();
        Connection connectionERP = dbConnection.baglantiAcERP();
        PreparedStatement preparedStatementERP = null;
        
        String ERPFirmaNoTemp = "";
        
        if(ERPFirmaNo.length() == 1)
                ERPFirmaNoTemp = "00" + ERPFirmaNo;
            else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
        String queryERP = " SELECT SUM(STI.ONHAND) AS FIILI_STOK " +
                          " FROM LV_" + ERPFirmaNoTemp + "_01_STINVTOT STI " +
                          " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_ITEMS ITM ON STI.STOCKREF = ITM.LOGICALREF " +
                          " WHERE STI.INVENNO = -1 " +
                            " AND ITM.CODE = '" + itemCode + "' " + 
                            " AND ITM.ACTIVE = 0 " + 
                          " GROUP BY STI.STOCKREF, ITM.CODE, ITM.NAME ";
               
        try 
        {
            preparedStatementERP = connectionERP.prepareStatement(queryERP);
            ResultSet resultSetERP = preparedStatementERP.executeQuery();
            
            resultSetERP.next();
            fiiliStok = resultSetERP.getDouble("FIILI_STOK");
                        
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
            return fiiliStok;
        }
    }
    
    public List<FiiliStokSatirBazinda> getirFiiliStokSatirTumu(String ERPFirmaNo
                                         ,String teklifBaslikRecordId) throws Exception
    {
        List<FiiliStokSatirBazinda> fiiliStokSatirBazindaList = new ArrayList<>();
        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        PreparedStatement preparedStatement = null;
        
        String ERPFirmaNoTemp = "";
        
        if(ERPFirmaNo.length() == 1)
                ERPFirmaNoTemp = "00" + ERPFirmaNo;
            else if(ERPFirmaNo.length() == 2)
            ERPFirmaNoTemp = "0" + ERPFirmaNo;
        
           String query =   " SELECT " +
                            " TS.TEKLIF_SATIR_ID, " + 
                            " SUM(STI.ONHAND) AS FIILI_STOK " +
                            " FROM TEKLIF_SATIR TS " +
                            " LEFT OUTER JOIN " + dbConnection.getDbNameERP() + ".dbo.LG_" + ERPFirmaNoTemp + "_ITEMS ITM ON TS.MALZEME_HIZMET_MASRAF_KODU = ITM.CODE " +
                                                    " AND ITM.ACTIVE = 0 " +
                            " LEFT OUTER JOIN " + dbConnection.getDbNameERP() + ".dbo.LV_" + ERPFirmaNoTemp + "_01_STINVTOT STI ON STI.STOCKREF = ITM.LOGICALREF " +
                                        " AND STI.INVENNO = -1 " +
                            " WHERE TS.TEKLIF_BASLIK_RECORD_ID = '" + teklifBaslikRecordId + "' " +
                              " AND TS.KULLANIM_DURUMU = 1 " +
                            " GROUP BY TS.TEKLIF_SATIR_ID,STI.STOCKREF, ITM.CODE, ITM.NAME " ;
               
        try 
        {
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {
                FiiliStokSatirBazinda fiiliStokSatirBazinda = new FiiliStokSatirBazinda();
                fiiliStokSatirBazinda.setFiiliStok(resultSet.getDouble("FIILI_STOK"));
                fiiliStokSatirBazinda.setTeklifSatirId(resultSet.getInt("TEKLIF_SATIR_ID")); 
                
                fiiliStokSatirBazindaList.add(fiiliStokSatirBazinda);
            }
           
                        
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
            return fiiliStokSatirBazindaList;
        }
    }
    
    public double getirMusteriKarMarji(String ERPFirmaNo
                                    ,String teklifBaslikRecordId) throws Exception
    {   
            double karMarji = 0.0;         
            
            DbConnection dbConnection = new DbConnection();
            Connection connectionERP = dbConnection.baglantiAcERP();
            PreparedStatement preparedStatementERP = null;

            String ERPFirmaNoTemp = "";

            if(ERPFirmaNo.length() == 1)
                    ERPFirmaNoTemp = "00" + ERPFirmaNo;
                else if(ERPFirmaNo.length() == 2)
                ERPFirmaNoTemp = "0" + ERPFirmaNo;
            
             String queryERP = " SELECT ISNULL(XT.DISCOUNT_RATE,0) AS CARI_HESAP_KAR_MARJI " +
                               " FROM LG_XT254_" + ERPFirmaNoTemp + " XT " +
                               " WHERE XT.PARLOGREF = (SELECT " +
                                                     " MUSTERI_RECORD_ID " +
                                                     " FROM " + dbConnection.getDbName() + ".dbo.TEKLIF_BASLIK " +
                                                     " WHERE RECORD_ID = '" + teklifBaslikRecordId + "') ";           
            
        try 
        {
            preparedStatementERP = connectionERP.prepareStatement(queryERP);
            ResultSet resultSetERP = preparedStatementERP.executeQuery();
            
            while(resultSetERP.next())
            {
                karMarji = resultSetERP.getDouble("CARI_HESAP_KAR_MARJI");
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
            return karMarji;
        }
    }
    
    public List<TanimliSatisFiyati> getirListeTanimliSatisFiyati(String ERPFirmaNo
                                                                ,String teklifBaslikRecordId) throws Exception
    {   
            List<TanimliSatisFiyati> tanimliSatisFiyatiList = new ArrayList<>();         
            
            DbConnection dbConnection = new DbConnection();
            Connection connectionERP = dbConnection.baglantiAcERP();
            PreparedStatement preparedStatementERP = null;

            String ERPFirmaNoTemp = "";

            if(ERPFirmaNo.length() == 1)
                    ERPFirmaNoTemp = "00" + ERPFirmaNo;
                else if(ERPFirmaNo.length() == 2)
                ERPFirmaNoTemp = "0" + ERPFirmaNo;
            
             String queryERP = " SELECT " +
                               " TABLO1.TEKLIF_SATIR_ID " +
                               //" ,TABLO1.LOGICALREF " +
                               //" ,TABLO1.CODE " +
                               //" ,TABLO1.NAME " +
                               " ,PRC.PRICE " +
                               //" ,PRC.CURRENCY " +
                               //" ,PRC.CODE " +			
                               //" ,PRC.CLSPECODE5 " +
                               //" ,PRC.PRIORITY " +
                               //" ,PRC.RECSTATUS " +
                               " FROM ( SELECT ITM.LOGICALREF " +
                                             " ,ITM.CODE " + 
                                             " ,ITM.NAME " +
                                             " ,TABLO2.MALZEME_HIZMET_MASRAF_BIRIM_KODU " +
                                             " ,TABLO2.PARA_BIRIMI " +
                                             " ,TABLO2.TEKLIF_SATIR_ID " +  
                                      " FROM (SELECT " +
                                            " TEKLIF_SATIR_ID " + 
                                            " ,MALZEME_HIZMET_MASRAF_KODU " +
                                            " ,MALZEME_HIZMET_MASRAF_BIRIM_KODU " +
                                            " ,PARA_BIRIMI " +
                                            " FROM " + dbConnection.getDbName() + ".dbo.TEKLIF_SATIR " +
                                            " WHERE TEKLIF_BASLIK_RECORD_ID = '" + teklifBaslikRecordId + "' " +
                                           " )TABLO2 " +
                                      " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_ITEMS ITM ON ITM.CODE = TABLO2.MALZEME_HIZMET_MASRAF_KODU " +
                                                  " AND ITM.ACTIVE = 0 " +
                                     " )TABLO1 " +
                               " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_PRCLIST PRC WITH(NOLOCK) ON TABLO1.LOGICALREF = PRC.CARDREF " + 
                                           " AND PRC.CURRENCY = TABLO1.PARA_BIRIMI " +
                                           " AND PRC.PTYPE = 2 " + 
                                           " AND PRC.ACTIVE = 0 " +
                                           " AND PRC.ENDDATE > GETDATE() " + 
                                           " AND PRC.PRICE > 0 " ;           
            
        try 
        {
            preparedStatementERP = connectionERP.prepareStatement(queryERP);
            ResultSet resultSetERP = preparedStatementERP.executeQuery();
            
            while(resultSetERP.next())
            {
                TanimliSatisFiyati tanimliSatisFiyati = new TanimliSatisFiyati();
                tanimliSatisFiyati.setTeklifSatirId(resultSetERP.getInt("TEKLIF_SATIR_ID"));
                tanimliSatisFiyati.setFiyat(resultSetERP.getDouble("PRICE"));     
                tanimliSatisFiyati.setMusteriMiListeMi(1);
                tanimliSatisFiyati.setSecildiMi(false);
                
                tanimliSatisFiyatiList.add(tanimliSatisFiyati);
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
            return tanimliSatisFiyatiList;
        }
    }
    
    public List<TanimliSatisFiyati> getirMusteriyeTanimliSatisFiyati(String ERPFirmaNo
                                                                    ,String teklifBaslikRecordId) throws Exception
    {   
            List<TanimliSatisFiyati> tanimliSatisFiyatiList = new ArrayList<>();         
            
            DbConnection dbConnection = new DbConnection();
            Connection connectionERP = dbConnection.baglantiAcERP();
            PreparedStatement preparedStatementERP = null;

            String ERPFirmaNoTemp = "";

            if(ERPFirmaNo.length() == 1)
                    ERPFirmaNoTemp = "00" + ERPFirmaNo;
                else if(ERPFirmaNo.length() == 2)
                ERPFirmaNoTemp = "0" + ERPFirmaNo;
            
             String queryERP =  " SELECT " +
                                " TABLO1.TEKLIF_SATIR_ID" +
                                " ,PRC.PRICE " +
                                //" ,PRC.CURRENCY " +
                                " FROM ( SELECT " +
                                         " LOGICALREF " +
                                         " ,CODE " + 
                                         " ,NAME  " +
                                         " ,TABLO2.MALZEME_HIZMET_MASRAF_BIRIM_KODU " + 
                                         " ,TABLO2.TEKLIF_SATIR_ID" +
                                         " ,TABLO2.PARA_BIRIMI" +
                                       " FROM (SELECT " +
                                             " TEKLIF_SATIR_ID " +
                                             " ,MALZEME_HIZMET_MASRAF_KODU " +
                                             " ,MALZEME_HIZMET_MASRAF_BIRIM_KODU " +
                                             " ,PARA_BIRIMI" +
                                             " FROM " + dbConnection.getDbName() + ".dbo.TEKLIF_SATIR " +
                                             " WHERE TEKLIF_BASLIK_RECORD_ID = '" + teklifBaslikRecordId + "' " +
                                            " )TABLO2 " +
                                       " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_ITEMS ITM ON ITM.CODE = TABLO2.MALZEME_HIZMET_MASRAF_KODU " +
                                                   " AND ITM.ACTIVE = 0 " +
                                    " )TABLO1 " +
                                " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_PRCLIST PRC WITH(NOLOCK) ON TABLO1.LOGICALREF = PRC.CARDREF " +
                                            " AND PRC.CURRENCY = TABLO1.PARA_BIRIMI " +
                                " WHERE PRC.CLIENTCODE = (SELECT CLC.CODE " +
                                                         " FROM LG_" + ERPFirmaNoTemp + "_CLCARD CLC " + 
                                                        " WHERE CLC.LOGICALREF = (SELECT MUSTERI_RECORD_ID " +
                                                                                 " FROM " + dbConnection.getDbName() + ".dbo.TEKLIF_BASLIK " +
                                                                                 " WHERE RECORD_ID = '" + teklifBaslikRecordId + "') " +
                                                        " ) " +
                                " AND PRC.PTYPE = 2 " + 
                                " AND PRC.ACTIVE = 0 " +
                                " AND PRC.ENDDATE > GETDATE() " + 
                                " AND PRC.PRICE > 0 " ;           
            
        try 
        {
            preparedStatementERP = connectionERP.prepareStatement(queryERP);
            ResultSet resultSetERP = preparedStatementERP.executeQuery();
            
            while(resultSetERP.next())
            {
                TanimliSatisFiyati tanimliSatisFiyati = new TanimliSatisFiyati();
                tanimliSatisFiyati.setTeklifSatirId(resultSetERP.getInt("TEKLIF_SATIR_ID"));
                tanimliSatisFiyati.setFiyat(resultSetERP.getDouble("PRICE"));     
                tanimliSatisFiyati.setMusteriMiListeMi(0); //0 : Müşteriye tanımlı, 1: Liste tanımlı
                tanimliSatisFiyati.setSecildiMi(false);
                
                tanimliSatisFiyatiList.add(tanimliSatisFiyati);
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
            return tanimliSatisFiyatiList;
        }
    }
    
    public List<SonTeklifVeSatinAlmaSatisSatiri> 
        getirSonTeklifVeSatinAlmaSatisSatiriIcin(String ERPFirmaNo
                                                ,String teklifBaslikRecordId) throws Exception
        {
            List<SonTeklifVeSatinAlmaSatisSatiri> sonTeklifVeSatinAlmaSatisSatiriList = new ArrayList<>();
            
            DbConnection dbConnection = new DbConnection();
            Connection connectionERP = dbConnection.baglantiAcERP();
            PreparedStatement preparedStatementERP = null;

            String ERPFirmaNoTemp = "";

            if(ERPFirmaNo.length() == 1)
                ERPFirmaNoTemp = "00" + ERPFirmaNo;
            else if(ERPFirmaNo.length() == 2)
                ERPFirmaNoTemp = "0" + ERPFirmaNo;    
            
            
             String queryERP =  " SELECT " +
                                    " TABLO1.TEKLIF_SATIR_ID " +
                                    " ,ISNULL((SELECT TOP 1 " + 
                                             " STR( " +
                                                 " CASE " +
                                                     " WHEN STL.TRCURR NOT IN (0,160) THEN STL.LINENET / STL.AMOUNT / STL.TRRATE " +
                                                     " ELSE STL.LINENET / STL.AMOUNT " +
                                                     " END " +
                                                     " , 25, 3) " +
                                                     " + '__' + CONVERT(VARCHAR, STL.DATE_, 4)  " +
                                                     " + '__' + CLC.CODE " +
                                                     " + '_' + CLC.DEFINITION_ " +
                                             " FROM LG_" + ERPFirmaNoTemp + "_01_STLINE STL " + 
                                             " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_CLCARD CLC WITH(NOLOCK) ON STL.CLIENTREF = CLC.LOGICALREF " +
                                             " WHERE STL.LOGICALREF = ( " +
                                                                        " SELECT TOP 1 " +
                                                                          " STL.LOGICALREF " +
                                                                        " FROM LG_" + ERPFirmaNoTemp + "_01_STLINE STL " +
                                                                        " WHERE STL.STOCKREF = TABLO1.LOGICALREF " +
                                                                            " AND STL.CANCELLED = 0 " +  
                                                                            " AND STL.LINETYPE = 0 " +
                                                                            " AND STL.TRCODE = 1 " +  
                                                                            " AND STL.INVOICEREF > 0 " +
                                                                            " AND STL.TRCURR = TABLO1.PARA_BIRIMI " +
                                                                        " ORDER BY STL.DATE_ DESC " +
                                                                     " ) " +
                                    " ),0) AS SATIN_ALMA_FIYAT_TARIH_CARI_KOD_UNVAN " + 
                                    " ,ISNULL((SELECT TOP 1 " + 
                                                " STR(TSY.BIRIM_FIYATI, 25, 3) " +
                                                " + '__' + CONVERT(VARCHAR, TSY.GUNCELLEME_TARIHI, 4) " + 
                                                " + '__' + M.MUSTERI_KODU " + 
                                                " + '_' + M.MUSTERI_UNVANI " + 
                                            " FROM " + dbConnection.getDbName() + ".dbo.TEKLIF_SATIR_YANIT TSY " + 
                                            " LEFT OUTER JOIN " + dbConnection.getDbName() + ".dbo.MAIL_ADRES MA ON MA.MAIL_ADRES_ID = TSY.MAIL_ADRES_ID " + 
                                            " LEFT OUTER JOIN " + dbConnection.getDbName() + ".dbo.TEKLIF_SATIR TS ON TS.TEKLIF_SATIR_ID = TSY.TEKLIF_SATIR_ID " + 
                                            " LEFT OUTER JOIN " + dbConnection.getDbName() + ".dbo.MUSTERI M ON M.RECORD_ID = MA.MUSTERI_RECORD_ID " + 
                                            " WHERE MA.PARA_BIRIMI = TABLO1.PARA_BIRIMI_STR " + 
                                              " AND TS.MALZEME_HIZMET_MASRAF_KODU = TABLO1.CODE	" + 
                                            " ORDER BY TSY.GUNCELLEME_TARIHI DESC " + 
                                    " ),0) AS TEKLIF_FIYAT_TARIH_CARI_KOD_UNVAN " + 
                                " FROM " +
                                " ( " +
                                " SELECT " +
                                " ITM.LOGICALREF " +
                                " ,ITM.CODE " +
                                " ,ITM.NAME " +
                                " ,TABLO2.TEKLIF_SATIR_ID " +
                                " ,TABLO2.PARA_BIRIMI " +
                                " ,TABLO2.PARA_BIRIMI_STR " +
                                " FROM (SELECT " +
                                      " TEKLIF_SATIR_ID " +            
                                      " ,MALZEME_HIZMET_MASRAF_KODU " +
                                      " ,PARA_BIRIMI " +
                                      " ,CASE PARA_BIRIMI " +  
                                       " WHEN 0 THEN 'TL' " +
                                       " WHEN 1 THEN 'USD' " +
                                       " WHEN 20 THEN 'EUR' " +
                                       " END AS PARA_BIRIMI_STR " +
                                      " FROM " + dbConnection.getDbName() + ".dbo.TEKLIF_SATIR " +
                                      " WHERE TEKLIF_BASLIK_RECORD_ID = '" + teklifBaslikRecordId + "' " +
                                      " )TABLO2 " +                            
                                " LEFT OUTER JOIN LG_" + ERPFirmaNoTemp + "_ITEMS ITM ON ITM.CODE = TABLO2.MALZEME_HIZMET_MASRAF_KODU " + 
                                            " AND ITM.ACTIVE = 0 " +
                                " )TABLO1 " ;
             
             try 
            {
                preparedStatementERP = connectionERP.prepareStatement(queryERP);
                ResultSet resultSetERP = preparedStatementERP.executeQuery();

                while(resultSetERP.next())
                {
                    SonTeklifVeSatinAlmaSatisSatiri sonTeklifVeSatinAlmaSatisSatiri = 
                            new SonTeklifVeSatinAlmaSatisSatiri();
                                        
                    String SATIN_ALMA_FIYAT_TARIH_CARI_KOD_UNVAN = "";
                    String TEKLIF_FIYAT_TARIH_CARI_KOD_UNVAN = "";
                    
                    SATIN_ALMA_FIYAT_TARIH_CARI_KOD_UNVAN = resultSetERP.getString("SATIN_ALMA_FIYAT_TARIH_CARI_KOD_UNVAN"); 
                    TEKLIF_FIYAT_TARIH_CARI_KOD_UNVAN = resultSetERP.getString("TEKLIF_FIYAT_TARIH_CARI_KOD_UNVAN"); 
                    
                    sonTeklifVeSatinAlmaSatisSatiri.setTeklifSatirId(resultSetERP.getInt("TEKLIF_SATIR_ID"));
                    
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    if(!SATIN_ALMA_FIYAT_TARIH_CARI_KOD_UNVAN.equals("0"))
                    {
                      //Önce boşlukları atıyoruz....
                      SATIN_ALMA_FIYAT_TARIH_CARI_KOD_UNVAN = SATIN_ALMA_FIYAT_TARIH_CARI_KOD_UNVAN.trim();
                      
                      String degerler[] = SATIN_ALMA_FIYAT_TARIH_CARI_KOD_UNVAN.split("__");
                      
                      String sonSatinAlmaFiyati = degerler[0];                            
                      String sonSatinAlmaTarihi = degerler[1];
                      String sonSatinAlmaCari = degerler[2];
                      
                      sonTeklifVeSatinAlmaSatisSatiri.setSonSatinAlmaFiyati(Double.valueOf(sonSatinAlmaFiyati));
                      
                      SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy");
                      sonTeklifVeSatinAlmaSatisSatiri.setSonSatinAlmaTarihi(formatter.parse(sonSatinAlmaTarihi));
                      
                      sonTeklifVeSatinAlmaSatisSatiri.setSonSatinAlmaCari(sonSatinAlmaCari);
                    }
                    else
                    {
                      sonTeklifVeSatinAlmaSatisSatiri.setSonSatinAlmaFiyati(0.0);
                      sonTeklifVeSatinAlmaSatisSatiri.setSonSatinAlmaTarihi(null);                        
                      sonTeklifVeSatinAlmaSatisSatiri.setSonSatinAlmaCari("-");
                    }
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    if(!TEKLIF_FIYAT_TARIH_CARI_KOD_UNVAN.equals("0"))
                    {
                      //Önce boşlukları atıyoruz....
                      TEKLIF_FIYAT_TARIH_CARI_KOD_UNVAN = TEKLIF_FIYAT_TARIH_CARI_KOD_UNVAN.trim();  
                      
                      String degerler[] = TEKLIF_FIYAT_TARIH_CARI_KOD_UNVAN.split("__");
                      
                      String sonTeklifFiyati = degerler[0];                            
                      String sonTeklifTarihi = degerler[1];
                      String sonTeklifCari = degerler[2];
                      
                      sonTeklifVeSatinAlmaSatisSatiri.setSonSatinAlmaTeklifFiyati(Double.valueOf(sonTeklifFiyati));
                      
                      SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy");
                      sonTeklifVeSatinAlmaSatisSatiri.setSonSatinAlmaTeklifTarihi(formatter.parse(sonTeklifTarihi));
                      
                      sonTeklifVeSatinAlmaSatisSatiri.setSonTeklifCari(sonTeklifCari);
                    }
                    else
                    {
                      sonTeklifVeSatinAlmaSatisSatiri.setSonSatinAlmaTeklifFiyati(0.0);
                      sonTeklifVeSatinAlmaSatisSatiri.setSonSatinAlmaTeklifTarihi(null);                         
                      sonTeklifVeSatinAlmaSatisSatiri.setSonTeklifCari("-");
                    }
                    
                    sonTeklifVeSatinAlmaSatisSatiriList.add(sonTeklifVeSatinAlmaSatisSatiri);
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
                return sonTeklifVeSatinAlmaSatisSatiriList;
            }
        }
    
    
    public void kaydetMailSatinAlma(String gonderen, 
                                    String konu,
                                    String icerik,
                                    int teklifBaslikId,
                                    String satisSatiriAtanmisTedarikciRecordId,
                                    int olusturanId) throws Exception
    {
        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        PreparedStatement preparedStatement4 = null;
        
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
                                    + " AND TEKLIF_BASLIK_ID = ? ";
                    
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
                                    " ,KUR) " +
                                    " VALUES(?,?,?,?,?,?,?)" ;
                    
                    preparedStatement3 = connection.prepareStatement(query3);
                    preparedStatement3.setString(1, gonderen);
                    preparedStatement3.setInt(2, mailId);
                    preparedStatement3.setString(3, satisSatiriAtanmisTedarikciRecordId); 
                    preparedStatement3.setInt(4, 1);
                    preparedStatement3.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
                    preparedStatement3.setInt(6, olusturanId); 
                    preparedStatement3.setDouble(7, 1.0);
                    preparedStatement3.executeUpdate();
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
    
    public EskiYeniList revizeEtMailSatinAlma(int eskiTeklifBaslikId
                                             ,int yeniTeklifBaslikId) throws Exception
    {
        EskiYeniList eskiYeniList = new EskiYeniList();
        eskiYeniList.setEskiYeniListMail(new ArrayList<>());
        eskiYeniList.setEskiYeniListMailAdres(new ArrayList<>());
        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;    
        PreparedStatement preparedStatement4 = null; 
                
          try 
            {            
                String query =  " SELECT " +
                                " M.MAIL_ID " +
                                " ,M.MAIL_GONDEREN " +
                                " ,M.MAIL_KONU " +
                                " ,M.MAIL_ICERIK " +
                                " ,M.KULLANIM_DURUMU AS MAIL_KULLANIM_DURUMU " +
                                " ,M.MAIL_GONDERIM_TARIHI " +
                                " ,MA.MAIL_ADRES_ID " +
                                " ,MA.MAIL_ADRESI " +
                                " ,MA.MUSTERI_RECORD_ID " +
                                " ,MA.KULLANIM_DURUMU AS MAIL_ADRES_KULLANIM_DURUMU " +
                                " ,MA.DURUMU " +
                                " ,MA.TAMAMLANDI_MI " +
                                " ,MA.YANITLAYAN_AD_SOYAD " +
                                " ,MA.PARA_BIRIMI " +
                                " ,MA.KUR " +
                                " FROM MAIL M " +
                                " LEFT OUTER JOIN MAIL_ADRES MA ON MA.MAIL_ID = M.MAIL_ID " +
                                " WHERE M.TEKLIF_BASLIK_ID = ? ";
                
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, eskiTeklifBaslikId);
                ResultSet resultSet = preparedStatement.executeQuery();
                
                 while(resultSet.next())
                {                       
                    int eskiMailAdresId = resultSet.getInt("MAIL_ADRES_ID");
                    int eskiMailId = resultSet.getInt("MAIL_ID");
                    
                    EskiYeni eskiYeniMail = new EskiYeni();
                    EskiYeni eskiYeniMailAdres = new EskiYeni();
                    
                    String query1 = "INSERT INTO [MAIL] " +
                                    " ( " +
                                    " MAIL_GONDEREN " +
                                    " ,MAIL_KONU " +
                                    " ,MAIL_ICERIK " +
                                    " ,TEKLIF_BASLIK_ID " +
                                    " ,KULLANIM_DURUMU " +
                                    " ,MAIL_GONDERIM_TARIHI " +
                                    " ) " +
                                    " VALUES(?,?,?,?,?,?)" ;
                    
                    preparedStatement1 = connection.prepareStatement(query1);
                    preparedStatement1.setString(1, resultSet.getString("MAIL_GONDEREN"));
                    preparedStatement1.setString(2, resultSet.getString("MAIL_KONU"));
                    preparedStatement1.setString(3, resultSet.getString("MAIL_ICERIK"));
                    preparedStatement1.setInt(4, yeniTeklifBaslikId);
                    preparedStatement1.setInt(5, resultSet.getInt("MAIL_KULLANIM_DURUMU"));
                    preparedStatement1.setTimestamp(6, resultSet.getTimestamp("MAIL_GONDERIM_TARIHI"));
                    preparedStatement1.executeUpdate();
                    preparedStatement1.close();
                    
                    //////////////////////////////////////////////////////////////
                    
                    String query2 = " SELECT "
                                    + " MAX(MAIL_ID) AS MAIL_ID "
                                    + " FROM MAIL "
                                    + " WHERE KULLANIM_DURUMU = 1 " 
                                    + " AND TEKLIF_BASLIK_ID = ? ";
                    
                    preparedStatement2 = connection.prepareStatement(query2);
                    preparedStatement2.setInt(1, yeniTeklifBaslikId);
                    ResultSet resultSet1 = preparedStatement2.executeQuery();
                    resultSet1.next();
                    
                    int yeniMailId = resultSet1.getInt("MAIL_ID");
                    
                    preparedStatement2.close();
                    
                    //////////////////////////////////////////////////////////////                    
                    
                    String query3 = "INSERT INTO [MAIL_ADRES] " +
                                    " ( " +
                                    "  MAIL_ADRESI " +
                                    " ,MAIL_ID " +
                                    " ,MUSTERI_RECORD_ID " +
                                    " ,KULLANIM_DURUMU " +
                                    " ,KUR " +
                                    " ,DURUMU " +
                                    " ,TAMAMLANDI_MI " +
                                    " ,YANITLAYAN_AD_SOYAD " +
                                    " ,PARA_BIRIMI " +
                                    " ) " +
                                    " VALUES(?,?,?,?,?,?,?,?,?)" ;
                    
                    preparedStatement3 = connection.prepareStatement(query3);
                    preparedStatement3.setString(1,  resultSet.getString("MAIL_ADRESI"));
                    preparedStatement3.setInt(2, yeniMailId);
                    preparedStatement3.setString(3,  resultSet.getString("MUSTERI_RECORD_ID")); 
                    preparedStatement3.setInt(4,  resultSet.getInt("MAIL_ADRES_KULLANIM_DURUMU"));
                    preparedStatement3.setDouble(5, resultSet.getDouble("KUR"));
                    preparedStatement3.setInt(6,  resultSet.getInt("DURUMU"));
                    preparedStatement3.setInt(7,  resultSet.getInt("TAMAMLANDI_MI"));
                    preparedStatement3.setString(8,  resultSet.getString("YANITLAYAN_AD_SOYAD"));
                    preparedStatement3.setString(9, resultSet.getString("PARA_BIRIMI"));
                    preparedStatement3.executeUpdate();
                    preparedStatement3.close();   
                    
                    //////////////////////////////////////////////////////////////
                    
                    String query4 = " SELECT "
                                    + " MAX(MAIL_ADRES_ID) AS MAIL_ADRES_ID "
                                    + " FROM MAIL_ADRES "
                                    + " WHERE MAIL_ID = ? ";
                    
                    preparedStatement4 = connection.prepareStatement(query4);
                    preparedStatement4.setInt(1, yeniMailId);
                    ResultSet resultSet2 = preparedStatement4.executeQuery();
                    resultSet2.next();                    
                    int yeniMailAdresId = resultSet2.getInt("MAIL_ADRES_ID");                    
                    preparedStatement4.close();
                    
                    eskiYeniMail.setEskiId(eskiMailId);
                    eskiYeniMail.setYeniId(yeniMailId);
                    
                    eskiYeniMailAdres.setEskiId(eskiMailAdresId);
                    eskiYeniMailAdres.setYeniId(yeniMailAdresId);
                    
                    eskiYeniList.getEskiYeniListMail().add(eskiYeniMail);
                    eskiYeniList.getEskiYeniListMailAdres().add(eskiYeniMailAdres);
                    
                    
                    //////////////////////////////////////////////////////////////  
                }
                 
                preparedStatement.close(); 
                return eskiYeniList;
            }
            catch (Exception ex) 
            {
                System.out.println(ex);
            }
            finally
            {
                if(preparedStatement!=null)
                { 
                  preparedStatement.close();
                } 
                
                if(preparedStatement1!=null)
                { 
                  preparedStatement1.close();
                } 
                
                if(preparedStatement2!=null)
                { 
                  preparedStatement2.close();
                } 
                
                if(preparedStatement3!=null)
                { 
                  preparedStatement3.close();
                } 
                
                if(preparedStatement4!=null)
                { 
                  preparedStatement4.close();
                } 
                
                dbConnection.baglantiKapat();
                return eskiYeniList;
            }        
    }
    
    
    public void kaydetTeklifSatirYanitTedarikci(List<MAIL_ADRES> mailAdresleri,
                                                List<Integer> teklifSatirIdList,
                                                int olusturanId,
                                                String ERPFirmaNo) throws Exception
    {
        
        List<TEKLIF_SATIR> teklifSatirlari = new ArrayList<>();
        
        for(Integer teklifSatirId : teklifSatirIdList)
        {
            teklifSatirlari.add(getirTeklifSatirIdIleTedarikci(teklifSatirId));
        }
        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        
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
                            preparedStatement.setDouble(5, getirSonMaliyetTedarikci(mailAdres.getMUSTERI_RECORD_ID()
                                                                                   ,teklifSatir.getMALZEME_HIZMET_MASRAF_KODU()
                                                                                   ,ERPFirmaNo));
                            preparedStatement.setString(6, teklifSatir.getMALZEME_HIZMET_MASRAF_BIRIM_KODU());
                            preparedStatement.executeUpdate();                          
                            
                    
                            //////////////////////////////////////////////////////////////
                            //SATISTAN_TEDARIKCI_KAYITLARI tablosuna da kayıt açıyoruz ki
                            //Satış teklifini tekrar açtığımızda hangi tedarikçiler
                            //ile hareket olmuş görebilelim...

                            String query2 = " SELECT "
                                            + " MAX(TEKLIF_SATIR_YANIT_ID) AS TEKLIF_SATIR_YANIT_ID "
                                            + " FROM TEKLIF_SATIR_YANIT "
                                            + " WHERE KULLANIM_DURUMU = 1 " 
                                            + " AND TEKLIF_SATIR_ID = ? "
                                            + " AND MAIL_ADRES_ID = ? ";

                            preparedStatement2 = connection.prepareStatement(query2);
                            preparedStatement2.setInt(1, teklifSatir.getTEKLIF_SATIR_ID());
                            preparedStatement2.setInt(2, mailAdres.getMAIL_ADRES_ID());
                            ResultSet resultSet = preparedStatement2.executeQuery();
                            resultSet.next();
                            int teklifSatirYanitId = resultSet.getInt("TEKLIF_SATIR_YANIT_ID");
                            preparedStatement2.close();
                            //////////////////////////////////////////////////////////////
                    
                            String query3 = "INSERT INTO " + dbConnection.getDbName() + ".[dbo].[SATISTAN_TEDARIKCI_KAYITLARI] " +
                                            " ( " +
                                            "  SIRA_NO " +
                                            " ,SECILDI_MI " +
                                            " ,TEKLIF_SATIR_YANIT_ID " +
                                            " ,TEKLIF_SATIR_ID " +
                                            " ,KULLANIM_DURUMU " +
                                            " ,OLUSTURMA_TARIHI " +
                                            " ,OLUSTURAN_ID " +
                                            " ) " +
                                            " VALUES(?,?,?,?,?,?,?)" ;

                            preparedStatement3 = connection.prepareStatement(query3);
                            preparedStatement3.setInt(1, 1);
                            preparedStatement3.setInt(2, 0);
                            preparedStatement3.setInt(3, teklifSatirYanitId); 
                            preparedStatement3.setInt(4, teklifSatir.getTEKLIF_SATIR_ID());  
                            preparedStatement3.setInt(5, 1);
                            preparedStatement3.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
                            preparedStatement3.setInt(7, olusturanId);  
                            preparedStatement3.executeUpdate();
                            preparedStatement3.close();

                            //////////////////////////////////////////////////////////////
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
    
    public EskiYeniList revizeEtTeklifSatirYanitTedarikci(List<TEKLIF_SATIR> revizeEdilecekTeklifSatirlari
                                                 ,EskiYeniList eskiYeniList
                                                 ,int eskiTeklifBaslikId) throws Exception
    {        
        //TeklifSatirYanit eski, yeni verilerini atama işlemi yapacağız...
        eskiYeniList.setEskiYeniListTeklifSatirYanit(new ArrayList<>());
        
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        
        String query = " SELECT " +
                       " TSY.TEKLIF_SATIR_YANIT_ID " +
                       " ,TSY.MAIL_ADRES_ID " +
                       " ,TSY.TEKLIF_SATIR_ID " +
                       " ,TSY.ACIKLAMA " +
                       " ,TSY.BIRIM_FIYATI " +
                       " ,TSY.STOKTA_VAR_YOK " +
                       " ,TSY.MIKTAR " +
                       " ,TSY.SONMALIYET " +
                       " ,TSY.BIRIM " +
                       " ,TSY.KULLANIM_DURUMU AS TSY_KULLANIM_DURUMU " +
                       " ,STK.KULLANIM_DURUMU AS STK_KULLANIM_DURUMU " +
                       " ,STK.TEKLIF_SATIR_ID " +
                       " ,STK.SIRA_NO " +
                       " ,STK.SECILDI_MI " +
                       " FROM TEKLIF_SATIR_YANIT TSY " +
                       " LEFT OUTER JOIN SATISTAN_TEDARIKCI_KAYITLARI STK ON STK.TEKLIF_SATIR_YANIT_ID = TSY.TEKLIF_SATIR_YANIT_ID " +
                       " WHERE TSY.MAIL_ADRES_ID IN ( " +
                                                    " SELECT " +
                                                    " MA.MAIL_ADRES_ID " +
                                                    " FROM MAIL M " +
                                                    " LEFT OUTER JOIN MAIL_ADRES MA ON MA.MAIL_ID = M.MAIL_ID " +
                                                    " WHERE M.TEKLIF_BASLIK_ID = " + eskiTeklifBaslikId +
                                                    " ) ";
              try 
                {       
                    preparedStatement = connection.prepareStatement(query);
                    //preparedStatement.setInt(1, eskiMailAdresId);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    
                     while(resultSet.next())
                    {
                        //Eski TeklifSatirId'yi kayıt altına alıyoruz...
                        int eskiTeklifSatirYanitId = resultSet.getInt("TEKLIF_SATIR_YANIT_ID");
                        
                        //yeniTeklifSatirId ve yeniMailAdresId her satır için değişecek...
                        int yeniTeklifSatirId = 0;
                        int yeniMailAdresId = 0;
                        
                        for(TEKLIF_SATIR teklifSatir : revizeEdilecekTeklifSatirlari)
                        {
                            //Eski id eşleşirse yeni id'yi alacağız ki yeni kayıtta kullanalım...
                            if(teklifSatir.getTEKLIF_SATIR_ID() == resultSet.getInt("TEKLIF_SATIR_ID"))
                            {
                              yeniTeklifSatirId = teklifSatir.getYeniTeklifSatirId();
                              break;
                            }
                        }
                        
                        for(EskiYeni eskiYeniMailAdres : eskiYeniList.getEskiYeniListMailAdres())
                        {
                            //Eski id eşleşirse yeni id'yi alacağız ki yeni kayıtta kullanalım...
                            if(eskiYeniMailAdres.getEskiId() == resultSet.getInt("MAIL_ADRES_ID"))
                                {
                                    yeniMailAdresId = eskiYeniMailAdres.getYeniId();
                                    break;
                                }                                
                        }
                        
                        //Satır yeni eklendiyse hareketleri aktarmaya gerek yok
                        //Eski satırlar ile hareket yapılmıştır.
                        
                        if(yeniTeklifSatirId != 0)
                        {
                            String query1 = "INSERT INTO [TEKLIF_SATIR_YANIT] " +
                                            " ( " +
                                            " KULLANIM_DURUMU " +
                                            " ,TEKLIF_SATIR_ID " +
                                            " ,MAIL_ADRES_ID " +
                                            " ,ACIKLAMA " +
                                            " ,BIRIM_FIYATI " +
                                            " ,STOKTA_VAR_YOK " +
                                            " ,MIKTAR " +
                                            " ,SONMALIYET " +
                                            " ,BIRIM " +
                                            " ) " +
                                            " VALUES(?,?,?,?,?,?,?,?,?)" ;

                            preparedStatement1 = connection.prepareStatement(query1);
                            preparedStatement1.setInt(1, resultSet.getInt("TSY_KULLANIM_DURUMU"));
                            preparedStatement1.setInt(2, yeniTeklifSatirId);
                            preparedStatement1.setInt(3, yeniMailAdresId);
                            preparedStatement1.setString(4, resultSet.getString("ACIKLAMA"));
                            preparedStatement1.setDouble(5, resultSet.getDouble("BIRIM_FIYATI"));
                            preparedStatement1.setInt(6, resultSet.getInt("STOKTA_VAR_YOK"));
                            preparedStatement1.setDouble(7, resultSet.getDouble("MIKTAR"));
                            preparedStatement1.setDouble(8, resultSet.getDouble("SONMALIYET"));
                            preparedStatement1.setString(9, resultSet.getString("BIRIM"));
                            preparedStatement1.executeUpdate();
                            preparedStatement1.close();

                            //////////////////////////////////////////////////////////////

                            String query2 = " SELECT "
                                            + " MAX(TEKLIF_SATIR_YANIT_ID) AS TEKLIF_SATIR_YANIT_ID "
                                            + " FROM TEKLIF_SATIR_YANIT ";

                            preparedStatement2 = connection.prepareStatement(query2);
                            ResultSet resultSet1 = preparedStatement2.executeQuery();
                            resultSet1.next();

                            int yeniTeklifSatirYanitId = resultSet1.getInt("TEKLIF_SATIR_YANIT_ID");

                            preparedStatement2.close();

                            //////////////////////////////////////////////////////////////      

                            EskiYeni eskiYeniTeklifSatirYanit = new EskiYeni();
                            eskiYeniTeklifSatirYanit.setEskiId(eskiTeklifSatirYanitId);
                            eskiYeniTeklifSatirYanit.setYeniId(yeniTeklifSatirYanitId);
                            eskiYeniList.getEskiYeniListTeklifSatirYanit().add(eskiYeniTeklifSatirYanit);

                            String query3 = "INSERT INTO [SATISTAN_TEDARIKCI_KAYITLARI] " +
                                            " ( " +
                                            "  KULLANIM_DURUMU " +
                                            " ,TEKLIF_SATIR_ID " +
                                            " ,TEKLIF_SATIR_YANIT_ID " +
                                            " ,SIRA_NO " +
                                            " ,SECILDI_MI " +
                                            " ) " +
                                            " VALUES(?,?,?,?,?)" ;

                            preparedStatement3 = connection.prepareStatement(query3);
                            preparedStatement3.setInt(1,  resultSet.getInt("STK_KULLANIM_DURUMU"));
                            preparedStatement3.setInt(2,  yeniTeklifSatirId); 
                            preparedStatement3.setInt(3, yeniTeklifSatirYanitId);
                            preparedStatement3.setInt(4,  resultSet.getInt("SIRA_NO"));
                            preparedStatement3.setInt(5, resultSet.getInt("SECILDI_MI"));
                            preparedStatement3.executeUpdate();
                            preparedStatement3.close();
                        }
                    }
                    
                    preparedStatement.close();
                    return eskiYeniList;
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
                    
                    if(preparedStatement1!=null)
                    { 
                      preparedStatement1.close();
                    } 
                     
                    if(preparedStatement2!=null)
                    { 
                      preparedStatement2.close();
                    } 
                      
                    if(preparedStatement3!=null)
                    { 
                      preparedStatement3.close();
                    } 
                    
                    return eskiYeniList;
                }        
    }
    
    public void temizleEskiMailHareketleriRevizyon(List<TEKLIF_SATIR> revizeEdilecekTeklifSatirlari
                                                  ,int yeniTeklifBaslikId) throws Exception
    {            
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String teklifIdListesi = "";
        for (TEKLIF_SATIR teklifSatir : revizeEdilecekTeklifSatirlari)
            if(teklifSatir.getTEKLIF_SATIR_ID()>0)
            teklifIdListesi = teklifIdListesi + ",'" + teklifSatir.getYeniTeklifSatirId()+ "'";
        
        //Birinci sıradaki virgülü silmek için...
        teklifIdListesi = teklifIdListesi.substring(1);
        
        String query = 
                       //Birinci Bölüm
                       //Mail tablosundaki gereksiz mailleri siliyoruz...
                       " UPDATE MAIL_ADRES " +
                       " SET KULLANIM_DURUMU = 0 " +
                       " WHERE MAIL_ID IN (  " +
                                            " SELECT MAIL_ID " +
                                            " FROM MAIL " +
                                            " WHERE TEKLIF_BASLIK_ID = " + yeniTeklifBaslikId +
                                            " AND MAIL_ID IN  (" +
                                                                 " SELECT MAIL_ID " +
                                                                 " FROM MAIL_ADRES " +
                                                                 " WHERE MAIL_ADRES_ID NOT IN ( " + 
                                                                                              " SELECT DISTINCT " +
                                                                                              " MAIL_ADRES_ID " +
                                                                                              " FROM TEKLIF_SATIR_YANIT " +
                                                                                              " WHERE KULLANIM_DURUMU = 1 " + 
                                                                                              " AND TEKLIF_SATIR_ID IN (" + teklifIdListesi + ")" +
                                                                                              " ) " +
                                                                " ) " +
                                         ") " +
                                                    
                                                    
                        //İkinci Bölüm
                        //MailAdres tablosundaki gereksiz mailleri siliyoruz...
                        " UPDATE MAIL " +
                        " SET KULLANIM_DURUMU = 0 " +
                        " WHERE TEKLIF_BASLIK_ID = " + yeniTeklifBaslikId +
                        " AND MAIL_ID  IN  (" +
                                             " SELECT MAIL_ID " +
                                             " FROM MAIL_ADRES " +
                                             " WHERE MAIL_ADRES_ID NOT IN ( " + 
                                                                          " SELECT DISTINCT " +
                                                                          " MAIL_ADRES_ID " +
                                                                          " FROM TEKLIF_SATIR_YANIT " +
                                                                          " WHERE KULLANIM_DURUMU = 1 " + 
                                                                          " AND TEKLIF_SATIR_ID IN (" + teklifIdListesi + ")" +
                                                                          " ) " +
                                            " ) "  ;
              try 
                {       
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.executeUpdate();     
                    
                    preparedStatement.close();
                    dbConnection.baglantiKapat();
                }
              
                catch (Exception ex) 
                {
                    System.out.println(ex);
                }
                finally
                {                    
                    if(preparedStatement!=null)
                    { 
                      preparedStatement.close();
                    }   
                    dbConnection.baglantiKapat();
                }        
    }
    
    public List<MAIL_ADRES> getirMailAdreslerTedarikci(int mailId) throws Exception
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
                mailAdres.setMAIL_ADRES_ID(resultSet.getInt("MAIL_ADRES_ID"));
                mailAdres.setMUSTERI_RECORD_ID(resultSet.getString("MUSTERI_RECORD_ID")); 

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
    
    public int getirMailIdTeklifBaslikIdIleTedarikci(int teklifBaslikId) throws Exception
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
    
    public double getirSonMaliyetTedarikci(String musteriRecordId, String malzemeKodu, String ERPFirmaNo) throws Exception
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
    
    public TEKLIF_SATIR getirTeklifSatirIdIleTedarikci(Integer teklifSatirId) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT * "
                     + " FROM TEKLIF_SATIR "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     + " AND TEKLIF_SATIR_ID = ? " ;
        
        preparedStatement = connection.prepareStatement(query);
        TEKLIF_SATIR teklifSatir = new TEKLIF_SATIR(); 
        
        try 
        {            
            preparedStatement.setString(1, teklifSatirId.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {
                teklifSatir.setTEKLIF_SATIR_ID(resultSet.getInt("TEKLIF_SATIR_ID"));
                teklifSatir.setMALZEME_HIZMET_MASRAF_KODU(resultSet.getString("MALZEME_HIZMET_MASRAF_KODU")); 
                teklifSatir.setMALZEME_HIZMET_MASRAF_MIKTARI(resultSet.getDouble("MALZEME_HIZMET_MASRAF_MIKTARI"));
                teklifSatir.setMALZEME_HIZMET_MASRAF_BIRIM_KODU(resultSet.getString("MALZEME_HIZMET_MASRAF_BIRIM_KODU"));
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
            return teklifSatir;
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
    
    public List<SATISTAN_TEDARIKCI_KAYITLARI> getirSatistanTedarikciKayitlariSatirBazinda(int teklifSatirId) 
                                                                                          throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        List<SATISTAN_TEDARIKCI_KAYITLARI> satistanTedarikciKayitlariList = new ArrayList<>();
        
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        
        String query = " SELECT * "
                     + " FROM SATISTAN_TEDARIKCI_KAYITLARI "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     + " AND TEKLIF_SATIR_ID = ? " ;
        
        String query2 = " SELECT * "
                      + " FROM TEKLIF_SATIR_YANIT "
                      + " WHERE KULLANIM_DURUMU = 1 "
                      + " AND TEKLIF_SATIR_YANIT_ID = ? " ;
        
        String query3 = " SELECT * "
                      + " FROM MAIL_ADRES "
                      + " WHERE KULLANIM_DURUMU = 1 "
                      + " AND MAIL_ADRES_ID = ? " ;
        
        preparedStatement = connection.prepareStatement(query);
        preparedStatement2 = connection.prepareStatement(query2);
        preparedStatement3 = connection.prepareStatement(query3);
        
        
            try 
            {            
                preparedStatement.setInt(1, teklifSatirId);
                ResultSet resultSet = preparedStatement.executeQuery();
                
                while(resultSet.next())
                {
                    //////////////////////////////////////////////////////////////////
                    ///////////SATISTAN_TEDARIKCI_KAYITLARI tablosu verileri
                    
                    SATISTAN_TEDARIKCI_KAYITLARI satistanTedarikciKayitlari= new SATISTAN_TEDARIKCI_KAYITLARI();
                    satistanTedarikciKayitlari.setSATISTAN_TEDARIKCI_KAYITLARI_ID(resultSet.getInt("SATISTAN_TEDARIKCI_KAYITLARI_ID"));                    
                    satistanTedarikciKayitlari.setTEKLIF_SATIR_ID(resultSet.getInt("TEKLIF_SATIR_ID"));
                    satistanTedarikciKayitlari.setTEKLIF_SATIR_YANIT_ID(resultSet.getInt("TEKLIF_SATIR_YANIT_ID"));
                    satistanTedarikciKayitlari.setSIRA_NO(resultSet.getInt("SIRA_NO"));
                    satistanTedarikciKayitlari.setSECILDI_MI(resultSet.getInt("SECILDI_MI"));
                    
                    //////////////////////////////////////////////////////////////////
                    ///////////TEKLIF_SATIR_YANIT tablosu verileri
                    
                    preparedStatement2.setInt(1, satistanTedarikciKayitlari.getTEKLIF_SATIR_YANIT_ID());
                    ResultSet resultSet2 = preparedStatement2.executeQuery();
                    resultSet2.next();              
                    satistanTedarikciKayitlari.setTeklifSatirYanitAciklama(resultSet2.getString("ACIKLAMA")); 
                    satistanTedarikciKayitlari.setTeklifSatirYanitBirimFiyat(resultSet2.getDouble("BIRIM_FIYATI"));
                    satistanTedarikciKayitlari.setTeklifSatirYanitStoktaVarYok(resultSet2.getInt("STOKTA_VAR_YOK"));
                    satistanTedarikciKayitlari.setTeklifSatirYanitMiktar(resultSet2.getDouble("MIKTAR"));
                    satistanTedarikciKayitlari.setTeklifSatirYanitBirim(resultSet2.getString("BIRIM"));
                    satistanTedarikciKayitlari.setTeklifSatirYanitMailAdresId(resultSet2.getInt("MAIL_ADRES_ID"));
                    
                    if(satistanTedarikciKayitlari.getTeklifSatirYanitStoktaVarYok()==0)
                        satistanTedarikciKayitlari.setTeklifSatirYanitStoktaVarYokBoolean(false);
                    else
                        satistanTedarikciKayitlari.setTeklifSatirYanitStoktaVarYokBoolean(true);
                    
                    //////////////////////////////////////////////////////////////////
                    ///////////MAIL_ADRES tablosu verileri
                    
                    preparedStatement3.setInt(1, satistanTedarikciKayitlari.getTeklifSatirYanitMailAdresId());
                    ResultSet resultSet3 = preparedStatement3.executeQuery();
                    resultSet3.next();
                    satistanTedarikciKayitlari.setMailAdresMailAdresi(resultSet3.getString("MAIL_ADRESI")); 
                    satistanTedarikciKayitlari.setMailAdresYanitlayanAdSoyad(resultSet3.getString("YANITLAYAN_AD_SOYAD"));
                    satistanTedarikciKayitlari.setMailAdresMusteriRecordId(resultSet3.getString("MUSTERI_RECORD_ID"));
                    satistanTedarikciKayitlari.setMailAdresParaBirimi(resultSet3.getString("PARA_BIRIMI")); 
                    satistanTedarikciKayitlari.setMailAdresKur(resultSet3.getDouble("KUR"));
                    
                    
                    if(resultSet3.getString("PARA_BIRIMI") != null)
                        if(!resultSet3.getString("PARA_BIRIMI").isEmpty())
                            satistanTedarikciKayitlari.setMailAdresParaBirimi(resultSet3.getString("PARA_BIRIMI")); 
                            //null değil ve boş da değilse atama yap
                        else
                            satistanTedarikciKayitlari.setMailAdresParaBirimi("TL"); //Eğer boş kalmışsa (eski kayıtlar için)
                    else                                                             //Otomatik olarak TL yap...
                        satistanTedarikciKayitlari.setMailAdresParaBirimi("TL"); //Eğer null ise TL atansın...
                    
                    satistanTedarikciKayitlari.setMailAdresTamamlandiMi(resultSet3.getInt("TAMAMLANDI_MI"));
                    
                    if(satistanTedarikciKayitlari.getMailAdresTamamlandiMi()==0)
                        satistanTedarikciKayitlari.setMailAdresTamamlandiMiBoolean(false);
                    else
                        satistanTedarikciKayitlari.setMailAdresTamamlandiMiBoolean(true);
                            
                    
                    //////////////////////////////////////////////////////////////////
                    ///////////MUSTERI tablosu verileri
                    MUSTERI musteri = getirMusteriRecorIdIle(satistanTedarikciKayitlari.getMailAdresMusteriRecordId());                    
                    satistanTedarikciKayitlari.setMusteriMusteriKoduVeUnvani(
                            musteri.getMUSTERI_KODU() + "-" + musteri.getMUSTERI_UNVANI());                
                    
                    satistanTedarikciKayitlariList.add(satistanTedarikciKayitlari);
                }
                
                preparedStatement2.close();
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
                return satistanTedarikciKayitlariList;
            }
    }   
    
    public List<SATISTAN_TEDARIKCI_KAYITLARI> getirSatistanTedarikciKayitlariSatirTumu(String teklifBaslikRecordId) 
                                                                                          throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        List<SATISTAN_TEDARIKCI_KAYITLARI> satistanTedarikciKayitlariList = new ArrayList<>();
        
        PreparedStatement preparedStatement = null;
        
        String query =  " SELECT " +
                        " TS.TEKLIF_SATIR_ID, " +
                        " STK.SATISTAN_TEDARIKCI_KAYITLARI_ID, " +
                        " STK.TEKLIF_SATIR_ID, " +
                        " STK.TEKLIF_SATIR_YANIT_ID, " +
                        " STK.SIRA_NO, " +
                        " STK.SECILDI_MI, " +
                        " TSY.ACIKLAMA, " +
                        " TSY.BIRIM_FIYATI, " +
                        " TSY.STOKTA_VAR_YOK, " +
                        " TSY.MIKTAR, " +
                        " TSY.BIRIM, " +
                        " TSY.MAIL_ADRES_ID, " +
                        " MA.MAIL_ADRESI, " +
                        " MA.YANITLAYAN_AD_SOYAD, " +
                        " MA.MUSTERI_RECORD_ID, " +
                        " MA.PARA_BIRIMI, " +
                        " MA.KUR, " +
                        " MA.TAMAMLANDI_MI, " +
                        " MA.MAIL_ID, " +
                        " MU.MUSTERI_KODU, " +
                        " MU.MUSTERI_UNVANI " +
                        " FROM TEKLIF_SATIR TS " +
                        " LEFT OUTER JOIN SATISTAN_TEDARIKCI_KAYITLARI STK ON STK.TEKLIF_SATIR_ID = TS.TEKLIF_SATIR_ID " +
                                                " AND STK.KULLANIM_DURUMU = 1 " +
                        " LEFT OUTER JOIN TEKLIF_SATIR_YANIT TSY ON TSY.TEKLIF_SATIR_YANIT_ID = STK.TEKLIF_SATIR_YANIT_ID " +
                                                " AND TSY.KULLANIM_DURUMU = 1 " +
                        " LEFT OUTER JOIN MAIL_ADRES MA ON MA.MAIL_ADRES_ID = TSY.MAIL_ADRES_ID " +
                                                " AND MA.KULLANIM_DURUMU = 1 " +
                        " LEFT OUTER JOIN MUSTERI MU ON MA.MUSTERI_RECORD_ID = MU.RECORD_ID " +
                                                " AND MU.KULLANIM_DURUMU = 1 " +
                        " WHERE TS.TEKLIF_BASLIK_RECORD_ID = '" + teklifBaslikRecordId + "' " +
                          " AND TS.KULLANIM_DURUMU = 1 ";
        
        preparedStatement = connection.prepareStatement(query);
        
        
            try 
            {            
                
                ResultSet resultSet = preparedStatement.executeQuery();
                
                while(resultSet.next())
                {
                    SATISTAN_TEDARIKCI_KAYITLARI satistanTedarikciKayitlari= new SATISTAN_TEDARIKCI_KAYITLARI();
                    satistanTedarikciKayitlari.setSATISTAN_TEDARIKCI_KAYITLARI_ID(resultSet.getInt("SATISTAN_TEDARIKCI_KAYITLARI_ID"));                    
                    satistanTedarikciKayitlari.setTEKLIF_SATIR_ID(resultSet.getInt("TEKLIF_SATIR_ID"));
                    satistanTedarikciKayitlari.setTEKLIF_SATIR_YANIT_ID(resultSet.getInt("TEKLIF_SATIR_YANIT_ID"));
                    satistanTedarikciKayitlari.setSIRA_NO(resultSet.getInt("SIRA_NO"));
                    satistanTedarikciKayitlari.setSECILDI_MI(resultSet.getInt("SECILDI_MI"));          
                    satistanTedarikciKayitlari.setTeklifSatirYanitAciklama(resultSet.getString("ACIKLAMA")); 
                    satistanTedarikciKayitlari.setTeklifSatirYanitBirimFiyat(resultSet.getDouble("BIRIM_FIYATI"));
                    satistanTedarikciKayitlari.setTeklifSatirYanitStoktaVarYok(resultSet.getInt("STOKTA_VAR_YOK"));
                    satistanTedarikciKayitlari.setTeklifSatirYanitMiktar(resultSet.getDouble("MIKTAR"));
                    satistanTedarikciKayitlari.setTeklifSatirYanitBirim(resultSet.getString("BIRIM"));
                    satistanTedarikciKayitlari.setTeklifSatirYanitMailAdresId(resultSet.getInt("MAIL_ADRES_ID"));
                    
                    if(satistanTedarikciKayitlari.getTeklifSatirYanitStoktaVarYok()==0)
                        satistanTedarikciKayitlari.setTeklifSatirYanitStoktaVarYokBoolean(false);
                    else
                        satistanTedarikciKayitlari.setTeklifSatirYanitStoktaVarYokBoolean(true);
                    
                    satistanTedarikciKayitlari.setMailAdresMailAdresi(resultSet.getString("MAIL_ADRESI")); 
                    satistanTedarikciKayitlari.setMailAdresYanitlayanAdSoyad(resultSet.getString("YANITLAYAN_AD_SOYAD"));
                    satistanTedarikciKayitlari.setMailAdresMusteriRecordId(resultSet.getString("MUSTERI_RECORD_ID"));
                    satistanTedarikciKayitlari.setMailAdresParaBirimi(resultSet.getString("PARA_BIRIMI")); 
                    satistanTedarikciKayitlari.setMailAdresKur(resultSet.getDouble("KUR"));                    
                    
                    if(resultSet.getString("PARA_BIRIMI") != null)
                        if(!resultSet.getString("PARA_BIRIMI").isEmpty())
                            satistanTedarikciKayitlari.setMailAdresParaBirimi(resultSet.getString("PARA_BIRIMI")); 
                            //null değil ve boş da değilse atama yap
                        else
                            satistanTedarikciKayitlari.setMailAdresParaBirimi("TL"); //Eğer boş kalmışsa (eski kayıtlar için)
                    else                                                             //Otomatik olarak TL yap...
                        satistanTedarikciKayitlari.setMailAdresParaBirimi("TL"); //Eğer null ise TL atansın...
                    
                    satistanTedarikciKayitlari.setMailAdresTamamlandiMi(resultSet.getInt("TAMAMLANDI_MI"));
                    
                    if(satistanTedarikciKayitlari.getMailAdresTamamlandiMi()==0)
                        satistanTedarikciKayitlari.setMailAdresTamamlandiMiBoolean(false);
                    else
                        satistanTedarikciKayitlari.setMailAdresTamamlandiMiBoolean(true);
                    
                    satistanTedarikciKayitlari.setMailAdresMailId(resultSet.getInt("MAIL_ID"));
                    
                    satistanTedarikciKayitlari.setMusteriMusteriKodu(resultSet.getString("MUSTERI_KODU"));
                    satistanTedarikciKayitlari.setMusteriMusteriUnvani(resultSet.getString("MUSTERI_UNVANI"));
                    satistanTedarikciKayitlari.setMusteriMusteriKoduVeUnvani(
                            resultSet.getString("MUSTERI_KODU") + "-" + resultSet.getString("MUSTERI_UNVANI"));                
                    
                    satistanTedarikciKayitlariList.add(satistanTedarikciKayitlari);
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
                return satistanTedarikciKayitlariList;
            }
    }
    
    public void gizleTeklifListesi(int kullaniciId, List<TEKLIF_BASLIK> teklifListesi) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String teklifIdListesi = "";
        for (TEKLIF_BASLIK teklifBaslik : teklifListesi)
            teklifIdListesi = teklifIdListesi + ",'" + teklifBaslik.getTEKLIF_BASLIK_ID() + "'";

        //Birinci sıradaki virgülü silmek için...
        teklifIdListesi = teklifIdListesi.substring(1);
        
        String query = " UPDATE  "
                     + " TEKLIF_BASLIK "
                     + " SET KULLANIM_DURUMU = 2, "
                     + " GUNCELLEYEN_ID =  " + kullaniciId
                     + " WHERE TEKLIF_BASLIK_ID IN (" + teklifIdListesi + ") " ;
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

    public List<TEKLIF_BASLIK> getirTeklifBasliklariGizlenen(String erpFirmaNumber) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT * "
                     + " FROM TEKLIF_BASLIK TB "
                     + " LEFT OUTER JOIN MUSTERI M ON TB.MUSTERI_RECORD_ID = M.RECORD_ID "
                                 + " AND M.KULLANIM_DURUMU = 1 "
                     + " WHERE TB.KULLANIM_DURUMU = 2 "
                     + " AND TB.SATIS_SATINALMA = 0 " //Satış olunca 0, satın alma olunca 1 olacak...
                     + " AND TB.ERP_FIRMA_NUMBER = ? " 
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
                teklifBaslik.setMusteriUnvani(resultSet.getString("MUSTERI_UNVANI"));
                teklifBaslik.setMusteriMail1(resultSet.getString("MUSTERI_MAIL_ADRESI1"));
                teklifBaslik.setMusteriMail2(resultSet.getString("MUSTERI_MAIL_ADRESI2"));  
                teklifBaslik.setMusteriKodu(resultSet.getString("MUSTERI_KODU"));
                
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
    
    public void geriGetirTeklifListesi(int kullaniciId, List<TEKLIF_BASLIK> teklifListesi) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String teklifIdListesi = "";
        for (TEKLIF_BASLIK teklifBaslik : teklifListesi)
            teklifIdListesi = teklifIdListesi + ",'" + teklifBaslik.getTEKLIF_BASLIK_ID() + "'";

        //Birinci sıradaki virgülü silmek için...
        teklifIdListesi = teklifIdListesi.substring(1);
        
        String query = " UPDATE  "
                     + " TEKLIF_BASLIK "
                     + " SET KULLANIM_DURUMU = 1, "
                     + " GUNCELLEYEN_ID =  " + kullaniciId
                     + " WHERE TEKLIF_BASLIK_ID IN (" + teklifIdListesi + ") " ;
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
    
    public void kaydetSatisTeklifi(TEKLIF_BASLIK teklifBaslik, List<TEKLIF_SATIR> teklifSatirlari,int kullaniciId) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        
        String queryBaslik =  " UPDATE  "
                            + " TEKLIF_BASLIK "
                            + " SET GUNCELLEYEN_ID =  " + kullaniciId
                            + " ,TEKLIF_TUTARI =  ? "
                            + " ,MUSTERI_MARJ = ? "
                            + " WHERE TEKLIF_BASLIK_ID = ? " ;
        try 
        {            
            preparedStatement = connection.prepareStatement(queryBaslik);
            preparedStatement.setDouble(1, teklifBaslik.getTEKLIF_TUTARI());
            preparedStatement.setDouble(2, teklifBaslik.getMUSTERI_MARJ());
            preparedStatement.setInt(3, teklifBaslik.getTEKLIF_BASLIK_ID());
            preparedStatement.executeUpdate();
            
            String querySatir =  " UPDATE  "
                                + " TEKLIF_SATIR "
                                + " SET GUNCELLEYEN_ID =  " + kullaniciId
                                + " ,MALZEME_HIZMET_MASRAF_BIRIM_FIYATI =  ? "
                                + " ,MALZEME_HIZMET_MASRAF_TUTARI =  ? "
                                + " ,SECILEN_TEDARIKCI_ID =  ? "
                                + " ,MUSTERI_MARJ =  ? "
                                + " ,MALIYET_BIRIM_FIYAT =  ? "
                                + " ,MALZEME_HIZMET_MASRAF_KODU =  ? "
                                + " WHERE TEKLIF_SATIR_ID = ? " ;
                        
            for(TEKLIF_SATIR  teklifSatiri : teklifSatirlari)
            {
                preparedStatement2 = connection.prepareStatement(querySatir);
                preparedStatement2.setDouble(1, teklifSatiri.getMALZEME_HIZMET_MASRAF_BIRIM_FIYATI());
                preparedStatement2.setDouble(2, teklifSatiri.getMALZEME_HIZMET_MASRAF_TUTARI());
                MUSTERI musteri = getirMusteriMusteriKodIle(teklifSatiri.getSecilenTedarikciKodu());  
                if(musteri == null)
                    preparedStatement2.setNull(3,  java.sql.Types.INTEGER);
                else
                    preparedStatement2.setInt(3, musteri.getMUSTERI_ID());
                
                preparedStatement2.setDouble(4, teklifSatiri.getMUSTERI_MARJ());
                preparedStatement2.setDouble(5, teklifSatiri.getMALIYET_BIRIM_FIYAT());
                preparedStatement2.setString(6, teklifSatiri.getMALZEME_HIZMET_MASRAF_KODU());
                preparedStatement2.setInt(7, teklifSatiri.getTEKLIF_SATIR_ID());
                preparedStatement2.executeUpdate();
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
            if(preparedStatement2!=null)
            { 
              preparedStatement2.close();
            }  
        }
    }
    
}
