import java.util.ArrayList;
import java.util.List;

public class DominoChainState {

    DominoChainState(
            List<Domino> remainingDominoes,
            List<Domino> builtChain
    ) {
        this.remainingDominoes = remainingDominoes;
        this.builtChain = builtChain;
    }

    private List<Domino> remainingDominoes;

    DominoChainState resetRemainingDominoes(List<Domino> remainingDominoes) {
        this.remainingDominoes = remainingDominoes;
        return this;
    }

    List<Domino> getRemainingDominoes() {
        return remainingDominoes;
    }

    private List<Domino> builtChain;

    List<Domino> getBuiltChain() {
        return builtChain;
    }

    @SuppressWarnings("UnusedReturnValue")
    DominoChainState appendToChain(Domino connectedDomino) {
        this.builtChain = prependToChain(connectedDomino);
        return this;
    }

    private List<Domino> prependToChain(final Domino connectedDomino) {
        List<Domino> newChain = new ArrayList<>();
        newChain.add(connectedDomino);
        newChain.addAll(this.builtChain);
        return newChain;
    }

    boolean hasRemainingDominoes() {
        return !remainingDominoes.isEmpty();
    }

    int getLeftmostValue() {
        return this.builtChain.get(0).getLeft();
    }
}