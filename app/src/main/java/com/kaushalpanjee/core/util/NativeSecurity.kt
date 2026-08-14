package com.kaushalpanjee.core.util

object NativeSecurity {

    init {
        System.loadLibrary("security")
    }

    external fun getClientSecret(): String
    external fun getRefreshTokenUrl(): String

    external fun getEncryptKey(): String
    external fun getEncryptIvKey(): String

    external fun getCryptLibAES(): String
    external fun getCryptId(): String
    external fun getCryptIV(): String

    external fun getWadhKey(): String

    external fun getSslPin1(): String
    external fun getSslPin2(): String

    external fun getBaseUrlDev(): String
    external fun getBaseUrlProd(): String
}