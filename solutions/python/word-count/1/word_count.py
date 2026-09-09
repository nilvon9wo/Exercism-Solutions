from typing import Any, Generator

def count_words(text):
    word_counts = {}
    for word in _get_words(text):
        normalized_word = word.lower()
        word_counts[normalized_word] = (
            word_counts.get(normalized_word, 0) + 1
        )

    return word_counts

def _get_words(text):
    word = yield from _get_word(text)
    if word:
        yield word

def _get_word(text) -> Generator[str, Any, str]:
    word = ""
    for index, character in enumerate(text):
        if character.isalnum():
            word += character
            continue

        if _is_internal_apostrophe(character, index, text, word):
            word += character
            continue

        if word:
            yield word
            word = ""

    return word

def _is_internal_apostrophe(
        character,
        index: int,
        text,
        word: str | Any):
    return (
            character == "'"
            and word
            and index + 1 < len(text)
            and text[index + 1].isalnum()
    )