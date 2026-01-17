type schedule = First | Second | Third | Fourth | Teenth | Last

type weekday =
  | Monday | Tuesday | Wednesday | Thursday | Friday | Saturday | Sunday

type date = int * int * int

let weekday_to_int = function
  | Monday -> 0
  | Tuesday -> 1
  | Wednesday -> 2
  | Thursday -> 3
  | Friday -> 4
  | Saturday -> 5
  | Sunday -> 6

let is_leap_year year =
  (year mod 4 = 0 && year mod 100 <> 0) || year mod 400 = 0

let days_in_month ~year ~month =
  match month with
  | 1 | 3 | 5 | 7 | 8 | 10 | 12 -> 31
  | 4 | 6 | 9 | 11 -> 30
  | 2 -> if is_leap_year year then 29 else 28
  | _ -> invalid_arg "month"

(* Zeller-like weekday calculation, Monday = 0 *)
let day_of_week ~year ~month ~day =
  let y, m =
    if month < 3 then (year - 1, month + 12) else (year, month)
  in
  let k = y mod 100 in
  let j = y / 100 in
  let h =
    (day
     + (13 * (m + 1)) / 5
     + k
     + k / 4
     + j / 4
     + 5 * j) mod 7
  in
  (* Zeller: 0=Saturday → normalize to Monday=0 *)
  (h + 5) mod 7

let matches_weekday wd ~year ~month ~day =
  day_of_week ~year ~month ~day = weekday_to_int wd

let find_nth ~year ~month ~weekday n =
  let rec loop day count =
    if day > days_in_month ~year ~month then failwith "no such day"
    else if matches_weekday weekday ~year ~month ~day then
      if count = n then day else loop (day + 1) (count + 1)
    else loop (day + 1) count
  in
  loop 1 1

let find_teenth ~year ~month ~weekday =
  let rec loop day =
    if matches_weekday weekday ~year ~month ~day then day
    else loop (day + 1)
  in
  loop 13

let find_last ~year ~month ~weekday =
  let rec loop day =
    if matches_weekday weekday ~year ~month ~day then day
    else loop (day - 1)
  in
  loop (days_in_month ~year ~month)

let meetup_day sched weekday ~year ~month =
  let day =
    match sched with
    | First -> find_nth ~year ~month ~weekday 1
    | Second -> find_nth ~year ~month ~weekday 2
    | Third -> find_nth ~year ~month ~weekday 3
    | Fourth -> find_nth ~year ~month ~weekday 4
    | Teenth -> find_teenth ~year ~month ~weekday
    | Last -> find_last ~year ~month ~weekday
  in
  (year, month, day)
