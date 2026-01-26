let is_leap_year year =
  (year mod 4 = 0
    && year mod 100 <> 0)
        || year mod 400 = 0

let days_in_month ~(date: Date.t) =
  match date.month with
  | 1 | 3 | 5 | 7 | 8 | 10 | 12 -> 31
  | 4 | 6 | 9 | 11 -> 30
  | 2 -> if is_leap_year date.year
    then 29
    else 28
  | _ -> invalid_arg "month must be 1..12"