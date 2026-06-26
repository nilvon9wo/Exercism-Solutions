import Constraints.{HouseRule, NeighborRule}

object Tester {

    private def applies(person: Person, aspect: Any): Boolean =
        aspect match {
            case color: Color =>
                person.color == color
            case nationality: Nationality =>
                person.nationality == nationality
            case pet: Pet =>
                person.pet == pet
            case drink: Drink =>
                person.drink == drink
            case hobby: Hobby =>
                person.hobby == hobby
            case house: House =>
                person.house == house
            case _ =>
                person.house == aspect.asInstanceOf[House]
        }

    def test(
                person: Person,
                rule: HouseRule
            ): Boolean = {

        val subject = rule._1
        val fact    = rule._2
        val isTrue  = rule._3

        if applies(person, subject) then
            (isTrue && applies(person, fact)) || (!isTrue && !applies(person, fact))
        else
            (isTrue && !applies(person, fact)) || !isTrue
    }

    def test(
                set: Iterable[Person],
                rule: NeighborRule
            ): Boolean = {

        val persons = set.toArray
        val sorted  = persons.sortBy(_.house.ordinal)

        val pairs =
            sorted.zip(sorted.drop(1))

        rule._3 match {

            case Neighbor.LeftOf =>
                pairs.exists { case (left, right) =>
                    applies(left, rule._1) && applies(right, rule._2)
                }

            case Neighbor.RightOf =>
                pairs.exists { case (left, right) =>
                    applies(right, rule._1) && applies(left, rule._2)
                }

            case _ =>
                test(set, (rule._1, rule._2, Neighbor.LeftOf)) ||
                    test(set, (rule._1, rule._2, Neighbor.RightOf))
        }
    }
}