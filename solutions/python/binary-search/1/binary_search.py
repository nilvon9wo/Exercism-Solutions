def find(array, value):
    left_index = 0
    right_index = len(array) - 1

    while left_index <= right_index:
        middle_index = (left_index + right_index) // 2
        middle_value = array[middle_index]

        if middle_value == value:
            return middle_index

        if middle_value < value:
            left_index = middle_index + 1
        else:
            right_index = middle_index - 1

    raise ValueError("value not in array")