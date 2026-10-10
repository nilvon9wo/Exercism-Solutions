"""Provide functionality for working with dice."""
class Dice:
    """Represent a collection of dice."""
    values: list[int]

    def __init__(self, values: list[int]):
        self.values = values

    def count(self, value: int) -> int:
        """Return the number of dice showing the specified value."""
        return self.values.count(value)

    def total(self) -> int:
        """Return the total value of all dice."""
        return sum(self.values)

    def has_count(self, count: int) -> bool:
        """Return whether any face occurs the specified number of times."""
        return any(
            self.count(value) == count
            for value in range(1, 7)
        )
