package com.kaushalpanjee.common.model.response



data class BannerResponse(
    val bannerList: List<Banner>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String
)

data class Banner(
    val bannerImage: String,
    val bannerId: Int
)
