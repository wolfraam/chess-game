package io.github.wolfraam.chessgame.board.visual;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BoardVisualizerTest {

    @Test
    public void testFenFromWhitePerspective() {
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        String expected =
                "+---+---+---+---+---+---+---+---+\n" +
                        "| r | n | b | q | k | b | n | r |  8\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "| p | p | p | p | p | p | p | p |  7\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   |   |   |   |   |   |   |   |  6\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   |   |   |   |   |   |   |   |  5\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   |   |   |   |   |   |   |   |  4\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   |   |   |   |   |   |   |   |  3\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "| P | P | P | P | P | P | P | P |  2\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "| R | N | B | Q | K | B | N | R |  1\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "  a   b   c   d   e   f   g   h\n";
        String actual = BoardVisualizer.fenToBigAscii(fen, BoardVisualizer.Perspective.WHITE);
        Assertions.assertEquals(expected, actual);

        fen = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2 ";
        expected =
                "+---+---+---+---+---+---+---+---+\n" +
                        "| r | n | b | q | k | b | n | r |  8\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "| p | p |   | p | p | p | p | p |  7\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   |   |   |   |   |   |   |   |  6\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   |   | p |   |   |   |   |   |  5\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   |   |   |   | P |   |   |   |  4\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   |   |   |   |   | N |   |   |  3\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "| P | P | P | P |   | P | P | P |  2\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "| R | N | B | Q | K | B |   | R |  1\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "  a   b   c   d   e   f   g   h\n";
        actual = BoardVisualizer.fenToBigAscii(fen);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFenFromBlackPerspective() {
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        String expected =
                "+---+---+---+---+---+---+---+---+\n" +
                        "| R | N | B | K | Q | B | N | R |  1\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "| P | P | P | P | P | P | P | P |  2\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   |   |   |   |   |   |   |   |  3\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   |   |   |   |   |   |   |   |  4\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   |   |   |   |   |   |   |   |  5\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   |   |   |   |   |   |   |   |  6\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "| p | p | p | p | p | p | p | p |  7\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "| r | n | b | k | q | b | n | r |  8\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "  h   g   f   e   d   c   b   a\n";
        String actual = BoardVisualizer.fenToBigAscii(fen, BoardVisualizer.Perspective.BLACK);
        Assertions.assertEquals(expected, actual);

        fen = "rn1q1bnr/p1pkppp1/3pb3/1p5p/P5Q1/N3P3/1PPP1PPP/R1BK1BNR w - - 4 6";
        expected =
                "+---+---+---+---+---+---+---+---+\n" +
                        "| R | N | B |   | K | B |   | R |  1\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "| P | P | P |   | P | P | P |   |  2\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   |   |   | P |   |   |   | N |  3\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   | Q |   |   |   |   |   | P |  4\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "| p |   |   |   |   |   | p |   |  5\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   |   |   | b | p |   |   |   |  6\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   | p | p | p | k | p |   | p |  7\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "| r | n | b |   | q |   | n | r |  8\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "  h   g   f   e   d   c   b   a\n";
        actual = BoardVisualizer.fenToBigAscii(fen, BoardVisualizer.Perspective.BLACK);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testFenFromEitherPerspective() {
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        String expected =
                "+---+---+---+---+---+---+---+---+\n" +
                        "| r | n | b | q | k | b | n | r |  8\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "| p | p | p | p | p | p | p | p |  7\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   |   |   |   |   |   |   |   |  6\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   |   |   |   |   |   |   |   |  5\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   |   |   |   |   |   |   |   |  4\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   |   |   |   |   |   |   |   |  3\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "| P | P | P | P | P | P | P | P |  2\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "| R | N | B | Q | K | B | N | R |  1\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "  a   b   c   d   e   f   g   h\n";
        String actual = BoardVisualizer.fenToBigAscii(fen, BoardVisualizer.Perspective.CURRENT_PLAYER);
        Assertions.assertEquals(expected, actual);

        fen = "q4b1r/2pnppp1/r2pk2n/pp5p/P5P1/N1P1P3/RP1P1PBP/1KB3NR b - - 4 12";
        expected =
                "+---+---+---+---+---+---+---+---+\n" +
                        "| R | N |   |   |   | B | K |   |  1\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "| P | B | P |   | P |   | P | R |  2\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   |   |   | P |   | P |   | N |  3\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   | P |   |   |   |   |   | P |  4\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "| p |   |   |   |   |   | p | p |  5\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "| n |   |   | k | p |   |   | r |  6\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "|   | p | p | p | n | p |   |   |  7\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "| r |   | b |   |   |   |   | q |  8\n" +
                        "+---+---+---+---+---+---+---+---+\n" +
                        "  h   g   f   e   d   c   b   a\n";
        actual = BoardVisualizer.fenToBigAscii(fen, BoardVisualizer.Perspective.CURRENT_PLAYER);
        Assertions.assertEquals(expected, actual);
    }

}
