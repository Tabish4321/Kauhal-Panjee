package com.kaushalpanjee.uidai.kyc_resp_pojo;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Poa")
public class Poa {
    @XStreamAsAttribute
    private String co;
    @XStreamAsAttribute
    private String country;
    @XStreamAsAttribute
    private String dist;
    @XStreamAsAttribute
    private String house;
    @XStreamAsAttribute
    private String landmark;
    @XStreamAsAttribute
    private String loc;
    @XStreamAsAttribute
    private String pc;
    @XStreamAsAttribute
    private String po;
    @XStreamAsAttribute
    private String state;

    @XStreamAsAttribute
    private String subdist;

    @XStreamAsAttribute
    private String vtc;


    public String getCareof() {
        return co;
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
