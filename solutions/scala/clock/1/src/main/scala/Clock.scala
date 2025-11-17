case class Clock(hour: Int, minutes: Int) {
  def +(other: Clock): Clock = combineClocks(other, (a, b) => a + b)

  def -(other: Clock): Clock = combineClocks(other, (a, b) => a - b)

  private def combineClocks(other: Clock, operation: (Int, Int) => Int): Clock = {
    val totalMinutes = operation(this.minutes, other.minutes) + operation(this.hour, other.hour) * 60
    Clock(totalMinutes)
  }
}

object Clock {
  private val MinutesPerHour = 60
  private val HoursPerDay    = 24
  private val MinutesPerDay: Int = MinutesPerHour * HoursPerDay

  def apply(hour: Int, minutes: Int): Clock = {
    def totalMinutes = hour * MinutesPerHour + minutes
    def normalizedMinutes = ((totalMinutes % MinutesPerDay) + MinutesPerDay) % MinutesPerDay % MinutesPerHour
    def normalizedHour = ((totalMinutes % MinutesPerDay) + MinutesPerDay) % MinutesPerDay / MinutesPerHour

    new Clock(normalizedHour, normalizedMinutes)
  }

  def apply(minutes: Int): Clock = apply(0, minutes)
}
