import java.util.function.IntPredicate;

public class StatementProperties {
    private final String trimmedInput;
    public StatementProperties(String trimmedInput) {
        this.trimmedInput = trimmedInput == null
                                    ? ""
                                    : trimmedInput.trim();
    }

    Boolean isSilent;
    boolean isSilent() {
        if (this.isSilent == null) {
            this.isSilent = this.trimmedInput.isEmpty();
        }

        return this.isSilent;
    }

    Boolean isQuestion;
    boolean isQuestion() {
        if (this.isQuestion == null) {
            this.isQuestion = this.trimmedInput.endsWith("?");
        }

        return this.isQuestion;
    }

    Boolean isShouting;
    boolean isShouting() {
        if (this.isShouting == null) {
            this.isShouting = this.hasLetters()
                                      && this.allLettersUppercase();
        }

        return this.isShouting;
    }

    Boolean isForcefulQuestion;
    boolean isForcefulQuestion() {
        if (this.isForcefulQuestion == null) {
            this.isForcefulQuestion = this.isShouting()
                                              && this.isQuestion();
        }

        return this.isForcefulQuestion;
    }

    private boolean hasLetters() {
        return this.trimmedInput.codePoints()
                       .anyMatch(Character::isLetter);
    }

    private boolean allLettersUppercase() {
        int letterCount = this.countMatchingCharacters(Character::isLetter);
        int uppercaseCount = this.countMatchingCharacters(Character::isUpperCase);
        return letterCount > 0
                       && letterCount == uppercaseCount;
    }

    private int countMatchingCharacters(IntPredicate predicate) {
        return (int) this.trimmedInput.codePoints()
                             .filter(predicate)
                             .count();
    }
}