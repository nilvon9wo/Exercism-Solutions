import java.util.stream.IntStream;

public class AlphaSolver {
    private final AlphaValidator validator;

    public AlphaSolver() {
        this(new AlphaValidator());
    }

    private AlphaSolver(AlphaValidator validator) {
        this.validator = validator;
    }

    public boolean assignDigitsDepthFirst(SolverContext context, int letterIndex) {
        return this.assignDigitsDepthFirst(new SolverFrame(context, letterIndex));
    }

    private boolean assignDigitsDepthFirst(SolverFrame frame) {
        if (isAssignmentComplete(frame)) {
            return this.validator.assignmentProducesCorrectSum(frame.context());
        }

        Character letterToAssign = letterAtFrame(frame);
        return IntStream.rangeClosed(0, 9)
                       .filter(frame::isDigitAvailable)
                       .anyMatch(digit -> this.attemptAssignment(frame, letterToAssign, digit));
    }

    private boolean isAssignmentComplete(SolverFrame frame) {
        return frame.letterIndex() == frame.getLettersSize();
    }

    private Character letterAtFrame(SolverFrame frame) {
        return frame.getLetterByIndex(frame.letterIndex());
    }

    private boolean attemptAssignment(SolverFrame frame, char letter, int digit) {
        if (digitViolatesLeadingZeroConstraint(frame, letter, digit)) {
            return false;
        }

        frame.applyDigitAssignment(letter, digit);
        boolean solved = this.assignDigitsDepthFirst(frame.getNextFrame());
        if (!solved) {
            frame.revertDigitAssignment(letter, digit);
        }

        return solved;
    }

    private boolean digitViolatesLeadingZeroConstraint(SolverFrame frame, char letter, int digit) {
        return frame.isLeadingLetter(letter)
                       && digit == 0;
    }

}
