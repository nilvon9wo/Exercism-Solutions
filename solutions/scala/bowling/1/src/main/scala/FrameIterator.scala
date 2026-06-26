private class FrameIterator(start: Frame) extends Iterator[Frame]:
    private var currentFrame: Option[Frame] = Some(start)

    def hasNext: Boolean = currentFrame.isDefined

    def next(): Frame =
        val frame = currentFrame.getOrElse(
            throw new NoSuchElementException("next on empty iterator")
        )
        currentFrame = frame.nextFrame
        frame