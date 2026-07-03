import java.util.*;

public final class GameState {
    public GameState(final List<String> playerA, final List<String> playerB) {
        playerA.forEach(deckA::add);
        playerB.forEach(deckB::add);
    }

    public final Deck deckA = new Deck();
    public final Deck deckB = new Deck();

    public Deck[] decks() {
        return new Deck[]{ deckA, deckB };
    }

    public final Deck pile = new Deck();

    public int tricks;
    public int cardsPlayed;
    public int turn;

    public final HashSet<Integer> history = new HashSet<>();
}