import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class KindergartenGarden {
	private final String[] rows;
	KindergartenGarden(String garden) {
		this.rows = garden.split("\n");
	}

	private static final List<String> DEFAULT_STUDENTS = Arrays.asList(
			"Alice","Bob","Charlie","David","Eve","Fred","Ginny",
			"Harriet","Ileana","Joseph","Kincaid","Larry"
	);

	private Map<String, List<Plant>> plantsByStudents;
	private Map<String, List<Plant>> getPlantsByStudents() {
		if (this.plantsByStudents == null) {
			int studentsCount = this.rows[0].length() / 2;
			this.plantsByStudents = IntStream.range(0, studentsCount)
					                        .boxed()
					                        .collect(this.studentPlantsCollector());
		}

		return this.plantsByStudents;
	}

	private Collector<Integer, ?, Map<String, List<Plant>>> studentPlantsCollector() {
		return Collectors.toMap(
				DEFAULT_STUDENTS::get,
				this::getPlantsForStudentIndex
		);
	}

	private List<Plant> getPlantsForStudentIndex(Integer index) {
		return Arrays.stream(this.rows)
				       .flatMap(row -> this.plantsFromRowForStudentIndex(row, index))
				       .toList();
	}

	private Stream<Plant> plantsFromRowForStudentIndex(String row, Integer index) {
		return Stream.of(
				Plant.fromCode(row.charAt(2 * index)),
				Plant.fromCode(row.charAt(2 * index + 1))
		);
	}

	List<Plant> getPlantsOfStudent(String student) {
		return this.getPlantsByStudents()
				       .getOrDefault(student, Collections.emptyList());
	}
}