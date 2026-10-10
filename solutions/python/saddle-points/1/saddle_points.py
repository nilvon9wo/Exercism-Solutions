"""Find saddle points in a matrix."""
type Matrix = list[list[int]]
type Point = dict[str, int]

def saddle_points(matrix: Matrix) -> list[Point]:
    """Return all saddle points in the matrix."""
    if not matrix:
        return []

    column_count = len(matrix[0])
    validate_matrix(matrix, column_count)
    row_maximums = get_row_maximums(matrix)
    column_minimums = get_column_minimums(matrix)
    return find_saddle_points(matrix, row_maximums, column_minimums)

def validate_matrix(matrix: Matrix, column_count: int) -> None:
    """Raise ValueError if the matrix has rows of different lengths."""
    if any(
            len(row) != column_count
            for row in matrix
    ):
        raise ValueError("irregular matrix")

def get_row_maximums(matrix: Matrix) -> list[int]:
    """Return the maximum value from each row."""
    return [
        max(row)
        for row in matrix
    ]

def get_column_minimums(matrix: Matrix) -> list[int]:
    """Return the minimum value from each column."""
    column_count = len(matrix[0])

    return [
        min(
                row[column_index]
                for row in matrix
        )
        for column_index in range(column_count)
    ]

def find_saddle_points(
    matrix: Matrix,
    row_maximums: list[int],
    column_minimums: list[int],
) -> list[Point]:
    """Find all values that are both row maximums and column minimums."""
    points: list[Point] = []
    for row_index, row in enumerate(matrix):
        for column_index, value in enumerate(row):
            if is_saddle_point(
                value,
                row_maximums[row_index],
                column_minimums[column_index],
            ):
                points.append(create_point(row_index, column_index))

    return points

def is_saddle_point(
    value: int,
    row_maximum: int,
    column_minimum: int,
) -> bool:
    """Return whether a value is both a row maximum and column minimum."""
    return (
            value == row_maximum
            and value == column_minimum
    )

def create_point(row_index: int, column_index: int) -> Point:
    """Create a one-based matrix coordinate."""
    return {
        "row": row_index + 1,
        "column": column_index + 1,
    }
