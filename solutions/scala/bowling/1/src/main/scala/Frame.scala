import Frame.strike
import scala.util.{Failure, Success, Try}

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
