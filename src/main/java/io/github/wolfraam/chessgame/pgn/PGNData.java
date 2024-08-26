package io.github.wolfraam.chessgame.pgn;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PGN data belonging to the ChessGame.
 */
public class PGNData {
    private final Map<Integer, List<PGNComment>> pgnMove2CommentAfter = new HashMap<>();
    private final Map<Integer, List<PGNComment>> pgnMove2CommentBefore = new HashMap<>();
    private final Map<PGNTag, String> pgnTag2Value = new EnumMap<>(PGNTag.class);

    public void copyFrom(final PGNData pgnData) {
        pgnMove2CommentAfter.putAll(pgnData.pgnMove2CommentAfter);
        pgnMove2CommentBefore.putAll(pgnData.pgnMove2CommentBefore);
        pgnTag2Value.putAll(pgnData.pgnTag2Value);
    }

    /**
     * @return a Map from the move index to the comments after that move.
     */
    public Map<Integer, List<PGNComment>> getPGNMove2CommentAfter() {
        return pgnMove2CommentAfter;
    }

    /**
     * @return a Map from the move index to the comments before that move.
     */
    public Map<Integer, List<PGNComment>> getPGNMove2CommentBefore() {
        return pgnMove2CommentBefore;
    }

    /**
     * @return a Map from the tag to its value.
     */
    public Map<PGNTag, String> getPGNTag2Value() {
        return pgnTag2Value;
    }

    public String getPGNTagValue(final PGNTag pgnTag) {
        return pgnTag2Value.get(pgnTag);
    }

    public void setPGNTag(final PGNTag pgnTag, final String value) {
        pgnTag2Value.put(pgnTag, value);
    }
}
