ACTION_BY_SUBJECT = {
    "house that Jack built": "",
    "malt": "lay in",
    "rat": "ate",
    "cat": "killed",
    "dog": "worried",
    "cow with the crumpled horn": "tossed",
    "maiden all forlorn": "milked",
    "man all tattered and torn": "kissed",
    "priest all shaven and shorn": "married",
    "rooster that crowed in the morn": "woke",
    "farmer sowing his corn": "kept",
    "horse and the hound and the horn": "belonged to",
}

def build_verse(subject):
    action = ACTION_BY_SUBJECT[subject]
    if not action:
        return f"the {subject}."

    subjects = list(ACTION_BY_SUBJECT)
    previous_subject = subjects[subjects.index(subject) - 1]
    return (
        f"the {subject} that {action} "
        f"{build_verse(previous_subject)}"
    )

def recite(start_verse, end_verse):
    subjects = list(ACTION_BY_SUBJECT)
    verses = []
    for verse_number in range(start_verse, end_verse + 1):
        subject = subjects[verse_number - 1]
        verses.append(f"This is {build_verse(subject)}")

    return verses