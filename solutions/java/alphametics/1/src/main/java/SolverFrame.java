import java.util.List;

public record SolverFrame(SolverContext context, int letterIndex) {
    public void applyDigitAssignment(char letter, int digit) {
        this.context()
                .applyDigitAssignment(letter, digit);
    }

    public void revertDigitAssignment(char letter, int digit) {
        this.context()
                .revertDigitAssignment(letter, digit);
    }

    public int getLettersSize() {
        return this.getLetters()
                       .size();
    }

    public Character getLetterByIndex(int index) {
        return this.getLetters()
                       .get(index);
    }

    private List<Character> getLetters() {
        return this.context
                       .puzzle()
                       .letters();
    }

    public boolean isDigitAvailable(int digit) {
        return !this.context()
                        .usedDigits()
                        .contains(digit);
    }

    public boolean isLeadingLetter(char letter) {
        return this.context()
                       .puzzle()
                       .leadingLetters()
                       .contains(letter);
    }

    public SolverFrame getNextFrame() {
        return new SolverFrame(this.context(), this.letterIndex + 1);
    }
}