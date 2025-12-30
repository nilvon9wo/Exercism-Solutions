type base = int

let is_invalid_base from target =
    from <= 1
        || target <= 1

let is_valid_digit base digit =
    digit >= 0
        && digit < base

let are_all_digits_valid base digits =
    digits
    |> List.for_all (is_valid_digit base)

let has_invalid_digits base digits
    = not (are_all_digits_valid base digits)

let accumulate_value base
    = fun accumulator digit ->
        accumulator * base + digit

let value_from_digits base digits =
    let accumulate = accumulate_value base in
    Base.List.fold digits ~init:0 ~f:accumulate

let rec convert_to_base target number accumulator =
  if number = 0
  then accumulator
  else
    let next_number = number / target in
    let remainder = number mod target in
    let next_accumulator = remainder :: accumulator in
    convert_to_base target next_number next_accumulator

let normalize_conversion_result converted = match converted with
  | [] -> [0]
  | non_empty -> non_empty

let convert_digits ~from ~digits ~target =
    let value = value_from_digits from digits in
    let converted = convert_to_base target value [] in
    Some (normalize_conversion_result converted)

let convert_bases ~from ~digits ~target =
  if is_invalid_base from target
    then None
  else if has_invalid_digits from digits
    then None
  else
    convert_digits ~from ~digits ~target
