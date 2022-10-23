package io.github.wolfraam.chessgame.move;

/**
 * Throws when an illegal move is attempted.
 */
public class IllegalMoveException extends RuntimeException {
    public IllegalMoveException(final String message) {
        super(message);
    }
}
