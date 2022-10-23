package io.github.wolfraam.chessgame.board;

import io.github.wolfraam.chessgame.util.EnumMapEnhanced;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Chess Piece.
 */
public enum Piece {
    WHITE_PAWN(Side.WHITE, PieceType.PAWN, 'P'),
    WHITE_KNIGHT(Side.WHITE, PieceType.KNIGHT, 'N'),
    WHITE_BISHOP(Side.WHITE, PieceType.BISHOP, 'B'),
    WHITE_ROOK(Side.WHITE, PieceType.ROOK, 'R'),
    WHITE_QUEEN(Side.WHITE, PieceType.QUEEN, 'Q'),
    WHITE_KING(Side.WHITE, PieceType.KING, 'K'),
    BLACK_PAWN(Side.BLACK, PieceType.PAWN, 'p'),
    BLACK_KNIGHT(Side.BLACK, PieceType.KNIGHT, 'n'),
    BLACK_BISHOP(Side.BLACK, PieceType.BISHOP, 'b'),
    BLACK_ROOK(Side.BLACK, PieceType.ROOK, 'r'),
    BLACK_QUEEN(Side.BLACK, PieceType.QUEEN, 'q'),
    BLACK_KING(Side.BLACK, PieceType.KING, 'k');

    private static final Map<Character, Piece> FEN_NOTATION_2_PIECE = new HashMap<>();
    private static final EnumMapEnhanced<PieceType, Map<Side, Piece>> PIECE_TYPE_2_SIDE_2_PIECE = new EnumMapEnhanced<>(PieceType.class);

    static {
        for (final Piece piece : Piece.values()) {
            PIECE_TYPE_2_SIDE_2_PIECE.computeIfAbsentSupport(piece.pieceType, k -> (new EnumMap<>(Side.class))).put(piece.side, piece);
            FEN_NOTATION_2_PIECE.put(piece.fenCharacter, piece);
        }
    }

    public static Piece fromFenNotation(final char fenNotation) {
        return FEN_NOTATION_2_PIECE.get(fenNotation);
    }

    public static Piece fromPieceTypeAndSide(final PieceType pieceType, final Side side) {
        return PIECE_TYPE_2_SIDE_2_PIECE.get(pieceType).get(side);
    }

    public final char fenCharacter;
    public final PieceType pieceType;
    public final Side side;

    Piece(final Side side, final PieceType pieceType, final char fenCharacter) {
        this.side = side;
        this.pieceType = pieceType;
        this.fenCharacter = fenCharacter;
    }
}
