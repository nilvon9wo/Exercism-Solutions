CLASSIFICATION_ERROR_MESSAGE = (
    "Classification is only possible for positive integers."
)

PERFECT = "perfect"
ABUNDANT = "abundant"
DEFICIENT = "deficient"

FIRST_PROPER_DIVISOR = 1
FIRST_DIVISOR_TO_TEST = 2

def validate_number(number):
    if number <= 0:
        raise ValueError(CLASSIFICATION_ERROR_MESSAGE)

def get_aliquot_sum(number):
    return sum(_get_proper_divisors(number))

def _get_proper_divisors(number):
    if number == FIRST_PROPER_DIVISOR:
        return []

    divisors = [FIRST_PROPER_DIVISOR]
    divisor = FIRST_DIVISOR_TO_TEST
    while divisor ** 2 <= number:
        if _is_divisor(number, divisor):
            divisors.extend(_get_divisor_pair(number, divisor))

        divisor += 1

    return divisors

def _is_divisor(number, candidate):
    return number % candidate == 0

def _get_divisor_pair(number, divisor):
    paired_divisor = number // divisor
    if paired_divisor == divisor:
        return [divisor]

    return [divisor, paired_divisor]

def classify(number):
    validate_number(number)
    aliquot_sum = get_aliquot_sum(number)

    if aliquot_sum == number:
        return PERFECT

    if aliquot_sum > number:
        return ABUNDANT

    return DEFICIENT