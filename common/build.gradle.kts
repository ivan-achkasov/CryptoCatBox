plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.6")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}