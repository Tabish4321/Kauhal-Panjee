package com.kaushalpanjee.common.model.response

import com.kaushalpanjee.common.model.WrappedList


data class DistrictResponse(
    val responseCode: Int,
    val responseDesc: String,
    val districtList: MutableList<DistrictList>
)

data class DistrictList(
    val districtName: String,
    val districtCode: String,
    val lgdDistrictCode:String,
)