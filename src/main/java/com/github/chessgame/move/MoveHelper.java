package com.github.chessgame.move;

import com.github.chessgame.board.Board;
import com.github.chessgame.board.Piece;
import com.github.chessgame.board.PieceType;
import com.github.chessgame.board.Side;
import com.github.chessgame.board.Square;
import com.github.chessgame.move.castle.CastleMoveType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Computes different things for chess moves.
 */
public class MoveHelper implements Serializable {
    private final Board board;

    public MoveHelper(final Board board) {
        this.board = board;
    }

    public KingState getKingState(final Side kingSide, final boolean checkOnMate) {
        final Square kingSquare = board.getSquares(Piece.fromPieceTypeAndSide(PieceType.KING, kingSide)).iterator().next();
        final TargetHelper targetHelper = new TargetHelper(board);
        targetHelper.setTo(kingSquare);
        targetHelper.setAttackOnly();
        final boolean isCheck = targetHelper.hasTargets(kingSide.flip());
        if (isCheck) {
            if (checkOnMate && isMate()) {
                return KingState.MATE;
            }
            return KingState.CHECK;
        }
        return KingState.NORMAL;
    }

    public KingState getKingStateAfterMove(final Side kingSide, final Move move, final boolean checkOnMate) {
        return board.playMoveAndRollBack(move.from, move.to, move.promotion, () -> getKingState(kingSide, checkOnMate));
    }

    public Set<Move> getLegalMoves() {
        final Set<Move> moves = new HashSet<>();
        for (final Square from : board.getOccupiedSquares()) {
            if (board.getPiece(from).side == board.getSideToMove()) {
                for (final Square to : new TargetHelper(board).getTargets(from)) {
                    postProcessing(moves, from, to);
                }
            }
        }
        return moves;
    }

    public Set<Move> getLegalMoves(final Piece piece, final Square targetSquare) {
        final Set<Move> moves = new HashSet<>();
        if (piece.side == board.getSideToMove()) {
            for (final Square from : board.getSquares(piece)) {
                if (piece.pieceType != PieceType.PAWN || Math.abs(targetSquare.x - from.x) < 2) {
                    final TargetHelper targetHelper = new TargetHelper(board);
                    targetHelper.setTo(targetSquare);
                    for (final Square to : targetHelper.getTargets(from)) {
                        postProcessing(moves, from, to);
                    }
                }
            }
        }
        return moves;
    }

    public Set<Move> getLegalMoves(final Square from) {
        final Set<Move> moves = new HashSet<>();
        final Piece piece = board.getPiece(from);
        if (piece != null && piece.side == board.getSideToMove()) {
            for (final Square to : new TargetHelper(board).getTargets(from)) {
                postProcessing(moves, from, to);
            }
        }
        return moves;
    }

    public Set<Square> getSquaresAttackingKing(final Side kingSide) {
        final Square kingSquare = board.getSquares(Piece.fromPieceTypeAndSide(PieceType.KING, kingSide)).iterator().next();
        final TargetHelper targetHelper = new TargetHelper(board);
        targetHelper.setTo(kingSquare);
        targetHelper.setAttackOnly();
        return targetHelper.getFromSet(kingSide.flip());
    }

    public boolean hasLegalMoves() {
        final Set<Move> moves = new HashSet<>();
        for (final Square from : board.getOccupiedSquares()) {
            if (board.getPiece(from).side == board.getSideToMove()) {
                for (final Square to : new TargetHelper(board).getTargets(from)) {
                    postProcessing(moves, from, to);
                    if (!moves.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isLegalMove(final Move move) {
        final Piece piece = board.getPiece(move.from);
        if (piece != null && piece.side == board.getSideToMove()) {
            final TargetHelper targetHelper = new TargetHelper(board);
            targetHelper.setTo(move.to);
            final Set<Square> targets = targetHelper.getTargets(move.from);
            if (!targets.isEmpty()) {
                final Set<Move> moves = new HashSet<>();
                postProcessing(moves, move.from, move.to);
                return moves.contains(move);
            }
        }
        return false;
    }

    private boolean hasTargets(final Square to) {
        final TargetHelper targetHelper = new TargetHelper(board);
        targetHelper.setTo(to);
        targetHelper.setAttackOnly();
        return targetHelper.hasTargets(board.getSideToMove().flip());
    }

    private boolean isIllegalCastle(final Square from, final Square to, final Piece piece) {
        final CastleMoveType castleMoveType = CastleMoveType.determine(from, to, piece);
        if (castleMoveType != null) {
            // Is Check
            final Square kingSquare = board.getSquares(Piece.fromPieceTypeAndSide(PieceType.KING, board.getSideToMove())).iterator().next();
            if (hasTargets(kingSquare)) {
                return true;
            }
            // Is "pass through" field attacked
            return hasTargets(castleMoveType.rookTo);
        }
        return false;
    }

    // Prerequisite: check
    private boolean isMate() {
        for (final Square from : board.getOccupiedSquares()) {
            final Piece piece = board.getPiece(from);
            if (piece.side == board.getSideToMove()) {
                for (final Square to : new TargetHelper(board).getTargets(from)) {
                    if (!isIllegalCastle(from, to, piece)) {
                        if (getKingStateAfterMove(board.getSideToMove(), new Move(from, to), false) == KingState.NORMAL) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void postProcessing(final Set<Move> moves, final Square from, final Square to) {

        final Piece piece = board.getPiece(from);

        final Move move;
        if (piece.pieceType == PieceType.PAWN && (to.y == 0 || to.y == 7)) {
            move = new Move(from, to, PieceType.QUEEN);
        } else {
            move = new Move(from, to);
        }

        // Check After Move
        if (getKingStateAfterMove(board.getSideToMove(), move, false) != KingState.NORMAL) {
            return;
        }

        // Illegal castle
        if (isIllegalCastle(move.from, move.to, piece)) {
            return;
        }

        moves.add(move);

        if (move.promotion != null) {
            moves.add(new Move(move.from, move.to, PieceType.ROOK));
            moves.add(new Move(move.from, move.to, PieceType.BISHOP));
            moves.add(new Move(move.from, move.to, PieceType.KNIGHT));
        }
    }
}
