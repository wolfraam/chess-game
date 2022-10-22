package com.github.chessgame.move.castle;

/**
 * The two castle move types for one side.
 */
public enum CastleType {
    KING_SIDE("O-O"), QUEEN_SIDE("O-O-O");

    public final String notation;

    CastleType(final String notation) {
        this.notation = notation;
    }
}