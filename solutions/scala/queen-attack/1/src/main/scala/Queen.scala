case class Queen(x: Int, y: Int) {
  def canAttack(that: Queen): Boolean =
    x == that.x ||
    y == that.y ||
    (x - that.x).abs == (y - that.y).abs
}

object Queen {
  def create(x: Int, y: Int): Option[Queen] =
    if (!(x >= 0) || !(x < 8) || !(y >= 0)|| !(y < 8)) {
      None
    }
    else {
      Some(new Queen(x, y))
    }
}
