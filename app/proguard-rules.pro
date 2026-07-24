##############################################
# 🔐 GLOBAL SETTINGS
##############################################

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Keep annotations (important for Retrofit, Hilt, Room)
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes EnclosingMethod

-keepclassmembers class * {
    public <init>(...);
}

##############################################
# 🧠 KOTLIN + METADATA
##############################################

-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**

##############################################
# 🏗️ APPLICATION CLASS
##############################################

-keep class ** extends android.app.Application { *; }

##############################################
# 🔥 HILT / DAGGER (IMPORTANT)
##############################################

-keep class dagger.hilt.** { *; }
-keep interface dagger.hilt.** { *; }
-keep class dagger.** { *; }
-keep interface dagger.** { *; }

-keep class javax.inject.** { *; }
-keep interface javax.inject.** { *; }

-dontwarn dagger.hilt.**
-dontwarn javax.inject.**

# Keep Hilt generated classes
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }

# Modules
-keep @dagger.Module class * { *; }
-keepclasseswithmembers class * {
    @dagger.Provides <methods>;
}

##############################################
# 🌐 RETROFIT (VERY IMPORTANT)
##############################################

-keep interface retrofit2.http.* { *; }
-dontwarn retrofit2.**
-keep interface * {
    @retrofit2.http.* <methods>;
}

-keep interface com.kaushalpanjee.** {
    @retrofit2.http.* <methods>;
}

-keep,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-keep class kotlin.coroutines.** { *; }

# Keep API interfaces
-keep interface com.kaushalpanjee.remote.** { *; }

##############################################
# 📡 OKHTTP
##############################################

-dontwarn okhttp3.**
# Do NOT keep full okhttp (let it shrink)

##############################################
# 🧾 GSON (IMPORTANT FOR MODELS)
##############################################

-keep class com.google.gson.reflect.TypeToken { *; }

# Keep only model classes (GOOD PRACTICE)
-keep class com.kaushalpanjee.model.** { *; }
-keep class com.kaushalpanjee.pojo.** { *; }
-keep class com.kaushalpanjee.common.model.** { *; }
-keep class com.kaushalpanjee.notification.with_api.** { *; }
-keep class com.kaushalpanjee.uidai.** { *; }

-dontwarn com.google.gson.**

##############################################
# 🗄️ ROOM DATABASE
##############################################

# Keep Entities
-keep @androidx.room.Entity class * { *; }

# Keep DAO
-keep @androidx.room.Dao class * { *; }

# Keep Database
-keep class * extends androidx.room.RoomDatabase { *; }

-dontwarn androidx.room.**

##############################################
# 🧩 FRAGMENTS / ACTIVITIES / BASE CLASSES
##############################################

-keep class * extends androidx.fragment.app.Fragment { *; }
-keep class * extends android.app.Activity { *; }

# Your base components
-keep class com.kaushalpanjee.basecomponent.** { *; }

##############################################
# 📦 REPOSITORY LAYER
##############################################

-keep class com.kaushalpanjee.repository.** { *; }

##############################################
# 🔐 CUSTOM INTERCEPTOR
##############################################

-keepclassmembers class com.kaushalpanjee.core.util.CustomInterceptor {
    public <methods>;
}

##############################################
# 🧪 SIMPLE XML
##############################################

-keep class org.simpleframework.xml.** { *; }
-dontwarn org.simpleframework.xml.**

##############################################
# 🧬 XML / STAX / XSTREAM CLEANUP
##############################################

-dontwarn org.xmlpull.**
-dontwarn org.kxml2.**
-dontwarn org.codehaus.stax2.**
-dontwarn org.codehaus.stax2.validation.**




##############################################
# 🔐 BOUNCY CASTLE (CRYPTO)
##############################################

-keep class org.bouncycastle.** { *; }
-dontwarn org.bouncycastle.**

-keep class javax.crypto.** { *; }
-dontwarn javax.crypto.**

##############################################
# 🎯 ML KIT / CAMERA / MEDIAPIPE
##############################################

-dontwarn com.google.mlkit.**
-dontwarn androidx.camera.**
-dontwarn com.google.mediapipe.**

##############################################
# 🎨 GLIDE
##############################################

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule

##############################################
# 🧱 COMPOSE (SAFE)
##############################################

-dontwarn androidx.compose.**

##############################################
# ⚙️ WORKMANAGER
##############################################

-keep class * extends androidx.work.ListenableWorker { *; }

##############################################
# 🧹 REMOVE LOGS (SECURITY)
##############################################

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

##############################################
# 🚫 REMOVE UNUSED WARNINGS
##############################################

-dontwarn java.awt.**
-dontwarn javax.swing.**
-dontwarn java.beans.**

##############################################
# 🧾 MOSHI (MINIMAL SAFE RULES)
##############################################

# Keep Moshi core (light)
-keep class com.squareup.moshi.** { *; }
-dontwarn com.squareup.moshi.**

##############################################
# 🔐 SSL PINNING (CRITICAL)
##############################################

# Keep OkHttp SSL + Certificate Pinner
-keep class okhttp3.CertificatePinner { *; }
-keep class okhttp3.internal.tls.** { *; }

# Keep TrustManager & SSL classes
-keep class javax.net.ssl.** { *; }
-dontwarn javax.net.ssl.**

# Keep X509 Certificates
-keep class java.security.cert.** { *; }
-dontwarn java.security.cert.**

# Keep your custom interceptor (important for pin logic)
-keep class com.kaushalpanjee.core.util.CustomInterceptor { *; }

# If using custom TrustManager
-keepclassmembers class * implements javax.net.ssl.X509TrustManager {
    public void checkServerTrusted(...);
    public void checkClientTrusted(...);
}

-keep class org.xmlpull.v1.** { *; }
-keep interface org.xmlpull.v1.** { *; }

# Ignore xmlpull (Android provides it)
-dontwarn org.xmlpull.v1.**
-dontwarn org.xmlpull.**

# Ignore StAX warnings
-dontwarn org.codehaus.stax2.**
-dontwarn javax.xml.stream.**

##############################################
# 🔐 PREVENT PIN STRING REMOVAL
##############################################

# Keep string constants (pins)
-keepclassmembers class * {
    java.lang.String *;
}



# ---- Ignore desktop Java classes (XStream issue) ----
-dontwarn java.awt.**
-dontwarn javax.swing.**

# ---- Ignore OSGi / bnd annotations (Woodstox / XML libs) ----
-dontwarn aQute.bnd.annotation.spi.**

# ---- Ignore StAX / XML streaming warnings ----
-dontwarn com.ctc.wstx.**
-dontwarn org.codehaus.stax2.**
-dontwarn javax.xml.stream.**

# ---- Ignore missing Koin annotations (only if you are NOT using Koin runtime) ----
-dontwarn org.koin.core.annotation.**

# ---- Keep XStream core (avoid aggressive stripping) ----
-keep class com.thoughtworks.xstream.** { *; }
-dontwarn com.thoughtworks.xstream.**

-keep class com.fasterxml.** { *; }
-dontwarn com.fasterxml.**

-keep class org.apache.xml.security.** { *; }
-dontwarn org.apache.xml.security.**

-keep class com.pehchaan.** { *; }

##############################################
# 🚀 FINAL SHRINKING OPTIMIZATION
##############################################

# Allow shrinking & obfuscation (IMPORTANT)
-allowaccessmodification