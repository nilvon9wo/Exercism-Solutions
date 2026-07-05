import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PokerHand {
    public PokerHand(Collection<PokerCard> cards) {
        if (cards == null || cards.isEmpty()) {
            throw new IllegalArgumentException("Cards must not be null or empty");
        }
        this.cards = cards;
    }

    private final Collection<PokerCard> cards;
    public Collection<PokerCard> getCards() {
        return this.cards;
    }

    private PokerHandValue handValue;
    public PokerHandValue getHandValue() {
        if (this.handValue == null) {
            this.handValue = this.evaluateHandValue();
        }
        return this.handValue;
    }

    private PokerHandValue evaluateHandValue() {
        Map<String, List<PokerCard>> rankGroups = this.cards
                .stream()
                .collect(Collectors.groupingBy(PokerCard::rank));
        Collection<List<PokerCard>> groups = rankGroups.values();
        boolean hasFourOfKind = groups.stream()
                                      .anyMatch(g -> g.size() == 4);
        if (hasFourOfKind) {
            return PokerHandValue.FOUR_OF_A_KIND;
        }

        boolean hasThreeOfKind = groups.stream()
                                       .anyMatch(g -> g.size() == 3);
        boolean hasPair = groups.stream()
                                .anyMatch(g -> g.size() == 2);
        long pairCount = groups.stream()
                               .filter(g -> g.size() == 2)
                               .count();
        if (hasThreeOfKind) {
            return hasPair
                   ? PokerHandValue.FULL_HOUSE
                   : PokerHandValue.THREE_OF_A_KIND;
        }

        if (pairCount == 2) {
            return PokerHandValue.TWO_PAIR;
        }

        if (hasPair) {
            return PokerHandValue.PAIR;
        }

        boolean flush = this.isFlush();
        boolean lowAceStraight = this.isLowAceStraight();
        boolean straight = this.isStraight();
        return flush
               ? lowAceStraight
                     ? PokerHandValue.LOW_ACE_STRAIGHT_FLUSH
                     : straight
                           ? PokerHandValue.STRAIGHT_FLUSH
                           : PokerHandValue.FLUSH
               : lowAceStraight
                     ? PokerHandValue.LOW_ACE_STRAIGHT
                     : straight
                           ? PokerHandValue.STRAIGHT
                           : PokerHandValue.HIGH_CARD;
    }

    private boolean isStraight() {
        List<Integer> sortedValues =
                cards.stream()
                     .map(PokerCard::getValue)
                     .sorted()
                     .toList();
        int start = sortedValues.get(0);
        return IntStream.range(start, start + 5)
                        .boxed()
                        .toList()
                        .equals(sortedValues);
    }

    private boolean isLowAceStraight() {
        return cards.stream()
                    .map(PokerCard::rank)
                    .sorted()
                    .toList()
                    .equals(List.of("2", "3", "4", "5", "A"));
    }

    private boolean isFlush() {
        String suit = cards.iterator()
                           .next()
                           .suit();
        return cards.stream()
                    .allMatch(card -> card.suit().equals(suit));
    }
}