package ch.leadrian.gradle.plugin.propertykeygenerator.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PropertyKeyTreeTest {

    @Test
    fun `should insert single property`() {
        val propertyKey = PropertyKey("foo.bar.baz")
        val tree = PropertyKeyTree.InternalNode()

        tree.put(propertyKey)

        assertThat(tree)
                .isEqualTo(
                        PropertyKeyTree.InternalNode(
                                path = null,
                                children = mutableMapOf(
                                        "foo" to PropertyKeyTree.InternalNode(
                                                path = "foo",
                                                children = mutableMapOf(
                                                        "bar" to PropertyKeyTree.InternalNode(
                                                                path = "foo.bar",
                                                                children = mutableMapOf("baz" to PropertyKeyTree.LeafNode(propertyKey))
                                                        )
                                                )
                                        )
                                )
                        )
                )
    }

    @Test
    fun `should insert multiple properties`() {
        val propertyKey1 = PropertyKey("foo.bar.baz")
        val propertyKey2 = PropertyKey("foo.qux")
        val propertyKey3 = PropertyKey("fubar")
        val tree = PropertyKeyTree.InternalNode()

        tree.putAll(listOf(propertyKey1, propertyKey2, propertyKey3))

        assertThat(tree)
                .isEqualTo(
                        PropertyKeyTree.InternalNode(
                                path = null,
                                children = mutableMapOf(
                                        "foo" to PropertyKeyTree.InternalNode(
                                                path = "foo",
                                                children = mutableMapOf(
                                                        "bar" to PropertyKeyTree.InternalNode(
                                                                path = "foo.bar",
                                                                children = mutableMapOf("baz" to PropertyKeyTree.LeafNode(propertyKey1))
                                                        ),
                                                        "qux" to PropertyKeyTree.LeafNode(propertyKey2)
                                                )
                                        ),
                                        "fubar" to PropertyKeyTree.LeafNode(propertyKey3)
                                )
                        )
                )
    }

}