package com.kaushalpanjee.common.model.request

data class AddressInsertReq(
    val appVersion: String,
    val loginId: String,
    val imeiNo: String,
    val sectionCount: String, // Mandatory and static
    val prStateCode: String,
    val prDistrictCode: String,
    val prBlockCode: String,
    val prGpCode: String,
    val prVillageCode: String,
    val prStreet1: String,
    val prStreet2: String,
    val prPinCode: String,
    val residenceCert: String,
    val isTempAddressSame: String,
    val tempStateCode: String,
    val tempDistrictCode: String,
    val tempBlockCode: String,
    val tempGpCode: String,
    val tempVillageCode: String,
    val tempStreet1: String,
    val tempStreet2: String,
    val tempPinCode: String,

    val prLocality: String,
    val tempLocality: String,

    val prUlbCode: String,
    val prWardCode: String,

    val tempWardCode: String,
    val tempUlbCode: String


)
