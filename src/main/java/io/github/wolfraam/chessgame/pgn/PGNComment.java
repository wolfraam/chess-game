package io.github.wolfraam.chessgame.pgn;

public class PGNComment {
    private final String text;

    public PGNComment(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
