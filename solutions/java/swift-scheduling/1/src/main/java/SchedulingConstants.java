import java.time.DayOfWeek;
import java.util.Set;

public final class SchedulingConstants {
    private SchedulingConstants() {}

    // Business hours
    public static final int MORNING_WORKDAY_START_HOUR = 8;
    public static final int END_OF_BUSINESS_DAY_HOUR = 17;
    public static final int AFTERNOON_THRESHOLD_HOUR = 13;
    public static final int EVENING_END_OF_WEEK_HOUR = 20;

    // NOW delay
    public static final int NOW_DELAY_HOURS = 2;

    // Calendar constants
    public static final int DAYS_PER_WEEK = 7;
    public static final int MONTHS_PER_QUARTER = 3;

    // Date offsets
    public static final int FIRST_DAY_OF_MONTH = 1;
    public static final int NEXT_DAY = 1;

    // Day groups
    public static final Set<DayOfWeek> EARLY_WEEK_DAYS = Set.of(
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY);

    public static final Set<DayOfWeek> WEEKEND_DAYS = Set.of(
            DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    public static final Set<DayOfWeek> WORK_DAYS = Set.of(
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);
}
