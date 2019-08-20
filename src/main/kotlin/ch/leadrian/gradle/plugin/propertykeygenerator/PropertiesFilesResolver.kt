package ch.leadrian.gradle.plugin.propertykeygenerator

import java.io.File
import java.util.regex.Pattern

internal object PropertiesFilesResolver {

    fun resolve(directories: Set<File>, spec: PropertyKeyGenerationSpec): List<File> {
        val pattern = getPattern(spec)
        return directories.map { it.resolve(spec.bundlePackageName.replace('.', File.separatorChar)) }
                .filter { it.exists() && it.isDirectory }
                .flatMap { it.listFiles()?.toList().orEmpty() }
                .filter { it.isFile && pattern.matcher(it.name).matches() }
    }

    private fun getPattern(spec: PropertyKeyGenerationSpec): Pattern =
            Pattern.compile(spec.pattern ?: "${spec.bundleName}(_[a-z]{2}(_[A-Z]{2})?)?\\.properties")

}