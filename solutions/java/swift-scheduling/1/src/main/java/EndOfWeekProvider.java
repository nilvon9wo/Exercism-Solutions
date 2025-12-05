import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public final class EndOfWeekProvider implements DateTimeProvider {
    @SuppressWarnings("unused")
    public static final Pattern PATTERN = Pattern.compile("^EOW$", Pattern.CASE_INSENSITIVE);
    public static final int DAYS_PER_WEEK = SchedulingConstants.DAYS_PER_WEEK;

    @Override
    public LocalDateTime translate(LocalDateTime meetingStart, String description) {
        DayOfWeek dayOfWeek = meetingStart.getDayOfWeek();
        return SchedulingConstants.EARLY_WEEK_DAYS.contains(dayOfWeek)
                       ? this.friday(meetingStart, dayOfWeek)
                       : this.sunday(meetingStart, dayOfWeek);
    }

    private LocalDateTime friday(LocalDateTime start, DayOfWeek dayOfWeek) {
        int days = daysUntil(dayOfWeek, DayOfWeek.FRIDAY);
        LocalDate date = start.toLocalDate().plusDays(days);
        return date.atTime(SchedulingConstants.END_OF_BUSINESS_DAY_HOUR, 0);
    }

    private LocalDateTime sunday(LocalDateTime start, DayOfWeek dayOfWeek) {
        int days = daysUntil(dayOfWeek, DayOfWeek.SUNDAY);
        if (days == 0) {
            days = DAYS_PER_WEEK;
        }

        return start.toLocalDate()
                       .plusDays(days)
                       .atTime(SchedulingConstants.EVENING_END_OF_WEEK_HOUR, 0);
    }

    private static int daysUntil(DayOfWeek from, DayOfWeek to) {
        return (to.getValue() - from.getValue() + DAYS_PER_WEEK) % DAYS_PER_WEEK;
    }
}
