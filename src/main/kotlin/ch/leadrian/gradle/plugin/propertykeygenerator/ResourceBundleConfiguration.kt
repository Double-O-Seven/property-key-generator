package ch.leadrian.gradle.plugin.propertykeygenerator

open class ResourceBundleConfiguration : PropertyKeyGenerationSpec {

    override lateinit var bundleName: String

    override lateinit var bundlePackageName: String

    override var outputClassName: String? = null

    override var outputPackageName: String? = null

    override var bundleNameCaseFormat: Any = PropertyKeyGenerationSpec.DEFAULT_RESOURCE_BUNDLE_CASE_FORMAT

    override var pattern: String? = null

    override var wrapperClass: WrapperClassConfiguration? = null

    override var pathVariableName: String = PropertyKeyGenerationSpec.DEFAULT_PATH_VARIABLE_NAME

    override var stringValuePrefix: String = PropertyKeyGenerationSpec.DEFAULT_STRING_VALUE_PREFIX

    override var stringValueSuffix: String = PropertyKeyGenerationSpec.DEFAULT_STRING_VALUE_SUFFIX

}