package io.github.wolfraam.chessgame.board;

import io.github.wolfraam.chessgame.board.visual.BoardVisualizer;
import io.github.wolfraam.chessgame.move.castle.CastleMoveType;
import io.github.wolfraam.chessgame.util.Supplier;
import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The chess board.
 */
public class Board implements Serializable, Cloneable {

    public static Board fromFen(final String fen) {
        final Board board = new Board(new BoardData());
        final String[] fenParts = fen.split(" ");
        if (fenParts.length != 6) {
            throw new IllegalArgumentException("Invalid fen");
        }

        int squareIndex = 0;
        for (final char c : fenParts[0].toCharArray()) {
            if (c != '/') {
                if (Character.isDigit(c)) {
                    squareIndex += Integer.parseInt(String.valueOf(c));
                } else {
                    final Square square = Square.FEN_SQUARE_LIST.get(squareIndex);
                    board.boardData.putPieceOnSquare(square, Piece.fromFenNotation(c));
                    squareIndex++;
                }
            }
        }

        board.sideToMove = "w".equals(fenParts[1]) ? Side.WHITE : Side.BLACK;

        if (fenParts[2].contains("k")) {
            board.allowedCastleMoveTypes.add(CastleMoveType.BLACK_KING_SIDE);
        }
        if (fenParts[2].contains("q")) {
            board.allowedCastleMoveTypes.add(CastleMoveType.BLACK_QUEEN_SIDE);
        }
        if (fenParts[2].contains("K")) {
            board.allowedCastleMoveTypes.add(CastleMoveType.WHITE_KING_SIDE);
        }
        if (fenParts[2].contains("Q")) {
            board.allowedCastleMoveTypes.add(CastleMoveType.WHITE_QUEEN_SIDE);
        }

        if (!fenParts[3].equals("-")) {
            board.enPassantTarget = Square.fromName(fenParts[3]);
        }

        board.halfMoveCount = Integer.parseInt(fenParts[4]);
        board.fullMoveCount = Integer.parseInt(fenParts[5]);
        board.updateHistory();
        return board;
    }

    public static Board fromInitialPosition() {
        final Board board = new Board(new BoardData());

        board.boardData.putPieceOnSquare(Square.A1, Piece.WHITE_ROOK);
        board.boardData.putPieceOnSquare(Square.B1, Piece.WHITE_KNIGHT);
        board.boardData.putPieceOnSquare(Square.C1, Piece.WHITE_BISHOP);
        board.boardData.putPieceOnSquare(Square.D1, Piece.WHITE_QUEEN);
        board.boardData.putPieceOnSquare(Square.E1, Piece.WHITE_KING);
        board.boardData.putPieceOnSquare(Square.F1, Piece.WHITE_BISHOP);
        board.boardData.putPieceOnSquare(Square.G1, Piece.WHITE_KNIGHT);
        board.boardData.putPieceOnSquare(Square.H1, Piece.WHITE_ROOK);

        board.boardData.putPieceOnSquare(Square.A2, Piece.WHITE_PAWN);
        board.boardData.putPieceOnSquare(Square.B2, Piece.WHITE_PAWN);
        board.boardData.putPieceOnSquare(Square.C2, Piece.WHITE_PAWN);
        board.boardData.putPieceOnSquare(Square.D2, Piece.WHITE_PAWN);
        board.boardData.putPieceOnSquare(Square.E2, Piece.WHITE_PAWN);
        board.boardData.putPieceOnSquare(Square.F2, Piece.WHITE_PAWN);
        board.boardData.putPieceOnSquare(Square.G2, Piece.WHITE_PAWN);
        board.boardData.putPieceOnSquare(Square.H2, Piece.WHITE_PAWN);

        board.boardData.putPieceOnSquare(Square.A8, Piece.BLACK_ROOK);
        board.boardData.putPieceOnSquare(Square.B8, Piece.BLACK_KNIGHT);
        board.boardData.putPieceOnSquare(Square.C8, Piece.BLACK_BISHOP);
        board.boardData.putPieceOnSquare(Square.D8, Piece.BLACK_QUEEN);
        board.boardData.putPieceOnSquare(Square.E8, Piece.BLACK_KING);
        board.boardData.putPieceOnSquare(Square.F8, Piece.BLACK_BISHOP);
        board.boardData.putPieceOnSquare(Square.G8, Piece.BLACK_KNIGHT);
        board.boardData.putPieceOnSquare(Square.H8, Piece.BLACK_ROOK);

        board.boardData.putPieceOnSquare(Square.A7, Piece.BLACK_PAWN);
        board.boardData.putPieceOnSquare(Square.B7, Piece.BLACK_PAWN);
        board.boardData.putPieceOnSquare(Square.C7, Piece.BLACK_PAWN);
        board.boardData.putPieceOnSquare(Square.D7, Piece.BLACK_PAWN);
        board.boardData.putPieceOnSquare(Square.E7, Piece.BLACK_PAWN);
        board.boardData.putPieceOnSquare(Square.F7, Piece.BLACK_PAWN);
        board.boardData.putPieceOnSquare(Square.G7, Piece.BLACK_PAWN);
        board.boardData.putPieceOnSquare(Square.H7, Piece.BLACK_PAWN);

        board.sideToMove = Side.WHITE;
        board.allowedCastleMoveTypes.addAll(EnumSet.allOf(CastleMoveType.class));

        board.halfMoveCount = 0;
        board.fullMoveCount = 1;
        board.updateHistory();

        return board;
    }

