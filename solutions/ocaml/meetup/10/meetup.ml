type schedule =
    First | Second | Third | Fourth | Teenth | Last
let to_schedule_t (sched : schedule) : Schedule.t =
  match sched with
  | First -> Schedule.First
  | Second -> Schedule.Second
  | Third -> Schedule.Third
  | Fourth -> Schedule.Fourth
  | Teenth -> Schedule.Teenth
  | Last -> Schedule.Last

type weekday =
  | Monday | Tuesday | Wednesday | Thursday | Friday | Saturday | Sunday
let to_weekday_t (weekday : weekday) : Weekday.t =
  match weekday with
  | Monday -> Weekday.Monday
  | Tuesday -> Weekday.Tuesday
  | Wednesday -> Weekday.Wednesday
  | Thursday -> Weekday.Thursday
  | Friday -> Weekday.Friday
  | Saturday -> Weekday.Saturday
  | Sunday -> Weekday.Sunday

type date = int * int * int  (* year, month, day *)
let to_meetup_date (date : Date.t) : date =
    match date.day with
    | Some day -> (date.year, date.month, day)
    | None -> invalid_arg "Date.to_meetup_date: day is None"

(* Convert a weekday to an integer, Monday = 0 *)
let weekday_to_index (weekday : Weekday.t) =
  match weekday with
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
let days_in_month ~(date: Date.t) =
  match date.month with
  | 1 | 3 | 5 | 7 | 8 | 10 | 12 -> 31
  | 4 | 6 | 9 | 11 -> 30
  | 2 -> if is_leap_year date.year
    then 29
    else 28
  | _ -> invalid_arg "month must be 1..12"

(* Zeller's Congruence to calculate day of week, Monday = 0 *)
let weekday_of_date ~(context: Context.t) =
  let date = context.date in
  let year, month =
    if (date.month < 3)
        then (date.year - 1, date.month + 12)
        else (date.year, date.month)
  in
  let century_part = year / 100 in
  let year_part = year mod 100 in
  let weekday_index_raw =
    (Date.get_day ~date
     + (13 * (month + 1)) / 5
     + year_part
     + year_part / 4
     + century_part / 4
     + 5 * century_part) mod 7
  in
  (* Zeller: 0=Saturday → normalize to Monday=0 *)
  (weekday_index_raw + 5) mod 7

(* Check if a specific day matches a weekday *)
let day_matches_weekday ~(context: Context.t) =
  weekday_of_date ~context = weekday_to_index context.weekday

(* Find the nth occurrence of a weekday in a month *)
let find_nth_weekday_in_month ~(context: Context.t) n =
  let rec loop day count =
    if day > days_in_month ~date:context.date
        then failwith "no such day in month"
        else if day_matches_weekday ~context:(Context.set_date ~context ~day)
            then if count = n
                then day
                else loop (day + 1) (count + 1)
    else loop (day + 1) count
  in
  loop 1 1

(* Find the "teenth" day for a weekday *)
let find_teenth_weekday ~(context: Context.t) =
  let rec loop day =
    if day_matches_weekday ~context:(Context.set_date ~context ~day)
        then day
        else loop (day + 1)
  in
  loop 13

(* Find the last occurrence of a weekday in a month *)
let find_last_weekday_in_month ~(context: Context.t) =
  let rec loop day =
    if day_matches_weekday ~context:(Context.set_date ~context ~day)
        then day
        else loop (day - 1)
  in
  loop (days_in_month ~date:context.date)

let compute_date ~(context: Context.t) =
  let day =
    match context.schedule with
    | First -> find_nth_weekday_in_month ~context 1
    | Second -> find_nth_weekday_in_month ~context 2
    | Third -> find_nth_weekday_in_month ~context 3
    | Fourth -> find_nth_weekday_in_month ~context 4
    | Teenth -> find_teenth_weekday ~context
    | Last -> find_last_weekday_in_month ~context
  in
  let date = context.date in
  Date.make_full ~year:date.year ~month:date.month ~day

(* Main function: compute the meetup day *)
let meetup_day input_schedule input_weekday ~year ~month =
  let result = compute_date ~context:{
                        schedule = to_schedule_t input_schedule;
                        weekday = to_weekday_t input_weekday;
                        date = Date.make_partial ~year ~month;
                      } in
  to_meetup_date result
