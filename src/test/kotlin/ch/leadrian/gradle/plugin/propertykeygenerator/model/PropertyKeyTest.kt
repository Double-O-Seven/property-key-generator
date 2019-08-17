package ch.leadrian.gradle.plugin.propertykeygenerator.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PropertyKeyTest {

    @Test
    fun `should compute segments`() {
        val propertyKey = PropertyKey("foo.bar.baz")

        val segments = propertyKey.segments

        assertThat(segments)
                .containsExactly("foo", "bar", "baz")
    }

}