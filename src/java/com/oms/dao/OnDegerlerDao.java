/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.dao;
import com.oms.models.ERP_FIRMA;
import com.oms.models.FIRMA;
import com.oms.models.KULLANICI;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ersan
 */
public class OnDegerlerDao {
    
    public List<KULLANICI> getirKullanicilar() throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();        
        
        //DbConnection dbConnectionERP = new DbConnection();
        Connection connectionERP = dbConnection.baglantiAcERP();
        
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        
        String queryKullanicilar = " SELECT * "
                                 + " FROM KULLANICI "
                                 + " WHERE KULLANIM_DURUMU = 1 ";
        
        preparedStatement = connection.prepareStatement(queryKullanicilar);
        List<KULLANICI> kullanicilar=new ArrayList<>();
        
        try 
        {
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {
                KULLANICI kullanici=new KULLANICI();                
                kullanici.setKULLANICI_ID(resultSet.getInt("KULLANICI_ID"));                 
                kullanici.setKULLANICI_ADI(resultSet.getString("KULLANICI_ADI")); 
                kullanici.setSIFRE(resultSet.getString("SIFRE")); 
                kullanici.setAD(resultSet.getString("AD")); 
                kullanici.setSOYAD(resultSet.getString("SOYAD")); 
                kullanici.setMAIL_ADRESI(resultSet.getString("MAIL_ADRESI"));
                kullanici.setON_DEGER_FIRMA_ID(resultSet.getInt("ON_DEGER_FIRMA_ID"));
                kullanici.setERP_ON_DEGER_FIRMA_NUMBER(resultSet.getString("ERP_ON_DEGER_FIRMA_NUMBER"));
                kullanici.setSON_GIRIS_ZAMANI(resultSet.getDate("SON_GIRIS_ZAMANI"));
                kullanici.setBASARISIZ_DENEME_SAYISI(resultSet.getInt("BASARISIZ_DENEME_SAYISI"));
                
                if(resultSet.getInt("AKTIF_PASIF_TUTAR")==0)
                    kullanici.setAktifPasifTutarBoolean(false);
                else
                    kullanici.setAktifPasifTutarBoolean(true);
                
                //Kullanıcının firmasının çekildiği yer (Modele dönştürüp çekiyoruz...)
                ////////////////////////////////////////////////////////////////
                String queryKullaniciFirma = " SELECT * "
                                           + " FROM FIRMA "
                                           + " WHERE KULLANIM_DURUMU = 1 "
                                             + " AND FIRMA_ID = " + kullanici.getON_DEGER_FIRMA_ID();
                
                preparedStatement2 = connection.prepareStatement(queryKullaniciFirma);
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                resultSet2.next();
                FIRMA firma = new FIRMA(); 
                firma.setFIRMA_ID(resultSet2.getInt("FIRMA_ID"));
                firma.setFIRMA_UNVANI(resultSet2.getString("FIRMA_UNVANI"));
                
                preparedStatement2.close();  
                
                ////////////////////////////////////////////////////////////////  
                //ERP firmasının unvanını ERP veri tabanından çekip basıyoruz...
                ////////////////////////////////////////////////////////////////
                String queryERPFirmaUnvan =  " SELECT NAME "
                                           + " FROM L_CAPIFIRM "
                                           + " WHERE NR = " + kullanici.getERP_ON_DEGER_FIRMA_NUMBER();                
                
                preparedStatement3 = connectionERP.prepareStatement(queryERPFirmaUnvan);
                ResultSet resultSet3 = preparedStatement3.executeQuery();
                resultSet3.next();
                ERP_FIRMA erpFirma = new ERP_FIRMA();                 
                erpFirma.setERP_FIRMA_UNVANI(resultSet3.getString("NAME"));
                erpFirma.setERP_FIRMA_NUMBER(kullanici.getERP_ON_DEGER_FIRMA_NUMBER());
                
                preparedStatement3.close();  
                
                ////////////////////////////////////////////////////////////////  
                kullanici.setERP_ON_DEGER_FIRMA(erpFirma);
                kullanici.setON_DEGER_FIRMA(firma); 
                
                kullanicilar.add(kullanici);
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
            return kullanicilar;
        }
    }
        
