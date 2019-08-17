[![Build Status](https://travis-ci.org/Double-O-Seven/property-key-generator.svg?branch=master)](https://travis-ci.org/Double-O-Seven/property-key-generator)
 [![Gradle Plugins Release](https://img.shields.io/github/tag/Double-O-Seven/property-key-generator.svg)](https://plugins.gradle.org/plugin/ch.leadrian.property-key-generator)

# Property Key Generator Gradle Plugin

A simple Gradle plugin that generates `ch.leadrian.samp.kamp.core.api.text.TextKey`s for Kamp.

Simple put a `strings.properties` file in your gamemodes package in `src/main/resources` of your project.

You may also specify a locale for your properties file. The following examples are all valid file names:
  * `strings.properties`
  * `strings_de_DE.properties`
  * `strings_EN.properties`

If your gamemode has the Java package `com.my.amazing.gamemode`, put your `strings.properties` files into the directory `src/main/resources/com/my/amazing/gamemode`.  The plugin automatically generate a class containing all property keys defined in the files.

Let's assume your `strings.properties` file contains the following properties and is located in the package `com.my.amazing.gamemode`:
```
sadm.welcome.message=What's up?
sadm.ban.message=Have fun on another server!
```
The plugin will generate a class named `TextKeys` that will allow you to access the property keys safely like this:
```kotlin
import com.my.amazing.gamemode

val textKey1 = TextKeys.sadm.welcome.message
val textKey2 = TextKeys.sadm.ban.message
```

Tasks and extension
-------------------

The plugin provides a single task `generateTextKeys` that generates `TextKeys` Java classes. They will automatically be added to the `main` source set. No manually adding of the generated code is required.

The plugin also provides the `textKeyGenerator` extension that allows you to override the name of the generate Java classes.

A typical Gradle setup using the plugin will look like this:

```kotlin
plugins {
    id("ch.leadrian.samp.kamp.property-key-generator") version "1.0.0-rc2"
    // Other plugins
}

textKeyGenerator {
    // Override the class name (recommended)
    className = "LvdmTextKeys"
}
```

For working examples have a look at the [kamp-examples](https://github.com/Double-O-Seven/kamp-examples) repository which contains several example gamemodes.
