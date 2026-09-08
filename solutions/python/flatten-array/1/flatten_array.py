def flatten(values):
    flattened_values = []

    for value in values:
        if value is None:
            continue

        if isinstance(value, list):
            flattened_values.extend(flatten(value))
            continue

        flattened_values.append(value)

    return flattened_values