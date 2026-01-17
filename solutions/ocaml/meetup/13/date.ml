type t = {
  year : int;
  month : int;
  day : int option;
}

let make_partial ~year ~month : t = {
    year;
    month;
    day = None
}

let make_full ~year ~month ~day : t = {
    year;
    month;
    day = Some day
}

let get_day ~(date: t) : int =
    match date.day with
    | Some(x) -> x
    | None -> invalid_arg "Day not assigned."
