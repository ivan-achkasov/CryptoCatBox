val springVersion: String by rootProject.extra
val springDataVersion: String by rootProject.extra
val springBootVersion: String by rootProject.extra
val okHttpVersion: String by rootProject.extra
val jacksonVersion: String by rootProject.extra
val binanceVersion: String by rootProject.extra
val h2Version: String by rootProject.extra
val jUnitVersion: String by rootProject.extra
val liquibaseVersion: String by rootProject.extra

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":common"))

    implementation("io.github.binance:binance-connector-java:$binanceVersion") {
        exclude(group = "com.squareup.okhttp3")
    }
    api("com.squareup.okhttp3:okhttp:$okHttpVersion")

    implementation("org.springframework:spring-context:$springVersion")
    implementation("org.springframework:spring-web:$springVersion")
    implementation("org.springframework.data:spring-data-jdbc:$springDataVersion")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")


    testImplementation("com.h2database:h2:$h2Version")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("org.liquibase:liquibase-core:$liquibaseVersion")
}