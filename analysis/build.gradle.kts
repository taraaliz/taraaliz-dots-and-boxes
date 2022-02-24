plugins {
    kotlin("jvm")
    application
}

val kotlin_version: String by project

dependencies {
    implementation(project(":extLib"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClass.set("uk.ac.bournemouth.ap.analysis.ComputerPlayerTesterKt")
}

kotlin.target.compilations.all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
