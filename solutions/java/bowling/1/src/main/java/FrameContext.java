import java.util.List;

public final class FrameContext {

    private final List<Frame> frames;
    private final int frameIndex;

    public FrameContext(final List<Frame> frames, final int frameIndex) {
        this.frames = frames;
        this.frameIndex = frameIndex;
    }

    public Frame getCurrentFrame() {
        return this.frames.get(this.frameIndex);
    }

    public Frame getNextFrame() {
        int nextIndex = this.frameIndex + 1;
        return nextIndex < this.frames.size()
               ? this.frames.get(nextIndex)
               : null;
    }

    public Frame getFollowingFrame() {
        int followingIndex = this.frameIndex + 2;
        return followingIndex < this.frames.size()
               ? this.frames.get(followingIndex)
               : null;
    }

    public boolean isLastFrame() {
        return this.frameIndex == this.frames.size() - 1;
    }
}
