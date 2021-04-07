[![Java CI with Gradle](https://github.com/Double-O-Seven/property-key-generator/actions/workflows/gradle.yml/badge.svg)](https://github.com/Double-O-Seven/property-key-generator/actions/workflows/gradle.yml)
[![Gradle Plugins Release](https://img.shields.io/github/tag/Double-O-Seven/property-key-generator.svg)](https://plugins.gradle.org/plugin/ch.leadrian.property-key-generator)

# Property Key Generator Gradle Plugin

A simple Gradle plugin that generates constants from Java resources bundles using property keys defined in `.properties` files.

The plugin collects all property key from a given resource bundle.

Given a property key `how.are.you` in a resource bundle `translation`, the plugin will generate code that will allow you to the exact property keys without having to write them out like this:
```java
ResouceBundle translations = ResourceBundle.getBundle("translations", Locale.GERMANY);
String howAreYou =  translations.getString(Translation.how.are.you);
```

The plugin can be configured to generate only String constants as well as to generate wrappers for the String constants.

If only String constants are generated, the String constants may be accessed using `Translation.how.are.you`.
If wrappers are generated, the String values will still be generated with an underscore as suffix, `Translation.how.are.you_` for example, while the wrapper values will be accessible without an underscore.

Tasks and extension
-------------------

The plugin creates a task `generate<ResourceBundleName>PropertyKeys` for each configured resource bundle as well as a default task `generatePropertyKeys` that depends on the other tasks and can be used to to generate all property keys for all resource bundles.
It is not require to manually trigger that since every Kotlin and Java compilation task will depend on `generatePropertyKeys`.
Additionally, an extension `propertyKeyGenerator` is provided to configure resource bundles, for which the property key constants should be generated.

A typical configuration may look like the following (using Kotlin Gradle DSL):

```kotlin
plugins {
    id("ch.leadrian.property-key-generator") version "2.1.0"
    // Other plugins
}

propertyKeyGenerator {

    // Resource bundle named translations in package org.mycompany.i18n
    resourceBundle("translations") {
        bundlePackageName = "org.mycompany.i18n"

        // Generate the property keys using a wrapper class in addition to Strings, using a single String-parameter constructor
        wrapperClass {
            packageName = "org.mycompany.i18n.model"
            // Create instances using a single String-parameter constructor
            className = "TranslationKey"
        }
    }

}
```

Let's assume we have the following properties in `translations_en_US.properties` for example.
```properties
hello.world=Hello world!
how.are.you=How are you?
how.old.are.you=How old are you?
hi=Hi!
```

The property keys will be generated as follows:
```java
package org.mycompany.i18n;

import javax.annotation.Generated;
import org.mycompany.i18n.model.TranslationKey;

@Generated("ch.leadrian.gradle.plugin.propertykeygenerator.PropertyKeyGeneratorPlugin")
public final class TranslationsKeys {
    public static final String hi_ = "hi";

    public static final TranslationKey hi = new TranslationKey(hi_);

    private TranslationsKeys() {
    }

    public static final class hello {
        public static final String PATH_ = "hello";

        public static final TranslationKey PATH = new TranslationKey(PATH_);

        public static final String world_ = "hello.world";

        public static final TranslationKey world = new TranslationKey(world_);

        private hello() {
        }
    }

    public static final class how {
        public static final String PATH_ = "how";

        public static final TranslationKey PATH = new TranslationKey(PATH_);

        private hello() {
        }

        public static final class are {
            public static final String PATH_ = "how.are";

            public static final TranslationKey PATH = new TranslationKey(PATH_);

            public static final String you_ = "how.are.you";

            public static final TranslationKey you = new TranslationKey(you_);

            private are() {
            }
        }

        public static final class old {
            public static final String PATH_ = "how.old";

            public static final TranslationKey PATH = new TranslationKey(PATH_);

            private old() {
            }

            public static final class are {
                public static final String PATH_ = "how.old.are";

                public static final TranslationKey PATH = new TranslationKey(PATH_);

                public static final String you_ = "how.old.are.you";

                public static final TranslationKey you = new TranslationKey(you_);

                private are() {
                }
            }
        }
    }
}
```

A minimal configuration may look like the following:

```kotlin
plugins {
    id("ch.leadrian.property-key-generator") version "2.1.0"
    // Other plugins
}

propertyKeyGenerator {

    // Resource bundle named translations in package org.mycompany.i18n
    resourceBundle("translations") {
        bundlePackageName = "org.mycompany.i18n"

        // Don't generate wrapper classes, only generate plain String constants
    }

    // Specify another resourceBundle
    resourceBundle("more-translations") {
        packageName = "org.mycompany.i18n"
    }

}
```

A full configuration may look like the following:

```kotlin
plugins {
    id("ch.leadrian.property-key-generator") version "2.1.0"
    // Other plugins
}

propertyKeyGenerator {

    resourceBundle("translations") {
        // Has the same effect as "translations" above
        bundleName = "translations"
        bundlePackageName = "org.mycompany.i18n"
        // Root class containing the String constants, by default it is "<upper-case resouce bundleName>Keys"
        outputClassName = "MyTranslations"
        // Package of the output class. If not set, the bundlePackageName will be used.
        outputPackageName = "org.mycompany.i18n.generated"
        // Override the default .properties lookup and only look in strings.properties and translations.properties for example.
        // By default, all .properties files in specified package belonging to the specified resource bundle are match, for example:
        // translations.properties, transations_en_US.properties, translations_en.properties, translations_de_DE.properties
        pattern = "^(strings\\.properties)|(translations\\.properties)$"

        // Case format used for naming the resource bundles, by default LOWER_HYPHEN (kebab-case) is used.
        // Applicable values are LOWER_HYPHEN, LOWER_UNDERSCORE, LOWER_CAMEL, UPPER_CAMEL, UPPER_UNDERSCORE.
        // See com.google.common.base.CaseFormat from Guava for more information.
        bundleNameCaseFormat = "UPPER_CAMEL"

        // Generates a public static final constant for each 'segment' of a property key, for example:
        // For the property key "foo.bar.baz", a constant value containing "foo" and "foo.bar" will be generated.
        // Those constants will be accessible using "TranslationsKeys.foo.bar.PATH" for example.
        // No PATH constant is generated for "foo.bar.baz" since TranslationKeys.foo.bar.baz is a property key itself.
        // The PATH constant may be helpful when one wants to dynamically resolve translation keys without having to manually write out the base.
        // By default the name of the constant will be PATH. Use the following property to override the name.
        pathVariableName = "CUSTOM_PATH"

        // Defines the prefix of the string value of the property keys. The configuration is only effective if a wrapper class
        // has been declared. Given a property key "foo.bar.baz", a prefix defined a "string_" and a declared wrapper class,
        // the name of the string variable will be "string_baz".
        stringValuePrefix = ""

        // Defines the suffix of the string value of the property keys. The configuration is only effective if a wrapper class
        // has been declared. Given a property key "foo.bar.baz", a suffix defined a "_string" and a declared wrapper class,
        // the name of the string variable will be "baz_string".
        stringValueSuffix = "_"

        // This configuration is optional, but may be applied only once.
        // Generate the property keys using a wrapper class in addition to Strings, using a single String-parameter constructor
        wrapperClass {
            // Package name of the wrapper class
            packageName = "org.mycompany.i18n.model"
            // Name of the wrapper class
            className = "TranslationKey"
            // If a factory method is specified, it will be used to instantiate the wrapper objects.
            // The factory method is required to accept a single String parameter and return an instance of the wrapper class.
            // If no factory method is specified, the wrapper class must have a constructor that accepts a single String parameter.
            factoryMethod = "valueOf"
        }
    }

}
```
