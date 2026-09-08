def transform(legacy_data):
    transformed_data = {}
    for score, letters in legacy_data.items():
        for letter in letters:
            transformed_data[letter.lower()] = score

    return transformed_data