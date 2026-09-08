def append(list_one, list_two):
    result = []
    for element in list_one:
        result.append(element)

    for element in list_two:
        result.append(element)

    return result

def concat(lists):
    result = []
    for current_list in lists:
        for element in current_list:
            result.append(element)

    return result


# noinspection shadowing-builtins
# pylint: disable=redefined-builtin
def filter(function, list_):
    result = []
    for element in list_:
        if function(element):
            result.append(element)

    return result

def length(list_):
    count = 0
    # pylint: disable=disallowed-name
    for _ in list_:
        count += 1

    return count

# noinspection shadowing-builtins
# pylint: disable=redefined-builtin
def map(function, list_):
    result = []
    for element in list_:
        result.append(function(element))

    return result

def foldl(function, list_, initial):
    accumulator = initial

    for element in list_:
        accumulator = function(accumulator, element)

    return accumulator

def foldr(function, list_, initial):
    accumulator = initial
    for index in range(length(list_) - 1, -1, -1):
        accumulator = function(accumulator, list_[index])

    return accumulator

def reverse(list_):
    result = []
    for index in range(length(list_) - 1, -1, -1):
        result.append(list_[index])

    return result