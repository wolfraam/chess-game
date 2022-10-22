package com.github.chessgame.notation.san;

import com.github.chessgame.board.PieceType;
import com.github.chessgame.board.Square;

/**
 * The result of parsing a SAN move.
 */
public class SanParseResult {
    public String disambiguationFile;
    public String disambiguationRow;
    public boolean isKingSideCastle;
    public boolean isQueenSideCastle;
    public PieceType pieceType;
    public PieceType promotionPiece;
    public Square targetSquare;
}
