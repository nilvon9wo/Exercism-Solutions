WORD_SEPARATORS = " -"

def abbreviate(phrase):
    initials = _get_initials(phrase)
    acronym = ""
    for initial in initials:
        acronym += initial.upper()

    return acronym


def _get_initials(phrase):
    initials = []
    at_word_start = True
    for character in phrase:
        if character in WORD_SEPARATORS:
            at_word_start = True
            continue

        if not character.isalnum():
            continue

        if at_word_start:
            initials.append(character)
            at_word_start = False

    return initials