package com.github.chessgame.pgn;

import com.github.chessgame.ChessGame;
import com.github.chessgame.move.IllegalMoveException;
import com.github.chessgame.notation.NotationType;
import com.github.chessgame.util.Consumer;
import com.github.chessgame.util.Supplier;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * Imports games in PGN format.
 */
public class PGNImporter {
    private ChessGame chessGame;
    private File file;
    private int inCommentLevel = 0;
    private boolean inMoveLines = false;
    private int inVariationLevel = 0;
    private final Map<Integer, String> lineNr2Line = new TreeMap<>();
    private Supplier<ChessGame> newGameSupplier = ChessGame::new;
    private Consumer<String> onError;
    private Consumer<ChessGame> onGame;
    private Consumer<String> onWarning;
    private final Map<PgnTag, String> pgnTag2Value = new EnumMap<>(PgnTag.class);

    public void run(final InputStream inputStream) {
        file = null;
        runWithInputStream(inputStream);
    }

    public void run(final File file) throws FileNotFoundException {
        this.file = file;
        final InputStream inputStream = new FileInputStream(file);
        runWithInputStream(inputStream);
    }

    public void setNewGameSupplier(final Supplier<ChessGame> newGameSupplier) {
        this.newGameSupplier = newGameSupplier;
    }

    public void setOnError(final Consumer<String> onError) {
        this.onError = onError;
    }

    public void setOnGame(final Consumer<ChessGame> onGame) {
        this.onGame = onGame;
    }

    public void setOnWarning(final Consumer<String> onWarning) {
        this.onWarning = onWarning;
    }

    private String getContext(final int lineNumber) {
        return (file == null ? "" : "File: " + file.getName() + " ") + "Line:" + lineNumber + " ";
    }

    private boolean isLastLine(final String line) {
        return line.endsWith("1/2-1/2") || line.endsWith("1-0") || line.endsWith("0-1") || line.endsWith("*");
    }

    private void playMoves(String line) {
        if (line.endsWith("1/2-1/2")) {
            line = line.substring(0, line.indexOf("1/2-1/2"));
        } else if (line.endsWith("1-0")) {
            line = line.substring(0, line.indexOf("1-0"));
        } else if (line.endsWith("0-1")) {
            line = line.substring(0, line.indexOf("0-1"));
        } else if (line.endsWith("*")) {
            line = line.substring(0, line.indexOf("*"));
        }

        final StringTokenizer stringTokenizer = new StringTokenizer(line, " \t(){}", true);
        while (stringTokenizer.hasMoreTokens()) {
            final String token = stringTokenizer.nextToken();
            switch (token) {
                case " ":
                    break;
                case "(":
                    inVariationLevel++;
                    break;
                case ")":
                    inVariationLevel--;
                    break;
                case "{":
                    inCommentLevel++;
                    break;
                case "}":
                    inCommentLevel--;
                    break;
                default:
                    if (inCommentLevel == 0 && inVariationLevel == 0) {
                        if (token.indexOf('.') != -1) {
                            final String beforeDot = token.substring(0, token.indexOf('.'));
                            final String afterDot = token.substring(token.lastIndexOf('.') + 1);
                            if (Integer.parseInt(beforeDot) != chessGame.getFullMoveCount()) {
                                throw new IllegalPgnException("Invalid move number " + beforeDot + " in line: " + line);
                            }
                            if (0 < afterDot.length()) {
                                chessGame.playMove(NotationType.SAN, afterDot);
                            }
                        } else {
                            chessGame.playMove(NotationType.SAN, token);
                        }
                    }
                    break;
            }
        }
    }

    private void reset() {
        chessGame = null;
        inMoveLines = false;
        inCommentLevel = 0;
        inVariationLevel = 0;
        pgnTag2Value.clear();
        lineNr2Line.clear();
    }

    private void runWithInputStream(final InputStream inputStream) {
        reset();
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int lineNumber = 1;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.length() != 0) {
                    if (line.startsWith("[")) {
                        if (inMoveLines) {
                            onError.accept(getContext(lineNumber) + "Error: Previous Game did not end properly");
                            reset();
                        }
                        final PgnTagAndValue pgnTagAndValue = PgnTagAndValue.fromLine(line);
                        if (pgnTagAndValue != null) {
                            pgnTag2Value.put(pgnTagAndValue.pgnTag, pgnTagAndValue.value);
                        } else {
                            onWarning.accept(getContext(lineNumber) + "Unknown Tag, line: " + line);
                        }
                    } else {
                        inMoveLines = true;
                        if (pgnTag2Value.isEmpty()) {
                            onError.accept(getContext(lineNumber) + "Expected PGN Tags");
                            reset();
                        } else {
                            lineNr2Line.put(lineNumber, line);
                            if (isLastLine(line)) {
                                if ((!"1".equals(pgnTag2Value.get(PgnTag.SET_UP))) &&
                                        pgnTag2Value.get(PgnTag.FEN) == null) {
                                    chessGame = newGameSupplier.get();
                                    for (final Map.Entry<PgnTag, String> entry : pgnTag2Value.entrySet()) {
                                        chessGame.setPgnTag(entry.getKey(), entry.getValue());
                                    }
                                    try {
                                        for (final String l : lineNr2Line.values()) {
                                            playMoves(l);
                                        }
                                        onGame.accept(chessGame);
                                    } catch (final IllegalPgnException | IllegalMoveException e) {
                                        onError.accept(getContext(lineNumber) + e.getMessage());
                                    }
                                }
                                reset();
                            }
                        }
                    }
                }
                lineNumber++;
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
