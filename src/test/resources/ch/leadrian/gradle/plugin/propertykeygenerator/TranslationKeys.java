package ch.leadrian.gradle.plugin.propertykeygenerator;

public final class TranslationKeys {

    private TranslationKeys() {
    }

    public static TranslationKey from(String value) {
        return TranslationKey.valueOf(value);
    }
}