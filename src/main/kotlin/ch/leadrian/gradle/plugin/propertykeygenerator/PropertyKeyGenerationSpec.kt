package ch.leadrian.gradle.plugin.propertykeygenerator

import com.google.common.base.CaseFormat
import groovy.lang.Closure
import org.gradle.api.Action
import org.gradle.util.ConfigureUtil.configureSelf

interface PropertyKeyGenerationSpec {

    companion object {

        val DEFAULT_RESOURCE_BUNDLE_CASE_FORMAT = CaseFormat.LOWER_HYPHEN

        const val DEFAULT_PATH_VARIABLE_NAME = "PATH"

        const val DEFAULT_STRING_VALUE_PREFIX = ""

        const val DEFAULT_STRING_VALUE_SUFFIX = "_"

    }

    var bundleName: String

    var bundlePackageName: String

    var outputClassName: String?

    var outputPackageName: String?

    var bundleNameCaseFormat: Any

    var pattern: String?

    var wrapperClass: WrapperClassConfiguration?

    var pathVariableName: String

    var stringValuePrefix: String

    var stringValueSuffix: String

    @JvmDefault
    fun wrapperClass(action: Action<in WrapperClassConfiguration>) {
        if (wrapperClass == null) {
            wrapperClass = WrapperClassConfiguration()
        }
        wrapperClass?.let(action::execute)
    }

    @JvmDefault
    fun wrapperClass(closure: Closure<in WrapperClassConfiguration>) {
        if (wrapperClass == null) {
            wrapperClass = WrapperClassConfiguration()
        }
        configureSelf(closure, wrapperClass!!)
    }
}

internal fun PropertyKeyGenerationSpec.upperCamelCaseBundleName(): String =
        CaseFormat.valueOf(bundleNameCaseFormat.toString()).to(CaseFormat.UPPER_CAMEL, bundleName)

internal val PropertyKeyGenerationSpec.resolvedOutputClassName: String
    get() = outputClassName ?: upperCamelCaseBundleName() + "Keys"

internal val PropertyKeyGenerationSpec.resolvedOutputPackageName: String
    get() = outputPackageName ?: bundlePackageName