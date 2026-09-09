ROMAN_SYMBOLS = {
    1: "I",
    5: "V",
    10: "X",
    50: "L",
    100: "C",
    500: "D",
    1000: "M",
}

BASE = 10
FIVE = 5
MAX_REPETITIONS = 3

def roman(number):
    roman_numeral = ""
    place_value = 1
    while number:
        digit = number % BASE
        roman_numeral = _convert_digit(digit, place_value) + roman_numeral
        number //= BASE
        place_value *= BASE

    return roman_numeral

def _convert_digit(digit, place_value):
    one_value = place_value
    five_value = place_value * FIVE
    ten_value = place_value * BASE

    one_symbol = ROMAN_SYMBOLS[one_value]
    if digit <= MAX_REPETITIONS:
        return one_symbol * digit

    five_symbol = ROMAN_SYMBOLS[five_value]
    if digit == FIVE - 1:
        return one_symbol + five_symbol

    if digit < BASE - 1:
        return five_symbol + one_symbol * (digit - FIVE)

    ten_symbol = ROMAN_SYMBOLS[ten_value]
    return one_symbol + ten_symbol