def sum_of_multiples(level, factors):
    multiples = set()
    for factor in factors:
        multiples.update(_get_multiples_below_limit(factor, level))

    return sum(multiples)

def _get_multiples_below_limit(factor, level):
    if factor == 0:
        return set()

    multiples = set()
    multiple = factor
    while multiple < level:
        multiples.add(multiple)
        multiple += factor

    return multiples
