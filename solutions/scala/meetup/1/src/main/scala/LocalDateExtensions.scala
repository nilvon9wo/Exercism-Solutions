import java.time.LocalDate

object LocalDateExtensions {
  implicit class LocalDateOps(date: LocalDate) {
    def plusWeek: LocalDate = date.plusWeeks(1)
  }
}
