import scala.util.Random

object RandomKeyGenerator {
  private val allowedChars: String = "abcdefghijklmnopqrstuvwxyz"
  private val random: Random = new Random()
  private val keyLength = 100

  def create: String =
    (0 to keyLength)
      .map(_ => allowedChars.charAt(random.nextInt(allowedChars.length)))
      .mkString
}
