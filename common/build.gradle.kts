val slf4jVersion: String by rootProject.extra
val okHttpVersion: String by rootProject.extra
val jUnitVersion: String by rootProject.extra

plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:$jUnitVersion")
}