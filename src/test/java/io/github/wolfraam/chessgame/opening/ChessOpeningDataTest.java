package io.github.wolfraam.chessgame.opening;

import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ChessOpeningDataTest {

    @Test
    void testIoException() {
        final ChessOpeningData chessOpeningData = new ChessOpeningData() {
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
        Assertions.assertThrows(RuntimeException.class, chessOpeningData::getRootChessOpeningElement);
    }

}