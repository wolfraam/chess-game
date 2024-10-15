package io.github.wolfraam.chessgame.pgn;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * PGN data belonging to the ChessGame.
 */
public class PGNData implements Serializable {
    private final Map<Integer, List<PGNComment>> pgnMove2CommentAfter = new HashMap<>();
    private final Map<Integer, List<PGNComment>> pgnMove2CommentBefore = new HashMap<>();
    private final Map<PGNTag, String> pgnTag2Value = new EnumMap<>(PGNTag.class);

    /**
     * adds the PGN Comments after the move
     */
    public void addPGNCommentAfter(final int moveIndex, final PGNComment pgnComment) {
        pgnMove2CommentAfter.computeIfAbsent(moveIndex, k -> new LinkedList<>()).add(pgnComment);
    }

    /**
     * adds the PGN Comments after the move
     */
    public void addPGNCommentBefore(final int moveIndex, final PGNComment pgnComment) {
        pgnMove2CommentBefore.computeIfAbsent(moveIndex, k -> new LinkedList<>()).add(pgnComment);
    }

    public void copyFrom(final PGNData pgnData) {
        pgnMove2CommentAfter.putAll(pgnData.pgnMove2CommentAfter);
        pgnMove2CommentBefore.putAll(pgnData.pgnMove2CommentBefore);
        pgnTag2Value.putAll(pgnData.pgnTag2Value);
    }

    public Set<PGNTag> getAvailablePGNTags() {
        return pgnTag2Value.keySet();
    }

    /**
     * @return the PGN Comments after the move
     */
    public List<PGNComment> getPGNCommentsAfter(final int moveIndex) {
        return pgnMove2CommentAfter.getOrDefault(moveIndex, List.of());
    }

    /**
     * @return the PGN Comments before the move
     */
    public List<PGNComment> getPGNCommentsBefore(final int moveIndex) {
        return pgnMove2CommentBefore.getOrDefault(moveIndex, List.of());
    }

    /**
     * @return the value of the PGN tag
     */
    public String getPGNTagValue(final PGNTag pgnTag) {
        return pgnTag2Value.get(pgnTag);
    }

    /**
     * Sets the value of the PGN tag
     */
    public void setPGNTag(final PGNTag pgnTag, final String value) {
        pgnTag2Value.put(pgnTag, value);
    }
}
