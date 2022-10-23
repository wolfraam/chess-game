package io.github.wolfraam.chessgame.opening;

import io.github.wolfraam.chessgame.ChessGame;
import io.github.wolfraam.chessgame.notation.NotationType;
import io.github.wolfraam.chessgame.pgn.PGNImporter;
import io.github.wolfraam.chessgame.pgn.PgnTag;
import java.io.InputStream;
import java.util.List;

/**
 * Determines the chess opening of a chess game.
 */
public class ChessOpeningHelper {
    private static final ChessOpeningElement root = new ChessOpeningElement();

    static {
        final InputStream inputStream = ChessOpeningHelper.class.getResourceAsStream(
            "/io/github/wolfraam/chessgame/opening/opening-book.pgn");
        final PGNImporter pgnImporter = new PGNImporter();
        pgnImporter.setOnGame(ChessOpeningHelper::addOpeningBookElement);
        pgnImporter.run(inputStream);
    }

    private static void addOpeningBookElement(final ChessGame chessGame) {
        ChessOpeningElement chessOpeningElement = root;
        for (final String move : chessGame.getNotationList(NotationType.SAN)) {
            ChessOpeningElement chessOpeningElementNew = chessOpeningElement.get(move);
            if (chessOpeningElementNew == null) {
                chessOpeningElementNew = new ChessOpeningElement();
                chessOpeningElement.put(move, chessOpeningElementNew);
            }
            chessOpeningElement = chessOpeningElementNew;
        }
        chessOpeningElement.setEco(chessGame.getPgnTagValue(PgnTag.ECO));
        chessOpeningElement.setName(chessGame.getPgnTagValue(PgnTag.OPENING));
        chessOpeningElement.setVariation(chessGame.getPgnTagValue(PgnTag.VARIATION));
    }

    public ChessOpening getChessOpening(final ChessGame chessGame) {
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
}