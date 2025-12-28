let divisible_by n year =
  year mod n = 0

let leap_year year =
  let is_divisible_by_4 = divisible_by 4 year in
  let is_century_year    = divisible_by 100 year in
  let is_divisible_by_400 = divisible_by 400 year in

  (is_divisible_by_4 && not is_century_year)
    || is_divisible_by_400
