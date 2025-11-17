import ForthError.ForthError

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
