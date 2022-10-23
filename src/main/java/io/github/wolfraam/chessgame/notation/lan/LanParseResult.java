package io.github.wolfraam.chessgame.notation.lan;

import io.github.wolfraam.chessgame.board.PieceType;
import io.github.wolfraam.chessgame.board.Square;

/**
 * The result of parsing a LAN move.
 */
public class LanParseResult {
    public Square fromSquare;
    public boolean isKingSideCastle;
    public boolean isQueenSideCastle;
    public PieceType pieceType;
    public PieceType promotionPiece;
    public Square targetSquare;
}
