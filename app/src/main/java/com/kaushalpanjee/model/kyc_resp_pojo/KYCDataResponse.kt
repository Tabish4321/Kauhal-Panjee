package com.kaushalpanjee.model.kyc_resp_pojo

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(name = "KycRes", strict = false)
data class KycResponse(
    @Attribute(name = "code") val code: String,
    @Attribute(name = "ret") val ret: String,
    @Attribute(name = "ts") val ts: String,
    @Attribute(name = "ttl") val ttl: String,
    @Attribute(name = "txn") val txn: String,
    @Element(name = "Rar") val rar: String,
    @Element(name = "UidData") val uidData: UidData
)

@Root(name = "Rar", strict = false)
data class Rar(
    @Element(name = "AuthRes") val authRes: AuthRes
)

@Root(name = "AuthRes", strict = false)
data class AuthRes(
    @Attribute(name = "code") val code: String,
    @Attribute(name = "info") val info: String,
    @Attribute(name = "ret") val ret: String,
    @Attribute(name = "ts") val ts: String,
    @Attribute(name = "txn") val txn: String,
    @Element(name = "Signature") val signature: Signature
)

@Root(name = "Signature", strict = false)
data class Signature(
    @Element(name = "SignedInfo") val signedInfo: SignedInfo,
    @Element(name = "SignatureValue") val signatureValue: String
)

@Root(name = "SignedInfo", strict = false)
data class SignedInfo(
    @Element(name = "CanonicalizationMethod") val canonicalizationMethod: String,
    @Element(name = "SignatureMethod") val signatureMethod: String,
    @Element(name = "Reference") val reference: Reference
)

@Root(name = "Reference", strict = false)
data class Reference(
    @Attribute(name = "URI") val uri: String,
    @Element(name = "DigestMethod") val digestMethod: DigestMethod,
    @Element(name = "DigestValue") val digestValue: String
)

@Root(name = "DigestMethod", strict = false)
data class DigestMethod(
    @Attribute(name = "Algorithm") val algorithm: String
)

@Root(name = "UidData", strict = false)
data class UidData(
    @Attribute(name = "tkn") val tkn: String,
    @Attribute(name = "uid") val uid: String,
    @Element(name = "Poi") val poi: Poi,
    @Element(name = "Poa") val poa: Poa,
    @Element(name = "Pht", required = false) val pht: String?
)

@Root(name = "Poi", strict = true)
data class Poi(
    @Attribute(name = "dob") val dob: String,
    @Attribute(name = "gender") val gender: String,
    @Attribute(name = "name") val name: String
)

@Root(name = "Poa", strict = true)
data class Poa(
    @Attribute(name = "co") val co: String,
    @Attribute(name = "country") val country: String,
    @Attribute(name = "dist") val dist: String,
    @Attribute(name = "loc") val loc: String,
    @Attribute(name = "pc") val pc: String,
    @Attribute(name = "po") val po: String,
    @Attribute(name = "state") val state: String,
    @Attribute(name = "subdist") val subdist: String,
    @Attribute(name = "vtc") val vtc: String
)


