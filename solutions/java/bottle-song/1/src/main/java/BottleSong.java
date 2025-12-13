import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BottleSong {

    private static final Map<Integer, String> NUMBER_TO_WORDS = Map.ofEntries(
            Map.entry(0, "no"),
            Map.entry(1, "One"),
            Map.entry(2, "Two"),
            Map.entry(3, "Three"),
            Map.entry(4, "Four"),
            Map.entry(5, "Five"),
            Map.entry(6, "Six"),
            Map.entry(7, "Seven"),
            Map.entry(8, "Eight"),
            Map.entry(9, "Nine"),
            Map.entry(10, "Ten")
    );

    public String recite(int startBottles, int takeDown) {
        return IntStream.range(0, takeDown)
                        .mapToObj(i -> this.getVerse(startBottles - i))
                        .map(verseLines -> String.join("\n", verseLines))
                        .collect(Collectors.joining("\n\n")) + "\n";
    }


    private String[] getVerse(int currentNumber) {
        String currentWord = NUMBER_TO_WORDS.get(currentNumber);
        String currentBottle = bottleWord(currentNumber);

        int nextNumber = currentNumber - 1;
        String nextWord = this.getNextNumber(nextNumber);
        String nextBottle = this.bottleWord(nextNumber);
        final String verseLine = currentWord + " green " + currentBottle + " hanging on the wall,";

        return new String[] {
                verseLine,
                verseLine,
                "And if one green bottle should accidentally fall,",
                "There'll be " + nextWord.toLowerCase() + " green " + nextBottle + " hanging on the wall."
        };
    }

    private String getNextNumber(final int nextNumber) {
        return NUMBER_TO_WORDS.getOrDefault(nextNumber, String.valueOf(nextNumber));
    }

    private String bottleWord(int number) {
        return number == 1
               ? "bottle"
               : "bottles";
    }
}
