import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class YachtDiceCounter {
    private final int[] diceValues;
    private final Map<Integer, Long> faceValueCounts;

    public YachtDiceCounter(int[] diceValues) {
        this.diceValues = Arrays.copyOf(diceValues, diceValues.length);
        this.faceValueCounts = this.buildCounts();
    }

    Map<Integer, Long> getFaceValueCounts() {
        return this.faceValueCounts;
    }

    private Map<Integer, Long> buildCounts() {
        return Arrays.stream(this.diceValues)
                       .boxed()
                       .collect(this.faceValueCountCollector());
    }

    private Collector<Integer, ?, Map<Integer, Long>> faceValueCountCollector() {
        return Collectors.groupingBy(
                dieValue -> dieValue,
                Collectors.counting()
        );
    }
}