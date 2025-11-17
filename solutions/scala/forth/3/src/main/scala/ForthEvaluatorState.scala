import scala.collection.immutable.HashMap

class ForthEvaluatorState(
                           val instructions: List[String],
                           val stack: List[Int] = Nil,
                           val instructionsByWord: HashMap[String, String] = HashMap()
                         ) {

  def defineHandler(input: String): Either[ForthError.ForthError, ForthEvaluatorState] = {
    val definition = input.split(' ')
                          .filterNot(_.isEmpty)
                          .toList
    defineHandler(definition)
  }

  private def defineHandler(definition: List[String]): Either[ForthError.ForthError, ForthEvaluatorState] =
    definition match {
      case head :: tail =>
        if (isInteger(head)) {
          Left(ForthError.InvalidWord)
        }
        else {
          val updatedInstructionsByWord = instructionsByWord + (head -> createInstruction(tail))
          Right(new ForthEvaluatorState(instructions, stack, updatedInstructionsByWord))
        }

      case _ =>
        Left(ForthError.StackUnderflow)
    }


  private def isInteger(string: String): Boolean = string.matches("-?\\d+")

  private def createInstruction(definition: List[String]): String =
    definition.map {
                     word =>
                       instructionsByWord.getOrElse(word, word)
                   }
              .mkString(" ")
              .trim()

  def pushToStack(value: Int): ForthEvaluatorState = new ForthEvaluatorState(
    instructions, stack :+ value, instructionsByWord)

  def withStack(function: List[Int] => Either[ForthError.ForthError, List[Int]]): Either[ForthError.ForthError,
    ForthEvaluatorState] =
    function(this.stack) match {
      case Left(error)     => Left(error)
      case Right(newStack) => Right(new ForthEvaluatorState(instructions, newStack, instructionsByWord))
    }

  def withStackReversed(function: List[Int] => Either[ForthError.ForthError, List[Int]]): Either[ForthError
  .ForthError, ForthEvaluatorState] =
    function(this.stack.reverse) match {
      case Left(error)     => Left(error)
      case Right(newStack) => Right(new ForthEvaluatorState(instructions, newStack.reverse, instructionsByWord))
    }

  def copy(
            instructions: List[String] = this.instructions,
            stack: List[Int] = this.stack,
            instructionsByWord: HashMap[String, String] = this.instructionsByWord
          ): ForthEvaluatorState =
    new ForthEvaluatorState(instructions, stack, instructionsByWord)

  override def toString: String = stack.mkString(" ")
}

object ForthEvaluatorState {
  def apply(input: String): ForthEvaluatorState = {
    new ForthEvaluatorState(input.split(";").toList)
  }
}
