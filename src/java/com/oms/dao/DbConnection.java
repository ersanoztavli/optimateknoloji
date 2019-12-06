/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.dao;

import com.oms.beans.admin.SessionBean;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author ersan
 */
@ManagedBean(name="dbConnection", eager = true)
@SessionScoped
public final class DbConnection implements Serializable{

    private static final long serialVersionUID = 1L;
    /**
     * Creates a new instance of DbConnection
     */
    
    private Connection connection = null;
    private Connection connectionERP = null;
    
    //OMS veri tabanı ile ilgili alanlar...
    private String dbHost;
    private String dbPort;
    private String dbName;
    private String dbUser;
    private String dbPassword;
    private String dbConnectionURL;
    
    //ERP veri tabanı ile ilgili alanlar...
    private String dbHostERP;
    private String dbPortERP;
    private String dbNameERP;
    private String dbUserERP;
    private String dbPasswordERP;
    private String dbConnectionURLERP;
    
   
    @ManagedProperty(value="#{sessionBean}")
    private SessionBean sessionBean;

    
    public DbConnection() 
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        sessionBean = (SessionBean)facesContext.getApplication().createValueBinding("#{sessionBean}").getValue(facesContext);
        
         setDbHost(sessionBean.getOMSVeriTabaniAyar().getSUNUCU_ADI());
         setDbPort(sessionBean.getOMSVeriTabaniAyar().getSUNUCU_PORT_NUMARASI());
         setDbName(sessionBean.getOMSVeriTabaniAyar().getVERI_TABANI_ADI());
         setDbUser(sessionBean.getOMSVeriTabaniAyar().getKULLANICI_ADI());
         setDbPassword(sessionBean.getOMSVeriTabaniAyar().getKULLANICI_SIFRE());
         
         //OMS bağlantı ayarlarına göre 
         //ERP bağlantı ayarlarını veri tabanından okuyup eşitliyoruz...
         doldurERPDbParametreler();         
    }
    
    public Connection baglantiAc()
    {
        PreparedStatement ps=null;        
        try 
        {
            //Eğer bağlantı varsa var olan bağlantıyı döndür...
            if (getConnection() != null && !getConnection().isClosed()) 
            {
                    return getConnection();
            }
            //Eğer bağlantı yoksa yeni bağlantı aç...
            else
            {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");            
                setDbConnectionURL("jdbc:sqlserver://" + getDbHost() + ":" + getDbPort() + ";databaseName=" + getDbName());

                setConnection(DriverManager.getConnection(getDbConnectionURL(), getDbUser(), getDbPassword()));

                if (getConnection()!= null) 
                {
                    return getConnection();
                }
                else return null;
            }   
        } 
        catch (Exception ex) 
        {
            return null;
        }
    }
    
    public Connection baglantiAcERP()
    {
        PreparedStatement ps=null;   
        
        try 
        {
            //Eğer bağlantı varsa var olan bağlantıyı döndür...
            if (getConnectionERP()!= null && !getConnectionERP().isClosed()) 
            {
                    return getConnectionERP();
            }
            //Eğer bağlantı yoksa yeni bağlantı aç...
            else
            {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");            
                setDbConnectionURLERP("jdbc:sqlserver://" + getDbHostERP()+ ":" + getDbPortERP()+ ";databaseName=" + getDbNameERP());

                setConnectionERP(DriverManager.getConnection(getDbConnectionURLERP(), getDbUserERP(), getDbPasswordERP()));

                if (getConnectionERP()!= null) 
                {
                    return getConnectionERP();
                }
                else return null;
            }   
        } 
        catch (Exception ex) 
        {
            return null;
        }
    }
    
    public void baglantiKapat()
    {
        try 
        {
            //Eğer bağlantı var ve kapalı değilse kapatma işlemi uygula...
            if (getConnection() != null && !getConnection().isClosed()) 
            {
                getConnection().close();
            }
        } 
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
    }
    
    public void baglantiKapatERP()
    {
        try 
        {
            //Eğer bağlantı var ve kapalı değilse kapatma işlemi uygula...
            if (getConnectionERP()!= null && !getConnectionERP().isClosed()) 
            {
                getConnectionERP().close();
            }
        } 
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
    }
    
    public void doldurERPDbParametreler()
    {
        PreparedStatement preparedStatement = null;
        
        String query = " SELECT * "
                     + " FROM VERI_TABANI_AYAR "
                     + " WHERE KULLANIM_DURUMU = 1 "
                     //ERP'de 0, OMS'de 1 olacak...
                     + " AND VERI_TABANI_TUR = 0";  
        
        try 
        {            
            preparedStatement = baglantiAc().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next())
            {                                     
                setDbHostERP(resultSet.getString("SUNUCU_ADI")); 
                setDbPortERP(resultSet.getString("SUNUCU_PORT_NUMARASI")); 
                setDbNameERP(resultSet.getString("VERI_TABANI_ADI")); 
                setDbUserERP(resultSet.getString("KULLANICI_ADI")); 
                setDbPasswordERP(resultSet.getString("KULLANICI_SIFRE")); 
            }             
        }
        catch (Exception ex) 
        {
            System.out.println(ex);
        }
        finally
        {
            baglantiKapat();   
            if(preparedStatement!=null)
            { 
                try 
                {
                    preparedStatement.close();
                } 
                catch (SQLException ex) 
                {
                    Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }     
        }
    }
    

    
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getDbConnectionURL() {
        return dbConnectionURL;
    }

    public void setDbConnectionURL(String dbConnectionURL) {
        this.dbConnectionURL = dbConnectionURL;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getDbPort() {
        return dbPort;
    }

    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public Connection getConnectionERP() {
        return connectionERP;
    }

    public void setConnectionERP(Connection connectionERP) {
        this.connectionERP = connectionERP;
    }

    public String getDbHostERP() {
        return dbHostERP;
    }

    public void setDbHostERP(String dbHostERP) {
        this.dbHostERP = dbHostERP;
    }

    public String getDbPortERP() {
        return dbPortERP;
    }

    public void setDbPortERP(String dbPortERP) {
        this.dbPortERP = dbPortERP;
    }

    public String getDbNameERP() {
        return dbNameERP;
    }

    public void setDbNameERP(String dbNameERP) {
        this.dbNameERP = dbNameERP;
    }

    public String getDbUserERP() {
        return dbUserERP;
    }

    public void setDbUserERP(String dbUserERP) {
        this.dbUserERP = dbUserERP;
    }

    public String getDbPasswordERP() {
        return dbPasswordERP;
    }

    public void setDbPasswordERP(String dbPasswordERP) {
        this.dbPasswordERP = dbPasswordERP;
    }

    public String getDbConnectionURLERP() {
        return dbConnectionURLERP;
    }

    public void setDbConnectionURLERP(String dbConnectionURLERP) {
        this.dbConnectionURLERP = dbConnectionURLERP;
    }
     
    
    
}
