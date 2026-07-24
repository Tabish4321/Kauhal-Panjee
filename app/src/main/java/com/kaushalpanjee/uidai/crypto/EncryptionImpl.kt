package com.kaushalpanjee.uidai.crypto


import com.kaushalpanjee.core.util.AppConstant
import kotlin.Throws
import java.lang.Exception
import javax.inject.Inject


class EncryptionImpl @Inject constructor(private val cryptLib: CryptLib) : CryptoStrategy {

    @Throws(Exception::class)
    override fun encrypt(body: String): String {
        return cryptLib.encrypt(body, AppConstant.Constants.CRYPT_ID, AppConstant.Constants.CRYPT_IV)
    }

    override fun decrypt(data: String): String? {
        return null
    }
}