package io.github.wolfraam.chessgame.result;

/**
 * The result of a chess game.
 */
public class ChessGameResult {
    public final ChessGameResultType chessGameResultType;
    public final DrawType drawType;

    public ChessGameResult(final ChessGameResultType chessGameResultType) {
        this(chessGameResultType, null);
    }

    public ChessGameResult(final ChessGameResultType chessGameResultType, final DrawType drawType) {
        this.chessGameResultType = chessGameResultType;
        this.drawType = drawType;
    }
}
