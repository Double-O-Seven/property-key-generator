package ch.leadrian.gradle.plugin.propertykeygenerator

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

open class WrapperClassConfiguration {

    @get:Input
    lateinit var className: String

    @get:Input
    lateinit var packageName: String

    @get:[Optional Input]
    var factoryMethod: String? = null
}