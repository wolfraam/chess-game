package io.github.wolfraam.chessgame;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.wolfraam.chessgame.ChessGame;
import io.github.wolfraam.chessgame.notation.NotationType;
import org.junit.jupiter.api.Test;

class MoveLanguageTest {
    @Test
    public void testDefaultEn() {
        final String enGame = "e4 e6 d4 d5 Nc3 Nf6 Bg5 dxe4 Nxe4 Be7 Bxf6 gxf6 g3 f5 Nc3 Bf6 Nge2 Nc6 d5 exd5 Nxd5 Bxb2 Bg2 O-O O-O Bh8 Nef4 Ne5 Qh5 Ng6 Rad1 c6 Ne3 Qf6 Kh1 Bg7 Bh3 Ne7 Rd3 Be6 Rfd1 Bh6 Rd4 Bxf4 Rxf4 Rad8 Rxd8 Rxd8 Bxf5 Nxf5 Nxf5 Rd5 g4 Bxf5 gxf5 h6 h3 Kh7 Qe2 Qe5 Qh5 Qf6 Qe2 Re5 Qd3 Rd5";

        check(enGame, enGame, "xx");
    }

    @Test
    public void testFr() {
        final String enGame = "e4 e6 d4 d5 Nc3 Nf6 Bg5 dxe4 Nxe4 Be7 Bxf6 gxf6 g3 f5 Nc3 Bf6 Nge2 Nc6 d5 exd5 Nxd5 Bxb2 Bg2 O-O O-O Bh8 Nef4 Ne5 Qh5 Ng6 Rad1 c6 Ne3 Qf6 Kh1 Bg7 Bh3 Ne7 Rd3 Be6 Rfd1 Bh6 Rd4 Bxf4 Rxf4 Rad8 Rxd8 Rxd8 Bxf5 Nxf5 Nxf5 Rd5 g4 Bxf5 gxf5 h6 h3 Kh7 Qe2 Qe5 Qh5 Qf6 Qe2 Re5 Qd3";
        final String frGame = "e4 e6 d4 d5 Cc3 Cf6 Fg5 dxe4 Cxe4 Fe7 Fxf6 gxf6 g3 f5 Cc3 Ff6 Cge2 Cc6 d5 exd5 Cxd5 Fxb2 Fg2 O-O O-O Fh8 Cef4 Ce5 Dh5 Cg6 Tad1 c6 Ce3 Df6 Rh1 Fg7 Fh3 Ce7 Td3 Fe6 Tfd1 Fh6 Td4 Fxf4 Txf4 Tad8 Txd8 Txd8 Fxf5 Cxf5 Cxf5 Td5 g4 Fxf5 gxf5 h6 h3 Rh7 De2 De5 Dh5 Df6 De2 Te5 Dd3";

        check(enGame, frGame, "fr");
    }

    @Test
    public void testNl() {
        final String enGame = "e4 e6 d4 d5 Nc3 Nf6 Bg5 dxe4 Nxe4 Be7 Bxf6 gxf6 g3 f5 Nc3 Bf6 Nge2 Nc6 d5 exd5 Nxd5 Bxb2 Bg2 O-O O-O Bh8 Nef4 Ne5 Qh5 Ng6 Rad1 c6 Ne3 Qf6 Kh1 Bg7 Bh3 Ne7 Rd3 Be6 Rfd1 Bh6 Rd4 Bxf4 Rxf4 Rad8 Rxd8 Rxd8 Bxf5 Nxf5 Nxf5 Rd5 g4 Bxf5 gxf5 h6 h3 Kh7 Qe2 Qe5 Qh5 Qf6 Qe2 Re5 Qd3 Rd5";
        final String nlGame = "e4 e6 d4 d5 Pc3 Pf6 Lg5 dxe4 Pxe4 Le7 Lxf6 gxf6 g3 f5 Pc3 Lf6 Pge2 Pc6 d5 exd5 Pxd5 Lxb2 Lg2 O-O O-O Lh8 Pef4 Pe5 Dh5 Pg6 Tad1 c6 Pe3 Df6 Kh1 Lg7 Lh3 Pe7 Td3 Le6 Tfd1 Lh6 Td4 Lxf4 Txf4 Tad8 Txd8 Txd8 Lxf5 Pxf5 Pxf5 Td5 g4 Lxf5 gxf5 h6 h3 Kh7 De2 De5 Dh5 Df6 De2 Te5 Dd3 Td5";

        check(enGame, nlGame, "nl");
    }

    private void check(final String sanGameEnglish, final String sanGameOtherLanguage, final String languageCode) {
        final ChessGame chessGame1 = new ChessGame();
        chessGame1.playMoves(NotationType.SAN, sanGameEnglish);
        assertEquals(sanGameOtherLanguage, String.join(" ", chessGame1.getNotationList(NotationType.SAN, languageCode)));

        final ChessGame chessGame2 = new ChessGame();
        chessGame2.playMoves(NotationType.SAN, languageCode, sanGameOtherLanguage);
        assertEquals(sanGameEnglish, String.join(" ", chessGame2.getNotationList(NotationType.SAN)));
    }


}