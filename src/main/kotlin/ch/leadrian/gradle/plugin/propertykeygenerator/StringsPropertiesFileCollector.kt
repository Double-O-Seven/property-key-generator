package ch.leadrian.gradle.plugin.propertykeygenerator

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors.groupingBy
import java.util.stream.Collectors.toSet

internal object StringsPropertiesFileCollector {

    fun getStringsPropertyFilesByPackageName(resourcesDirectories: Collection<Path>): Map<String, Set<Path>> {
        val result: MutableMap<String, MutableSet<Path>> = mutableMapOf()
        resourcesDirectories.map { it }.forEach { resourcesDirectory ->
            getStringsPropertyFilesByPackageName(resourcesDirectory).forEach { key, value ->
                result.merge(key, value) { v1, v2 -> v1.apply { addAll(v2) } }
            }
        }
        return result
    }

    private fun getStringsPropertyFilesByPackageName(resourcesDirectory: Path): Map<String, MutableSet<Path>> {
        return Files
                .walk(resourcesDirectory)
                .filter { Files.isRegularFile(it) }
                .filter { PropertyKeyGeneratorPlugin.STRINGS_FILE_PATTERN.matcher(it.fileName.toString()).matches() }
                .collect(
                        groupingBy(
                                { file: Path -> resourcesDirectory.relativize(file.parent).joinToString(".") },
                                toSet<Path>()
                        )
                )
    }

}