package ch.leadrian.samp.kamp.gradle.plugin.textkeygenerator

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.Properties

internal fun Path.loadProperties(): Properties {
    val properties = Properties()
    Files.newBufferedReader(this, StandardCharsets.ISO_8859_1).use { reader -> properties.load(reader) }
    return properties
}