let divisible_by n x = x mod n = 0

let is_leap_year year =
  let divisible_by_4 = divisible_by 4 year in
  let divisible_by_100 = divisible_by 100 year in
  let divisible_by_400 = divisible_by 400 year in
  (divisible_by_4 && not divisible_by_100)
        || divisible_by_400

let long_months = [1; 3; 5; 7; 8; 10; 12]
let short_months = [4; 6; 9; 11]

let days_in_month ~(date: Date.t) =
  if List.mem date.month long_months
        then 31
        else if List.mem date.month short_months
            then 30
            else if date.month = 2
                then if is_leap_year date.year
                    then 29
                    else 28
                else invalid_arg "month must be 1..12"