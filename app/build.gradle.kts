import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.jhainusa.jss_student"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.jhainusa.jss_student"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
        }
        buildConfigField("String", "GEMINI_API_KEY", "\"${localProperties.getProperty("GEMINI_API_KEY") ?: ""}\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
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
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.compose.material:material:1.6.0")
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.34.0")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.1") // or your Compose version

// Lifecycle + ViewModel (required for LiveData and ViewModel)
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    implementation("me.saket.swipe:swipe:1.3.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

// Converter for JSON parsing (Gson)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// OkHttp (used internally by Retrofit, and for logging)
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

// OkHttp Logging Interceptor (optional, useful for debugging)
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    kapt("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("io.coil-kt:coil-compose:2.2.2")

    implementation("androidx.compose.runtime:runtime-livedata:1.6.1")
    implementation ("com.airbnb.android:lottie-compose:6.0.0")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("androidx.compose.material3:material3:1.2.1")

        // Add the Firebase Cloud Messaging dependency
        implementation("com.google.firebase:firebase-messaging:24.0.0")


    // Glance for App Widgets
    implementation("androidx.glance:glance-appwidget:1.1.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
}