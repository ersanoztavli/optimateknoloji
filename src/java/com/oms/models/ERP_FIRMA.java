/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.models;

/**
 *
 * @author ersan
 */

//Bu model sanal bir model.
//Veri Tabanında böyle bir model yok. 
//Sadece ERP'deki hangi tabloları çekeceğimizi anlamak için ERP firmalarını çekiyoruz.
public class ERP_FIRMA {
    
    private String ERP_FIRMA_NUMBER;
    private String ERP_FIRMA_UNVANI;

    public String getERP_FIRMA_NUMBER() {
        return ERP_FIRMA_NUMBER;
    }

    public void setERP_FIRMA_NUMBER(String ERP_FIRMA_NUMBER) {
        this.ERP_FIRMA_NUMBER = ERP_FIRMA_NUMBER;
    }

    public String getERP_FIRMA_UNVANI() {
        return ERP_FIRMA_UNVANI;
    }

    public void setERP_FIRMA_UNVANI(String ERP_FIRMA_UNVANI) {
        this.ERP_FIRMA_UNVANI = ERP_FIRMA_UNVANI;
    }
    
    
}
