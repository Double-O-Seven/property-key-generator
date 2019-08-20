package ch.leadrian.gradle.plugin.propertykeygenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import ch.leadrian.gradle.plugin.propertykeygenerator.test.NonDefaultConfigKeys;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TranslationKeyTest {

    @Nested
    class WrapperClassTests {

        @Test
        void shouldAccessSingleSegmentPropertyKey() {
            assertAll(
                    () -> assertThat(WrapperClassTestKeys.foo.value).isEqualTo("foo"),
                    () -> assertThat(WrapperClassTestKeys.foo_).isEqualTo("foo")
            );
        }

        @Test
        void shouldAccessSecondLevelPropertyKeys() {
            assertAll(
                    () -> assertThat(WrapperClassTestKeys.bar.baz.value).isEqualTo("bar.baz"),
                    () -> assertThat(WrapperClassTestKeys.bar.baz_).isEqualTo("bar.baz")
            );
        }

        @Test
        void shouldAccessThirdLevelSiblingPropertyKeys() {
            assertAll(
                    () -> assertThat(WrapperClassTestKeys.fubar.qux.bla.value).isEqualTo("fubar.qux.bla"),
                    () -> assertThat(WrapperClassTestKeys.fubar.qux.bla_).isEqualTo("fubar.qux.bla"),
                    () -> assertThat(WrapperClassTestKeys.fubar.qux.blub.value).isEqualTo("fubar.qux.blub"),
                    () -> assertThat(WrapperClassTestKeys.fubar.qux.blub_).isEqualTo("fubar.qux.blub")
            );
        }

        @Test
        void shouldAccessPaths() {
            assertAll(
                    () -> assertThat(WrapperClassTestKeys.fubar.PATH.value).isEqualTo("fubar"),
                    () -> assertThat(WrapperClassTestKeys.fubar.PATH_).isEqualTo("fubar"),
                    () -> assertThat(WrapperClassTestKeys.fubar.qux.PATH.value).isEqualTo("fubar.qux"),
                    () -> assertThat(WrapperClassTestKeys.fubar.qux.PATH_).isEqualTo("fubar.qux"),
                    () -> assertThat(WrapperClassTestKeys.bar.PATH.value).isEqualTo("bar"),
                    () -> assertThat(WrapperClassTestKeys.bar.PATH_).isEqualTo("bar")
            );
        }
    }

    @Nested
    class FactoryMethodTests {

        @Test
        void shouldAccessSingleSegmentPropertyKey() {
            assertAll(
                    () -> assertThat(FactoryMethodTestKeys.foo.value).isEqualTo("foo"),
                    () -> assertThat(FactoryMethodTestKeys.foo_).isEqualTo("foo")
            );
        }

        @Test
        void shouldAccessSecondLevelPropertyKeys() {
            assertAll(
                    () -> assertThat(FactoryMethodTestKeys.bar.baz.value).isEqualTo("bar.baz"),
                    () -> assertThat(FactoryMethodTestKeys.bar.baz_).isEqualTo("bar.baz")
            );
        }

        @Test
        void shouldAccessThirdLevelSiblingPropertyKeys() {
            assertAll(
                    () -> assertThat(FactoryMethodTestKeys.fubar.qux.bla.value).isEqualTo("fubar.qux.bla"),
                    () -> assertThat(FactoryMethodTestKeys.fubar.qux.bla_).isEqualTo("fubar.qux.bla"),
                    () -> assertThat(FactoryMethodTestKeys.fubar.qux.blub.value).isEqualTo("fubar.qux.blub"),
                    () -> assertThat(FactoryMethodTestKeys.fubar.qux.blub_).isEqualTo("fubar.qux.blub")
            );
        }

        @Test
        void shouldAccessPaths() {
            assertAll(
                    () -> assertThat(FactoryMethodTestKeys.fubar.PATH.value).isEqualTo("fubar"),
                    () -> assertThat(FactoryMethodTestKeys.fubar.PATH_).isEqualTo("fubar"),
                    () -> assertThat(FactoryMethodTestKeys.fubar.qux.PATH.value).isEqualTo("fubar.qux"),
                    () -> assertThat(FactoryMethodTestKeys.fubar.qux.PATH_).isEqualTo("fubar.qux"),
                    () -> assertThat(FactoryMethodTestKeys.bar.PATH.value).isEqualTo("bar"),
                    () -> assertThat(FactoryMethodTestKeys.bar.PATH_).isEqualTo("bar")
            );
        }
    }

    @Nested
    class StringConstantTests {

        @Test
        void shouldAccessSingleSegmentPropertyKey() {
            assertThat(StringConstantsTestKeys.foo).isEqualTo("foo");
        }

        @Test
        void shouldAccessSecondLevelPropertyKeys() {
            assertThat(StringConstantsTestKeys.bar.baz).isEqualTo("bar.baz");
        }

        @Test
        void shouldAccessThirdLevelSiblingPropertyKeys() {
            assertAll(
                    () -> assertThat(StringConstantsTestKeys.fubar.qux.bla).isEqualTo("fubar.qux.bla"),
                    () -> assertThat(StringConstantsTestKeys.fubar.qux.blub).isEqualTo("fubar.qux.blub")
            );
        }

        @Test
        void shouldAccessPaths() {
            assertAll(
                    () -> assertThat(StringConstantsTestKeys.fubar.PATH).isEqualTo("fubar"),
                    () -> assertThat(StringConstantsTestKeys.fubar.qux.PATH).isEqualTo("fubar.qux"),
                    () -> assertThat(StringConstantsTestKeys.bar.PATH).isEqualTo("bar")
            );
        }
    }

    @Nested
    class NonDefaultConfigurationTests {

        @Test
        void shouldAccessSingleSegmentPropertyKey() {
            assertThat(NonDefaultConfigKeys.foo).isEqualTo("foo");
        }

        @Test
        void shouldAccessSecondLevelPropertyKeys() {
            assertThat(NonDefaultConfigKeys.bar.baz).isEqualTo("bar.baz");
        }

        @Test
        void shouldAccessThirdLevelSiblingPropertyKeys() {
            assertAll(
                    () -> assertThat(NonDefaultConfigKeys.fubar.qux.bla).isEqualTo("fubar.qux.bla"),
                    () -> assertThat(NonDefaultConfigKeys.fubar.qux.blub).isEqualTo("fubar.qux.blub")
            );
        }

        @Test
        void shouldAccessPaths() {
            assertAll(
                    () -> assertThat(NonDefaultConfigKeys.fubar.CUSTOM_PATH).isEqualTo("fubar"),
                    () -> assertThat(NonDefaultConfigKeys.fubar.qux.CUSTOM_PATH).isEqualTo("fubar.qux"),
                    () -> assertThat(NonDefaultConfigKeys.bar.CUSTOM_PATH).isEqualTo("bar")
            );
        }
    }

}
