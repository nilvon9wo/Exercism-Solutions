import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class BreadthFirstSearch {

    public <NodeType> OptionalInt findShortestDistance(
            NodeType startNode,
            Predicate<NodeType> goalTest,
            Function<NodeType, Stream<NodeType>> neighborProvider
    ) {
        BreadthContext<NodeType> initialContext = BreadthContext.createFrom(startNode);
        return this.traverseFrontier(initialContext, goalTest, neighborProvider);
    }

    private <NodeType> OptionalInt traverseFrontier(
            BreadthContext<NodeType> context,
            Predicate<NodeType> goalTest,
            Function<NodeType, Stream<NodeType>> neighborProvider
    ) {
        if (context.currentFrontier().isEmpty()) {
            return OptionalInt.empty();
        }

        OptionalInt foundDistance = this.findGoalDistance(context, goalTest);
        if (foundDistance.isPresent()) {
            return foundDistance;
        }

        BreadthContext<NodeType> nextContext = this.computeNextContext(context, neighborProvider);
        return traverseFrontier(nextContext, goalTest, neighborProvider);
    }

    private <NodeType> OptionalInt findGoalDistance(
            BreadthContext<NodeType> context,
            Predicate<NodeType> goalTest
    ) {
        return context.currentFrontier()
                       .stream()
                       .filter(step -> context.visitedNodes().add(step.node()))
                       .filter(step -> goalTest.test(step.node()))
                       .mapToInt(BreadthNodeStep::distance)
                       .findFirst();
    }

    private <NodeType> BreadthContext<NodeType> computeNextContext(
            BreadthContext<NodeType> context,
            Function<NodeType, Stream<NodeType>> neighborProvider
    ) {
        List<BreadthNodeStep<NodeType>> nextFrontier = context.currentFrontier()
                                                               .stream()
                                                               .flatMap(step -> this.expandStep(context, neighborProvider, step))
                                                               .toList();

        return new BreadthContext<>(nextFrontier, context.visitedNodes());
    }

    private <NodeType> Stream<BreadthNodeStep<NodeType>> expandStep(
            BreadthContext<NodeType> context,
            Function<NodeType, Stream<NodeType>> neighborProvider,
            BreadthNodeStep<NodeType> step
    ) {
        return neighborProvider.apply(step.node())
                       .filter(neighbor -> !context.visitedNodes().contains(neighbor))
                       .map(step::advanceToNeighbor);
    }
}
