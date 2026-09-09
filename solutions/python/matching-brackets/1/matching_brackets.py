OPENING_BRACKET_BY_CLOSING_BRACKETS = {
    ")": "(",
    "]": "[",
    "}": "{",
}

OPENING_BRACKETS = set(OPENING_BRACKET_BY_CLOSING_BRACKETS.values())
CLOSING_BRACKETS = set(OPENING_BRACKET_BY_CLOSING_BRACKETS.keys())

def is_paired(text):
    opening_brackets = []

    for character in text:
        if character in OPENING_BRACKETS:
            opening_brackets.append(character)
            continue

        if character not in CLOSING_BRACKETS:
            continue

        if not opening_brackets:
            return False

        if opening_brackets.pop() != OPENING_BRACKET_BY_CLOSING_BRACKETS[character]:
            return False

    return not opening_brackets