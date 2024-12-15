package com.kaushalpanjee.uidai.kyc_resp_pojo;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Poa")
public class Poa {
    @JacksonXmlProperty(isAttribute = true)
    private String careof;
    @JacksonXmlProperty(isAttribute = true)
    private String country;
    @JacksonXmlProperty(isAttribute = true)
    private String dist;
    @JacksonXmlProperty(isAttribute = true)
    private String house;
    @JacksonXmlProperty(isAttribute = true)
    private String landmark;
    @JacksonXmlProperty(isAttribute = true)
    private String loc;
    @JacksonXmlProperty(isAttribute = true)
    private String pc;
    @JacksonXmlProperty(isAttribute = true)
    private String po;
    @JacksonXmlProperty(isAttribute = true)
    private String state;
    @JacksonXmlProperty(isAttribute = true)
    private String street;
    @JacksonXmlProperty(isAttribute = true)
    private String subdist;
    @JacksonXmlProperty(isAttribute = true)
    private String vtc;


    public String getCareof() {
        return careof;
    }


    public String getDistrict() {
        return dist;
    }


    public String getPc() {
        return pc;
    }

    public String getPo() {
        return po;
    }

    public String getState() {
        return state;
    }

    public String getsubDist() {
        return subdist;
    }

    public String getStreet() {
        return street;
    }

    public String getVtc() {
        return vtc;
    }


    public String getHouse() {
        return house;
    }

    public String getLandmark() {
        return landmark;
    }
}
