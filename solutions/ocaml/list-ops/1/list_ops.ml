open Base

let length list =
  let rec compute_length remaining_elements accumulated_length =
    match remaining_elements with
    | [] ->
        accumulated_length
    | _ :: tail_elements ->
        let new_length = accumulated_length + 1 in
        compute_length tail_elements new_length
  in
  compute_length list 0

let reverse list =
  let rec build_reversed_list remaining_elements reversed_accumulator =
    match remaining_elements with
    | [] ->
        reversed_accumulator
    | head_element :: tail_elements ->
        let new_accumulator = head_element :: reversed_accumulator in
        build_reversed_list tail_elements new_accumulator
  in
  build_reversed_list list []

let map ~f list =
  let rec build_mapped_list remaining_elements accumulated_mapped_list =
    match remaining_elements with
    | [] ->
        reverse accumulated_mapped_list
    | head_element :: tail_elements ->
        let mapped_element = f head_element in
        let new_accumulator = mapped_element :: accumulated_mapped_list in
        build_mapped_list tail_elements new_accumulator
  in
  build_mapped_list list []

let filter ~f list =
  let rec build_filtered_list remaining_elements accumulated_filtered_list =
    match remaining_elements with
    | [] ->
        reverse accumulated_filtered_list
    | head_element :: tail_elements ->
        if f head_element
          then
            let new_accumulator = head_element :: accumulated_filtered_list in
            build_filtered_list tail_elements new_accumulator
          else build_filtered_list tail_elements accumulated_filtered_list
  in
  build_filtered_list list []

let fold ~init ~f list =
  let rec accumulate_fold remaining_elements accumulated_value =
    match remaining_elements with
    | [] -> accumulated_value
    | head_element :: tail_elements ->
        let updated_accumulated_value = f accumulated_value head_element in
        accumulate_fold tail_elements updated_accumulated_value
  in
  accumulate_fold list init

let append first_list second_list =
  let rec build_appended_list remaining_first_list second_list accumulated_list =
    match remaining_first_list with
    | [] ->
        List.rev accumulated_list @ second_list
    | head_element :: tail_elements ->
        let new_accumulator = head_element :: accumulated_list in
        build_appended_list tail_elements second_list new_accumulator
  in
  build_appended_list first_list second_list []

let prepend_element_to_list accumulated_elements current_element =
    current_element :: accumulated_elements

let concat list_of_lists =
  let rec build_concatenated_list remaining_lists accumulated_list =
    match remaining_lists with
    | [] ->
        reverse accumulated_list
    | head_list :: tail_lists ->
        let new_accumulated_list = List.fold head_list ~init:accumulated_list ~f:prepend_element_to_list in
        build_concatenated_list tail_lists new_accumulated_list
  in
  build_concatenated_list list_of_lists []
