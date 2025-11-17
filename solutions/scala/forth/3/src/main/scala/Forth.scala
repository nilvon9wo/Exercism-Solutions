import Forth.{defaultHandlerByToken, newDefinitionPattern}
import ForthError.ForthError

import scala.annotation.tailrec
import scala.collection.immutable.HashMap
import scala.util.matching.Regex

class Forth
  extends ForthEvaluator {
  def eval(text: String): Either[ForthError, ForthEvaluatorState] = {
    val state: ForthEvaluatorState = ForthEvaluatorState(text)
    evaluate(state)
  }

  private def evaluate(state: ForthEvaluatorState): Either[ForthError, ForthEvaluatorState] = {
    state.instructions.foldLeft(Right(state): Either[ForthError, ForthEvaluatorState]) {
                                                                                         (eitherState, instruction) =>
                                                                                           eitherState match {
                                                                                             case Left(error) => Left(
                                                                                               error)
                                                                                             case Right(
                                                                                             currentState)    =>
                                                                                             evaluate(
                                                                                               currentState,
                                                                                               instruction.trim.toLowerCase)
                                                                                           }
                                                                                       }
  }

  private def evaluate(state: ForthEvaluatorState, instruction: String): Either[ForthError, ForthEvaluatorState] = {
    val instructionMatch = newDefinitionPattern.findFirstMatchIn(instruction)
    if (instructionMatch.isDefined) {
      val newWord = instructionMatch.get.group(1)
      state.defineHandler(newWord)
    }
    else {
      followInstruction(state, instruction)
    }
  }

  private def followInstruction(state: ForthEvaluatorState, instruction: String) = {
    val tokens = instruction.split(' ')
    followInstructions(state, tokens.toList)
  }

  @tailrec
  private def followInstructions(currentState: ForthEvaluatorState, remainingTokens: List[String])
  : Either[ForthError, ForthEvaluatorState] =
    remainingTokens match {
      case Nil           => Right(currentState)
      case token :: tail =>
        processToken(currentState, token) match {
          case Right(handledState) => followInstructions(handledState, tail)
          case Left(error)         => Left(error)
        }
    }

  private def processToken(currentState: ForthEvaluatorState, token: String): Either[ForthError, ForthEvaluatorState] =
    shiftTokensToStack(currentState, List(token)) match {
      case (newState, Some(operation)) => doOperation(newState, operation)
      case (newState, None)            => Right(newState)
    }

  @tailrec
  private def shiftTokensToStack(state: ForthEvaluatorState, tokens: List[String]): (ForthEvaluatorState,
    Option[String]) = tokens match {
    case Nil          => (state, None)
    case head :: tail =>
      val (newContext, nextOperation) = if (head.matches("-?\\d+")) {
        (state.pushToStack(head.toInt), None)
      }
                                        else {
                                          (state, Some(head))
                                        }

      if (tail.nonEmpty && nextOperation.isEmpty) {
        shiftTokensToStack(newContext, tail)
      }
      else {
        (newContext, nextOperation)
      }
  }

  private def doOperation(state: ForthEvaluatorState, operation: String): Either[ForthError, ForthEvaluatorState] = {
    if (operation == null || operation.isEmpty) {
      Right(state)
    }
    else {
      val operationLowerCase = operation.toLowerCase
      state.instructionsByWord.get(operationLowerCase) match {
        case Some(instruction) => followInstruction(state, instruction)
        case None              => defaultHandlerByToken.get(operationLowerCase) match {
          case Some(handler) => handler(state)
          case None          => Left(ForthError.UnknownWord)
        }
      }
    }
  }
}

object Forth {
  val newDefinitionPattern: Regex = """^:(.*)$""".r

  val defaultHandlerByToken: HashMap[String, ForthEvaluatorState => Either[ForthError, ForthEvaluatorState]] =
    HashMap(
      "+" -> MathHelpers.add,
      "-" -> MathHelpers.subtract,
      "*" -> MathHelpers.multiply,
      "/" -> MathHelpers.divide,
      "dup" -> StackHelpers.duplicateLast,
      "drop" -> StackHelpers.dropLast,
      "swap" -> StackHelpers.swapLast,
      "over" -> StackHelpers.penultimateValueCopy
      )
}
