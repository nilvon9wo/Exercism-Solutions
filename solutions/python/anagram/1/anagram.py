from collections import Counter

def get_letter_counts(word):
    return Counter(word.lower())

def is_anagram(target, candidate):
    if target.lower() == candidate.lower():
        return False

    return get_letter_counts(target) == get_letter_counts(candidate)

def find_anagrams(target, candidates):
    anagrams = []
    for candidate in candidates:
        if is_anagram(target, candidate):
            anagrams.append(candidate)

    return anagrams