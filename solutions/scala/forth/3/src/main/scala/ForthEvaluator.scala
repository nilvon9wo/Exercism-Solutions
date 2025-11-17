import ForthError.ForthError

trait ForthEvaluator {
  def eval(text: String): Either[ForthError, ForthEvaluatorState]
}
