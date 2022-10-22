package com.github.chessgame.notation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.chessgame.ChessGame;
import com.github.chessgame.board.Square;
import com.github.chessgame.move.IllegalMoveException;
import com.github.chessgame.move.Move;
import java.util.StringTokenizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LanTest {

    @Test
    public void test1() {
        final String sanGame = "a4 h5 a5 h4 a6 h3 axb7 hxg2 bxa8=Q gxh1=R Nc3 Nf6 b3 g6 Bb2 Bg7 e3 O-O Qh5 Rxg1 O-O-O Rxf1 Qh7+";
        final String lanGame = "a2-a4 h7-h5 a4-a5 h5-h4 a5-a6 h4-h3 a6xb7 h3xg2 b7xa8=Q g2xh1=R Nb1-c3 Ng8-f6 b2-b3 g7-g6 Bc1-b2 Bf8-g7 e2-e3 O-O Qd1-h5 " +
                "Rh1xg1 O-O-O Rg1xf1 Qh5-h7+";

        check(sanGame, lanGame);
    }

    @Test
    public void test2() {
        final String sanGame = "h4 a5 h5 a4 h6 a3 hxg7 axb2 gxh8=B bxa1=N Nf3 d6 e3 Bg4 Bc4 Nc6 O-O Qd7 Bd5 O-O-O Bxc6 Nxc2 Qxc2 Nf6 Qb3 Bxf3 Qxb7#";
        final String lanGame = "h2-h4 a7-a5 h4-h5 a5-a4 h5-h6 a4-a3 h6xg7 a3xb2 g7xh8=B b2xa1=N Ng1-f3 d7-d6 e2-e3 Bc8-g4 Bf1-c4 Nb8-c6 O-O Qd8-d7 " +
                "Bc4-d5 O-O-O Bd5xc6 Na1xc2 Qd1xc2 Ng8-f6 Qc2-b3 Bg4xf3 Qb3xb7#";

        check(sanGame, lanGame);
    }

    @Test
    public void test3() {
        final String sanGame = "e4 c6 e5 f5 exf6 Kf7 Ke2";
        final String lanGame = "e2-e4 c7-c6 e4-e5 f7-f5 e5xf6 Ke8-f7 Ke1-e2";

        check(sanGame, lanGame);
    }

    @Test
    public void testGetSanNotationIllegal() {
        checkIllegal(new Move(Square.A3, Square.H6));
        checkIllegal(new Move(Square.B1, Square.B3));
        checkIllegal(new Move(Square.A2, Square.C3));
        checkIllegal(new Move(Square.A7, Square.A6));
    }

    @Test
    public void testIllegal() {
        checkIllegal("Rb1-c3");
        checkIllegal("e2-e5");
        checkIllegal("Qd1-d2");
        checkIllegal("Nb1-c0");
    }

    @Test
    public void testLegal() {
        checkLegal("Nb1-c3");
    }

    private void check(final String sanGame, final String lanGame) {
        final ChessGame chessGame1 = new ChessGame();
        chessGame1.playMoves(NotationType.SAN, sanGame);
        assertEquals(lanGame, String.join(" ", chessGame1.getNotationList(NotationType.LAN)));

        final ChessGame chessGame2 = new ChessGame();
        chessGame2.playMoves(NotationType.LAN, lanGame);
        assertEquals(sanGame, String.join(" ", chessGame2.getNotationList(NotationType.SAN)));
    }

    private void checkIllegal(final String lan) {
        Assertions.assertThrows(IllegalMoveException.class, () -> new ChessGame().playMoves(NotationType.LAN, lan));
    }

    private void checkIllegal(final Move move) {
        Assertions.assertThrows(IllegalMoveException.class, () -> new ChessGame().getNotation(NotationType.LAN, move));
    }

    private void checkLegal(final String movesArgument) {
        final StringTokenizer stringTokenizer = new StringTokenizer(movesArgument, " ");
        final ChessGame chessGame = new ChessGame();
        while (stringTokenizer.hasMoreTokens()) {
            final String moveString = stringTokenizer.nextToken();
            final Move move = chessGame.getMove(NotationType.LAN, moveString);
            assertEquals(chessGame.getNotation(NotationType.LAN, move), moveString);
            chessGame.playMove(NotationType.LAN, moveString);
        }
    }
}