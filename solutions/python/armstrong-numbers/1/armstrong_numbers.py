def get_digits(number):
    return [int(digit) for digit in str(number)]

def get_armstrong_sum(number):
    digits = get_digits(number)
    number_of_digits = len(digits)
    return sum(digit ** number_of_digits for digit in digits)

def is_armstrong_number(number):
    return get_armstrong_sum(number) == number