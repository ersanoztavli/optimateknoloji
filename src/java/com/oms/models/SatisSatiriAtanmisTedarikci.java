/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.models;

import java.util.List;
import javax.servlet.http.Part;

/**
 * Satış faturalarındaki herbir satırın
 * ilgili tedarikçilerini listeleyebilmek sanal model oluşturulmuştur...
 * @author ersan
 */
public class SatisSatiriAtanmisTedarikci {
        
    private String musteriRecordId;
    private List<Integer> teklifSatirIdList;
    
    //Her bir göderilecek tedarikçi için ayrı olacağı için düzenleme yaptık...
    private List<MAIL_DOSYA> mailDosyaList;
    private List<MailList> mailToList;
    private String mailGonderen;
    private String mailKonu;
    private String mailIcerik;
    private Part yuklenecekDosya;

    private String tedarikciUnvan;
            
    public List<Integer> getTeklifSatirIdList() {
        return teklifSatirIdList;
    }

    public void setTeklifSatirIdList(List<Integer> teklifSatirIdList) {
        this.teklifSatirIdList = teklifSatirIdList;
    }    

    public String getMusteriRecordId() {
        return musteriRecordId;
    }

    public void setMusteriRecordId(String musteriRecordId) {
        this.musteriRecordId = musteriRecordId;
    }

    public List<MAIL_DOSYA> getMailDosyaList() {
        return mailDosyaList;
    }

    public void setMailDosyaList(List<MAIL_DOSYA> mailDosyaList) {
        this.mailDosyaList = mailDosyaList;
    }

    public List<MailList> getMailToList() {
        return mailToList;
    }

    public void setMailToList(List<MailList> mailToList) {
        this.mailToList = mailToList;
    }

    public String getMailGonderen() {
        return mailGonderen;
    }

    public void setMailGonderen(String mailGonderen) {
        this.mailGonderen = mailGonderen;
    }

    public String getMailKonu() {
        return mailKonu;
    }

    public void setMailKonu(String mailKonu) {
        this.mailKonu = mailKonu;
    }

    public String getMailIcerik() {
        return mailIcerik;
    }

    public void setMailIcerik(String mailIcerik) {
        this.mailIcerik = mailIcerik;
    }    

    public Part getYuklenecekDosya() {
        return yuklenecekDosya;
    }

    public void setYuklenecekDosya(Part yuklenecekDosya) {
        this.yuklenecekDosya = yuklenecekDosya;
    }

    public String getTedarikciUnvan() {
        return tedarikciUnvan;
    }

    public void setTedarikciUnvan(String tedarikciUnvan) {
        this.tedarikciUnvan = tedarikciUnvan;
    }
    
}
