package io.github.wolfraam.chessgame.board;

/**
 * Chess sides, black and white.
 */
public enum Side {
    BLACK, WHITE;

    public Side flip() {
        if (this == BLACK) {
            return WHITE;
        }
        return BLACK;
    }
}
