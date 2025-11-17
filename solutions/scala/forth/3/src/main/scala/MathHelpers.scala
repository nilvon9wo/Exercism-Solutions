import ForthError.ForthError

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
