"""Functions for working with lists of card game rounds and hands."""


def get_rounds(round_number):
    """Return the current round and the next two rounds."""
    return [round_number, round_number + 1, round_number + 2]


def concatenate_rounds(rounds_1, rounds_2):
    """Return the two round lists concatenated together."""
    return rounds_1 + rounds_2


def list_contains_round(rounds, round_number):
    """Return whether the specified round is in the list."""
    return round_number in rounds


def card_average(hand):
    """Return the average value of the cards in the hand."""
    return sum(hand) / len(hand)


def approx_average_is_average(hand):
    """Return whether either approximate average equals the actual average."""
    actual_average = card_average(hand)
    first_and_last_average = (hand[0] + hand[-1]) / 2
    median = hand[len(hand) // 2]

    return actual_average == first_and_last_average or actual_average == median


def average_even_is_average_odd(hand):
    """Return whether the even-index and odd-index averages are equal."""
    even_average = sum(hand[::2]) / len(hand[::2])
    odd_average = sum(hand[1::2]) / len(hand[1::2])

    return even_average == odd_average


def maybe_double_last(hand):
    """Double the last card if it is a Jack."""
    if hand[-1] == 11:
        hand[-1] *= 2

    return hand