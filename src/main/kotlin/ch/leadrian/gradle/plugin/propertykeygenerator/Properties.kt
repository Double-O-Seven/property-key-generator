package ch.leadrian.gradle.plugin.propertykeygenerator

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.Properties

internal fun File.loadProperties(): Properties {
    val properties = Properties()
    BufferedReader(FileReader(this)).use { reader -> properties.load(reader) }
    return properties
}