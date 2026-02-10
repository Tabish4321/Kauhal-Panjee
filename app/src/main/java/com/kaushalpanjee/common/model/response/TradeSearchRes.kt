package com.kaushalpanjee.common.model.response


data class TradeSearchRes(
    val wrappedList: List<TradeItem>,
    val responseCode: Int,
    val responseDesc: String
)

data class TradeItem(
    val sectorId: Int,
    val trade: String,
    val tradeCode: String,
    val sectorName: String
)
