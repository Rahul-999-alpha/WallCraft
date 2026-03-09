# ── Retrofit ──────────────────────────────────────────────────────────────────
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# ── Gson ──────────────────────────────────────────────────────────────────────
-keepattributes Signature
-keep class com.google.gson.** { *; }
-keep class com.rahul.clearwalls.data.remote.dto.** { *; }

# ── Room ──────────────────────────────────────────────────────────────────────
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *

# ── OkHttp ────────────────────────────────────────────────────────────────────
-dontwarn okhttp3.**
-dontwarn okio.**

# ── Firebase ──────────────────────────────────────────────────────────────────
-keep class com.google.firebase.** { *; }

# ── Coil 3 ────────────────────────────────────────────────────────────────────
-keep class coil3.** { *; }
-keep interface coil3.** { *; }
-dontwarn coil3.**
-keepclassmembers class coil3.** { *; }

# ── Hilt ──────────────────────────────────────────────────────────────────────
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# ── Google Mobile Ads (AdMob) ─────────────────────────────────────────────────
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.ads.** { *; }
-dontwarn com.google.android.gms.ads.**

# ── Google Play Services ──────────────────────────────────────────────────────
-keep class com.google.android.gms.common.** { *; }
-keep class com.google.android.gms.internal.** { *; }
-dontwarn com.google.android.gms.internal.**

# ── BUG-015 FIX: ClearWalls core classes ─────────────────────────────────────
-keep class com.rahul.clearwalls.core.** { *; }
-keep class com.rahul.clearwalls.data.repository.** { *; }

# BUG-015 FIX: Preserve Hilt-injected constructors
-keepclassmembers class com.rahul.clearwalls.** {
    @javax.inject.Inject <init>(...);
}

# BUG-015 FIX: Preserve DataStore Preferences key constants
-keepclassmembers class com.rahul.clearwalls.data.local.** {
    static final java.lang.String *;
    static final androidx.datastore.preferences.core.Preferences$Key *;
}
