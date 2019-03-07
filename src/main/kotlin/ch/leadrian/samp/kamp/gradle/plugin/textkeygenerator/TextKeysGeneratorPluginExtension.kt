package ch.leadrian.samp.kamp.gradle.plugin.textkeygenerator

import org.gradle.api.tasks.Input

open class TextKeysGeneratorPluginExtension {

    @get:Input
    var className: String = "TextKeys"

}