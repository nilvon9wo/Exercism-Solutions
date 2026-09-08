ALPHABET = "abcdefghijklmnopqrstuvwxyz"

def is_pangram(sentence):
    normalized_sentence = sentence.lower()
    for letter in ALPHABET:
        if letter not in normalized_sentence:
            return False

    return True