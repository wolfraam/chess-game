package com.github.chessgame.notation.lan;

import com.github.chessgame.board.PieceType;
import com.github.chessgame.board.Square;

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
