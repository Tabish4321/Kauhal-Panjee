package com.kaushalpanjee.common.uidai.kyc_resp_pojo;

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
        if (null==Poi)
            return null;
        return Poi.getName();
    }
}
