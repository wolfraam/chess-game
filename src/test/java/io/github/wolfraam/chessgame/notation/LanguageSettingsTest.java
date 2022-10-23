package io.github.wolfraam.chessgame.notation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LanguageSettingsTest {
    @Test
    public void testException() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                LanguageSettings.addLanguage("en", "a", "b", "c", "d", "e"));
    }
}