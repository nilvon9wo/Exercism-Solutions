import java.time.LocalDateTime;

public final class SwiftScheduling {
    private static final SwiftSchedulingLookup lookup = new SwiftSchedulingLookup();

    private SwiftScheduling() {}

    public static LocalDateTime convertToDeliveryDate(LocalDateTime meetingStart, String description) {
        return lookup.getProvider(description).translate(meetingStart, description);
    }
}
