enum MeetupSchedule {
    FIRST(1),
    SECOND(2),
    THIRD(3),
    FOURTH(4),
    LAST(null),
    TEENTH(null);

    MeetupSchedule(Integer ordinal) {
        this.ordinal = ordinal;
    }

    private final Integer ordinal;
    int getOrdinal() {
        return this.ordinal;
    }

    boolean hasOrdinal() {
        return this.ordinal != null;
    }
}