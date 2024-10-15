package io.github.wolfraam.chessgame.opening;

import io.github.wolfraam.chessgame.ChessGame;
import io.github.wolfraam.chessgame.notation.NotationType;
import java.util.List;

/**
 * Determines the chess opening of a chess game.
 */
public class ChessOpeningHelper {

    private static final ChessOpeningElement ROOT = new ChessOpeningData().getRootChessOpeningElement();

    public ChessOpening getChessOpening(final ChessGame chessGame) {
        ChessOpeningElement currentChessOpeningElement = ROOT;
        ChessOpening currentChessOpening = new ChessOpening(null, null, null, false);

        final List<String> sanMoves = chessGame.getNotationList(NotationType.SAN);
        for (final String sanMove : sanMoves) {
            currentChessOpeningElement = currentChessOpeningElement.get(sanMove);
            if (currentChessOpeningElement == null) {
                currentChessOpening = new ChessOpening(currentChessOpening.getEco(),
                        currentChessOpening.getName(),
                        currentChessOpening.getVariation(), true);
                break;
            } else {
                if (currentChessOpeningElement.getEco() != null) {
                    currentChessOpening = new ChessOpening(currentChessOpeningElement.getEco(),
                            currentChessOpeningElement.getName(),
                            currentChessOpeningElement.getVariation(), false);
                }
            }
        }
        return currentChessOpening;
    }
}