import java.util.Map;
import java.util.Optional;

public class ScheduleOrdinalHelper {
    private static Map<MeetupSchedule, Integer> ORDINAL_BY_SCHEDULE
            = Map.of(
                    MeetupSchedule.FIRST, 1,
                    MeetupSchedule.SECOND, 2,
                    MeetupSchedule.THIRD, 3,
                    MeetupSchedule.FOURTH, 4
    );

    static Optional<Integer> get(MeetupSchedule schedule) {
        return Optional.ofNullable(ORDINAL_BY_SCHEDULE.get(schedule));
    }
}
