import java.util.*;
import java.util.stream.*;

public class PlantHelper {

	private final Map<Character, Plant> CODE_MAP = Map.of(
			'V', Plant.VIOLETS,
			'R', Plant.RADISHES,
			'C', Plant.CLOVER,
			'G', Plant.GRASS
	);

	public Collector<Integer, ?, Map<String, List<Plant>>> studentPlantsCollector(List<String> students, String[] rows) {
		return Collectors.toMap(
				students::get,
				i -> getPlantsForStudentIndex(i, rows)
		);
	}

	private List<Plant> getPlantsForStudentIndex(Integer i, String[] rows) {
		return Arrays.stream(rows)
				       .flatMap(row -> plantsFromRowForStudentIndex(i, row))
				       .toList();
	}

	private Stream<Plant> plantsFromRowForStudentIndex(Integer i, String row) {
		return Stream.of(
				fromCode(row.charAt(2 * i)),
				fromCode(row.charAt(2 * i + 1))
		);
	}

	private Plant fromCode(char code) {
		Plant plant = CODE_MAP.get(code);
		if (plant == null) {
			throw new IllegalArgumentException("Unknown plant code: " + code);
		}

		return plant;
	}
}
