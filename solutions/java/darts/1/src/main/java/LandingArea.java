import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public enum LandingArea {
	OUTSIDE_TARGET(0, Double.POSITIVE_INFINITY),
	OUTER_CIRCLE(1, 10.0),
	MIDDLE_CIRCLE(5, 5.0),
	BULLS_EYE(10, 1.0);

	private final int points;
	private final double maxRadius;

	LandingArea(int points, double maxRadius) {
		this.points = points;
		this.maxRadius = maxRadius;
	}

	public double getMaxRadius() {
		return maxRadius;
	}

	public int getPoints() {
		return points;
	}

	private static final List<Map.Entry<Double, LandingArea>> SCORING_RANGES =
			Arrays.stream(values())
					.filter(area -> area != OUTSIDE_TARGET)
					.sorted(Comparator.comparingDouble(LandingArea::getMaxRadius))
					.map(area -> Map.entry(area.getMaxRadius(), area))
					.toList();

	public static LandingArea from(double xOfDart, double yOfDart) {
		double radius = Math.hypot(xOfDart, yOfDart);
		return SCORING_RANGES.stream()
				       .filter(entry -> radius <= entry.getKey())
				       .map(Map.Entry::getValue)
				       .findFirst()
				       .orElse(OUTSIDE_TARGET);
	}
}
