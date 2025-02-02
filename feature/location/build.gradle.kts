import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

val localDefaults = Properties()
val localDefaultsFile = rootProject.file("local.defaults.properties")
if (localDefaultsFile.exists()) {
    localDefaults.load(FileInputStream(localDefaultsFile))
}

android {
    namespace = "com.sevban.location"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "MAPS_API_KEY", "\"${localDefaults["MAPS_API_KEY"]}\"")
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
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.ui)
    implementation(projects.core.model)
    implementation(projects.core.common)
    implementation(projects.core.designsystem)

    implementation(libs.androidx.ktx)
    implementation(libs.androidx.appcompat)

    // Compose
    implementation(libs.bundles.compose)
    androidTestImplementation(libs.bundles.compose.testing)
    // Maps
    implementation(libs.google.maps.compose)
    implementation(libs.google.maps.compose.utils)

    // Coil
    implementation(libs.coil.compose)

    // Dagger-Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)

    // Serialization
    implementation(libs.kotlin.serialization)

    implementation(libs.google.maps.places)


    // Testing
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.android.testing)
}