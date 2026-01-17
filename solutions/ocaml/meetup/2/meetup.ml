type schedule = First | Second | Third | Fourth | Teenth | Last

type weekday =
  | Monday | Tuesday | Wednesday | Thursday | Friday | Saturday | Sunday

type date = int * int * int  (* year, month, day *)

(* Convert a weekday to an integer, Monday = 0 *)
let weekday_to_index = function
  | Monday -> 0
  | Tuesday -> 1
  | Wednesday -> 2
  | Thursday -> 3
  | Friday -> 4
  | Saturday -> 5
  | Sunday -> 6

(* Determine if a given year is a leap year *)
let is_leap_year year =
  (year mod 4 = 0 && year mod 100 <> 0) || year mod 400 = 0

(* Return the number of days in a given month and year *)
let days_in_month ~year ~month =
  match month with
  | 1 | 3 | 5 | 7 | 8 | 10 | 12 -> 31
  | 4 | 6 | 9 | 11 -> 30
  | 2 -> if is_leap_year year then 29 else 28
  | _ -> invalid_arg "month must be 1..12"

(* Zeller's Congruence to calculate day of week, Monday = 0 *)
let weekday_of_date ~year ~month ~day =
  let y, m =
    if month < 3 then (year - 1, month + 12) else (year, month)
  in
  let century_part = y / 100 in
  let year_part = y mod 100 in
  let weekday_index_raw =
    (day
     + (13 * (m + 1)) / 5
     + year_part
     + year_part / 4
     + century_part / 4
     + 5 * century_part) mod 7
  in
  (* Zeller: 0=Saturday → normalize to Monday=0 *)
  (weekday_index_raw + 5) mod 7

(* Check if a specific day matches a weekday *)
let day_matches_weekday weekday ~year ~month ~day =
  weekday_of_date ~year ~month ~day = weekday_to_index weekday

(* Find the nth occurrence of a weekday in a month *)
let find_nth_weekday_in_month ~year ~month ~weekday n =
  let rec loop day count =
    if day > days_in_month ~year ~month then failwith "no such day in month"
    else if day_matches_weekday weekday ~year ~month ~day then
      if count = n then day else loop (day + 1) (count + 1)
    else loop (day + 1) count
  in
  loop 1 1

(* Find the "teenth" day for a weekday *)
let find_teenth_weekday ~year ~month ~weekday =
  let rec loop day =
    if day_matches_weekday weekday ~year ~month ~day then day
    else loop (day + 1)
  in
  loop 13

(* Find the last occurrence of a weekday in a month *)
let find_last_weekday_in_month ~year ~month ~weekday =
  let rec loop day =
    if day_matches_weekday weekday ~year ~month ~day then day
    else loop (day - 1)
  in
  loop (days_in_month ~year ~month)

(* Main function: compute the meetup day *)
let meetup_day schedule weekday ~year ~month =
  let day =
    match schedule with
    | First -> find_nth_weekday_in_month ~year ~month ~weekday 1
    | Second -> find_nth_weekday_in_month ~year ~month ~weekday 2
    | Third -> find_nth_weekday_in_month ~year ~month ~weekday 3
    | Fourth -> find_nth_weekday_in_month ~year ~month ~weekday 4
    | Teenth -> find_teenth_weekday ~year ~month ~weekday
    | Last -> find_last_weekday_in_month ~year ~month ~weekday
  in
  (year, month, day)
