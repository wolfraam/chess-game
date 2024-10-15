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

    private static final String PGN = """
            [Event "World \\" Championship 28th"]
            [Site "Reykjavik \\\\ Reykjavik"]
            [Date "1972.??.??"]
            [Round "18"]
            [White "Fischer, Robert James"]
            [Black "Spassky, Boris V"]
            [Result "1/2-1/2"]
            [BlackElo "2660"]
            [WhiteElo "2785"]
            [ECO "B69"]
                        
            1. e4 1... c5 2. Nf3 2... d6 3. Nc3 3... Nc6 4. d4 4... cxd4 5. Nxd4 5... Nf6
            6. Bg5 6... e6 7. Qd2 7... a6 8. O-O-O 8... Bd7 9. f4 9... Be7 10. Nf3 10...
            b5 11. Bxf6 11... gxf6 12. Bd3 12... Qa5 13. Kb1 13... b4 14. Ne2 14... Qc5
            15. f5 15... a5 16. Nf4 16... a4 17. Rc1 17... Rb8 18. c3 18... b3 19. a3
            19... Ne5 20. Rhf1 20... Nc4 21. Bxc4 21... Qxc4 22. Rce1 22... Kd8 23. Ka1
            23... Rb5 24. Nd4 24... Ra5 25. Nd3 25... Kc7 26. Nb4 26... h5 27. g3 27...
            Re5 28. Nd3 28... Rb8 29. Qe2 29... Ra5 30. fxe6 30... fxe6 31. Rf2 31... e5
            32. Nf5 32... Bxf5 33. Rxf5 33... d5 34. exd5 34... Qxd5 35. Nb4 35... Qd7 36.
            Rxh5 36... Bxb4 37. cxb4 37... Rd5 38. Rc1+ 38... Kb7 39. Qe4 39... Rc8 40.
            Rb1 40... Kb6 41. Rh7 41... Rd4 42. Qg6 42... Qc6 43. Rf7 43... Rd6 44. Qh6
            44... Qf3 45. Qh7 45... Qc6 46. Qh6 46... Qf3 47. Qh7 47... Qc6 1/2-1/2
            """;
    private static final String PGN_2_GAMES = PGN + "\n" + PGN;
    private static final String PGN_COMMENTS = """
            [Event "Live Chess"]
            [Site "Chess.com"]
            [Date "2024.08.23"]
            [Round "?"]
            [White "a"]
            [Black "b"]
            [Result "1-0"]
            [ECO "D20"]
                        
            1. {comment0} (variation0) d4 {comment1} (variation1) 1... {comment2} d5
            {comment3} 2. {variation2} c4 {variation3} 2... {comment4} dxc4 3. e4
            {comment4} 1-0
            """;

    private Set<ChessGame> getGames(final String pgn) {
        final Set<ChessGame> chessGameSet = new HashSet<>();
        try (final InputStream inputStream = new ByteArrayInputStream(pgn.getBytes())) {
            final PGNImporter pgnImporter = new PGNImporter();
            pgnImporter.setOnGame(chessGameSet::add);
            pgnImporter.setOnError((string, e) -> System.out.println(string));
            pgnImporter.setOnWarning(System.out::println);
            pgnImporter.run(inputStream);
            return chessGameSet;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void test2Games() {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final PGNExporter pgnExporter = new PGNExporter(byteArrayOutputStream);
        for (final ChessGame game : getGames(PGN_2_GAMES)) {
            pgnExporter.write(game);
        }
        final String pgn = byteArrayOutputStream.toString();
        assertEquals(PGN_2_GAMES, pgn);
    }

    @Test
    void testCheckResult() {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, "e4 c5 Nf3");
        chessGame.getPGNData().setPGNTag(PGNTag.EVENT, "Test Event");
        final PGNExporter pgnExporter = new PGNExporter(System.out);
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                pgnExporter.write(chessGame));
    }

    @Test
    void testComments() {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final PGNExporter pgnExporter = new PGNExporter(byteArrayOutputStream);
        for (final ChessGame game : getGames(PGN_COMMENTS)) {
            pgnExporter.write(game);
        }
        final String pgn = byteArrayOutputStream.toString();
        assertEquals(PGN_COMMENTS, pgn);
    }
}