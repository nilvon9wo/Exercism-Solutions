# ruff: noqa: N803, N806
INNER_RADIUS = 1
MIDDLE_RADIUS = 5
OUTER_RADIUS = 10

INNER_SCORE = 10
MIDDLE_SCORE = 5
OUTER_SCORE = 1
MISS_SCORE = 0

def get_distance_squared_from_center(x, y):
    return x ** 2 + y ** 2

def score(x, y):
    distance_squared = get_distance_squared_from_center(x, y)

    if distance_squared <= INNER_RADIUS ** 2:
        return INNER_SCORE

    if distance_squared <= MIDDLE_RADIUS ** 2:
        return MIDDLE_SCORE

    if distance_squared <= OUTER_RADIUS ** 2:
        return OUTER_SCORE

    return MISS_SCORE