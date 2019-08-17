package ch.leadrian.gradle.plugin.propertykeygenerator

open class ResourceBundleConfiguration : PropertyKeyGenerationSpec {

    override lateinit var bundleName: String

    override var className: String? = null

    override var resourceBundleNameCaseFormat: Any = PropertyKeyGenerationSpec.DEFAULT_RESOURCE_BUNDLE_CASE_FORMAT

    override lateinit var packageName: String

    override var pattern: String? = null

    override var wrapperClass: WrapperClassConfiguration? = null

    override var pathVariableName: String = PropertyKeyGenerationSpec.DEFAULT_PATH_VARIABLE_NAME

}