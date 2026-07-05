import java.util.ArrayList;
import java.util.List;

public class DominoConnectabilityPartition {
    List<Domino> prematchExclusive;
    List<Domino> postmatchInclusive;

    DominoConnectabilityPartition(List<Domino> prematchExclusive, List<Domino> postmatchInclusive) {
        this.prematchExclusive = prematchExclusive;
        this.postmatchInclusive = postmatchInclusive;
    }

    static DominoConnectabilityPartition create(int stone, List<Domino> dominoes) {
        List<Domino> nonConnectableDominoes = new ArrayList<>();
        List<Domino> connectableDominoes = new ArrayList<>();

        boolean inPrefixPhase = true;

        for (Domino domino : dominoes) {
            if (inPrefixPhase && isNotConnectable(stone, domino)) {
                nonConnectableDominoes.add(domino);
            }
            else {
                inPrefixPhase = false;
                connectableDominoes.add(domino);
            }
        }

        return new DominoConnectabilityPartition(nonConnectableDominoes, connectableDominoes);
    }

    private static  boolean isNotConnectable(int stone, Domino domino) {
        return connect(stone, domino) == null;
    }

    static Domino connect(int stone, Domino domino) {
        return domino.getLeft() == stone
               ? new Domino(domino.getRight(), domino.getLeft())
               : domino.getRight() == stone
                     ? domino
                     : null;
    }

    List<Domino> toList() {
        List<Domino> newRemainingDominoes = new ArrayList<>();
        newRemainingDominoes.addAll(prematchExclusive);
        newRemainingDominoes.addAll(postmatchInclusive);
        return newRemainingDominoes;
    }
}