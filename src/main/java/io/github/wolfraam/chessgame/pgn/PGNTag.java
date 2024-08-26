package io.github.wolfraam.chessgame.pgn;

/**
 * Supported PGN tags.
 */
public enum PGNTag {
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
    CURRENT_POSITION("CurrentPosition"),
    TIMEZONE("Timezone"),
    ECO_URL("ECOUrl"),
    UTC_DATE("UTCDate"),
    UTC_TIME("UTCTime"),
    START_TIME("StartTime"),
    END_DATE("EndDate"),
    END_TIME("EndTime"),
    LINK("Link"),
    OPENING("Opening"),
    VARIATION("Variation"),
    FEN("FEN"),
    SET_UP("SetUp"),
    VARIANT("Variant"),
    ANNOTATOR("Annotator"),
    DEPTH("Depth");

    private final String tag;

    PGNTag(final String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}

