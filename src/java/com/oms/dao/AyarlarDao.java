/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.dao;

import com.oms.models.EMAIL_AYAR;
import com.oms.models.REST_SERVIS_AYAR;
import com.oms.models.VERI_TABANI_AYAR;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author ersan
 */
public class AyarlarDao {
    
    
    public void kaydetVeriTabaniAyar(VERI_TABANI_AYAR veriTabaniAyar, int ERPvsOMS, int olusturanId) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        PreparedStatement preparedStatement = null;
        
        String query = " INSERT  "
                     + " INTO VERI_TABANI_AYAR "
                     + " (SUNUCU_ADI,SUNUCU_PORT_NUMARASI,VERI_TABANI_ADI,KULLANICI_ADI,"
                     + " KULLANICI_SIFRE,VERI_TABANI_TUR,KULLANIM_DURUMU,OLUSTURMA_TARIHI,OLUSTURAN_ID) "
                     + " VALUES(?,?,?,?,?,?,?,?,?)" ;
        
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, veriTabaniAyar.getSUNUCU_ADI());
            preparedStatement.setString(2, veriTabaniAyar.getSUNUCU_PORT_NUMARASI());
            preparedStatement.setString(3, veriTabaniAyar.getVERI_TABANI_ADI());
            preparedStatement.setString(4, veriTabaniAyar.getKULLANICI_ADI());
            preparedStatement.setString(5, veriTabaniAyar.getKULLANICI_SIFRE());
            preparedStatement.setInt(6, ERPvsOMS);
            preparedStatement.setInt(7, 1);
            preparedStatement.setTimestamp(8, new java.sql.Timestamp(new java.util.Date().getTime()));
            preparedStatement.setInt(9, olusturanId);
            
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
    
    public void kaydetEmailAyar(EMAIL_AYAR emailAyar, int olusturanId) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        PreparedStatement preparedStatement = null;
        
        String query = " INSERT  "
                     + " INTO EMAIL_AYAR "
                     + " (EMAIL_SUNUCU_ADI,EMAIL_SUNUCU_PORT_NUMARASI,"
                     + " EMAIL_ADRES,EMAIL_SIFRE,KULLANIM_DURUMU,OLUSTURMA_TARIHI,OLUSTURAN_ID)"
                     + " VALUES(?,?,?,?,?,?,?)" ;      
        
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, emailAyar.getEMAIL_SUNUCU_ADI());
            preparedStatement.setString(2, emailAyar.getEMAIL_SUNUCU_PORT_NUMARASI());
            preparedStatement.setString(3, emailAyar.getEMAIL_ADRES());
            preparedStatement.setString(4, emailAyar.getEMAIL_SIFRE());
            preparedStatement.setInt(5, 1);
            preparedStatement.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
            preparedStatement.setInt(7, olusturanId);
            
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
    
    public void kaydetRestServisAyar(REST_SERVIS_AYAR restServisAyar, int olusturanId) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        PreparedStatement preparedStatement = null;
        
        String query = " INSERT  "
                     + " INTO REST_SERVIS_AYAR "
                     + " (REST_SERVIS_SUNUCU_ADI_IP, "
                     + " REST_SERVIS_SUNUCU_PORT, "
                     + " REST_SERVIS_CLIENT_ID,"
                     + " REST_SERVIS_CLIENT_SECRET, "
                     + " REST_SERVIS_USERNAME, "
                     + " REST_SERVIS_PASSWORD, "
                     + " REST_SERVIS_FIRMNO, "
                     + " KULLANIM_DURUMU, "
                     + " OLUSTURMA_TARIHI, "
                     + " OLUSTURAN_ID)"
                     + " VALUES(?,?,?,?,?,?,?,?,?,?)" ;      
        
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, restServisAyar.getREST_SERVIS_SUNUCU_ADI_IP());
            preparedStatement.setString(2, restServisAyar.getREST_SERVIS_SUNUCU_PORT());
            preparedStatement.setString(3, restServisAyar.getREST_SERVIS_CLIENT_ID());
            preparedStatement.setString(4, restServisAyar.getREST_SERVIS_CLIENT_SECRET());
            preparedStatement.setString(5, restServisAyar.getREST_SERVIS_USERNAME());
            preparedStatement.setString(6, restServisAyar.getREST_SERVIS_PASSWORD());
            preparedStatement.setString(7, restServisAyar.getREST_SERVIS_FIRMNO());
            preparedStatement.setInt(8, 1);
            preparedStatement.setTimestamp(9, new java.sql.Timestamp(new java.util.Date().getTime()));
            preparedStatement.setInt(10, olusturanId);
            
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
    
    public void guncelleEmailAyar(EMAIL_AYAR emailAyar, int guncelleyenId)throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        PreparedStatement preparedStatement = null;
        
        String query = " UPDATE  "
                     + " EMAIL_AYAR "
                     + " SET EMAIL_SUNUCU_ADI = ?, "
                     + "     EMAIL_SUNUCU_PORT_NUMARASI = ?, "
                     + "     EMAIL_ADRES = ?, "
                     + "     EMAIL_SIFRE = ?, "
                     + "     GUNCELLEME_TARIHI = ?, "
                     + "     GUNCELLEYEN_ID = ? "
                     + " WHERE EMAIL_AYAR_ID = ? " ;  
        
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, emailAyar.getEMAIL_SUNUCU_ADI());
            preparedStatement.setString(2, emailAyar.getEMAIL_SUNUCU_PORT_NUMARASI());
            preparedStatement.setString(3, emailAyar.getEMAIL_ADRES());
            preparedStatement.setString(4, emailAyar.getEMAIL_SIFRE());
            preparedStatement.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));
            preparedStatement.setInt(6, guncelleyenId);
            preparedStatement.setInt(7, emailAyar.getEMAIL_AYAR_ID());
            
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
    
    public void guncelleRestServisAyar(REST_SERVIS_AYAR restServisAyar, int guncelleyenId)throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        PreparedStatement preparedStatement = null;
        
        String query = " UPDATE  "
                     + " REST_SERVIS_AYAR "
                     + " SET REST_SERVIS_SUNUCU_ADI_IP = ?, "
                     + "     REST_SERVIS_SUNUCU_PORT = ?, "
                     + "     REST_SERVIS_CLIENT_ID = ?, "
                     + "     REST_SERVIS_CLIENT_SECRET = ?, "
                     + "     REST_SERVIS_USERNAME = ?, "
                     + "     REST_SERVIS_PASSWORD = ?, "
                     + "     REST_SERVIS_FIRMNO = ?, "
                     + "     GUNCELLEME_TARIHI = ?, "
                     + "     GUNCELLEYEN_ID = ? "
                     + " WHERE REST_SERVIS_AYAR_ID = ? " ;  
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, restServisAyar.getREST_SERVIS_SUNUCU_ADI_IP());
            preparedStatement.setString(2, restServisAyar.getREST_SERVIS_SUNUCU_PORT());
            preparedStatement.setString(3, restServisAyar.getREST_SERVIS_CLIENT_ID());
            preparedStatement.setString(4, restServisAyar.getREST_SERVIS_CLIENT_SECRET());
            preparedStatement.setString(5, restServisAyar.getREST_SERVIS_USERNAME());
            preparedStatement.setString(6, restServisAyar.getREST_SERVIS_PASSWORD());
            preparedStatement.setString(7, restServisAyar.getREST_SERVIS_FIRMNO());
            preparedStatement.setTimestamp(8, new java.sql.Timestamp(new java.util.Date().getTime()));
            preparedStatement.setInt(9, guncelleyenId);
            preparedStatement.setInt(10, restServisAyar.getREST_SERVIS_AYAR_ID());
            
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
    
    public void guncelleVeriTabaniAyar(VERI_TABANI_AYAR veriTabaniAyar, int guncelleyenId)throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        PreparedStatement preparedStatement = null;
        
        String query = " UPDATE  "
                     + " VERI_TABANI_AYAR "
                     + " SET SUNUCU_ADI = ?, "
                     + "     SUNUCU_PORT_NUMARASI = ?, "
                     + "     VERI_TABANI_ADI = ?, "
                     + "     KULLANICI_ADI = ?, "
                     + "     KULLANICI_SIFRE = ?, "
                     + "     GUNCELLEME_TARIHI = ?, "
                     + "     GUNCELLEYEN_ID = ? "
                     + " WHERE VERI_TABANI_AYAR_ID = ? " ;  
        
        try 
        {            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, veriTabaniAyar.getSUNUCU_ADI());
            preparedStatement.setString(2, veriTabaniAyar.getSUNUCU_PORT_NUMARASI());
            preparedStatement.setString(3, veriTabaniAyar.getVERI_TABANI_ADI());
            preparedStatement.setString(4, veriTabaniAyar.getKULLANICI_ADI());
            preparedStatement.setString(5, veriTabaniAyar.getKULLANICI_SIFRE());
            preparedStatement.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
            preparedStatement.setInt(7, guncelleyenId);
            preparedStatement.setInt(8, veriTabaniAyar.getVERI_TABANI_AYAR_ID());
            
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
    
    public VERI_TABANI_AYAR getirVeriTabaniAyar(int ERPvsOMS) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT * "
                     + " FROM VERI_TABANI_AYAR "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     //ERP'de 0, OMS'de 1 olacak...
                     + " AND VERI_TABANI_TUR = " + ERPvsOMS;
        
        preparedStatement = connection.prepareStatement(query);
        VERI_TABANI_AYAR veriTabaniAyar = new VERI_TABANI_AYAR();
        
        try 
        {            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {                     
                veriTabaniAyar.setVERI_TABANI_AYAR_ID(resultSet.getInt("VERI_TABANI_AYAR_ID"));                 
                veriTabaniAyar.setSUNUCU_ADI(resultSet.getString("SUNUCU_ADI")); 
                veriTabaniAyar.setSUNUCU_PORT_NUMARASI(resultSet.getString("SUNUCU_PORT_NUMARASI")); 
                veriTabaniAyar.setVERI_TABANI_ADI(resultSet.getString("VERI_TABANI_ADI")); 
                veriTabaniAyar.setKULLANICI_ADI(resultSet.getString("KULLANICI_ADI")); 
                veriTabaniAyar.setKULLANICI_SIFRE(resultSet.getString("KULLANICI_SIFRE")); 
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
            return veriTabaniAyar;
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
    
    public REST_SERVIS_AYAR getirRestServisAyar() throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        Connection connection = dbConnection.baglantiAc();
        
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT * "
                     + " FROM REST_SERVIS_AYAR "
                     + " WHERE KULLANIM_DURUMU = 1 ";
        
        preparedStatement = connection.prepareStatement(query);
        REST_SERVIS_AYAR restServisAyar = new REST_SERVIS_AYAR();
        
        try 
        {            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {                     
                restServisAyar.setREST_SERVIS_AYAR_ID(resultSet.getInt("REST_SERVIS_AYAR_ID"));                 
                restServisAyar.setREST_SERVIS_SUNUCU_ADI_IP(resultSet.getString("REST_SERVIS_SUNUCU_ADI_IP")); 
                restServisAyar.setREST_SERVIS_SUNUCU_PORT(resultSet.getString("REST_SERVIS_SUNUCU_PORT")); 
                restServisAyar.setREST_SERVIS_CLIENT_ID(resultSet.getString("REST_SERVIS_CLIENT_ID")); 
                restServisAyar.setREST_SERVIS_CLIENT_SECRET(resultSet.getString("REST_SERVIS_CLIENT_SECRET")); 
                restServisAyar.setREST_SERVIS_USERNAME(resultSet.getString("REST_SERVIS_USERNAME")); 
                restServisAyar.setREST_SERVIS_PASSWORD(resultSet.getString("REST_SERVIS_PASSWORD")); 
                restServisAyar.setREST_SERVIS_FIRMNO(resultSet.getString("REST_SERVIS_FIRMNO")); 
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
            return restServisAyar;
        }
    }
}
