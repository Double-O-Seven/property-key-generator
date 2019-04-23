import groovy.lang.Closure

plugins {
    kotlin("jvm") version "1.3.11"
    `java-library`
    `java-gradle-plugin`
    `maven-publish`
    maven
    signing
    `build-scan`
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

group = "ch.leadrian.samp.kamp"

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
        create("kampTextKeyGeneratorPlugin") {
            id = "ch.leadrian.samp.kamp.kamp-textkey-generator"
            implementationClass = "ch.leadrian.samp.kamp.gradle.plugin.textkeygenerator.TextKeysGeneratorPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/Double-O-Seven/kamp-textkey-generator"
    vcsUrl = "https://github.com/Double-O-Seven/kamp-textkey-generator"
    description = "Gradle plugin to generate text keys for Kamp from property files"
    tags = listOf("samp")

    (plugins) {
        "kampTextKeyGeneratorPlugin" {
            displayName = "Kamp TextKey Generator plugin"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            pom {
                name.set("Kamp TextKey Generator")
                description.set("Gradle Plugin to generate Kamp TextKeys")
                url.set("https://github.com/Double-O-Seven/kamp-textkey-generator")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("Double-O-Seven")
                        name.set("Adrian-Philipp Leuenberger")
                        email.set("thewishwithin@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/Double-O-Seven/kamp-textkey-generator.git")
                    developerConnection.set("scm:git:ssh://github.com/Double-O-Seven/kamp-textkey-generator.git")
                    url.set("https://github.com/Double-O-Seven/kamp-textkey-generator")
                }
            }
        }
    }
    repositories {
        maven {
            val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
            val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            url = if (version.toString().contains("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                val ossrhUsername: String? by extra
                val ossrhPassword: String? by extra
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
}
