import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HandInfo {
    public HandInfo(String handString) {
        if (handString == null || handString.isBlank()) {
            throw new IllegalArgumentException("handString must not be null or blank");
        }
        this.handString = handString;
    }

    private final String handString;
    public String getHandString() {
        return this.handString;
    }

    private PokerHand hand;
    public PokerHand getHand() {
        if (this.hand == null) {
            this.hand = evaluateHand();
        }
        return this.hand;
    }

    private PokerHand evaluateHand() {
        List<PokerCard> orderedCards = Arrays.stream(handString.split(" "))
                                             .map(PokerCard::from)
                                             .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                                             .collect(Collectors.toList());

        return new PokerHand(orderedCards);
    }
}