import java.time.Instant;

public class Window {
	Instant start;
	int count;

	Window(Instant start, int count) {
		this.start = start;
		this.count = count;
	}

	Window(Instant start) {
		this(start,0);
	}
}