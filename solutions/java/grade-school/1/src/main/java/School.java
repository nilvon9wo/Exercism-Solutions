import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class School {

    private final Map<String, Integer> studentsByGrade = new HashMap<>();

    boolean add(String student, int grade) {
        return this.studentsByGrade.putIfAbsent(student, grade) == null;
    }

    List<String> roster() {
        return this.studentsByGrade.entrySet()
                              .stream()
                              .sorted(this.byGradeThenName())
                              .map(Entry::getKey)
                              .toList();
    }

    private Comparator<Entry<String, Integer>> byGradeThenName() {
        return Comparator
                .comparingInt(Entry<String, Integer>::getValue)
                .thenComparing(Entry::getKey);
    }

    List<String> grade(int grade) {
        return this.studentsByGrade.entrySet()
                              .stream()
                              .filter(entry -> this.isInGrade(entry, grade))
                              .map(Entry::getKey)
                              .sorted()
                              .toList();
    }

    private boolean isInGrade(final Entry<String, Integer> entry, final int grade) {
        return entry.getValue() == grade;
    }
}
