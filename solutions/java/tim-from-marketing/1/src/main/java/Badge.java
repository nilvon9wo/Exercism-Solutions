import java.util.ArrayList;
import java.util.List;

class Badge {
	private static final String delimiter = " - ";

	public String print(Integer id, String name, String department) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name must not be null or empty.");
		}

		List<String> parts = new ArrayList<>();
		if (id != null) {
			parts.add("[" + id + "]");
		}

		parts.add(name);

		String role = (department != null && !department.isBlank())
				           ? department.toUpperCase()
				           : "OWNER";
		parts.add(role);
		return String.join(delimiter, parts);
	}
}