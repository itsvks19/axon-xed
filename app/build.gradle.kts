import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization") version "2.1.21"
}

android {
    namespace = "com.axon"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.axon"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            //proguard must be disabled
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        //should match with xed-editor
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += arrayOf(
                "META-INF/INDEX.LIST",
                "META-INF/io.netty.versions.properties"
            )
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

//always try to match the versions of library to the versions used in xed-editor
dependencies {
    compileOnly(files("libs/xed-editor-sdk.jar"))

    //if a library is common in both your plugin and xed-editor then you should use compileOnly() instead of implementation()
    compileOnly(libs.appcompat)
    compileOnly(libs.material)
    compileOnly(libs.constraintlayout)
    compileOnly(libs.navigation.fragment)
    compileOnly(libs.navigation.ui)
    compileOnly(libs.asynclayoutinflater)
    compileOnly(libs.navigation.fragment.ktx)
    compileOnly(libs.navigation.ui.ktx)
    implementation(libs.activity)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    compileOnly(libs.activity.compose)
    compileOnly(platform(libs.compose.bom))
    compileOnly(libs.ui)
    compileOnly(libs.ui.graphics)
    compileOnly(libs.material3)
    compileOnly(libs.navigation.compose)
    compileOnly(libs.utilcode)
    compileOnly(libs.coil.compose)
    //compileOnly(libs.org.eclipse.jgit)
    compileOnly(libs.gson)
    compileOnly(libs.commons.net)

    implementation(libs.koog.agents)
    implementation(libs.icons.lucide)

    //implementation(kotlin("reflect"))
    compileOnly("org.jetbrains.kotlin:kotlin-reflect:2.0.10")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

// compileOnly(libs.jcodings)
    // compileOnly(libs.joni)
    // compileOnly(libs.snakeyaml.engine)
    //compileOnly(libs.jdt.annotation)
//    compileOnly(libs.okhttp)
//    compileOnly(libs.material.motion.compose.core)
//    compileOnly(libs.nanohttpd)
//    compileOnly(libs.photoview)
//    compileOnly(libs.glide)
//    compileOnly(libs.media3.exoplayer)
//    compileOnly(libs.media3.exoplayer.dash)
//    compileOnly(libs.media3.ui)
//    compileOnly(libs.browser)
//    compileOnly(libs.quickjs.android)
//    compileOnly(libs.anrwatchdog)
//    compileOnly(libs.word.wrap)
}
