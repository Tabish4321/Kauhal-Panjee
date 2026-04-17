plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.navigationSafeargsKotlin) apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false

}

