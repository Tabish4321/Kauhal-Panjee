package com.kaushalpanjee.uidai.capture;

import android.content.Context;

import com.kaushalpanjee.uidai.AssetsPropertyReader;
import com.kaushalpanjee.uidai.Auth;
import com.kaushalpanjee.uidai.AuthRes;
import com.kaushalpanjee.uidai.DigitalSigner;
import com.kaushalpanjee.uidai.Meta;
import com.kaushalpanjee.uidai.Param;
import com.kaushalpanjee.uidai.Uses;
import com.kaushalpanjee.uidai.kyc_resp_pojo.KycRes;
import com.thoughtworks.xstream.XStream;
import com.kaushalpanjee.uidai.PidData;
import com.kaushalpanjee.uidai.Data;

import java.util.Properties;
import java.util.UUID;

public class XstreamCommonMethos {


    public static PidData pidXmlToPojo(String xml) {
        String receivedXml = xml;
        XStream xstream = new XStream();
        xstream.processAnnotations(PidData.class);
        xstream.processAnnotations(Param.class);
        xstream.processAnnotations(Resp.class);
        xstream.processAnnotations(Skey.class);
        xstream.processAnnotations(CustOpts.class);
        xstream.autodetectAnnotations(true);

        if (receivedXml.equals("")) {
            return null;
        }

        PidData data = (PidData) xstream.fromXML(receivedXml);
        return data;

    }

    public static AuthRes respXmlToPojo(String xml) {
        String receivedXml = xml;
        XStream xstream = new XStream();
        xstream.processAnnotations(AuthRes.class);
        xstream.autodetectAnnotations(true);

        if (receivedXml.equals("")) {
            return null;
        }

        AuthRes data = (AuthRes) xstream.fromXML(receivedXml);
        return data;

    }

    public static KycRes respDecodedXmlToPojo(String xml) {
        String receivedXml = xml;
        XStream xstream = new XStream();
        xstream.processAnnotations(KycRes.class);
        xstream.autodetectAnnotations(true);

        if (receivedXml.equals("")) {
            return null;
        }

        KycRes data = (KycRes) xstream.fromXML(receivedXml);
        return data;

    }

    public static AuthRes rarDecodedXmlToPojo(String xml) {
        String receivedXml = xml;
        XStream xstream = new XStream();
        xstream.processAnnotations(AuthRes.class);
        xstream.autodetectAnnotations(true);

        if (receivedXml.equals("")) {
            return null;
        }

        AuthRes data = (AuthRes) xstream.fromXML(receivedXml);
        return data;
    }

    public static String processPidBlock(String pidXml, String uid, Boolean isOtpUsed, Context context) throws Exception {

        AssetsPropertyReader assetsPropertyReader;
        Properties face_auth_properties;
        // Parsing received pid block xml
        assetsPropertyReader = new AssetsPropertyReader(context);
        face_auth_properties = assetsPropertyReader.getProperties("face_auth.properties");

        PidData pidDataObject = pidXmlToPojo(pidXml);

        ConfigParams configParams = ConfigUtils.getConfigData(context);
        // Constructing Auth xml

        XStream xtremeForAuthXml = new XStream();
        xtremeForAuthXml.processAnnotations(Auth.class);
        Auth authxml = new Auth();
        authxml.setUid(uid);
        authxml.setVer(face_auth_properties.getProperty("AUTH_VERSION"));
        authxml.setTid(face_auth_properties.getProperty("AUTH_TID"));
        authxml.setRc(face_auth_properties.getProperty("RESIDENT_CONCENT"));
        authxml.setAc(configParams.getAuaCode());
        authxml.setSa(configParams.getSubAUACode());
        authxml.setLk(configParams.getAuaLicenceKey());
        com.kaushalpanjee.uidai.Skey skey= new com.kaushalpanjee.uidai.Skey();
        skey.setCi(pidDataObject.getSkey().getCi());
        //skey.setCi("20250825");
        skey.setContent(pidDataObject.getSkey().getContent());
        authxml.setSkey(skey);
        Meta meta = new Meta();
        meta.setDc(pidDataObject.getDeviceInfo().getDc());
        meta.setDpId(pidDataObject.getDeviceInfo().getDpId());
        meta.setRdsVer(pidDataObject.getDeviceInfo().getRdsVer());
        meta.setRdsId(pidDataObject.getDeviceInfo().getRdsId());
        meta.setMi(pidDataObject.getDeviceInfo().getMi());
        meta.setMc(pidDataObject.getDeviceInfo().getMc());
        Data data = new Data();
        data.setContent(pidDataObject.getData().getContent());
        data.setType(pidDataObject.getData().getType());
        authxml.setHmac(pidDataObject.getHmac());


        Uses uses = new Uses();
        uses.setBio("y");
        uses.setBt("FID");
        if (isOtpUsed) {
            uses.setOtp("y");
        } else {
            uses.setOtp("n");
        }
        uses.setPa("n");
        uses.setPfa("n");
        uses.setPi("n");
        uses.setPin("n");

        authxml.setUses(uses);

        //authxml.setTxn("UKC:" + UUID.randomUUID().toString());
        authxml.setTxn(UUID.randomUUID().toString());

        authxml.setData(data);
        authxml.setMeta(meta);

        String authXml = xtremeForAuthXml.toXML(authxml);

        DigitalSigner ds = new DigitalSigner(context);


        authXml = ds.signXML(authXml, true);

        // return here for stagging.
         return authXml;

/*        XStream xtremeForKycXml = new XStream();
        xtremeForKycXml.processAnnotations(Kyc.class);
        xtremeForKycXml.autodetectAnnotations(true);
        Kyc kycxml = new Kyc();
        kycxml.setVer(face_auth_properties.getProperty("AUTH_VERSION"));
        kycxml.setRa("P");
        kycxml.setRc("Y");

        kycxml.setLr("N");
        kycxml.setDe("N");
        kycxml.setPfr("N");
        String rad = "";
        try {
            rad = Base64.encodeToString(authXml.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        kycxml.setRad(rad);

        String kycXml = xtremeForKycXml.toXML(kycxml);


        return kycXml;*/
    }


}
