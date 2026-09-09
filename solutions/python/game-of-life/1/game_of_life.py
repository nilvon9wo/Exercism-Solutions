"""Implement Conway's Game of Life."""
LIVE = 1
DEAD = 0

MIN_LIVE_NEIGHBORS = 2
MAX_LIVE_NEIGHBORS = 3
REQUIRED_LIVE_NEIGHBORS_FOR_BIRTH = 3

NEIGHBOR_OFFSETS = (
    (-1, -1),
    (-1, 0),
    (-1, 1),
    (0, -1),
    (0, 1),
    (1, -1),
    (1, 0),
    (1, 1),
)

def tick(matrix: list[list[int]]) -> list[list[int]]:
    """Return the next generation of the Game of Life."""
    next_generation: list[list[int]] = []

    for row in range(len(matrix)):
        next_row = _get_next_row(matrix, row)
        next_generation.append(next_row)

    return next_generation

def _get_next_row(matrix: list[list[int]], row: int) -> list[int]:
    """Return the next generation of one row."""
    next_row: list[int] = []
    for column in range(len(matrix[row])):
        next_cell = _get_next_cell(matrix, row, column)
        next_row.append(next_cell)

    return next_row

def _get_next_cell(matrix: list[list[int]], row: int, column: int) -> int:
    """Return the next state of one cell."""
    live_neighbors = _count_live_neighbors(matrix, row, column)
    cell = matrix[row][column]

    if cell == LIVE:
        if MIN_LIVE_NEIGHBORS <= live_neighbors <= MAX_LIVE_NEIGHBORS:
            return LIVE

        return DEAD

    if live_neighbors == REQUIRED_LIVE_NEIGHBORS_FOR_BIRTH:
        return LIVE

    return DEAD

def _count_live_neighbors(
    matrix: list[list[int]],
    row: int,
    column: int,
) -> int:
    """Return the number of live neighboring cells."""
    return sum(
        _get_neighbor(
                matrix, row + row_offset,
                column + column_offset
        )
        for row_offset, column_offset in NEIGHBOR_OFFSETS
    )

def _get_neighbor(
    matrix: list[list[int]],
    row: int,
    column: int,
) -> int:
    """Return a neighboring cell, or DEAD when outside the matrix."""
    if row < 0 or row >= len(matrix):
        return DEAD

    if column < 0 or column >= len(matrix[row]):
        return DEAD

    return matrix[row][column]
