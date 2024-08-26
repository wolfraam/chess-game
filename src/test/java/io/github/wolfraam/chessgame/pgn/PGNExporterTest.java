package io.github.wolfraam.chessgame.pgn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.wolfraam.chessgame.ChessGame;
import io.github.wolfraam.chessgame.notation.NotationType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PGNExporterTest {

    private static final String PGN = "[Event \"World \\\" Championship 28th\"]\n" +
            "[Site \"Reykjavik \\\\ Reykjavik\"]\n" +
            "[Date \"1972.??.??\"]\n" +
            "[Round \"18\"]\n" +
            "[White \"Fischer, Robert James\"]\n" +
            "[Black \"Spassky, Boris V\"]\n" +
            "[Result \"1/2-1/2\"]\n" +
            "[BlackElo \"2660\"]\n" +
            "[WhiteElo \"2785\"]\n" +
            "[ECO \"B69\"]\n" +
            "\n" +
            "1.e4 c5 2.Nf3 d6 3.Nc3 Nc6 4.d4 cxd4 5.Nxd4 Nf6 6.Bg5 e6 7.Qd2 a6 8.O-O-O Bd7\n" +
            "9.f4 Be7 10.Nf3 b5 11.Bxf6 gxf6 12.Bd3 Qa5 13.Kb1 b4 14.Ne2 Qc5 15.f5 a5\n" +
            "16.Nf4 a4 17.Rc1 Rb8 18.c3 b3 19.a3 Ne5 20.Rhf1 Nc4 21.Bxc4 Qxc4 22.Rce1 Kd8\n" +
            "23.Ka1 Rb5 24.Nd4 Ra5 25.Nd3 Kc7 26.Nb4 h5 27.g3 Re5 28.Nd3 Rb8 29.Qe2 Ra5\n" +
            "30.fxe6 fxe6 31.Rf2 e5 32.Nf5 Bxf5 33.Rxf5 d5 34.exd5 Qxd5 35.Nb4 Qd7 36.Rxh5\n" +
            "Bxb4 37.cxb4 Rd5 38.Rc1+ Kb7 39.Qe4 Rc8 40.Rb1 Kb6 41.Rh7 Rd4 42.Qg6 Qc6\n" +
            "43.Rf7 Rd6 44.Qh6 Qf3 45.Qh7 Qc6 46.Qh6 Qf3 47.Qh7 Qc6 1/2-1/2\n";

    private static final String PGN_2_GAMES = PGN + "\n" + PGN;

    @Test
    public void testCheckResult() {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, "e4 c5 Nf3");
        chessGame.getPGNData().setPGNTag(PGNTag.EVENT, "Test Event");
        final PGNExporter pgnExporter = new PGNExporter(System.out);
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                pgnExporter.write(chessGame));
    }

    @Test
    public void testExport2Games() {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final PGNExporter pgnExporter = new PGNExporter(byteArrayOutputStream);
        for (final ChessGame game : getGames()) {
            pgnExporter.write(game);
        }
        final String pgn = byteArrayOutputStream.toString();
        assertEquals(PGN_2_GAMES, pgn);
    }

    private Set<ChessGame> getGames() {
        final Set<ChessGame> chessGameSet = new HashSet<>();
        try (final InputStream inputStream = new ByteArrayInputStream(PGN_2_GAMES.getBytes())) {
            final PGNImporter pgnImporter = new PGNImporter();
            pgnImporter.setOnGame(chessGameSet::add);
            pgnImporter.setOnError(System.out::println);
            pgnImporter.setOnWarning(System.out::println);
            pgnImporter.run(inputStream);
            return chessGameSet;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}