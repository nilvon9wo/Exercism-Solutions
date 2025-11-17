import java.time.{LocalDate, LocalDateTime}

object Gigasecond {

  val secondsPerGigasecond = 1000000000

  def add(startDate: LocalDate): LocalDateTime =
    startDate.atTime(0, 0)
      .plusSeconds(secondsPerGigasecond)

  def add(startDateTime: LocalDateTime): LocalDateTime =
    startDateTime.plusSeconds(secondsPerGigasecond)
}
