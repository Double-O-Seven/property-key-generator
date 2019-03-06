package ch.leadrian.samp.kamp.gradle.plugin.textkeygenerator

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

internal fun Path.loadProperties(charset: Charset): Properties {
    val properties = Properties()
    Files.newBufferedReader(this, charset).use { reader -> properties.load(reader) }
    return properties
}