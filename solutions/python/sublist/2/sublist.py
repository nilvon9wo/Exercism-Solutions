SUBLIST = "sublist"
SUPERLIST = "superlist"
EQUAL = "equal"
UNEQUAL = "unequal"

def is_sublist(smaller_list, larger_list):
    if not smaller_list:
        return True

    if len(smaller_list) > len(larger_list):
        return False

    return _contains_sublist(smaller_list, larger_list)

def _contains_sublist(smaller_list, larger_list):
    last_start_index = len(larger_list) - len(smaller_list)
    return any(
        _contains_at_index(smaller_list, larger_list, start_index)
        for start_index in range(last_start_index + 1)
    )

def _contains_at_index(smaller_list, larger_list, start_index):
    end_index = start_index + len(smaller_list)
    return larger_list[start_index:end_index] == smaller_list

def sublist(list_one, list_two):
    if list_one == list_two:
        return EQUAL

    if is_sublist(list_one, list_two):
        return SUBLIST

    if is_sublist(list_two, list_one):
        return SUPERLIST

    return UNEQUAL