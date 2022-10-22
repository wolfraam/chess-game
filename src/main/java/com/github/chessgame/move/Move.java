package com.github.chessgame.move;

import com.github.chessgame.board.PieceType;
import com.github.chessgame.board.Square;
import java.io.Serializable;
import java.util.Objects;

/**
 * A move for a chess player.
 */
public class Move implements Serializable {
    public final Square from;
    public final PieceType promotion;
    public final Square to;

    public Move(final Square from, final Square to) {
        this(from, to, null);
    }

    public Move(final Square from, final Square to, final PieceType promotion) {
        this.from = from;
        this.to = to;
        this.promotion = promotion;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Move move = (Move) o;
        return from == move.from && promotion == move.promotion && to == move.to;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, promotion, to);
    }

    @Override
    public String toString() {
        return from.name + "-" + to.name + ((promotion == null ? "" : (" " + promotion.name())));
    }
}
