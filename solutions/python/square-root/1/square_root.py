def square_root(number):
    lower_bound = 1
    upper_bound = number

    while lower_bound <= upper_bound:
        candidate = (lower_bound + upper_bound) // 2
        candidate_squared = candidate ** 2
        if candidate_squared == number:
            return candidate

        if candidate_squared < number:
            lower_bound = candidate + 1
        else:
            upper_bound = candidate - 1

    return None