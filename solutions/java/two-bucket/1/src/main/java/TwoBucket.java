import java.util.*;

class TwoBucket {

    private final int bucketOneCapacity;
    private final int bucketTwoCapacity;
    private final int goalLiters;
    private final String startingBucket;

    TwoBucket(int bucketOneCap, int bucketTwoCap, int desiredLiters, String startBucket) {
        this.bucketOneCapacity = bucketOneCap;
        this.bucketTwoCapacity = bucketTwoCap;
        this.goalLiters = desiredLiters;
        this.startingBucket = startBucket;
    }

    private static final String ONE = "one";
    private static final String TWO = "two";
    
    Result getResult() {
        if (this.goalLiters > this.bucketOneCapacity && this.goalLiters > this.bucketTwoCapacity) {
            throw new UnreachableGoalException();
        }

        State startState = createStartState();
        Queue<State> statesToExplore = createQueue(startState);
        Set<State> visitedStates = createVisitedStates(startState);
        while (!statesToExplore.isEmpty()) {
            State currentState = statesToExplore.poll();

            if (currentState.bucketOneAmount == this.goalLiters) {
                return resultFrom(currentState, ONE);
            }

            if (currentState.bucketTwoAmount == this.goalLiters) {
                return resultFrom(currentState, TWO);
            }

            exploreNextStates(currentState, visitedStates, statesToExplore);
        }

        throw new UnreachableGoalException();
    }

    private static Queue<State> createQueue(final State startState) {
        Queue<State> queue = new ArrayDeque<>();
        queue.add(startState);
        return queue;
    }

    private static Set<State> createVisitedStates(final State startState) {
        Set<State> visited = new HashSet<>();
        visited.add(startState);
        return visited;
    }

    private void exploreNextStates(
            final State currentState,
            final Set<State> visitedStates,
            final Queue<State> statesToExplore
    ) {
        currentState.nextStates(this.bucketOneCapacity, this.bucketTwoCapacity)
                    .stream()
                    .filter(nextState -> !isInvalidStartViolation(nextState))
                    .filter(visitedStates::add)
                    .forEachOrdered(statesToExplore::add);
    }

    private State createStartState() {
        return ONE.equals(this.startingBucket)
               ? new State(this.bucketOneCapacity, 0, 1)
               : new State(0, this.bucketTwoCapacity, 1);
    }

    private boolean isInvalidStartViolation(State nextState) {
        return ONE.equals(startingBucket)
               ? nextState.bucketOneAmount == 0
                    && nextState.bucketTwoAmount == this.bucketTwoCapacity
               : nextState.bucketTwoAmount == 0
                    && nextState.bucketOneAmount == this.bucketOneCapacity;
    }

    private Result resultFrom(State state, String finalBucket) {
        final int otherBucketAmount = ONE.equals(finalBucket)
                                      ? state.bucketTwoAmount
                                      : state.bucketOneAmount;

        return new Result(state.moveCount, finalBucket, otherBucketAmount);
    }
}