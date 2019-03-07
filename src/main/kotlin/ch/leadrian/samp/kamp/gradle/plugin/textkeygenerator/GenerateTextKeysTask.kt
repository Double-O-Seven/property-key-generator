package ch.leadrian.samp.kamp.gradle.plugin.textkeygenerator

import org.gradle.api.DefaultTask
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

open class GenerateTextKeysTask : DefaultTask() {

    @get:Nested
    internal val extension: TextKeysGeneratorPluginExtension by lazy {
        project.extensions.getByType(TextKeysGeneratorPluginExtension::class.java)
    }

    private val resourcesDirectories: List<Path> by lazy {
        project
                .convention
                .findPlugin(JavaPluginConvention::class.java)
                ?.sourceSets
                ?.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
                ?.resources
                ?.sourceDirectories
                ?.files
                ?.map { it.toPath() }
                .orEmpty()
    }

    private val stringsPropertiesFilesByPackageName: Map<String, Collection<Path>> by lazy {
        StringsPropertiesFileCollector.getStringsPropertyFilesByPackageName(resourcesDirectories)
    }

    @InputFiles
    fun getInputFiles(): List<Path> = stringsPropertiesFilesByPackageName.values.flatten()

    @OutputFiles
    fun getOutputFiles(): List<Path> =
            stringsPropertiesFilesByPackageName.keys.map { packageName ->
                getOutputDirectory(packageName).resolve("${extension.className}.java")
            }

    @TaskAction
    fun generateTextKeys() {
        stringsPropertiesFilesByPackageName.forEach { packageName, stringsPropertiesFiles ->
            generateTextKeys(packageName, stringsPropertiesFiles)
        }
    }

    private fun generateTextKeys(packageName: String, stringsPropertiesFiles: Collection<Path>) {
        val propertyKeys = getPropertyKeys(stringsPropertiesFiles)
        val outputDirectory = getOutputDirectory(packageName)
        Files.createDirectories(outputDirectory)
        val textKeysJavaFile = outputDirectory.resolve("${extension.className}.java")

        Files.newBufferedWriter(textKeysJavaFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE).use { writer ->
            TextKeysGenerator.generateTextKeyClasses(
                    rootClassName = extension.className,
                    packageName = packageName,
                    propertyKeys = propertyKeys,
                    writer = writer
            )
        }
    }

    private fun getPropertyKeys(stringsPropertiesFiles: Collection<Path>): Set<String> {
        return stringsPropertiesFiles
                .map { it.loadProperties() }
                .flatMap { it.stringPropertyNames() }
                .toSet()
    }

    private fun getOutputDirectory(packageName: String): Path {
        val generatedSourceDirectory = project.buildDir.toPath()
                .resolve(TextKeysGeneratorPlugin.GENERATED_SOURCE_DIRECTORY)
        return generatedSourceDirectory.resolve(packageNameToPath(packageName))
    }

    private fun packageNameToPath(packageName: String): String = packageName.replace('.', File.separatorChar)

}
