# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keepattributes *Annotation*
-keepattributes Signature
-keep class com.google.gson.reflect.TypeToken { *; }



-keep class org.simpleframework.xml.** { *; }
-dontwarn org.simpleframework.xml.**

-keep class com.kaushalpanjee.model.kyc_resp_pojo.** { *; }
-keep class com.kaushalpanjee.model.** { *; }

-keepclassmembers class * {
    @org.simpleframework.xml.* <fields>;
    @org.simpleframework.xml.* <methods>;
}

-keepclassmembers class ** {
    @org.simpleframework.xml.Root <init>(...);
    @org.simpleframework.xml.Element *;
    @org.simpleframework.xml.Attribute *;
}

-keep class com.kaushalpanjee.common.model.** { *; }
-keep class com.kaushalpanjee.core.domain.** { *; }
-keep class com.kaushalpanjee.pojo.** { *; }

# Room (Added for entity/DAO serialization)
-keep class * extends androidx.room.** { *; }
-dontwarn androidx.room.**

# Keep hilt generated code
-keep class dagger.hilt.** { *; }
-keep interface dagger.hilt.** { *; }
-keep class dagger.** { *; }
-keep interface dagger.** { *; }
-keep class javax.inject.** { *; }
-keep interface javax.inject.** { *; }
-dontwarn dagger.hilt.**
-dontwarn javax.inject.**

# Keep Dagger modules (like AppModule)
-keep @dagger.Module class * { *; }
-keepclasseswithmembers class * {
    @dagger.Provides <methods>;
}

# Retrofit
-keepattributes Signature, Exceptions, InnerClasses, *Annotation*
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-keep interface retrofit2.http.* { *; }

# OkHttp
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Gson
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

# Moshi
-keep class com.squareup.moshi.** { *; }
-dontwarn com.squareup.moshi.**

-keepclassmembers class com.kaushalpanjee.core.util.CustomInterceptor {
    public <methods>;
}

-keep class ** extends android.app.Application { *; }

#-keep class org.simpleframework.* { *; }
#-keepclasseswithmembers class org.simpleframework.** { *; }
# ... (your existing rules unchanged up to the XML section)

# Ignore xmlpull / stax2 warnings during R8 shrink/minify
# Suppress warnings for xmlpull and related classes
-dontwarn org.xmlpull.v1.**
-dontnote org.xmlpull.v1.**
-dontwarn org.xmlpull.mxp1.**
-dontwarn org.xmlpull.**
-dontwarn android.content.res.**  # ✅ Suppresses framework inversion (safe per #123054725)
-dontwarn org.codehaus.stax2.validation.**

# Keep XmlPullParser classes intact (BROADENED)
-keep class org.xmlpull.** { *; }  # ✅ UPDATED: org.xmlpull.** (not just v1)
-keepclassmembers class org.xmlpull.** { *; }

# Optional: If using KXML or similar, add
-dontwarn org.kxml2.io.**

# StAX2/Woodstox validation (fixes META-INF services warnings)
-dontwarn org.codehaus.stax2.validation.**  # ✅ NEW: Covers DTD/RelaxNG/W3C schema factories


-dontwarn aQute.bnd.annotation.spi.**
-keep class aQute.bnd.annotation.spi.ServiceProvider.** { *; }

# Java AWT/Swing (XStream desktop converters - safe to suppress on Android)
-dontwarn java.awt.**
-keep class java.awt.Color.* { *; }
-keep class java.awt.Font.* { *; }
-dontwarn javax.swing.**
-keep class javax.swing.LookAndFeel.* { *; }
-keep class javax.swing.plaf.FontUIResource.* { *; }

# Java Beans (Jackson Java7Support)
-dontwarn java.beans.**
-keep class java.beans.ConstructorProperties.* { *; }
-keep class java.beans.Transient.* { *; }

# Java Beans (Jackson Java7Support)
-dontwarn java.beans.**
-keep class java.beans.ConstructorProperties.* { *; }
-keep class java.beans.Transient.* { *; }

# Koin DI (From pehchaanlib.aar - Keep if using injection in face detection)
-dontwarn org.koin.core.annotation.**
-keep class org.koin.core.annotation.Single.* { *; }
-keep @org.koin.core.annotation.Single.* class * { *; }  # Preserves annotated classes

-keep class org.bouncycastle.** { *; }
-dontwarn org.bouncycastle.**
-keep class javax.crypto.** { *; }
-dontwarn javax.crypto.**