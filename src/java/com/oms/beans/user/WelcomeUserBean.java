/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.beans.user;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author ersan
 */
@ManagedBean(name="welcomeUserBean")
@SessionScoped
public class WelcomeUserBean {

    public WelcomeUserBean() {
    }
    
    private static Map<String,Object> countries;
    private String localeCode="tr";
    
    @PostConstruct
    public void init() 
    {
        countries = new LinkedHashMap<String,Object>();
        countries.put("Türkçe", new Locale("tr",""));
        countries.put("English", new Locale("en","")); 
	countries.put("Chinese", new Locale("zh",""));
        
        if(localeCode.equals("tr"))
            FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("tr",""));
        else if(localeCode.equals("en"))
            FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("en",""));
    }
    
    public Map<String, Object> getCountriesInMap() {
		return countries;
    }
    
    public String getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }    
    
    public void countryLocaleCodeChanged() 
    {
       String newLocaleValue = localeCode;
        //loop country map to compare the locale code
        for (Map.Entry<String, Object> entry : countries.entrySet()) 
        {        
           if(entry.getValue().toString().equals(newLocaleValue))
           {        		
                FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale)entry.getValue());        		
           }
       }
    }
    
}
