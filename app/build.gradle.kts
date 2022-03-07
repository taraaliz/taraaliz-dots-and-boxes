plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val kotlin_version: String by project
val nav_version: String by project

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "uk.ac.bournemouth.ap.dotsandboxes.myUniqueId"
        minSdk = 20
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
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
//    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation(project(":logic"))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    // Kotlin
    implementation("android.arch.core:core-testing:1.1.1")
}
