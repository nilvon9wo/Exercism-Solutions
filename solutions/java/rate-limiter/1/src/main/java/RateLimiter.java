import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class RateLimiter<K> {

	private final int limit;
	private final Duration windowSize;
	private final TimeSource timeSource;

	private final Map<K, Window> windowStateByKey = new HashMap<>();

	public RateLimiter(int limit, Duration windowSize, TimeSource timeSource) {
		this.limit = limit;
		this.windowSize = windowSize;
		this.timeSource = timeSource;
	}

	public boolean allow(K clientId) {
		Instant now = this.timeSource.now();
		Window window = this.windowStateByKey.computeIfAbsent(clientId, k -> new Window(now));
		Instant windowStart = this.computeWindowStart(now);

		if (!window.start.equals(windowStart)) {
			window.start = windowStart;
			window.count = 0;
		}

		if (window.count < limit) {
			window.count++;
			return true;
		}
		else {
			return false;
		}
	}

	private Instant computeWindowStart(Instant now) {
		Duration elapsedDurationSinceEpoch = Duration.between(Instant.EPOCH, now);
		long windowIndex = elapsedDurationSinceEpoch.toNanos()
				                   / windowSize.toNanos();
		Duration windowOffset = windowSize.multipliedBy(windowIndex);
		return Instant.EPOCH.plus(windowOffset);
	}
}
