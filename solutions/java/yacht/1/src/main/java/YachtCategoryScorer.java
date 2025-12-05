import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public record YachtCategoryScorer(int[] diceValues, YachtDiceCounter yachtDiceCounter) {

    private static final long THREE_OF_A_KIND_COUNT = 3L;
    private static final long PAIR_COUNT = 2L;
    private static final int FOUR_OF_A_KIND_COUNT = 4;
    private static final int STRAIGHT_LENGTH = 5;

    public static final Long DEFAULT_FACE_COUNT = 0L;

    public YachtCategoryScorer(int[] diceValues, YachtDiceCounter yachtDiceCounter) {
        this.diceValues = Arrays.copyOf(diceValues, diceValues.length);
        this.yachtDiceCounter = yachtDiceCounter;
    }

    public YachtCategoryScorer(int[] diceValues) {
        this(diceValues, new YachtDiceCounter(diceValues));
    }

    int sumDice() {
        return Arrays.stream(this.diceValues).sum();
    }

    int scoreNumber(int targetNumber) {
        Long faceCount = this.yachtDiceCounter.getFaceValueCounts()
                                 .getOrDefault(targetNumber, DEFAULT_FACE_COUNT);
        long totalScoreForTarget = faceCount * targetNumber;
        return (int) totalScoreForTarget;
    }

    int scoreYacht(int yachtScore) {
        return this.yachtDiceCounter.getFaceValueCounts().size() == 1
                       ? yachtScore
                       : 0;
    }

    int scoreFullHouse() {
        Map<Integer, Long> faceValueCounts = this.yachtDiceCounter.getFaceValueCounts();
        boolean hasThreeOfAKind = faceValueCounts.containsValue(THREE_OF_A_KIND_COUNT);
        boolean hasPair = faceValueCounts.containsValue(PAIR_COUNT);
        return (hasThreeOfAKind && hasPair)
                       ? this.sumDice()
                       : 0;
    }

    int scoreFourOfAKind() {
        return this.yachtDiceCounter.getFaceValueCounts().entrySet().stream()
                       .filter(entry -> entry.getValue() >= FOUR_OF_A_KIND_COUNT)
                       .mapToInt(entry -> entry.getKey() * FOUR_OF_A_KIND_COUNT)
                       .findFirst()
                       .orElse(0);
    }

    int scoreStraight(int straightStart, int straightScore) {
        return this.isStraight(straightStart)
                       ? straightScore
                       : 0;
    }

    private boolean isStraight(int straightStart) {
        List<Integer> expectedStraightSequence = IntStream.range(straightStart, straightStart + STRAIGHT_LENGTH)
                                                         .boxed()
                                                         .toList();
        return Arrays.stream(this.diceValues)
                       .sorted()
                       .boxed()
                       .toList()
                       .equals(expectedStraightSequence);
    }
}
