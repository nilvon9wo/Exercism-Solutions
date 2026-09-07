def exchange_money(budget, exchange_rate):
    """Calculate the estimated value after exchange."""
    return budget / exchange_rate


def get_change(budget, exchanging_value):
    """Calculate the amount left after an exchange."""
    return budget - exchanging_value


def get_value_of_bills(denomination, number_of_bills):
    """Calculate the total value of the bills."""
    return denomination * number_of_bills


def get_number_of_bills(amount, denomination):
    """Calculate the number of whole bills."""
    return int(amount // denomination)


def get_leftover_of_bills(amount, denomination):
    """Calculate the amount left after exchanging into whole bills."""
    return amount % denomination


def exchangeable_value(budget, exchange_rate, spread, denomination):
    """Calculate the maximum value of the new currency."""
    actual_exchange_rate = exchange_rate * (1 + spread / 100)
    exchanged_amount = budget / actual_exchange_rate
    return int(exchanged_amount // denomination) * denomination