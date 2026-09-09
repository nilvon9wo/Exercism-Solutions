FIRST_PRIME = 2
FIRST_CANDIDATE = 3
DIVISOR_INCREMENT = 2

def prime(number):
    if number < 1:
        raise ValueError("there is no zeroth prime")

    if number == 1:
        return FIRST_PRIME

    prime_count = 1
    candidate = FIRST_CANDIDATE
    while prime_count < number:
        if _is_prime(candidate):
            prime_count += 1

        if prime_count == number:
            return candidate

        candidate += DIVISOR_INCREMENT

    return None

def _is_prime(number):
    divisor = FIRST_PRIME
    while divisor * divisor <= number:
        if number % divisor == 0:
            return False

        divisor += 1

    return True