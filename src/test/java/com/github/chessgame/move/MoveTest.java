package com.github.chessgame.move;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.chessgame.board.PieceType;
import com.github.chessgame.board.Square;
import org.junit.jupiter.api.Test;

class MoveTest {

    @Test
    public void testEquals() {
        assertEquals(new Move(Square.A1, Square.A2, PieceType.KNIGHT), new Move(Square.A1, Square.A2, PieceType.KNIGHT));
        assertEquals(new Move(Square.A1, Square.A2), new Move(Square.A1, Square.A2));

        final Move m = new Move(Square.A1, Square.A2);
        assertTrue(m.equals(m));
    }

    @Test
    public void testNotEquals() {
        assertFalse(new Move(Square.A1, Square.A2).equals(null));
        assertFalse(new Move(Square.A1, Square.A2).equals("A"));
        assertNotEquals(new Move(Square.A1, Square.A2, PieceType.KNIGHT), new Move(Square.A1, Square.A2, PieceType.BISHOP));
        assertNotEquals(new Move(Square.A1, Square.A2, PieceType.KNIGHT), new Move(Square.A1, Square.A2));
        assertNotEquals(new Move(Square.A1, Square.A2), new Move(Square.A1, Square.A2, PieceType.BISHOP));
        assertNotEquals(new Move(Square.A1, Square.A2), new Move(Square.A1, Square.A3));
    }


}