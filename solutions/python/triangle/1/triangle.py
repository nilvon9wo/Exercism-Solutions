def is_valid_triangle(sides):
    first_side, second_side, third_side = sides
    has_nonpositive_side = (
            first_side <= 0
            or second_side <= 0
            or third_side <= 0
    )
    if has_nonpositive_side:
        return False

    return (
            first_side + second_side >= third_side
            and second_side + third_side >= first_side
            and first_side + third_side >= second_side
    )

def equilateral(sides):
    if not is_valid_triangle(sides):
        return False

    first_side, second_side, third_side = sides
    return first_side == second_side == third_side


def isosceles(sides):
    if not is_valid_triangle(sides):
        return False

    first_side, second_side, third_side = sides
    return (
        first_side == second_side
        or first_side == third_side
        or second_side == third_side
    )


def scalene(sides):
    if not is_valid_triangle(sides):
        return False

    first_side, second_side, third_side = sides
    return (
        first_side != second_side
        and first_side != third_side
        and second_side != third_side
    )