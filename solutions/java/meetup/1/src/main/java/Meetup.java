import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

class Meetup {
    private final Month month;
    private final int year;

    Meetup(int monthOfYear, int year) {
        this.month = Month.of(monthOfYear);
        this.year = year;
    }

    LocalDate day(DayOfWeek dayOfWeek, MeetupSchedule schedule) {
        if (schedule.hasOrdinal()) {
            return this.ordinal(dayOfWeek, schedule.getOrdinal());
        }

        return switch (schedule) {
            case LAST -> last(dayOfWeek);
            case TEENTH -> teenth(dayOfWeek);
            default -> throw new IllegalStateException();
        };
    }

    private LocalDate ordinal(DayOfWeek dayOfWeek, int ordinal) {
        return firstOfMonth()
                .with(TemporalAdjusters.nextOrSame(dayOfWeek))
                .plusWeeks(ordinal - 1L);
    }

    private LocalDate firstOfMonth() {
        return LocalDate.of(year, month, 1);
    }

    private LocalDate last(DayOfWeek dayOfWeek) {
        return lastOfMonth()
                .with(TemporalAdjusters.previousOrSame(dayOfWeek));
    }

    private LocalDate lastOfMonth() {
        return firstOfMonth()
                .with(TemporalAdjusters.lastDayOfMonth());
    }

    private LocalDate teenth(DayOfWeek dayOfWeek) {
        return LocalDate.of(year, month, 13)
                        .with(TemporalAdjusters.nextOrSame(dayOfWeek));
    }
}