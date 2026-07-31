package com.kaushalpanjee.common.model.request

data class InsertAadhaarTxnReq(

    val txnAadhaar: String,
    val txnApp: String,
    val ret: String,
    val aadhaarCode: String,
)
