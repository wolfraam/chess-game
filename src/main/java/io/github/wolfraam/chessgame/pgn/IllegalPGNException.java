package io.github.wolfraam.chessgame.pgn;

/**
 * Thrown when in illegal PGN is imported.
 */
public class IllegalPGNException extends RuntimeException {
    public IllegalPGNException(final String message) {
        super(message);
    }
}
