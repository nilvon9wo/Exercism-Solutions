import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class House {

    public String verse(int verseNumber) {
        List<HousePart> parts = HousePart.getParts(verseNumber + 1)
                                         .reversed();
        return this.buildVerse(parts);
    }

    private String buildVerse(List<HousePart> parts) {
        String chain = this.buildChain(parts);
        String lastLine = this.buildFinalLine(parts);
        return ("This is" + chain + lastLine)
                .trim();
    }

    private String buildChain(final List<HousePart> parts) {
        return IntStream.range(0, parts.size() - 2)
                        .mapToObj(parts::get)
                        .map(this::combinePart)
                        .collect(Collectors.joining());
    }

    private String combinePart(HousePart part) {
        final String nounPhase = part.getNounPhase();
        final String action = part.getAction();
        return this.merge(nounPhase, action);
    }

    private String buildFinalLine(final List<HousePart> parts) {
        final int size = parts.size();
        List<HousePart> lastTwo = parts.subList(size - 2, size);
        final String firstPart = this.formatWithAction(lastTwo.get(0));
        final String secondPart = this.formatWithAction(lastTwo.get(1));
        return this.merge(firstPart, secondPart);
    }

    private String merge(final String a, final String b) {
        return " " + a + " that " + b;
    }

    private String formatWithAction(HousePart part) {
        String nounPhase = part.getNounPhase();
        String action = part.getAction();
        return nounPhase + this.appendAction(action);
    }

    private String appendAction(final String action) {
        return (action != null)
               ? (action + ".")
               : "";
    }

    public String verses(int start, int end) {
        return IntStream.rangeClosed(start, end)
                        .mapToObj(this::verse)
                        .collect(Collectors.joining("\n"));
    }

    String sing() {
        final int partsCount = HousePart.values()
                .length;
        return this.verses(1, partsCount - 1);
    }
}
