package io.github.wolfraam.chessgame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.wolfraam.chessgame.notation.NotationType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class MoveTest {
    private Move parse(final String line) {
        final StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
        final Move move = new Move();
        int i = 0;
        while (stringTokenizer.hasMoreTokens()) {
            final String token = stringTokenizer.nextToken();
            if (i == 0) {
                move.san = token;
            } else if (i == 1) {
                move.fen = token;
            } else {
                move.legalMoves.add(token);
            }
            i++;
        }
        return move;
    }

    private void testFile(final String fileName) {
        try (final InputStream inputStream = MoveTest.class.getResourceAsStream("/" + fileName)) {
            final InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            final ChessGame chessGame = new ChessGame();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final Move move = parse(line);
                assertTrue(chessGame.isLegalMove(chessGame.getMove(NotationType.SAN, move.san)));
                chessGame.playMove(NotationType.SAN, move.san);

                assertEquals(move.fen, chessGame.getFen(), "After move " + move.san);

                final Set<String> legalMoves = chessGame.getLegalMoves().stream().map(move1 -> chessGame.getNotation(NotationType.SAN, move1)).collect(Collectors.toSet());
                assertEquals(move.legalMoves, legalMoves, "After move " + move.san);
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class Move {
        String fen;
        Set<String> legalMoves = new HashSet<>();
        String san;
    }

    @Test
    void testCastling() {
        testFile("castling/testCastlingAllowedKingSide.txt");
        testFile("castling/testCastlingAllowedQueenSide.txt");
        testFile("castling/testCastlingNotAllowedAfterKingMove.txt");
        testFile("castling/testCastlingNotAllowedAfterRookMove1.txt");
        testFile("castling/testCastlingNotAllowedAfterRookMove2.txt");
        testFile("castling/testCastlingNotAllowedCheck.txt");
        testFile("castling/testCastlingNotAllowedCheckAfterwards.txt");
        testFile("castling/testCastlingNotAllowedPassThroughFieldAttacked.txt");
        testFile("castling/testCastlingNotAllowedToGetOutOfMate.txt");
    }

    @Test
    void testCheck() {
        testFile("testCheck.txt");
    }

    @Test
    void testCheckMate() {
        testFile("testCheckMate.txt");
    }

    @Test
    void testDisambiguation() {
        testFile("disambiguation/testDisambiguation1.txt");
        testFile("disambiguation/testDisambiguation2.txt");
        testFile("disambiguation/testDisambiguation3.txt");
    }

    @Test
    void testEnPassant() {
        testFile("enpassant/testEnPassant.txt");
    }

    @Test
    void testPromotion() {
        testFile("promotion/testPromotionBishop.txt");
        testFile("promotion/testPromotionCheck.txt");
        testFile("promotion/testPromotionKnight.txt");
        testFile("promotion/testPromotionRook.txt");
    }

    @Test
    void testStaleMate() {
        testFile("testStaleMate.txt");
    }
}