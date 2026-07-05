import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class State {
    final int bucketOneAmount;
    final int bucketTwoAmount;
    final int moveCount;

    State(int bucketOneAmount, int bucketTwoAmount, int moveCount) {
        this.bucketOneAmount = bucketOneAmount;
        this.bucketTwoAmount = bucketTwoAmount;
        this.moveCount = moveCount;
    }

    List<State> nextStates(int bucketOneCapacity, int bucketTwoCapacity) {
        List<State> nextStates = new ArrayList<>();
        this.generateFillStates(nextStates, bucketOneCapacity, bucketTwoCapacity);
        this.generateFillStates(nextStates, 0, 0);
        this.generatePourFromOneToTwo(bucketTwoCapacity, nextStates);
        this.generatePourFromTwoToOne(bucketOneCapacity, nextStates);
        return nextStates;
    }

    private void generateFillStates(
            final List<State> nextStates,
            final int fillBucketOneCapacity,
            final int fillBucketTwoCapacity
    ) {
        nextStates.add(new State(fillBucketOneCapacity, this.bucketTwoAmount, moveCount + 1));
        nextStates.add(new State(this.bucketOneAmount, fillBucketTwoCapacity, moveCount + 1));
    }

    private void generatePourFromOneToTwo(final int bucketTwoCapacity, final List<State> nextStates) {
        int transferableAmount = Math.min(this.bucketOneAmount, bucketTwoCapacity - this.bucketTwoAmount);
        nextStates.add(new State(
                this.bucketOneAmount - transferableAmount,
                this.bucketTwoAmount + transferableAmount,
                moveCount + 1
        ));
    }

    private void generatePourFromTwoToOne(final int bucketOneCapacity, final List<State> nextStates) {
        int transferableAmount = Math.min(this.bucketTwoAmount, bucketOneCapacity - this.bucketOneAmount);
        nextStates.add(new State(
                this.bucketOneAmount + transferableAmount,
                this.bucketTwoAmount - transferableAmount,
                moveCount + 1
        ));
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof State state
               && this.bucketOneAmount == state.bucketOneAmount
               && this.bucketTwoAmount == state.bucketTwoAmount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bucketOneAmount, bucketTwoAmount);
    }
}