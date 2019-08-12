package ch.leadrian.gradle.plugin.propertykeygenerator

import org.gradle.api.tasks.Input

open class PropertyKeyGeneratorPluginExtension {

    @get:Input
    var className: String = "TextKeys"

}