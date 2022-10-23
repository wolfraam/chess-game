package io.github.wolfraam.chessgame.pgn;

/**
 * Supported PGN tags.
 */
public enum PgnTag {
    EVENT("Event"),
    EVENT_DATE("EventDate"),
    EVENT_TYPE("EventType"),
    EVENT_ROUNDS("EventRounds"),
    EVENT_COUNTRY("EventCountry"),
    EVENT_CATEGORY("EventCategory"),
    SITE("Site"),
    DATE("Date"),
    ROUND("Round"),
    BOARD("Board"),
    WHITE("White"),
    BLACK("Black"),
    RESULT("Result"),
    BLACK_TITLE("BlackTitle"),
    BLACK_ELO("BlackElo"),
    BLACK_TEAM("BlackTeam"),
    BLACK_TEAM_COUNTRY("BlackTeamCountry"),
    BLACK_FIDE_ID("BlackFideId"),
    WHITE_TITLE("WhiteTitle"),
    WHITE_ELO("WhiteElo"),
    WHITE_TEAM("WhiteTeam"),
    WHITE_TEAM_COUNTRY("WhiteTeamCountry"),
    WHITE_FIDE_ID("WhiteFideId"),
    ECO("ECO"),
    TIME_CONTROL("TimeControl"),
    TERMINATION("Termination"),
    OPENING("Opening"),
    VARIATION("Variation"),
    FEN("FEN"),
    SET_UP("SetUp"),
    VARIANT("Variant"),
    ANNOTATOR("Annotator"),
    DEPTH("Depth");

    private final String tag;

    PgnTag(final String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}