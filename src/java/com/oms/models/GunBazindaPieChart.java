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
public class GunBazindaPieChart {
    
    private int gunBazindaTip; //0:teklif toplamı , 1: , 2: , 3: 
    private String yil;
    private double pazartesi;
    private double sali;
    private double carsamba;
    private double persembe;
    private double cuma;
    private double cumartesi;
    private double pazar;

    public int getGunBazindaTip() {
        return gunBazindaTip;
    }

    public void setGunBazindaTip(int gunBazindaTip) {
        this.gunBazindaTip = gunBazindaTip;
    }

    

    public double getPazartesi() {
        return pazartesi;
    }

    public void setPazartesi(double pazartesi) {
        this.pazartesi = pazartesi;
    }

    public double getSali() {
        return sali;
    }

    public void setSali(double sali) {
        this.sali = sali;
    }

    public double getCarsamba() {
        return carsamba;
    }

    public void setCarsamba(double carsamba) {
        this.carsamba = carsamba;
    }

    public double getPersembe() {
        return persembe;
    }

    public void setPersembe(double persembe) {
        this.persembe = persembe;
    }

    public double getCuma() {
        return cuma;
    }

    public void setCuma(double cuma) {
        this.cuma = cuma;
    }

    public double getCumartesi() {
        return cumartesi;
    }

    public void setCumartesi(double cumartesi) {
        this.cumartesi = cumartesi;
    }

    public double getPazar() {
        return pazar;
    }

    public void setPazar(double pazar) {
        this.pazar = pazar;
    }   

    public String getYil() {
        return yil;
    }

    public void setYil(String yil) {
        this.yil = yil;
    }       
    
}
