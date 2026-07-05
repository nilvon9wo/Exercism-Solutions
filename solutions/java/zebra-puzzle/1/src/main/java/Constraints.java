import java.util.List;

final class Constraints {
    static final List<HouseRule> HOUSE_RULES = List.of(
            new HouseRule(Nationality.Englishman, Color.Red, true), // The Englishman lives in the red house
            new HouseRule(Nationality.Spaniard, Pet.Dog, true), // The Spaniard owns the dog
            new HouseRule(Drink.Coffee, Color.Green, true), // Green house drinks coffee
            new HouseRule(Nationality.Ukranian, Drink.Tea, true), // Ukrainian drinks tea
            new HouseRule(Hobby.Dancing, Pet.Snails, true), // Snail owner likes to go dancing
            new HouseRule(Color.Yellow, Hobby.Painting, true), // Yellow house is painter
            new HouseRule(Drink.Milk, House.Middle, true), // Middle house drinks milk
            new HouseRule(Nationality.Norwegian, House.FarLeft, true), // Norwegian in first house
            new HouseRule(Hobby.Reading, Pet.Fox, false), // Reading next to fox
            new HouseRule(Hobby.Painting, Pet.Horse, false), // Painter next to horse
            new HouseRule(Hobby.Football, Drink.OrangeJuice, true), // Football drinks orange juice
            new HouseRule(Nationality.Japanese, Hobby.Chess, true), // Japanese plays chess
            new HouseRule(Nationality.Norwegian, Color.Blue, false) // Norwegian next to blue house
    );

    static final List<NeighborRule>  NEIGHBOR_RULES = List.of(
            new NeighborRule(Color.Green, Color.Ivory, Neighbor.RightOf), // green is right of ivory
            new NeighborRule(Hobby.Reading, Pet.Fox, Neighbor.Either), // reading next to fox
            new NeighborRule(Hobby.Painting, Pet.Horse, Neighbor.Either), // painter next to horse
            new NeighborRule(Nationality.Norwegian, Color.Blue, Neighbor.Either) // Norwegian next to blue
    );
}