    public List<FIRMA> getirFirmalar() throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT * "
                     + " FROM FIRMA "
                     + " WHERE KULLANIM_DURUMU = 1 ";
        
        preparedStatement = connection.prepareStatement(query);
        List<FIRMA> firmalar = new ArrayList<>();
        
        try 
        {            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {
                FIRMA firma = new FIRMA();                          
                firma.setFIRMA_ID(resultSet.getInt("FIRMA_ID"));                 
                firma.setFIRMA_UNVANI(resultSet.getString("FIRMA_UNVANI")); 
                firma.setFIRMA_VKNTCKN(resultSet.getString("FIRMA_VKNTCKN")); 
                firma.setFIRMA_VERGI_DAIRESI(resultSet.getString("FIRMA_VERGI_DAIRESI")); 
                firma.setFIRMA_ADRESI(resultSet.getString("FIRMA_ADRESI")); 
                firma.setFIRMA_IL(resultSet.getString("FIRMA_IL")); 
                firma.setFIRMA_ILCE(resultSet.getString("FIRMA_ILCE")); 
                firma.setFIRMA_MAIL_ADRESI1(resultSet.getString("FIRMA_MAIL_ADRESI1")); 
                firma.setFIRMA_MAIL_ADRESI2(resultSet.getString("FIRMA_MAIL_ADRESI2"));
                firma.setFIRMA_TELEFON1(resultSet.getString("FIRMA_TELEFON1"));
                firma.setFIRMA_TELEFON2(resultSet.getString("FIRMA_TELEFON2"));                 
                firma.setFIRMA_FAX(resultSet.getString("FIRMA_FAX")); 
                firmalar.add(firma);
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
            return firmalar;
        }
    }
    
    public List<FIRMA> getirKullaniciFirmalari() throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String queryFirmalar = " SELECT * "
                                 + " FROM FIRMA "
                                 + " WHERE KULLANIM_DURUMU = 1 ";
        
        preparedStatement = connection.prepareStatement(queryFirmalar);
        List<FIRMA> firmalar = new ArrayList<>();
        
        try 
        {            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {
                FIRMA firma = new FIRMA();                
                firma.setFIRMA_ID(resultSet.getInt("FIRMA_ID"));                 
                firma.setFIRMA_UNVANI(resultSet.getString("FIRMA_UNVANI"));
                firmalar.add(firma);
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
            return firmalar;
        }
    }
    
    public List<ERP_FIRMA> getirERPFirmalari() throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connectionERP = dbConnection.baglantiAcERP();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT "
                     + " NR, NAME "
                     + " FROM L_CAPIFIRM ";
        
        preparedStatement = connectionERP.prepareStatement(query);
        List<ERP_FIRMA> ERPFirmalar = new ArrayList<>();
        
        try 
        {            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {
                ERP_FIRMA ERPFirma = new ERP_FIRMA();                
                ERPFirma.setERP_FIRMA_NUMBER(resultSet.getString("NR"));                 
                ERPFirma.setERP_FIRMA_UNVANI(resultSet.getString("NAME"));
                ERPFirmalar.add(ERPFirma);
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
            return ERPFirmalar;
        }
    }
    
