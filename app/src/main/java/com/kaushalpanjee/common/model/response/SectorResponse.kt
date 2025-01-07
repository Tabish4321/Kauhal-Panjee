package com.kaushalpanjee.common.model.response


data class SectorResponse(
    val wrappedList: List<SubSector>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String?
)

data class SubSector(
    val sectorId: String,
    val subSectorName: String,
    val sectorName: String,
    val subSectorId: String
)
