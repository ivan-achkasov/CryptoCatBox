import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val springVersion by extra { "6.0.6" }
val springDataVersion by extra { "3.0.4" }
val springBootVersion by extra { "3.0.4" }
val jacksonVersion by extra { "2.14.2" }
val slf4jVersion by extra { "2.0.6" }
val okHttpVersion by extra { "4.10.0" }
val binanceVersion by extra { "1.12.0" }
val liquibaseVersion by extra { "4.20.0" }
val coroutinesVersion by extra { "1.6.4" }
val postgresqlVersion by extra { "42.5.4" }
val h2Version by extra { "2.1.214" }

val jUnitVersion by extra { "5.9.2" }
val mockVersion by extra { "1.12.4" }

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    id("org.springframework.boot") version "3.0.4" apply false
    id("io.spring.dependency-management") version "1.1.0" apply false
    kotlin("jvm") version "1.6.21" apply false
    kotlin("plugin.spring") version "1.6.21" apply false
}

allprojects {
    group = "cryptocatbox"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

subprojects {
    repositories {
        mavenCentral()
    }

    apply {
        plugin("io.spring.dependency-management")
    }
}
