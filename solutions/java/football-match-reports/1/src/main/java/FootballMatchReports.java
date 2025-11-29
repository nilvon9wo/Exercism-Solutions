import java.util.Objects;

public class FootballMatchReports {
	private static final RangeMap<Integer, String> playerDescriptionByShirtRange =
			RangeMap.of(
					Ranges.number(1).to("goalie"),
					Ranges.number(2).to("left back"),
					Ranges.numberRange(3, 4).to("center back"),
					Ranges.number(5).to("right back"),
					Ranges.numberRange(6, 8).to("midfielder"),
					Ranges.number(9).to("left wing"),
					Ranges.number(10).to("striker"),
					Ranges.number(11).to("right wing")
			);

    public static String onField(int shirtNum) {
	    String position = playerDescriptionByShirtRange.get(shirtNum);
	    return Objects.requireNonNullElse(position, "invalid");
    }
}
