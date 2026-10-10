"""Provide functionality for determining whether chess queens can attack each other."""
BOARD_SIZE = 8

# pylint: disable=too-few-public-methods
class Queen:
    """Represent a queen on an 8 by 8 chess board."""
    column: int
    row: int

    def __init__(self, row: int, column: int):
        if row < 0:
            raise ValueError("row not positive")

        if row >= BOARD_SIZE:
            raise ValueError("row not on board")

        if column < 0:
            raise ValueError("column not positive")

        if column >= BOARD_SIZE:
            raise ValueError("column not on board")

        self.row = row
        self.column = column

    def can_attack(self, other: Queen) -> bool:
        """Return whether this queen can attack another queen."""
        if self.row == other.row and self.column == other.column:
            raise ValueError("Invalid queen position: both queens in the same square")

        same_row = self.row == other.row
        same_column = self.column == other.column
        same_diagonal = abs(self.row - other.row) == abs(self.column - other.column)
        return (
                same_row
                or same_column
                or same_diagonal
        )
