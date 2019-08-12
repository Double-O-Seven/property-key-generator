import groovy.lang.Closure

plugins {
    kotlin("jvm") version "1.3.11"
    `java-library`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.10.1"
    id("org.jetbrains.dokka") version "0.9.17"
    id("com.palantir.git-version") version "0.12.0-rc2"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-gradle-plugin", version = "1.3.11")
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8", version = "1.3.11")
    implementation(group = "com.squareup", name = "javapoet", version = "1.11.1")

    api(gradleApi())

    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = "5.4.0")
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-params", version = "5.4.0")
    testImplementation(group = "org.assertj", name = "assertj-core", version = "3.11.1")

    testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = "5.4.0")
}

val gitVersion: Closure<String> by extra

version = gitVersion()

group = "ch.leadrian.propertykeygenerator"

tasks.register<Jar>("sourcesJar") {
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
}

tasks.register<Jar>("javadocJar") {
    from(tasks.dokka)
    archiveClassifier.set("javadoc")
}

tasks {
    compileKotlin {
        sourceCompatibility = "1.8"
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    compileTestKotlin {
        sourceCompatibility = "1.8"
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    test {
        useJUnitPlatform()
    }

    dokka {
        reportUndocumented = false
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
    description = "Gradle plugin to property keys from properties files"
    tags = listOf("properties", "property", "key")

    (plugins) {
        "propertyKeyGeneratorPlugin" {
            displayName = "Property Generator Plugin"
        }
    }
}
