EVEN_DIVISOR = 2
ODD_MULTIPLIER = 3
ODD_INCREMENT = 1
TARGET_NUMBER = 1
FIRST_VALID_NUMBER = 1

def steps(number):
    _validate_starting_number(number)
    step_count = 0

    while number != TARGET_NUMBER:
        number = _next_collatz_number(number)
        step_count += 1

    return step_count

def _validate_starting_number(number):
    if number < FIRST_VALID_NUMBER:
        raise ValueError("Only positive integers are allowed")

def _next_collatz_number(number):
    if number % EVEN_DIVISOR == 0:
        return number // EVEN_DIVISOR

    return number * ODD_MULTIPLIER + ODD_INCREMENT
