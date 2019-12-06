/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.models;

/**
 * Mail gönderirirken adresleri tutabilmek için
 * sanal model oluşturulmuştur...
 * @author ersan
 */
public class MailList {
    
    private String mailNumber;
    private String mailAdress;

    public String getMailNumber() {
        return mailNumber;
    }

    public void setMailNumber(String mailNumber) {
        this.mailNumber = mailNumber;
    }

    public String getMailAdress() {
        return mailAdress;
    }

    public void setMailAdress(String mailAdress) {
        this.mailAdress = mailAdress;
    }
    
    
}
