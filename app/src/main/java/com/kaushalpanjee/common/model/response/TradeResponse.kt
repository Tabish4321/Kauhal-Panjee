package com.kaushalpanjee.common.model.response

data class TradeResponse(
    val wrappedList: List<Trade>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String?
)

data class Trade(
    val trade: String,
    val tradeCode: String
)
