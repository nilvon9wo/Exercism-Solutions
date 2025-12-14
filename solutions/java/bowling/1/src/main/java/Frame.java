import java.util.ArrayList;
import java.util.List;

public final class Frame {

    public static final int STRIKE_PINS = 10;
    private final List<Integer> rolls = new ArrayList<>();
    private final boolean isFinalFrame;

    public Frame(final boolean isLastFrame) {
        this.isFinalFrame = isLastFrame;
    }

    public void addRoll(final int pins) {
        this.rolls.add(pins);
    }

    public int firstRoll() {
        return this.getRollOrZero(0);
    }

    public int secondRoll() {
        return this.getRollOrZero(1);
    }

    private int getRollOrZero(int index) {
        return index < this.rolls.size()
               ? this.rolls.get(index)
               : 0;
    }

    public boolean isStrike() {
        return this.firstRoll() == STRIKE_PINS;
    }

    public boolean isSpare() {
        final int totalPins = this.firstRoll() + this.secondRoll();
        return !this.isStrike() && totalPins == STRIKE_PINS;
    }

    public int rollCount() {
        return this.rolls.size();
    }

    public boolean isLastFrame() {
        return this.isFinalFrame;
    }

    public boolean isComplete() {
        return !this.isIncomplete();
    }

    public boolean isIncomplete() {
        if (!this.isFinalFrame) {
            return this.rollCount() < 2 && !this.isStrike();
        }

        return this.needsBonusRoll();
    }

    private boolean needsBonusRoll() {
        if (this.rollCount() < 2) {
            return true; // always need at least two rolls
        }

        if (this.rollCount() == 2) {
            return this.isStrike() || this.isSpare(); // bonus roll allowed
        }

        // Third roll has already been taken
        return false;
    }

    public int pinsTotal() {
        return this.rolls.stream()
                         .mapToInt(Integer::intValue)
                         .sum();
    }

    public List<Integer> getRolls() {
        return new ArrayList<>(this.rolls);
    }

    public boolean canAcceptRoll() {
        return this.isIncomplete();
    }
}
