open Base

let is_not_dash character =
  Char.(character <> '-')

let strip_dashes isbn =
  isbn
  |> String.to_list
  |> List.filter ~f:is_not_dash

let char_to_digit_value position character =
  match character with
  | 'X' when position = 9 ->
      Some 10
  | '0' .. '9' ->
      Some (Char.to_int character - Char.to_int '0')
  | _ ->
      None

let weight_for_position position digit_value =
    digit_value * (10 - position)

let apply_weight digit_value_function =
  fun position character ->
    character
    |> digit_value_function position
    |> Option.map ~f:(weight_for_position position)

let compute_weighted_digits characters digit_value_function =
  characters
  |> List.mapi ~f:(apply_weight digit_value_function)

let sum_valid_weights weighted_digits =
  weighted_digits
  |> List.filter_map ~f:Fn.id
  |> List.fold ~init:0 ~f:( + )

let is_checksum_divisible_by_11 weighted_digits =
    let total = sum_valid_weights weighted_digits in
    Int.rem total 11 = 0

let is_valid_checksum characters =
  let weighted_digits =
    compute_weighted_digits characters char_to_digit_value
  in

  if List.exists weighted_digits ~f:Option.is_none
  then false
  else is_checksum_divisible_by_11 weighted_digits

let has_exactly_10_characters characters =
  match characters with
  | [_; _; _; _; _; _; _; _; _; _] ->
      is_valid_checksum characters
  | _ ->
      false

let is_valid isbn =
  let characters = strip_dashes isbn in
  has_exactly_10_characters characters
