import java.util.*;
import java.util.stream.Collectors;
import java.util.List;

class Poker {
    private final List<String> hand;

    Poker(List<String> hand) {
        if (hand == null) {
            throw new IllegalArgumentException("handStrings must not be null");
        }

        this.hand = hand;
    }

    List<String> getBestHands() {
        BestHandInfo initial = new BestHandInfo(new ArrayList<>(), PokerHandValue.HIGH_CARD, new ArrayList<>());
        BestHandInfo result = this.hand.stream()
                                       .map(HandInfo::new)
                                       .reduce(initial, Poker::accumulateBestHands, (a, b) -> a);
        return result.bestHands()
                     .stream()
                     .toList();
    }

    private static BestHandInfo accumulateBestHands(BestHandInfo aggregate, HandInfo next) {
        int comparison = compareHands(next.getHand(), aggregate.bestValue(), aggregate.bestRankValues());
        if (comparison > 0) {
            return new BestHandInfo(
                    new ArrayList<>(List.of(next.getHandString())),
                    next.getHand()
                        .getHandValue(),
                    next.getHand()
                         .getCards()
                         .stream()
                         .map(PokerCard::getValue)
                         .collect(Collectors.toList())
            );
        }

        if (comparison == 0) {
            List<String> updatedHands = new ArrayList<>(aggregate.bestHands());
            updatedHands.add(next.getHandString());
            return new BestHandInfo(
                    updatedHands,
                    aggregate.bestValue(),
                    aggregate.bestRankValues()
            );
        }

        return aggregate;
    }

    private static int compareHands(PokerHand pokerHand, PokerHandValue bestValue, List<Integer> bestRankValues) {
        if (pokerHand.getHandValue()
                     .compareTo(bestValue) > 0) {
            return 1;
        }

        if (pokerHand.getHandValue()
                     .compareTo(bestValue) < 0) {
            return -1;
        }

        if (bestValue == PokerHandValue.FULL_HOUSE || bestValue == PokerHandValue.FOUR_OF_A_KIND) {
            String pokerHandTripletValue = EnumerableUtilities.findTripletKey(pokerHand.getCards(), PokerCard::rank);
            String bestTripletValue = findTripletKeyFromRankValues(bestRankValues);
            if (pokerHandTripletValue != null && bestTripletValue != null) {
                int tripletComparison = compareTripletValues(pokerHandTripletValue, bestTripletValue);
                if (tripletComparison != 0) {
                    return tripletComparison;
                }
            }
        }

        return compareCardValues(pokerHand, bestRankValues);
    }

    private static String findTripletKeyFromRankValues(List<Integer> values) {
        Map<String, Long> grouped = values.stream()
                                          .collect(Collectors.groupingBy(String::valueOf, Collectors.counting()));
        return grouped.entrySet()
                      .stream()
                      .filter(e -> e.getValue() >= 3)
                      .map(Map.Entry::getKey)
                      .findFirst()
                      .orElse(null);
    }

    private static int compareTripletValues(String pokerHandTripletValue, String bestTripletValue) {
        return Integer.compare(getCardValue(pokerHandTripletValue), getCardValue(bestTripletValue));
    }

    private static int compareCardValues(PokerHand pokerHand, List<Integer> bestRankValues) {
        List<Integer> cardValues = pokerHand.getCards()
                                            .stream()
                                            .map(PokerCard::getValue)
                                            .collect(Collectors.toList());
        return ListUtilities.compareTo(cardValues, bestRankValues);
    }

    private static int getCardValue(String rank) {
        return PokerCard.getCardValue(rank);
    }
}