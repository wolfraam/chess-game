package io.github.wolfraam.chessgame.result;

import io.github.wolfraam.chessgame.board.Side;

/**
 * Possible result types of a chess game.
 */
public enum ChessGameResultType {
    WHITE_WINS, BLACK_WINS, DRAW;

    public static ChessGameResultType fromWinningSide(final Side side) {
        if (side == Side.BLACK) {
            return BLACK_WINS;
        } else {
            return WHITE_WINS;
        }
    }
}
