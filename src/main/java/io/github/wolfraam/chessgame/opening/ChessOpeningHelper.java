package io.github.wolfraam.chessgame.opening;

import io.github.wolfraam.chessgame.ChessGame;
import io.github.wolfraam.chessgame.notation.NotationType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Determines the chess opening of a chess game.
 */
public class ChessOpeningHelper {

    private ChessOpeningElement root;

    public ChessOpening getChessOpening(final ChessGame chessGame) {
        if (root == null) {
            fillRoot();
        }
        ChessOpeningElement currentChessOpeningElement = root;
        ChessOpening currentChessOpening = new ChessOpening(null, null, null);

        final List<String> sanMoves = chessGame.getNotationList(NotationType.SAN);
        for (final String sanMove : sanMoves) {
            currentChessOpeningElement = currentChessOpeningElement.get(sanMove);
            if (currentChessOpeningElement == null) {
                break;
            } else {
                if (currentChessOpeningElement.getEco() != null) {
                    currentChessOpening = new ChessOpening(currentChessOpeningElement.getEco(),
                            currentChessOpeningElement.getName(),
                            currentChessOpeningElement.getVariation());
                }
            }
        }
        return currentChessOpening;
    }

    protected InputStream getOpeningBookCsvInputStream() {
        return ChessOpeningHelper.class.getResourceAsStream("/io/github/wolfraam/chessgame/opening/opening-book.csv");
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

    private void fillRoot() {
        final ChessOpeningElement newRoot = new ChessOpeningElement();

        try (final InputStream inputStream = getOpeningBookCsvInputStream()) {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String[] split = line.split(",");
                final String eco = split[0];
                final String opening = split[1];
                final String variation = split[2].length() == 0 ? null : split[2];
                final String[] sanMoves = Arrays.copyOfRange(split, 3, split.length);
                addOpeningBookElement(newRoot, eco, opening, variation, Arrays.asList(sanMoves));
            }
            root = newRoot;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}