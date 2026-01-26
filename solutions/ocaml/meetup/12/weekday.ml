type t =
  | Monday
  | Tuesday
  | Wednesday
  | Thursday
  | Friday
  | Saturday
  | Sunday

let weekday_to_index (weekday : t) =
  match weekday with
  | Monday -> 0
  | Tuesday -> 1
  | Wednesday -> 2
  | Thursday -> 3
  | Friday -> 4
  | Saturday -> 5
  | Sunday -> 6