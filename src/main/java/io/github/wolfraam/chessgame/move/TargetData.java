package io.github.wolfraam.chessgame.move;

import io.github.wolfraam.chessgame.board.Piece;
import io.github.wolfraam.chessgame.board.PieceType;
import io.github.wolfraam.chessgame.board.Side;
import io.github.wolfraam.chessgame.board.Square;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Contains all possible targets for a piece.
 */
public class TargetData {
    private final EnumMap<Square, Map<PieceType, Set<Square>>> kingAndKnightMap = new EnumMap<>(Square.class);

    private final EnumMap<Square, Map<PieceType, List<List<Square>>>> otherMap = new EnumMap<>(Square.class);

    private final EnumMap<Square, Map<Side, Set<Square>>> pawnMap = new EnumMap<>(Square.class);

    public Set<Square> getKingOrKnightTargets(final Square square, final Piece piece) {
        return kingAndKnightMap.get(square).get(piece.pieceType);
    }

    public List<List<Square>> getOtherTargets(final Square square, final Piece piece) {
        return otherMap.get(square).get(piece.pieceType);
    }

    public Set<Square> getPawnTargets(final Square square, final Piece piece) {
        return pawnMap.get(square).get(piece.side);
    }

    public TargetData init() {
        for (final Square square : Square.values()) {
            for (final PieceType pieceType : PieceType.values()) {
                switch (pieceType) {
                    case PAWN:
                        if (0 < square.y && square.y < 7) {
                            for (final Side side : Side.values()) {
                                final Set<Square> squareSet = EnumSet.noneOf(Square.class);
                                final int newY = square.y + (side == Side.WHITE ? 1 : -1);
                                addTarget(squareSet, square.x, newY);
                                addTarget(squareSet, square.x + 1, newY);
                                addTarget(squareSet, square.x - 1, newY);
                                if (square.y == (side == Side.WHITE ? 1 : 6)) {
                                    addTarget(squareSet, square.x, square.y + (side == Side.WHITE ? 2 : -2));
                                }
                                pawnMap.computeIfAbsent(square, k -> (new EnumMap<>(Side.class))).put(side, squareSet);
                            }
                        }
                        break;
                    case KNIGHT:
                    case KING:
                        final Set<Square> squareSet = EnumSet.noneOf(Square.class);
                        addKingOrKnightTargets(pieceType, square, squareSet);
                        kingAndKnightMap.computeIfAbsent(square, k -> (new EnumMap<>(PieceType.class))).put(pieceType, squareSet);
                        break;
                    case BISHOP:
                    case ROOK:
                    case QUEEN:
                        final List<List<Square>> squareListList = new LinkedList<>();
                        addOtherTargets(pieceType, square, squareListList);
                        otherMap.computeIfAbsent(square, k -> (new EnumMap<>(PieceType.class))).put(pieceType, squareListList);
                        break;
                }
            }
        }
        return this;
    }

    private void addKingOrKnightTargets(final PieceType pieceType, final Square square, final Set<Square> squareSet) {
        if (pieceType == PieceType.KNIGHT) {
            addTarget(squareSet, square.x + 2, square.y + 1);
            addTarget(squareSet, square.x + 2, square.y - 1);
            addTarget(squareSet, square.x + 1, square.y + 2);
            addTarget(squareSet, square.x + 1, square.y - 2);
            addTarget(squareSet, square.x - 1, square.y + 2);
            addTarget(squareSet, square.x - 1, square.y - 2);
            addTarget(squareSet, square.x - 2, square.y + 1);
            addTarget(squareSet, square.x - 2, square.y - 1);
        } else if (pieceType == PieceType.KING) {
            addTarget(squareSet, square.x + 1, square.y);
            addTarget(squareSet, square.x - 1, square.y);
            addTarget(squareSet, square.x, square.y + 1);
            addTarget(squareSet, square.x, square.y - 1);
            addTarget(squareSet, square.x + 1, square.y + 1);
            addTarget(squareSet, square.x - 1, square.y + 1);
            addTarget(squareSet, square.x + 1, square.y - 1);
            addTarget(squareSet, square.x - 1, square.y - 1);

            if (square == Square.E1 || square == Square.E8) {
                addTarget(squareSet, square.x + 2, square.y);
                addTarget(squareSet, square.x - 2, square.y);
            }
        }
    }

    private void addOtherTargets(final PieceType pieceType, final Square square, final List<List<Square>> squareListList) {
        if (pieceType == PieceType.BISHOP || pieceType == PieceType.QUEEN) {
            squareListList.add(addTargetLine(square, 1, 1));
            squareListList.add(addTargetLine(square, -1, 1));
            squareListList.add(addTargetLine(square, -1, -1));
            squareListList.add(addTargetLine(square, 1, -1));
        }
        if (pieceType == PieceType.ROOK || pieceType == PieceType.QUEEN) {
            squareListList.add(addTargetLine(square, 1, 0));
            squareListList.add(addTargetLine(square, -1, 0));
            squareListList.add(addTargetLine(square, 0, 1));
            squareListList.add(addTargetLine(square, 0, -1));
        }
    }

    private void addTarget(final Set<Square> targets, final int x, final int y) {
        if (0 <= x && x < 8 && 0 <= y && y < 8) {
            targets.add(Square.fromCoordinates(x, y));
        }
    }

    private List<Square> addTargetLine(final Square square, final int xIncrease, final int yIncrease) {
        final List<Square> list = new LinkedList<>();
        for (int x = square.x + xIncrease, y = square.y + yIncrease;
             0 <= x && x < 8 && 0 <= y && y < 8;
             x += xIncrease, y += yIncrease) {
            list.add(Square.fromCoordinates(x, y));
        }
        return list;
    }
}
