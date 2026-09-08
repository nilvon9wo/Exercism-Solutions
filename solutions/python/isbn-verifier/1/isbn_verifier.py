ISBN_LENGTH = 10
CHECKSUM_MODULUS = 11
X_VALUE = 10
CHECK_CHARACTER = "X"
DASH = "-"

def _remove_dashes(isbn):
    return isbn.replace(DASH, "")

def _has_valid_characters(isbn):
    for character in isbn[:-1]:
        if not character.isdigit():
            return False

    return (
            isbn[-1].isdigit()
            or isbn[-1] == CHECK_CHARACTER
    )

def _get_check_value(check_character):
    if check_character == CHECK_CHARACTER:
        return X_VALUE

    return int(check_character)

def _calculate_checksum(isbn):
    checksum = 0
    for index, character in enumerate(isbn):
        weight = ISBN_LENGTH - index
        value = _get_check_value(character)
        checksum += value * weight

    return checksum

def is_valid(isbn):
    normalized_isbn = _remove_dashes(isbn)
    if len(normalized_isbn) != ISBN_LENGTH:
        return False

    if not _has_valid_characters(normalized_isbn):
        return False

    checksum = _calculate_checksum(normalized_isbn)
    return checksum % CHECKSUM_MODULUS == 0