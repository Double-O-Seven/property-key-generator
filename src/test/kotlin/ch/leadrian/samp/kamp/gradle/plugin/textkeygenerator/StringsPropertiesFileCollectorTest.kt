package ch.leadrian.samp.kamp.gradle.plugin.textkeygenerator

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.entry
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import java.util.Locale

internal class StringsPropertiesFileCollectorTest {

    @Test
    fun shouldGetStringsPropertiesFilesByPackageName(@TempDir resourcesDirectory: Path) {
        resourcesDirectory.createStringsPropertiesFile("ch.foo.bar", Locale.GERMANY)
        resourcesDirectory.createStringsPropertiesFile("ch.foo.bar")
        resourcesDirectory.createStringsPropertiesFile("ch.foo.lol")
        resourcesDirectory.createStringsPropertiesFile("ch.foo.rofl", Locale.ENGLISH)

        val stringsPropertiesFilesByPackageName = StringsPropertiesFileCollector.getStringsPropertyFilesByPackageName(
                listOf(resourcesDirectory)
        )

        assertThat(stringsPropertiesFilesByPackageName)
                .containsOnly(
                        entry(
                                "ch.foo.bar", setOf(
                                resourcesDirectory.resolve("ch/foo/bar/strings_de_DE.properties"),
                                resourcesDirectory.resolve("ch/foo/bar/strings.properties")
                        )
                        ),
                        entry("ch.foo.lol", setOf(resourcesDirectory.resolve("ch/foo/lol/strings.properties"))),
                        entry("ch.foo.rofl", setOf(resourcesDirectory.resolve("ch/foo/rofl/strings_en.properties")))
                )
    }

    private fun Path.createStringsPropertiesFile(packageName: String, locale: Locale? = null) {
        val packageSegments = packageName.split('.')
        val outputDirectory = packageSegments.fold(this, Path::resolve)
        Files.createDirectories(outputDirectory)
        val stringPropertiesFile = locale?.let { "strings_$it.properties" } ?: "strings.properties"
        Files.createFile(outputDirectory.resolve(stringPropertiesFile))
    }

}