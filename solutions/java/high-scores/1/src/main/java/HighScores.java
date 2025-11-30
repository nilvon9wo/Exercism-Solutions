import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

record HighScores(List<Integer> highScores) {

	List<Integer> scores() {
		return this.highScores;
	}

	Integer latest() {
		return this.highScores.getLast();
	}

	Integer personalBest() {
		return this.highScores.stream()
				       .max(Integer::compareTo)
				       .orElseThrow();
	}

	List<Integer> personalTopThree() {
		return this.highScores.stream()
				        .sorted(Comparator.reverseOrder())
				        .limit(3)
				        .collect(Collectors.toList());
	}
}
