import scala.concurrent.duration.Duration
import scala.concurrent.Await

object Frequency {
  def frequency(numberOfWorkers: Int, texts: Seq[String]): Map[Char, Int] = {
    val processedTexts = new TextProcessor(numberOfWorkers)
      .processTexts(texts)
    Await.result(processedTexts , Duration.Inf)
  }
}
