import scala.util.{Success, Failure}

class Bowling {
    private val strike: Int = Frame.strike
    private val perfectScore: Int = 300
    private val frames: Frames = Frames()
    private var invalidState: Option[String] = None

    def roll(pins: Int): Bowling =
        if invalidState.isEmpty
        then frames.addRoll(pins) match
                case Success(_) =>
                case Failure(exception) =>
                    invalidState = Some(exception.getMessage)
        this

    def score(): Either[String, Int] =
        handleInvalidState()
            .orElse(handleIncompleteGame())
            .orElse(handlePerfectGame())
            .getOrElse(computeNormalScore())

    private def handleInvalidState(): Option[Either[String, Int]] =
        invalidState.map(Left(_))

    private def handleIncompleteGame(): Option[Either[String, Int]] =
        if !frames.areComplete
        then Some(Left("An incomplete game cannot be scored."))
        else None

    private def handlePerfectGame(): Option[Either[String, Int]] =
        if frames.arePerfect
        then Some(Right(perfectScore))
        else None

    private def computeNormalScore(): Either[String, Int] =
        Right(
            frames.foldLeft(0)(
                (acc, frame) =>
                    acc + getFrameScore(frame)
            )
        )

    private def getFrameScore(frame: Frame): Int =
        if frame.isStrike
        then strike + getStrikeBonus(frame)
        else if frame.isSpare
             then strike + getSpareBonus(frame)
             else getNormalFrameScore(frame)

    private def getNormalFrameScore(frame: Frame): Int =
        frame.getRollOne + frame.getRollTwo.getOrElse(0)

    private def getSpareBonus(frame: Frame): Int =
        if frame.isFinalFrame
        then frame.getBonusRollOne.getOrElse(0)
        else frame.nextFrame match
            case Some(nextFrame) => nextFrame.getRollOne
            case None => 0

    private def getStrikeBonus(frame: Frame): Int =
        if frame.isFinalFrame
        then getLastFrameBonus(frame)
        else {
            val nextFrame = frame.nextFrame.get
            if nextFrame.isStrike
            then calculateSecondStrikeBonus(nextFrame)
            else calculateSecondNonStrikeBonus(nextFrame)
        }

    private def getLastFrameBonus(frame: Frame): Int =
        frame.getBonusRollOne.getOrElse(0)
            + frame.getBonusRollTwo.getOrElse(0)

    private def calculateSecondStrikeBonus(secondFrame: Frame): Int =
        addNextFrameBonus(secondFrame.nextFrame, strike, calculateThirdFrameBonus)

    private def calculateSecondNonStrikeBonus(secondFrame: Frame): Int =
        val bonus = getNormalFrameScore(secondFrame)
        addNextFrameBonus(secondFrame.nextFrame, bonus, getNormalFrameScore)

    private def addNextFrameBonus(
                                     nextFrame: Option[Frame],
                                     base: Int,
                                     bonusFunction: Frame => Int
                                 ): Int =
        base + nextFrame.fold(0)(bonusFunction)

    private def calculateThirdFrameBonus(frame: Frame): Int =
        if frame.isStrike
        then strike
        else frame.getRollOne
}