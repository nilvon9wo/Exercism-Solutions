VOWELS = "aeiou"
AY_ENDING = "ay"

def is_vowel(character, index):
    return character in VOWELS or (character == "y" and index > 0)

def get_translation_start(word):
    if _starts_with_vowel(word) or _starts_with_special_vowel_sound(word):
        return 0

    prefix_length = _get_consonant_prefix_length(word)
    if word[prefix_length - 1:prefix_length + 1] == "qu":
        return prefix_length  + 1

    return prefix_length

def _starts_with_vowel(word):
    return word[0] in VOWELS

def _starts_with_special_vowel_sound(word):
    return word.startswith(("xr", "yt"))

def _get_consonant_prefix_length(word):
    index = 0
    while (
            index < len(word)
           and not is_vowel(word[index], index)
    ):
        index += 1

    return index

def translate_word(word):
    translation_start = get_translation_start(word)

    if translation_start == 0:
        return word + AY_ENDING

    beginning = word[:translation_start]
    remainder = word[translation_start:]
    return remainder + beginning + AY_ENDING

def translate(text):
    translated_words = []
    for word in text.split():
        translated_words.append(translate_word(word))

    return " ".join(translated_words)