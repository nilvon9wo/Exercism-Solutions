import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TextProcessor(numberOfWorkers: Int) {
  def processTexts(texts: Seq[String]): Future[Map[Char, Int]] = {
    val textChunks = createChunks(texts)
    val processedText = parallelProcess(textChunks)
    combine(processedText)
  }

  private def createChunks(texts: Seq[String]): List[Seq[String]] = {
    if (texts.nonEmpty && numberOfWorkers > 0) {
      val totalTexts = texts.size + numberOfWorkers - 1
      texts.grouped(totalTexts / numberOfWorkers).toList
    } else {
      List.empty
    }
  }

  private def parallelProcess(textChunks: List[Seq[String]]): Seq[Future[Map[Char, Int]]] =
    textChunks.map(processChunk)

  private def processChunk(chunk: Seq[String]): Future[Map[Char, Int]] =
    Future {
             chunk
               .flatMap(Extractor.extractLetters)
               .groupBy(identity)
               .view.mapValues(_.size)
               .toMap
           }

  private def combine(futures: Seq[Future[Map[Char, Int]]]): Future[Map[Char, Int]] =
    Future.foldLeft(futures)(Map.empty[Char, Int]) {
                                                     case (accumulator, frequencyByCharacters) =>
                                                       combineMaps(accumulator, frequencyByCharacters)
                                                   }

  private def combineMaps(map1: Map[Char, Int], map2: Map[Char, Int]): Map[Char, Int] =
    (map1.keySet ++ map2.keySet)
      .filterNot(_.isWhitespace)
      .map(Extractor.combineCharacterFrequency(map1, map2))
      .toMap
}
