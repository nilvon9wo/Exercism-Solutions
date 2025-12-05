import java.time.LocalDateTime;

public interface DateTimeProvider {
    LocalDateTime translate(LocalDateTime meetingStart, String description);
}
