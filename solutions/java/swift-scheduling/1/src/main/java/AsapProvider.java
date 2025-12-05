import java.time.LocalDateTime;
import java.util.regex.Pattern;

public final class AsapProvider implements DateTimeProvider {
    @SuppressWarnings("unused")
    public static final Pattern PATTERN = Pattern.compile("^ASAP$", Pattern.CASE_INSENSITIVE);

    @Override
    public LocalDateTime translate(LocalDateTime meetingStart, String description) {
        return meetingStart.getHour() < SchedulingConstants.AFTERNOON_THRESHOLD_HOUR
                       ? this.endOfBusinessDay(meetingStart)
                       : this.nextAfternoon(meetingStart);
    }

    private LocalDateTime endOfBusinessDay(LocalDateTime start) {
        return start.withHour(SchedulingConstants.END_OF_BUSINESS_DAY_HOUR)
                       .withMinute(0)
                       .withSecond(0)
                       .withNano(0);
    }

    private LocalDateTime nextAfternoon(LocalDateTime start) {
        return start.plusDays(SchedulingConstants.NEXT_DAY)
                       .toLocalDate()
                       .atTime(SchedulingConstants.AFTERNOON_THRESHOLD_HOUR, 0);
    }
}
