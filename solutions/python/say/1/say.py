ONES = (
    "zero", "one", "two", "three", "four",
    "five", "six", "seven", "eight", "nine",
)

TEENS = (
    "ten", "eleven", "twelve", "thirteen", "fourteen",
    "fifteen", "sixteen", "seventeen", "eighteen", "nineteen",
)

TENS = (
    "", "", "twenty", "thirty", "forty",
    "fifty", "sixty", "seventy", "eighty", "ninety",
)

SCALES = ("", "thousand", "million", "billion")

GROUP_SIZE = 1000
MAXIMUM = 999_999_999_999

def say(number):
    if number < 0 or number > MAXIMUM:
        raise ValueError("input out of range")

    if number == 0:
        return "zero"

    words = []
    scale = 0
    while number:
        group = number % GROUP_SIZE
        if group:
            words.append(_say_group(group, SCALES[scale]))

        number //= GROUP_SIZE
        scale += 1

    return " ".join(reversed(words))

def _split_into_groups(number):
    groups = []
    scale = 0
    while number:
        groups.append((number % GROUP_SIZE, scale))
        number //= GROUP_SIZE
        scale += 1

    return groups

def _say_group(number, scale):
    words = []
    if number >= 100:
        words.append(f"{ONES[number // 100]} hundred")
        number %= 100

    if number:
        words.append(_say_below_hundred(number))

    if scale:
        words.append(scale)

    return " ".join(words)

def _say_below_hundred(number):
    if number < 10:
        return ONES[number]

    if number < 20:
        return TEENS[number - 10]

    tens = TENS[number // 10]
    ones = number % 10
    if not ones:
        return tens

    return  f"{tens}-{ONES[ones]}"

def _get_scale(scale):
    if SCALES[scale]:
        return f" {SCALES[scale]}"

    return ""