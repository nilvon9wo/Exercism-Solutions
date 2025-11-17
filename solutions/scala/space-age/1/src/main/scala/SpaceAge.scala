import Planet.Planet

object SpaceAge {
  private val secondsPerEarthYear               = 31_557_600
  private val orbitalPeriodInEarthYearsByPlanet = Map(
    Planet.Mercury -> 0.2408467,
    Planet.Venus -> 0.61519726,
    Planet.Mars -> 1.8808158,
    Planet.Earth -> 1,
    Planet.Jupiter -> 11.862615,
    Planet.Saturn -> 29.447498,
    Planet.Uranus -> 84.016846,
    Planet.Neptune -> 164.79132,
    )

  def onVenus(seconds: Double): Double = ageOn(seconds, Planet.Venus)

  def onMercury(seconds: Double): Double = ageOn(seconds, Planet.Mercury)

  def onMars(seconds: Double): Double = ageOn(seconds, Planet.Mars)

  def onJupiter(seconds: Double): Double = ageOn(seconds, Planet.Jupiter)

  def onSaturn(seconds: Double): Double = ageOn(seconds, Planet.Saturn)

  def onUranus(seconds: Double): Double = ageOn(seconds, Planet.Uranus)

  def onNeptune(seconds: Double): Double = ageOn(seconds, Planet.Neptune)

  private def ageOn(age: Double, planet: Planet): Double = {
    val earthAge = onEarth(age)
    orbitalPeriodInEarthYearsByPlanet.get(planet) match {
      case Some(orbitalPeriod: Double) => earthAge / orbitalPeriod
      case _                           => earthAge
    }
  }

  def onEarth(seconds: Double): Double = seconds / secondsPerEarthYear
}

object Planet
  extends Enumeration {
  type Planet = Value
  val Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune = Value
}
