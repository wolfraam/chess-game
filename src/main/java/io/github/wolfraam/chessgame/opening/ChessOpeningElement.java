package io.github.wolfraam.chessgame.opening;

import java.util.TreeMap;

/**
 * One of the elements in the chess opening tree.
 */
public class ChessOpeningElement extends TreeMap<String, ChessOpeningElement> {
    private String eco;
    private String name;
    private String variation;

    public String getEco() {
        return eco;
    }

    public String getName() {
        return name;
    }

    public String getVariation() {
        return variation;
    }

    public void setEco(final String eco) {
        this.eco = eco;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setVariation(final String variation) {
        this.variation = variation;
    }
}
