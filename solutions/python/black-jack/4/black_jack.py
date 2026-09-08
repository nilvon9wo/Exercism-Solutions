"""Functions for playing a game of Blackjack."""


def value_of_card(card):
    """Return the numerical value of a card."""
    if card in {"J", "Q", "K"}:
        return 10

    if card == "A":
        return 1

    return int(card)


def higher_card(card_one, card_two):
    """Return the card with the higher Blackjack value."""
    value_one = value_of_card(card_one)
    value_two = value_of_card(card_two)

    if value_one == value_two:
        return card_one, card_two

    if value_one > value_two:
        return card_one

    return card_two


def value_of_ace(card_one, card_two):
    """Return the value of an ace based on the existing hand."""
    if "A" in {card_one, card_two}:
        return 1

    if value_of_card(card_one) + value_of_card(card_two) <= 10:
        return 11

    return 1


def is_blackjack(card_one, card_two):
    """Return whether the two cards form a Blackjack."""
    has_ace = card_one == "A" or card_two == "A"
    return has_ace and has_ten_value_card(card_one, card_two)


def has_ten_value_card(card_one, card_two) -> bool:
    cards = [card_one, card_two]
    card_iterator = (value_of_card(card) for card in cards)
    return 10 in card_iterator


def can_split_pairs(card_one, card_two):
    """Return whether the two cards can be split into separate hands."""
    return value_of_card(card_one) == value_of_card(card_two)


def can_double_down(card_one, card_two):
    """Return whether the hand can be doubled down."""
    hand_value = value_of_card(card_one) + value_of_card(card_two)

    return 9 <= hand_value <= 11