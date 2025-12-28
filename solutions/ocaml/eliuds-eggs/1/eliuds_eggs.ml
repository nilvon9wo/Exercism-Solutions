let is_least_significant_bit_one n =
  n mod 2 = 1

let shift_right n =
  n / 2

let bit_value n = if is_least_significant_bit_one n
    then 1
    else 0

let rec count_ones number accumulator =
  match number with
  | 0 -> accumulator
  | _ ->
    let next_number = shift_right number in
    let updated_count = accumulator + bit_value number in
    count_ones next_number updated_count

let egg_count number =
  count_ones number 0
