package io.github.wolfraam.chessgame.board;

import java.util.EnumSet;
import java.util.Set;

/**
 * Piece Type.
 */
public enum PieceType {
    PAWN,
    KNIGHT,
    BISHOP,
    ROOK,
    QUEEN,
    KING;

    public static final Set<PieceType> NON_PAWNS = EnumSet.of(PieceType.KNIGHT, PieceType.BISHOP, PieceType.ROOK, PieceType.QUEEN, PieceType.KING);
}
