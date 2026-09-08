from typing import Any

def response(message):
    if _is_silence(message):
        return "Fine. Be that way!"

    question = _is_question(message)
    yelling = _is_yelling(message)
    if yelling and question:
        return "Calm down, I know what I'm doing!"

    if yelling:
        return "Whoa, chill out!"

    if question:
        return "Sure."

    return "Whatever."

def _is_silence(message):
    return not message.strip()

def _is_question(message):
    return message.rstrip().endswith("?")

def _is_yelling(message):
    letters = [character for character in message if character.isalpha()]
    return bool(letters) and _are_all_letters_uppercase(letters)

def _are_all_letters_uppercase(letters: list[Any]) -> bool:
    return all(character.isupper() for character in letters)

