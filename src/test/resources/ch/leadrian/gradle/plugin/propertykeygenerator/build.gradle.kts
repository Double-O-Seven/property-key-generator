buildscript {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("ch.leadrian.property-key-generator")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = "5.5.1")
    testImplementation(group = "org.assertj", name = "assertj-core", version = "3.13.2")

    testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = "5.5.1")
}

version = "1.0.0"

propertyKeyGenerator {

    resourceBundle("wrapper-class-test") {
        bundlePackageName = "ch.leadrian.gradle.plugin.propertykeygenerator"

        wrapperClass {
            packageName = "ch.leadrian.gradle.plugin.propertykeygenerator"
            className = "TranslationKey"
        }
    }

    resourceBundle {
        bundleName = "factory-method-test"
        bundlePackageName = "ch.leadrian.gradle.plugin.propertykeygenerator"

        wrapperClass {
            packageName = "ch.leadrian.gradle.plugin.propertykeygenerator"
            className = "TranslationKey"
            factoryMethod = "valueOf"
        }
    }

    resourceBundle {
        bundleName = "string-constants-test"
        bundlePackageName = "ch.leadrian.gradle.plugin.propertykeygenerator"
    }

}

tasks {

    test {
        useJUnitPlatform()
    }
    
}