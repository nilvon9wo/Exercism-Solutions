def slices(series, length):
    _validate_series(series, length)
    return _get_slices(series, length)

def _validate_series(series, length):
    if not series:
        raise ValueError("series cannot be empty")

    if length < 0:
        raise ValueError("slice length cannot be negative")

    if length == 0:
        raise ValueError("slice length cannot be zero")

    if length > len(series):
        raise ValueError("slice length cannot be greater than series length")

def _get_slices(series, length):
    series_slices = []
    number_of_slices = len(series) - length + 1
    for start_index in range(number_of_slices):
        end_index = start_index + length
        series_slices.append(series[start_index:end_index])

    return series_slices