package ch.leadrian.gradle.plugin.propertykeygenerator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.io.StringWriter
import java.util.stream.Stream

internal class PropertyKeyGeneratorTest {

    @ParameterizedTest
    @ArgumentsSource(GenerateTextKeyClassesArgumentsProvider::class)
    fun shouldGenerateTextKeyClasses(
            rootClassName: String,
            packageName: String,
            stringPropertyNames: Set<String>,
            expectedClassString: String
    ) {
        StringWriter().use { writer ->
            PropertyKeyGenerator.generateTextKeyClasses(
                    rootClassName = rootClassName,
                    packageName = packageName,
                    propertyKeys = stringPropertyNames,
                    writer = writer
            )

            assertThat(writer.toString())
                    .isEqualTo(expectedClassString)
        }
    }

    private class GenerateTextKeyClassesArgumentsProvider : ArgumentsProvider {

        override fun provideArguments(context: ExtensionContext?): Stream<GenerateTextKeyClassesArguments> = Stream.of(
                GenerateTextKeyClassesArguments(
                        rootClassName = "TextKeys",
                        packageName = "ch.leadrian.samp.kamp.core.api.text.test",
                        stringPropertyNames = setOf(),
                        expectedClassString = ("package ch.leadrian.samp.kamp.core.api.text.test;\n\n" +
                                "import javax.annotation.Generated;\n\n" +
                                "@Generated(\"ch.leadrian.gradle.plugin.propertykeygenerator.PropertyKeyGenerator\")\n" +
                                "public final class TextKeys {\n" +
                                "  private TextKeys() {\n" +
                                "  }\n" +
                                "}\n")
                ),
                GenerateTextKeyClassesArguments(
                        rootClassName = "TextKeys",
                        packageName = "ch.leadrian.samp.kamp.core.api.text.test",
                        stringPropertyNames = setOf("test"),
                        expectedClassString = ("package ch.leadrian.samp.kamp.core.api.text.test;\n\n" +
                                "import ch.leadrian.samp.kamp.core.api.text.TextKey;\n" +
                                "import javax.annotation.Generated;\n\n" +
                                "@Generated(\"ch.leadrian.gradle.plugin.propertykeygenerator.PropertyKeyGenerator\")\n" +
                                "public final class TextKeys {\n" +
                                "  public static final String test_ = \"test\";\n\n" +
                                "  public static final TextKey test = new TextKey(test_);\n\n" +
                                "  private TextKeys() {\n" +
                                "  }\n" +
                                "}\n")
                ),
                GenerateTextKeyClassesArguments(
                        rootClassName = "TextKeys",
                        packageName = "ch.leadrian.samp.kamp.core.api.text.test",
                        stringPropertyNames = setOf("test2", "test1"),
                        expectedClassString = ("package ch.leadrian.samp.kamp.core.api.text.test;\n\n" +
                                "import ch.leadrian.samp.kamp.core.api.text.TextKey;\n" +
                                "import javax.annotation.Generated;\n\n" +
                                "@Generated(\"ch.leadrian.gradle.plugin.propertykeygenerator.PropertyKeyGenerator\")\n" +
                                "public final class TextKeys {\n" +
                                "  public static final String test1_ = \"test1\";\n\n" +
                                "  public static final TextKey test1 = new TextKey(test1_);\n\n" +
                                "  public static final String test2_ = \"test2\";\n\n" +
                                "  public static final TextKey test2 = new TextKey(test2_);\n\n" +
                                "  private TextKeys() {\n" +
                                "  }\n" +
                                "}\n")
                ),
                GenerateTextKeyClassesArguments(
                        rootClassName = "TextKeys",
                        packageName = "ch.leadrian.samp.kamp.core.api.text.test",
                        stringPropertyNames = setOf(
                                "test2.abc",
                                "test2.xyz.t1",
                                "test2.xyz.t2",
                                "test1",
                                "test3.lol"
                        ),
                        expectedClassString = ("package ch.leadrian.samp.kamp.core.api.text.test;\n\n" +
                                "import ch.leadrian.samp.kamp.core.api.text.TextKey;\n" +
                                "import javax.annotation.Generated;\n\n" +
                                "@Generated(\"ch.leadrian.gradle.plugin.propertykeygenerator.PropertyKeyGenerator\")\n" +
                                "public final class TextKeys {\n" +
                                "  public static final String test1_ = \"test1\";\n\n" +
                                "  public static final TextKey test1 = new TextKey(test1_);\n\n" +
                                "  private TextKeys() {\n" +
                                "  }\n\n" +
                                "  public static final class test2 {\n" +
                                "    public static final String abc_ = \"test2.abc\";\n\n" +
                                "    public static final TextKey abc = new TextKey(abc_);\n\n" +
                                "    private test2() {\n" +
                                "    }\n\n" +
                                "    public static final class xyz {\n" +
                                "      public static final String t1_ = \"test2.xyz.t1\";\n\n" +
                                "      public static final TextKey t1 = new TextKey(t1_);\n\n" +
                                "      public static final String t2_ = \"test2.xyz.t2\";\n\n" +
                                "      public static final TextKey t2 = new TextKey(t2_);\n\n" +
                                "      private xyz() {\n" +
                                "      }\n" +
                                "    }\n" +
                                "  }\n\n" +
                                "  public static final class test3 {\n" +
                                "    public static final String lol_ = \"test3.lol\";\n\n" +
                                "    public static final TextKey lol = new TextKey(lol_);\n\n" +
                                "    private test3() {\n" +
                                "    }\n" +
                                "  }\n" +
                                "}\n")
                )
        )

    }

    private class GenerateTextKeyClassesArguments(
            private val rootClassName: String,
            private val packageName: String,
            private val stringPropertyNames: Set<String>,
            private val expectedClassString: String
    ) : Arguments {

        override fun get(): Array<Any> = arrayOf(rootClassName, packageName, stringPropertyNames, expectedClassString)

    }
}