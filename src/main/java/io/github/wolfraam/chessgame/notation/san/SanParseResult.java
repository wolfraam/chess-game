package io.github.wolfraam.chessgame.notation.san;

import io.github.wolfraam.chessgame.board.PieceType;
import io.github.wolfraam.chessgame.board.Square;

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
