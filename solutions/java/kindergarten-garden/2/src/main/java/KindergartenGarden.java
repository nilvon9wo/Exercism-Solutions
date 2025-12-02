import java.util.*;
import java.util.stream.IntStream;

public class KindergartenGarden {
	private final String[] rows;
	private final PlantHelper plantHelper;
	KindergartenGarden(String garden, PlantHelper plantHelper) {
		this.rows = garden.split("\n");
		this.plantHelper = plantHelper;
	}

	KindergartenGarden(String garden) {
		this(garden, new PlantHelper());
	}

	private static final List<String> DEFAULT_STUDENTS = Arrays.asList(
			"Alice","Bob","Charlie","David","Eve","Fred","Ginny",
			"Harriet","Ileana","Joseph","Kincaid","Larry"
	);

	private Map<String, List<Plant>> plantsByStudents;
	private Map<String, List<Plant>> getPlantsByStudents() {
		if (this.plantsByStudents == null) {
			int studentCount = this.rows[0].length() / 2;
			plantsByStudents = IntStream.range(0, studentCount)
					                .boxed()
					                .collect(this.plantHelper.studentPlantsCollector(DEFAULT_STUDENTS, rows));		}

		return this.plantsByStudents;
	}

	List<Plant> getPlantsOfStudent(String student) {
		return this.getPlantsByStudents()
				       .getOrDefault(student, Collections.emptyList());
	}
}


