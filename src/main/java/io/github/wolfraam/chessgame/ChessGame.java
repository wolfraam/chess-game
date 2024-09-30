package io.github.wolfraam.chessgame;

import io.github.wolfraam.chessgame.board.Board;
import io.github.wolfraam.chessgame.board.Piece;
import io.github.wolfraam.chessgame.board.Side;
import io.github.wolfraam.chessgame.board.Square;
import io.github.wolfraam.chessgame.move.IllegalMoveException;
import io.github.wolfraam.chessgame.move.KingState;
import io.github.wolfraam.chessgame.move.Move;
import io.github.wolfraam.chessgame.move.MoveHelper;
import io.github.wolfraam.chessgame.notation.LanguageSettings;
import io.github.wolfraam.chessgame.notation.NotationHelper;
import io.github.wolfraam.chessgame.notation.NotationMapping;
import io.github.wolfraam.chessgame.notation.NotationType;
import io.github.wolfraam.chessgame.opening.ChessOpening;
import io.github.wolfraam.chessgame.opening.ChessOpeningHelper;
import io.github.wolfraam.chessgame.pgn.PGNData;
import io.github.wolfraam.chessgame.pgn.PGNTag;
import io.github.wolfraam.chessgame.result.ChessGameResult;
import io.github.wolfraam.chessgame.result.ChessGameResultType;
import io.github.wolfraam.chessgame.result.DrawType;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * A Chess Game.
 */
public class ChessGame implements Serializable, Cloneable {

    private static final ChessOpeningHelper CHESS_OPENING_HELPER = new ChessOpeningHelper();
    private static final String DEFAULT_LANGUAGE_CODE = "en";
    private static final NotationMapping DEFAULT_NOTATION_MAPPING = LanguageSettings.getNotationMapping(DEFAULT_LANGUAGE_CODE);
    public static final String STANDARD_INITIAL_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private final Board board;
    private final MoveHelper moveHelper;
    private final List<Move> moves = new LinkedList<>();
    private final PGNData pgnData = new PGNData();

    /**
     * Constructs a chess game with the initial position.
     */
    public ChessGame() {
        this(STANDARD_INITIAL_FEN);
    }

    /**
     * Constructs a chess game with the starting position of the fen argument.
     */
    public ChessGame(final String fen) {
        this(STANDARD_INITIAL_FEN.equals(fen) ? Board.fromInitialPosition() : Board.fromFen(fen));
    }

    /**
     * Constructs a chess game with the given board.
     */
    private ChessGame(final Board board) {
        this.board = board;
        moveHelper = new MoveHelper(board);
    }

    /**
     * Clones the chess game.
     */
    @Override
    @SuppressWarnings("all")
    public ChessGame clone() {
        final ChessGame chessGame = new ChessGame(board.clone());
        chessGame.moves.addAll(moves);
        chessGame.pgnData.copyFrom(pgnData);
        return chessGame;
    }

    /**
     * @return an ascii representation of the board
     */
    public String getASCII() {
        return board.getASCII();
    }

    /**
     * @return a set of all the PGN tags available. These are filled when a PGN import has been done.
     */
    public Set<PGNTag> getAvailablePGNTags() {
        return pgnData.getAvailablePGNTags();
    }

    /**
     * @return a list of all captured pieces in the order in which they were captured.
     */
    public List<Piece> getCapturedPieces() {
        final List<Piece> capturedPieces = new LinkedList<>();
        final ChessGame replayChessGame = new ChessGame(getInitialFen());
        for (final Move move : moves) {
            final Piece capturedPiece = replayChessGame.playMove(move);
            if (capturedPiece != null) {
                capturedPieces.add(capturedPiece);
            }
        }
        return capturedPieces;
    }

    /**
     * @return the chess opening for this game.
     */
    public ChessOpening getChessOpening() {
        return CHESS_OPENING_HELPER.getChessOpening(this);
    }

    /**
     * @return the current board in FEN notation (Forsyth-Edwards Notation)
     */
    public String getFen() {
        return board.getFen();
    }

    /**
     * A shorter version of FEN, without new lines and slashes and which combines empty squares
     * across files.
     */
    public String getFenSmall() {
        return board.getFenSmall();
    }

    /**
     * The number of the full moves. It starts at 1 and is incremented after Black's move.
     */
    public int getFullMoveCount() {
        return board.getFullMoveCount();
    }

