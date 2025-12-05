import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class VerseProvider {

    private static final Map<Integer, GiftType> GIFT_BY_VERSE_NUMBER = Map.ofEntries(
            Map.entry(1, GiftType.Partridge),
            Map.entry(2, GiftType.Dove),
            Map.entry(3, GiftType.Hen),
            Map.entry(4, GiftType.Bird),
            Map.entry(5, GiftType.Ring),
            Map.entry(6, GiftType.Goose),
            Map.entry(7, GiftType.Swan),
            Map.entry(8, GiftType.Maid),
            Map.entry(9, GiftType.Lady),
            Map.entry(10, GiftType.Lord),
            Map.entry(11, GiftType.Piper),
            Map.entry(12, GiftType.Drummer)
    );

    String get(int verseNumber) {
        if (!GIFT_BY_VERSE_NUMBER.containsKey(verseNumber)) {
            throw new IllegalArgumentException("Invalid verse number");
        }

        String gifts = this.getGifts(verseNumber);
        if (verseNumber > 1) {
            int lastCommaIndex = gifts.lastIndexOf(',');
            gifts = lastCommaIndex >= 0
                            ? gifts.substring(0, lastCommaIndex)
                                      + ", and"
                                      + gifts.substring(lastCommaIndex + 1)
                            : "and " + gifts;
        }

        return "On the " + Number.fromInt(verseNumber).toOrdinal() +
                       " day of Christmas my true love gave to me: " + gifts + ".";
    }

    private String getGifts(int verseNumber) {
        return IntStream.rangeClosed(1, verseNumber)
                       .map(i -> verseNumber - i + 1) // reverse order
                       .mapToObj(currentGiftNumber
                                         -> GIFT_BY_VERSE_NUMBER.get(currentGiftNumber)
                                                    .toPhrase(currentGiftNumber)
                       )
                       .collect(Collectors.joining(" "));
    }
}