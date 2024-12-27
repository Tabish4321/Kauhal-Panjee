package com.kaushalpanjee.uidai.kyc_resp_pojo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("Poi")
public class Poi {
    @XStreamAsAttribute
     String dob;

    @XStreamAsAttribute
     String gender;

    @XStreamAsAttribute
     String name;

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    // Setter for gender
    public void setGender(String gender) {
        this.gender = gender;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }
}
