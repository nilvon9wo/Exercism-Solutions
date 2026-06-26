import Frame.strike
import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success, Try}

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
        frames.iterator

object Frame:
    val strike: Int = 10

class Frame(rollOne: Int, val isFinalFrame: Boolean):
    def getRollOne: Int = rollOne

    var nextFrame: Option[Frame] = None

    private var rollTwo: Option[Int] = None
    def getRollTwo: Option[Int] = rollTwo
    private def setRollTwo(pins: Int): Try[Frame] =
        updateField(
            validateSecondRoll(pins),
            value => rollTwo = Some(value)
        )
    private def validateSecondRoll(pins: Int): Try[Int] =
        val validatedPins = validateRoll(pins)
        validatedPins match
            case Success(p) if isStrike =>
                Failure(new IllegalArgumentException("No second rolls allowed when first roll is strike."))
            case Success(p) if rollOne + p > strike =>
                Failure(new IllegalArgumentException(s"First and second roll cannot add up to more than $strike pins."))
            case _ =>
                validatedPins

    private var bonusRollOne: Option[Int] = None
    def getBonusRollOne: Option[Int] = bonusRollOne
    private def setBonusRollOne(pins: Int): Try[Frame] =
        updateField(
            validateBonusRollOne(pins),
            value => bonusRollOne = Some(value)
        )

    private def validateBonusRollOne(pins: Int): Try[Int] =
        val validatedPins = validateRoll(pins)
        validatedPins match
            case Success(_) if !isFinalFrame =>
                Failure(new IllegalArgumentException("Bonus rolls only in final frame."))
            case Success(_) if !(isStrike || isSpare) =>
                Failure(new IllegalArgumentException("Bonus rolls only allowed after strike or spare in final frame."))
            case other =>
                other

    private var bonusRollTwo: Option[Int] = None
    def getBonusRollTwo: Option[Int] = bonusRollTwo
    private def setBonusRollTwo(pins: Int): Try[Frame] =
        updateField(
            validateBonusRollTwo(pins),
            value => bonusRollTwo = Some(value)
        )

    private def validateBonusRollTwo(pins: Int): Try[Int] =
        val validatedPins = validateRoll(pins)
        validatedPins match
            case Success(_) if bonusRollTwo.isDefined =>
                Failure(new IllegalArgumentException("No more bonus rolls left."))

            case Success(_) if bonusRollOne.isEmpty =>
                Failure(new IllegalArgumentException("First bonus roll has not been rolled yet."))

            case Success(_) if exceedsFinalFrameBonusPinLimit(pins) =>
                Failure(
                    new IllegalArgumentException(
                        s"First and second bonus rolls cannot add up to more than $strike pins."
                    )
                )

            case other =>
                other

    private def exceedsFinalFrameBonusPinLimit(pins: Int) =
        isFinalFrame
            && bonusRollOne.exists(_ != strike)
            && bonusRollOne.exists(_ + pins > strike)

    private def updateField(validation: Try[Int], update: Int => Unit): Try[Frame] =
        validation match
            case Success(value) =>
                update(value)
                Success(this)
            case Failure(exception) =>
                Failure(exception)

    private def validateRoll(pins: Int): Try[Int] =
        pins match {
            case p if p < 0 =>
                Failure(new IllegalArgumentException("It is impossible to roll less than 0."))
            case p if p > strike =>
                Failure(new IllegalArgumentException(s"It is impossible to roll more than $strike."))
            case _ =>
                Success(pins)
        }

    def getNextFrame: Option[Frame] = nextFrame
    def setNextFrame(value: Frame): Unit = nextFrame = Some(value)

    def addRoll(pins: Int): Try[Frame] =
        if isRegularRollPhase
        then setRollTwo(pins)
        else if isFirstBonusRollPhase
             then setBonusRollOne(pins)
             else setBonusRollTwo(pins)

    private def isRegularRollPhase: Boolean =
        !isFinalFrame
            || (!isStrike && !isSpare)

    private def isFirstBonusRollPhase: Boolean =
        isFinalFrame
            && bonusRollOne.isEmpty
            && (isStrike || isSpare)

    def isStrike: Boolean =
        rollOne == strike

    def isSpare: Boolean =
        !isStrike
            && rollTwo.isDefined
            && rollTwo.exists(rollOne + _ == strike)

    def isComplete: Boolean =
        if isFinalFrame
        then if isStrike
             then bonusRollTwo.isDefined
             else if isSpare
                  then bonusRollOne.isDefined
                  else rollTwo.isDefined
        else isStrike || rollTwo.isDefined

    def isPerfect: Boolean =
        isStrike
            && bonusRollOne.contains(strike)
            && bonusRollTwo.contains(strike)

