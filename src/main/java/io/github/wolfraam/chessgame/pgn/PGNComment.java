package io.github.wolfraam.chessgame.pgn;

import java.io.Serializable;

public class PGNComment implements Serializable {
    private final String text;

    public PGNComment(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
