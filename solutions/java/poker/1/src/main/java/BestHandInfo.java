import java.util.Collection;
import java.util.List;

public record BestHandInfo(
        Collection<String> bestHands,
        PokerHandValue bestValue,
        List<Integer> bestRankValues
) {}