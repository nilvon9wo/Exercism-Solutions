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
  weekday_of_date ~context = Weekday.weekday_to_index context.weekday

(* Find the nth occurrence of a weekday in a month *)
let find_nth_weekday_in_month ~(context: Context.t) n =
  let rec loop day count =
    if day > Date_helpers.days_in_month ~date:context.date
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
  loop (Date_helpers.days_in_month ~date:context.date)

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