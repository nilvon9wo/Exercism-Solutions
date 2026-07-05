import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BookStore {
    private static final int BOOK_TYPES = 5;
    private static final double BOOK_PRICE = 8.0;
    private static final double[] DISCOUNT_FACTOR_BY_GROUP_SIZE = {
            0.0,   // unused index 0
            1.0,   // 1 book: no discount
            0.95,  // 2 books: 5%
            0.90,  // 3 books: 10%
            0.80,  // 4 books: 20%
            0.75   // 5 books: 25%
    };

    double calculateBasketCost(List<Integer> books) {
        if (books.isEmpty()) {
            return 0.0;
        }

        int[] remainingCopiesPerTitle = this.buildFrequencyTable(books);
        return this.calculateMinimumCost(remainingCopiesPerTitle, new HashMap<>());
    }

    private int[] buildFrequencyTable(List<Integer> books) {
        int[] counts = new int[BOOK_TYPES];
        books.forEach(book -> counts[book - 1]++);
        return counts;
    }

    private double calculateMinimumCost(
            int[] remainingCopiesPerTitle,
            Map<String, Double> minimumCostByBooks
    ) {
        String stateKey = this.encodeState(remainingCopiesPerTitle);
        if (minimumCostByBooks.containsKey(stateKey)) {
            return minimumCostByBooks.get(stateKey);
        }

        if (this.isBasketEmpty(remainingCopiesPerTitle)) {
            return 0.0;
        }

        double bestCost
                = this.findCheapestGrouping(remainingCopiesPerTitle, minimumCostByBooks);
        minimumCostByBooks.put(stateKey, bestCost);
        return bestCost;
    }

    private boolean isBasketEmpty(int[] counts) {
        return Arrays.stream(counts)
                     .noneMatch(count -> count > 0);
    }

    private double findCheapestGrouping(
            int[] remainingCopiesPerTitle,
            Map<String, Double> minimumCostByBooks
    ) {
        double bestCost = Double.MAX_VALUE;
        for (
                int subsetMask = 1;
                subsetMask < (1 << BOOK_TYPES);
                subsetMask++
        ) {
            bestCost = this.evaluateGroupingOption(
                    remainingCopiesPerTitle,
                    subsetMask,
                    bestCost,
                    minimumCostByBooks
            );
        }

        return bestCost;
    }

    private double evaluateGroupingOption(
            int[] remainingCopiesPerTitle,
            int subsetMask,
            double currentBest,
            Map<String, Double> minimumCostByBooks
    ) {

        int[] nextState = remainingCopiesPerTitle.clone();
        int booksInGroup = applyGroupingSubset(nextState, subsetMask);
        if (booksInGroup != Integer.bitCount(subsetMask)) {
            return currentBest;
        }

        double groupCost = calculateGroupCost(booksInGroup);
        double totalCost = groupCost
                           + this.calculateMinimumCost(nextState, minimumCostByBooks);
        return Math.min(currentBest, totalCost);
    }

    private int applyGroupingSubset(int[] remainingCopiesPerTitle,
                                    int subsetMask) {
        int booksInGroup = 0;
        for (int i = 0; i < BOOK_TYPES; i++) {
            if ((subsetMask & (1 << i)) != 0 && remainingCopiesPerTitle[i] > 0) {
                remainingCopiesPerTitle[i]--;
                booksInGroup++;
            }
        }

        return booksInGroup;
    }

    private double calculateGroupCost(int groupSize) {
        double baseCost = groupSize * BOOK_PRICE;
        double discountFactor = DISCOUNT_FACTOR_BY_GROUP_SIZE[groupSize];
        return baseCost * discountFactor;
    }

    private String encodeState(int[] counts) {
        return counts[0] + "," +
               counts[1] + "," +
               counts[2] + "," +
               counts[3] + "," +
               counts[4];
    }
}