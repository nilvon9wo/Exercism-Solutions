import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

class Knapsack {
    int maximumValue(int maximumWeight, List<Item> items) {
        int[] initialBestValues = new int[maximumWeight + 1];
        int[] finalBestValues = this.computeBestValuesForAllItems(maximumWeight, items, initialBestValues);
        return finalBestValues[maximumWeight];
    }

    private int[] computeBestValuesForAllItems(
            int maximumWeight,
            List<Item> items,
            int[] initialBestValues
    ) {
        return items.stream()
                       .reduce(
                               initialBestValues,
                               this.updateBestValuesForItem(maximumWeight),
                               this.pickBestValues()
                       );
    }

    private BiFunction<int[], Item, int[]> updateBestValuesForItem(int maximumWeight) {
        return (currentBestValues, item)
                       -> this.computeBestValuesWithItem(maximumWeight, item, currentBestValues);
    }

    private int[] computeBestValuesWithItem(
            int maximumWeight,
            Item item,
            int[] previousBestValues
    ) {
        return IntStream.rangeClosed(0, maximumWeight)
                       .map(this.computeBestValueForWeightLimit(item, previousBestValues))
                       .toArray();
    }

    private IntUnaryOperator computeBestValueForWeightLimit(Item item, int[] previousBestValues) {
        return weightLimit ->
                       this.computeBestValueForWeightLimit(weightLimit, item, previousBestValues);
    }

    private int computeBestValueForWeightLimit(
            int weightLimit,
            Item item,
            int[] previousBestValues
    ) {
        int bestWithoutItem = previousBestValues[weightLimit];
        int bestWithItem = (item.weight <= weightLimit)
                                   ? previousBestValues[weightLimit - item.weight] + item.value
                                   : 0;
        return Math.max(bestWithoutItem, bestWithItem);
    }

    private BinaryOperator<int[]> pickBestValues() {
        return (leftBestValues, unusedRightSide) -> leftBestValues;
    }
}
