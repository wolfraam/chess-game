package io.github.wolfraam.chessgame.notation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.wolfraam.chessgame.ChessGame;
import org.junit.jupiter.api.Test;

class UciTest {

    @Test
    public void test1() {
        final String sanGame = "a4 h5 a5 h4 a6 h3 axb7 hxg2 bxa8=Q gxh1=R Nc3 Nf6 b3 g6 Bb2 Bg7 e3 O-O Qh5 Rxg1 O-O-O Rxf1 Qh7+";
        final String uciGame = "a2a4 h7h5 a4a5 h5h4 a5a6 h4h3 a6b7 h3g2 b7a8q g2h1r b1c3 g8f6 b2b3 g7g6 c1b2 f8g7 e2e3 e8g8 d1h5 h1g1 e1c1 g1f1 h5h7";

        check(sanGame, uciGame);
    }

    @Test
    public void test2() {
        final String sanGame = "h4 a5 h5 a4 h6 a3 hxg7 axb2 gxh8=B bxa1=N Nf3 d6 e3 Bg4 Bc4 Nc6 O-O Qd7 Bd5 O-O-O Bxc6 Nxc2 Qxc2 Nf6 Qb3 Bxf3 Qxb7#";
        final String uciGame = "h2h4 a7a5 h4h5 a5a4 h5h6 a4a3 h6g7 a3b2 g7h8b b2a1n g1f3 d7d6 e2e3 c8g4 f1c4 b8c6 e1g1 d8d7 c4d5 e8c8 d5c6 a1c2 d1c2 g8f6 c2b3 g4f3 b3b7";

        check(sanGame, uciGame);
    }

    @Test
    public void test3() {
        final String sanGame = "e4 c6 e5 f5 exf6 Kf7 Ke2";
        final String uciGame = "e2e4 c7c6 e4e5 f7f5 e5f6 e8f7 e1e2";

        check(sanGame, uciGame);
    }

    private void check(final String sanGame, final String uciGame) {
        final ChessGame chessGame1 = new ChessGame();
        chessGame1.playMoves(NotationType.SAN, sanGame);
        assertEquals(uciGame, String.join(" ", chessGame1.getNotationList(NotationType.UCI)));

        final ChessGame chessGame2 = new ChessGame();
        chessGame2.playMoves(NotationType.UCI, uciGame);
        assertEquals(sanGame, String.join(" ", chessGame2.getNotationList(NotationType.SAN)));
    }
}