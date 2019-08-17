package ch.leadrian.gradle.plugin.propertykeygenerator;

public final class TranslationKey {

    public static TranslationKey valueOf(String value) {
        return new TranslationKey(value);
    }

    public final String value;

    public TranslationKey(String value) {
        this.value = value;
    }
}