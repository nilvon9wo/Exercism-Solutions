ALPHABET_SIZE = 26
LOWERCASE_A = ord("a")
UPPERCASE_A = ord("A")

def _rotate_character(character, key):
    if character.islower():
        alphabet_start = LOWERCASE_A
    elif character.isupper():
        alphabet_start = UPPERCASE_A
    else:
        return character

    character_position = ord(character) - alphabet_start
    rotated_position = (character_position + key) % ALPHABET_SIZE
    return chr(alphabet_start + rotated_position)


def rotate(text, key):
    rotated_text = ""
    for character in text:
        rotated_text += _rotate_character(character, key)

    return rotated_text