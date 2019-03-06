package ch.leadrian.samp.kamp.gradle.plugin.textkeygenerator

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

open class TextKeysGeneratorPluginExtension {

    var className: String = "TextKeys"

    var charset: Charset = StandardCharsets.ISO_8859_1
}
