package ch.leadrian.gradle.plugin.propertykeygenerator;

import ch.leadrian.gradle.plugin.propertykeygenerator.test.NonDefaultConfigKeys;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

class TranslationKeyTest {

    @Nested
    class WrapperClassTests {

        @Test
        void shouldGenerateResourceBundle() {
            Throwable caughtThrowable = catchThrowable(() -> WrapperClassTestKeys.getResourceBundle(Locale.GERMANY));

            assertThat(caughtThrowable).isNull();
        }

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
        void shouldGenerateResourceBundle() {
            Throwable caughtThrowable = catchThrowable(() -> FactoryMethodTestKeys.getResourceBundle(Locale.GERMANY));

            assertThat(caughtThrowable).isNull();
        }

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
    class FactoryMethodTests2 {

        @Test
        void shouldGenerateResourceBundle() {
            Throwable caughtThrowable = catchThrowable(() -> FactoryMethodTest2Keys.getResourceBundle(Locale.GERMANY));

            assertThat(caughtThrowable).isNull();
        }

        @Test
        void shouldAccessSingleSegmentPropertyKey() {
            assertAll(
                    () -> assertThat(FactoryMethodTest2Keys.foo.value).isEqualTo("foo"),
                    () -> assertThat(FactoryMethodTest2Keys.foo_).isEqualTo("foo")
            );
        }

        @Test
        void shouldAccessSecondLevelPropertyKeys() {
            assertAll(
                    () -> assertThat(FactoryMethodTest2Keys.bar.baz.value).isEqualTo("bar.baz"),
                    () -> assertThat(FactoryMethodTest2Keys.bar.baz_).isEqualTo("bar.baz")
            );
        }

        @Test
        void shouldAccessThirdLevelSiblingPropertyKeys() {
            assertAll(
                    () -> assertThat(FactoryMethodTest2Keys.fubar.qux.bla.value).isEqualTo("fubar.qux.bla"),
                    () -> assertThat(FactoryMethodTest2Keys.fubar.qux.bla_).isEqualTo("fubar.qux.bla"),
                    () -> assertThat(FactoryMethodTest2Keys.fubar.qux.blub.value).isEqualTo("fubar.qux.blub"),
                    () -> assertThat(FactoryMethodTest2Keys.fubar.qux.blub_).isEqualTo("fubar.qux.blub")
            );
        }

        @Test
        void shouldAccessPaths() {
            assertAll(
                    () -> assertThat(FactoryMethodTest2Keys.fubar.PATH.value).isEqualTo("fubar"),
                    () -> assertThat(FactoryMethodTest2Keys.fubar.PATH_).isEqualTo("fubar"),
                    () -> assertThat(FactoryMethodTest2Keys.fubar.qux.PATH.value).isEqualTo("fubar.qux"),
                    () -> assertThat(FactoryMethodTest2Keys.fubar.qux.PATH_).isEqualTo("fubar.qux"),
                    () -> assertThat(FactoryMethodTest2Keys.bar.PATH.value).isEqualTo("bar"),
                    () -> assertThat(FactoryMethodTest2Keys.bar.PATH_).isEqualTo("bar")
            );
        }
    }

    @Nested
    class StringConstantTests {

        @Test
        void shouldGenerateResourceBundle() {
            Throwable caughtThrowable = catchThrowable(() -> StringConstantsTestKeys.getResourceBundle(Locale.GERMANY));

            assertThat(caughtThrowable).isNull();
        }

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
        void shouldGenerateResourceBundle() {
            Throwable caughtThrowable = catchThrowable(() -> NonDefaultConfigKeys.getResourceBundle(Locale.GERMANY));

            assertThat(caughtThrowable).isNull();
        }

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
