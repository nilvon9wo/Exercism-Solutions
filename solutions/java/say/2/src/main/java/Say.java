import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Say {
	private static final String[] BELOW_TWENTY = {
			"zero", "one", "two", "three", "four", "five",
			"six", "seven", "eight", "nine", "ten",
			"eleven", "twelve", "thirteen", "fourteen", "fifteen",
			"sixteen", "seventeen", "eighteen", "nineteen"
	};

	private static final Map<Integer, String> SINGLE_DIGIT_CHUNKS
			= IntStream.range(1, 20)
                        .boxed()
                        .collect(Collectors.toMap(i -> i, i -> BELOW_TWENTY[i]));

	private static final Map<Integer, String> TENS = Map.ofEntries(
			Map.entry(20, "twenty"),
			Map.entry(30, "thirty"),
			Map.entry(40, "forty"),
			Map.entry(50, "fifty"),
			Map.entry(60, "sixty"),
			Map.entry(70, "seventy"),
			Map.entry(80, "eighty"),
			Map.entry(90, "ninety")
	);

	private static final String[] PERIOD_NAMES = {"", "thousand", "million", "billion"};

	public String say(long number) {
		if (number < 0 || number > 999_999_999_999L) {
			throw new IllegalArgumentException("Number out of range");
		}

		return this.streamOverThousandsChunks(number);
	}

	private String streamOverThousandsChunks(long number) {
		return IntStream.range(0, PERIOD_NAMES.length)
				       .mapToObj(i -> chunkToWords(number, i))
				       .filter(Objects::nonNull)
				       .reduce(this::toProperWordOrder)
				       .orElse("zero");
	}

	private String chunkToWords(long number, int periodIndex) {
		double periodBase = Math.pow(1000, periodIndex);
		double periodValue = number / periodBase;
		int chunk = (int) (periodValue % 1000);
		return chunk == 0
				       ? null
				       : this.toWords(chunk, periodIndex);

	}

	private String toWords(int chunk, int periodIndex) {
		String chunkWords = this.convertChunk(chunk);
		String periodLabel = PERIOD_NAMES[periodIndex].isEmpty()
				                     ? ""
				                     : " " + PERIOD_NAMES[periodIndex];
		return chunkWords + periodLabel;
	}

	private String toProperWordOrder(String a, String b) {
		return b + " " + a;
	}

	private String convertChunk(int number) {
		return number >= 100
				       ? this.convertOver100Inclusive(number)
				       : number >= 20
						         ? this.convertOver20Inclusive(number)
						         : SINGLE_DIGIT_CHUNKS.get(number);
	}

	private String convertOver100Inclusive(int number) {
		int hundreds = number / 100;
		int remainder = number % 100;
		String remainderWords = remainder != 0
				                        ? " " + this.convertChunk(remainder)
				                        : "";
		return BELOW_TWENTY[hundreds] + " hundred" + remainderWords;
	}

	private String convertOver20Inclusive(int number) {
		int tensPart = (number / 10) * 10;
		int units = number % 10;
		String unitsWords = units != 0
				                    ? "-" + BELOW_TWENTY[units]
				                    : "";
		return TENS.get(tensPart) + unitsWords;
	}
}