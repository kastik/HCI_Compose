buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.15")
        //classpath(kotlin("gradle-plugin", version = "1.8.0"))
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.0.1" apply false
    id ("com.android.library" ) version "8.0.1" apply false
    id ("org.jetbrains.kotlin.android" ) version "1.8.20" apply false
    id("com.google.devtools.ksp") version "1.8.21-1.0.11" apply false
    //kotlin("jvm") version "1.8.0" apply false
}