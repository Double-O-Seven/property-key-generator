package ch.leadrian.gradle.plugin.propertykeygenerator

internal object PropertyKeysClassNameResolver {

    fun resolve(spec: PropertyKeyGenerationSpec): String =
            spec.className ?: spec.upperCamelCaseBundleName() + "Keys"

}