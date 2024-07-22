package io.github.wolfraam.chessgame.board.visual;

public class BoardVisualizer {

    public static final Perspective DEFAULT_PERSPECTIVE = Perspective.WHITE;
    private static final String horizontalLine;
    private static final String columnIndexLine;
    private static final String flippedColumnIndexLine;
    private static final String[] lineIndexEndings;
    private static final String emptySquare;
    private static final String lineStart;

    static {
        horizontalLine = "+---+---+---+---+---+---+---+---+\n";
        columnIndexLine = "  a   b   c   d   e   f   g   h\n";
        flippedColumnIndexLine = "  h   g   f   e   d   c    b  a\n";
        lineIndexEndings = new String[]{"  8\n", "  7\n", "  6\n", "  5\n", "  4\n", "  3\n", "  2\n", "  1\n"};
        emptySquare = "   |";
        lineStart = "|";
    }

    public static String fenToBigAscii(String fen) {
        return fenToBigAscii(fen, DEFAULT_PERSPECTIVE);
    }

    public static String fenToBigAscii(String fen, Perspective perspective) {
        String position;

        if (perspective == Perspective.WHITE) {
            position = fen.split(" ")[0];
            return positionToBigAscii(position, 0, 1) + columnIndexLine;
        } else if (perspective == Perspective.BLACK) {
            position = new StringBuilder(fen.split(" ")[0]).reverse().toString();
            return positionToBigAscii(position, 7, -1) + flippedColumnIndexLine;
        } else {
            if (fen.split(" ")[1].equals("w")) {
                position = fen.split(" ")[0];
                return positionToBigAscii(position, 0, 1) + columnIndexLine;
            } else {
                position = new StringBuilder(fen.split(" ")[0]).reverse().toString();
                return positionToBigAscii(position, 7, -1) + flippedColumnIndexLine;
            }
        }
    }

    public static String positionToBigAscii(String position, int lineIndexCounter, int incrementDirection) {

        StringBuilder asciiBoard = new StringBuilder(horizontalLine).append(lineStart);

        for (char each : position.toCharArray()) {
            try {
                int emptySquares = Integer.parseInt(String.valueOf(each));
                for (int i = 0; i < emptySquares; i++) {
                    asciiBoard.append(emptySquare);
                }
            } catch (NumberFormatException e) {
                if (each == '/') {
                    asciiBoard.append(lineIndexEndings[lineIndexCounter]).append(horizontalLine).append(lineStart);
                    lineIndexCounter += incrementDirection;
                } else {
                    asciiBoard.append(" ").append(each).append(" |");
                }
            }
        }
        return asciiBoard.append(lineIndexEndings[lineIndexCounter]).append(horizontalLine).toString();
    }


    public enum Perspective {
        WHITE,
        BLACK,
        CURRENT_PLAYER
    }


}
