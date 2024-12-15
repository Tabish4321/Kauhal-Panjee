package com.kaushalpanjee.uidai.kyc_resp_pojo;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("UidData")
public class UidData {
    @JacksonXmlProperty(localName = "Pht")
    private String Pht;

    @JacksonXmlProperty(localName = "Poi")
    public Poi Poi;

    @JacksonXmlProperty(localName = "Poa")
    public Poa Poa;

    @JacksonXmlProperty(localName = "LData")
    private String LData;


    public String getPht() {
        if (Pht == null) {
            return "";
        }
        return Pht;
    }

    @Nullable
    public String getName(){
        if (Poi==null)
            return "";
        return Poi.getName();
    }

    @Nullable
    public String getGender(){
        if (Poi==null)
            return "";
        return Poi.getGender();
    }

    @Nullable
    public String getDob(){
        if (Poi==null)
            return "";
        return Poi.getDob();
    }

    @Nullable
    public String getCareOf(){
        if (Poa==null)
            return "";
        return Poa.getCareof();
    }
    @Nullable
    public String getDistrict(){
        if (Poa==null)
            return "";
        return Poa.getDistrict();
    }

    @Nullable
    public String getPc(){
        if (Poa==null)
            return "";
        return Poa.getPc();
    }

    @Nullable
    public String getPo(){
        if (Poa==null)
            return "";
        return Poa.getPo();
    }
     @Nullable
    public String getState(){
        if (Poa==null)
            return "";
        return Poa.getState();
    }

    @Nullable
    public String getsubDist(){
        if (Poa==null)
            return "";
        return Poa.getsubDist();
    }

    @Nullable
    public String getStreet(){
        if (Poa==null)
            return "";
        return Poa.getStreet();
    }
    @Nullable
    public String getVtc(){
        if (Poa==null)
            return "";
        return Poa.getVtc();
    }

    @Nullable
    public String getHouse(){
        if (Poa==null)
            return "";
        return Poa.getHouse();
    }

    @Nullable
    public String getLandmark(){
        if (Poa==null)
            return "";
        return Poa.getLandmark();
    }

}
