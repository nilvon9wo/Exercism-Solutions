"""Determine the state of a Tic-Tac-Toe game."""

X = "X"  # pylint: disable=disallowed-name
O = "O"  # pylint: disable=disallowed-name
EMPTY = " "

ONGOING = "ongoing"
DRAW = "draw"
WIN = "win"

WINNING_LINES = (
    ((0, 0), (0, 1), (0, 2)),
    ((1, 0), (1, 1), (1, 2)),
    ((2, 0), (2, 1), (2, 2)),
    ((0, 0), (1, 0), (2, 0)),
    ((0, 1), (1, 1), (2, 1)),
    ((0, 2), (1, 2), (2, 2)),
    ((0, 0), (1, 1), (2, 2)),
    ((0, 2), (1, 1), (2, 0)),
)

# noinspection SpellCheckingInspection
def gamestate(board: list[str]) -> str:
    """Return the state of a Tic-Tac-Toe board."""
    x_count = _count_marks(board, X)
    o_count = _count_marks(board, O)

    _validate_turn_order(x_count, o_count)
    x_won = _has_won(board, X)
    o_won = _has_won(board, O)

    if x_won and o_won:
        raise ValueError("Impossible board: game should have ended after the game was won")

    if x_won:
        _validate_winning_move(board, X)
        return WIN

    if o_won:
        _validate_winning_move(board, O)
        return WIN

    if _is_full(board):
        return DRAW

    return ONGOING

def _count_marks(board: list[str], mark: str) -> int:
    """Return the number of occurrences of a mark."""
    return sum(row.count(mark) for row in board)

def _validate_turn_order(x_count: int, o_count: int) -> None:
    """Validate that X and O took turns correctly."""
    if o_count > x_count:
        raise ValueError("Wrong turn order: O started")

    if x_count > o_count + 1:
        raise ValueError("Wrong turn order: X went twice")

def _has_won(board: list[str], mark: str) -> bool:
    """Return whether a player has a winning line."""
    return any(
        _line_contains_mark(board, line, mark)
        for line in WINNING_LINES
    )

def _line_contains_mark(
    board: list[str],
    line: tuple[tuple[int, int], ...],
    mark: str,
) -> bool:
    """Return whether every position in a line contains the mark."""
    return all(
        board[row][column] == mark
        for row, column in line
    )

def _validate_winning_move(board: list[str], winner: str) -> None:
    """Validate that the winner's last move ended the game."""
    for row in range(3):
        for column in range(3):
            if board[row][column] == winner:
                previous_board = _remove_mark(board, row, column)
                if not _has_won(previous_board, winner):
                    return

    raise ValueError("Impossible board: game should have ended after the game was won")

def _remove_mark(board: list[str], row: int, column: int) -> list[str]:
    """Return a board with one mark removed."""
    previous_board = [list(board_row) for board_row in board]
    previous_board[row][column] = EMPTY
    return [
        "".join(board_row)
        for board_row in previous_board
    ]

def _is_full(board: list[str]) -> bool:
    """Return whether the board contains no empty cells."""
    return all(
            EMPTY not in row
            for row in board
    )
