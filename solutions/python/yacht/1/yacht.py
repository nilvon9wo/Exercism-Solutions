"""Score Yacht dice rolls."""
from dice import Dice  # pyright: ignore[reportImplicitRelativeImport]

ONES = "ones"
TWOS = "twos"
THREES = "threes"
FOURS = "fours"
FIVES = "fives"
SIXES = "sixes"
FULL_HOUSE = "full house"
FOUR_OF_A_KIND = "four of a kind"
LITTLE_STRAIGHT = "little straight"
BIG_STRAIGHT = "big straight"
CHOICE = "choice"
YACHT = "yacht"

YACHT_SIZE = 5
YACHT_SCORE = 50
STRAIGHT_SCORE = 30
FOUR_OF_A_KIND_SIZE = 4

NUMBER_CATEGORIES = {
    ONES: 1,
    TWOS: 2,
    THREES: 3,
    FOURS: 4,
    FIVES: 5,
    SIXES: 6,
}

def score(dice: list[int], category: str) -> int:
    """Return the score for the dice in the specified category."""
    dice_roll = Dice(dice)

    if category in NUMBER_CATEGORIES:
        return _score_number(dice_roll, NUMBER_CATEGORIES[category])

    if category == FULL_HOUSE:
        return _score_full_house(dice_roll)

    if category == FOUR_OF_A_KIND:
        return _score_four_of_a_kind(dice_roll)

    if category == LITTLE_STRAIGHT:
        return _score_straight(dice_roll, 1, 5)

    if category == BIG_STRAIGHT:
        return _score_straight(dice_roll, 2, 6)

    if category == CHOICE:
        return dice_roll.total()

    if category == YACHT:
        return _score_yacht(dice_roll)

    return 0

def _score_number(dice: Dice, value: int) -> int:
    """Score a numbered category."""
    return value * dice.count(value)

def _score_full_house(dice: Dice) -> int:
    """Score a full house."""
    if dice.has_count(2) and dice.has_count(3):
        return dice.total()

    return 0

def _score_four_of_a_kind(dice: Dice) -> int:
    """Score four of a kind."""
    for value in range(1, 7):
        if dice.count(value) >= FOUR_OF_A_KIND_SIZE:
            return value * FOUR_OF_A_KIND_SIZE

    return 0

def _score_straight(dice: Dice, first_value: int, last_value: int) -> int:
    """Score a straight."""
    for value in range(first_value, last_value + 1):
        if dice.count(value) != 1:
            return 0

    return STRAIGHT_SCORE

def _score_yacht(dice: Dice) -> int:
    """Score a Yacht."""
    if dice.has_count(YACHT_SIZE):
        return YACHT_SCORE

    return 0
