# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# ==================== OTIMIZAÇÕES DE PERFORMANCE ====================

# Otimizações agressivas
-optimizationpasses 5
-allowaccessmodification
-repackageclasses ''

# Remover logs em release
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}

# ==================== ROOM DATABASE ====================

# Keep Room entities
-keep class com.gestantes.checklist.data.entity.** { *; }

# Keep Room DAOs
-keep class com.gestantes.checklist.data.dao.** { *; }

# ==================== GSON / SERIALIZAÇÃO ====================

# Keep data classes para Gson
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep UserPreferences data classes
-keep class com.gestantes.checklist.data.preferences.** { *; }

# Gson specific
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.stream.** { *; }

# ==================== COMPOSE ====================

# Compose performance
-dontwarn androidx.compose.**

# ==================== COIL ====================

# Coil image loading
-dontwarn coil.**
-keep class coil.** { *; }

