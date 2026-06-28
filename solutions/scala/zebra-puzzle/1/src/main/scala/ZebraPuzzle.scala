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

