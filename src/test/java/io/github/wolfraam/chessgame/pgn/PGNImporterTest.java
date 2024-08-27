package io.github.wolfraam.chessgame.pgn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.wolfraam.chessgame.ChessGame;
import io.github.wolfraam.chessgame.notation.NotationType;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PGNImporterTest {

    private ChessGame test(final String pgn, final String lastMove) {
        final Set<ChessGame> chessGameSet = new HashSet<>();
        try (final InputStream inputStream = new ByteArrayInputStream(pgn.getBytes())) {
            final PGNImporter pgnImporter = new PGNImporter();
            pgnImporter.setOnGame((game) -> {
                assertEquals(lastMove, game.getNotationList(NotationType.SAN).get(game.getMoves().size() - 1));
                chessGameSet.add(game);
            });
            pgnImporter.setOnError(System.out::println);
            pgnImporter.setOnWarning(System.out::println);
            pgnImporter.run(inputStream);
            return chessGameSet.iterator().next();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<String> testErrors(final String pgn) {
        final Set<String> errors = new HashSet<>();
        try (final InputStream inputStream = new ByteArrayInputStream(pgn.getBytes())) {
            final PGNImporter pgnImporter = new PGNImporter();
            pgnImporter.setOnError(errors::add);
            pgnImporter.setOnWarning(System.out::println);
            pgnImporter.run(inputStream);
            return errors;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<String> testWarnings(final String pgn) {
        final Set<String> warnings = new HashSet<>();
        try (final InputStream inputStream = new ByteArrayInputStream(pgn.getBytes())) {
            final PGNImporter pgnImporter = new PGNImporter();
            pgnImporter.setOnError(System.out::println);
            pgnImporter.setOnWarning(warnings::add);
            pgnImporter.setOnGame((game) -> {
            });
            pgnImporter.run(inputStream);
            return warnings;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testComments() {
        final String pgn =
                """
                        [Event "Event"]
                        [Site "Site"]
                        [Date "2023.01.29"]
                        [Round "13"]
                        [White "Erigaisi Arjun"]
                        [Black "Carlsen, Magnus"]
                        [Result "0-1"]

                        1. {comment before move 0} (variation before move 0) Nf3 {comment after move 0} (variation after move 0)\s
                        1... {comment before move 1} (variation before move 1) Nf6 {comment after move 1} (variation after move 1)\s
                        2. g3 {[%clk 1:40:41]} 2...b6 {comment after move 3} 3. Bg2 {[%clk 1:41:04]} 3... Bb7 {[%clk 1:37:32]} 0-1""";

        final ChessGame chessGame = test(pgn, "Bb7");

        assertEquals("comment before move 0", chessGame.getPGNData().getPGNMove2CommentBefore().get(0).get(0).getText());
        assertEquals(PGNComment.class, chessGame.getPGNData().getPGNMove2CommentBefore().get(0).get(0).getClass());
        assertEquals("variation before move 0", chessGame.getPGNData().getPGNMove2CommentBefore().get(0).get(1).getText());
        assertEquals(PGNVariation.class, chessGame.getPGNData().getPGNMove2CommentBefore().get(0).get(1).getClass());

        assertEquals("comment after move 0", chessGame.getPGNData().getPGNMove2CommentAfter().get(0).get(0).getText());
        assertEquals(PGNComment.class, chessGame.getPGNData().getPGNMove2CommentAfter().get(0).get(0).getClass());
        assertEquals("variation after move 0", chessGame.getPGNData().getPGNMove2CommentAfter().get(0).get(1).getText());
        assertEquals(PGNVariation.class, chessGame.getPGNData().getPGNMove2CommentAfter().get(0).get(1).getClass());

        assertEquals("comment after move 3", chessGame.getPGNData().getPGNMove2CommentAfter().get(3).get(0).getText());

    }

    @Test
    void testDontAcceptSetup() {
        final String pgn = """
                [Event ""]
                [Site ""]
                [Date ""]
                [Round ""]
                [White ""]
                [Black ""]
                [Result ""]
                [FEN "8/K7/8/8/8/1k6/1N1p4/8 w - - 0 1"]
                [SetUp "1"]

                Kb8 *""";

        final Set<ChessGame> chessGameSet = new HashSet<>();
        try (final InputStream inputStream = new ByteArrayInputStream(pgn.getBytes())) {
            final PGNImporter pgnImporter = new PGNImporter();
            pgnImporter.setOnGame(chessGameSet::add);
            pgnImporter.setOnError(System.out::println);
            pgnImporter.setOnWarning(System.out::println);
            pgnImporter.setAcceptTagsPredicate(pgnTagStringMap -> !pgnTagStringMap.containsKey(PGNTag.SET_UP));
            pgnImporter.run(inputStream);
            assertEquals(0, chessGameSet.size());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFile() {
        final PGNImporter pgnImporter = new PGNImporter();
        pgnImporter.setOnGame((game) -> {
        });
        pgnImporter.setOnError(System.out::println);
        pgnImporter.setOnWarning(System.out::println);

        File tempFile = null;
        try {
            tempFile = File.createTempFile("tmp", "tmp");
            pgnImporter.run(tempFile);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (tempFile != null) {
                tempFile.delete();
            }
        }
    }

    @Test
    void testImport() {
        final String pgn = """
                [Event "F/S Return Match"]
                [Site "Belgrade, Serbia JUG"]
                [Date "1992.11.04"]
                [Round "29"]
                [White "Fischer, Robert \\"Bobby\\" J."]
                [Black "Spassky \\\\ Boris V."]
                [Result "1/2-1/2"]
                \s
                1.e4 e5 2.Nf3 Nc6 3.Bb5 {Deze opening wordt Spaans genoemd.} a6 4.Ba4 Nf6
                5.O-O Be7 6.Re1 b5 7.Bb3 d6 8.c3 O-O 9. h3 Nb8 10.d4 Nbd7 11.c4 c6
                12.cxb5 axb5 13.Nc3 Bb7 14.Bg5 b4 15.Nb1 h6 16.Bh4 c5 17.dxe5 Nxe4
                18.Bxe7 Qxe7 19.exd6 Qf6 20.Nbd2 Nxd6 21.Nc4 Nxc4 22.Bxc4 Nb6 23.Ne5
                Rae8 24.Bxf7+ Rxf7 25.Nxf7 Rxe1+ 26.Qxe1 Kxf7 27.Qe3 Qg5 28.Qxg5 hxg5
                29.b3 Ke6 30.a3 Kd6 31.axb4 cxb4 32.Ra5 Nd5 33. f3 Bc8 34.Kf2 Bf5 35.Ra7
                g6 36.Ra6+ Kc5 37.Ke1 Nf4 38.g3 Nxh3 39.Kd2 Kb5 40.Rd6 Kc5 41.Ra6 Nf2
                42.g4 Bd3 43.Re6 1-0""";

        final ChessGame chessGame = test(pgn, "Re6");

        assertEquals("Fischer, Robert \"Bobby\" J.", chessGame.getPGNData().getPGNTagValue(PGNTag.WHITE));
        assertEquals("Spassky \\ Boris V.", chessGame.getPGNData().getPGNTagValue(PGNTag.BLACK));

    }

    @Test
    void testImportChessCom() {
        final String pgn = """
                [Event "?"]
                [Site "?"]
                [Date "????.??.??"]
                [Round "?"]
                [White "?"]
                [Black "?"]
                [Result "*"]

                1. d4 f5 2. Nc3 e5 3. dxe5 (3. d5 {Bla} h6 (3... g6)) {Bla} 3... h6 *""";

        test(pgn, "h6");
    }

    @Test
    void testImportInvalidMoveNumber() {
        final String pgn = """
                [Event "?"]
                [Site "?"]
                [Date "????.??.??"]

                1. d4 f5 3. Nc3 0-1""";

        assertEquals("Line:5 Invalid move number 3 in line: 1. d4 f5 3. Nc3 ", testErrors(pgn).iterator().next());
    }

    @Test
    void testImportLiChess() {
        final String pgn = """
                [Event "?"]
                [Site "?"]
                [Date "????.??.??"]
                [Round "?"]
                [White "?"]
                [Black "?"]
                [Result "*"]
                [WhiteElo "?"]
                [BlackElo "?"]
                [Variant "Standard"]
                [TimeControl "-"]
                [ECO "A80"]
                [Opening "Dutch Defense: Raphael Variation"]
                [Termination "Unterminated"]
                [Annotator "lichess.org"]

                1. d4 f5 2. Nc3 { A80 Dutch Defense: Raphael Variation } e5 3. dxe5 h6 0-1""";

        test(pgn, "h6");
    }

    @Test
    void testImportNoCorrectEnding() {
        final String pgn = """
                [Event "?"]
                [Site "?"]
                [Date "????.??.??"]

                1. d4 f5 2. Nc3
                [Site "?"]
                """;

        assertEquals("Line:6 Error: Previous Game did not end properly", testErrors(pgn).iterator().next());
    }

    @Test
    void testImportWithSetup() {
        final String pgn = """
                [Event ""]
                [Site ""]
                [Date ""]
                [Round ""]
                [White ""]
                [Black ""]
                [Result ""]
                [FEN "8/K7/8/8/8/1k6/1N1p4/8 w - - 0 1"]
                [SetUp "1"]

                Kb8 *""";

        final ChessGame chessGame = test(pgn, "Kb8");
        final ChessGame chessGameStartingPosition = chessGame.getSubset(0);

        assertEquals("8/K7/8/8/8/1k6/1N1p4/8 w - - 0 1", chessGameStartingPosition.getFen());
        assertEquals("8/K7/8/8/8/1k6/1N1p4/8 w - - 0 1", chessGame.getInitialFen());
    }

    @Test
    void testIncorrectTag() {
        final String pgn = """
                [Event "?"]
                [Site ...

                1. d4 f5 2. Nc3 0-1""";

        assertEquals("Line:2 Unknown Tag, line: [Site ...", testWarnings(pgn).iterator().next());
        test(pgn, "Nc3");
    }

    @Test
    void testIoException() {
        final PGNImporter pgnImporter = new PGNImporter();
        pgnImporter.setFen2NewChessGameFunction(ChessGame::new);
        pgnImporter.setOnGame((game) -> {
        });
        pgnImporter.setOnError(System.out::println);
        pgnImporter.setOnWarning(System.out::println);

        Assertions.assertThrows(RuntimeException.class, () ->
                pgnImporter.run(new InputStream() {
                    @Override
                    public int read() throws IOException {
                        throw new IOException();
                    }
                }));
    }

    @Test
    void testNoTags() {
        final String pgn = "1. d4 f5 2. Nc3 0-1";
        assertEquals("Line:1 Expected PGN Tags", testErrors(pgn).iterator().next());
    }

    @Test
    void testUnkownTag() {
        final String pgn = """
                [Event "?"]
                [Site "?"]
                [Asd "????.??.??"]

                1. d4 f5 2. Nc3 0-1""";

        assertEquals("Line:3 Unknown Tag, line: [Asd \"????.??.??\"]", testWarnings(pgn).iterator().next());
        test(pgn, "Nc3");
    }
}