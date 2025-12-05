import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public final class QuarterProvider implements DateTimeProvider {
    @SuppressWarnings("unused")
    public static final Pattern PATTERN = Pattern.compile("^Q(\\d+)$", Pattern.CASE_INSENSITIVE);

    @Override
    public LocalDateTime translate(LocalDateTime meetingStart, String description) {
        int targetQuarter = Integer.parseInt(description.replaceAll("\\D", ""));
        int currentQuarter = (meetingStart.getMonthValue() - 1)
                                     / SchedulingConstants.MONTHS_PER_QUARTER + 1;

        int targetYear = meetingStart.getYear();
        if (currentQuarter > targetQuarter) targetYear++;

        int lastMonthOfQuarter = targetQuarter * SchedulingConstants.MONTHS_PER_QUARTER;
        LocalDate lastDayOfQuarter = LocalDate.of(targetYear, lastMonthOfQuarter,
                YearMonth.of(targetYear, lastMonthOfQuarter).lengthOfMonth());

        return this.getLastWorkdayOfQuarter(lastDayOfQuarter);
    }

    private LocalDateTime getLastWorkdayOfQuarter(LocalDate lastDayOfQuarter) {
        return IntStream.range(0, SchedulingConstants.DAYS_PER_WEEK)
                       .mapToObj(lastDayOfQuarter::minusDays)
                       .filter(date -> SchedulingConstants.WORK_DAYS.contains(date.getDayOfWeek()))
                       .findFirst()
                       .orElseThrow()
                       .atTime(SchedulingConstants.MORNING_WORKDAY_START_HOUR, 0);
    }
}
