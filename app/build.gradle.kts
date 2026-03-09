import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) load(file.inputStream())
}

// BUG-002/BUG-003 FIX: Validates required keys exist before building release.
fun requireKey(key: String): String =
    localProperties.getProperty(key)?.takeIf { it.isNotBlank() }
        ?: error("$key is missing or blank in local.properties. Required for release build.")

android {
    namespace = "com.rahul.clearwalls"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rahul.clearwalls"
        minSdk = 26
        targetSdk = 35
        versionCode = 6
        versionName = "1.0.5"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Active content API keys
        buildConfigField("String", "PEXELS_API_KEY",      "\"${localProperties.getProperty("PEXELS_API_KEY",      "")}\"")
        buildConfigField("String", "UNSPLASH_ACCESS_KEY", "\"${localProperties.getProperty("UNSPLASH_ACCESS_KEY", "")}\"")
        buildConfigField("String", "STABILITY_AI_API_KEY","\"${localProperties.getProperty("STABILITY_AI_API_KEY","")}\"")

        // DISABLED APIs — empty stubs so source files compile. Not wired into the pipeline.
        buildConfigField("String", "PIXABAY_API_KEY",        "\"\"")
        buildConfigField("String", "WALLHAVEN_API_KEY",      "\"\"")
        buildConfigField("String", "PINTEREST_ACCESS_TOKEN", "\"\"")
        buildConfigField("String", "FREEPIK_API_KEY",        "\"\"")

        // AdMob IDs — empty in defaultConfig; debug overrides to test IDs, release enforces real IDs.
        buildConfigField("String", "ADMOB_BANNER_ID",       "\"\"")
        buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\"\"")
        buildConfigField("String", "ADMOB_REWARDED_ID",     "\"\"")
        buildConfigField("String", "ADMOB_NATIVE_ID",       "\"\"")
        buildConfigField("String", "ADMOB_APP_OPEN_ID",     "\"\"")

        // BUG-003: admobAppId has no fallback here — each buildType must supply it explicitly.
        manifestPlaceholders["admobAppId"] = localProperties.getProperty("ADMOB_APP_ID", "")
    }

    // BUG-001 FIX: signingConfig reads from local.properties.
    signingConfigs {
        create("release") {
            storeFile     = file(localProperties.getProperty("KEY_STORE_PATH", "KEYSTORE_NOT_CONFIGURED.jks"))
            storePassword = localProperties.getProperty("KEY_STORE_PASS", "")
            keyAlias      = localProperties.getProperty("KEY_ALIAS", "")
            keyPassword   = localProperties.getProperty("KEY_PASS", "")
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            // Use real AdMob IDs — debug builds are distributed to users.
            buildConfigField("String", "ADMOB_BANNER_ID",       "\"${localProperties.getProperty("ADMOB_BANNER_ID",       "")}\"")
            buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\"${localProperties.getProperty("ADMOB_INTERSTITIAL_ID", "")}\"")
            buildConfigField("String", "ADMOB_REWARDED_ID",     "\"${localProperties.getProperty("ADMOB_REWARDED_ID",     "")}\"")
            buildConfigField("String", "ADMOB_NATIVE_ID",       "\"${localProperties.getProperty("ADMOB_NATIVE_ID",       "")}\"")
            buildConfigField("String", "ADMOB_APP_OPEN_ID",     "\"${localProperties.getProperty("ADMOB_APP_OPEN_ID",     "")}\"")
            manifestPlaceholders["admobAppId"] = localProperties.getProperty("ADMOB_APP_ID", "")
        }

        release {
            isMinifyEnabled   = true
            isShrinkResources = true
            signingConfig     = signingConfigs.getByName("release") // BUG-001 FIX

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // BUG-002 FIX: requireKey() validates every AdMob ID at build time.
            buildConfigField("String", "ADMOB_BANNER_ID",       "\"${requireKey("ADMOB_BANNER_ID")}\"")
            buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\"${requireKey("ADMOB_INTERSTITIAL_ID")}\"")
            buildConfigField("String", "ADMOB_REWARDED_ID",     "\"${requireKey("ADMOB_REWARDED_ID")}\"")
            buildConfigField("String", "ADMOB_NATIVE_ID",       "\"${requireKey("ADMOB_NATIVE_ID")}\"")
            buildConfigField("String", "ADMOB_APP_OPEN_ID",     "\"${requireKey("ADMOB_APP_OPEN_ID")}\"")

            // BUG-003 FIX: admobAppId validated — no test-ID fallback possible.
            manifestPlaceholders["admobAppId"] = requireKey("ADMOB_APP_ID")

            // Active content API keys validated for release.
            buildConfigField("String", "PEXELS_API_KEY",       "\"${requireKey("PEXELS_API_KEY")}\"")
            buildConfigField("String", "UNSPLASH_ACCESS_KEY",  "\"${requireKey("UNSPLASH_ACCESS_KEY")}\"")
            // Stability AI key is optional — AI Create tab shows error if missing.
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures { compose = true; buildConfig = true }
}

dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-process:2.8.7")
    implementation("androidx.activity:activity-compose:1.9.3")

    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.animation:animation")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.5")

    // Hilt DI
    implementation("com.google.dagger:hilt-android:2.54")
    ksp("com.google.dagger:hilt-compiler:2.54")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("androidx.hilt:hilt-work:1.2.0")
    ksp("androidx.hilt:hilt-compiler:1.2.0")

    // Retrofit + OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Coil
    implementation("io.coil-kt.coil3:coil-compose:3.0.4")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-paging:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Paging 3
    implementation("androidx.paging:paging-runtime-ktx:3.3.5")
    implementation("androidx.paging:paging-compose:3.3.5")

    // Firebase BOM
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-config-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    // AdMob
    implementation("com.google.android.gms:play-services-ads:23.6.0")

    // BUG-013 FIX: Material Components required for Theme.MaterialComponents parent
    implementation("com.google.android.material:material:1.12.0")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.10.0")

    // Google Fonts
    implementation("androidx.compose.ui:ui-text-google-fonts:1.7.6")

    // Gson
    implementation("com.google.code.gson:gson:2.11.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.12.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
