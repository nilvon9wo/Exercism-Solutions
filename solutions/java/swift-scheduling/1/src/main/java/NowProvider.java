import java.time.LocalDateTime;
import java.util.regex.Pattern;

public final class NowProvider implements DateTimeProvider {
    @SuppressWarnings("unused")
    public static final Pattern PATTERN = Pattern.compile("^NOW$", Pattern.CASE_INSENSITIVE);

    @Override
    public LocalDateTime translate(LocalDateTime meetingStart, String description) {
        return meetingStart.plusHours(SchedulingConstants.NOW_DELAY_HOURS);
    }
}
