BOARD_SIZE = 64
FIRST_SQUARE = 1
INITIAL_GRAINS = 1
DOUBLING_BASE = 2

def validate_square(square_number):
    if (
            square_number < FIRST_SQUARE
            or square_number > BOARD_SIZE
    ):
        raise ValueError("square must be between 1 and 64")

def square(square_number):
    validate_square(square_number)
    return INITIAL_GRAINS * DOUBLING_BASE ** (square_number - FIRST_SQUARE)

def total():
    return DOUBLING_BASE ** BOARD_SIZE - INITIAL_GRAINS