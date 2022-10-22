package com.github.chessgame.notation;

import com.github.chessgame.board.Board;
import com.github.chessgame.board.Piece;
import com.github.chessgame.board.PieceType;
import com.github.chessgame.board.Square;
import com.github.chessgame.move.IllegalMoveException;
import com.github.chessgame.move.KingState;
import com.github.chessgame.move.Move;
import com.github.chessgame.move.MoveHelper;
import com.github.chessgame.move.castle.CastleMoveType;
import com.github.chessgame.notation.lan.LanParser;
import com.github.chessgame.notation.san.SanParser;
import com.github.chessgame.util.RemoveIfSupport;
import java.util.HashSet;
import java.util.Set;

/**
 * Parses or formats a move in a notation type.
 */
public class NotationHelper {

    private static final NotationMapping NOTATION_MAPPING_FAN = new NotationMapping("♚", "♛", "♜", "♝", "♞");
    private static final NotationMapping NOTATION_MAPPING_UCI = new NotationMapping("k", "q", "r", "b", "n");

    public Move getMove(final NotationMapping notationMapping, final Board board, final NotationType notationType, final String moveString) {
        Move move = null;
        switch (notationType) {
            case SAN:
                move = new SanParser().convertToMove(board, notationMapping, moveString);
                break;
            case UCI:
                move = new Move(Square.fromName(moveString.substring(0, 2)),
                        Square.fromName(moveString.substring(2, 4)),
                        moveString.length() < 5 ? null : NOTATION_MAPPING_UCI.getPieceType(moveString.substring(4, 5)));
                break;
            case LAN:
                move = new LanParser().convertToMove(board, notationMapping, moveString);
                break;
            case FAN:
                move = new SanParser().convertToMove(board, NOTATION_MAPPING_FAN, moveString);
                break;
        }
        return move;
    }

    public String getMoveNotation(final NotationMapping notationMapping, final Board board, final NotationType notationType, final Move move) {
        if (!new MoveHelper(board).isLegalMove(move)) {
            throw new IllegalMoveException("Move " + move + " is illegal");
        }

        String string = null;
        switch (notationType) {
            case SAN:
                string = getSanNotation(board, notationMapping, move);
                break;
            case UCI:
                string = getUciNotation(move);
                break;
            case LAN:
                string = getLanNotation(board, notationMapping, move);
                break;
            case FAN:
                string = getSanNotation(board, NOTATION_MAPPING_FAN, move);
                break;
        }
        return string;

    }

    private void appendKingState(final Board board, final Move move, final StringBuilder stringBuilder) {
        final KingState kingState = new MoveHelper(board).getKingStateAfterMove(board.getSideToMove().flip(), move, true);
        if (kingState == KingState.CHECK) {
            stringBuilder.append('+');
        } else if (kingState == KingState.MATE) {
            stringBuilder.append('#');
        }
    }


    private boolean disambiguatesToOneMoveByFile(final Set<Move> legalMoves, final Square from) {
        final Set<Move> moves = new HashSet<>(legalMoves);
        RemoveIfSupport.run(moves, move -> move.from.name.charAt(0) != from.name.charAt(0));
        return moves.size() == 1;
    }

    private boolean disambiguatesToOneMoveByRow(final Set<Move> legalMoves, final Square from) {
        final Set<Move> moves = new HashSet<>(legalMoves);
        RemoveIfSupport.run(moves, move -> move.from.name.charAt(1) != from.name.charAt(1));
        return moves.size() == 1;
    }

    private String getLanNotation(final Board board, final NotationMapping notationMapping, final Move move) {
        final Piece piece = board.getPiece(move.from);
        final StringBuilder stringBuilder = new StringBuilder();
        final CastleMoveType castleMoveType = CastleMoveType.determine(move.from, move.to, piece);
        if (castleMoveType != null) {
            stringBuilder.append(castleMoveType.castleType.notation);
        } else {
            boolean isCapture = board.getPiece(move.to) != null;
            if (!isCapture && board.isEnPassant(move.from, move.to)) {
                // En passant
                isCapture = true;
            }

            stringBuilder.append(notationMapping.getNotation(piece.pieceType));
            stringBuilder.append(move.from.name);

            if (isCapture) {
                stringBuilder.append('x');
            } else {
                stringBuilder.append('-');
            }

            stringBuilder.append(move.to.name);

            if (move.promotion != null) {
                stringBuilder.append('=');
                stringBuilder.append(notationMapping.getNotation(move.promotion));
            }
        }

        appendKingState(board, move, stringBuilder);
        return stringBuilder.toString();
    }

    private String getSanNotation(final Board board, final NotationMapping notationMapping, final Move move) {
        final Piece piece = board.getPiece(move.from);
        final Set<Move> legalMoves = new MoveHelper(board).getLegalMoves(piece, move.to);
        final StringBuilder stringBuilder = new StringBuilder();

        final CastleMoveType castleMoveType = CastleMoveType.determine(move.from, move.to, piece);
        if (castleMoveType != null) {
            stringBuilder.append(castleMoveType.castleType.notation);
        } else {
            boolean isCapture = board.getPiece(move.to) != null;
            if (!isCapture && board.isEnPassant(move.from, move.to)) {
                // En passant
                isCapture = true;
            }

            if (piece.pieceType != PieceType.PAWN) {
                stringBuilder.append(notationMapping.getNotation(piece.pieceType));

                if (1 < legalMoves.size()) {
                    if (disambiguatesToOneMoveByFile(legalMoves, move.from)) {
                        stringBuilder.append(move.from.name.charAt(0));
                    } else if (disambiguatesToOneMoveByRow(legalMoves, move.from)) {
                        stringBuilder.append(move.from.name.charAt(1));
                    } else {
                        stringBuilder.append(move.from.name);
                    }
                }
            } else {
                if (isCapture) {
                    stringBuilder.append(move.from.name.charAt(0));
                }
            }

            if (isCapture) {
                stringBuilder.append('x');
            }

            stringBuilder.append(move.to.name);

            if (move.promotion != null) {
                stringBuilder.append('=');
                stringBuilder.append(notationMapping.getNotation(move.promotion));
            }
        }

        appendKingState(board, move, stringBuilder);
        return stringBuilder.toString();
    }

    private String getUciNotation(final Move move) {
        final String firstPart = move.from.name + move.to.name;
        if (move.promotion == null) {
            return firstPart;
        }
        return firstPart + NOTATION_MAPPING_UCI.getNotation(move.promotion);
    }
}
