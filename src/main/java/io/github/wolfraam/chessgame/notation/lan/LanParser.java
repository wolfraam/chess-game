package io.github.wolfraam.chessgame.notation.lan;

import io.github.wolfraam.chessgame.board.Board;
import io.github.wolfraam.chessgame.board.PieceType;
import io.github.wolfraam.chessgame.board.Side;
import io.github.wolfraam.chessgame.board.Square;
import io.github.wolfraam.chessgame.move.IllegalMoveException;
import io.github.wolfraam.chessgame.move.Move;
import io.github.wolfraam.chessgame.move.MoveHelper;
import io.github.wolfraam.chessgame.notation.NotationMapping;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses LAN moves.
 */
public class LanParser {

    public Move convertToMove(final Board board, final NotationMapping notationMapping, final String lan) {
        final LanParseResult lanParseResult = parse(notationMapping, lan);

        final Square fromSquare;
        final Square targetSquare;
        if (lanParseResult.isKingSideCastle) {
            fromSquare = board.getSideToMove() == Side.WHITE ? Square.E1 : Square.E8;
            targetSquare = board.getSideToMove() == Side.WHITE ? Square.G1 : Square.G8;
        } else if (lanParseResult.isQueenSideCastle) {
            fromSquare = board.getSideToMove() == Side.WHITE ? Square.E1 : Square.E8;
            targetSquare = board.getSideToMove() == Side.WHITE ? Square.C1 : Square.C8;
        } else {
            fromSquare = lanParseResult.fromSquare;
            targetSquare = lanParseResult.targetSquare;
        }

        final Move move = new Move(fromSquare, targetSquare, lanParseResult.promotionPiece);

        if (!new MoveHelper(board).isLegalMove(move)) {
            throw new IllegalMoveException("Move " + lan + " is illegal");
        }

        if (board.getPiece(move.from).pieceType != lanParseResult.pieceType) {
            throw new IllegalMoveException("Move " + lan + " is illegal");
        }

        return move;
    }

    public LanParseResult parse(final NotationMapping notationMapping, final String originalLan) {
        final LanParseResult lanParseResult = new LanParseResult();
        String lan = originalLan;
        if (lan.endsWith("+") || lan.endsWith("#")) {
            lan = lan.substring(0, lan.length() - 1);
        }
        if (lan.equals("O-O")) {
            lanParseResult.pieceType = PieceType.KING;
            lanParseResult.isKingSideCastle = true;
            return lanParseResult;
        }
        if (lan.equals("O-O-O")) {
            lanParseResult.pieceType = PieceType.KING;
            lanParseResult.isQueenSideCastle = true;
            return lanParseResult;
        }

        final String pieceString = getPieceString(notationMapping, lan);
        if (pieceString != null) {
            lan = lan.substring(pieceString.length());
        }
        final String promotion = getPromotion(notationMapping, lan);
        if (promotion != null) {
            lan = lan.substring(0, lan.length() - promotion.length() - 1);
        }

        final Matcher matcher = Pattern.compile("([a-h][1-8])([-x])([a-h][1-8])").matcher(lan);
        if (!matcher.matches()) {
            throw new IllegalMoveException("Move " + originalLan + " is illegal");
        }

        lanParseResult.pieceType = pieceString == null ? PieceType.PAWN : notationMapping.getPieceType(pieceString);
        final String fromSquareString = matcher.group(1);
        lanParseResult.fromSquare = Square.fromName(fromSquareString);

        final String targetSquareString = matcher.group(3);
        lanParseResult.targetSquare = Square.fromName(targetSquareString);

        if (promotion != null) {
            lanParseResult.promotionPiece = notationMapping.getPieceType(promotion);
        }

        return lanParseResult;
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
}
