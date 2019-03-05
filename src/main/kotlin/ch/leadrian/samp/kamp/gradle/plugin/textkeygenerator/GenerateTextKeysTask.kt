package ch.leadrian.samp.kamp.gradle.plugin.textkeygenerator

import org.gradle.api.DefaultTask
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectories
import org.gradle.api.tasks.SourceSet
import java.io.File
import java.io.IOException
import java.io.UncheckedIOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.*
import java.util.regex.Pattern.compile
import kotlin.streams.toList

open class GenerateTextKeysTask : DefaultTask() {

    companion object {

        private val STRINGS_FILE_PATTERN = compile("strings(_[a-z]{2}(_[A-Z]{2})?)?\\.properties")
    }

    init {
        doLast { generateTextKeys() }
    }

    private val extension: TextKeysGeneratorPluginExtension
        get() = project.extensions.getByType(TextKeysGeneratorPluginExtension::class.java)

    private val resourcesDirectories: Set<File> by lazy {
        project
                .convention
                .findPlugin(JavaPluginConvention::class.java)
                ?.sourceSets
                ?.getByName(SourceSet.MAIN_SOURCE_SET_NAME)
                ?.resources
                ?.sourceDirectories
                ?.files
                .orEmpty()
    }

    @InputFiles
    fun getInputFiles(): List<File> {
        return extension.packageNames.flatMap { packageName ->
            getStringsPropertiesFiles(resourcesDirectories, packageName)
        }
    }

    @OutputDirectories
    fun getOutputDirectories(): List<File> = extension.packageNames.map { this.getOutputDirectory(it) }

    private fun generateTextKeys() {
        val extension = extension
        extension.packageNames.forEach { packageName ->
            val stringsPropertiesFiles = getStringsPropertiesFiles(resourcesDirectories, packageName)
            try {
                generateTextKeys(packageName, stringsPropertiesFiles)
            } catch (e: IOException) {
                throw UncheckedIOException(e)
            }
        }
    }

    @Throws(IOException::class)
    private fun generateTextKeys(packageName: String, stringsPropertiesFiles: List<File>) {
        val stringPropertyNames = stringsPropertiesFiles
                .map { it.toPath() }
                .map { this.loadProperties(it) }
                .flatMap { it.stringPropertyNames() }
                .toSet()
        val textKeysGenerator = TextKeysGenerator()
        val outputDirectory = getOutputDirectory(packageName).toPath()

        Files.createDirectories(outputDirectory)
        val outputFile = outputDirectory.resolve("${extension.className}.java")
        Files.newBufferedWriter(outputFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE).use { writer ->
            textKeysGenerator.generateTextKeyClasses(
                    rootClassName = extension.className,
                    packageName = packageName,
                    stringPropertyNames = stringPropertyNames,
                    writer = writer
            )
        }
    }

    private fun getStringsPropertiesFiles(resourceDirectores: Set<File>, packageName: String): List<File> {
        val packagePath = packageNameToPath(packageName)
        return resourceDirectores
                .flatMap { resourceDirectory ->
                    Files
                            .list(resourceDirectory.toPath().resolve(packagePath))
                            .filter { Files.isRegularFile(it) }
                            .filter { path -> STRINGS_FILE_PATTERN.matcher(path.fileName.toString()).matches() }
                            .map { it.toFile() }
                            .toList()
                }
    }

    private fun loadProperties(path: Path): Properties {
        val properties = Properties()
        try {
            Files.newBufferedReader(path, extension.charset).use { reader -> properties.load(reader) }
        } catch (e: IOException) {
            throw UncheckedIOException(e)
        }
        return properties
    }

    private fun getOutputDirectory(packageName: String): File {
        val outputDirectory = project.buildDir.resolve(TextKeysGeneratorPlugin.GENERATED_SOURCE_DIRECTORY)
        return File(outputDirectory, packageNameToPath(packageName))
    }

    private fun packageNameToPath(packageName: String): String = packageName.replace('.', File.separatorChar)

}
