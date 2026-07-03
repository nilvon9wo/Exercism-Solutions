import java.util.*;
import java.util.stream.Collectors;

public final class Deck {
    private final Queue<Card> cards = new ArrayDeque<>();

    public boolean isEmpty() {
        return this.cards.isEmpty();
    }

    public Optional<Card> tryDraw() {
        return Optional.ofNullable(this.cards.poll());
    }

    public void clear() {
        this.cards.clear();
    }

    public void add(Card card) {
        this.cards.add(card);
    }

    public void add(String card) {
        add(new Card(card));
    }

    public void addAllCards(Collection<Card> cards) {
        cards.forEach(this::add);
    }

    public Iterable<Card> asEnumerable() {
        return cards;
    }

    public String encodePayments() {
        return cards.stream()
                    .mapToInt(Card::payment)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(","));
    }
}