import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class FoodChain {
    private static final Map<Integer, Animal> ANIMAL_BY_NUMBER = Map.of(
            1, Animal.FLY,
            2, Animal.SPIDER,
            3, Animal.BIRD,
            4, Animal.CAT,
            5, Animal.DOG,
            6, Animal.GOAT,
            7, Animal.COW,
            8, Animal.HORSE
    );

    private static final int LAST_ANIMAL = Collections.max(ANIMAL_BY_NUMBER.keySet());

    public String verse(int verseNumber) {
        Animal animal = Optional.ofNullable(ANIMAL_BY_NUMBER.get(verseNumber))
                                .orElseThrow(() -> new IllegalArgumentException(
                                        "Neither animal nor verse exists for " + verseNumber));

        return this.createVerse(verseNumber, animal);
    }

    private String createVerse(final int verseNumber, final Animal animal) {
        final String remark = animal.getRemark();
        String verse = "I know an old lady who swallowed " + animal.indefiniteForm() + ".";
        if (remark != null && !remark.isBlank()) {
            verse += "\n" + remark;
        }
        verse += this.createRefrain(verseNumber);
        return verse;
    }

    private String createRefrain(int verseNumber) {
        if (verseNumber == LAST_ANIMAL) {
            return "";
        }

        String firstNewline = "\n";

        String lines = IntStream.iterate(verseNumber, n -> n > 1, n -> n - 1)
                                .mapToObj(this::createSwallowLine)
                                .collect(Collectors.joining());

        return firstNewline + lines + "I don't know why she swallowed the fly. Perhaps she'll die.";
    }

    private String createSwallowLine(int animalNumber) {
        Animal current = ANIMAL_BY_NUMBER.get(animalNumber);
        Animal previous = ANIMAL_BY_NUMBER.get(animalNumber - 1);

        String lineEnd = previous.hasExtendVerse()
                         ? " " + previous.verseExtension()
                         : ".";

        return "She swallowed the " + current.getName() +
               " to catch the " + previous.getName() + lineEnd + "\n";
    }

    public String verses(int startVerse, int endVerse) {
        return IntStream.rangeClosed(startVerse, endVerse)
                        .mapToObj(this::verse)
                        .collect(Collectors.joining("\n\n"));
    }
}
