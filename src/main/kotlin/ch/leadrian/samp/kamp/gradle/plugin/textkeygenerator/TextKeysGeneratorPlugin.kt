package ch.leadrian.samp.kamp.gradle.plugin.textkeygenerator

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.regex.Pattern

open class TextKeysGeneratorPlugin : Plugin<Project> {

    companion object {

        const val GENERATED_SOURCE_DIRECTORY = "generated-src/main/java"

        val STRINGS_FILE_PATTERN: Pattern = Pattern.compile("strings(_[a-z]{2}(_[A-Z]{2})?)?\\.properties")

    }

    override fun apply(project: Project) {
        createExtension(project)
        configureTask(project)
        configureSourceSets(project)
        applyJavaPlugin(project)
    }

    private fun createExtension(project: Project) {
        project.extensions.create("textKeyGenerator", TextKeysGeneratorPluginExtension::class.java)
    }

    private fun configureTask(project: Project) {
        val generateTextKeysTask = project.tasks.create("generateTextKeys", GenerateTextKeysTask::class.java)
        project.tasks.withType(JavaCompile::class.java) { it.dependsOn(generateTextKeysTask) }
        project.tasks.withType(KotlinCompile::class.java) { it.dependsOn(generateTextKeysTask) }
    }

    private fun configureSourceSets(project: Project) {
        project
                .convention
                .findPlugin(JavaPluginConvention::class.java)
                ?.sourceSets
                ?.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
                ?.java
                ?.srcDir(project.buildDir.resolve(GENERATED_SOURCE_DIRECTORY))
    }

    private fun applyJavaPlugin(project: Project) {
        project.pluginManager.apply(JavaPlugin::class.java)
    }
}