    /**
     * The result of the game as determined by the board. This does NOT take the "Result" tag of an PGN import
     * into account.
     * Returns null if there is no result yet.
     */
    public ChessGameResult getGameResult() {
        if (moveHelper.getKingState(board.getSideToMove(), true) == KingState.MATE) {
            return new ChessGameResult(ChessGameResultType.fromWinningSide(board.getSideToMove().flip()));
        } else if (board.isDrawInsufficientMaterial()) {
            return new ChessGameResult(ChessGameResultType.DRAW, DrawType.INSUFFICIENT_MATERIAL);
        } else if (board.isDrawFiftyMoveRule()) {
            return new ChessGameResult(ChessGameResultType.DRAW, DrawType.FIFTY_MOVE_RULE);
        } else if (board.isDrawThreefoldRepetition()) {
            return new ChessGameResult(ChessGameResultType.DRAW, DrawType.THREEFOLD_REPETITION);
        } else if (!moveHelper.hasLegalMoves()) {
            return new ChessGameResult(ChessGameResultType.DRAW, DrawType.STALE_MATE);
        }
        return null;
    }

    /**
     * The result of the game as determined by the board. This does NOT take the "Result" tag of an PGN import
     * into account.
     * Returns null if there is no result yet.
     */
    public ChessGameResultType getGameResultType() {
        final ChessGameResult chessGameResult = getGameResult();
        if (chessGameResult != null) {
            return chessGameResult.chessGameResultType;
        }
        return null;
    }

    /**
     * The fen of the board before move 1.
     */
    public String getInitialFen() {
        return board.getInitialFen();
    }

    /**
     * The last played move, or null if no moves have been played.
     */
    public Move getLastMove() {
        if (!moves.isEmpty()) {
            return moves.get(moves.size() - 1);
        } else {
            return null;
        }
    }

    /**
     * @return all legal moves for the current position.
     */
    public Set<Move> getLegalMoves() {
        return moveHelper.getLegalMoves();
    }

    /**
     * @return all legal moves for the piece on the square.
     */
    public Set<Move> getLegalMoves(final Square from) {
        return moveHelper.getLegalMoves(from);
    }

    /**
     * @return the Move parsed by the rules of the notationType
     * @throws IllegalMoveException if the notation is not correct
     */
    public Move getMove(final NotationType notationType, final String move) throws IllegalMoveException {
        return getMove(notationType, DEFAULT_LANGUAGE_CODE, move);
    }

    /**
     * @return the Move parsed by the rules of the notationType and the given language
     * @throws IllegalMoveException if the notation is not correct
     */
    public Move getMove(final NotationType notationType, final String languageCode, final String move) throws IllegalMoveException {
        final NotationMapping notationMapping = getNotationMapping(languageCode);
        return new NotationHelper().getMove(notationMapping, board, notationType, move);
    }

    /**
     * @return all played moves of this game.
     */
    public List<Move> getMoves() {
        return Collections.unmodifiableList(moves);
    }

    /**
     * @return the notation of the move in the given notationType
     */
    public String getNotation(final NotationType notationType, final Move move) {
        return getNotation(notationType, DEFAULT_LANGUAGE_CODE, move);
    }

    /**
     * @return the notation of the move in the given notationType and language
     */
    public String getNotation(final NotationType notationType, final String languageCode, final Move move) {
        final NotationMapping notationMapping = getNotationMapping(languageCode);
        return new NotationHelper().getMoveNotation(notationMapping, board, notationType, move);
    }

    /**
     * @return all played moves of this game in the given notationType
     */
    public List<String> getNotationList(final NotationType notationType) {
        return getNotationList(notationType, DEFAULT_LANGUAGE_CODE);
    }

    /**
     * @return all played moves of this game in the given notationType and language
     */
    public List<String> getNotationList(final NotationType notationType, final String languageCode) {
        final List<String> list = new LinkedList<>();
        final NotationMapping notationMapping = getNotationMapping(languageCode);
        final ChessGame replayChessGame = new ChessGame(getInitialFen());
        final NotationHelper notationHelper = new NotationHelper();
        for (final Move move : moves) {
            list.add(notationHelper.getMoveNotation(notationMapping, replayChessGame.board, notationType, move));
            replayChessGame.playMove(move);
        }
        return list;
    }

    /**
     * @return the occupied squares of the current board
     */
    public Set<Square> getOccupiedSquares() {
        return board.getOccupiedSquares();
    }

    /**
     * @return the PGNData.
     */
    public PGNData getPGNData() {
        return pgnData;
    }

    /**
     * @return piece on the square of the current board.
     */
    public Piece getPiece(final Square square) {
        return board.getPiece(square);
    }

