import LocalDateExtensions.LocalDateOps
import Schedule.{Last, Schedule, Teenth}

import java.time.{DayOfWeek, LocalDate}

case class Meetup(month: Int, year: Int) {
  private val TeenthOffset = 13
  private val DaysInWeek = 7
  private val FirstDayOfMonth = 1

  def day(targetWeekday: Int, schedule: Schedule): LocalDate =
    schedule match {
      case Teenth => findDay(targetWeekday, TeenthOffset, x => x)
      case Last   => findLastDay(targetWeekday)
      case _      => findDay(targetWeekday, FirstDayOfMonth, x => findByIndexedOrdinal(schedule, x))
    }

  private def dayOfWeekToInt(day: DayOfWeek): Int = day.getValue

  private def findLastDay(targetWeekday: Int): LocalDate = {
    val fourthTargetDay = day(targetWeekday, Schedule.Fourth)
    val candidateDate   = fourthTargetDay.plusWeek
    if (month == candidateDate.getMonthValue) {
      candidateDate
    } else {
      fourthTargetDay
    }
  }

  private def findByIndexedOrdinal(schedule: Schedule.Value, dayDifference: Int): Int =
    DaysInWeek * schedule.id + dayDifference

  private def findDay(targetWeekday: Int, baseDay: Int, baseOffset: Int => Int): LocalDate = {
    val nthOfMonth    = LocalDate.of(year, month, baseDay)
    val nthDayNumber  = dayOfWeekToInt(nthOfMonth.getDayOfWeek)
    val dayDifference = targetWeekday - nthDayNumber

    val candidateDay = nthOfMonth.plusDays(baseOffset(dayDifference))
    if (nthDayNumber <= targetWeekday) {
      candidateDay
    } else {
      candidateDay.plusWeek
    }
  }
}

object Meetup {
  val Mon: Int = DayOfWeek.MONDAY.getValue
  val Tue: Int = DayOfWeek.TUESDAY.getValue
  val Wed: Int = DayOfWeek.WEDNESDAY.getValue
  val Thu: Int = DayOfWeek.THURSDAY.getValue
  val Fri: Int = DayOfWeek.FRIDAY.getValue
  val Sat: Int = DayOfWeek.SATURDAY.getValue
  val Sun: Int = DayOfWeek.SUNDAY.getValue
}
