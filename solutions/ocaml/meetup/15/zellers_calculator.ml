(* Zeller's Congruence constants *)
let january = 1
let february = 2
let march = 3
let months_in_year = 12
let saturday_index = 0
let monday_offset = 5
let days_in_week = 7

let zeller_year ~(date : Date.t) =
  if date.month < march
      then date.year - 1
      else date.year

let zeller_month ~(date : Date.t) =
  if date.month < march
      then date.month + months_in_year
      else date.month

let century_part year =
  year / 100

let year_of_century year =
  year mod 100

let zeller_month_term month =
  (13 * (month + 1)) / 5

let zeller_raw_index ~date =
  let year = zeller_year ~date in
  let century = century_part year in
  let year_part = year_of_century year in

  let month = zeller_month ~date in
  (Date.get_day ~date)
  + (zeller_month_term month)
  + year_part
  + year_part / 4
  + century / 4
  + 5 * century

(* Zeller: 0 = Saturday → normalize to Monday = 0 *)
let normalize_zeller_index weekday_index_raw =
  (weekday_index_raw + monday_offset) mod days_in_week

  (* Zeller's Congruence to calculate day of week, Monday = 0 *)
  let weekday_of_date ~(context : Context.t) =
    let date = context.date in
    let weekday_index_raw = zeller_raw_index ~date in
    normalize_zeller_index weekday_index_raw

