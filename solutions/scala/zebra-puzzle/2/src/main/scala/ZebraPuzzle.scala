import Constraints.{HouseRule, NeighborRule}

object ZebraPuzzle:
    case class Solution(waterDrinker: Nationality, zebraOwner: Nationality)

    def solve: Solution =
        Solution(
            waterDrinker = findNationalityFor(_.drink == Drink.Water),
            zebraOwner   = findNationalityFor(_.pet == Pet.Zebra)
        )

    private def findNationalityFor(predicate: Person => Boolean): Nationality =
        find(predicate).nationality

    private def find(predicate: Person => Boolean): Person =
        solution.find(predicate).get

    private lazy val solution: Iterable[Person] =
        val candidates: Array[Person] =
            PersonFactory
                .createAll()
                .filter(person => Constraints.HouseRules.forall(rule => Tester.test(person, rule)))
                .toArray

        PersonSetEnumerator
            .enumerate(candidates)
            .find(set => Constraints.NeighborRules.forall(rule => Tester.test(set, rule)))
            .get

enum Color:
    case Red, Green, Ivory, Yellow, Blue

enum Drink:
    case Coffee, Tea, Milk, OrangeJuice, Water

enum Hobby:
    case Dancing, Reading, Football, Chess, Painting

enum Nationality:
    case Englishman, Spaniard, Ukrainian, Norwegian, Japanese
export Nationality.*

enum Pet:
    case Dog, Snail, Fox, Horse, Zebra

enum House:
    case FarLeft, Left, Middle, Right, FarRight

enum Neighbor:
    case LeftOf, RightOf, Either

object Constraints {
    type HouseRule = (Any, Any, Boolean)

    val HouseRules: Array[HouseRule] =
        Array(
            (Nationality.Englishman, Color.Red, true), // The Englishman lives in the red house
            (Nationality.Spaniard, Pet.Dog, true), // The Spaniard owns the dog
            (Drink.Coffee, Color.Green, true), // Green house drinks coffee
            (Nationality.Ukrainian, Drink.Tea, true), // Ukrainian drinks tea
            (Hobby.Dancing, Pet.Snail, true), // Snail owner likes dancing
            (Color.Yellow, Hobby.Painting, true), // Yellow house is painter
            (Drink.Milk, House.Middle, true), // Middle house drinks milk
            (Nationality.Norwegian, House.FarLeft, true), // Norwegian in first house
            (Hobby.Reading, Pet.Fox, false), // Reading next to fox
            (Hobby.Painting, Pet.Horse, false), // Painter next to horse
            (Hobby.Football, Drink.OrangeJuice, true), // Football drinks orange juice
            (Nationality.Japanese, Hobby.Chess, true), // Japanese plays chess
            (Nationality.Norwegian, Color.Blue, false) // Norwegian next to blue house
        )

    type NeighborRule = (Any, Any, Neighbor)

    val NeighborRules: Array[NeighborRule] =
        Array(
            (Color.Green, Color.Ivory, Neighbor.RightOf), // green right of ivory
            (Hobby.Reading, Pet.Fox, Neighbor.Either), // reading next to fox
            (Hobby.Painting, Pet.Horse, Neighbor.Either), // painting next to horse
            (Nationality.Norwegian, Color.Blue, Neighbor.Either) // Norwegian next to blue
        )
}

final class Person(
                      val color: Color,
                      val nationality: Nationality,
                      val pet: Pet,
                      val drink: Drink,
                      val hobby: Hobby,
                      val house: House
                  ) {

    def noConflict(other: Person): Boolean = {
        require(other != null, "other")

        (this.color != other.color) &&
            (this.nationality != other.nationality) &&
            (this.pet != other.pet) &&
            (this.drink != other.drink) &&
            (this.hobby != other.hobby) &&
            (this.house != other.house)
    }
}

object PersonFactory {

    def createAll(): Iterator[Person] =
        createRange()
            .flatMap(createForColor)
            .map(identity)

    private def createForColor(color: Int): Iterator[Person] =
        createRange()
            .flatMap(nationality => createForNationality(color, nationality))

    private def createForNationality(color: Int, nationality: Int): Iterator[Person] =
        createRange()
            .flatMap(pet => createFor(color, nationality, pet))

    private def createFor(color: Int, nationality: Int, pet: Int): Iterator[Person] =
        createRange()
            .flatMap(drink => createForDrink(color, nationality, pet, drink))

    private def createForDrink(
                                  color: Int,
                                  nationality: Int,
                                  pet: Int,
                                  drink: Int
                              ): Iterator[Person] =
        createRange()
            .flatMap(hobby =>
                createHousePermutations(color, nationality, pet, drink, hobby)
            )

    private def createHousePermutations(
                                           color: Int,
                                           nationality: Int,
                                           pet: Int,
                                           drink: Int,
                                           hobby: Int
                                       ): Iterator[Person] =
        createRange()
            .map(house =>
                new Person(
                    Color.fromOrdinal(color),
                    Nationality.fromOrdinal(nationality),
                    Pet.fromOrdinal(pet),
                    Drink.fromOrdinal(drink),
                    Hobby.fromOrdinal(hobby),
                    House.fromOrdinal(house)
                )
            )

    private def createRange(): Iterator[Int] =
        Iterator.range(0, 5)
}

object PersonSetEnumerator {

    def enumerate(allCandidates: Array[Person]): Iterator[List[Person]] =
        enumerate(allCandidates, List.empty)

    private def enumerate(
                          allCandidates: Array[Person],
                          partialCandidateSet: List[Person]
                      ): Iterator[List[Person]] =
        val candidateSet: List[Person] = partialCandidateSet

        if candidateSet.size == 5
        then Iterator.single(candidateSet)
        else extendCandidates(candidateSet, allCandidates)

    private def extendCandidates(
                                existingCandidates: List[Person],
                                allCandidates: Array[Person]
                            ): Iterator[List[Person]] =
        allCandidates.iterator
                     .filter(candidate => isConsistent(existingCandidates, candidate))
                     .flatMap { candidate =>
                         enumerate(
                             allCandidates,
                             existingCandidates.appended(candidate)
                         )
                     }
                     .distinct

    private def isConsistent(
                                    existingPeople: List[Person],
                                    candidate: Person
                                ): Boolean =
        val existingArray = existingPeople.toArray
        !existingArray.contains(candidate) &&
            existingArray.forall(existing => existing.noConflict(candidate))
}

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


