ALPHABET = "abcdefghijklmnopqrstuvwxyz"

def is_pangram(sentence):
    normalized_sentence = sentence.lower()
    return all(
        letter in normalized_sentence
        for letter in ALPHABET
    )