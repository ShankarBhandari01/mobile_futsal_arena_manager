plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)

    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlinx-serialization")

}


android {
    namespace = "com.example.futsalmanager"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.futsalmanager"
        minSdk = 25
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.6.0"
    }

}

dependencies {

    // Google Maps for Compose
    implementation("com.google.maps.android:maps-compose:8.0.1")
    // Google Play Services for Maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    //  For Utils (like Clustering or Heatmaps)
    implementation("com.google.maps.android:maps-compose-utils:8.0.1")
    //permission
    implementation(libs.accompanist.permissions)
    // Play Services for Location
    implementation(libs.play.services.location)
    implementation(libs.androidx.core.splashscreen)
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.compose.material.icons)
    implementation(libs.hilt.android)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3.lint)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.play.services.maps)
    ksp("com.google.dagger:hilt-android-compiler:2.59")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    ksp("androidx.hilt:hilt-compiler:1.3.0")

    implementation(libs.androidx.datastore.preferences)
    implementation("androidx.security:security-crypto:1.1.0")

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")
    implementation(libs.ktor.client.logging)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation(libs.firebase.crashlytics)

    testImplementation("io.mockk:mockk:1.14.9")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    testImplementation("app.cash.turbine:turbine:1.2.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)


}