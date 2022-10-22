package com.github.chessgame.notation;

import com.github.chessgame.board.PieceType;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * The mapping of the pieces to their string representation.
 */
public class NotationMapping {
    private final Map<String, PieceType> notation2PieceType = new HashMap<>();
    private final Map<PieceType, String> pieceType2Notation = new EnumMap<>(PieceType.class);

    public NotationMapping(final String k, final String q, final String r, final String b, final String n) {
        if (k == null
                || q == null
                || r == null
                || b == null
                || n == null) {
            throw new NullPointerException();
        }

        notation2PieceType.put(k, PieceType.KING);
        notation2PieceType.put(q, PieceType.QUEEN);
        notation2PieceType.put(r, PieceType.ROOK);
        notation2PieceType.put(b, PieceType.BISHOP);
        notation2PieceType.put(n, PieceType.KNIGHT);

        pieceType2Notation.put(PieceType.KING, k);
        pieceType2Notation.put(PieceType.QUEEN, q);
        pieceType2Notation.put(PieceType.ROOK, r);
        pieceType2Notation.put(PieceType.BISHOP, b);
        pieceType2Notation.put(PieceType.KNIGHT, n);
    }

    public String getNotation(final PieceType pieceType) {
        if (pieceType == PieceType.PAWN) {
            return "";
        }
        return pieceType2Notation.get(pieceType);
    }

    public PieceType getPieceType(final String notation) {
        return notation2PieceType.get(notation);
    }
}
