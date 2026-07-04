class Clock {
    private int totalMinutes;

    Clock(int hours, int minutes) {
        this.totalMinutes = this.normalize(hours * 60L + minutes);
    }

    private static final int MINUTES_PER_DAY = 24 * 60;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_MINUTE = 60;

    void add(int minutes) {
        long updated = (long) this.totalMinutes + minutes;
        this.totalMinutes = this.normalize(updated);
    }

    @Override
    public String toString() {
        int hours = this.totalMinutes / MINUTES_PER_HOUR;
        int mins = this.totalMinutes % SECONDS_PER_MINUTE;
        return String.format("%02d:%02d", hours, mins);
    }

    @Override
    public boolean equals(Object object) {
        return this == object
               || object instanceof Clock other
                  && this.totalMinutes == other.totalMinutes;
    }

    private int normalize(long minutes) {
        long mod = minutes % MINUTES_PER_DAY;
        if (mod < 0) {
            mod += MINUTES_PER_DAY;
        }
        return (int) mod;
    }
}