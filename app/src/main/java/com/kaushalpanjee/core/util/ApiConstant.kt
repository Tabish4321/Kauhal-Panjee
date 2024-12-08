package com.kaushalpanjee.core.util

object ApiConstant {


    /**
     * BASE_URL_DEV="https://nrlm.gov.in/"
     * BASE_URL_STAGE="https://nrlm.gov.in/"
     * BASE_URL_PROD=""
     *
     */

    const val CONNECT_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 10L
    const val READ_TIMEOUT = 60L

    const val DEVICE_TYPE = 2 // android , 1-> ios
    const val DEVICE_TYPE_NAME = "android"



    private const val API_VERSION = "v1/"

    private const val API_FOLDER_BACKEND = "backend/"
    private const val API_FOLDER_CITIZEN = "citizen/"
     const val  API_SMS_OTP= "${API_FOLDER_BACKEND}${API_FOLDER_CITIZEN}smsOTP"
     const val  API_EMAIL_OTP= "${API_FOLDER_BACKEND}${API_FOLDER_CITIZEN}mailOTP"
     const val  API_STATE= "${API_FOLDER_BACKEND}${API_FOLDER_CITIZEN}stateList"





}