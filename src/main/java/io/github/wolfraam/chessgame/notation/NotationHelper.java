package io.github.wolfraam.chessgame.notation;

import io.github.wolfraam.chessgame.board.Board;
import io.github.wolfraam.chessgame.board.Piece;
import io.github.wolfraam.chessgame.board.PieceType;
import io.github.wolfraam.chessgame.board.Square;
import io.github.wolfraam.chessgame.move.IllegalMoveException;
import io.github.wolfraam.chessgame.move.KingState;
import io.github.wolfraam.chessgame.move.Move;
import io.github.wolfraam.chessgame.move.MoveHelper;
import io.github.wolfraam.chessgame.move.castle.CastleMoveType;
import io.github.wolfraam.chessgame.notation.lan.LanParser;
import io.github.wolfraam.chessgame.notation.san.SanParser;
import java.util.HashSet;
import java.util.Set;

/**
 * Parses or formats a move in a notation type.
 */
public class NotationHelper {

    private static final NotationMapping NOTATION_MAPPING_FAN = new NotationMapping("♚", "♛", "♜", "♝", "♞");
    private static final NotationMapping NOTATION_MAPPING_UCI = new NotationMapping("k", "q", "r", "b", "n");

    public Move getMove(final NotationMapping notationMapping, final Board board, final NotationType notationType, final String moveString) {
        return switch (notationType) {
            case SAN -> new SanParser().convertToMove(board, notationMapping, moveString);
            case UCI -> new Move(Square.fromName(moveString.substring(0, 2)),
                    Square.fromName(moveString.substring(2, 4)),
                    moveString.length() < 5 ? null : NOTATION_MAPPING_UCI.getPieceType(moveString.substring(4, 5)));
            case LAN -> new LanParser().convertToMove(board, notationMapping, moveString);
            case FAN -> new SanParser().convertToMove(board, NOTATION_MAPPING_FAN, moveString);
        };
    }

    public String getMoveNotation(final NotationMapping notationMapping, final Board board, final NotationType notationType, final Move move) {
        if (!new MoveHelper(board).isLegalMove(move)) {
            throw new IllegalMoveException("Move " + move + " is illegal");
        }

        return switch (notationType) {
            case SAN -> getSanNotation(board, notationMapping, move);
            case UCI -> getUciNotation(move);
            case LAN -> getLanNotation(board, notationMapping, move);
            case FAN -> getSanNotation(board, NOTATION_MAPPING_FAN, move);
        };

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
        moves.removeIf(move -> move.from.name.charAt(0) != from.name.charAt(0));
        return moves.size() == 1;
    }

    private boolean disambiguatesToOneMoveByRow(final Set<Move> legalMoves, final Square from) {
        final Set<Move> moves = new HashSet<>(legalMoves);
        moves.removeIf(move -> move.from.name.charAt(1) != from.name.charAt(1));
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
