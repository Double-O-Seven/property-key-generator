package ch.leadrian.gradle.plugin.propertykeygenerator.model

internal data class PropertyKey(val value: String) {

    companion object {

        const val PROPERTY_SEPARATOR = '.'

    }

    val segments: List<String>
        get() = value.split(PROPERTY_SEPARATOR)

}