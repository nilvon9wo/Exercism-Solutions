import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IntArrayCollector {
    private final List<Integer> digits = new ArrayList<>();

    void add(int value) {
        digits.add(value);
    }

    void combine(IntArrayCollector other) {
        digits.addAll(other.digits);
    }

    int[] toReversedArray() {
        Collections.reverse(digits);
        return digits.stream()
                       .mapToInt(Integer::intValue)
                       .toArray();
    }
}
