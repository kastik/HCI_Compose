plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace="com.kastik.hci"
    compileSdk=33

    defaultConfig {
        applicationId="com.kastik.hci"
        minSdk=24
        targetSdk=33
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary=true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            //proguardFiles getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.6"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
    buildToolsVersion = "33.0.2"
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.1")
    implementation(platform("androidx.compose:compose-bom:2023.05.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.core:core-ktx:1.10.0")
    debugImplementation("androidx.compose.ui:ui-tooling")
    //val room_version = "2.5.1"
    implementation("androidx.room:room-runtime:2.5.1")
    annotationProcessor("androidx.room:room-compiler:2.5.1")
    kapt("androidx.room:room-compiler:2.5.1")
    // To use Kotlin Symbol Processing (KSP)
    //ksp("androidx.room:room-compiler:$room_version")


}