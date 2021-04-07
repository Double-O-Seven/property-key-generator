import groovy.lang.Closure

plugins {
    `java-library`
    `java-gradle-plugin`
    kotlin("jvm") version "1.4.20"
    id("com.gradle.plugin-publish") version "0.14.0"
    id("com.palantir.git-version") version "0.12.3"
    id("org.jetbrains.dokka") version "1.4.20"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("gradle-plugin"))
    implementation(group = "com.squareup", name = "javapoet", version = "1.13.0")
    implementation(group = "com.google.guava", name = "guava", version = "30.1.1-jre")

    testImplementation(platform("org.junit:junit-bom:5.7.1"))
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api")
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-params")
    testImplementation(group = "org.assertj", name = "assertj-core", version = "3.19.0")

    testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine")
}

val gitVersion: Closure<String> by extra

version = gitVersion()

group = "ch.leadrian.propertykeygenerator"

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    compileKotlin {
        sourceCompatibility = "1.8"
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjvm-default=compatibility")
        }
    }

    compileTestKotlin {
        sourceCompatibility = "1.8"
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjvm-default=compatibility")
        }
    }

    test {
        useJUnitPlatform()
    }

    named<Jar>("javadocJar") {
        from(dokkaJavadoc)
    }
}

gradlePlugin {
    plugins {
        create("propertyKeyGeneratorPlugin") {
            id = "ch.leadrian.property-key-generator"
            implementationClass = "ch.leadrian.gradle.plugin.propertykeygenerator.PropertyKeyGeneratorPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/Double-O-Seven/property-key-generator"
    vcsUrl = "https://github.com/Double-O-Seven/property-key-generator"
    description = "Gradle plugin to generate property keys from properties files"
    tags = listOf("properties", "property", "key")

    (plugins) {
        "propertyKeyGeneratorPlugin" {
            displayName = "Property Generator Plugin"
        }
    }
}
