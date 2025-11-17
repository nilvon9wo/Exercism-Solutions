object Wordy {
  val functionByWord: Map[String, (Int, Int) => Int] = Map[String, (Int, Int) => Int](
    "plus" -> add,
    "minus" -> subtract,
    "multiplied" -> multiply,
    "divided" -> divide
  )

  def add(a: Int, b: Int): Int = a + b

  def subtract(a: Int, b: Int): Int = a - b

  def multiply(a: Int, b: Int): Int = a * b

  def divide(a: Int, b: Int): Int = a / b

  def answer(question: String): Option[Int] = {
    this.answer(question.split(" ").toList)
  }

  private def answer(words: List[String]): Option[Int] = {
    words match {
      case "What" :: "is" :: tail =>
        this.evaluate(tail)

      case _ =>
        None
    }
  }

  private def evaluate(words: List[String]): Option[Int] = {
    this.extract_first_words(words) match {
      case Some((firstNumber, operation, secondNumber, tail)) =>
        this.evaluate(firstNumber, operation, secondNumber, tail)

      case _ =>
        None
    }
  }

  private def extract_first_words(words: List[String]): Option[(Int, String, Int, List[String])] = {
    words match {
      case firstNumber :: operation :: secondNumber :: tail => {
        if (secondNumber == "by") {
          val secondNumber2 :: tail2 = tail
          Some((to_integer(firstNumber), operation, to_integer(secondNumber2), tail2))
        }
        else {
          Some((to_integer(firstNumber), operation, to_integer(secondNumber), tail))
        }
      }
      case _ =>
        None
    }
  }

  private def evaluate(
                        firstNumber: Int,
                        operation: String,
                        secondNumber: Int,
                        tail: List[String]
                      ): Option[Int] = {
    val result: Int = functionByWord(operation)(firstNumber, secondNumber)

    if (tail.nonEmpty) {
      this.answer(List("What", "is", result.toString) ::: tail)
    }
    else {
      Some(result)
    }
  }

  private def to_integer(value: String): Int = {
    value.replace("?", "")
      .toInt
  }
}
