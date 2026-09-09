def proverb(*pieces, qualifier=None):
    if not pieces:
        return []

    verses = []
    for index in range(len(pieces) - 1):
        verses.append(_build_consequence(pieces[index], pieces[index + 1]))

    verses.append(_build_final_verse(pieces[0], qualifier))
    return verses

def _build_consequence(lost_item, next_item):
    return f"For want of a {lost_item} the {next_item} was lost."

def _build_final_verse(first_item, qualifier):
    if qualifier:
        first_item = f"{qualifier} {first_item}"

    return f"And all for the want of a {first_item}."