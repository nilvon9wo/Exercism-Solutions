ORDINAL_SUFFIXES = {
    1: "st",
    2: "nd",
    3: "rd",
}

def get_ordinal_suffix(number):
    last_two_digits = number % 100
    if 11 <= last_two_digits <= 13:
        return "th"

    last_digit = number % 10
    return ORDINAL_SUFFIXES.get(last_digit, "th")

def line_up(name, number):
    ordinal_suffix = get_ordinal_suffix(number)
    return (
        f"{name}, you are the {number}{ordinal_suffix} "
        "customer we serve today. Thank you!"
    )