package io.github.wolfraam.chessgame.opening;

import io.github.wolfraam.chessgame.ChessGame;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ChessOpeningHelperTest {

    @Test
    void testIoException() {
        final ChessOpeningHelper chessOpeningHelper = new ChessOpeningHelper() {
            @Override
            protected InputStream getOpeningBookCsvInputStream() {
                return new InputStream() {
                    @Override
                    public int read() throws IOException {
                        throw new IOException();
                    }
                };
            }
        };
        Assertions.assertThrows(RuntimeException.class, () ->
                chessOpeningHelper.getChessOpening(new ChessGame()));
    }

}