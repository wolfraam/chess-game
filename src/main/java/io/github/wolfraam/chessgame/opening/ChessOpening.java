package io.github.wolfraam.chessgame.opening;

/**
 * A Chess Opening.
 */
public class ChessOpening {
    private final String eco;
    private final boolean endOfVariant;
    private final String name;
    private final String variation;

    public ChessOpening(final String eco, final String name, final String variation, final boolean endOfVariant) {
        this.eco = eco;
        this.name = name;
        this.variation = variation;
        this.endOfVariant = endOfVariant;
    }

    public String getEco() {
        return eco;
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

    public String getName() {
        return name;
    }

    public String getVariation() {
        return variation;
    }

    public boolean isEndOfVariation() {
        return endOfVariant;
    }
}
