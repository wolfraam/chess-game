package io.github.wolfraam.chessgame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.wolfraam.chessgame.board.Piece;
import io.github.wolfraam.chessgame.board.Square;
import io.github.wolfraam.chessgame.move.Move;
import io.github.wolfraam.chessgame.notation.NotationType;
import io.github.wolfraam.chessgame.opening.ChessOpening;
import io.github.wolfraam.chessgame.pgn.PGNTag;
import io.github.wolfraam.chessgame.result.ChessGameResultType;
import io.github.wolfraam.chessgame.result.DrawType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class ChessGameTest {
    @Test
    void testClone() {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, "e4 e6 d4 d5 Nc3 Nf6 Bg5 dxe4 Nxe4 Be7 Bxf6 gxf6 g3 f5 Nc3 Bf6 Nge2 Nc6 d5 exd5 Nxd5 Bxb2 Bg2 O-O O-O Bh8 Nef4 Ne5 Qh5 Ng6 Rad1 c6 Ne3 Qf6 Kh1 Bg7 Bh3 Ne7 Rd3 Be6 Rfd1 Bh6 Rd4 Bxf4 Rxf4 Rad8 Rxd8 Rxd8 Bxf5 Nxf5 Nxf5 Rd5 g4 Bxf5 gxf5 h6 h3 Kh7 Qe2 Qe5 Qh5 Qf6 Qe2 Re5 Qd3 Rd5");
        final ChessGame chessGame2 = chessGame.clone();
        assertEquals(chessGame2.getFen(), chessGame.getFen());
        assertEquals(chessGame2.getMoves(), chessGame.getMoves());
        assertEquals(chessGame2.getPGNData().getAvailablePGNTags(), chessGame.getPGNData().getAvailablePGNTags());
    }

    @Test
    void testSerialize() throws IOException, ClassNotFoundException {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, "e4 e6");
        chessGame.getPGNData().setPGNTag(PGNTag.SITE, "Site");

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(chessGame);
        objectOutputStream.flush();
        byteArrayOutputStream.flush();

        final ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));

        final Object o = objectInputStream.readObject();
        final ChessGame chessGame2 = (ChessGame) o;

        assertEquals(chessGame2.getFen(), chessGame.getFen());
        assertEquals(chessGame2.getMoves(), chessGame.getMoves());
        assertEquals(chessGame2.getPGNData().getAvailablePGNTags(), chessGame.getPGNData().getAvailablePGNTags());
    }

    @Test
    void testFiftyMoveRule1() {
        final ChessGame chessGame = new ChessGame("K7/8/7k/8/Bb6/8/8/8 w - - 100 1");
        assertEquals(ChessGameResultType.DRAW, chessGame.getGameResultType());
        assertEquals(DrawType.FIFTY_MOVE_RULE, chessGame.getGameResult().drawType);
    }

    @Test
    void testFiftyMoveRule2() {
        final ChessGame chessGame = new ChessGame("K7/8/7k/8/Bb6/8/8/8 w - - 99 1");
        assertNull(chessGame.getGameResult());
        assertNull(chessGame.getGameResultType());
        chessGame.playMoves(NotationType.SAN, "Bb3");
        assertEquals(ChessGameResultType.DRAW, chessGame.getGameResultType());
        assertEquals(DrawType.FIFTY_MOVE_RULE, chessGame.getGameResult().drawType);
    }

    @Test
    void testGetASCII() {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, "e4 e6");
        assertEquals("""
                rnbqkbnr
                pppp_ppp
                ____p___
                ________
                ____P___
                ________
                PPPP_PPP
                RNBQKBNR
                """, chessGame.getASCII());
    }

    @Test
    void testGetCapturedPieces() {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, "e4 c5 Nf3 d6 d4 cxd4");
        assertTrue(chessGame.getCapturedPieces().contains(Piece.WHITE_PAWN));
    }

    @Test
    void testGetCapturedPiecesWithInitialFen() {
        final ChessGame chessGame = new ChessGame("rn1q1rk1/pp2bppp/5n2/3p4/2pP2b1/2N1PN1P/PPB2PP1/R1BQ1RK1 w KQkq - 0 1");
        chessGame.playMoves(NotationType.SAN, "hxg4");
        assertTrue(chessGame.getCapturedPieces().contains(Piece.BLACK_BISHOP));
    }

    @Test
    void testGetFen() {
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", new ChessGame().getFen());
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", new ChessGame().getInitialFen());
    }

    @Test
    void testGetFenSmall() {
        assertEquals("rnbqkbnrpppppppp32PPPPPPPPRNBQKBNRw", new ChessGame().getFenSmall());
    }

    @Test
    void testGetGameResult() {
        final ChessGame chessGame = new ChessGame();
        assertNull(chessGame.getGameResult());
        assertNull(chessGame.getGameResultType());
    }

    @Test
    void testGetGameResultBlackWins() {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, "e4 e5 Nc3 Bc5 Nf3 Qh4 Bb5");
        assertNull(chessGame.getGameResult());
        assertNull(chessGame.getGameResultType());
        chessGame.playMoves(NotationType.SAN, "Qxf2#");
        assertEquals(ChessGameResultType.BLACK_WINS, chessGame.getGameResultType());
        assertNull(chessGame.getGameResult().drawType);
    }

    @Test
    void testGetGameResultDrawStaleMate() {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, "e3 a5 Qh5 Ra6 Qxa5 h5 h4 Rah6 Qxc7 f6 Qxd7+ Kf7 Qxb7 Qd3 Qxb8 Qh7 Qxc8 Kg6");
        assertNull(chessGame.getGameResult());
        assertNull(chessGame.getGameResultType());
        chessGame.playMoves(NotationType.SAN, "Qe6");
        assertEquals(ChessGameResultType.DRAW, chessGame.getGameResultType());
        assertEquals(DrawType.STALE_MATE, chessGame.getGameResult().drawType);
    }

    @Test
    void testGetGameResultDrawThreeFoldRepetition() {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, "e4 e6 d4 d5 Nc3 Nf6 Bg5 dxe4 Nxe4 Be7 Bxf6 gxf6 g3 f5 Nc3 Bf6 Nge2 Nc6 d5 exd5 Nxd5 Bxb2 Bg2 O-O O-O Bh8 Nef4 Ne5 Qh5 Ng6 Rad1 c6 Ne3 Qf6 Kh1 Bg7 Bh3 Ne7 Rd3 Be6 Rfd1 Bh6 Rd4 Bxf4 Rxf4 Rad8 Rxd8 Rxd8 Bxf5 Nxf5 Nxf5 Rd5 g4 Bxf5 gxf5 h6 h3 Kh7 Qe2 Qe5 Qh5 Qf6 Qe2 Re5 Qd3 Rd5");
        assertNull(chessGame.getGameResult());
        assertNull(chessGame.getGameResultType());
        chessGame.playMoves(NotationType.SAN, "Qe2");
        assertEquals(ChessGameResultType.DRAW, chessGame.getGameResultType());
        assertEquals(DrawType.THREEFOLD_REPETITION, chessGame.getGameResult().drawType);
    }

    @Test
    void testGetGameResultWhiteWins() {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, "e4 f6 Nc3 g5");
        assertNull(chessGame.getGameResult());
        assertNull(chessGame.getGameResultType());
        chessGame.playMoves(NotationType.SAN, "Qh5#");
        assertEquals(ChessGameResultType.WHITE_WINS, chessGame.getGameResultType());
        assertNull(chessGame.getGameResult().drawType);
    }

    @Test
    void testGetLastMove() {
        final ChessGame chessGame = new ChessGame();
        assertNull(chessGame.getLastMove());
        chessGame.playMoves(NotationType.SAN, "e4 c5 Nf3");
        assertEquals(new Move(Square.G1, Square.F3), chessGame.getLastMove());
    }

    @Test
    void testGetLegalMoves() {
        final ChessGame chessGame = new ChessGame();
        assertEquals("Na3,Nc3,Nf3,Nh3,a3,a4,b3,b4,c3,c4,d3,d4,e3,e4,f3,f4,g3,g4,h3,h4", chessGame.getLegalMoves().stream().map(move -> chessGame.getNotation(NotationType.SAN, move)).sorted().collect(Collectors.joining(",")));
    }

    @Test
    void testGetLegalMovesForSquare() {
        final ChessGame chessGame = new ChessGame();
        assertEquals("Na3,Nc3", chessGame.getLegalMoves(Square.B1).stream().map(move -> chessGame.getNotation(NotationType.SAN, move)).sorted().collect(Collectors.joining(",")));
    }

    @Test
    void testGetNotationList() {
        final ChessGame chessGame = new ChessGame("4rr2/2p1n1R1/pq1pkp2/1N6/BpppN1P1/b5B1/8/3K3Q w KQkq - 0 1");
        chessGame.playMoves(NotationType.SAN, "Nc5+ dxc5 Nxd4+ cxd4 Qe4#");
        assertEquals(List.of("Ne4-c5+", "d6xc5", "Nb5xd4+", "c5xd4", "Qh1-e4#"), chessGame.getNotationList(NotationType.LAN));
    }

    @Test
    void testGetOccupiedSquares() {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, "e4");
        assertTrue(chessGame.getOccupiedSquares().contains(Square.A1));
        assertFalse(chessGame.getOccupiedSquares().contains(Square.E2));
    }

    @Test
    void testGetPiece() {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, "e4");
        assertEquals(Piece.WHITE_ROOK, chessGame.getPiece(Square.A1));
        assertNull(chessGame.getPiece(Square.E2));
    }

    @Test
    void testGetScore() {
        final ChessGame chessGame = new ChessGame();
        assertEquals(0, chessGame.getScore());
        chessGame.playMoves(NotationType.SAN, "e4 c5 Nf3 d6 d4 cxd4");
        assertEquals(-1, chessGame.getScore());
    }

    @Test
    void testGetSquares() {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, "e4");
        assertTrue(chessGame.getSquares(Piece.WHITE_PAWN).contains(Square.A2));
        assertFalse(chessGame.getSquares(Piece.WHITE_PAWN).contains(Square.E2));
    }

    @Test
    void testGetSquaresAttackingKing() {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, "e4 f6 Qh5+");
        assertEquals(Collections.singleton(Square.H5), chessGame.getSquaresAttackingKing());
    }

    @Test
    void testGetSubset() {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, "e4 c5 Nf3 g5");
        final ChessGame chessGame2 = chessGame.getSubset(3);
        assertEquals(new Move(Square.G1, Square.F3), chessGame2.getLastMove());
    }

    @Test
    void testGetSubset2() {
        final ChessGame chessGame = new ChessGame("4rr2/2p1n1R1/pq1pkp2/1N6/BpppN1P1/b5B1/8/3K3Q w KQkq - 0 1");
        chessGame.playMoves(NotationType.SAN, "Nc5+ dxc5 Nxd4+ cxd4 Qe4#");
        final ChessGame chessGame2 = chessGame.getSubset(3);
        assertEquals(new Move(Square.B5, Square.D4), chessGame2.getLastMove());
    }

    @Test
    void testIsKingAttacked() {
        final ChessGame chessGame = new ChessGame();
        assertFalse(chessGame.isKingAttacked());
        chessGame.playMoves(NotationType.SAN, "e4 f6 Qh5+");
        assertTrue(chessGame.isKingAttacked());
    }

    @Test
    void testOpeningBenko() {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, "g3 e5 a3 h6");
        final ChessOpening chessOpening = chessGame.getChessOpening();
        assertEquals("A00", chessOpening.getEco());
        assertEquals("Benko's opening", chessOpening.getName());
        assertNull(chessOpening.getVariation());
        assertEquals("Benko's opening (A00)", chessOpening.getFullName());
    }

    @Test
    void testOpeningNajdorf() {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, "e4 c5 Nf3 d6 d4 cxd4 Nxd4 Nf6 Nc3 a6");
        final ChessOpening chessOpening = chessGame.getChessOpening();
        assertEquals("B90", chessOpening.getEco());
        assertEquals("Sicilian", chessOpening.getName());
        assertEquals("Najdorf", chessOpening.getVariation());
        assertEquals("Sicilian / Najdorf (B90)", chessOpening.getFullName());
    }

    @Test
    void testOpeningNoMoves() {
        final ChessGame chessGame = new ChessGame();
        final ChessOpening chessOpening = chessGame.getChessOpening();
        assertNull(chessOpening.getEco());
        assertNull(chessOpening.getName());
        assertNull(chessOpening.getVariation());
        assertNull(chessOpening.getFullName());
    }

    @Test
    void testOpeningQueensGambit() {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, "d4 d5 c4");
        final ChessOpening chessOpening = chessGame.getChessOpening();
        assertEquals("D06", chessOpening.getEco());
        assertEquals("Queen's Gambit", chessOpening.getName());
        assertNull(chessOpening.getVariation());
        assertEquals("Queen's Gambit (D06)", chessOpening.getFullName());
    }

    @Test
    void testEndOfVariant() {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, "b4");
        assertEquals("Polish (Sokolsky) opening (A00)", chessGame.getChessOpening().getFullName());
        assertFalse(chessGame.getChessOpening().isEndOfVariation());

        chessGame.playMoves(NotationType.SAN, "Nh6");
        assertEquals("Polish / Tuebingen variation (A00)", chessGame.getChessOpening().getFullName());
        assertFalse(chessGame.getChessOpening().isEndOfVariation());

        chessGame.playMoves(NotationType.SAN, "e4");
        assertEquals("Polish / Tuebingen variation (A00)", chessGame.getChessOpening().getFullName());
        assertTrue(chessGame.getChessOpening().isEndOfVariation());

        chessGame.playMoves(NotationType.SAN, "e6");
        assertEquals("Polish / Tuebingen variation (A00)", chessGame.getChessOpening().getFullName());
        assertTrue(chessGame.getChessOpening().isEndOfVariation());
    }

    @Test
    void testPlayMoves() {
        final ChessGame chessGame = new ChessGame();
        chessGame.playMoves(NotationType.SAN, Arrays.asList("e4", "e5"));
        assertEquals(new Move(Square.E7, Square.E5), chessGame.getLastMove());
    }
}