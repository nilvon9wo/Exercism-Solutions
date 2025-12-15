import java.util.List;
import java.util.stream.IntStream;

public enum HousePart {
    JACK("built", "", true),
    HOUSE(),
    MALT("lay in"),
    RAT("ate"),
    CAT("killed"),
    DOG("worried"),
    COW("tossed", "with the crumpled horn"),
    MAIDEN("milked", "all forlorn"),
    MAN("kissed", "all tattered and torn"),
    PRIEST("married", "all shaven and shorn"),
    ROOSTER("woke", "that crowed in the morn"),
    FARMER("kept", "sowing his corn"),
    HORSE("belonged to", "and the hound and the horn");

    private static final List<HousePart> ORDER = List.of(values());

    private final String action;
    private final String adjectivePhrase;
    private final boolean isProperNoun;

    HousePart(String action, String adjectivePhrase, boolean isProperNoun) {
        this.action = action;
        this.adjectivePhrase = adjectivePhrase;
        this.isProperNoun = isProperNoun;
    }

    HousePart(String action, String adjPhrase) {
        this(action, adjPhrase, false);
    }

    HousePart(String action) {
        this(action, null, false);
    }

    HousePart() {
        this(null, null, false);
    }

    public String getNounPhase() {
        String nounPhrase = (isProperNoun)
                         ? this.getProperCaseNoun()
                         : this.getNounPhraseWithArticle();
        if (this.adjectivePhrase != null) {
            nounPhrase += " " + this.adjectivePhrase;
        }

        return nounPhrase;
    }

    private String getProperCaseNoun() {
        final String name = name();
        return Character.toUpperCase(name.charAt(0))
               + name.substring(1)
                     .toLowerCase();
    }

    private String getNounPhraseWithArticle() {
        return "the " + this.name()
                            .toLowerCase();
    }

    public String getAction() {
        return action;
    }

    public static List<HousePart> getParts(int count) {
        return IntStream.range(0, count)
                        .mapToObj(ORDER::get)
                        .toList();
    }
}
