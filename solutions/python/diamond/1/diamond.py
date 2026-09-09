FIRST_LETTER = "A"
FIRST_LETTER_CODE = ord(FIRST_LETTER)

def rows(letter):
    last_letter_index = _get_letter_index(letter)
    top_half = _build_top_half(last_letter_index)
    bottom_half = list(reversed(top_half[:-1]))
    return top_half + bottom_half

def _get_letter_index(letter):
    return ord(letter) - FIRST_LETTER_CODE

def _build_top_half(last_letter_index):
    diamond_rows = []
    for letter_index in range(last_letter_index + 1):
        diamond_rows.append(
            _build_row(letter_index, last_letter_index)
        )

    return diamond_rows

def _build_row(letter_index, last_letter_index):
    outer_spaces = last_letter_index - letter_index
    if letter_index == 0:
        return " " * outer_spaces + FIRST_LETTER + " " * outer_spaces

    inner_spaces = letter_index * 2 - 1
    letter = chr(FIRST_LETTER_CODE + letter_index)
    return (
        " " * outer_spaces
        + letter
        + " " * inner_spaces
        + letter
        + " " * outer_spaces
    )