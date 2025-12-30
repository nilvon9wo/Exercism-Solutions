open Base

(* ---- Character-level helpers ---- *)

let is_digit_or_space character =
  Char.is_digit character
    || Char.equal character ' '

let is_invalid_character character =
  not (is_digit_or_space character)

let has_invalid_characters input =
  String.exists input ~f:is_invalid_character

(* ---- Digit extraction ---- *)

let extract_digits_from_string input =
  input
  |> String.to_list
  |> List.filter ~f:Char.is_digit

(* ---- Luhn transformations ---- *)

let double_digit_and_adjust digit =
  let adjusted = digit * 2 in
  if adjusted > 9
    then adjusted - 9
    else adjusted

let transform_digit_by_index index character =
  let digit = Char.to_int character - Char.to_int '0' in
  let is_odd_index = Int.rem index 2 = 1 in
  if is_odd_index
    then double_digit_and_adjust digit
    else digit

let sum_transformed_digits digits_reversed =
  digits_reversed
  |> List.mapi ~f:transform_digit_by_index
  |> List.sum (module Int) ~f:Fn.id

let is_luhn_total_divisible_by_10 digits =
  let digits_reversed = List.rev digits in
  let total_sum = sum_transformed_digits digits_reversed in
  let remainder = Int.rem total_sum 10 in
  remainder = 0

(* ---- Validation rules ---- *)

let has_minimum_required_digits digits =
  match digits with
  | [] | [_] ->
    false
  | _ ->
    is_luhn_total_divisible_by_10 digits

(* ---- Main entry point ---- *)

let check_digits_and_validate input =
  let digits = extract_digits_from_string input in
  has_minimum_required_digits digits

let valid input =
  if has_invalid_characters input
  then false
  else check_digits_and_validate input
