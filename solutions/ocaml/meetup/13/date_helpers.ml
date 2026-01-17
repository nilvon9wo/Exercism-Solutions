let divisible_by n x = x mod n = 0

let is_leap_year year =
  let divisible_by_4 = divisible_by 4 year in
  let divisible_by_100 = divisible_by 100 year in
  let divisible_by_400 = divisible_by 400 year in
  (divisible_by_4 && not divisible_by_100)
        || divisible_by_400

let long_months = [1; 3; 5; 7; 8; 10; 12]
let short_months = [4; 6; 9; 11]

let days_in_leap_year ~(year: int) =
    if is_leap_year year
        then 29
        else 28

let days_in_month ~(date: Date.t) =
  match date.month with
  | 1 | 3 | 5 | 7 | 8 | 10 | 12 -> 31
  | 4 | 6 | 9 | 11 -> 30
  | 2 -> days_in_leap_year ~year:date.year
  | _ -> invalid_arg "month must be 1..12"