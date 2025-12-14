import java.util.List;
import java.util.stream.IntStream;

public final class ScoreCalculator {
    private static final int MAX_FRAMES = 10;
    private final FrameScorer frameScorer;

    public ScoreCalculator() {
        this.frameScorer = new FrameScorer();
    }

    public static final String SCORE_NOT_AVAILABLE_MESSAGE = "Score cannot be taken until the end of the game";

    public int calculateScore(final List<Frame> frames) {
        this.validateGameIsComplete(frames);
        return IntStream.range(0, MAX_FRAMES)
                        .map(index -> this.frameScorer.scoreFrame(new FrameContext(frames, index)))
                        .sum();
    }

    private void validateGameIsComplete(final List<Frame> frames) {
        boolean noFrames = frames.isEmpty();
        boolean lessThanTenFrames = frames.size() < MAX_FRAMES;

        if (noFrames || lessThanTenFrames) {
            throw new IllegalStateException(SCORE_NOT_AVAILABLE_MESSAGE);
        }
    }
}
