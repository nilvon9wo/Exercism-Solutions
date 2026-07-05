import java.util.Arrays;

public class ChangeCalculationState {
    public ChangeCalculationState(int target){
        int unreachableCoinCount = target + 1;
        this.minimumCoinCounts = this.createInitializedArray(target, unreachableCoinCount);
        this.minimumCoinCounts[0] = 0;

        this.lastCoinsUsed = this.createInitializedArray(target, -1);
    }
    
    private int[] createInitializedArray(
            final int target,
            final int initialValue
    ) {
        int[] values = new int[target + 1];
        Arrays.fill(values, initialValue);
        return values;
    }

    private final int[] minimumCoinCounts;
    public int getMinimumCoinCountAt(int index) {
        return this.minimumCoinCounts[index];
    }

    private final int[] lastCoinsUsed;
    public int[] getLastCoinsUsed() {
        return this.lastCoinsUsed;
    }

    int currentAmount;
    public ChangeCalculationState forAmount(final int currentAmount) {
        this.currentAmount = currentAmount;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ChangeCalculationState recordCoinUsage(int coin) {
        minimumCoinCounts[this.currentAmount] = minimumCoinCounts[this.currentAmount - coin] + 1;
        lastCoinsUsed[this.currentAmount] = coin;
        return this;
    }

    public boolean canUseCoinForImprovedSolution(int coin) {
        return this.coinFitsAmount(coin)
               && this.producesSmallerCoinCount(coin);
    }

    private boolean coinFitsAmount(int coin) {
        return coin <= this.currentAmount;
    }

    private boolean producesSmallerCoinCount(int coin) {
        final int candidateCoinCount = this.minimumCoinCounts[currentAmount - coin] + 1;
        final int currentBestCoinCount = this.minimumCoinCounts[currentAmount];
        return candidateCoinCount < currentBestCoinCount;
    }
}
