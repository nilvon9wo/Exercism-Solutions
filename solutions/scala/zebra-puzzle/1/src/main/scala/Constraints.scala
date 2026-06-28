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
