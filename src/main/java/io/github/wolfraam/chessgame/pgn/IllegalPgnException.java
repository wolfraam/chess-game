package io.github.wolfraam.chessgame.pgn;

/**
 * Thrown when in illegal PGN is imported.
 */
public class IllegalPgnException extends RuntimeException {
    public IllegalPgnException(final String message) {
        super(message);
    }
}
