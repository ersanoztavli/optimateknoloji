/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.dao;

import com.oms.models.KULLANICI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author ersan
 */
public class SessionDao {
    
    public KULLANICI getirKullanici(KULLANICI kullanici) throws Exception
    {
        DbConnection dbConnection = new DbConnection();
        DbConnection dbConnectionERP = new DbConnection();
        
        Connection connection = dbConnection.baglantiAc();
        Connection connectionERP = dbConnectionERP.baglantiAcERP();
        
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        
        String query = " SELECT * "
                     + " FROM KULLANICI "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     + " AND KULLANICI_ADI = ? "
                     + " AND SIFRE = ? ";
        
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, kullanici.getKULLANICI_ADI());
        preparedStatement.setString(2, kullanici.getSIFRE());
        
        KULLANICI kayitliKullanici = new KULLANICI();
        
        try 
        {            
            ResultSet resultSet = preparedStatement.executeQuery();            
            resultSet.next();
            
            kayitliKullanici.setKULLANICI_ID(resultSet.getInt("KULLANICI_ID"));  
            kayitliKullanici.setKULLANICI_ADI(resultSet.getString("KULLANICI_ADI"));
            kayitliKullanici.setSIFRE(resultSet.getString("SIFRE")); 
            kayitliKullanici.setAD(resultSet.getString("AD"));
            kayitliKullanici.setSOYAD(resultSet.getString("SOYAD")); 
            kayitliKullanici.setMAIL_ADRESI(resultSet.getString("MAIL_ADRESI")); 
            kayitliKullanici.setON_DEGER_FIRMA_ID(resultSet.getInt("ON_DEGER_FIRMA_ID")); 
            kayitliKullanici.setERP_ON_DEGER_FIRMA_NUMBER(resultSet.getString("ERP_ON_DEGER_FIRMA_NUMBER"));
            kayitliKullanici.setSON_GIRIS_ZAMANI(resultSet.getDate("SON_GIRIS_ZAMANI")); 
            kayitliKullanici.setBASARISIZ_DENEME_SAYISI(resultSet.getInt("BASARISIZ_DENEME_SAYISI")); 
            
            if(resultSet.getInt("AKTIF_PASIF_TUTAR") == 0)
                kayitliKullanici.setAktifPasifTutarBoolean(false);
            else
                kayitliKullanici.setAktifPasifTutarBoolean(true);
            
            /////////////////////////////////////////////////////
            //Ön değer ERP firma bilgisini çekiyoruz...
            
            String query2 =  " SELECT NAME "
                           + " FROM L_CAPIFIRM "
                           + " WHERE NR = " + kayitliKullanici.getERP_ON_DEGER_FIRMA_NUMBER();
            
            preparedStatement2 = connectionERP.prepareStatement(query2);
            
            ResultSet resultSet2 = preparedStatement2.executeQuery();            
            resultSet2.next();
            kayitliKullanici.setERP_ON_DEGER_FIRMA_UNVAN(resultSet2.getString("NAME"));  
            preparedStatement2.close();
            /////////////////////////////////////////////////////
            //Ön değer OMS firma bilgisini çekiyoruz...
            
            String query3 =  " SELECT FIRMA_UNVANI, FIRMA_MAIL_ADRESI1 "
                           + " FROM FIRMA "
                           + " WHERE KULLANIM_DURUMU = 1 "
                           + " AND FIRMA_ID = " + kayitliKullanici.getON_DEGER_FIRMA_ID();
            
            preparedStatement3 = connection.prepareStatement(query3);
            ResultSet resultSet3 = preparedStatement3.executeQuery();
            resultSet3.next();
            kayitliKullanici.setON_DEGER_FIRMA_UNVAN(resultSet3.getString("FIRMA_UNVANI"));  
            kayitliKullanici.setFirmaMailAdresi(resultSet3.getString("FIRMA_MAIL_ADRESI1"));
            preparedStatement3.close();
            
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
            return kayitliKullanici;
        }
    }
    
}
