val springVersion: String by rootProject.extra
val jacksonVersion: String by rootProject.extra

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":common"))

    implementation("org.springframework:spring-context:$springVersion")
    implementation("org.springframework:spring-web:$springVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
}