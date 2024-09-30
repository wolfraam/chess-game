package io.github.wolfraam.chessgame.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BoardTest {

    @Test
    void testEnPassant() {
        final String testFen = "r1bqkbnr/ppp1pppp/2n5/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 3";
        assertTrue(Board.fromFen(testFen).isEnPassant(Square.E5, Square.D6));
    }

    @Test
    void testFen() {
        final String testFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        assertEquals(testFen, Board.fromFen(testFen).getFen());
    }

    @Test
    void testIllegalFen() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> Board.fromFen("bla"));
    }

    @Test
    void testFixFen() {
        final String fen = "4rr2/2p1n1R1/pq1pkp2/1N6/BpppN1P1/b5B1/8/3K3Q w KQkq - 0 1";
        final String fixedFen = "4rr2/2p1n1R1/pq1pkp2/1N6/BpppN1P1/b5B1/8/3K3Q w - - 0 1";
        assertEquals(fixedFen, Board.fromFen(fen).getInitialFen());
        assertEquals(fixedFen, Board.fromFen(fen).getFen());
    }

    @Test
    void testGetFen() {
        final String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        assertEquals(fen, Board.fromFen(fen).getFen());
        assertEquals(fen, Board.fromFen(fen).getInitialFen());
        assertEquals(fen, Board.fromInitialPosition().getFen());
        assertEquals(fen, Board.fromInitialPosition().getInitialFen());
    }
}