    public void kaydetYeniKullanici(KULLANICI kullanici, int olusturanId) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        PreparedStatement preparedStatement = null;
        String query = " INSERT  "
                     + " INTO KULLANICI "
                     + " (KULLANICI_ADI,SIFRE,AD,SOYAD,ON_DEGER_FIRMA_ID,"
                     + " ERP_ON_DEGER_FIRMA_NUMBER,KULLANIM_DURUMU,OLUSTURMA_TARIHI,OLUSTURAN_ID,MAIL_ADRESI,AKTIF_PASIF_TUTAR) "
                     + " VALUES(?,?,?,?,?,?,?,?,?,?,?)" ;
        
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, kullanici.getKULLANICI_ADI());
            preparedStatement.setString(2, kullanici.getSIFRE());
            preparedStatement.setString(3, kullanici.getAD());
            preparedStatement.setString(4, kullanici.getSOYAD());
            preparedStatement.setInt(5, kullanici.getON_DEGER_FIRMA_ID());
            preparedStatement.setString(6, kullanici.getERP_ON_DEGER_FIRMA_NUMBER());
            preparedStatement.setInt(7, 1);
            preparedStatement.setTimestamp(8, new java.sql.Timestamp(new java.util.Date().getTime()));
            preparedStatement.setInt(9, olusturanId);
            preparedStatement.setString(10, kullanici.getMAIL_ADRESI());
            
            //Tutar alanlarını görecek mi?
            if(kullanici.isAktifPasifTutarBoolean())
                preparedStatement.setInt(11, 1);
            else
                preparedStatement.setInt(11, 0);
                
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
    
    public void kaydetYeniFirma(FIRMA firma, int olusturanId) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        PreparedStatement preparedStatement = null;
        String query = " INSERT  "
                     + " INTO FIRMA "
                     + " (FIRMA_UNVANI,"
                     + " FIRMA_VKNTCKN, "
                     + " FIRMA_VERGI_DAIRESI, "
                     + " FIRMA_ADRESI, "
                     + " FIRMA_IL, "
                     + " FIRMA_ILCE, "
                     + " FIRMA_MAIL_ADRESI1, "
                     + " FIRMA_MAIL_ADRESI2,"
                     + " FIRMA_TELEFON1, "
                     + " FIRMA_TELEFON2, "
                     + " FIRMA_FAX, "
                     + " KULLANIM_DURUMU, "
                     + " OLUSTURMA_TARIHI, "
                     + " OLUSTURAN_ID) "
                     + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)" ;
                                              
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, firma.getFIRMA_UNVANI());
            preparedStatement.setString(2, firma.getFIRMA_VKNTCKN());
            preparedStatement.setString(3, firma.getFIRMA_VERGI_DAIRESI());
            preparedStatement.setString(4, firma.getFIRMA_ADRESI());
            preparedStatement.setString(5, firma.getFIRMA_IL());
            preparedStatement.setString(6, firma.getFIRMA_ILCE());
            preparedStatement.setString(7, firma.getFIRMA_MAIL_ADRESI1());
            preparedStatement.setString(8, firma.getFIRMA_MAIL_ADRESI2());
            preparedStatement.setString(9, firma.getFIRMA_TELEFON1());
            preparedStatement.setString(10, firma.getFIRMA_TELEFON2());
            preparedStatement.setString(11, firma.getFIRMA_FAX());
            preparedStatement.setInt(12, 1);
            preparedStatement.setTimestamp(13, new java.sql.Timestamp(new java.util.Date().getTime()));
            preparedStatement.setInt(14, olusturanId);
            
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
    
    public void guncelleKullanici(KULLANICI kullanici, int guncelleyenId)throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        PreparedStatement preparedStatement = null;
        
        String query = " UPDATE  "
                     + " KULLANICI "
                     + " SET KULLANICI_ADI = ?, "
                     + "     SIFRE = ?, "
                     + "     AD = ?, "
                     + "     SOYAD = ?, "
                     + "     ON_DEGER_FIRMA_ID = ?, "
                     + "     ERP_ON_DEGER_FIRMA_NUMBER = ?, "
                     + "     GUNCELLEME_TARIHI = ?, "
                     + "     GUNCELLEYEN_ID = ?, "
                     + "     MAIL_ADRESI = ?, "
                     + "     AKTIF_PASIF_TUTAR = ? "
                     + " WHERE KULLANICI_ID = ? " ;
        
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, kullanici.getKULLANICI_ADI());
            preparedStatement.setString(2, kullanici.getSIFRE());
            preparedStatement.setString(3, kullanici.getAD());
            preparedStatement.setString(4, kullanici.getSOYAD());
            preparedStatement.setInt(5, kullanici.getON_DEGER_FIRMA().getFIRMA_ID());
            preparedStatement.setString(6, kullanici.getERP_ON_DEGER_FIRMA().getERP_FIRMA_NUMBER());
            preparedStatement.setTimestamp(7, new java.sql.Timestamp(new java.util.Date().getTime()));
            preparedStatement.setInt(8, guncelleyenId);
            preparedStatement.setString(9, kullanici.getMAIL_ADRESI());
            
            //Tutar aktif/pasif alanlarının eklenmesi...
            if(kullanici.isAktifPasifTutarBoolean())
                preparedStatement.setInt(10, 1);
            else
                preparedStatement.setInt(10, 0);
            
            preparedStatement.setInt(11, kullanici.getKULLANICI_ID());
            
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
    
    public void guncelleFirma(FIRMA firma, int guncelleyenId)throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        PreparedStatement preparedStatement = null;
        String query = " UPDATE  "
                     + " FIRMA "
                     + " SET FIRMA_UNVANI = ?, "
                     + "     FIRMA_VKNTCKN = ?, "
                     + "     FIRMA_VERGI_DAIRESI = ?, "
                     + "     FIRMA_ADRESI = ?, "
                     + "     FIRMA_IL = ?, "
                     + "     FIRMA_ILCE = ?, "
                     + "     FIRMA_MAIL_ADRESI1 = ?, "
                     + "     FIRMA_MAIL_ADRESI2 = ?, "
                     + "     FIRMA_TELEFON1 = ?, "
                     + "     FIRMA_TELEFON2 = ?, "
                     + "     FIRMA_FAX = ?, "
                     + "     GUNCELLEME_TARIHI = ?, "
                     + "     GUNCELLEYEN_ID = ? "
                     + " WHERE FIRMA_ID = ? " ;         
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, firma.getFIRMA_UNVANI());
            preparedStatement.setString(2, firma.getFIRMA_VKNTCKN());
            preparedStatement.setString(3, firma.getFIRMA_VERGI_DAIRESI());
            preparedStatement.setString(4, firma.getFIRMA_ADRESI());
            preparedStatement.setString(5, firma.getFIRMA_IL());
            preparedStatement.setString(6, firma.getFIRMA_ILCE());
            preparedStatement.setString(7, firma.getFIRMA_MAIL_ADRESI1());
            preparedStatement.setString(8, firma.getFIRMA_MAIL_ADRESI2());
            preparedStatement.setString(9, firma.getFIRMA_TELEFON1());
            preparedStatement.setString(10, firma.getFIRMA_TELEFON2());
            preparedStatement.setString(11, firma.getFIRMA_FAX());
            preparedStatement.setTimestamp(12, new java.sql.Timestamp(new java.util.Date().getTime()));
            preparedStatement.setInt(13, guncelleyenId);
            preparedStatement.setInt(14, firma.getFIRMA_ID());
            
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
    
    public void silKullanici(KULLANICI kullanici, int guncelleyenId)throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        PreparedStatement preparedStatement = null;
        String query = " UPDATE  "
                     + " KULLANICI "
                     + " SET KULLANIM_DURUMU = 0, "
                     + "     GUNCELLEME_TARIHI = ?, "
                     + "     GUNCELLEYEN_ID = ? "
                     + " WHERE KULLANICI_ID = ? " ;
        
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setTimestamp(1, new java.sql.Timestamp(new java.util.Date().getTime()));
            preparedStatement.setInt(2, guncelleyenId);
            preparedStatement.setInt(3, kullanici.getKULLANICI_ID());
            
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
    
    public void silFirma(FIRMA firma, int guncelleyenId)throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        PreparedStatement preparedStatement = null;
        String query = " UPDATE  "
                     + " FIRMA "
                     + " SET KULLANIM_DURUMU = 0, "
                     + "     GUNCELLEME_TARIHI = ?, "
                     + "     GUNCELLEYEN_ID = ? "
                     + " WHERE FIRMA_ID = ? " ;
        
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setTimestamp(1, new java.sql.Timestamp(new java.util.Date().getTime()));
            preparedStatement.setInt(2, guncelleyenId);
            preparedStatement.setInt(3, firma.getFIRMA_ID());
            
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
