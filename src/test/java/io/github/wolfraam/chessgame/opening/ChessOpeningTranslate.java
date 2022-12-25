package io.github.wolfraam.chessgame.opening;

import io.github.wolfraam.chessgame.ChessGame;
import io.github.wolfraam.chessgame.notation.NotationType;
import io.github.wolfraam.chessgame.pgn.PGNImporter;
import io.github.wolfraam.chessgame.pgn.PgnTag;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static io.github.wolfraam.chessgame.opening.ChessOpeningHelper.SEPARATOR;

public class ChessOpeningTranslate {

    public static void main(final String[] args) {
        new ChessOpeningTranslate().run();
    }

    private void run() {
        try (final InputStream inputStream = Files.newInputStream(
                Paths.get("./src/test/resources/io/github/wolfraam/chessgame/opening/opening-book.pgn"))) {
            try (final OutputStream outputStream = Files.newOutputStream(
                    Paths.get("./src/main/resources/io/github/wolfraam/chessgame/opening/opening-book.csv"))) {

                final PrintWriter printWriter = new PrintWriter(outputStream);

                final PGNImporter pgnImporter = new PGNImporter();
                pgnImporter.setOnGame(chessGame -> writeGame(chessGame, printWriter));
                pgnImporter.run(inputStream);
                printWriter.flush();
            }

        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeGame(final ChessGame chessGame, final PrintWriter printWriter) {
        final List<String> sanList = chessGame.getNotationList(NotationType.SAN);
        final String eco = chessGame.getPgnTagValue(PgnTag.ECO);
        final String opening = chessGame.getPgnTagValue(PgnTag.OPENING);
        final String variation = chessGame.getPgnTagValue(PgnTag.VARIATION);
        if (eco.contains(String.valueOf(SEPARATOR))) {
            throw new RuntimeException(eco);
        }
        printWriter.print(eco);
        printWriter.print(SEPARATOR);
        if (opening != null) {
            if (opening.contains(String.valueOf(SEPARATOR))) {
                throw new RuntimeException(opening);
            }
            printWriter.print(opening);
        }
        printWriter.print(SEPARATOR);
        if (variation != null) {
            if (variation.contains(String.valueOf(SEPARATOR))) {
                throw new RuntimeException(variation);
            }
            printWriter.print(variation);
        }
        printWriter.print(SEPARATOR);
        for (final String san : sanList) {
            printWriter.print(san);
            printWriter.print(' ');
        }
        printWriter.println();
    }
}