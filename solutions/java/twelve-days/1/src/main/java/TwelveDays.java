import java.util.stream.Collectors;
import java.util.stream.IntStream;

record TwelveDays(VerseProvider verseProvider) {

    public TwelveDays() {
        this(new VerseProvider());
    }

    String verse(int verseNumber) {
        return this.verseProvider.get(verseNumber) + "\n";
    }

    String verses(int startVerse, int endVerse) {
        return IntStream.rangeClosed(startVerse, endVerse)
                       .mapToObj(this::removeLinefeedAfterVerse)
                       .collect(Collectors.joining("\n\n"))
                       + "\n";
    }

    private String removeLinefeedAfterVerse(int i) {
        return verse(i)
                   .stripTrailing();
    }

    String sing() {
        return this.verses(1, 12);
    }
}
