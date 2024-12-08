package com.utilize.core.util

import android.util.Base64
import com.kaushalpanjee.core.util.log
import java.io.UnsupportedEncodingException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AESCryptography {

    private var enableEncryption = true

    @Throws(
        UnsupportedEncodingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )

    fun encryptIntoHexString(inputText: String, secretKey: String, ivKey: String): String {
        if (enableEncryption) {
            val keySpec = SecretKeySpec(secretKey.toByteArray(charset("UTF-8")), "AES")
            val ivSpec = IvParameterSpec(ivKey.toByteArray(charset("UTF-8")))
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
            val results: ByteArray = cipher.doFinal(inputText.toByteArray(charset("UTF-8")))
            return convertByteArrayToHexString(results)
        } else return inputText

    }

    fun decryptIntoString(inputText: String, secretKey: String, ivKey: String): String {
        try {
//            if (ivKey.toByteArray(Charsets.UTF_8).size != 16) {
//                throw IllegalArgumentException("Decryption failed: IV must be 16 bytes long")
//            }
//            if (secretKey.trim().toByteArray(Charsets.UTF_8).size != 16) {
//                throw IllegalArgumentException("Decryption failed : Key must be 16 bytes long")
//            }

            val keySpec = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "AES")
            val ivSpec = IvParameterSpec(ivKey.toByteArray(Charsets.UTF_8))
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)

            val decodedValue = Base64.decode(inputText, Base64.DEFAULT)
            val decryptedBytes = cipher.doFinal(decodedValue)

            return String(decryptedBytes, Charsets.UTF_8)

        } catch (e: Exception) {
            e.printStackTrace()
            log("Decryption failed cause", e.message.toString())
            return ""
        }

    }

    private fun convertByteArrayToHexString(byteArray: ByteArray): String {
        val sb = StringBuffer()
        for (b in byteArray) {
            val hexString = String.format("%02X", b)
            sb.append(hexString)
        }
        return sb.toString()
    }

    fun aesDecrypt(encryptedText: String, secretKey: String, ivKey: String): String {
        return try {
            // Define AES settings
            val keySpec = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "AES")
            val ivSpec = IvParameterSpec(ivKey.toByteArray(Charsets.UTF_8))

            // Create AES cipher instance
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)

            // Decode hex string to byte array
            val encryptedBytes = hexStringToByteArray(encryptedText)

            // Perform decryption
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            String(decryptedBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            ""
        }
    }

    // Helper function to convert hex string to byte array
    fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)
        for (i in 0 until len step 2) {
            data[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
        }
        return data
    }

}