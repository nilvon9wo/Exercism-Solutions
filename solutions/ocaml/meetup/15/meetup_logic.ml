let check_day_matches_weekday ~(context : Context.t) =
  let actual_weekday = Zellers_calculator.weekday_of_date ~context in
  let expected_weekday = Weekday.weekday_to_index context.weekday in
  actual_weekday = expected_weekday

let is_within_month ~(date : Date.t) ~day =
  day <= Date_helpers.days_in_month ~date

let is_matching_weekday ~(context : Context.t) ~day =
  let context_with_day = Context.set_date ~context ~day in
  check_day_matches_weekday ~context:context_with_day

let next_occurrence_index ~is_match ~current =
  if is_match
    then current + 1
    else current

(* Helper: advance to the next day and update occurrence index *)
let advance_day ~day ~occurrence_index ~is_match loop_fn =
  let next_day = day + 1 in
  let next_occurrence = next_occurrence_index ~is_match ~current:occurrence_index in
  loop_fn next_day next_occurrence

(* Find the nth occurrence of a weekday in a month *)
let find_nth_weekday_in_month ~(context : Context.t) n =
  let rec loop day occurrence_index =
    if not (is_within_month ~date:context.date ~day)
        then failwith "no such day in month";

    let is_match = is_matching_weekday ~context ~day in
    if is_match && occurrence_index = n
        then day
        else advance_day ~day ~occurrence_index ~is_match loop
  in
  loop 1 1

(* Find the "teenth" day for a weekday *)
let find_teenth_weekday ~(context: Context.t) =
  let rec loop day =
    if check_day_matches_weekday ~context:(Context.set_date ~context ~day)
        then day
        else loop (day + 1)
  in
  loop 13

(* Find the last occurrence of a weekday in a month *)
let find_last_weekday_in_month ~(context: Context.t) =
  let rec loop day =
    if check_day_matches_weekday ~context:(Context.set_date ~context ~day)
        then day
        else loop (day - 1)
  in
  let initial_day = Date_helpers.days_in_month ~date:context.date in
  loop initial_day

let find_day ~(context: Context.t) =
    match context.schedule with
    | First -> find_nth_weekday_in_month ~context 1
    | Second -> find_nth_weekday_in_month ~context 2
    | Third -> find_nth_weekday_in_month ~context 3
    | Fourth -> find_nth_weekday_in_month ~context 4
    | Teenth -> find_teenth_weekday ~context
    | Last -> find_last_weekday_in_month ~context

let compute_date ~(context: Context.t) = {
    context.date with
    day = Some (find_day ~context)
  }
