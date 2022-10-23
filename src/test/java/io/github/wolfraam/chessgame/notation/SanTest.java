package io.github.wolfraam.chessgame.notation;

import io.github.wolfraam.chessgame.ChessGame;
import io.github.wolfraam.chessgame.board.Square;
import io.github.wolfraam.chessgame.move.IllegalMoveException;
import io.github.wolfraam.chessgame.move.Move;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SanTest {

    @Test
    public void checkGetMoveNotationIllegal() {
        checkGetMoveNotationIllegal(new Move(Square.A3, Square.H6));
        checkGetMoveNotationIllegal(new Move(Square.B1, Square.B3));
        checkGetMoveNotationIllegal(new Move(Square.A2, Square.C3));
        checkGetMoveNotationIllegal(new Move(Square.A7, Square.A6));
    }

    @Test
    public void checkPlayMovesIllegal() {
        checkPlayMovesIllegal("e9");
        checkPlayMovesIllegal("Qa0");
        checkPlayMovesIllegal("Nb3");
        checkPlayMovesIllegal("Nc3 e6 e3 Nf6 Ne2");
    }

    private void checkGetMoveNotationIllegal(final Move move) {
        Assertions.assertThrows(IllegalMoveException.class, () -> new ChessGame().getNotation(NotationType.SAN, move));
    }

    private void checkPlayMovesIllegal(final String san) {
        Assertions.assertThrows(IllegalMoveException.class, () -> new ChessGame().playMoves(NotationType.SAN, san));
    }
}