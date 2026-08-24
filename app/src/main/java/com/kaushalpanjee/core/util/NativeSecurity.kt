package com.kaushalpanjee.core.util

import android.util.Log

object NativeSecurity {

    private const val TAG = "NativeSecurity"

    private var libraryLoaded = false

    val isLibraryLoaded: Boolean
        get() = libraryLoaded

    init {
        libraryLoaded = try {

            System.loadLibrary("security")

            Log.d(
                TAG,
                "libsecurity.so loaded successfully"
            )

            true

        } catch (e: UnsatisfiedLinkError) {

            Log.e(
                TAG,
                "Failed to load libsecurity.so",
                e
            )

            false

        } catch (e: Throwable) {

            Log.e(
                TAG,
                "Unexpected error while loading libsecurity.so",
                e
            )

            false
        }
    }


    // =========================================================
    // PRIVATE JNI METHODS
    // =========================================================

    private external fun nativeGetClientSecret(): String

    private external fun nativeGetRefreshTokenUrl(): String

    private external fun nativeGetEncryptKey(): String

    private external fun nativeGetEncryptIvKey(): String

    private external fun nativeGetCryptLibAES(): String

    private external fun nativeGetCryptId(): String

    private external fun nativeGetCryptIV(): String

    private external fun nativeGetWadhKey(): String

    private external fun nativeGetSslPin1(): String

    private external fun nativeGetSslPin2(): String

    private external fun nativeGetBaseUrlDev(): String

    private external fun nativeGetBaseUrlProd(): String


    // =========================================================
    // SAFE PUBLIC METHODS
    // Existing code will still receive String, not String?
    // =========================================================

    fun getClientSecret(): String =
        executeSafely("getClientSecret") {
            nativeGetClientSecret()
        }

    fun getRefreshTokenUrl(): String =
        executeSafely("getRefreshTokenUrl") {
            nativeGetRefreshTokenUrl()
        }

    fun getEncryptKey(): String =
        executeSafely("getEncryptKey") {
            nativeGetEncryptKey()
        }

    fun getEncryptIvKey(): String =
        executeSafely("getEncryptIvKey") {
            nativeGetEncryptIvKey()
        }

    fun getCryptLibAES(): String =
        executeSafely("getCryptLibAES") {
            nativeGetCryptLibAES()
        }

    fun getCryptId(): String =
        executeSafely("getCryptId") {
            nativeGetCryptId()
        }

    fun getCryptIV(): String =
        executeSafely("getCryptIV") {
            nativeGetCryptIV()
        }

    fun getWadhKey(): String =
        executeSafely("getWadhKey") {
            nativeGetWadhKey()
        }

    fun getSslPin1(): String =
        executeSafely("getSslPin1") {
            nativeGetSslPin1()
        }

    fun getSslPin2(): String =
        executeSafely("getSslPin2") {
            nativeGetSslPin2()
        }

    fun getBaseUrlDev(): String =
        executeSafely("getBaseUrlDev") {
            nativeGetBaseUrlDev()
        }

    fun getBaseUrlProd(): String =
        executeSafely("getBaseUrlProd") {
            nativeGetBaseUrlProd()
        }


    // =========================================================
    // COMMON SAFE EXECUTION
    // =========================================================

    private inline fun executeSafely(
        methodName: String,
        block: () -> String
    ): String {

        if (!libraryLoaded) {

            Log.e(
                TAG,
                "$methodName failed: native library not loaded"
            )

            return ""
        }

        return try {

            block()

        } catch (e: UnsatisfiedLinkError) {

            Log.e(
                TAG,
                "$methodName failed",
                e
            )

            ""

        } catch (e: Throwable) {

            Log.e(
                TAG,
                "$methodName unexpected error",
                e
            )

            ""
        }
    }
}