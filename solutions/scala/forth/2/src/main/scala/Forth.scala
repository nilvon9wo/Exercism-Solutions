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
    if (operation.isBlank) {
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

object ForthError
  extends Enumeration {
  type ForthError = Value
  val DivisionByZero, StackUnderflow, InvalidWord, UnknownWord = Value
}

trait ForthEvaluator {
  def eval(text: String): Either[ForthError, ForthEvaluatorState]
}

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

  override def toString: String = stack.mkString(" ")
}

object ForthEvaluatorState {
  def apply(input: String): ForthEvaluatorState = {
    new ForthEvaluatorState(input.split(";").toList)
  }
}

object MathHelpers {
  def add(state: ForthEvaluatorState): Either[ForthError, ForthEvaluatorState] =
    doMath(state, _ + _)()

  def subtract(state: ForthEvaluatorState): Either[ForthError, ForthEvaluatorState] =
    doMath(state, _ - _)()

  def multiply(state: ForthEvaluatorState): Either[ForthError, ForthEvaluatorState] =
    doMath(state, _ * _)()

  def divide(state: ForthEvaluatorState): Either[ForthError, ForthEvaluatorState] =
    doMath(state, _ / _) {
                           case _ :: 0 :: _ => Left(ForthError.DivisionByZero)
                         }

  private def doMath(state: ForthEvaluatorState, op: (Int, Int) => Int)
                    (pf: PartialFunction[List[Int], Either[ForthError, List[Int]]] = PartialFunction.empty): Either[ForthError, ForthEvaluatorState] =
    state.withStack {
                      case stack if pf.isDefinedAt(stack) => pf(stack)
                      case x :: y :: tail =>
                        Right(op(x, y) :: tail)
                      case _ =>
                        Left(ForthError.StackUnderflow)
                    }
}

object StackHelpers {
  def duplicateLast(state: ForthEvaluatorState): Either[ForthError, ForthEvaluatorState] =
    state.withStack {
                      case Nil   => Left(ForthError.StackUnderflow)
                      case stack => Right(stack :+ stack.last)
                    }

  def dropLast(state: ForthEvaluatorState): Either[ForthError, ForthEvaluatorState] =
    withStackReversed(state) {
                               case _ :: tail => Right(tail)
                             }

  def swapLast(state: ForthEvaluatorState): Either[ForthError, ForthEvaluatorState] =
    withStackReversed(state) {
                               case x :: y :: tail => Right(y :: x :: tail)
                             }

  def penultimateValueCopy(state: ForthEvaluatorState): Either[ForthError, ForthEvaluatorState] =
    withStackReversed(state) {
                               case x :: y :: tail => Right(y :: x :: y :: tail)
                             }

  private def withStackReversed(state: ForthEvaluatorState)
                               (partialFunction: PartialFunction[List[Int], Either[ForthError, List[Int]]]): Either[ForthError, ForthEvaluatorState] =
    state.withStackReversed {
                              case stack if partialFunction.isDefinedAt(stack) => partialFunction(stack)
                                .map(newStack => newStack)

                              case _                             => Left(ForthError.StackUnderflow)
                            }
}
