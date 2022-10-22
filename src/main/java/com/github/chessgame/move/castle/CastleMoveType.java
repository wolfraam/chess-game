package com.github.chessgame.move.castle;

import com.github.chessgame.board.Piece;
import com.github.chessgame.board.PieceType;
import com.github.chessgame.board.Side;
import com.github.chessgame.board.Square;
import java.util.EnumSet;
import java.util.Set;

/**
 * The four different castle move types.
 */
public enum CastleMoveType {
    BLACK_KING_SIDE(CastleType.KING_SIDE, Side.BLACK),
    BLACK_QUEEN_SIDE(CastleType.QUEEN_SIDE, Side.BLACK),
    WHITE_KING_SIDE(CastleType.KING_SIDE, Side.WHITE),
    WHITE_QUEEN_SIDE(CastleType.QUEEN_SIDE, Side.WHITE);

    public static CastleMoveType determine(final Square from, final Square to, final Piece piece) {
        if (piece.pieceType == PieceType.KING && Math.abs(from.x - to.x) == 2) {
            final boolean isKingSide = from.x < to.x;
            if (piece.side == Side.BLACK && from == Square.E8) {
                if (isKingSide && to == Square.G8) {
                    return BLACK_KING_SIDE;
                }
                if (!isKingSide && to == Square.C8) {
                    return BLACK_QUEEN_SIDE;
                }
            }
            if (piece.side == Side.WHITE && from == Square.E1) {
                if (isKingSide && to == Square.G1) {
                    return WHITE_KING_SIDE;
                }
                if (!isKingSide && to == Square.C1) {
                    return WHITE_QUEEN_SIDE;
                }
            }
        }
        return null;
    }

    public final CastleType castleType;
    public final Set<Square> requiringEmptySquares;
    public final Square rookFrom;
    public final Piece rookPiece;
    public final Square rookTo;
    public final Side side;

    CastleMoveType(final CastleType castleType, final Side side) {
        this.castleType = castleType;
        this.side = side;

        rookPiece = Piece.fromPieceTypeAndSide(PieceType.ROOK, side);

        switch (castleType) {
            case KING_SIDE:
                switch (side) {
                    case BLACK:
                        requiringEmptySquares = EnumSet.of(Square.F8, Square.G8);
                        rookFrom = Square.H8;
                        rookTo = Square.F8;
                        return;
                    case WHITE:
                    default:
                        requiringEmptySquares = EnumSet.of(Square.F1, Square.G1);
                        rookFrom = Square.H1;
                        rookTo = Square.F1;
                        return;
                }
            default:
            case QUEEN_SIDE:
                switch (side) {
                    case BLACK:
                        requiringEmptySquares = EnumSet.of(Square.B8, Square.C8, Square.D8);
                        rookFrom = Square.A8;
                        rookTo = Square.D8;
                        return;
                    default:
                    case WHITE:
                        requiringEmptySquares = EnumSet.of(Square.B1, Square.C1, Square.D1);
                        rookFrom = Square.A1;
                        rookTo = Square.D1;
                        return;
                }
        }
    }
}