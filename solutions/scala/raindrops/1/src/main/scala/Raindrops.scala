object Raindrops {
  val soundByFactors: Map[Int, String] = Map[Int, String](
    3 → "Pling",
    5 → "Plang",
    7 → "Plong"
  )

  val noSound = ""
  val isFactor = true
  val isNotFactor = false

  def convert(number: Int): String = {
    val sounds: String = soundByFactors.map(this.toSound(number, _))
      .filterNot(_ == noSound)
      .mkString("")

    if (!sounds.isBlank) {
      sounds
    }
    else {
      number.toString
    }
  }

  private def toSound(number: Int, soundKeyValue: (Int, String)): String = {
    val (key, sound) = soundKeyValue
    if (isFactor(number, key)) {
      sound
    }
    else {
      noSound
    }
  }

  private def isFactor(number: Int, value: Int) =
    number % value == 0
}