    /**
     * The current score. Returns a positive integer when white has captured more, negative when black has captured more
     * and 0 when black and white have captured the same. pawn = 1, knight = 3, bishop = 3, rook = 5, queen = 9
     */
    public int getScore() {
        int white = 0;
        int black = 0;

        for (final Square square : getOccupiedSquares()) {
            final Piece piece = getPiece(square);
            switch (piece) {
                case WHITE_PAWN:
                    white++;
                    break;
                case WHITE_KNIGHT:
                case WHITE_BISHOP:
                    white += 3;
                    break;
                case WHITE_ROOK:
                    white += 5;
                    break;
                case WHITE_QUEEN:
                    white += 9;
                    break;
                case BLACK_PAWN:
                    black++;
                    break;
                case BLACK_KNIGHT:
                case BLACK_BISHOP:
                    black += 3;
                    break;
                case BLACK_ROOK:
                    black += 5;
                    break;
                case BLACK_QUEEN:
                    black += 9;
                    break;
                case WHITE_KING:
                case BLACK_KING:
                    break;
            }
        }
        return white - black;
    }

    /**
     * @return the side whose turn it is to move.
     */
    public Side getSideToMove() {
        return board.getSideToMove();
    }

    /**
     * @return all the squares where the given piece is on the board
     */
    public Set<Square> getSquares(final Piece piece) {
        return board.getSquares(piece);
    }

    /**
     * @return all the squares of pieces which are checking the opposing king
     */
    public Set<Square> getSquaresAttackingKing() {
        return moveHelper.getSquaresAttackingKing(getSideToMove());
    }

    /**
     * @return a new chess game which a subset of the moves of this game.
     */
    public ChessGame getSubset(final int moveCount) {
        final ChessGame chessGame = new ChessGame(getInitialFen());
        int i = 0;
        for (final Move move : getMoves()) {
            if (i >= moveCount) {
                break;
            }
            chessGame.playMove(move);
            i++;
        }
        return chessGame;
    }

    /**
     * @return is the king checked?
     */
    public boolean isKingAttacked() {
        return moveHelper.getKingState(board.getSideToMove(), false) == KingState.CHECK;
    }

    /**
     * @return whether the move is legal.
     */
    public boolean isLegalMove(final Move move) {
        return moveHelper.isLegalMove(move);
    }

    /**
     * @return plays the move. Does NOT check whether the move is legal.
     */
    public Piece playMove(final Move move) {
        final Piece capturedPiece = board.playMove(move.from, move.to, move.promotion, true);
        moves.add(move);
        return capturedPiece;
    }

    /**
     * Plays the move given in the notation type.
     *
     * @throws IllegalMoveException if the notation is not correct
     */
    public void playMove(final NotationType notationType, final String move) throws IllegalMoveException {
        playMove(notationType, DEFAULT_LANGUAGE_CODE, move);
    }

    /**
     * Plays the move given in the language and notation type.
     *
     * @throws IllegalMoveException if the notation is not correct
     */
    public void playMove(final NotationType notationType, final String languageCode, final String move) throws IllegalMoveException {
        playMove(getMove(notationType, languageCode, move));
    }

    /**
     * Plays the moves given in notation type.
     *
     * @param movesArgument the moves space or comma seperated
     * @throws IllegalMoveException if a notation is not correct
     */
    public void playMoves(final NotationType notationType, final String movesArgument) throws IllegalMoveException {
        playMoves(notationType, DEFAULT_LANGUAGE_CODE, movesArgument);
    }

    /**
     * Plays the moves given in the notation type.
     *
     * @throws IllegalMoveException if a notation is not correct
     */
    public void playMoves(final NotationType notationType, final List<String> moveList) throws IllegalMoveException {
        playMoves(DEFAULT_LANGUAGE_CODE, notationType, moveList);
    }

    /**
     * Plays the moves given in the notation type and language.
     *
     * @param movesArgument the moves space or comma seperated
     * @throws IllegalMoveException if a notation is not correct
     */
    public void playMoves(final NotationType notationType, final String languageCode, final String movesArgument) throws IllegalMoveException {
        playMoves(languageCode, notationType, stringToList(movesArgument));
    }

    /**
     * Plays the moves given in the notation type and language.
     *
     * @throws IllegalMoveException if a notation is not correct
     */
    public void playMoves(final String languageCode, final NotationType notationType, final List<String> moveList) throws IllegalMoveException {
        for (final String move : moveList) {
            playMove(notationType, languageCode, move);
        }
    }

    private NotationMapping getNotationMapping(final String languageCode) {
        if (languageCode.equals(DEFAULT_LANGUAGE_CODE)) {
            return DEFAULT_NOTATION_MAPPING;
        }
        NotationMapping notationMapping = LanguageSettings.getNotationMapping(languageCode);
        if (notationMapping == null) {
            notationMapping = DEFAULT_NOTATION_MAPPING;
        }
        return notationMapping;
    }

    private List<String> stringToList(final String movesArgument) {
        final List<String> list = new LinkedList<>();
        final StringTokenizer stringTokenizer = new StringTokenizer(movesArgument, " ,");
        while (stringTokenizer.hasMoreTokens()) {
            list.add(stringTokenizer.nextToken());
        }
        return list;
    }
}
