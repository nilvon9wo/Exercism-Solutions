import java.util.Map;

public record PokerCard(String rank, String suit) {
    public PokerCard {
        if (rank == null || suit == null) {
            throw new IllegalArgumentException("Rank and suit must not be null");
        }
    }

    private static final Map<String, Integer> VALUE_BY_RANK = Map.ofEntries(
            Map.entry("2", 2), Map.entry("3", 3), Map.entry("4", 4), Map.entry("5", 5), Map.entry("6", 6),
            Map.entry("7", 7), Map.entry("8", 8), Map.entry("9", 9), Map.entry("10", 10), Map.entry("J", 11),
            Map.entry("Q", 12), Map.entry("K", 13), Map.entry("A", 14)
    );

    public static int getCardValue(String rank) {
        Integer value = VALUE_BY_RANK.get(rank);
        if (value == null) {
            throw new IllegalArgumentException("Invalid card rank: " + rank);
        }

        return value;
    }

    public int getValue() {
        return getCardValue();
    }

    public static PokerCard from(String cardString) {
        if (cardString == null || cardString.length() < 2) {
            throw new IllegalArgumentException("Invalid card string: " + cardString);
        }

        String rank = cardString.substring(0, cardString.length() - 1);
        String suit = cardString.substring(cardString.length() - 1);
        return new PokerCard(rank, suit);
    }

    public int getCardValue() {
        return getCardValue(this.rank);
    }
}