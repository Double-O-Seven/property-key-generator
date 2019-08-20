package ch.leadrian.gradle.plugin.propertykeygenerator

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

open class PropertyKeyGeneratorPlugin : Plugin<Project> {

    companion object {

        const val GENERATED_SOURCE_DIRECTORY = "generated/sources/propertyKeyGenerator/java/main"

    }

    override fun apply(project: Project) {
        applyJavaPlugin(project)
        val extension = createExtension(project)
        project.afterEvaluate {
            createTasks(extension, project)
        }
        configureSourceSets(project)
    }

    private fun applyJavaPlugin(project: Project) {
        project.pluginManager.apply(JavaPlugin::class.java)
    }

    private fun createExtension(project: Project): PropertyKeyGeneratorPluginExtension =
            project.extensions.create("propertyKeyGenerator", PropertyKeyGeneratorPluginExtension::class.java)

    private fun configureSourceSets(project: Project) {
        project
                .convention
                .findPlugin(JavaPluginConvention::class.java)
                ?.sourceSets
                ?.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
                ?.java
                ?.srcDir(project.buildDir.resolve(GENERATED_SOURCE_DIRECTORY))
    }

    private fun createTasks(extension: PropertyKeyGeneratorPluginExtension, project: Project) {
        val parentTask: Task = project.tasks.create("generatePropertyKeys")
        project.tasks.withType(JavaCompile::class.java) { it.dependsOn(parentTask) }
        project.tasks.withType(KotlinCompile::class.java) { it.dependsOn(parentTask) }
        extension.resourceBundles.forEach { resourceBundleConfiguration ->
            createTask(resourceBundleConfiguration, project, parentTask)
        }
    }

    private fun createTask(resourceBundleConfiguration: ResourceBundleConfiguration, project: Project, parentTask: Task) {
        val bundleName = resourceBundleConfiguration.upperCamelCaseBundleName()
        project.tasks.create("generate${bundleName}PropertyKeys", GeneratePropertyKeys::class.java) { generatePropertyKeys ->
            generatePropertyKeys.with(resourceBundleConfiguration)
            parentTask.dependsOn(generatePropertyKeys)
        }
    }
}
