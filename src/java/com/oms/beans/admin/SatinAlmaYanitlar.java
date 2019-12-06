/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.beans.admin;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author ersan
 */
@ManagedBean(name="satinAlmaYanitlar", eager = true)
@SessionScoped
public class SatinAlmaYanitlar implements Serializable{
    /**
     * Creates a new instance of satinAlmaYanitlar
     */
    public SatinAlmaYanitlar() {
    }
    
    private static final long serialVersionUID = 1L;
     
    @ManagedProperty(value="#{sessionBean}")
    private SatinAlmaBean satinAlmaBean;  
    
    @PostConstruct
    public void init() 
    {        
        try 
        {
            
        } 
        catch (Exception ex) 
        {            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sayfa başlatılırken hata oluştu.", ""));
        }
    }
    
}
