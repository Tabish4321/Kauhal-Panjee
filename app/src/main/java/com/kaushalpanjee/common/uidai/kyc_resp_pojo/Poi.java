package com.kaushalpanjee.common.uidai.kyc_resp_pojo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Poi")
class Poi {
    @JacksonXmlProperty(isAttribute = true)
    private String dob;
    @JacksonXmlProperty(isAttribute = true)
    private String e;
    @JacksonXmlProperty(isAttribute = true)
    private String gender;
    @JacksonXmlProperty(isAttribute = true)
    private String m;
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    public String getName() {
        return name;
    }
}
