package io.github.wolfraam.chessgame.notation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.wolfraam.chessgame.ChessGame;
import org.junit.jupiter.api.Test;

class FanTest {

    private void check(final String sanGame, final String fanGame) {
        final ChessGame chessGame1 = new ChessGame();
        chessGame1.playMoves(NotationType.SAN, sanGame);
        assertEquals(fanGame, String.join(" ", chessGame1.getNotationList(NotationType.FAN)));

        final ChessGame chessGame2 = new ChessGame();
        chessGame2.playMoves(NotationType.FAN, fanGame);
        assertEquals(sanGame, String.join(" ", chessGame2.getNotationList(NotationType.SAN)));
    }

    @Test
    void test1() {
        final String sanGame = "a4 h5 a5 h4 a6 h3 axb7 hxg2 bxa8=Q gxh1=R Nc3 Nf6 b3 g6 Bb2 Bg7 e3 O-O Qh5 Rxg1 O-O-O Rxf1 Qh7+";
        final String lanGame = "a4 h5 a5 h4 a6 h3 axb7 hxg2 bxa8=♛ gxh1=♜ ♞c3 ♞f6 b3 g6 ♝b2 ♝g7 e3 O-O ♛h5 ♜xg1 O-O-O ♜xf1 ♛h7+";

        check(sanGame, lanGame);
    }

    @Test
    void test2() {
        final String sanGame = "h4 a5 h5 a4 h6 a3 hxg7 axb2 gxh8=B bxa1=N Nf3 d6 e3 Bg4 Bc4 Nc6 O-O Qd7 Bd5 O-O-O Bxc6 Nxc2 Qxc2 Nf6 Qb3 Bxf3 Qxb7#";
        final String lanGame = "h4 a5 h5 a4 h6 a3 hxg7 axb2 gxh8=♝ bxa1=♞ ♞f3 d6 e3 ♝g4 ♝c4 ♞c6 O-O ♛d7 ♝d5 O-O-O ♝xc6 ♞xc2 ♛xc2 ♞f6 ♛b3 ♝xf3 ♛xb7#";

        check(sanGame, lanGame);
    }

    @Test
    void test3() {
        final String sanGame = "e4 c6 e5 f5 exf6 Kf7 Ke2";
        final String lanGame = "e4 c6 e5 f5 exf6 ♚f7 ♚e2";

        check(sanGame, lanGame);
    }
}