package com.github.chessgame.pgn;

/**
 * A PGN tag with its value.
 */
public class PgnTagAndValue {

    public static PgnTagAndValue fromLine(final String line) {
        final PgnTag pgnTag = determinePgnTag(line);
        if (pgnTag == null) {
            return null;
        }

        String restOfLine = line.substring(pgnTag.getTag().length() + 1);
        restOfLine = restOfLine.substring(restOfLine.indexOf('"') + 1);
        if (restOfLine.endsWith("\"]")) {
            String value = restOfLine.substring(0, restOfLine.lastIndexOf("\"]")).trim();
            while (value.contains("\\\"")) {
                value = value.replace("\\\"", "\"");
            }
            while (value.contains("\\\\")) {
                value = value.replace("\\\\", "\\");
            }
            return new PgnTagAndValue(pgnTag, value);
        } else {
            return null;
        }
    }

    private static PgnTag determinePgnTag(final String line) {
        for (final PgnTag pgnTag : PgnTag.values()) {
            if (line.startsWith("[" + pgnTag.getTag() + " ")) {
                return pgnTag;
            }
        }
        return null;
    }

    public final PgnTag pgnTag;

    public final String value;

    public PgnTagAndValue(final PgnTag pgnTag, final String value) {
        this.pgnTag = pgnTag;
        this.value = value;
    }
}
