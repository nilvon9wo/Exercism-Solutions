GIFT_BY_DAYS = {
    "first": "a Partridge in a Pear Tree",
    "second": "two Turtle Doves",
    "third": "three French Hens",
    "fourth": "four Calling Birds",
    "fifth": "five Gold Rings",
    "sixth": "six Geese-a-Laying",
    "seventh": "seven Swans-a-Swimming",
    "eighth": "eight Maids-a-Milking",
    "ninth": "nine Ladies Dancing",
    "tenth": "ten Lords-a-Leaping",
    "eleventh": "eleven Pipers Piping",
    "twelfth": "twelve Drummers Drumming",
}

DAY_NAMES = tuple(GIFT_BY_DAYS)

def recite(start_verse, end_verse):
    verses = []
    for verse_number in range(start_verse, end_verse + 1):
        verses.append(_build_verse(verse_number))

    return verses

def _build_verse(verse_number):
    day = DAY_NAMES[verse_number - 1]
    gifts = _get_gifts(verse_number)
    gift_text = _format_gifts(gifts)

    return (
        f"On the {day} day of Christmas my true love gave to me: "
        f"{gift_text}."
    )

def _get_gifts(verse_number):
    gifts = []
    for day in reversed(DAY_NAMES[:verse_number]):
        gifts.append(GIFT_BY_DAYS[day])

    return gifts

def _format_gifts(gifts):
    if len(gifts) == 1:
        return gifts[0]

    return ", ".join(gifts[:-1]) + ", and " + gifts[-1]