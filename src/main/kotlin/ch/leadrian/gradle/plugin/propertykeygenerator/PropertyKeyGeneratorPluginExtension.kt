package ch.leadrian.gradle.plugin.propertykeygenerator

import groovy.lang.Closure
import org.gradle.api.Action
import org.gradle.util.ConfigureUtil.configureSelf

open class PropertyKeyGeneratorPluginExtension {

    val resourceBundles: MutableList<ResourceBundleConfiguration> = mutableListOf()

    @JvmOverloads
    fun resourceBundle(bundleName: String? = null, action: Action<in ResourceBundleConfiguration>) {
        val resourceBundleConfiguration = ResourceBundleConfiguration()
        bundleName?.let { resourceBundleConfiguration.bundleName = it }
        action.execute(resourceBundleConfiguration)
        resourceBundles += resourceBundleConfiguration
    }

    @JvmOverloads
    fun resourceBundle(bundleName: String? = null, closure: Closure<in ResourceBundleConfiguration>) {
        val resourceBundleConfiguration = ResourceBundleConfiguration()
        bundleName?.let { resourceBundleConfiguration.bundleName = it }
        configureSelf(closure, resourceBundleConfiguration)
        resourceBundles += resourceBundleConfiguration
    }

}