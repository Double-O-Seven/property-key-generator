package ch.leadrian.gradle.plugin.propertykeygenerator

import com.google.common.io.ByteStreams
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.nio.file.Files
import java.nio.file.Path

internal class PropertyKeyGeneratorPluginTest {

    private lateinit var projectDir: Path

    @BeforeEach
    fun setUp(@TempDir tempDir: Path) {
        projectDir = tempDir
    }

    @ParameterizedTest
    @CsvSource(
            "'build.gradle.kts', 'settings.gradle.kts'",
            "'build.gradle', 'settings.gradle'"
    )
    fun `should successfully build project`(buildFile: String, settingsFile: String) {
        copyResource(
                resourceName = "TranslationKey.java",
                file = projectDir.resolve("src/main/java/ch/leadrian/gradle/plugin/propertykeygenerator/TranslationKey.java")
        )
        copyResource(
                resourceName = "TranslationKeyTest.java",
                file = projectDir.resolve("src/test/java/ch/leadrian/gradle/plugin/propertykeygenerator/TranslationKeyTest.java")
        )
        copyResource(
                resourceName = buildFile,
                file = projectDir.resolve(buildFile)
        )
        copyResource(
                resourceName = settingsFile,
                file = projectDir.resolve(settingsFile)
        )
        copyResource(
                resourceName = "wrapper-class-test.properties",
                file = projectDir.resolve("src/main/resources/ch/leadrian/gradle/plugin/propertykeygenerator/wrapper-class-test.properties")
        )
        copyResource(
                resourceName = "factory-method-test.properties",
                file = projectDir.resolve("src/main/resources/ch/leadrian/gradle/plugin/propertykeygenerator/factory-method-test.properties")
        )
        copyResource(
                resourceName = "string-constants-test.properties",
                file = projectDir.resolve("src/main/resources/ch/leadrian/gradle/plugin/propertykeygenerator/string-constants-test.properties")
        )

        val buildResult = GradleRunner.create()
                .withProjectDir(projectDir.toFile())
                .withArguments("build", "--stacktrace")
                .withPluginClasspath()
                .build()

        assertAll(
                { assertThat(buildResult.task(":generateWrapperClassTestPropertyKeys")?.outcome).isEqualTo(TaskOutcome.SUCCESS) },
                { assertThat(buildResult.task(":generateFactoryMethodTestPropertyKeys")?.outcome).isEqualTo(TaskOutcome.SUCCESS) },
                { assertThat(buildResult.task(":generateStringConstantsTestPropertyKeys")?.outcome).isEqualTo(TaskOutcome.SUCCESS) }
        )
    }

    private fun copyResource(resourceName: String, file: Path) {
        Files.createDirectories(file.parent)
        javaClass.getResourceAsStream(resourceName).use { inputStream ->
            Files.newOutputStream(file).use { outputStream ->
                ByteStreams.copy(inputStream, outputStream)
            }
        }
    }

}