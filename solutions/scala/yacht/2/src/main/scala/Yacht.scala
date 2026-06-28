object Yacht {
    private val littleStraight = List(1, 2, 3, 4, 5)
    private val bigStraight = List(2, 3, 4, 5, 6)
    def score(dice: List[Int], category: String): Int =
        category match
            case "ones"   => sumOfFace(dice, 1)
            case "twos"   => sumOfFace(dice, 2)
            case "threes" => sumOfFace(dice, 3)
            case "fours"  => sumOfFace(dice, 4)
            case "fives"  => sumOfFace(dice, 5)
            case "sixes"  => sumOfFace(dice, 6)
            case "choice" => dice.sum
            case "yacht" => scoreYacht(dice)
            case "full house" => scoreFullHouse(dice)
            case "four of a kind" => scoreFourOfKind(dice)
            case "little straight" => scoreStraight(littleStraight, dice)
            case "big straight" => scoreStraight(bigStraight, dice)
            case _ => 0

    private def scoreYacht(dice: List[Int]) =
        if dice.distinct.size == 1
        then 50
        else 0

    private def scoreFullHouse(dice: List[Int]) =
        val grouped = dice.groupBy(identity)
                          .values.map(_.size)
                          .toList.sorted
        if grouped == List(2, 3)
        then dice.sum
        else 0

    private def scoreFourOfKind(dice: List[Int]) =
        dice.groupBy(identity)
            .values
            .find(_.size >= 4)
            .map(g => g.head * 4)
            .getOrElse(0)

    private def scoreStraight(target: List[Int], dices: List[Int]): Int =
        if dices.sorted == target
        then 30
        else 0

    private def sumOfFace(dices: List[Int], face: Int): Int =
        dices.count(_ == face) * face
}