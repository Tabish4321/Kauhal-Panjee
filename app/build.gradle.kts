import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.navigationSafeargsKotlin)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.kaushalpanjee"
    compileSdk = 35

    val  keystorePropertiesFile = rootProject.file("keystore.properties")
    val projectProperties = readProperties(keystorePropertiesFile)

    defaultConfig {
        applicationId = "com.kaushalpanjee"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "CLIENT_SECRET_KEY", projectProperties["CLIENT_SECRET_KEY"] as String)
            buildConfigField("String", "REFRESH_TOKEN_URL", projectProperties["REFRESH_TOKEN_URL"] as String)
        }

        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "CLIENT_SECRET_KEY", projectProperties["CLIENT_SECRET_KEY"] as String)
            buildConfigField("String", "REFRESH_TOKEN_URL", projectProperties["REFRESH_TOKEN_URL"] as String)
        }
    }

    buildFeatures{
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    flavorDimensions += listOf("app")
    productFlavors {
        create("dev") {
            dimension = "app"
            buildConfigField("String", "BASE_URL", projectProperties["BASE_URL_DEV"] as String)

        }
        create("prod") {
            dimension = "app"
            buildConfigField("String", "BASE_URL", projectProperties["BASE_URL_PROD"] as String)

        }
    }
}

fun readProperties(propertiesFile: File) = Properties().apply {
    propertiesFile.inputStream().use { fis ->
        load(fis)
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.viewbinding)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation (libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.fragment.ktx)

    implementation(libs.androidx.navigation.ui.ktx)

    implementation(libs.androidx.room.ktx)
    implementation (libs.androidx.room.runtime)
    kapt (libs.androidx.room.compiler)

    implementation (libs.bundles.retrofitBundle)
    implementation (libs.chucker)

    //circle image

    implementation (libs.circleimageview)


    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.datastore.preferences.core)
    implementation(libs.androidx.datastore.preferences)

    //Glide
    implementation (libs.glide)
    annotationProcessor (libs.compiler)

    implementation (libs.androidx.core.splashscreen)

    //Xml
    implementation(libs.jackson.dataformat.xml)
    implementation("com.thoughtworks.xstream:xstream:1.4.7") {
        exclude(group = "xmlpull", module = "xmlpull")
    }

    implementation(libs.bcprov.jdk16)
    implementation(libs.jsr105.api)
    implementation("org.apache.santuario:xmlsec:2.0.3") {
        exclude(group = "org.codehaus.woodstox")
    }
    implementation(libs.stax.api)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

}

kapt {
    correctErrorTypes = true
}