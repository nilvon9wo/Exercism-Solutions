import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

enum Classification {
	ABUNDANT(-1),
	PERFECT(0),
	DEFICIENT(1);

	private final int compareResult;

	Classification(int compareResult) {
		this.compareResult = compareResult;
	}

	private static final Map<Integer, Classification> COMPARE_RESULT_TO_CLASSIFICATION =
			Stream.of(values())
					.collect(Collectors.toMap(
							classification -> classification.compareResult,
							classification -> classification
					));

	public static Classification fromNumberAndAliquotSum(int number, int aliquotSum) {
		int compareResult = Integer.compare(number, aliquotSum);
		return COMPARE_RESULT_TO_CLASSIFICATION.get(compareResult);
	}
}
