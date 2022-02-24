plugins {
    id("org.jetbrains.kotlin.jvm")
}

val kotlin_version: String by project
val junitJupiterVersion: String by project

dependencies {
    api(project(":lib"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
    implementation("org.junit.jupiter:junit-jupiter-params:$junitJupiterVersion")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

kotlin.target.compilations.all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
