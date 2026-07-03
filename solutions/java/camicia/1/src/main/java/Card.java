import java.util.Map;

public record Card(int payment) {
    private static final Map<String, Integer> CARD_VALUES = Map.of(
            "J", 1,
            "Q", 2,
            "K", 3,
            "A", 4
    );

    public Card(String payment) {
        this((payment == null)
             ? 0
             : CARD_VALUES.getOrDefault(payment, 0));
    }
}