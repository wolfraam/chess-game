package io.github.wolfraam.chessgame.opening;

/**
 * A Chess Opening.
 */
public class ChessOpening {
    public final String eco;
    public final String name;
    public final String variation;

    public ChessOpening(final String eco, final String name, final String variation) {
        this.eco = eco;
        this.name = name;
        this.variation = variation;
    }

    public String getFullName() {
        if (eco == null) {
            return null;
        }

        final StringBuilder stringBuilder = new StringBuilder();
        if (name != null) {
            stringBuilder.append(name);
        }
        if (variation != null) {
            stringBuilder.append(" / ").append(variation);
        }
        stringBuilder.append(" (").append(eco).append(")");
        return stringBuilder.toString();
    }
}
