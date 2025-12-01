import java.time.LocalDate;
import java.time.LocalDateTime;

public record Gigasecond(LocalDateTime dateTime) {
	private static final long GIGASECOND = 1_000_000_000L;

	public Gigasecond(LocalDateTime dateTime) {
		this.dateTime = dateTime.plusSeconds(GIGASECOND);
	}

	public Gigasecond(LocalDate moment) {
		this(moment.atStartOfDay());
	}

	@SuppressWarnings("unused")
	public LocalDateTime getDateTime() {
		return dateTime;
	}
}
