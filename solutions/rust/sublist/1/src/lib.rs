#[derive(Debug, PartialEq)]
pub enum Comparison {
    Equal,
    Sublist,
    Superlist,
    Unequal,
}

pub fn sublist<T: PartialEq>(first_list: &[T], second_list: &[T]) -> Comparison  {
    let is_every_member_of_1st_list_in_2nd_list = is_containing(&first_list, &second_list);
    let is_every_member_of_2nd_list_in_1st_list = is_containing(&second_list, &first_list);

    if is_every_member_of_1st_list_in_2nd_list && is_every_member_of_2nd_list_in_1st_list {
        Comparison::Equal
    }
    else if is_every_member_of_1st_list_in_2nd_list && !is_every_member_of_2nd_list_in_1st_list {
        Comparison::Sublist
    }
    else if !is_every_member_of_1st_list_in_2nd_list && is_every_member_of_2nd_list_in_1st_list {
        Comparison::Superlist
    }
    else {
        Comparison::Unequal
    }
}

fn is_containing<T: PartialEq>(potential_containee: &[T], potential_container: &[T]) -> bool {
    match potential_containee.get(0) {
        Some(first_value) => {
            let container_indexes_of_first_value
                    = find_indexes(potential_container, &first_value);

            if container_indexes_of_first_value.is_empty() {
                false
            }
            else {
                second_contains_match_for_first_at_index(potential_containee, potential_container, container_indexes_of_first_value)
            }
        },

        None =>
            true
    }
}

fn second_contains_match_for_first_at_index<T: PartialEq>(potential_containee: &[T], potential_container: &[T], container_indexes_of_first_value: Vec<usize>) -> bool {
    for container_index in container_indexes_of_first_value {
        let mut contains_all = true;
        for (containee_index, containee_value) in potential_containee.iter().enumerate() {
            match potential_container.get(container_index + containee_index) {
                Some(container_value) => {
                    if containee_value != container_value {
                        contains_all = false;
                        break;
                    }
                },
                None => {
                    contains_all = false;
                    break;
                }
            }
        }

        if contains_all == true {
            return true;
        }
    }
    false
}

fn find_indexes<T: PartialEq>(haystack: &[T], needle: &T) -> Vec<usize> {
    let mut indexes: Vec<usize> = Vec::new();
    for (index, value) in haystack.iter().enumerate() {
        if value == needle {
            indexes.push(index)
        }
    }
    return indexes;
}