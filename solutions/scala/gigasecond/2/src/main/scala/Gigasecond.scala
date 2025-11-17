import java.time.{LocalDate, LocalDateTime}

object Gigasecond {
  val secondsPerGigasecond = 1000000000

  def add(startDate: LocalDate): LocalDateTime =
    this.add(startDate.atTime(0, 0))

  def add(startDateTime: LocalDateTime): LocalDateTime =
    startDateTime.plusSeconds(secondsPerGigasecond)
}
