public final class RollContext {

    private final Frame frame;
    private final int pins;
    private final int rollIndex;

    public RollContext(final Frame frame, final int pins) {
        this.frame = frame;
        this.pins = pins;
        this.rollIndex = frame.rollCount();
    }

    public Frame getFrame() {
        return this.frame;
    }

    public int getPins() {
        return this.pins;
    }

    public int getRollIndex() {
        return this.rollIndex;
    }
}
