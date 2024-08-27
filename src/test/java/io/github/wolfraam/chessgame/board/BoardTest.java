package io.github.wolfraam.chessgame.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BoardTest {

    @Test
    void testEnpassant() {
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


}