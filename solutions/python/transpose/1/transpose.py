"""Transpose text."""

def transpose(text: str) -> str:
    """Return the transposed representation of the input text."""
    if not text:
        return ""

    rows = text.split("\n")
    maximum_length = max(len(row) for row in rows)
    transposed_rows: list[str] = []
    for column in range(maximum_length):
        transposed_rows.append(_transpose_column(rows, column))

    return "\n".join(transposed_rows)


def _transpose_column(rows: list[str], column: int) -> str:
    """Transpose one column of the input rows."""
    last_row = _find_last_row_with_character(rows, column)
    characters: list[str] = []
    for row in range(last_row + 1):
        if column < len(rows[row]):
            characters.append(rows[row][column])
        else:
            characters.append(" ")

    return "".join(characters)


def _find_last_row_with_character(rows: list[str], column: int) -> int:
    """Return the last row containing a character at the given column."""
    for row in range(len(rows) - 1, -1, -1):
        if column < len(rows[row]):
            return row

    return -1
