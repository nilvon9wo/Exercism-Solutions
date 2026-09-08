def is_isogram(phrase):
    normalized_phrase = phrase.lower()
    letters_seen = set()
    for character in normalized_phrase:
        if character in " -":
            continue

        if character in letters_seen:
            return False

        letters_seen.add(character)

    return True