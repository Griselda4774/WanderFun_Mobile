plugins {
    alias(libs.plugins.android.application)
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.wanderfunmobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.wanderfunmobile"
        minSdk = 26
        targetSdk = 34
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.dagger.hilt.android)
    annotationProcessor(libs.hilt.compiler)
    implementation(libs.modelmapper)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
}