val springVersion: String by rootProject.extra
val springBootVersion: String by rootProject.extra
val coroutinesVersion: String by rootProject.extra
val postgresqlVersion: String by rootProject.extra
val liquibaseVersion: String by rootProject.extra

val jUnitVersion: String by rootProject.extra
val mockVersion: String by rootProject.extra

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":binance"))
    implementation(project(":whitebit"))
    implementation(project(":pancakeswap"))

    implementation("org.springframework:spring-jdbc:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("org.liquibase:liquibase-core:$liquibaseVersion")

    testImplementation("io.mockk:mockk:$mockVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")
}