    private final EnumSet<CastleMoveType> allowedCastleMoveTypes = EnumSet.noneOf(CastleMoveType.class);
    private final BoardData boardData;
    private Square enPassantTarget;
    private final Map<String, Integer> fen2Repetitions = new HashMap<>();
    private int fullMoveCount;
    private int halfMoveCount;
    private Side sideToMove;

    private Board(final BoardData boardData) {
        this.boardData = boardData;
    }

    public boolean canCastle(final CastleMoveType castleMoveType) {
        return allowedCastleMoveTypes.contains(castleMoveType);
    }

    @Override
    @SuppressWarnings("all")
    public Board clone() {
        final Board board = new Board(boardData.clone());
        board.allowedCastleMoveTypes.addAll(allowedCastleMoveTypes);
        board.enPassantTarget = enPassantTarget;
        board.fullMoveCount = fullMoveCount;
        board.halfMoveCount = halfMoveCount;
        board.fen2Repetitions.putAll(fen2Repetitions);
        board.sideToMove = sideToMove;

        return board;
    }

    public String getASCII() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final Square square : Square.FEN_SQUARE_LIST) {
            final Piece piece = boardData.getPiece(square);
            if (piece != null) {
                stringBuilder.append(piece.fenCharacter);
            } else {
                stringBuilder.append('_');
            }
            if (square.x == 7) {
                stringBuilder.append('\n');
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Returns an advanced printable ASCII representation of the board
     *
     * @return an advanced printable ASCII representation of the board
     */
    public String getBigAscii() {
        return BoardVisualizer.fenToBigAscii(getFen());
    }

    /**
     * Returns an advanced printable ASCII representation of the board from the specified perspective
     *
     * @param perspective the perspective of the board (which player is at the bottom)
     * @return an advanced printable ASCII representation of the board
     */
    public String getBigAscii(BoardVisualizer.Perspective perspective){
        return BoardVisualizer.fenToBigAscii(getFen(), perspective);
    }

    public String getFen() {
        return getFen(true);
    }

    public String getFenSmall() {
        final StringBuilder stringBuilder = new StringBuilder();
        int blankCount = 0;
        for (final Square square : Square.FEN_SQUARE_LIST) {
            final Piece piece = boardData.getPiece(square);
            if (piece != null) {
                if (blankCount != 0) {
                    stringBuilder.append(blankCount);
                    blankCount = 0;
                }
                stringBuilder.append(piece.fenCharacter);
            } else {
                blankCount++;
            }
        }
        if (sideToMove == Side.WHITE) {
            stringBuilder.append('w');
        }
        return stringBuilder.toString();
    }

    public int getFullMoveCount() {
        return fullMoveCount;
    }

    public Set<Square> getOccupiedSquares() {
        return boardData.getOccupiedSquares();
    }

    public Piece getPiece(final Square square) {
        return boardData.getPiece(square);
    }

    public Side getSideToMove() {
        return sideToMove;
    }

    public Set<Square> getSquares(final Piece piece) {
        return boardData.getSquares(piece);
    }

    public boolean isDrawFiftyMoveRule() {
        return 100 <= halfMoveCount;
    }

    public boolean isDrawInsufficientMaterial() {
        if (getSquares(Piece.BLACK_PAWN).isEmpty() &&
                getSquares(Piece.WHITE_PAWN).isEmpty() &&
                getSquares(Piece.BLACK_ROOK).isEmpty() &&
                getSquares(Piece.WHITE_ROOK).isEmpty() &&
                getSquares(Piece.BLACK_QUEEN).isEmpty() &&
                getSquares(Piece.WHITE_QUEEN).isEmpty()) {
            final Set<Square> blackBishopSquares = getSquares(Piece.BLACK_BISHOP);
            final Set<Square> whiteBishopSquares = getSquares(Piece.WHITE_BISHOP);

            final int blackBishopsAndKnights = blackBishopSquares.size() + getSquares(Piece.BLACK_KNIGHT).size();
            final int whiteBishopsAndKnights = whiteBishopSquares.size() + getSquares(Piece.WHITE_KNIGHT).size();

            if (blackBishopsAndKnights < 2 && whiteBishopsAndKnights < 2) {
                if (blackBishopsAndKnights == 0 || whiteBishopsAndKnights == 0) {
                    // king against king,
                    // king against king and bishop,
                    // king against king and knight
                    return true;
                } else {
                    final int blackBishops = blackBishopSquares.size();
                    final int whiteBishops = whiteBishopSquares.size();

                    // king and bishop against king and bishop, with both bishops on squares of the same color
                    if (blackBishops == 1 && whiteBishops == 1) {
                        return blackBishopSquares.iterator().next().squareColor ==
                                whiteBishopSquares.iterator().next().squareColor;
                    }
                }
            }
        }
        return false;
    }

    public boolean isDrawThreefoldRepetition() {
        final Integer repetitions = fen2Repetitions.get(getFen(false));
        return repetitions != null && 2 < repetitions;
    }

    public boolean isEnPassant(final Square from, final Square to) {
        if (to == enPassantTarget) {
            final Piece fromPiece = getPiece(from);
            if (fromPiece.pieceType == PieceType.PAWN) {
                if (from.x != to.x) {
                    return squareIsEmpty(to);
                }
            }
        }
        return false;
    }

    public Piece playMove(final Square from, final Square to, final PieceType promotion, final boolean updateHistory) {
        final boolean isEnPassant = isEnPassant(from, to);

        final Piece piece = boardData.getPiece(from);
        boardData.removePieceFromSquare(from);
        Piece capturedPiece = boardData.getPiece(to);

        if (isEnPassant) {
            final Square capturedPawnSquare = Square.fromCoordinates(to.x, from.y);
            capturedPiece = boardData.getPiece(capturedPawnSquare);
            boardData.removePieceFromSquare(capturedPawnSquare);
        } else if (capturedPiece != null) {
            boardData.removePieceFromSquare(to);
        }

        if (promotion != null) {
            boardData.putPieceOnSquare(to, Piece.fromPieceTypeAndSide(promotion, piece.side));
        } else {
            boardData.putPieceOnSquare(to, piece);
        }

        final CastleMoveType castleMoveType = CastleMoveType.determine(from, to, piece);
        if (castleMoveType != null) {
            final Piece rookPiece = boardData.getPiece(castleMoveType.rookFrom);
            boardData.removePieceFromSquare(castleMoveType.rookFrom);
            boardData.putPieceOnSquare(castleMoveType.rookTo, rookPiece);
        }

        if (piece.pieceType == PieceType.PAWN && Math.abs(from.y - to.y) == 2) {
            enPassantTarget = Square.fromCoordinates(to.x, to.y + (sideToMove == Side.WHITE ? -1 : 1));
        } else {
            enPassantTarget = null;
        }

        if ((piece == Piece.WHITE_ROOK && from == Square.A1) || (capturedPiece == Piece.WHITE_ROOK && to == Square.A1) || piece == Piece.WHITE_KING) {
            allowedCastleMoveTypes.remove(CastleMoveType.WHITE_QUEEN_SIDE);
        }
        if ((piece == Piece.WHITE_ROOK && from == Square.H1) || (capturedPiece == Piece.WHITE_ROOK && to == Square.H1) || piece == Piece.WHITE_KING) {
            allowedCastleMoveTypes.remove(CastleMoveType.WHITE_KING_SIDE);
        }
        if ((piece == Piece.BLACK_ROOK && from == Square.A8) || (capturedPiece == Piece.BLACK_ROOK && to == Square.A8) || piece == Piece.BLACK_KING) {
            allowedCastleMoveTypes.remove(CastleMoveType.BLACK_QUEEN_SIDE);
        }
        if ((piece == Piece.BLACK_ROOK && from == Square.H8) || (capturedPiece == Piece.BLACK_ROOK && to == Square.H8) || piece == Piece.BLACK_KING) {
            allowedCastleMoveTypes.remove(CastleMoveType.BLACK_KING_SIDE);
        }

        sideToMove = sideToMove.flip();
        if (sideToMove == Side.WHITE) {
            fullMoveCount++;
        }
        if (piece.pieceType == PieceType.PAWN || capturedPiece != null) {
            halfMoveCount = 0;
        } else {
            halfMoveCount++;
        }

        if (updateHistory) {
            if (piece.pieceType == PieceType.PAWN) {
                fen2Repetitions.clear();
            }
            updateHistory();
        }

        return capturedPiece;
    }

    public <T> T playMoveAndRollBack(final Square from, final Square to, final PieceType promotion, final Supplier<T> supplier) {

        // Store data
        final EnumSet<CastleMoveType> currentAllowedCastleMoveTypes = allowedCastleMoveTypes.clone();

        final Square currentEnPassantTarget = enPassantTarget;
        final int currentFullMoveCount = fullMoveCount;
        final int currenHalfMoveCount = halfMoveCount;

        // Play Move
        final boolean isEnPassant = isEnPassant(from, to);
        final Piece piece = boardData.getPiece(from);
        final Piece capturedPiece = playMove(from, to, promotion, false);

        // Call supplier
        final T returnValue = supplier.get();

        // Roll Back
        boardData.removePieceFromSquare(to);
        boardData.putPieceOnSquare(from, piece);

        if (isEnPassant) {
            final Square capturedPawnSquare = Square.fromCoordinates(to.x, from.y);
            boardData.putPieceOnSquare(capturedPawnSquare, capturedPiece);
        } else {
            if (capturedPiece != null) {
                boardData.putPieceOnSquare(to, capturedPiece);
            }
        }

        final CastleMoveType castleMoveType = CastleMoveType.determine(from, to, piece);
        if (castleMoveType != null) {
            final Piece rookPiece = boardData.getPiece(castleMoveType.rookTo);
            boardData.removePieceFromSquare(castleMoveType.rookTo);
            boardData.putPieceOnSquare(castleMoveType.rookFrom, rookPiece);
        }

        sideToMove = sideToMove.flip();

        allowedCastleMoveTypes.clear();
        allowedCastleMoveTypes.addAll(currentAllowedCastleMoveTypes);
        enPassantTarget = currentEnPassantTarget;
        fullMoveCount = currentFullMoveCount;
        halfMoveCount = currenHalfMoveCount;

        return returnValue;
    }

    public boolean squareIsEmpty(final Square square) {
        return boardData.getPiece(square) == null;
    }

    private String getFen(final boolean includeCounters) {
        final StringBuilder stringBuilder = new StringBuilder();
        int blankCount = 0;
        for (final Square square : Square.FEN_SQUARE_LIST) {
            final Piece piece = boardData.getPiece(square);
            if (piece != null) {
                if (blankCount != 0) {
                    stringBuilder.append(blankCount);
                    blankCount = 0;
                }
                stringBuilder.append(piece.fenCharacter);
            } else {
                blankCount++;
            }
            if (square.x == 7) {
                if (blankCount != 0) {
                    stringBuilder.append(blankCount);
                    blankCount = 0;
                }
                if (square.y != 0) {
                    stringBuilder.append('/');
                }
            }
        }
        stringBuilder.append(' ');
        if (sideToMove == Side.BLACK) {
            stringBuilder.append('b');
        } else if (sideToMove == Side.WHITE) {
            stringBuilder.append('w');
        }

        stringBuilder.append(' ');
        if (allowedCastleMoveTypes.contains(CastleMoveType.WHITE_KING_SIDE)) {
            stringBuilder.append('K');
        }
        if (allowedCastleMoveTypes.contains(CastleMoveType.WHITE_QUEEN_SIDE)) {
            stringBuilder.append('Q');
        }
        if (allowedCastleMoveTypes.contains(CastleMoveType.BLACK_KING_SIDE)) {
            stringBuilder.append('k');
        }
        if (allowedCastleMoveTypes.contains(CastleMoveType.BLACK_QUEEN_SIDE)) {
            stringBuilder.append('q');
        }
        if (allowedCastleMoveTypes.isEmpty()) {
            stringBuilder.append('-');
        }

        stringBuilder.append(' ');

        if (enPassantTarget == null) {
            stringBuilder.append('-');
        } else {
            stringBuilder.append(enPassantTarget.name);
        }

        if (includeCounters) {
            stringBuilder.append(' ');
            stringBuilder.append(halfMoveCount);

            stringBuilder.append(' ');
            stringBuilder.append(fullMoveCount);
        }

        return stringBuilder.toString();
    }

    private void updateHistory() {
        final String fen = getFen(false);
        Integer repetitions = fen2Repetitions.get(fen);
        if (repetitions == null) {
            repetitions = 0;
        }
        repetitions++;
        fen2Repetitions.put(fen, repetitions);
    }
}
