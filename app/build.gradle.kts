// FILE: app/build.gradle.kts (Inside app folder)
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.hawk.launcher"
    compileSdk = 34 

    defaultConfig {
        applicationId = "com.hawk.launcher"
        minSdk = 26
        targetSdk = 34
        versionCode = 15
        versionName = "3.3"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.drawerlayout:drawerlayout:1.1.1")
}
