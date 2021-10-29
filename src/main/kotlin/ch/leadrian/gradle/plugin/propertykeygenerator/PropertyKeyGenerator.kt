package ch.leadrian.gradle.plugin.propertykeygenerator

import ch.leadrian.gradle.plugin.propertykeygenerator.model.PropertyKeyTree
import com.squareup.javapoet.*
import java.io.Writer
import java.util.*
import javax.lang.model.element.Modifier

internal class PropertyKeyGenerator(
        private val spec: PropertyKeyGenerationSpec,
        private val propertyKeyTree: PropertyKeyTree
) {

    fun generatePropertyKeyRootClass(writer: Writer) {
        val rootClassName = ClassName.get(spec.resolvedOutputPackageName, spec.resolvedOutputClassName)
        val rootClass = TypeSpec
                .classBuilder(rootClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addPrivateConstructor()
                .apply {
                    val methodName = spec.resourceBundleMethodName
                    if (methodName != null) {
                        addGetResourceBundleFunction(
                                bundleName = "${spec.bundlePackageName}.${spec.bundleName}",
                                methodName = methodName,
                                rootClassName = rootClassName
                        )
                    }
                }
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

    private fun TypeSpec.Builder.addGetResourceBundleFunction(bundleName: String, methodName: String, rootClassName: ClassName): TypeSpec.Builder {
        val localeParameter = ParameterSpec.builder(Locale::class.java, "locale").build()
        val methodSpec = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(localeParameter)
                .addCode("return \$T.getBundle(\$S, locale, \$T.class.getClassLoader());", ResourceBundle::class.java, bundleName, rootClassName)
                .returns(ResourceBundle::class.java)
                .build()
        return addMethod(methodSpec)
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
            else -> "${spec.stringValuePrefix}$segment${spec.stringValueSuffix}"
        }
        val stringField = FieldSpec
                .builder(String::class.java, stringFieldName, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("\$S", propertyKeyValue)
                .build()
        addField(stringField)
        if (wrapperClass != null) {
            val wrapperClassName = ClassName.get(wrapperClass.packageName, wrapperClass.className)
            val factoryMethodName = wrapperClass.factoryMethod
            val factoryClassName = if (wrapperClass.factoryClassName != null && wrapperClass.factoryPackageName != null) {
                ClassName.get(wrapperClass.factoryPackageName, wrapperClass.factoryClassName)
            } else {
                null
            }
            val creatingClassName = when {
                factoryMethodName != null && factoryClassName != null -> factoryClassName
                else -> wrapperClassName
            }
            val initializerFormat = when {
                factoryMethodName != null -> "\$T.$factoryMethodName(\$N)"
                else -> "new \$T(\$N)"
            }
            val wrapperField = FieldSpec
                    .builder(wrapperClassName, segment, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer(initializerFormat, creatingClassName, stringField)
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
