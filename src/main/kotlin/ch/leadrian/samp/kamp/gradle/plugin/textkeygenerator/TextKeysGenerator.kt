package ch.leadrian.samp.kamp.gradle.plugin.textkeygenerator

import com.squareup.javapoet.*
import java.io.Writer
import javax.annotation.Generated
import javax.lang.model.element.Modifier

class TextKeysGenerator {

    companion object {

        private val textKeyTypeSpec = ClassName.get("ch.leadrian.samp.kamp.core.api.text", "TextKey")
    }

    @Generated
    fun generateTextKeyClasses(rootClassName: String, packageName: String, stringPropertyNames: Set<String>, writer: Writer) {
        val rootTypeSpecBuilder = TypeSpec
                .classBuilder(rootClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(AnnotationSpec
                        .builder(Generated::class.java)
                        .addMember("value", "\$S", this::class.java.name)
                        .build())
                .addMethod(MethodSpec
                        .constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .build())
        getTextKeyTrees(stringPropertyNames.map { TextKey(it) }).forEach { it.write(rootTypeSpecBuilder) }
        val javaFile = JavaFile
                .builder(packageName, rootTypeSpecBuilder.build())
                .skipJavaLangImports(true)
                .build()
        javaFile.writeTo(writer)
    }

    private fun getTextKeyTrees(textKeys: List<TextKey>): List<TextKeyTree> {
        val textKeysByFirstSegment = textKeys.groupBy(
                keySelector = { it.propertyNameSegments.first() },
                valueTransform = { it.copy(propertyNameSegments = it.propertyNameSegments.drop(1)) }
        )
        val subtreesBySegment = mutableListOf<TextKeyTree>()
        textKeysByFirstSegment.toSortedMap().forEach { segment, groupedTextKeys ->
            if (groupedTextKeys.size == 1) {
                groupedTextKeys.first().apply {
                    subtreesBySegment += when {
                        propertyNameSegments.isEmpty() -> listOf(TextKeyTree.Leaf(segment, propertyName))
                        else -> listOf(TextKeyTree.Node(segment, getTextKeyTrees(listOf(this))))
                    }
                }
            } else {
                val leaf = groupedTextKeys.find { it.propertyNameSegments.isEmpty() }
                if (leaf != null) throw IllegalStateException("Property ${leaf.propertyName} cannot be a prefix of other properties")
                subtreesBySegment += listOf(TextKeyTree.Node(segment, getTextKeyTrees(groupedTextKeys)))
            }
        }
        return subtreesBySegment
    }

    private sealed class TextKeyTree(val segment: String) {

        abstract fun write(typeSpecBuilder: TypeSpec.Builder)

        class Node(segment: String, val subtrees: List<TextKeyTree>) : TextKeyTree(segment) {

            override fun write(typeSpecBuilder: TypeSpec.Builder) {
                val nestedTypeSpecBuilder = TypeSpec
                        .classBuilder(segment)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .addMethod(MethodSpec
                                .constructorBuilder()
                                .addModifiers(Modifier.PRIVATE)
                                .build())
                subtrees.forEach { it.write(nestedTypeSpecBuilder) }
                typeSpecBuilder.addType(nestedTypeSpecBuilder.build())
            }
        }

        class Leaf(segment: String, val propertyName: String) : TextKeyTree(segment) {

            override fun write(typeSpecBuilder: TypeSpec.Builder) {
                val stringFieldSpec = FieldSpec
                        .builder(String::class.java, "${segment}_", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .initializer("\$S", propertyName)
                        .build()
                typeSpecBuilder.addField(stringFieldSpec)
                typeSpecBuilder.addField(FieldSpec
                        .builder(textKeyTypeSpec, segment, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .initializer("new \$T(\$N)", textKeyTypeSpec, stringFieldSpec)
                        .build())
            }
        }
    }

    private data class TextKey(val propertyName: String, val propertyNameSegments: List<String>) {
        constructor(propertyName: String) : this(propertyName, propertyName.split(".").toList())
    }

}
