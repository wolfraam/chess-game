package io.github.wolfraam.chessgame.pgn;

/**
 * A PGN tag with its value.
 */
public class PGNTagAndValue {

    public static PGNTagAndValue fromLine(final String line) {
        final PGNTag pgnTag = determinePGNTag(line);
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
            return new PGNTagAndValue(pgnTag, value);
        } else {
            return null;
        }
    }

    private static PGNTag determinePGNTag(final String line) {
        for (final PGNTag pgnTag : PGNTag.values()) {
            if (line.startsWith("[" + pgnTag.getTag() + " ")) {
                return pgnTag;
            }
        }
        return null;
    }

    public final PGNTag pgnTag;

    public final String value;

    public PGNTagAndValue(final PGNTag pgnTag, final String value) {
        this.pgnTag = pgnTag;
        this.value = value;
    }
}
