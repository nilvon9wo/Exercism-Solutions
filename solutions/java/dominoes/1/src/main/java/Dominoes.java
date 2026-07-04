import java.util.*;

class Dominoes {
    List<Domino> formChain(List<Domino> inputDominoes) throws ChainNotFoundException {
        if (inputDominoes == null) {
            return Collections.emptyList();
        }

        List<Domino> orderedDominoes = this.prioritizeSelfLoops(inputDominoes);
        if (orderedDominoes.isEmpty()) {
            return Collections.emptyList();
        }

        Domino startingDomino = orderedDominoes.get(0);
        List<Domino> remainingDominoes = new ArrayList<>(orderedDominoes.subList(1, orderedDominoes.size()));
        List<Domino> chainResult = this.solveDominoChain(remainingDominoes, new ArrayList<>(List.of(startingDomino)));
        if (this.isInvalidChainResult(chainResult, startingDomino)) {
            throw new ChainNotFoundException("No domino chain found.");
        }

        return chainResult;
    }

    private boolean isInvalidChainResult(final List<Domino> chain, final Domino startDomino) {
        return chain == null
               || chain.isEmpty()
               || chain.get(0).getLeft() != startDomino.getRight();
    }

    private List<Domino> prioritizeSelfLoops(List<Domino> dominoes) {
        List<Domino> selfLoopDominoes = new ArrayList<>();
        List<Domino> nonSelfLoopDominoes = new ArrayList<>();

        for (Domino domino : dominoes) {
            if (domino.getLeft() == domino.getRight()) {
                selfLoopDominoes.add(domino);
            }
            else {
                nonSelfLoopDominoes.add(domino);
            }
        }

        List<Domino> orderedDominoes = new ArrayList<>();
        orderedDominoes.addAll(selfLoopDominoes);
        orderedDominoes.addAll(nonSelfLoopDominoes);
        return orderedDominoes;
    }

    private List<Domino> solveDominoChain(
            List<Domino> remainingDominoes,
            List<Domino> builtChain
    ) {
        if (remainingDominoes.isEmpty()) {
            return builtChain;
        }

        DominoChainState dominoChainState = new DominoChainState(remainingDominoes, builtChain);
        while (dominoChainState.hasRemainingDominoes()) {
            int requiredConnectorValue = dominoChainState.getLeftmostValue();
            DominoConnectabilityPartition partition =
                    DominoConnectabilityPartition.create(requiredConnectorValue, dominoChainState.getRemainingDominoes());

            List<Domino> candidateDominoes = partition.postmatchInclusive;
            if (candidateDominoes.isEmpty()) {
                return null;
            }

            Domino connectedDomino =
                    this.selectAndConnect(candidateDominoes, requiredConnectorValue);
            if (connectedDomino == null) {
                return null;
            }

            dominoChainState.resetRemainingDominoes(partition.toList())
                            .appendToChain(connectedDomino);
        }

        return dominoChainState.getBuiltChain();
    }

    private Domino selectAndConnect(final List<Domino> candidateDominoes, final int requiredConnectorValue) {
        Domino selectedDomino = candidateDominoes.get(0);
        candidateDominoes.remove(0);
        return DominoConnectabilityPartition.connect(requiredConnectorValue, selectedDomino);
    }
}