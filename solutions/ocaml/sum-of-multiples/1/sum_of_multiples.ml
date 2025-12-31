open Base

let is_non_zero_factor =
  (<>) 0

let remove_zero_factors factors =
  factors
  |> List.filter ~f:is_non_zero_factor

let rec greatest_common_divisor left right =
  if right = 0
      then left
      else greatest_common_divisor right (left % right)

let least_common_multiple left right =
    let greatest_common_divisor_of_operands = greatest_common_divisor left right in
    left / greatest_common_divisor_of_operands * right

let sum_of_multiples_of_factor upper_bound factor =
  let max_inclusive = upper_bound - 1 in
  let number_of_multiples = max_inclusive / factor in
  let successor_count = number_of_multiples + 1 in
  factor * number_of_multiples * successor_count / 2

let prepend_to_subset head element_subset =
  head :: element_subset

let build_subsets_with_head non_empty_subsets head tail =
  let subsets_of_tail = non_empty_subsets tail in
  [ [ head ] ]
  @ subsets_of_tail
  @ List.map subsets_of_tail ~f:(prepend_to_subset head)

let rec non_empty_subsets = function
  | [] ->
      []
  | head :: tail ->
      build_subsets_with_head non_empty_subsets head tail

let sum_for_factor_subset upper_bound factor_subset =
  factor_subset
  |> List.reduce_exn ~f:least_common_multiple
  |> sum_of_multiples_of_factor upper_bound

let has_odd_cardinality factor_subset =
    let subset_cardinality_parity = List.length factor_subset % 2 in
    subset_cardinality_parity = 1

let inclusion_exclusion_accumulator upper_bound =
  fun accumulated_sum factor_subset ->
    let subset_contribution = sum_for_factor_subset upper_bound factor_subset in
    let is_odd_cardinality = has_odd_cardinality factor_subset in

    if is_odd_cardinality
    then accumulated_sum + subset_contribution
    else accumulated_sum - subset_contribution

let sum factors limit =
  factors
  |> remove_zero_factors
  |> non_empty_subsets
  |> List.fold ~init:0 ~f:(inclusion_exclusion_accumulator limit)
