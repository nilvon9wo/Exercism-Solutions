public enum StatColumn {
    TEAM("Team", FormattingRules.TEAM_COLUMN_WIDTH, Alignment.LEFT),
    MATCHES_PLAYED("MP", FormattingRules.STAT_COLUMN_WIDTH, Alignment.RIGHT),
    WINS("W", FormattingRules.STAT_COLUMN_WIDTH, Alignment.RIGHT),
    DRAWS("D", FormattingRules.STAT_COLUMN_WIDTH, Alignment.RIGHT),
    LOSSES("L", FormattingRules.STAT_COLUMN_WIDTH, Alignment.RIGHT),
    POINTS("P", FormattingRules.STAT_COLUMN_WIDTH, Alignment.RIGHT);

    private final String label;
    private final int width;
    private final Alignment alignment;

    StatColumn(String label, int width, Alignment alignment) {
        this.label = label;
        this.width = width;
        this.alignment = alignment;
    }

    String formatHeaderCell() {
        return this.format(label);
    }

    String formatRowCell(TeamRecord record) {
        try {
            Object value = StatFieldMap.FIELD_BY_COLUMNS.get(this)
                                                        .get(record);
            final String valueString = String.valueOf(value);
            return this.format(valueString);
        }
        catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    boolean isTeamColumn() {
        return this == TEAM;
    }

    String format(String value) {
        String alignmentFlag = this.alignment.getFlag();
        return String.format("%" + alignmentFlag + this.width + "s", value);
    }
}