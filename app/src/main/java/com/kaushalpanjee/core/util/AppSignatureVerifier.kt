package com.kaushalpanjee.core.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import java.security.MessageDigest

object AppSignatureVerifier {

    private const val EXPECTED_SIGNATURE =
        "CA:72:9A:C1:AB:C3:A7:87:11:C2:B8:FB:23:1E:48:FB:06:8E:DD:2E:87:15:81:9D:F9:D5:8D:CC:DA:03:7E:2F"
       //"fdsfsdfsdfsdf"

    fun isSignatureValid(context: Context): Boolean {

        return try {

            val packageInfo =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    context.packageManager.getPackageInfo(
                        context.packageName,
                        PackageManager.GET_SIGNING_CERTIFICATES
                    )
                } else {
                    @Suppress("DEPRECATION")
                    context.packageManager.getPackageInfo(
                        context.packageName,
                        PackageManager.GET_SIGNATURES
                    )
                }

            val signatures =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    packageInfo.signingInfo?.apkContentsSigners
                else
                    @Suppress("DEPRECATION")
                    packageInfo.signatures

            val md = MessageDigest.getInstance("SHA-256")

            signatures?.forEach {

                val digest = md.digest(it.toByteArray())

                val current = digest.joinToString(":") {
                    "%02X".format(it)
                }

                Log.e("SIGNATURE", current)

                if (current.equals(EXPECTED_SIGNATURE, true))
                    return true
            }

            false

        } catch (e: Exception) {
            false
        }
    }
}