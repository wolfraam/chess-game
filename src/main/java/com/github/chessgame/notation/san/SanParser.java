package com.github.chessgame.notation.san;

import com.github.chessgame.board.Board;
import com.github.chessgame.board.Piece;
import com.github.chessgame.board.PieceType;
import com.github.chessgame.board.Side;
import com.github.chessgame.board.Square;
import com.github.chessgame.move.IllegalMoveException;
import com.github.chessgame.move.Move;
import com.github.chessgame.move.MoveHelper;
import com.github.chessgame.notation.NotationMapping;
import com.github.chessgame.util.RemoveIfSupport;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses SAN moves.
 */
public class SanParser {

    public Move convertToMove(final Board board, final NotationMapping notationMapping, final String moveNotation) {
        final SanParseResult sanParseResult = parse(notationMapping, moveNotation);

        final Square targetSquare;
        if (sanParseResult.isKingSideCastle) {
            targetSquare = board.getSideToMove() == Side.WHITE ? Square.G1 : Square.G8;
        } else if (sanParseResult.isQueenSideCastle) {
            targetSquare = board.getSideToMove() == Side.WHITE ? Square.C1 : Square.C8;
        } else {
            targetSquare = sanParseResult.targetSquare;
        }

        final Piece piece = Piece.fromPieceTypeAndSide(sanParseResult.pieceType, board.getSideToMove());
        final Set<Move> legalMoves = new MoveHelper(board).getLegalMoves(piece, targetSquare);

        if (legalMoves.isEmpty()) {
            throw new IllegalMoveException("Move " + moveNotation + " is illegal");
        }

        if (sanParseResult.promotionPiece != null) {
            RemoveIfSupport.run(legalMoves, move -> move.promotion != sanParseResult.promotionPiece);
        }

        if (legalMoves.size() == 1) {
            return legalMoves.iterator().next();
        }

        if (sanParseResult.disambiguationFile != null) {
            RemoveIfSupport.run(legalMoves, move -> !sanParseResult.disambiguationFile.equals(move.from.file));
        }
        if (sanParseResult.disambiguationRow != null) {
            RemoveIfSupport.run(legalMoves, move -> !sanParseResult.disambiguationRow.equals(move.from.row));
        }

        if (legalMoves.size() != 1) {
            throw new IllegalMoveException("Move " + moveNotation + " is illegal");
        }
        return legalMoves.iterator().next();
    }

    private String getPieceString(final NotationMapping notationMapping, final String san) {
        for (final PieceType pieceType : PieceType.NON_PAWNS) {
            if (san.startsWith(notationMapping.getNotation(pieceType))) {
                return notationMapping.getNotation(pieceType);
            }
        }
        return null;
    }

    private String getPromotion(final NotationMapping notationMapping, final String san) {
        if (san.contains("=")) {
            for (final PieceType pieceType : PieceType.NON_PAWNS) {
                if (san.endsWith("=" + notationMapping.getNotation(pieceType))) {
                    return notationMapping.getNotation(pieceType);
                }
            }
        }
        return null;
    }

    private SanParseResult parse(final NotationMapping notationMapping, final String originalSan) {
        final SanParseResult sanParseResult = new SanParseResult();
        if (originalSan.length() == 2) {
            sanParseResult.targetSquare = Square.fromName(originalSan);
            if (sanParseResult.targetSquare == null) {
                throw new IllegalMoveException("Move " + originalSan + " is illegal");
            }
            sanParseResult.pieceType = PieceType.PAWN;
        } else {
            String san = originalSan;
            if (san.endsWith("+") || san.endsWith("#")) {
                san = san.substring(0, san.length() - 1);
            }
            if (san.equals("O-O")) {
                sanParseResult.pieceType = PieceType.KING;
                sanParseResult.isKingSideCastle = true;
                return sanParseResult;
            }
            if (san.equals("O-O-O")) {
                sanParseResult.pieceType = PieceType.KING;
                sanParseResult.isQueenSideCastle = true;
                return sanParseResult;
            }

            final String pieceString = getPieceString(notationMapping, san);
            if (pieceString != null) {
                san = san.substring(pieceString.length());
            }
            final String promotion = getPromotion(notationMapping, san);
            if (promotion != null) {
                san = san.substring(0, san.length() - promotion.length() - 1);
            }

            final Matcher matcher = Pattern.compile("([a-h])?([1-8])?([x])?([a-h][1-8])").matcher(san);
            if (!matcher.matches()) {
                throw new IllegalMoveException("Move " + originalSan + " is illegal");
            }

            sanParseResult.pieceType = pieceString == null ? PieceType.PAWN : notationMapping.getPieceType(pieceString);
            sanParseResult.disambiguationFile = matcher.group(1);
            sanParseResult.disambiguationRow = matcher.group(2);

            final String targetSquareString = matcher.group(4);
            sanParseResult.targetSquare = Square.fromName(targetSquareString);

            if (promotion != null) {
                sanParseResult.promotionPiece = notationMapping.getPieceType(promotion);
            }
        }
        return sanParseResult;
    }
}
