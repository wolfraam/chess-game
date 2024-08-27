package io.github.wolfraam.chessgame.board;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Chess board data, uses two maps for increased performance.
 */
public class BoardData implements Serializable, Cloneable {

    private final EnumMap<Piece, EnumSet<Square>> piece2Squares = new EnumMap<>(Piece.class);
    private final EnumMap<Square, Piece> square2Piece = new EnumMap<>(Square.class);

    @Override
    @SuppressWarnings("all")
    public BoardData clone() {
        final BoardData boardData = new BoardData();
        for (final Map.Entry<Square, Piece> entry : square2Piece.entrySet()) {
            boardData.putPieceOnSquare(entry.getKey(), entry.getValue());
        }
        return boardData;
    }

    public Set<Square> getOccupiedSquares() {
        return square2Piece.keySet();
    }

    public Piece getPiece(final Square square) {
        return square2Piece.get(square);
    }

    public Set<Square> getSquares(final Piece piece) {
        return piece2Squares.computeIfAbsent(piece, k -> EnumSet.noneOf(Square.class));
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
