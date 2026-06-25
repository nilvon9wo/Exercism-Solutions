enum Color(val token: Char):
    case Black extends Color('X')
    case White extends Color('O')

object Color:
    def fromChar(character: Char): Option[Color] =
        values.find(_.token == character)