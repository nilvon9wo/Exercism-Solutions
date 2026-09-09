ALPHABET = "abcdefghijklmnopqrstuvwxyz"
CIPHER_ALPHABET = ALPHABET[::-1]
GROUP_SIZE = 5

def encode(plain_text):
    encoded_text = _encode_text(plain_text)
    return _group_text(encoded_text)

def decode(cipher_text):
    return _encode_text(cipher_text)

def _encode_text(plain_text):
    encoded_text = ""
    for character in plain_text:
        encoded_text += _encode_character(character)

    return encoded_text

def _encode_character(character):
    if character.isalpha():
        character_index = ord(character.lower()) - ord("a")
        return CIPHER_ALPHABET[character_index]

    if character.isdigit():
        return character

    return ""

def _group_text(text):
    grouped_text = []
    for start_index in range(0, len(text), GROUP_SIZE):
        grouped_text.append(text[start_index:start_index + GROUP_SIZE])

    return " ".join(grouped_text)