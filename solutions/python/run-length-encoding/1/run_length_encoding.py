def encode(text):
    if not text:
        return ""

    encoded_runs = _get_encoded_runs(text)
    return "".join(encoded_runs)


def _get_encoded_runs(text):
    encoded_runs = []
    current_character = text[0]
    run_length = 1
    for character in text[1:]:
        if character == current_character:
            run_length += 1
            continue

        encoded_runs.append(_encode_run(current_character, run_length))
        current_character = character
        run_length = 1

    encoded_runs.append(_encode_run(current_character, run_length))

    return encoded_runs

def _encode_run(character, run_length):
    if run_length == 1:
        return character

    return f"{run_length}{character}"

def decode(encoded_text):
    decoded_text = ""
    count = ""
    for character in encoded_text:
        if character.isdigit():
            count += character
            continue

        decoded_text += _decode_run(character, count)
        count = ""

    return decoded_text

def _decode_run(character, count):
    if not count:
        return character

    return character * int(count)