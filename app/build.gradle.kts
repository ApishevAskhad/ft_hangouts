plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.android.extensions")
    id("org.jetbrains.kotlin.kapt")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "ru.school21.gloras.ft_hangouts"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.21")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.core:core-ktx:1.3.2")

    //Asynchronous
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")

    //UI
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.recyclerview:recyclerview:1.1.0")

    //DI
    implementation("com.google.dagger:dagger:2.31.2")
    kapt("com.google.dagger:dagger-compiler:2.31.2")

    //Database
    implementation("androidx.room:room-runtime:2.2.6")
    implementation("androidx.room:room-ktx:2.2.6")
    kapt("androidx.room:room-compiler:2.2.6")
}

repositories {
    mavenCentral()
}