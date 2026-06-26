import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success, Try}

class Frames extends Iterable[Frame]:
    private val totalFrameCount: Int = 10
    private val finalFrameIndex: Int = totalFrameCount - 1
    private val frames: ListBuffer[Frame] = ListBuffer.empty

    private def lastFrame: Option[Frame] =
        frames.lastOption
    private def finalFrame: Option[Frame] =
        if frames.size == totalFrameCount
        then Some(frames(finalFrameIndex))
        else None

    def addRoll(pins: Int): Try[Frames] =
        validateGameNotComplete()
            .flatMap(_ => applyRoll(pins))

    private def validateGameNotComplete(): Try[Unit] =
        if areComplete
        then Failure(new IllegalArgumentException("A complete game cannot receive more rolls."))
        else Success(())

    private def applyRoll(pins: Int): Try[Frames] =
        lastFrame match
            case None =>
                startNewFrame(pins)
            case Some(frame) if frame.isComplete =>
                startNewFrame(pins)
            case Some(frame) =>
                frame.addRoll(pins).map(_ => this)

    private def startNewFrame(pins: Int): Try[Frames] =
        val newFrame =  Frame(
            rollOne = pins,
            isFinalFrame = IsPreviousFramePenultimateFrame
        )
        lastFrame.foreach(_.nextFrame = Some(newFrame))
        frames.addOne(newFrame)
        Success(this)

    private def IsPreviousFramePenultimateFrame: Boolean =
        frames.size == totalFrameCount - 1

    def areComplete: Boolean =
        frames.size == totalFrameCount
            && frames.forall(_.isComplete)

    def arePerfect:Boolean =
        areComplete
            && frames.forall(_.isStrike)
            && finalFrame.exists(_.isPerfect)

    def apply(frameIndex: Int): Frame =
        frames(frameIndex)

    override def iterator: Iterator[Frame] =
        frames.headOption
              .map(new FrameIterator(_))
              .getOrElse(throw new IllegalStateException("No frames yet!"))