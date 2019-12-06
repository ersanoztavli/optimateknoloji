/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oms.beans.admin;

import com.oms.dao.SatisDao;
import com.oms.models.ERPItem;
import com.oms.models.TEKLIF_BASLIK;
import com.oms.models.TEKLIF_SATIR;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ersan
 */
@ManagedBean(name="excelSatisTeklifiBean", eager = true)
@SessionScoped
public class ExcelSatisTeklifiBean implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private TEKLIF_BASLIK exceldenYuklenecekTeklifBaslik;
    private List<TEKLIF_SATIR> exceldenYuklenecekTeklifSatirlari;
    private List<String> birimListesi;
    private List<String> paraBirimiListesi;
    private TEKLIF_SATIR silinecekSatir;
    private StreamedContent sablonExcel;
    
    private SatisDao satisDao; 
    @ManagedProperty(value="#{sessionBean}")
    private SessionBean sessionBean;
    
    @PostConstruct
    public void init() 
    {          
        try 
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            sessionBean = (SessionBean)facesContext.getApplication().createValueBinding("#{sessionBean}").getValue(facesContext);
            satisDao = new SatisDao();
            
            exceldenYuklenecekTeklifBaslik = new TEKLIF_BASLIK();
            exceldenYuklenecekTeklifBaslik.setTEKLIF_TARIHI(new java.sql.Timestamp(new java.util.Date().getTime()));
            exceldenYuklenecekTeklifBaslik.setTEKLIF_BASLANGIC_TARIHI(new java.sql.Timestamp(new java.util.Date().getTime()));
            exceldenYuklenecekTeklifBaslik.setTEKLIF_BITIS_TARIHI(new java.sql.Timestamp(new java.util.Date().getTime()));
            exceldenYuklenecekTeklifBaslik.setTEKLIF_KUR(1.0);
            teklifNoUret();
            
            exceldenYuklenecekTeklifSatirlari = new ArrayList<>();
            for(int i = 0; i < 400; i++)
               exceldenYuklenecekTeklifSatirlari.add(new TEKLIF_SATIR());
            
            birimListesi = new ArrayList<>();
            paraBirimiListesi = new ArrayList<>();
            paraBirimiListesi.add("TL");
            paraBirimiListesi.add("EUR");
            paraBirimiListesi.add("USD"); 
            
            silinecekSatir = new TEKLIF_SATIR();
            //tamamlanacakSatirIndex = 0;
            getirTumBirimler();   
        } 
        catch (Exception ex) 
        {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sayfa başlatılırken hata meydana geldi.", ""));
        }
    }
    
    public void sablonExcelHazirla() 
    {
        List<String> musteriler = new ArrayList<>();
        try 
        {   
            //////////////////////////////////
            File file = new File(
                        FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") 
                                + "resources/templates", "templateOfferLines.xlsx");
            InputStream input = new FileInputStream(file);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            sablonExcel = new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName());
            //////////////////////////////////
        }
        catch (Exception ex) 
        {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Excel getirme işleminde hata meydana geldi.", ""));
        } 
    }
    
    
     public void handleFileUpload(FileUploadEvent event) 
     {
        try 
        {   
            exceldenYuklenecekTeklifSatirlari = new ArrayList<>();
            
            XSSFWorkbook workbook = new XSSFWorkbook(event.getFile().getInputstream());
            XSSFSheet sheet = workbook.getSheetAt(0);            
            
            for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext();) 
            {
                TEKLIF_SATIR teklifSatir = new TEKLIF_SATIR();
                Row row = rit.next();
                
                //Birinci sıradaki başlığı atlamak için
                if(row.getRowNum()==0)
                    row = rit.next();
                
                for (int cn=0; cn<8; cn++) 
                {
                    if(cn == 0)
                    {
                        Cell c = row.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                        if (c == null)
                           teklifSatir.setMALZEME_HIZMET_MASRAF_KODU("AAA");
                        else
                            {
                                c.setCellType(Cell.CELL_TYPE_STRING);
                                
                                //Eğer malzeme ERP'de yoksa AAA kodu atanacak...
                                if(satisDao.malzemeERPdeVarMi(c.getStringCellValue(), sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER()))
                                    teklifSatir.setMALZEME_HIZMET_MASRAF_KODU(c.getStringCellValue());
                                else
                                    teklifSatir.setMALZEME_HIZMET_MASRAF_KODU("AAA");
                            }
                    }
                    if(cn == 1)
                    {
                        Cell c = row.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                        if (c == null)
                           teklifSatir.setMALZEME_HIZMET_MASRAF_ADI("");
                        else
                            {
                                c.setCellType(Cell.CELL_TYPE_STRING);
                                teklifSatir.setMALZEME_HIZMET_MASRAF_ADI(c.getStringCellValue());
                            }
                    }
                    if(cn == 2)
                    {
                        Cell c = row.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                        if (c == null)
                           teklifSatir.setMALZEME_HIZMET_MASRAF_ADI2("");
                        else
                            {
                                c.setCellType(Cell.CELL_TYPE_STRING);
                                teklifSatir.setMALZEME_HIZMET_MASRAF_ADI2(c.getStringCellValue());
                            }
                    }
                    if(cn == 3)
                    {
                        Cell c = row.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                        if (c == null)
                           teklifSatir.setMALZEME_HIZMET_MASRAF_ACIKLAMASI("");
                        else
                            {
                                c.setCellType(Cell.CELL_TYPE_STRING);
                                teklifSatir.setMALZEME_HIZMET_MASRAF_ACIKLAMASI(c.getStringCellValue());
                            }
                    }
                    if(cn == 4)
                    {
                        Cell c = row.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                        if (c == null)
                           teklifSatir.setMALZEME_HIZMET_MASRAF_MIKTARI(0.0);
                        else
                            {
                               c.setCellType(Cell.CELL_TYPE_NUMERIC);
                               teklifSatir.setMALZEME_HIZMET_MASRAF_MIKTARI(c.getNumericCellValue());
                            }
                    }
                    if(cn == 5)
                    {
                        Cell c = row.getCell(cn, Row.RETURN_BLANK_AS_NULL);
                        if (c == null)
                           teklifSatir.setBirimIsim("");
                        else
                            {
                                c.setCellType(Cell.CELL_TYPE_STRING);
                                teklifSatir.setBirimIsim(c.getStringCellValue().toUpperCase());
                            }
                    }
//                    if(cn == 6)
//                    {
//                        Cell c = row.getCell(cn, Row.RETURN_BLANK_AS_NULL);
//                        if (c == null)
//                           teklifSatir.setParaBirimiIsim("");
//                        else
//                            {
//                                c.setCellType(Cell.CELL_TYPE_STRING);
//                                teklifSatir.setParaBirimiIsim(c.getStringCellValue());
//                            }
//                    }
//                    if(cn == 7)
//                    {
//                        Cell c = row.getCell(cn, Row.RETURN_BLANK_AS_NULL);
//                        if (c == null)
//                           teklifSatir.setKUR(1.0);
//                        else
//                            {
//                               c.setCellType(Cell.CELL_TYPE_NUMERIC);
//                               teklifSatir.setKUR(c.getNumericCellValue());
//                            }
//                    }
                 }
                exceldenYuklenecekTeklifSatirlari.add(teklifSatir);
            }
        }
        catch (Exception ex) 
        {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Excel yükleme işleminde hata meydana geldi.", ""));
        }  
    }
         
     
    public List<String> musteriTamamla(String query) 
    {
        List<String> musteriler = new ArrayList<>();
        try 
        {   
            musteriler = satisDao.getirMusteriListUnvanVeyaKodIle(query);
            return musteriler;
        }
        catch (Exception ex) 
        {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Müşteri getirme işleminde hata meydana geldi.", ""));
           return musteriler;
        } 
    }
    
    public List<String> satirKodTamamla(String query) 
    {
        List<String> satirlar = new ArrayList<>();
        try 
        {   
            satirlar = satisDao.getirSatirListKodIle(query,sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
            return satirlar;
        }
        catch (Exception ex) 
        {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Satır getirme işleminde hata meydana geldi.", ""));
           return satirlar;
        } 
    }
    
    public List<String> satirAdTamamla(String query) 
    {
        List<String> satirlar = new ArrayList<>();
        try 
        {   
            satirlar = satisDao.getirSatirListAdIle(query,sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
            return satirlar;
        }
        catch (Exception ex) 
        {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Satır getirme işleminde hata meydana geldi.", ""));
           return satirlar;
        } 
    }
    
    public List<String> satirAd2Tamamla(String query) 
    {
        List<String> satirlar = new ArrayList<>();
        try 
        {   
            satirlar = satisDao.getirSatirListAd2Ile(query,sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
            return satirlar;
        }
        catch (Exception ex) 
        {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Satır getirme işleminde hata meydana geldi.", ""));
           return satirlar;
        } 
    }
    
    public String kaydetTeklif() 
    {
        //kaydederken TeklifNo üretiyoruz çünkü aynı anda başkası aynı numara ile kayıt yapıyor olabilir...
        teklifNoUret();
        
        try 
        {  
            int kontrol = 0;
            
            if(exceldenYuklenecekTeklifSatirlari.size()==0)
            {
                kontrol++;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Lütfen en az bir tane satır girişi yapınız!", ""));
                return "";
            }
            if(exceldenYuklenecekTeklifBaslik.getMUSTERI_RECORD_ID()==null 
                    || exceldenYuklenecekTeklifBaslik.getPARA_BIRIMI()==null)
            {
                kontrol++;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Lütfen müşteri ve para birimi alanlarını kontrol ediniz!", ""));
                return "";
            }
            if(exceldenYuklenecekTeklifBaslik.getMUSTERI_RECORD_ID()!=null 
                    && exceldenYuklenecekTeklifBaslik.getPARA_BIRIMI()!=null)
            {
                if(exceldenYuklenecekTeklifBaslik.getMUSTERI_RECORD_ID().isEmpty() 
                    || exceldenYuklenecekTeklifBaslik.getPARA_BIRIMI().isEmpty())
                {
                    kontrol++;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Lütfen müşteri ve para birimi alanlarını kontrol ediniz!", ""));
                    return "";
                }                
            }
            
            //Kodları girilmemiş bomboş bir liste kaydedilmek isteniyorsa
            int kontrol2 = 0;
            
            for(TEKLIF_SATIR teklifSatir : exceldenYuklenecekTeklifSatirlari)
            {
                if(teklifSatir.getMALZEME_HIZMET_MASRAF_KODU()==null)
                   kontrol2++;
                else if(teklifSatir.getMALZEME_HIZMET_MASRAF_KODU()!=null)
                    if(teklifSatir.getMALZEME_HIZMET_MASRAF_KODU().equals(""))
                        kontrol2++;                       
            }            
            if(exceldenYuklenecekTeklifSatirlari.size() == kontrol2)
            {
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Lütfen satırların kodlarını girdikten sonra kayıt işlemi yapınız!", ""));
               return "";
            }
            
            if(kontrol == 0)
            {            
                String randomUUIDString = 
                        satisDao.kaydetTeklifBaslikManuel(exceldenYuklenecekTeklifBaslik, 
                                                          sessionBean.getKullanici().getKULLANICI_ID(),
                                                          sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER(),
                                                          sessionBean.getKullanici().getON_DEGER_FIRMA_ID(),
                                                          sessionBean.getKullanici().getAD() + " " + sessionBean.getKullanici().getSOYAD());

                for(TEKLIF_SATIR teklifSatir : exceldenYuklenecekTeklifSatirlari)
                {
                   //Kodu, adı veya ad2'si boş da değilse
                   if(!teklifSatir.getMALZEME_HIZMET_MASRAF_KODU().equals("")
                      || !teklifSatir.getMALZEME_HIZMET_MASRAF_ADI().equals("")
                      || !teklifSatir.getMALZEME_HIZMET_MASRAF_ADI2().equals(""))
                    {
                            teklifSatir.setParaBirimiIsim(exceldenYuklenecekTeklifBaslik.getPARA_BIRIMI());
                            teklifSatir.setKUR(exceldenYuklenecekTeklifBaslik.getTEKLIF_KUR());

                            satisDao.kaydetTeklifSatirManuel(randomUUIDString, teklifSatir,
                                                        sessionBean.getKullanici().getKULLANICI_ID(),
                                                        sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER(),
                                                        sessionBean.getKullanici().getAD() + " " + sessionBean.getKullanici().getSOYAD());
                    }              
                }
                
               FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Teklif başarıyla kaydedilmiştir.", ""));
               return "/admin/pages/satis.jsf?faces-redirect=true"; 
            }
            else
            {
                return "";
            }
            
        }
        catch (Exception ex) 
        {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklif kaydetme işleminde hata meydana geldi.", ""));
           return "";
        } 
    }
    
    public void temizleTeklif()
    {
        try 
        {
           exceldenYuklenecekTeklifBaslik = new TEKLIF_BASLIK();
           exceldenYuklenecekTeklifBaslik.setTEKLIF_TARIHI(new java.sql.Timestamp(new java.util.Date().getTime()));
           exceldenYuklenecekTeklifBaslik.setTEKLIF_BASLANGIC_TARIHI(new java.sql.Timestamp(new java.util.Date().getTime()));
           exceldenYuklenecekTeklifBaslik.setTEKLIF_BITIS_TARIHI(new java.sql.Timestamp(new java.util.Date().getTime()));
           exceldenYuklenecekTeklifBaslik.setTEKLIF_KUR(1.0);
           teklifNoUret();
            
           exceldenYuklenecekTeklifSatirlari = new ArrayList<>();
           for(int i = 0; i < 400; i++)
               exceldenYuklenecekTeklifSatirlari.add(new TEKLIF_SATIR());
           
           silinecekSatir = new TEKLIF_SATIR();
        }
        catch (Exception ex) 
        {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklif temizlenirken hata meydana geldi.", ""));
        } 
    }
    
    public void getirTumBirimler()
    {
    try 
        {
            birimListesi = satisDao.getirTumBirimler(sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
        }
        catch (Exception ex) 
        {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Birimler getirilirken hata meydana geldi.", ""));
        } 
    }
    
    public void silSatir() 
    {
        try 
        {            
            exceldenYuklenecekTeklifSatirlari.remove(silinecekSatir);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Satır başarıyla silinmiştir.", ""));
        } 
        catch (Exception ex) 
        {
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kullanıcı silme işleminde hata meydana geldi.", ""));
        }
    }
    
    public void ekleSatir() 
    {
        try 
        {   
            TEKLIF_SATIR teklifSatir = new TEKLIF_SATIR();
            if(exceldenYuklenecekTeklifSatirlari.size()>0)
            {
                teklifSatir = exceldenYuklenecekTeklifSatirlari.get(exceldenYuklenecekTeklifSatirlari.size()-1);
                
                if(teklifSatir.getMALZEME_HIZMET_MASRAF_KODU()!=null)
                    exceldenYuklenecekTeklifSatirlari.add(new TEKLIF_SATIR());
            }
            else                
                exceldenYuklenecekTeklifSatirlari.add(new TEKLIF_SATIR());
        } 
        catch (Exception ex) 
        {
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Satır ekleme işleminde hata meydana geldi.", ""));
        }
    } 
     
    public void musteriSecildi(SelectEvent event) 
    {
        try 
        {
            exceldenYuklenecekTeklifBaslik
                .setPARA_BIRIMI(satisDao
                                .getirMusteriParaBirimiMusteriKodIle(exceldenYuklenecekTeklifBaslik
                                                                        .getMUSTERI_RECORD_ID().substring(0 , exceldenYuklenecekTeklifBaslik.getMUSTERI_RECORD_ID().indexOf("___"))
                                                                     ,sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER())
                                );
            
            
            guncelleKur();
            
            for(TEKLIF_SATIR teklifSatir :exceldenYuklenecekTeklifSatirlari)
            {
                teklifSatir.setParaBirimiIsim(exceldenYuklenecekTeklifBaslik.getPARA_BIRIMI());
                teklifSatir.setKUR(exceldenYuklenecekTeklifBaslik.getTEKLIF_KUR());
            }
            
        } 
        catch (Exception ex) 
        {
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Müşteri para birimi getirme işleminde hata meydana geldi.", ""));
        }
        
    }
    
    public void satirKodSecildi(SelectEvent event) 
    {      
        try 
        {  
           for(TEKLIF_SATIR teklifSatir :exceldenYuklenecekTeklifSatirlari)
            {
                if(teklifSatir.getMALZEME_HIZMET_MASRAF_KODU()!=null)
                if(teklifSatir.getMALZEME_HIZMET_MASRAF_KODU().equals(event.getObject().toString()))
                {
                    teklifSatir.setMALZEME_HIZMET_MASRAF_KODU(event.getObject().toString().substring(0 , event.getObject().toString().indexOf("___")));
                    ERPItem  ERPItm = satisDao.getirItemERPdenKodIle(teklifSatir.getMALZEME_HIZMET_MASRAF_KODU(), sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
                    
                    //teklifSatir.setMALZEME_HIZMET_MASRAF_ADI(event.getObject().toString().substring(event.getObject().toString().indexOf("___") + 3 , event.getObject().toString().length()));
                    teklifSatir.setMALZEME_HIZMET_MASRAF_ADI(ERPItm.getAd());
                    teklifSatir.setMALZEME_HIZMET_MASRAF_ADI2(ERPItm.getAd2());
                    teklifSatir.setBirimIsim(ERPItm.getBirim());
                    
                    break;
                }
            }            
        } 
        catch (Exception ex) 
        {
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Satır getirme işleminde hata meydana geldi.", ""));
        }
        
    }
    
    public void satirAdSecildi(SelectEvent event) 
    {
        try 
        { 
            for(TEKLIF_SATIR teklifSatir :exceldenYuklenecekTeklifSatirlari)
            {
                if(teklifSatir.getMALZEME_HIZMET_MASRAF_ADI()!=null)
                if(teklifSatir.getMALZEME_HIZMET_MASRAF_ADI().equals(event.getObject().toString()))
                {
                    teklifSatir.setMALZEME_HIZMET_MASRAF_KODU(event.getObject().toString().substring(0 , event.getObject().toString().indexOf("___")));
                    ERPItem  ERPItm = satisDao.getirItemERPdenKodIle(teklifSatir.getMALZEME_HIZMET_MASRAF_KODU(), sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());

                    //teklifSatir.setMALZEME_HIZMET_MASRAF_ADI(event.getObject().toString().substring(event.getObject().toString().indexOf("___") + 3 , event.getObject().toString().length()));
                    teklifSatir.setMALZEME_HIZMET_MASRAF_ADI(ERPItm.getAd());
                    teklifSatir.setMALZEME_HIZMET_MASRAF_ADI2(ERPItm.getAd2());
                    teklifSatir.setBirimIsim(ERPItm.getBirim());
                    
                    break;
                }
            }
        } 
        catch (Exception ex) 
        {
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Satır getirme işleminde hata meydana geldi.", ""));
        }
    }
    
     public void satirAd2Secildi(SelectEvent event) 
    {
        try 
        {        
            for(TEKLIF_SATIR teklifSatir :exceldenYuklenecekTeklifSatirlari)
            {
                if(teklifSatir.getMALZEME_HIZMET_MASRAF_ADI2()!=null)
                if(teklifSatir.getMALZEME_HIZMET_MASRAF_ADI2().equals(event.getObject().toString()))
                {
                    teklifSatir.setMALZEME_HIZMET_MASRAF_KODU(event.getObject().toString().substring(0 , event.getObject().toString().indexOf("___")));
                    ERPItem  ERPItm = satisDao.getirItemERPdenKodIle(teklifSatir.getMALZEME_HIZMET_MASRAF_KODU(), sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
                    
                    //teklifSatir.setMALZEME_HIZMET_MASRAF_ADI(event.getObject().toString().substring(event.getObject().toString().indexOf("___") + 3 , event.getObject().toString().length()));
                    teklifSatir.setMALZEME_HIZMET_MASRAF_ADI(ERPItm.getAd());
                    teklifSatir.setMALZEME_HIZMET_MASRAF_ADI2(ERPItm.getAd2());
                    teklifSatir.setBirimIsim(ERPItm.getBirim());
                    
                    break;
                }
            }        
        } 
        catch (Exception ex) 
        {
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Satır getirme işleminde hata meydana geldi.", ""));
        }
    }
     
    public void guncelleMusteriler() 
    {
        try 
        {    
            String ERPFirmaNoTemp = sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER();
            String enBuyukMusteriRecordId = satisDao.getirEnBuyukMusteriRecordId(ERPFirmaNoTemp);
            satisDao.kaydetMusterilerERPden(enBuyukMusteriRecordId, 
                                            sessionBean.getKullanici().getKULLANICI_ID(),
                                            sessionBean.getKullanici().getERP_ON_DEGER_FIRMA_NUMBER());
        } 
        catch (Exception ex) 
        {
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Müşterileri güncelleme işleminde hata meydana geldi.", ""));
        }
    }
    
    public void teklifNoUret() 
    {
        try 
        {    
            String teklifNumarasi = satisDao.getirEnBuyukTeklifNumarasi();
            if(teklifNumarasi.equals(""))
                teklifNumarasi = "T0000001";
            else
            {
                int result = Integer.parseInt(teklifNumarasi);	
                result++;
                teklifNumarasi = "T" + String.format("%07d", result);
            }
            
            exceldenYuklenecekTeklifBaslik.setTEKLIF_NUMARASI(teklifNumarasi);
        } 
        catch (Exception ex) 
        {
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teklif no. üretme işleminde hata meydana geldi.", ""));
        }
    }       
            
     public void guncelleKur() 
    {
        try 
        {   
            if(exceldenYuklenecekTeklifBaslik.getPARA_BIRIMI()!=null)
            {
                
                if(exceldenYuklenecekTeklifBaslik.getPARA_BIRIMI().equals("TL"))
                    exceldenYuklenecekTeklifBaslik.setTEKLIF_KUR(1.0000);
                else
                {
                    
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(new URL("https://www.tcmb.gov.tr/kurlar/today.xml").openStream());

                    doc.getDocumentElement().normalize();

                    NodeList nList = doc.getElementsByTagName("Currency");

                    for (int temp = 0; temp < nList.getLength(); temp++) 
                    {
                        Node nNode = nList.item(temp);				

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) 
                        {
                            Element eElement = (Element) nNode;

                            if(exceldenYuklenecekTeklifBaslik.getPARA_BIRIMI().equals(eElement.getAttribute("CurrencyCode")))
                            {
                                exceldenYuklenecekTeklifBaslik.setTEKLIF_KUR(
                                            Double.valueOf(eElement.getElementsByTagName("BanknoteSelling").item(0).getTextContent())
                                                                            );
                                break;
                            }
                        }
                    }
                }
            }            
        } 
        
        catch (Exception ex) 
        {
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kur güncelleme işleminde hata meydana geldi.", ""));
        }
    }
        
    public void satirlarinParaBiriminiTopluGuncelle() 
    {
        try 
        {    
            for(TEKLIF_SATIR teklifSatir :exceldenYuklenecekTeklifSatirlari)
            {
                teklifSatir.setParaBirimiIsim(exceldenYuklenecekTeklifBaslik.getPARA_BIRIMI());
            }
        } 
        catch (Exception ex) 
        {
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Müşterileri güncelleme işleminde hata meydana geldi.", ""));
        }
    }
    
    public void satirlarinKurunuTopluGuncelle() 
    {
        try 
        {    
            for(TEKLIF_SATIR teklifSatir :exceldenYuklenecekTeklifSatirlari)
            {
                teklifSatir.setKUR(exceldenYuklenecekTeklifBaslik.getTEKLIF_KUR());
            }
        } 
        catch (Exception ex) 
        {
          FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Müşterileri güncelleme işleminde hata meydana geldi.", ""));
        }
    }

    public TEKLIF_BASLIK getExceldenYuklenecekTeklifBaslik() {
        return exceldenYuklenecekTeklifBaslik;
    }

    public void setExceldenYuklenecekTeklifBaslik(TEKLIF_BASLIK exceldenYuklenecekTeklifBaslik) {
        this.exceldenYuklenecekTeklifBaslik = exceldenYuklenecekTeklifBaslik;
    }

    public List<TEKLIF_SATIR> getExceldenYuklenecekTeklifSatirlari() {
        return exceldenYuklenecekTeklifSatirlari;
    }

    public void setExceldenYuklenecekTeklifSatirlari(List<TEKLIF_SATIR> exceldenYuklenecekTeklifSatirlari) {
        this.exceldenYuklenecekTeklifSatirlari = exceldenYuklenecekTeklifSatirlari;
    }
    
    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public SatisDao getSatisDao() {
        return satisDao;
    }

    public void setSatisDao(SatisDao satisDao) {
        this.satisDao = satisDao;
    }

    public List<String> getBirimListesi() {
        return birimListesi;
    }

    public void setBirimListesi(List<String> birimListesi) {
        this.birimListesi = birimListesi;
    }

    public List<String> getParaBirimiListesi() {
        return paraBirimiListesi;
    }

    public void setParaBirimiListesi(List<String> paraBirimiListesi) {
        this.paraBirimiListesi = paraBirimiListesi;
    }

    public TEKLIF_SATIR getSilinecekSatir() {
        return silinecekSatir;
    }

    public void setSilinecekSatir(TEKLIF_SATIR silinecekSatir) {
        this.silinecekSatir = silinecekSatir;
    }

    public StreamedContent getSablonExcel() {
        return sablonExcel;
    }

    public void setSablonExcel(StreamedContent sablonExcel) {
        this.sablonExcel = sablonExcel;
    }
}
