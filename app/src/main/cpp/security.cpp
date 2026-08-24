#include <jni.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_kaushalpanjee_core_util_NativeSecurity_nativeGetClientSecret(
        JNIEnv *env,
        jobject thiz
) {
    return env->NewStringUTF("dgtbncbehkcbjebccnkec78yf37bc");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_kaushalpanjee_core_util_NativeSecurity_nativeGetRefreshTokenUrl(
        JNIEnv *env,
        jobject thiz
) {
    return env->NewStringUTF("jhbheugcy2373y379y37gydygdy");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_kaushalpanjee_core_util_NativeSecurity_nativeGetEncryptKey(
        JNIEnv *env,
        jobject thiz
) {
    return env->NewStringUTF(
            "$10A80$10A80$10A80$10A80$10A80$10A80$10A80$10A80"
    );
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_kaushalpanjee_core_util_NativeSecurity_nativeGetEncryptIvKey(
        JNIEnv *env,
        jobject thiz
) {
    return env->NewStringUTF(
            "$10A80$10A80$10A80$10A80$10A80$10A80$10A80$10A80"
    );
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_kaushalpanjee_core_util_NativeSecurity_nativeGetCryptLibAES(
        JNIEnv *env,
        jobject thiz
) {
    return env->NewStringUTF("AES/CBC/PKCS5PADDING");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_kaushalpanjee_core_util_NativeSecurity_nativeGetCryptId(
        JNIEnv *env,
        jobject thiz
) {
    return env->NewStringUTF("8080808080808080");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_kaushalpanjee_core_util_NativeSecurity_nativeGetCryptIV(
        JNIEnv *env,
        jobject thiz
) {
    return env->NewStringUTF("8080808080808080");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_kaushalpanjee_core_util_NativeSecurity_nativeGetWadhKey(
        JNIEnv *env,
        jobject thiz
) {
    return env->NewStringUTF(
            "sgydIC09zzy6f8Lb3xaAqzKquKe9lFcNR9uTvYxFp+A="
    );
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_kaushalpanjee_core_util_NativeSecurity_nativeGetSslPin1(
        JNIEnv *env,
        jobject thiz
) {
    return env->NewStringUTF(
            "sha256/t2g2DXw0vWYjKaDq/d4dWk6MTb+Lho+sF01J4RIX3IU="
    );
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_kaushalpanjee_core_util_NativeSecurity_nativeGetSslPin2(
        JNIEnv *env,
        jobject thiz
) {
    return env->NewStringUTF(
            "sha256/rnhtVs65ADYfQGtMuB0jq2kZwwHy6/iqnBiUKcK1m0Y="
    );
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_kaushalpanjee_core_util_NativeSecurity_nativeGetBaseUrlDev(
        JNIEnv *env,
        jobject thiz
) {
    return env->NewStringUTF("https://nrlm.gov.in/");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_kaushalpanjee_core_util_NativeSecurity_nativeGetBaseUrlProd(
        JNIEnv *env,
        jobject thiz
) {
    return env->NewStringUTF("https://nrlm.gov.in/");
}