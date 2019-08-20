package ch.leadrian.gradle.plugin.propertykeygenerator

import ch.leadrian.gradle.plugin.propertykeygenerator.model.PropertyKeyTree
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import java.io.Writer
import javax.annotation.Generated
import javax.lang.model.element.Modifier

internal class PropertyKeyGenerator(
        private val spec: PropertyKeyGenerationSpec,
        private val propertyKeyTree: PropertyKeyTree
) {

    fun generatePropertyKeyRootClass(writer: Writer) {
        val rootClass = TypeSpec
                .classBuilder(ClassName.get(spec.resolvedOutputPackageName, spec.resolvedOutputClassName))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addPrivateConstructor()
                .addGeneratedAnnotation()
                .addDeclarations(propertyKeyTree)
                .build()
        JavaFile
                .builder(spec.resolvedOutputPackageName, rootClass)
                .skipJavaLangImports(true)
                .build()
                .writeTo(writer)
    }

    private fun TypeSpec.Builder.addPrivateConstructor(): TypeSpec.Builder {
        return addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build())
    }

    private fun TypeSpec.Builder.addGeneratedAnnotation(): TypeSpec.Builder {
        val annotation = AnnotationSpec
                .builder(Generated::class.java)
                .addMember("value", "\$S", PropertyKeyGeneratorPlugin::class.java.name)
                .build()
        return addAnnotation(annotation)
    }

    private fun TypeSpec.Builder.addDeclarations(node: PropertyKeyTree): TypeSpec.Builder {
        if (node is PropertyKeyTree.InternalNode && node.path != null) {
            addKeyDeclaration(segment = spec.pathVariableName, propertyKeyValue = node.path)
        }
        node.children.forEach { (segment, childNode) ->
            when (childNode) {
                is PropertyKeyTree.LeafNode -> addKeyDeclaration(segment = segment, propertyKeyValue = childNode.propertyKey.value)
                is PropertyKeyTree.InternalNode -> addChildClassDeclaration(segment, childNode)
            }
        }
        return this
    }

    private fun TypeSpec.Builder.addKeyDeclaration(segment: String, propertyKeyValue: String): TypeSpec.Builder {
        val wrapperClass = spec.wrapperClass
        val stringFieldName = when (wrapperClass) {
            null -> segment
            else -> "${segment}_"
        }
        val stringField = FieldSpec
                .builder(String::class.java, stringFieldName, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("\$S", propertyKeyValue)
                .build()
        addField(stringField)
        if (wrapperClass != null) {
            val wrapperClassName = ClassName.get(wrapperClass.packageName, wrapperClass.className)
            val factoryMethodName = wrapperClass.factoryMethod
            val initializerFormat = when {
                factoryMethodName != null -> "\$T.$factoryMethodName(\$N)"
                else -> "new \$T(\$N)"
            }
            val wrapperField = FieldSpec
                    .builder(wrapperClassName, segment, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer(initializerFormat, wrapperClassName, stringField)
                    .build()
            addField(wrapperField)
        }
        return this
    }

    private fun TypeSpec.Builder.addChildClassDeclaration(segment: String, node: PropertyKeyTree): TypeSpec.Builder {
        val typeBuilder = TypeSpec
                .classBuilder(ClassName.get(spec.resolvedOutputPackageName, segment))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .addPrivateConstructor()
                .addDeclarations(node)
        return addType(typeBuilder.build())
    }

}
