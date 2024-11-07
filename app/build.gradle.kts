import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.dagger.hilt)
    alias(libs.plugins.realm.kotlin)
    id("kotlin-kapt")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "org.maternalcare"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.maternalcare"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        val envFile = project.rootProject.file("local.properties")
        val properties = Properties()
        properties.load(envFile.inputStream())

        buildConfigField("String", "REALM_APP_ID", properties.getProperty("realm.app.id"))
        buildConfigField("String", "REALM_API_KEY", properties.getProperty("realm.app.api.key"))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "META-INF/NOTICE.md"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.android.hilt)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.places)
    kapt(libs.android.hilt.compiler)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.realm.kotlin.base)
    implementation(libs.realm.kotlin.sync)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.jbcrypt)
    implementation(libs.play.services.maps)
    implementation(libs.maps.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.coil.compose)
    implementation (libs.itextpdf)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

}

kapt {
    correctErrorTypes = true
}
