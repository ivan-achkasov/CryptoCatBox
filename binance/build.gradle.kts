plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":common"))

    implementation("io.github.binance:binance-connector-java:1.8.0")

    implementation("org.springframework:spring-context:5.3.22")
    implementation("org.springframework:spring-web:5.3.22")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
}