package io.github.wolfraam.chessgame.pgn;

import io.github.wolfraam.chessgame.ChessGame;
import io.github.wolfraam.chessgame.notation.NotationType;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

/**
 * Exports games in PGN format.
 */
public class PGNExporter {
    private boolean firstGame = true;
    private final PrintWriter printWriter;

    public PGNExporter(final OutputStream outputStream) {
        printWriter = new PrintWriter(outputStream);
    }

    public void write(final ChessGame chessGame) {
        if (chessGame.getPGNData().getPGNTagValue(PGNTag.RESULT) == null) {
            throw new IllegalArgumentException("Can't export chess game without PGNTag.RESULT");
        }
        if (firstGame) {
            firstGame = false;
        } else {
            printWriter.println();
        }
        for (final PGNTag pgnTag : chessGame.getAvailablePGNTags()) {
            printWriter.print('[');
            printWriter.print(pgnTag.getTag());
            printWriter.print(" \"");
            final String unescaped = chessGame.getPGNData().getPGNTagValue(pgnTag);
            printWriter.print(escape(unescaped));
            printWriter.println("\"]");
        }
        printWriter.println();
        StringBuilder currentLine = new StringBuilder();
        for (final String moveString : getMoveStrings(chessGame)) {
            final boolean needsLeadingSpace = currentLine.length() != 0;
            if (78 < currentLine.length() + moveString.length() + (needsLeadingSpace ? 1 : 0)) {
                printWriter.println(currentLine);
                currentLine = new StringBuilder();
            }
            if (currentLine.length() != 0) {
                currentLine.append(" ");
            }
            currentLine.append(moveString);
        }
        if (0 < currentLine.length()) {
            printWriter.println(currentLine);
        }

        printWriter.flush();
    }

    private String escape(final String unescaped) {
        if (!unescaped.contains("\"") && !unescaped.contains("\\")) {
            return unescaped;
        }
        final StringBuilder stringBuilder = new StringBuilder();
        for (final char c : unescaped.toCharArray()) {
            if (c == '\\') {
                stringBuilder.append("\\\\");
            } else if (c == '"') {
                stringBuilder.append("\\\"");
            } else {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }

    private List<String> getMoveStrings(final ChessGame chessGame) {
        final LinkedList<String> list = new LinkedList<>();
        int halfMoveCount = 0;
        for (final String move : chessGame.getNotationList(NotationType.SAN)) {
            if (halfMoveCount % 2 == 0) {
                final int fullMoveCount = halfMoveCount / 2 + 1;
                list.add(fullMoveCount + "." + move);
            } else {
                list.add(move);
            }
            halfMoveCount++;
        }
        list.add(chessGame.getPGNData().getPGNTagValue(PGNTag.RESULT));
        return list;
    }

}
