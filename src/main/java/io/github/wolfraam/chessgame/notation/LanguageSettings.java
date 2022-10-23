package io.github.wolfraam.chessgame.notation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The language settings used by chess notation.
 */
public class LanguageSettings {
    private static final Map<String, NotationMapping> LANGUAGE_CODE_2_NOTATION_MAPPING = Collections.synchronizedMap(new HashMap<>());

    static {
        addLanguage("en", "K", "Q", "R", "B", "N");
        addLanguage("de", "K", "D", "T", "L", "S");
        addLanguage("fr", "R", "D", "T", "F", "C");
        addLanguage("ru", "Кр", "Ф", "Л", "С", "К");
        addLanguage("es", "R", "D", "T", "A", "C");
        addLanguage("it", "R", "D", "T", "A", "C");
        addLanguage("id", "R", "M", "B", "G", "K");
        addLanguage("nl", "K", "D", "T", "L", "P");
        addLanguage("pt", "R", "D", "T", "B", "C");
        addLanguage("tr", "Ş", "V", "K", "F", "A");
    }

    /**
     * Adds a language which is not supported by default.*
     *
     * @param languageCode the language code
     * @param k            king
     * @param q            queen
     * @param r            rook
     * @param b            bishop
     * @param n            knight
     */
    public static void addLanguage(final String languageCode, final String k, final String q, final String r, final String b, final String n) {
        if (LANGUAGE_CODE_2_NOTATION_MAPPING.containsKey(languageCode)) {
            throw new IllegalArgumentException("Can't override mapping for languageCode: " + languageCode);
        }
        LANGUAGE_CODE_2_NOTATION_MAPPING.put(languageCode, new NotationMapping(k, q, r, b, n));
    }

    public static NotationMapping getNotationMapping(final String languageCode) {
        return LANGUAGE_CODE_2_NOTATION_MAPPING.get(languageCode);
    }
}
