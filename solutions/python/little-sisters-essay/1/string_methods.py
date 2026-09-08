"""Functions for editing Little Sister's essay."""


def capitalize_title(title):
    """Capitalize the first letter of each word."""
    return title.title()


def check_sentence_ending(sentence):
    """Return whether the sentence ends with a period."""
    return sentence.endswith(".")


def clean_up_spacing(sentence):
    """Remove whitespace from the beginning and end of the sentence."""
    return sentence.strip()


def replace_word_choice(sentence, old_word, new_word):
    """Replace all occurrences of a word with its synonym."""
    return sentence.replace(old_word, new_word)