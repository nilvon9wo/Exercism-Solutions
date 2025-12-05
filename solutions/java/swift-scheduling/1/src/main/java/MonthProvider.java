import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public final class MonthProvider implements DateTimeProvider {
    @SuppressWarnings("unused")
    public static final Pattern PATTERN = Pattern.compile("^(\\d+)M$", Pattern.CASE_INSENSITIVE);

    @Override
    public LocalDateTime translate(LocalDateTime meetingStart, String description) {
        int targetMonth = Integer.parseInt(description.replaceAll("\\D", ""));
        int targetYear = meetingStart.getYear();
        if (meetingStart.getMonthValue() >= targetMonth) {
            targetYear++;
        }

        LocalDate firstDay = LocalDate.of(targetYear, targetMonth, SchedulingConstants.FIRST_DAY_OF_MONTH);
        return this.getFirstWorkday(firstDay);
    }

    private LocalDateTime getFirstWorkday(LocalDate firstDay) {
        return IntStream.range(0, SchedulingConstants.DAYS_PER_WEEK)
                       .mapToObj(firstDay::plusDays)
                       .filter(d -> SchedulingConstants.WORK_DAYS.contains(d.getDayOfWeek()))
                       .findFirst()
                       .orElseThrow()
                       .atTime(SchedulingConstants.MORNING_WORKDAY_START_HOUR, 0);
    }
}
