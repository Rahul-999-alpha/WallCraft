plugins {
    // AGP 8.9.x = smallest bump that officially supports compileSdk 36 (Play's
    // 31 Aug 2026 target-API requirement) while keeping the Gradle 8.11.1 wrapper.
    id("com.android.application") version "8.9.3" apply false
    id("com.android.library") version "8.9.3" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0" apply false
    id("com.google.dagger.hilt.android") version "2.54" apply false
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.firebase.crashlytics") version "3.0.2" apply false
}
