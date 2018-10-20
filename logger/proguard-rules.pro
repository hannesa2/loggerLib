# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/hannes/Development/adt/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

#sqlcipher
-keep class net.sqlcipher.** {
    *;
}
-keep class net.sqlcipher.database.** {
    *;
}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
-keep class sun.misc.Unsafe { *; }
-dontnote sun.misc.Unsafe
#-keep class com.google.gson.stream.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializertheUnsafe
-dontnote com.google.gson.JsonDeserializertheUnsafe
-dontnote com.google.gson.internal.UnsafeAllocator
-dontnote com.google.gson.internal.reflect.UnsafeReflectionAccessor
##---------------End: proguard configuration for Gson  ----------
# Kotlin
-keep class kotlin.Metadata { *; }
-dontnote kotlin.internal.PlatformImplementationsKt
-dontnote kotlin.reflect.jvm.internal.**
