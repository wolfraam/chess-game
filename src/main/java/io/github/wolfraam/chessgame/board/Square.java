package io.github.wolfraam.chessgame.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Squares on the chess board.
 */
public enum Square {

    A1("a1", 0, 0),
    A2("a2", 0, 1),
    A3("a3", 0, 2),
    A4("a4", 0, 3),
    A5("a5", 0, 4),
    A6("a6", 0, 5),
    A7("a7", 0, 6),
    A8("a8", 0, 7),
    B1("b1", 1, 0),
    B2("b2", 1, 1),
    B3("b3", 1, 2),
    B4("b4", 1, 3),
    B5("b5", 1, 4),
    B6("b6", 1, 5),
    B7("b7", 1, 6),
    B8("b8", 1, 7),
    C1("c1", 2, 0),
    C2("c2", 2, 1),
    C3("c3", 2, 2),
    C4("c4", 2, 3),
    C5("c5", 2, 4),
    C6("c6", 2, 5),
    C7("c7", 2, 6),
    C8("c8", 2, 7),
    D1("d1", 3, 0),
    D2("d2", 3, 1),
    D3("d3", 3, 2),
    D4("d4", 3, 3),
    D5("d5", 3, 4),
    D6("d6", 3, 5),
    D7("d7", 3, 6),
    D8("d8", 3, 7),
    E1("e1", 4, 0),
    E2("e2", 4, 1),
    E3("e3", 4, 2),
    E4("e4", 4, 3),
    E5("e5", 4, 4),
    E6("e6", 4, 5),
    E7("e7", 4, 6),
    E8("e8", 4, 7),
    F1("f1", 5, 0),
    F2("f2", 5, 1),
    F3("f3", 5, 2),
    F4("f4", 5, 3),
    F5("f5", 5, 4),
    F6("f6", 5, 5),
    F7("f7", 5, 6),
    F8("f8", 5, 7),
    G1("g1", 6, 0),
    G2("g2", 6, 1),
    G3("g3", 6, 2),
    G4("g4", 6, 3),
    G5("g5", 6, 4),
    G6("g6", 6, 5),
    G7("g7", 6, 6),
    G8("g8", 6, 7),
    H1("h1", 7, 0),
    H2("h2", 7, 1),
    H3("h3", 7, 2),
    H4("h4", 7, 3),
    H5("h5", 7, 4),
    H6("h6", 7, 5),
    H7("h7", 7, 6),
    H8("h8", 7, 7);

    public static final List<Square> FEN_SQUARE_LIST = new ArrayList<>(64);
    private static final Map<String, Square> NAME_2_SQUARE = new HashMap<>();
    private static final List<List<Square>> SQUARE_LIST_LIST = new ArrayList<>(8);

    static {
        List<Square> currentList = null;
        for (final Square square : values()) {
            if (currentList == null) {
                currentList = new ArrayList<>(8);
            }
            currentList.add(square);
            if (currentList.size() == 8) {
                SQUARE_LIST_LIST.add(currentList);
                currentList = null;
            }
            NAME_2_SQUARE.put(square.name, square);
        }
        for (int y = 7; y >= 0; y--) {
            for (int x = 0; x < 8; x++) {
                FEN_SQUARE_LIST.add(Square.fromCoordinates(x, y));
            }
        }
    }

    public static Square fromCoordinates(final int x, final int y) {
        return SQUARE_LIST_LIST.get(x).get(y);
    }

    public static Square fromName(final String name) {
        return NAME_2_SQUARE.get(name);
    }

    public final String file;
    public final String name;
    public final String row;
    public final SquareColor squareColor;
    public final int x;
    public final int y;

    Square(final String name, final int x, final int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        file = name.substring(0, 1);
        row = name.substring(1);
        squareColor = ((x + y) % 2 == 0) ? SquareColor.DARK : SquareColor.LIGHT;
    }
}