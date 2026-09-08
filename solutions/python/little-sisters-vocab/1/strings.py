"""Functions for helping with vocabulary exercises."""


def add_prefix_un(word):
    """Add the 'un' prefix to a word."""
    return "un" + word


def make_word_groups(vocab_words):
    """Apply the prefix to each word and join the results."""
    prefix = vocab_words[0]
    return " :: ".join([prefix] + [prefix + word for word in vocab_words[1:]])


def remove_suffix_ness(word):
    """Remove the 'ness' suffix and restore a changed 'y'."""
    root = word.removesuffix("ness")

    if root.endswith("i"):
        root = root[:-1] + "y"

    return root


def adjective_to_verb(sentence, index):
    """Extract a word from a sentence and add the 'en' suffix."""
    adjective = (sentence.split()[index]
                 .removesuffix("."))
    return adjective + "en"