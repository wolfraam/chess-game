package io.github.wolfraam.chessgame.board;

import io.github.wolfraam.chessgame.util.EnumMapEnhanced;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Chess board data, uses two maps for increases performance.
 */
public class BoardData implements Serializable, Cloneable {

    private final EnumMapEnhanced<Piece, EnumSet<Square>> piece2Squares = new EnumMapEnhanced<>(Piece.class);
    private final EnumMap<Square, Piece> square2Piece = new EnumMap<>(Square.class);

    @Override
    @SuppressWarnings("all")
    public BoardData clone() {
        final BoardData bitBoard = new BoardData();
        for (final Map.Entry<Square, Piece> entry : square2Piece.entrySet()) {
            bitBoard.putPieceOnSquare(entry.getKey(), entry.getValue());
        }
        return bitBoard;
    }

    public Set<Square> getOccupiedSquares() {
        return square2Piece.keySet();
    }

    public Piece getPiece(final Square square) {
        return square2Piece.get(square);
    }

    public Set<Square> getSquares(final Piece piece) {
        return piece2Squares.computeIfAbsentSupport(piece, k -> EnumSet.noneOf(Square.class));
    }

    public void putPieceOnSquare(final Square square, final Piece piece) {
        final Piece previousPiece;
        if (piece != null) {
            previousPiece = square2Piece.put(square, piece);
        } else {
            previousPiece = square2Piece.remove(square);
        }
        if (previousPiece != null) {
            getSquares(previousPiece).remove(square);
        }
        if (piece != null) {
            getSquares(piece).add(square);
        }
    }

    public void removePieceFromSquare(final Square square) {
        putPieceOnSquare(square, null);
    }
}
