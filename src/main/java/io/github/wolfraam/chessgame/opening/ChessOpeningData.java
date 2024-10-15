package io.github.wolfraam.chessgame.opening;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Determines the chess opening of a chess game.
 */
public class ChessOpeningData {

    protected static final char SEPARATOR = '|';

    public ChessOpeningElement getRootChessOpeningElement() {
        final ChessOpeningElement chessOpeningElement = new ChessOpeningElement();
        try (final InputStream inputStream = getOpeningBookCsvInputStream()) {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String[] split = line.split("\\" + SEPARATOR);
                final String eco = split[0];
                final String opening = split[1];
                final String variation = split[2].isEmpty() ? null : split[2];
                final String[] sanMoves = split[3].split(" ");
                final List<String> sanList = Arrays.asList(sanMoves);
                addOpeningBookElement(chessOpeningElement, eco, opening, variation, sanList);
            }
            return chessOpeningElement;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected InputStream getOpeningBookCsvInputStream() {
        return ChessOpeningData.class.getResourceAsStream("/io/github/wolfraam/chessgame/opening/opening-book.csv");
    }

    private void addOpeningBookElement(final ChessOpeningElement newRoot, final String eco, final String opening, final String variation, final List<String> sanList) {
        ChessOpeningElement chessOpeningElement = newRoot;
        for (final String move : sanList) {
            ChessOpeningElement chessOpeningElementNew = chessOpeningElement.get(move);
            if (chessOpeningElementNew == null) {
                chessOpeningElementNew = new ChessOpeningElement();
                chessOpeningElement.put(move, chessOpeningElementNew);
            }
            chessOpeningElement = chessOpeningElementNew;
        }
        chessOpeningElement.setEco(eco);
        chessOpeningElement.setName(opening);
        chessOpeningElement.setVariation(variation);
    }
}