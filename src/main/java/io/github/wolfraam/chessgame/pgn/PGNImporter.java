package io.github.wolfraam.chessgame.pgn;

import io.github.wolfraam.chessgame.ChessGame;
import io.github.wolfraam.chessgame.move.IllegalMoveException;
import io.github.wolfraam.chessgame.notation.NotationType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Imports games in PGN format.
 */
public class PGNImporter {
    private Predicate<Map<PGNTag, String>> acceptTagsPredicate = pgnTagStringMap -> true;
    private ChessGame chessGame;
    private Function<String, ChessGame> fen2NewChessGameFunction = ChessGame::new;
    private File file;
    private boolean inMoveLines = false;
    private int indentLevel = 0;
    private final List<String> lines = new LinkedList<>();
    private Consumer<String> onError;
    private Consumer<ChessGame> onGame;
    private Consumer<String> onWarning;
    private final Map<PGNTag, String> pgnTag2Value = new EnumMap<>(PGNTag.class);

    /**
     * Imports the game from the InputStream.
     */
    public void run(final InputStream inputStream) {
        file = null;
        runWithInputStream(inputStream);
    }

    /**
     * Imports the game from the File.
     */
    public void run(final File file) throws FileNotFoundException {
        this.file = file;
        final InputStream inputStream = new FileInputStream(file);
        runWithInputStream(inputStream);
    }

    /**
     * Sets a Predicate which determines whether the game should be imported, based on the supplied tags.
     */
    public void setAcceptTagsPredicate(final Predicate<Map<PGNTag, String>> acceptTagsPredicate) {
        this.acceptTagsPredicate = acceptTagsPredicate;
    }

    /**
     * Sets a Function which generates a ChessGame with the given FEN String. Use this when you want to use
     * a subclass of ChessGame.
     */
    public void setFen2NewChessGameFunction(final Function<String, ChessGame> fen2NewChessGameFunction) {
        this.fen2NewChessGameFunction = fen2NewChessGameFunction;
    }

    /**
     * Sets a Consumer which will be called with import errors.
     */
    public void setOnError(final Consumer<String> onError) {
        this.onError = onError;
    }

    /**
     * Sets a Consumer which will be called with the imported ChessGames.
     */
    public void setOnGame(final Consumer<ChessGame> onGame) {
        this.onGame = onGame;
    }

    /**
     * Sets a Consumer which will be called with import warnings.
     */
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

        final StringBuilder stringBuilderComment = new StringBuilder();
        boolean isBeforeMove = true;
        final StringTokenizer stringTokenizer = new StringTokenizer(line, " \t(){}", true);
        while (stringTokenizer.hasMoreTokens()) {
            final String token = stringTokenizer.nextToken();
            switch (token) {
                case " ":
                case "\t":
                    if (indentLevel != 0) {
                        stringBuilderComment.append(token);
                    }
                    break;
                case "(":
                case "{":
                    if (indentLevel != 0) {
                        stringBuilderComment.append(token);
                    }
                    indentLevel++;
                    break;
                case ")":
                case "}":
                    indentLevel--;
                    if (indentLevel != 0) {
                        stringBuilderComment.append(token);
                    }
                    if (indentLevel == 0) {
                        if (!stringBuilderComment.isEmpty()) {
                            final Map<Integer, List<PGNComment>> map;
                            if (isBeforeMove) {
                                map = chessGame.getPGNData().getPGNMove2CommentBefore();
                            } else {
                                map = chessGame.getPGNData().getPGNMove2CommentAfter();
                            }
                            final PGNComment pgnComment;
                            if (token.equals(")")) {
                                pgnComment = new PGNVariation(stringBuilderComment.toString());
                            } else {
                                pgnComment = new PGNComment(stringBuilderComment.toString());
                            }
                            map.computeIfAbsent(chessGame.getMoves().size() - (isBeforeMove ? 0 : 1), k -> new LinkedList<>())
                                    .add(pgnComment);
                            stringBuilderComment.setLength(0);
                        }
                    }
                    break;
                default:
                    if (indentLevel == 0) {
                        if (token.indexOf('.') != -1) {
                            final String beforeDot = token.substring(0, token.indexOf('.'));
                            final String afterDot = token.substring(token.lastIndexOf('.') + 1);
                            final int processedNumber = Integer.parseInt(beforeDot);
                            if (processedNumber != chessGame.getFullMoveCount()) {
                                throw new IllegalPGNException("Invalid move number " + beforeDot + " in line: " + line);
                            }
                            isBeforeMove = true;
                            if (!afterDot.isEmpty()) {
                                chessGame.playMove(NotationType.SAN, afterDot);
                                isBeforeMove = false;
                            }
                        } else {
                            chessGame.playMove(NotationType.SAN, token);
                            isBeforeMove = false;
                        }
                    } else {
                        stringBuilderComment.append(token);
                    }
                    break;
            }
        }
    }

    private void reset() {
        chessGame = null;
        inMoveLines = false;
        indentLevel = 0;
        pgnTag2Value.clear();
        lines.clear();
    }

    private void runWithInputStream(final InputStream inputStream) {
        reset();
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int lineNumber = 1;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    if (line.startsWith("[")) {
                        if (inMoveLines) {
                            onError.accept(getContext(lineNumber) + "Error: Previous Game did not end properly");
                            reset();
                        }
                        final PGNTagAndValue pgnTagAndValue = PGNTagAndValue.fromLine(line);
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
                            lines.add(line);
                            if (isLastLine(line)) {
                                String fen = pgnTag2Value.get(PGNTag.FEN);
                                if (fen == null) {
                                    fen = ChessGame.STANDARD_INITIAL_FEN;
                                }
                                if (acceptTagsPredicate.test(pgnTag2Value)) {
                                    chessGame = fen2NewChessGameFunction.apply(fen);
                                    for (final Map.Entry<PGNTag, String> entry : pgnTag2Value.entrySet()) {
                                        chessGame.getPGNData().setPGNTag(entry.getKey(), entry.getValue());
                                    }
                                    try {
                                        playMoves(String.join(" ", lines));
                                        onGame.accept(chessGame);
                                    } catch (final IllegalPGNException | IllegalMoveException e) {
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
