package io.github.wolfraam.chessgame;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import io.github.wolfraam.chessgame.ChessGame;
import io.github.wolfraam.chessgame.result.ChessGameResultType;
import org.junit.jupiter.api.Test;

class InsufficientMaterialTest {
    @Test
    public void testInsufficientMaterial() {
        // King vs King
        testDraw("K7/8/7k/8/8/8/8/8 w - - 0 1");

        // King vs King and Bishop
        testDraw("K7/8/7k/8/B7/8/8/8 w - - 0 1");
        testDraw("K7/8/7k/8/b7/8/8/8 w - - 0 1");

        // King and Bishop vs King and Bishop with both bishops on squares of the same color
        testDraw("K7/8/7k/8/B1b5/8/8/8 w - - 0 1");
    }

    @Test
    public void testSufficientMaterial() {
        // Pawn
        testNotDraw("K7/8/7k/8/p7/8/8/8 w - - 0 1");
        testNotDraw("K7/8/7k/8/P7/8/8/8 w - - 0 1");

        // Rook
        testNotDraw("K7/8/7k/8/r7/8/8/8 w - - 0 1");
        testNotDraw("K7/8/7k/8/R7/8/8/8 w - - 0 1");

        // Queen
        testNotDraw("K7/8/7k/8/q7/8/8/8 w - - 0 1");
        testNotDraw("K7/8/7k/8/Q7/8/8/8 w - - 0 1");

        // King vs King and 2 bishops
        testNotDraw("K7/8/7k/8/BB6/8/8/8 w - - 0 1");
        testNotDraw("K7/8/7k/8/bb6/8/8/8 w - - 0 1");

        // King and Bishop vs King and Bishop with both bishops on squares of different colors
        testNotDraw("K7/8/7k/8/Bb6/8/8/8 w - - 0 1");
    }

    private void testDraw(final String fen) {
        assertSame(ChessGameResultType.DRAW, new ChessGame(fen).getGameResult().chessGameResultType);
    }

    private void testNotDraw(final String fen) {
        assertNull(new ChessGame(fen).getGameResult());
    }
}