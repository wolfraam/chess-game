package com.github.chessgame.move;

import com.github.chessgame.board.Board;
import com.github.chessgame.board.Piece;
import com.github.chessgame.board.PieceType;
import com.github.chessgame.board.Side;
import com.github.chessgame.board.Square;
import com.github.chessgame.move.castle.CastleMoveType;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Computes the targets for a piece.
 */
public class TargetHelper {

    private static final TargetData TARGET_DATA = new TargetData().init();
    private boolean attackOnly;
    private final Board board;
    private Square to;

    public TargetHelper(final Board board) {
        this.board = board;
    }

    public Set<Square> getFromSet(final Side side) {
        final Set<Square> returnSet = EnumSet.noneOf(Square.class);
        for (final Square from : board.getOccupiedSquares()) {
            final Piece piece = board.getPiece(from);
            if (piece.side == side) {
                final Set<Square> targets = getTargets(from);
                if (!targets.isEmpty()) {
                    returnSet.add(from);
                }
            }
        }
        return returnSet;
    }

    public Set<Square> getTargets(final Square from) {
        final Piece piece = board.getPiece(from);
        final Set<Square> returnSet = EnumSet.noneOf(Square.class);
        switch (piece.pieceType) {
            case PAWN:
                Set<Square> pawnTargets = TARGET_DATA.getPawnTargets(from, piece);
                if (to != null) {
                    if (pawnTargets.contains(to)) {
                        pawnTargets = Collections.singleton(to);
                    } else {
                        pawnTargets = Collections.emptySet();
                    }
                }
                for (final Square potentialTo : pawnTargets) {
                    final boolean doubleMove = Math.abs(potentialTo.y - from.y) == 2;
                    final boolean capture = potentialTo.x != from.x;
                    final Piece targetPiece = board.getPiece(potentialTo);
                    if (doubleMove) {
                        final Square inBetweenSquare = Square.fromCoordinates(potentialTo.x, potentialTo.y + (board.getSideToMove() == Side.WHITE ? -1 : 1));
                        if (targetPiece == null && board.getPiece(inBetweenSquare) == null && !attackOnly) {
                            returnSet.add(potentialTo);
                        }
                    } else if (capture) {
                        if (targetPiece != null && targetPiece.side != piece.side) {
                            returnSet.add(potentialTo);
                        }
                        if (targetPiece == null && attackOnly) {
                            returnSet.add(potentialTo);
                        }
                        if (board.isEnPassant(from, potentialTo)) {
                            returnSet.add(potentialTo);
                        }
                    } else {
                        if (targetPiece == null && !attackOnly) {
                            returnSet.add(potentialTo);
                        }
                    }
                }
                break;
            case KNIGHT:
            case KING:
                Set<Square> kingOrKnightTargets = TARGET_DATA.getKingOrKnightTargets(from, piece);
                if (to != null) {
                    if (kingOrKnightTargets.contains(to)) {
                        kingOrKnightTargets = Collections.singleton(to);
                    } else {
                        kingOrKnightTargets = Collections.emptySet();
                    }
                }
                for (final Square potentialTo : kingOrKnightTargets) {
                    if (piece.pieceType == PieceType.KING && Math.abs(from.x - potentialTo.x) == 2) {
                        final CastleMoveType castleMoveType = CastleMoveType.determine(from, potentialTo, piece);
                        if (castleMoveType != null) {
                            if (!attackOnly) {
                                if (canCastle(castleMoveType)) {
                                    returnSet.add(potentialTo);
                                }
                            }
                        }
                    } else {
                        final Piece targetPiece = board.getPiece(potentialTo);
                        if (targetPiece == null || targetPiece.side != piece.side) {
                            returnSet.add(potentialTo);
                        }
                    }
                }
                break;
            default:
                final List<List<Square>> squareListList = TARGET_DATA.getOtherTargets(from, piece);
                for (final List<Square> squareList : squareListList) {
                    for (final Square potentialTo : squareList) {
                        final Piece targetPiece = board.getPiece(potentialTo);
                        if (targetPiece == null || targetPiece.side != piece.side) {
                            if (to == null || to == potentialTo) {
                                returnSet.add(potentialTo);
                            }
                        }
                        if (targetPiece != null) {
                            break;
                        }
                    }
                }
                break;
        }
        return returnSet;
    }

    public boolean hasTargets(final Side side) {
        for (final Square from : board.getOccupiedSquares()) {
            final Piece piece = board.getPiece(from);
            if (piece.side == side) {
                final Set<Square> targets = getTargets(from);
                if (!targets.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setAttackOnly() {
        attackOnly = true;
    }

    public void setTo(final Square to) {
        this.to = to;
    }

    private boolean canCastle(final CastleMoveType castleMoveType) {
        return board.canCastle(castleMoveType)
                && squaresAreEmpty(castleMoveType.requiringEmptySquares)
                && board.getPiece(castleMoveType.rookFrom) == castleMoveType.rookPiece;
    }

    private boolean squaresAreEmpty(final Set<Square> squares) {
        for (final Square square : squares) {
            if (!board.squareIsEmpty(square)) {
                return false;
            }
        }
        return true;
    }
}
