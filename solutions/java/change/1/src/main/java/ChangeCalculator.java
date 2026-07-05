import java.util.*;
import java.util.stream.IntStream;

class ChangeCalculator {
    private final List<Integer> coins;

    ChangeCalculator(List<Integer> currencyCoins) {
        if (currencyCoins == null || currencyCoins.isEmpty()) {
            throw new IllegalArgumentException("Currency must have coins.");
        }

        this.coins = currencyCoins.stream()
                                  .sorted()
                                  .toList();
    }

    List<Integer> computeMostEfficientChange(int grandTotal) {
        if (grandTotal < 0) {
            throw new IllegalArgumentException("Negative totals are not allowed.");
        }

        if (grandTotal == 0) {
            return List.of();
        }

        List<Integer> result = solve(grandTotal);
        if (result == null) {
            throw new IllegalArgumentException("The total " + grandTotal + " cannot be represented in the given currency.");
        }

        return result;
    }

    private List<Integer> solve(int target) {
        ChangeCalculationState changeCalculationState = new ChangeCalculationState(target);
        IntStream.rangeClosed(1, target)
                 .forEach(amount -> this.updateMinimumCoinSolutionForAmount(changeCalculationState.forAmount(amount)));
        return changeCalculationState.getMinimumCoinCountAt(target) == target + 1
               ? null
               : this.reconstruct(target, changeCalculationState.getLastCoinsUsed());
    }

    private void updateMinimumCoinSolutionForAmount(final ChangeCalculationState changeCalculationState) {
        coins.stream()
             .mapToInt(coin -> coin)
             .filter(changeCalculationState::canUseCoinForImprovedSolution)
             .forEach(changeCalculationState::recordCoinUsage);
    }

    private List<Integer> reconstruct(int target, int[] lastCoin) {
        List<Integer> result = new ArrayList<>();
        int current = target;
        while (current > 0) {
            int coin = lastCoin[current];
            if (coin == -1) {
                return null;
            }

            result.add(coin);
            current -= coin;
        }

        return result;
    }
}